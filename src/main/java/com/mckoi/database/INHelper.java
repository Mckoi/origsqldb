**
 * com.mckoi.database.INHelper  17 Sep 1998
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

import com.mckoi.util.BlockIntegerList;
import com.mckoi.util.IntegerVector;

/**
 * This is a static class that provides methods for performing the Query
 * table command 'in' and 'not in'.  This command finds a match between one
 * of the columns in two tables.  If match between a cell in one column is also
 * found in the column of the other table, the row is included in the resultant
 * table (or discluded (is that a word?) for 'not in').
 * <p>
 * @author Tobias Downer
 */

final class INHelper {

  /**
   * This implements the 'in' command.  Returns the rows selected from table1.
   * <p>
   * <strong>NOTE:</strong> This is actually an incorrect implementation.  We
   *   only keep for compatibility with DQL system.  The may return multiple
   *   values from 'table1'
   */
  final static IntegerVector origIn(Table table1, Table table2,
                                    int column1, int column2) {

    // First pick the the smallest and largest table.  We only want to iterate
    // through the smallest table.
    // NOTE: This optimisation can't be performed for the 'not_in' command.

    Table small_table;
    Table large_table;
    int small_column;
    int large_column;

    if (table1.getRowCount() < table2.getRowCount()) {
      small_table = table1;
      large_table = table2;

      small_column = column1;
      large_column = column2;

    }
    else {
      small_table = table2;
      large_table = table1;

      small_column = column2;
      large_column = column1;
    }

    // Iterate through the small table's column.  If we can find identical
    // cells in the large table's column, then we should include the row in our
    // final result.

    IntegerVector result_rows = new IntegerVector();

    RowEnumeration e = small_table.rowEnumeration();

    Operator EQUALSOP = Operator.get("=");

    while (e.hasMoreRows()) {

      int small_row_index = e.nextRowIndex();
      TObject cell =
                  small_table.getCellContents(small_column, small_row_index);

      IntegerVector selected_set =
                  large_table.selectRows(large_column, EQUALSOP, cell);

      // We've found cells that are IN both columns,

      if (selected_set.size() > 0) {

        // If the large table is what our result table will be based on, append
        // the rows selected to our result set.  Otherwise add the index of
        // our small table.  This only works because we are performing an
        // EQUALS operation.

        if (large_table == table1) {
          result_rows.append(selected_set);
        }
        else {
          result_rows.addInt(small_row_index);
        }
      }

    }

    return result_rows;

  }

  /**
   * This implements the 'in' command.  Returns the rows selected from table1.
   * This correctly implements the 'in' relation.  The 'origIn' implementation
   * may return multiple rows from the largest table.
   */
  final static IntegerVector in(Table table1, Table table2,
                                int column1, int column2) {

    // First pick the the smallest and largest table.  We only want to iterate
    // through the smallest table.
    // NOTE: This optimisation can't be performed for the 'not_in' command.

    Table small_table;
    Table large_table;
    int small_column;
    int large_column;

    if (table1.getRowCount() < table2.getRowCount()) {
      small_table = table1;
      large_table = table2;

      small_column = column1;
      large_column = column2;

    }
    else {
      small_table = table2;
      large_table = table1;

      small_column = column2;
      large_column = column1;
    }

    // Iterate through the small table's column.  If we can find identical
    // cells in the large table's column, then we should include the row in our
    // final result.

    BlockIntegerList result_rows = new BlockIntegerList();

    RowEnumeration e = small_table.rowEnumeration();

    Operator EQUALSOP = Operator.get("=");

    while (e.hasMoreRows()) {

      int small_row_index = e.nextRowIndex();
      TObject cell =
                 small_table.getCellContents(small_column, small_row_index);

      IntegerVector selected_set =
                 large_table.selectRows(large_column, EQUALSOP, cell);

      // We've found cells that are IN both columns,

      if (selected_set.size() > 0) {

        // If the large table is what our result table will be based on, append
        // the rows selected to our result set.  Otherwise add the index of
        // our small table.  This only works because we are performing an
        // EQUALS operation.

        if (large_table == table1) {
          // Only allow unique rows into the table set.
          int sz = selected_set.size();
          boolean rs = true;
          for (int i = 0; rs == true && i < sz; ++i) {
            rs = result_rows.uniqueInsertSort(selected_set.intAt(i));
          }
        }
        else {
          // Don't bother adding in sorted order because it's not important.
          result_rows.add(small_row_index);
        }
      }

    }

    return new IntegerVector(result_rows);

  }

  /**
   * A multi-column version of IN.
   */
  final static IntegerVector in(Table table1, Table table2,
                                int[] t1_cols, int[] t2_cols) {
    if (t1_cols.length > 1) {
      throw new Error("Multi-column 'in' not supported.");
    }
    return in(table1, table2, t1_cols[0], t2_cols[0]);
  }

  /**
   * This implements the 'not_in' command.
   * ISSUE: This will be less efficient than 'in' if table1 has many rows and
   *   table2 has few rows.
   */
  final static IntegerVector notIn(Table table1, Table table2,
                                   int col1, int col2) {

    // Handle trivial cases
    int t2_row_count = table2.getRowCount();
    if (t2_row_count == 0) {
      // No rows so include all rows.
      return table1.selectAll(col1);
    }
    else if (t2_row_count == 1) {
      // 1 row so select all from table1 that doesn't equal the value.
      RowEnumeration e = table2.rowEnumeration();
      TObject cell = table2.getCellContents(col2, e.nextRowIndex());
      return table1.selectRows(col1, Operator.get("<>"), cell);
    }

    // Iterate through table1's column.  If we can find identical cell in the
    // tables's column, then we should not include the row in our final
    // result.
    IntegerVector result_rows = new IntegerVector();

    RowEnumeration e = table1.rowEnumeration();

    Operator EQUALSOP = Operator.get("=");

    while (e.hasMoreRows()) {

      int row_index = e.nextRowIndex();
      TObject cell = table1.getCellContents(col1, row_index);

      IntegerVector selected_set =
                            table2.selectRows(col2, Operator.get("="), cell);

      // We've found a row in table1 that doesn't have an identical cell in
      // table2, so we should include it in the result.

      if (selected_set.size() <= 0) {
        result_rows.addInt(row_index);
      }

    }

    return result_rows;
  }

  /**
   * A multi-column version of NOT IN.
   */
  final static IntegerVector notIn(Table table1, Table table2,
                                   int[] t1_cols, int[] t2_cols) {
    if (t1_cols.length > 1) {
      throw new Error("Multi-column 'not in' not supported.");
    }
    return notIn(table1, table2, t1_cols[0], t2_cols[0]);
  }


}
