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

import oracle.ide.config.Preferences;
import oracle.ide.editor.EditorAddin;
import oracle.ide.editor.EditorManager;
import oracle.ide.util.MenuSpec;

import org.exbin.framework.bined.preferences.BinaryEditorPreferences;
import org.exbin.framework.preferences.PreferencesWrapper;

/**
 * Binary editor extension initialization.
 *
 * @author ExBin Project (http://exbin.org)
 * @version 0.2.0 2019/08/12
 */
public class BinEdEditorAddin extends EditorAddin {

    public BinEdEditorAddin() {
        super();
        
//        BinaryEditorPreferences preferences = new BinaryEditorPreferences(new PreferencesWrapper(Preferences.getPreferences()));
        
    }

    public void initialize() {
        EditorManager.getEditorManager().registerDynamic(this);
    }

    @Override
    public Class getEditorClass() {
        return BinEdFileEditor.class;
    }

    @Override
    public MenuSpec getMenuSpecification() {
        return new MenuSpec("Binary", null);
    }
    
    @Override
    public float getEditorWeight(oracle.ide.model.Element element) {
//        if (element == null || element.getData() == null) {
//            return Float.NaN;
//        }

        return -1f;
    }
}
