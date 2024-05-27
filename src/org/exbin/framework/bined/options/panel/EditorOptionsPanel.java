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
package org.exbin.framework.bined.options.panel;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.basic.EnterKeyHandlingMode;
import org.exbin.framework.bined.FileHandlingMode;
import org.exbin.framework.bined.options.impl.EditorOptionsImpl;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.options.api.OptionsCapable;
import org.exbin.framework.gui.options.api.OptionsModifiedListener;

/**
 * Editor preference parameters panel.
 *
 * @version 0.2.1 2019/07/20
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorOptionsPanel extends javax.swing.JPanel implements OptionsCapable<EditorOptionsImpl> {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(EditorOptionsPanel.class);

    public EditorOptionsPanel() {
        initComponents();
    }

    @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public void saveToOptions(EditorOptionsImpl options) {
        options.setFileHandlingMode(FileHandlingMode.valueOf((String) fileHandlingModeComboBox.getSelectedItem()));
        options.setShowValuesPanel(showValuesPanelCheckBox.isSelected());
        options.setEnterKeyHandlingMode(EnterKeyHandlingMode.valueOf((String) enterKeyHandlingModeComboBox.getSelectedItem()));
    }

    @Override
    public void loadFromOptions(EditorOptionsImpl options) {
        fileHandlingModeComboBox.setSelectedIndex(options.getFileHandlingMode().ordinal());
        showValuesPanelCheckBox.setSelected(options.isShowValuesPanel());
        enterKeyHandlingModeComboBox.setSelectedIndex(options.getEnterKeyHandlingMode().ordinal());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileHandlingModeLabel = new javax.swing.JLabel();
        fileHandlingModeComboBox = new javax.swing.JComboBox<>();
        showValuesPanelCheckBox = new javax.swing.JCheckBox();
        enterKeyHandlingModeLabel = new javax.swing.JLabel();
        enterKeyHandlingModeComboBox = new javax.swing.JComboBox<>();

        fileHandlingModeLabel.setText(resourceBundle.getString("fileHandlingModeLabel.text")); // NOI18N

        fileHandlingModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MEMORY", "DELTA" }));

        showValuesPanelCheckBox.setText(resourceBundle.getString("showValuesPanelCheckBox.text")); // NOI18N

        enterKeyHandlingModeLabel.setText(resourceBundle.getString("enterKeyHandlingModeLabel.text")); // NOI18N

        enterKeyHandlingModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PLATFORM_SPECIFIC", "CR", "LF", "CRLF", "IGNORE" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileHandlingModeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showValuesPanelCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileHandlingModeLabel)
                            .addComponent(enterKeyHandlingModeLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(enterKeyHandlingModeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showValuesPanelCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileHandlingModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileHandlingModeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterKeyHandlingModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterKeyHandlingModeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new EditorOptionsPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> enterKeyHandlingModeComboBox;
    private javax.swing.JLabel enterKeyHandlingModeLabel;
    private javax.swing.JComboBox<String> fileHandlingModeComboBox;
    private javax.swing.JLabel fileHandlingModeLabel;
    private javax.swing.JCheckBox showValuesPanelCheckBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setOptionsModifiedListener(OptionsModifiedListener listener) {
    }
}
