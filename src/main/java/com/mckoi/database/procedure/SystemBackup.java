/**
 * com.mckoi.database.procedure.SystemBackup  27 Feb 2003
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

package com.mckoi.database.procedure;

import com.mckoi.database.ProcedureConnection;
import com.mckoi.database.ProcedureException;
import java.io.File;
import java.io.IOException;

/**
 * A stored procedure that backs up the entire database to the given directory
 * in the file system.  Requires one parameter, the locate to back up the
 * database to.
 *
 * @author Tobias Downer
 */

public class SystemBackup {

  /**
   * The stored procedure invokation method.
   */
  public static String invoke(ProcedureConnection db_connection,
                              String path) {

    File f = new File(path);
    if (!f.exists() || !f.isDirectory()) {
      throw new ProcedureException("Path '" + path +
                                   "' doesn't exist or is not a directory.");
    }

    try {
      db_connection.getDatabase().liveCopyTo(f);
      return path;
    }
    catch (IOException e) {
      e.printStackTrace();
      throw new ProcedureException("IO Error: " + e.getMessage());
    }

  }

}

