**
 * com.mckoi.database.DataCellCache  21 Mar 1998
 *
 * Mckoi SQL Database ( http://www.mckoi.com/database )
 * Copyright (C) 2000-2018 Diehl and Associates, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mckoi.database;

import com.mckoi.util.Cache;
import com.mckoi.debug.*;
import java.util.HashMap;

/**
 * This object represents a cache for accesses to the the data cells within
 * a Table.  Whenever a column/row index to a cell is accessed, the cache is
 * first checked.  If the cell is not in the cache then it may go ahead and
 * read the cell from the file.
 * <p>
 * ISSUE: We may need to keep track of memory used.  Since a String may
 * use up much memory, we may need a cap on the maximum size the cache can grow
 * to.  For example, we wouldn't want to cache a large document.  This could
 * be handled at a higher level?
 *
 * @author Tobias Downer
 */

final class DataCellCache {

  /**
   * The TransactionSystem that this cache is from.
   */
  private final TransactionSystem system;

  /**
   * The maximum size of a DataCell that is allowed to go in the cache.
   */
  private int MAX_CELL_SIZE;

  /**
   * The master cache.
   */
  private final DCCache cache;

  /**
   * The current size of the cache.
   */
  private long current_cache_size;

  /**
   * The Constructors.
   *
   * @param max_cache_size the maximum size in bytes that the cache is allowed
   *   to grow to (eg. 4000000).
   * @param max_cell_size the maximum size of an object that can be stored in
   *   the cache.
   * @param hash_size the number of elements in the hash (should be a prime
   *   number).
   */
  DataCellCache(TransactionSystem system,
                int max_cache_size, int max_cell_size, int hash_size) {
    this.system = system;
    MAX_CELL_SIZE = max_cell_size;

    cache = new DCCache(hash_size, max_cache_size);
  }

  DataCellCache(TransactionSystem system,
                int max_cache_size, int max_cell_size) {
    this(system,
         max_cache_size, max_cell_size, 88547);  // Good prime number hash size
  }

  /**
   * Dynamically resizes the data cell cache so it can store more/less data.
   * This is used to change cache dynamics at runtime.
   */
  public synchronized void alterCacheDynamics(
                                      int max_cache_size, int max_cell_size) {
    MAX_CELL_SIZE = max_cell_size;
    cache.setCacheSize(max_cache_size);
  }

  /**
   * Inner class that creates an object that hashes nicely over the cache
   * source.
   */
  private final static class DCCacheKey {
    final int row;
    final short column;
    final int table_id;
    DCCacheKey(final int table_id, final short column, final int row) {
      this.table_id = table_id;
      this.column = column;
      this.row = row;
    }
    public boolean equals(Object ob) {
      DCCacheKey dest_key = (DCCacheKey) ob;
      return row == dest_key.row &&
             column == dest_key.column &&
             table_id == dest_key.table_id;
    }
    public int hashCode() {
      // Yicks - this one is the best by far!
      return (((int) column + table_id + (row * 189977)) * 50021) << 4;
    }
  }

  /**
   * Returns an approximation of the amount of memory taken by a TObject.
   */
  private static final int amountMemory(TObject cell) {
    return 16 + cell.approximateMemoryUse();
  }

  /**
   * Puts a TObject on the cache for the given row/column of the table.
   * Ignores any cells that are larger than the maximum size.
   */
  public synchronized void put(int table_key, int row, int column,
                               TObject cell) {
    int memory_use = amountMemory(cell);
    if (memory_use <= MAX_CELL_SIZE) {
      // Generate the key
      DCCacheKey key = new DCCacheKey(table_key, (short) column, row);
      // If there is an existing object here, remove it from the cache and
      // update the current_cache_size.
      TObject removed_cell = (TObject) cache.remove(key);
      if (removed_cell != null) {
        current_cache_size -= amountMemory(removed_cell);
      }
      // Put the new entry in the cache
      cache.put(key, cell);
      current_cache_size += memory_use;
    }
    else {
      // If the object is larger than the minimum object size that can be
      // cached, remove any existing entry (possibly smaller) from the cache.
      remove(table_key, row, column);
    }
  }

  /**
   * Gets a TObject from the cache.  If the row/column is not in the cache
   * then it returns null.
   */
  public synchronized TObject get(int table_key, int row, int column) {
    return (TObject) cache.get(new DCCacheKey(table_key, (short) column, row));
  }

  /**
   * Removes a TObject from the cache.  This is used when we need to notify
   * the cache that an object has become outdated.  This should be used when
   * the cell has been removed or changed.
   * Returns the cell that was removed, or null if there was no cell at the
   * given location.
   */
  public synchronized TObject remove(int table_key, int row, int column) {
    TObject cell = (TObject) cache.remove(
                               new DCCacheKey(table_key, (short) column, row));

    if (cell != null) {
      current_cache_size -= amountMemory(cell);
    }
    return cell;
  }

  /**
   * Completely wipe the cache of all entries.
   */
  public synchronized void wipe() {
    if (cache.nodeCount() == 0 && current_cache_size != 0) {
      system.Debug().write(Lvl.ERROR, this,
          "Assertion failed - if nodeCount = 0 then current_cache_size " +
          "must also be 0.");
    }
    if (cache.nodeCount() != 0) {
      cache.removeAll();
      system.stats().increment("DataCellCache.total_cache_wipe");
    }
    current_cache_size = 0;
  }

  /**
   * Returns an estimation of the current cache size in bytes.
   */
  public synchronized long getCurrentCacheSize() {
    return current_cache_size;
  }

  /**
   * Reduce the cache size by the given amount.
   */
  private void reduceCacheSize(long val) {
    current_cache_size -= val;
  }

  // ---------- Primes ----------

  /**
   * Returns a prime number from PRIME_LIST that is the closest prime greater
   * or equal to the given value.
   */
  static int closestPrime(int value) {
    for (int i = 0; i < PRIME_LIST.length; ++i) {
      if (PRIME_LIST[i] >= value) {
        return PRIME_LIST[i];
      }
    }
    // Return the last prime
    return PRIME_LIST[PRIME_LIST.length - 1];
  }

  /**
   * A list of primes ordered from lowest to highest.
   */
  private final static int[] PRIME_LIST = new int[] {
     3001, 4799, 13999, 15377, 21803, 24247, 35083, 40531, 43669, 44263, 47387,
     50377, 57059, 57773, 59399, 59999, 75913, 96821, 140551, 149011, 175633,
     176389, 183299, 205507, 209771, 223099, 240259, 258551, 263909, 270761,
     274679, 286129, 290531, 296269, 298021, 300961, 306407, 327493, 338851,
     351037, 365489, 366811, 376769, 385069, 410623, 430709, 433729, 434509,
     441913, 458531, 464351, 470531, 475207, 479629, 501703, 510709, 516017,
     522211, 528527, 536311, 539723, 557567, 593587, 596209, 597451, 608897,
     611069, 642547, 670511, 677827, 679051, 688477, 696743, 717683, 745931,
     757109, 760813, 763957, 766261, 781559, 785597, 788353, 804493, 813559,
     836917, 854257, 859973, 883217, 884789, 891493, 902281, 910199, 915199,
     930847, 939749, 940483, 958609, 963847, 974887, 983849, 984299, 996211,
     999217, 1007519, 1013329, 1014287, 1032959, 1035829, 1043593, 1046459,
     1076171, 1078109, 1081027, 1090303, 1095613, 1098847, 1114037, 1124429,
     1125017, 1130191, 1159393, 1170311, 1180631, 1198609, 1200809, 1212943,
     1213087, 1226581, 1232851, 1287109, 1289867, 1297123, 1304987, 1318661,
     1331107, 1343161, 1345471, 1377793, 1385117, 1394681, 1410803, 1411987,
     1445261, 1460497, 1463981, 1464391, 1481173, 1488943, 1491547, 1492807,
     1528993, 1539961, 1545001, 1548247, 1549843, 1551001, 1553023, 1571417,
     1579099, 1600259, 1606153, 1606541, 1639751, 1649587, 1657661, 1662653,
     1667051, 1675273, 1678837, 1715537, 1718489, 1726343, 1746281, 1749107,
     1775489, 1781881, 1800157, 1806859, 1809149, 1826753, 1834607, 1846561,
     1849241, 1851991, 1855033, 1879931, 1891133, 1893737, 1899137, 1909513,
     1916599, 1917749, 1918549, 1919347, 1925557, 1946489, 1961551, 1965389,
     2011073, 2033077, 2039761, 2054047, 2060171, 2082503, 2084107, 2095099,
     2096011, 2112193, 2125601, 2144977, 2150831, 2157401, 2170141, 2221829,
     2233019, 2269027, 2270771, 2292449, 2299397, 2303867, 2309891, 2312407,
     2344301, 2348573, 2377007, 2385113, 2386661, 2390051, 2395763, 2422999,
     2448367, 2500529, 2508203, 2509841, 2513677, 2516197, 2518151, 2518177,
     2542091, 2547469, 2549951, 2556991, 2563601, 2575543, 2597629, 2599577,
     2612249, 2620003, 2626363, 2626781, 2636773, 2661557, 2674297, 2691571,
     2718269, 2725691, 2729381, 2772199, 2774953, 2791363, 2792939, 2804293,
     2843021, 2844911, 2851313, 2863519, 2880797, 2891821, 2897731, 2904887,
     2910251, 2928943, 2958341, 2975389
  };

  // ---------- Inner classes ----------

  /**
   * This extends the 'Cache' class.
   */
  private final class DCCache extends Cache {

    /**
     * The maximum size that the cache can grow to in bytes.
     */
    private int MAX_CACHE_SIZE;

    /**
     * The Constructor.
     */
    public DCCache(int cache_hash_size, int max_cache_size) {
      super(cache_hash_size, -1, 20);
      this.MAX_CACHE_SIZE = max_cache_size;
    }

    /**
     * Used to dynamically alter the size of the cache.  May cause a cache
     * clean if the size is over the limit.
     */
    public void setCacheSize(int cache_size) {
      this.MAX_CACHE_SIZE = cache_size;
      checkClean();
    }

    // ----- Overwritten from Cache -----

    protected void checkClean() {

      if (getCurrentCacheSize() >= MAX_CACHE_SIZE) {

        // Update the current cache size (before we wiped).
        system.stats().set((int) getCurrentCacheSize(),
                             "DataCellCache.current_cache_size");
        clean();

        // The number of times we've cleared away old data cell nodes.
        system.stats().increment("DataCellCache.cache_clean");

      }
    }

    protected boolean shouldWipeMoreNodes() {
      return (getCurrentCacheSize() >= (int) ((MAX_CACHE_SIZE * 100L) / 115L));
    }

    protected void notifyWipingNode(Object ob) {
      super.notifyWipingNode(ob);

      // Update our memory indicator accordingly.
      TObject cell = (TObject) ob;
      reduceCacheSize(amountMemory(cell));
      
    }

    protected void notifyGetWalks(long total_walks, long total_get_ops) {
      int avg = (int) ((total_walks * 1000000L) / total_get_ops);
      system.stats().set(avg, "DataCellCache.avg_hash_get_mul_1000000");
      system.stats().set((int) getCurrentCacheSize(),
                                      "DataCellCache.current_cache_size");
      system.stats().set(nodeCount(), "DataCellCache.current_node_count");
    }

  }

}
