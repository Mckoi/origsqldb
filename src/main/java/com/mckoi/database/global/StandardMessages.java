**
 * com.mckoi.database.global.StandardMessages  22 May 1998
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

package com.mckoi.database.global;

/**
 * This class contains a number of standard messages that are displayed
 * throughout the operation of the database.  They are put into a single class
 * to allow for easy future modification.
 *
 * @author Tobias Downer
 */

public final class StandardMessages {

  /**
   * The name of the author (me).
   */
  public static String AUTHOR = "Tobias Downer";

  /**
   * The standard copyright message.
   */
  public static String COPYRIGHT =
       "Copyright (C) 2000 - 2013 Diehl and Associates, Inc.  " +
       "All rights reserved.";

  /**
   * The global version number of the database system.
   */
  public static String VERSION = "1.0.6";

  /**
   * The global name of the system.
   */
  public static String NAME = "Mckoi SQL Database ( " + VERSION + " )";

}
