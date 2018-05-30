**
 * com.mckoi.database.SchemaDef  29 Aug 2001
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
 * A definition of a schema.
 *
 * @author Tobias Downer
 */

public final class SchemaDef {

  /**
   * The name of the schema (eg. APP).
   */
  private String name;

  /**
   * The type of this schema (eg. SYSTEM, USER, etc)
   */
  private String type;

  /**
   * Constructs the SchemaDef.
   */
  public SchemaDef(String name, String type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Returns the case correct name of the schema.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the type of this schema.
   */
  public String getType() {
    return type;
  }

  public String toString() {
    return getName();
  }
  
}
