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

import java.net.URL;

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.dialogs.DialogUtil;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.editor.OpenEditorOptions;
import oracle.ide.extension.RegisteredByExtension;
import oracle.ide.model.Element;
import oracle.ide.model.Locatable;
import oracle.ide.model.Node;
import oracle.ide.net.URLChooser;

/**
 * Controller for action org.exbin.bined.jdeveloper.openfileasbinary.
 *
 * @version 0.2.0 2019/08/13
 * @author ExBin Project (http://exbin.org)
 */
@RegisteredByExtension("org.exbin.bined.jdeveloper")
public final class OpenFileAsBinaryController implements Controller {

    private static final String OPEN_FILE_AS_BINARY_ID = "org.exbin.bined.jdeveloper.openFileAsBinaryAction";
    static final int OPEN_FILE_AS_BINARY_CMD_ID = Ide.findOrCreateCmdID(OPEN_FILE_AS_BINARY_ID);

    public boolean handleEvent(IdeAction action, Context context) {
        URLChooser chooser = DialogUtil.newURLChooser(context);
        chooser.setSelectionMode(URLChooser.SINGLE_SELECTION);

        int result = chooser.showOpenDialog(Ide.getMainWindow(), "Open Binary File");
        if (result != URLChooser.APPROVE_OPTION) return true;
        
        URL url = chooser.getSelectedURL();
        if (url != null) {
            EditorManager editorManager = EditorManager.getEditorManager();
            Context wrappedContext = new Context(context);
            BinaryNode node = new BinaryNode(new Node(url));
            wrappedContext.setNode(node);
            editorManager.openEditorInFrame(BinEdFileEditor.class, wrappedContext);
        }

        return true;
    }

    public boolean update(IdeAction action, Context context) {
        action.setEnabled(true);
        return true;
    }
}
