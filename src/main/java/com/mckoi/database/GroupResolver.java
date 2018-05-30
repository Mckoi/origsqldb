**
 * com.mckoi.database.GroupResolver  14 Jul 2000
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

import com.mckoi.util.IntegerVector;

/**
 * Similar to VariableResolver, this method is used by grouping Functions to
 * find information about the current group being evaluated (used for
 * evaluating aggregate functions).
 *
 * @author Tobias Downer
 */

public interface GroupResolver {

  /**
   * A number that uniquely identifies this group from all the others in the
   * set of groups.
   */
  public int groupID();

  /**
   * The total number of set items in this group.
   */
  public int size();

  /**
   * Returns the value of a variable of a group.  The set index signifies the
   * set item of the group.  For example, if the group contains 10 items, then
   * set_index may be between 0 and 9.  Return types must be either
   * a String, BigDecimal or Boolean.
   */
  public TObject resolve(Variable variable, int set_index);

  /**
   * Returns a VariableResolver that can be used to resolve variable in the
   * get set of the group.  The object returned is undefined after the next
   * call to this method.
   */
  public VariableResolver getVariableResolver(int set_index);

}
