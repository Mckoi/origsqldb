**
 * com.mckoi.database.RowEnumeration  05 Apr 1998
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

/**
 * This enumeration allows for access to a tables rows.  Each call to
 * 'nextRowIndex()' returns an int that can be used in the
 * 'Table.getCellContents(int row, int column)'.
 * <p>
 * @author Tobias Downer
 */

public interface RowEnumeration {

  /**
   * Determines if there are any rows left in the enumeration.
   */
  public boolean hasMoreRows();

  /**
   * Returns the next row index from the enumeration.
   */
  public int nextRowIndex();

}
