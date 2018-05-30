**
 * com.mckoi.database.IndexSet  19 Sep 2001
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

import com.mckoi.util.IntegerListInterface;

/**
 * A set of list of indexes.  This will often expose an isolated snapshot of a
 * set of indices for a table.
 *
 * @author Tobias Downer
 */

public interface IndexSet {

  /**
   * Returns a mutable object that implements IntegerListInterface for the
   * given index number in this set of indices.
   */
  IntegerListInterface getIndex(int n);

  /**
   * Cleans up and disposes the resources associated with this set of index.
   */
  void dispose();

}
