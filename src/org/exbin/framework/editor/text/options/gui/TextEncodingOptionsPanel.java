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
package org.exbin.framework.editor.text.options.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.exbin.framework.editor.text.options.impl.TextEncodingOptionsImpl;
import org.exbin.framework.editor.text.preferences.TextEncodingPreferences;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.options.api.OptionsCapable;
import org.exbin.framework.gui.options.api.OptionsModifiedListener;
import org.exbin.framework.editor.text.service.TextEncodingService;
import org.exbin.xbup.core.util.StringUtils;

/**
 * Text encoding options panel.
 *
 * @version 0.2.1 2019/07/21
 * @author ExBin Project (http://exbin.org)
 */
public class TextEncodingOptionsPanel extends javax.swing.JPanel implements OptionsCapable<TextEncodingOptionsImpl> {

    private OptionsModifiedListener optionsModifiedListener;
    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(TextEncodingOptionsPanel.class);
    private TextEncodingService textEncodingService;
    private final TextEncodingPanel encodingPanel;
    private final DefaultEncodingComboBoxModel encodingComboBoxModel = new DefaultEncodingComboBoxModel();

    public TextEncodingOptionsPanel() {
        encodingPanel = new TextEncodingPanel();

        initComponents();
        init();
    }

    private void init() {
        encodingPanel.setEnabled(false);
        encodingPanel.setOptionsModifiedListener(() -> {
            setModified(true);
            updateEncodings();
        });
        super.add(encodingPanel, BorderLayout.CENTER);
    }

    @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setTextEncodingService(TextEncodingService textEncodingService) {
        this.textEncodingService = textEncodingService;
        fillCurrentEncodingButton.setEnabled(true);
        fillCurrentEncodingsButton.setEnabled(true);
    }

    @Override
    public void saveToOptions(TextEncodingOptionsImpl options) {
        encodingPanel.saveToOptions(options);
        options.setSelectedEncoding((String) defaultEncodingComboBox.getSelectedItem());
    }

    @Override
    public void loadFromOptions(TextEncodingOptionsImpl options) {
        encodingPanel.loadFromOptions(options);
        defaultEncodingComboBox.setSelectedItem(options.getSelectedEncoding());
        updateEncodings();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        defaultEncodingPanel = new javax.swing.JPanel();
        defaultEncodingComboBox = new javax.swing.JComboBox();
        defaultEncodingLabel = new javax.swing.JLabel();
        fillCurrentEncodingButton = new javax.swing.JButton();
        encodingsControlPanel = new javax.swing.JPanel();
        fillCurrentEncodingsButton = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        defaultEncodingPanel.setName("defaultEncodingPanel"); // NOI18N

        defaultEncodingComboBox.setModel(encodingComboBoxModel);
        defaultEncodingComboBox.setName("defaultEncodingComboBox"); // NOI18N
        defaultEncodingComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                defaultEncodingComboBoxItemStateChanged(evt);
            }
        });

        defaultEncodingLabel.setText(resourceBundle.getString("defaultEncodingLabel.text")); // NOI18N
        defaultEncodingLabel.setName("defaultEncodingLabel"); // NOI18N

        fillCurrentEncodingButton.setText(resourceBundle.getString("fillCurrentEncodingButton.text")); // NOI18N
        fillCurrentEncodingButton.setEnabled(false);
        fillCurrentEncodingButton.setName("fillCurrentEncodingButton"); // NOI18N
        fillCurrentEncodingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillCurrentEncodingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout defaultEncodingPanelLayout = new javax.swing.GroupLayout(defaultEncodingPanel);
        defaultEncodingPanel.setLayout(defaultEncodingPanelLayout);
        defaultEncodingPanelLayout.setHorizontalGroup(
            defaultEncodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(defaultEncodingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(defaultEncodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defaultEncodingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(defaultEncodingPanelLayout.createSequentialGroup()
                        .addComponent(fillCurrentEncodingButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(defaultEncodingComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        defaultEncodingPanelLayout.setVerticalGroup(
            defaultEncodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(defaultEncodingPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(defaultEncodingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defaultEncodingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fillCurrentEncodingButton))
        );

        add(defaultEncodingPanel, java.awt.BorderLayout.NORTH);

        encodingsControlPanel.setName("encodingsControlPanel"); // NOI18N

        fillCurrentEncodingsButton.setText(resourceBundle.getString("fillCurrentEncodingsButton.text")); // NOI18N
        fillCurrentEncodingsButton.setEnabled(false);
        fillCurrentEncodingsButton.setName("fillCurrentEncodingsButton"); // NOI18N
        fillCurrentEncodingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillCurrentEncodingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout encodingsControlPanelLayout = new javax.swing.GroupLayout(encodingsControlPanel);
        encodingsControlPanel.setLayout(encodingsControlPanelLayout);
        encodingsControlPanelLayout.setHorizontalGroup(
            encodingsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encodingsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fillCurrentEncodingsButton)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        encodingsControlPanelLayout.setVerticalGroup(
            encodingsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encodingsControlPanelLayout.createSequentialGroup()
                .addComponent(fillCurrentEncodingsButton)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        add(encodingsControlPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void fillCurrentEncodingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillCurrentEncodingsButtonActionPerformed
        encodingPanel.setEncodingList(textEncodingService.getEncodings());
        encodingPanel.repaint();
        updateEncodings();
        setModified(true);
    }//GEN-LAST:event_fillCurrentEncodingsButtonActionPerformed

    private void fillCurrentEncodingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillCurrentEncodingButtonActionPerformed
        defaultEncodingComboBox.setSelectedItem(textEncodingService.getSelectedEncoding());
        defaultEncodingComboBox.repaint();
        setModified(true);
    }//GEN-LAST:event_fillCurrentEncodingButtonActionPerformed

    private void defaultEncodingComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_defaultEncodingComboBoxItemStateChanged
        setModified(true);
    }//GEN-LAST:event_defaultEncodingComboBoxItemStateChanged

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new TextEncodingOptionsPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox defaultEncodingComboBox;
    private javax.swing.JLabel defaultEncodingLabel;
    private javax.swing.JPanel defaultEncodingPanel;
    private javax.swing.JPanel encodingsControlPanel;
    private javax.swing.JButton fillCurrentEncodingButton;
    private javax.swing.JButton fillCurrentEncodingsButton;
    // End of variables declaration//GEN-END:variables

    private void setModified(boolean b) {
        if (optionsModifiedListener != null) {
            optionsModifiedListener.wasModified();
        }
    }

    @Override
    public void setOptionsModifiedListener(OptionsModifiedListener optionsModifiedListener) {
        this.optionsModifiedListener = optionsModifiedListener;
    }

    private void updateEncodings() {
        encodingComboBoxModel.setAvailableEncodings(encodingPanel.getEncodingList());
        defaultEncodingComboBox.repaint();
    }

    public void setAddEncodingsOperation(TextEncodingPanel.AddEncodingsOperation addEncodingsOperation) {
        encodingPanel.setAddEncodingsOperation(addEncodingsOperation);
    }

    public class DefaultEncodingComboBoxModel implements ComboBoxModel<String> {

        private List<String> availableEncodings = new ArrayList<>();
        private String selectedEncoding = null;
        private final List<ListDataListener> dataListeners = new ArrayList<>();

        public DefaultEncodingComboBoxModel() {
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selectedEncoding = (String) anItem;
        }

        @Override
        public Object getSelectedItem() {
            return selectedEncoding;
        }

        @Override
        public int getSize() {
            return availableEncodings.size();
        }

        @Override
        public String getElementAt(int index) {
            return availableEncodings.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener listener) {
            dataListeners.add(listener);
        }

        @Override
        public void removeListDataListener(ListDataListener listener) {
            dataListeners.remove(listener);
        }

        public List<String> getAvailableEncodings() {
            return availableEncodings;
        }

        public void setAvailableEncodings(List<String> encodings) {
            availableEncodings = new ArrayList<>();
            if (encodings.isEmpty()) {
                availableEncodings.add(StringUtils.ENCODING_UTF8);
            } else {
                availableEncodings.addAll(encodings);
            }
            int position = availableEncodings.indexOf(selectedEncoding);
            selectedEncoding = availableEncodings.get(position > 0 ? position : 0);

            for (int index = 0; index < dataListeners.size(); index++) {
                ListDataListener listDataListener = dataListeners.get(index);
                listDataListener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, availableEncodings.size()));
            }
        }

        public String getSelectedEncoding() {
            return selectedEncoding;
        }

        public void setSelectedEncoding(String selectedEncoding) {
            this.selectedEncoding = selectedEncoding;
        }
    }
}
