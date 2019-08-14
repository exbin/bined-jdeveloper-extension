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

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.editor.OpenEditorOptions;
import oracle.ide.extension.RegisteredByExtension;
import oracle.ide.model.Element;
import oracle.ide.model.Locatable;

/**
 * Controller for action org.exbin.bined.jdeveloper.openasbinary.
 * 
 * @version 0.2.0 2019/08/11
 * @author ExBin Project (http://exbin.org)
 */
@RegisteredByExtension("org.exbin.bined.jdeveloper")
public final class OpenAsBinaryController implements Controller {

    private static final String OPEN_AS_BINARY_ID = "org.exbin.bined.jdeveloper.openAsBinaryAction";
    static final int OPEN_AS_BINARY_CMD_ID = Ide.findOrCreateCmdID(OPEN_AS_BINARY_ID);

    public boolean handleEvent(IdeAction action, Context context) {
        EditorManager editorManager = EditorManager.getEditorManager();
//        Editor editor = editorManager.openEditorInFrame(BinEdFileEditor.class, context);
//        editor.getToolbar().add

        Context wrappedContext = new Context(context);
        wrappedContext.setNode(new BinaryNode(wrappedContext.getNode()));

//        Element element = context.getElement();
//        wrapperContext.setElement(new Element(){});
        
//        final Element[] selection = context.getSelection();
//        OpenEditorOptions options = new OpenEditorOptions(wrappedContext);
//        options.setEditorClass(BinEdFileEditor.class);
//        Editor editor = EditorManager.getEditorManager().openEditor(BinEdFileEditor.class, options);
        editorManager.openEditorInFrame(BinEdFileEditor.class, wrappedContext);
//        editorManager.openDefaultEditorInFrame(context);
//        editor.
        return true;
    }

    public boolean update(IdeAction action, Context context) {
        action.setEnabled(actionIsEnabled(context));
        return true;
    }

    private boolean actionIsEnabled(Context context) {
        final Element[] selection = context.getSelection();
        if (selection == null || selection.length != 1 ||
             !(selection[0] instanceof Locatable)) return false;
        
//        Locatable locatable = (Locatable) selection[0];
//        if (locatable instanceof FavoritesFolder) return false;
        
//        return !FavoritesFolder.getFavoritesFolder().containsChild( selection[0] );
        return true;
    }
}
