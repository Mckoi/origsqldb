/**
 * com.mckoi.util.UserTerminal  12 Apr 2001
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
 * An interface that represents a terminal that is asked questions in human
 * and machine understandable terms, and sends answers.  This interface is
 * intended for an interface in which the user is asked questions, or for an
 * automated tool.
 *
 * @author Tobias Downer
 */

public interface UserTerminal {

  /**
   * Outputs a string of information to the terminal.
   */
  public void print(String str);

  /**
   * Outputs a string of information and a newline to the terminal.
   */
  public void println(String str);

  /**
   * Asks the user a question from the 'question' string.  The 'options' list
   * is the list of options that the user may select from.  The
   * 'default_answer' is the option that is selected by default.
   */
  public int ask(String question, String[] options, int default_answer);

}
