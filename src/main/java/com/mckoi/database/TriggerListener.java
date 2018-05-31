/**
 * com.mckoi.database.TriggerListener  02 Oct 2000
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
 * A listener that can listen for high layer trigger events.
 *
 * @author Tobias Downer
 */

public interface TriggerListener {

  /**
   * Notifies that a trigger event fired.
   *
   * @param database the DatabaseConnection that this trigger is registered
   *                 for.
   * @param trigger_evt the trigger event that was fired.
   */
  void fireTrigger(DatabaseConnection database, String trigger_name,
                   TriggerEvent trigger_evt);

}
