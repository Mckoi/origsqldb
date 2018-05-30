**
 * com.mckoi.debug.Lvl  28 Mar 2002
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

package com.mckoi.debug;

/**
 * Debug level static values.
 *
 * @author Tobias Downer
 */

public interface Lvl {

  /**
   * Some sample debug levels.
   */
  public final static int INFORMATION = 10;    // General processing 'noise'
  public final static int WARNING     = 20;    // A message of some importance
  public final static int ALERT       = 30;    // Crackers, etc
  public final static int ERROR       = 40;    // Errors, exceptions
  public final static int MESSAGE     = 10000; // Always printed messages
                                               // (not error's however)

}
