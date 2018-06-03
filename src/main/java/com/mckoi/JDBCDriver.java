/**
 * com.mckoi.JDBCDriver  22 Jul 2000
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

package com.mckoi;

/**
 * Instance class that registers the mckoi JDBC driver with the JDBC
 * Driver Manager.
 * <p>
 * This class now also extends com.mckoi.database.jdbc.MDriver.
 *
 * @author Tobias Downer
 */

public class JDBCDriver extends com.mckoi.database.jdbc.MDriver {

  /**
   * Just referencing this class will register the JDBC driver.  Any objections
   * to this behaviour?
   */
  static {
    com.mckoi.database.jdbc.MDriver.register();
  }

  /**
   * Constructor.
   */
  public JDBCDriver() {
    super();
    // Or we could move driver registering here...
  }

}
