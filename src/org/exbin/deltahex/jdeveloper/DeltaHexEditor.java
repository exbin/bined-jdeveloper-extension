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
package org.exbin.deltahex.jdeveloper;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.UIManager;

import oracle.ide.editor.Editor;
import oracle.ide.model.UpdateMessage;

public class DeltaHexEditor extends Editor {

    private DeltaHexEditorPanel _panel = null;

    public DeltaHexEditor() {
        super();
    }

    @Override
    public Object getEditorAttribute(String attribute) {
        // By convention, flat editors use the window color as their background
        // color. The window color is usually white on most systems.
        if (ATTRIBUTE_BACKGROUND_COLOR.equals(attribute))
            return UIManager.getColor("window");

        // If you want framework provided scrolling, comment out the next three
        // if conditions. You probably want to control your own scrolling if your
        // editor contains category tabs.
        if (ATTRIBUTE_SCROLLABLE.equals(attribute))
            return Boolean.FALSE;

        if (ATTRIBUTE_HORIZONTAL_SCROLLBAR_POLICY.equals(attribute))
            return JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

        if (ATTRIBUTE_VERTICAL_SCROLLBAR_POLICY.equals(attribute))
            return JScrollPane.VERTICAL_SCROLLBAR_NEVER;

        return super.getEditorAttribute(attribute);
    }

    public void open() {
    }

    public Component getGUI() {
        if (_panel == null)
            _panel = new DeltaHexEditorPanel();
        return _panel;
    }

    public void update(Object object, UpdateMessage updateMessage) {
    }
}
