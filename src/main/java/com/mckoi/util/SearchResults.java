/**
 * com.mckoi.util.SearchResults  29 Mar 1998
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

/**
 * This object stores the result of a given search.  It provides information
 * object where in the set the found elements are, and the number of elements
 * in the set that match the search criteria.
 * <p>
 * @author Tobias Downer
 */

public final class SearchResults {

  /**
   * The index in the array of the found elements.
   */
  int found_index;

  /**
   * The number of elements in the array that match the given search criteria.
   */
  int found_count;

  /**
   * The Constructor.
   */
  public SearchResults() {
  }

  /**
   * Functions for querying information in the results.
   */
  public int getCount() {
    return found_count;
  }

  public int getIndex() {
    return found_index;
  }

}
