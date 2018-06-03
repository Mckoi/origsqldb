/**
 * com.mckoi.tests2.ManyTableTest  09 Feb 2003
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

package com.mckoi.tests;

import java.sql.*;

/**
 * 
 *
 * @author Tobias Downer
 */

public class ManyTableTest {

  public static void main(String[] args) {
    
    try {
      
      Class.forName("com.mckoi.JDBCDriver");

      Connection c = DriverManager.getConnection(
                                "jdbc:mckoi:local://db.conf", "test", "test");
      Statement stmt = c.createStatement();

      for (int i = 0; i < 1000; ++i) {
        stmt.executeQuery("CREATE TABLE Table" + i + " ( c1 int, c2 varchar )");
      }
      
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    
  }

}

