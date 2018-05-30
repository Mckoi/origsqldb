**
 * com.mckoi.database.ProcedureException  06 Mar 2003
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
 * An exception that is generated from a stored procedure when some erronious
 * condition occurs.  This error is typically returned back to the client.
 *
 * @author Tobias Downer
 */

public class ProcedureException extends RuntimeException {

  /**
   * Construct the exception.
   */
  public ProcedureException(String str) {
    super(str);
  }

}

