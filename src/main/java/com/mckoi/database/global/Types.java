/**
 * com.mckoi.database.global.Types  11 May 1998
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
 * The possible types used in the database.
 * <p>
 * @author Tobias Downer
 */

public interface Types {

  public static final int DB_UNKNOWN = -1;

  public static final int DB_STRING  = 1;
  public static final int DB_NUMERIC = 2;
  public static final int DB_TIME    = 3;
  public static final int DB_BINARY  = 4;    // @deprecated - use BLOB
  public static final int DB_BOOLEAN = 5;
  public static final int DB_BLOB    = 6;
  public static final int DB_OBJECT  = 7;
  
  // This is an extended numeric type that handles neg and positive infinity
  // and NaN.
  public static final int DB_NUMERIC_EXTENDED = 8;

}
