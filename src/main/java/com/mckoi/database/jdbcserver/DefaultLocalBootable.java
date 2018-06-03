/**
 * com.mckoi.database.jdbcserver.DefaultLocalBootable  28 Mar 2002
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

package com.mckoi.database.jdbcserver;

//import com.mckoi.runtime.BootMain;
import com.mckoi.database.control.DBConfig;
import com.mckoi.database.control.DBSystem;
import com.mckoi.database.control.DBController;
import com.mckoi.database.control.DefaultDBConfig;
import com.mckoi.database.DatabaseSystem;
import com.mckoi.database.Database;
import com.mckoi.database.DatabaseException;
import com.mckoi.database.jdbc.LocalBootable;
import com.mckoi.database.jdbc.DatabaseCallBack;
import com.mckoi.database.jdbc.DatabaseInterface;
import com.mckoi.database.jdbc.QueryResponse;
import com.mckoi.database.jdbc.ResultPart;
import com.mckoi.database.jdbc.SQLQuery;

import java.io.File;
import java.sql.SQLException;

/**
 * A bootable object that filters through to a JDBCDatabaseInterface but
 * is thread-safe and multi-threaded.  This is to be used when you have a
 * local JDBC Client accessing a stand-alone database.
 *
 * @author Tobias Downer
 */

public class DefaultLocalBootable implements LocalBootable {

  /**
   * Set to true if the database is booted.
   */
  private boolean booted = false;

  /**
   * Set to true when this interface is active.
   */
  private boolean active = false;

  /**
   * The local DBSystem database object.
   */
  private DBSystem dbsys;

  /**
   * The connection id.  This is incremented by 1 each time an
   * interface connects to the local JVM.
   */
  private int connect_id = 0;

  /**
   * The number of connections that are current open.
   */
  private int open_connections = 0;

  /**
   * The connection lock object.
   */
  private Object connection_lock = new Object();

  /**
   * Creates and boots a local database with the given configuration.  This
   * is implemented from LocalBootable.
   *
   * @param config the configuration variables.
   */
  public DatabaseInterface create(String username, String password,
                                  DBConfig config) throws SQLException {

    if (username.equals("") || password.equals("")) {
      throw new SQLException("Username and Password must both be set.");
    }

    if (!booted) {
      // Local connections are formatted as;
      // 'Local/[type]/[connect_id]'
      String host_string = "Local/Create/";

      // Create the DBSystem and bind it to a DatabaseInterface.
      DBController controller = DBController.getDefault();
      dbsys = controller.createDatabase(config, username, password);
      DatabaseInterface db_interface =
         new LocalJDBCDatabaseInterface(dbsys.getDatabase(), host_string);

      booted = true;
      ++open_connections;
      active = true;

      return db_interface;
    }

    throw new SQLException("Database is already created.");

  }

  /**
   * Boots the local database with the given configuration.  This is
   * implemented from LocalBootable.
   *
   * @param config the configuration variables.
   */
  public DatabaseInterface boot(DBConfig config) throws SQLException {
    if (!booted) {
      // Local connections are formatted as;
      // 'Local/[type]/[connect_id]'
      String host_string = "Local/Boot/";

      // Start the DBSystem and bind it to a DatabaseInterface.
      DBController controller = DBController.getDefault();
      dbsys = controller.startDatabase(config);
      DatabaseInterface db_interface =
         new LocalJDBCDatabaseInterface(dbsys.getDatabase(), host_string);

      booted = true;
      ++open_connections;
      active = true;

      return db_interface;

    }
    else {
      throw new SQLException("Database was booted more than once.");
    }
  }

  /**
   * Attempts to test if the database exists or not.  Returns true if the
   * database exists.
   *
   * @param config the configuration variables.
   */
  public boolean checkExists(DBConfig config) throws SQLException {
    if (!booted) {
      DBController controller = DBController.getDefault();
      return controller.databaseExists(config);
    }
    else {
      throw new SQLException("The database is already booted.");
    }
  }

  /**
   * Returns true if a database has successfully been booted in this JVM.  If
   * a database hasn't been botted then it returns false.
   */
  public boolean isBooted() throws SQLException {
    return booted;
  }

  /**
   * Creates a new LocalDatabaseInterface that is connected to the database
   * currently running in this VM.  Calling this method must guarentee that
   * either 'boot' or 'create' has been called in this VM beforehand.
   */
  public DatabaseInterface connectToJVM() throws SQLException {
    if (booted) {

      // Local connections are formatted as;
      // 'Local/[type]/[connect_id]'
      String host_string = "Local/Connection/" + connect_id;

      // Create a DatabaseInterface,
      DatabaseInterface db_interface =
          new LocalJDBCDatabaseInterface(dbsys.getDatabase(), host_string);

      ++connect_id;
      ++open_connections;
      active = true;

      return db_interface;

    }
    else {
      throw new SQLException("The database is not started.");
    }

  }

  // ---------- Inner classes ----------

  /**
   * A local implementation of JDBCDatabaseInterface that will dispose the
   * parent LocalBootable object when the last open connection is disposed.
   */
  private class LocalJDBCDatabaseInterface extends JDBCDatabaseInterface {

    boolean closed = false;

    public LocalJDBCDatabaseInterface(Database database, String host_string) {
      super(database, host_string);
    }

    // ---------- Overwritten from JDBCDatabaseInterface ----------

    public void dispose() throws SQLException {
      if (!closed) {
        super.dispose();

        --open_connections;

        // When all connections are closed, shut down...
        if (open_connections <= 0) {
          // When the local database interface is disposed, we must shut down
          // the database system.
          dbsys.close();
          active = false;
          booted = false;
          dbsys = null;
        }
        closed = true;
      }

    }

    // ---------- Clean up ----------

    public void finalize() throws Throwable {
      super.finalize();
      dispose();
    }

  }

}
