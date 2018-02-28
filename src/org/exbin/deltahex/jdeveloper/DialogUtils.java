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

import java.awt.BorderLayout;

import javax.swing.*;

/**
 * Dialog utilities.
 *
 * @author ExBin Project (http://exbin.org)
 * @version 0.1.0 2018/02/28
 */
public class DialogUtils {

    public static JDialog createDialog(JComponent dialogPanel, String dialogTitle) {
        return createDialog(dialogPanel, dialogTitle, null);
    }

    public static JDialog createDialog(JComponent dialogPanel, String dialogTitle, JComponent focusedComponent) {
        JDialog dialog = new JDialog();
        dialog.setTitle(dialogTitle);
        dialog.add(dialogPanel, BorderLayout.CENTER);
        focusedComponent.grabFocus();
        
        return dialog;
    }
}
