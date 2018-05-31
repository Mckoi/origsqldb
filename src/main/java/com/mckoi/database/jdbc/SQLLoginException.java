/**
 * com.mckoi.database.jdbc.SQLLoginException  20 Jul 2000
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

package com.mckoi.database.jdbc;

import java.sql.*;

/**
 * An SQLException that signifies username/password authentication failed.
 *
 * @author Tobias Downer
 */

public class SQLLoginException extends SQLException {

  public SQLLoginException(String reason, String SQLState, int vendorCode) {
    super(reason, SQLState, vendorCode);
  }

  public SQLLoginException(String reason, String SQLState) {
    super(reason, SQLState);
  }

  public SQLLoginException(String reason) {
    super(reason);
  }

  public SQLLoginException() {
    super();
  }

}
