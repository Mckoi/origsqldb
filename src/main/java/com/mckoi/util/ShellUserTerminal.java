/**
 * com.mckoi.util.ShellUserTerminal  12 Apr 2001
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

package com.mckoi.util;

/**
 * An implementation of UserTerminal that uses the shell terminal via
 * System.in and System.out.
 *
 * @author Tobias Downer
 */

public class ShellUserTerminal implements UserTerminal {

  // ---------- Implemented from UserTerminal ----------

  public void print(String str) {
    System.out.print(str);
    System.out.flush();
  }

  public void println(String str) {
    System.out.println(str);
  }

  public int ask(String question, String[] options, int default_answer) {
    // TODO
    return default_answer;
  }

}
