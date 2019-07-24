/*
 * Copyright (C) ExBin Project
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
 */
package org.exbin.bined.jdeveloper;

import oracle.ide.Ide;
import oracle.ide.controller.Command;
import oracle.ide.editor.EditorManager;
import oracle.ide.extension.RegisteredByExtension;

/**
 * Open as binary command.
 *
 * @version 0.2.0 2019/07/24
 * @author ExBin Project (http://exbin.org)
 */

@RegisteredByExtension("org.exbin.bined.jdeveloper")
public final class OpenAsBinaryCommand extends Command {

    public OpenAsBinaryCommand() {
        super(actionId());
    }

    public static int actionId() {
        final Integer cmdId = Ide.findCmdID("org.exbin.bined.jdeveloper.openAsBinaryAction");
        if (cmdId == null)
            throw new IllegalStateException("Action org.exbin.bined.jdeveloper.openAsBinaryAction not found.");
        return cmdId;
    }

    @Override
    public int doit() throws Exception {
        EditorManager.getEditorManager().openEditorInFrame(BinEdFileEditor.class, context);
        return 0;
    }
}
