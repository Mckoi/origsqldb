/**
 * com.mckoi.database.DatabaseConstants  04 May 1998
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
 * Contant static values that determine several parameters of the database
 * operation.  It is important that a database data generated from a
 * compilation from one set of constants is not used with the same database
 * with different constants.
 * <p>
 * @author Tobias Downer
 */

public interface DatabaseConstants {

  /**
   * The maximum length in characters of the string that represents the name
   * of the database.
   */
  public static final int MAX_DATABASE_NAME_LENGTH = 50;

  /**
   * The maximum length in characters of the string that represents the name
   * of a privaledge group.
   */
  public static final int MAX_PRIVGROUP_NAME_LENGTH = 50;

  /**
   * The maximum length in characters of the string that holds the table
   * name.  The table name is used to reference a Table object in a Database.
   */
  public static final int MAX_TABLE_NAME_LENGTH = 50;

  /**
   * The maximum length in characters of the string that holds the user
   * name.  The user name is used in many security and priviledge operations.
   */
  public static final int MAX_USER_NAME_LENGTH = 50;

  /**
   * The maximum length in character of the string that holds a users
   * password.  The password is used when logging into the database.
   */
  public static final int MAX_PASSWORD_LENGTH = 80;

}
