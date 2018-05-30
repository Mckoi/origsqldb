**
 * com.mckoi.database.interpret.DropTrigger  14 Sep 2001
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

package com.mckoi.database.interpret;

import com.mckoi.database.*;
import com.mckoi.util.IntegerVector;
import java.util.ArrayList;
import java.util.List;

/**
 * A parsed state container for the 'DROP TRIGGER' statement.
 *
 * @author Tobias Downer
 */

public class DropTrigger extends Statement {

  /**
   * The name of this trigger.
   */
  String trigger_name;


  // ---------- Implemented from Statement ----------

  public void prepare() throws DatabaseException {
    trigger_name = (String) cmd.getObject("trigger_name");
  }

  public Table evaluate() throws DatabaseException {

    String type = (String) cmd.getObject("type");

    DatabaseQueryContext context = new DatabaseQueryContext(database);

    if (type.equals("callback_trigger")) {
      database.deleteTrigger(trigger_name);
    }
    else {

      // Convert the trigger into a table name,
      String schema_name = database.getCurrentSchema();
      TableName t_name = TableName.resolve(schema_name, trigger_name);
      t_name = database.tryResolveCase(t_name);

      ConnectionTriggerManager manager = database.getConnectionTriggerManager();
      manager.dropTrigger(t_name.getSchema(), t_name.getName());

      // Drop the grants for this object
      database.getGrantManager().revokeAllGrantsOnObject(
                                    GrantManager.TABLE, t_name.toString());
    }
    
    // Return '0' if we created the trigger.
    return FunctionTable.resultTable(context, 0);
  }


}
