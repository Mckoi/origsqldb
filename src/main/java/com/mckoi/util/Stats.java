**
 * com.mckoi.util.Stats  04 Jul 2000
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

package com.mckoi.util;

import java.util.*;
import java.io.PrintStream;

/**
 * An object that is used to store and update various stats.
 * <p>
 * NOTE: This object is thread safe.
 *
 * @author Tobias Downer
 */

public final class Stats {

  /**
   * Where the stat properties are held.
   */
  private HashMap properties;

  /**
   * Constructs the object.
   */
  public Stats() {
    // We need lookup on this hash to be really quick, so load factor is
    // low and initial capacity is high.
    properties = new HashMap(250, 0.50f);
  }

  /**
   * Resets all stats that start with "{session}" to 0.  This should be
   * called when we are collecting stats over a given session and a session
   * has finished.
   */
  public synchronized void resetSession() {
    Set key_set = properties.keySet();
    String[] keys = new String[key_set.size()];
    int index = 0;
    Iterator it = key_set.iterator();
    while (it.hasNext()) {
      keys[index] = (String) it.next();
      ++index;
    }

    // If key starts with a "{session}" then reset it to 0.
    for (int i = 0; i < keys.length; ++i) {
      if (keys[i].startsWith("{session}")) {
        IntegerStat stat = (IntegerStat) properties.get(keys[i]);
        stat.value = 0;
      }
    }
  }

  /**
   * Adds the given value to a stat property.
   */
  public synchronized void add(int value, String stat_name) {
    IntegerStat stat = (IntegerStat) properties.get(stat_name);
    if (stat != null) {
      stat.value += value;
    }
    else {
      stat = new IntegerStat();
      stat.value = value;
      properties.put(stat_name, stat);
    }
  }

  /**
   * Increments a stat property.  eg.
   * stats.increment("File Hits");
   */
  public synchronized void increment(String stat_name) {
    IntegerStat stat = (IntegerStat) properties.get(stat_name);
    if (stat != null) {
      ++stat.value;
    }
    else {
      stat = new IntegerStat();
      stat.value = 1;
      properties.put(stat_name, stat);
    }
  }

  /**
   * Decrements a stat property.
   */
  public synchronized void decrement(String stat_name) {
    IntegerStat stat = (IntegerStat) properties.get(stat_name);
    if (stat != null) {
      --stat.value;
    }
    else {
      stat = new IntegerStat();
      stat.value = -1;
      properties.put(stat_name, stat);
    }
  }

  /**
   * Retrieves the current Object value of a stat property.  Returns null if
   * the stat wasn't found.
   */
  public synchronized Object get(String stat_name) {
    IntegerStat stat = (IntegerStat) properties.get(stat_name);
    if (stat != null) {
      return new Long(stat.value);
    }
    return null;
  }

  /**
   * Sets the given stat name with the given value.
   */
  public synchronized void set(int value, String stat_name) {
    IntegerStat stat = (IntegerStat) properties.get(stat_name);
    if (stat != null) {
      stat.value = value;
    }
    else {
      stat = new IntegerStat();
      stat.value = value;
      properties.put(stat_name, stat);
    }
  }

  /**
   * Return a String array of all stat keys sorted in order from lowest to
   * highest.
   */
  public synchronized String[] keyList() {
    Set key_set = properties.keySet();

    String[] keys = new String[key_set.size()];
    int index = 0;
    Iterator it = key_set.iterator();
    while (it.hasNext()) {
      keys[index] = (String) it.next();
      ++index;
    }

    // Sort the keys
    Arrays.sort(keys, STRING_COMPARATOR);

    return keys;
  }

  /**
   * Comparator for sorting the list of keys (for 1.1 implementation without
   * Comparable String objects).
   */
  final static Comparator STRING_COMPARATOR = new Comparator() {
    public int compare(Object ob1, Object ob2) {
      return ((String) ob1).compareTo((String) ob2);
    }
  };



  /**
   * Returns a String representation of the stat with the given key name.
   */
  public synchronized String statString(String key) {
    IntegerStat stat = (IntegerStat) properties.get(key);
    return Long.toString(stat.value);
  }

  /**
   * Returns a String that can be use to print out the values of all the stats.
   */
  public synchronized String toString() {

    String[] keys = keyList();

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < keys.length; ++i) {
      IntegerStat stat = (IntegerStat) properties.get(keys[i]);
      buf.append(keys[i]);
      buf.append(": ");
      buf.append(stat.value);
      buf.append('\n');
    }

    return new String(buf);
  }

  /**
   * Outputs the stats to a print stream.
   */
  public synchronized void printTo(PrintStream out) {

    String[] keys = keyList();

    for (int i = 0; i < keys.length; ++i) {
      IntegerStat stat = (IntegerStat) properties.get(keys[i]);
      out.print(keys[i]);
      out.print(": ");
      out.println(stat.value);
    }
  }




  // ---------- Inner class ----------

  private static class IntegerStat {
    long value;
  }

}
