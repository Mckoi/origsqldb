/**
 * com.mckoi.database.ProcedureConnection  06 Mar 2003
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

import java.sql.Connection;

/**
 * An interface for accessing a database connection inside a stored procedure.
 *
 * @author Tobias Downer
 */

public interface ProcedureConnection {

  /**
   * Returns a JDBC Connection implementation for executing queries on this
   * connection.  The Connection has auto-commit turned off, and it
   * disables the ability for the connection to 'commit' changes to the
   * database.
   * <p>
   * This method is intended to provide the procedure developer with a
   * convenient and consistent way to query and manipulate the database from 
   * the body of a stored procedure method.
   * <p>
   * The java.sql.Connection object returned here may invalidate when the
   * procedure invokation call ends so the returned object must not be cached
   * to be used again later.
   * <p>
   * The returned java.sql.Connection object is NOT thread safe and should
   * only be used by a single thread.  Accessing this connection from multiple
   * threads will result in undefined behaviour.
   * <p>
   * The Connection object returned here has the same privs as the user who
   * owns the stored procedure.
   */
  Connection getJDBCConnection();

  /**
   * Returns the Database object for this database providing access to various
   * general database features including backing up replication and
   * configuration.  Some procedures may not be allowed access to this object
   * in which case a ProcedureException is thrown notifying of the security
   * violation.
   */
  Database getDatabase();

}

