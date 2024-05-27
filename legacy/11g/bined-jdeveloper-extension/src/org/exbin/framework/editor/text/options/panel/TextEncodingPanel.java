/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.framework.editor.text.options.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.exbin.framework.editor.text.options.impl.TextEncodingOptionsImpl;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.options.api.OptionsCapable;
import org.exbin.framework.gui.options.api.OptionsModifiedListener;

/**
 * Text encoding selection panel.
 *
 * @version 0.2.1 2019/07/20
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class TextEncodingPanel extends javax.swing.JPanel implements OptionsCapable<TextEncodingOptionsImpl> {

    private OptionsModifiedListener optionsModifiedListener;
    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(TextEncodingPanel.class);
    private AddEncodingsOperation addEncodingsOperation = null;

    public TextEncodingPanel() {
        initComponents();
        init();
    }

    private void init() {
        encodingsList.getModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                selectAllButton.setEnabled(encodingsList.getModel().getSize() > 0);
            }
        });

        encodingsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean emptySelection = encodingsList.isSelectionEmpty();
                    removeButton.setEnabled(!emptySelection);
                    selectAllButton.setEnabled(encodingsList.getModel().getSize() > 0);
                    if (!emptySelection) {
                        int[] indices = encodingsList.getSelectedIndices();
                        upButton.setEnabled(encodingsList.getMaxSelectionIndex() >= indices.length);
                        downButton.setEnabled(encodingsList.getMinSelectionIndex() + indices.length < encodingsList.getModel().getSize());
                    } else {
                        upButton.setEnabled(false);
                        downButton.setEnabled(false);
                    }
                }
            }
        });
    }

    @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public void saveToOptions(TextEncodingOptionsImpl options) {
        options.setEncodings(getEncodingList());
    }

    @Override
    public void loadFromOptions(TextEncodingOptionsImpl options) {
        setEncodingList(options.getEncodings());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        encodingsListScrollPane = new javax.swing.JScrollPane();
        encodingsList = new javax.swing.JList();
        encodingsControlPanel = new javax.swing.JPanel();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        encodingsListScrollPane.setName("encodingsListScrollPane"); // NOI18N

        encodingsList.setModel(new EncodingsListModel());
        encodingsList.setName("encodingsList"); // NOI18N
        encodingsListScrollPane.setViewportView(encodingsList);

        encodingsControlPanel.setName("encodingsControlPanel"); // NOI18N

        upButton.setText(resourceBundle.getString("upButton.text")); // NOI18N
        upButton.setEnabled(false);
        upButton.setName("upButton"); // NOI18N
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText(resourceBundle.getString("downButton.text")); // NOI18N
        downButton.setEnabled(false);
        downButton.setName("downButton"); // NOI18N
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        addButton.setText(resourceBundle.getString("addButton.text")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText(resourceBundle.getString("removeButton.text")); // NOI18N
        removeButton.setEnabled(false);
        removeButton.setName("removeButton"); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        selectAllButton.setText(resourceBundle.getString("selectAllButton.text")); // NOI18N
        selectAllButton.setEnabled(false);
        selectAllButton.setName("selectAllButton"); // NOI18N
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout encodingsControlPanelLayout = new javax.swing.GroupLayout(encodingsControlPanel);
        encodingsControlPanel.setLayout(encodingsControlPanelLayout);
        encodingsControlPanelLayout.setHorizontalGroup(
            encodingsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encodingsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(encodingsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(selectAllButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(downButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                .addContainerGap())
        );
        encodingsControlPanelLayout.setVerticalGroup(
            encodingsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encodingsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(upButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectAllButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(encodingsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encodingsControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(encodingsControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(encodingsListScrollPane)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (addEncodingsOperation != null) {
            List<String> encodings = addEncodingsOperation.run(((EncodingsListModel) encodingsList.getModel()).getCharsets());
            if (encodings != null) {
                ((EncodingsListModel) encodingsList.getModel()).addAll(encodings, encodingsList.getSelectedIndex());
                encodingsList.clearSelection();
                wasModified();
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        ((EncodingsListModel) encodingsList.getModel()).removeIndices(encodingsList.getSelectedIndices());
        encodingsList.clearSelection();
        wasModified();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int[] indices = encodingsList.getSelectedIndices();
        int last = 0;
        for (int i = 0; i < indices.length; i++) {
            int next = indices[i];
            if (last != next) {
                EncodingsListModel model = (EncodingsListModel) encodingsList.getModel();
                String item = (String) model.getElementAt(next);
                model.add(next - 1, item);
                encodingsList.getSelectionModel().addSelectionInterval(next - 1, next - 1);
                model.remove(next + 1);
            } else {
                last++;
            }
        }
        wasModified();
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int[] indices = encodingsList.getSelectedIndices();
        int last = encodingsList.getModel().getSize() - 1;
        for (int i = indices.length; i > 0; i--) {
            int next = indices[i - 1];
            if (last != next) {
                EncodingsListModel model = (EncodingsListModel) encodingsList.getModel();
                String item = (String) model.getElementAt(next);
                model.add(next + 2, item);
                encodingsList.getSelectionModel().addSelectionInterval(next + 2, next + 2);
                model.remove(next);
            } else {
                last--;
            }
        }
        wasModified();
    }//GEN-LAST:event_downButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        if (encodingsList.getSelectedIndices().length < encodingsList.getModel().getSize()) {
            encodingsList.setSelectionInterval(0, encodingsList.getModel().getSize() - 1);
        } else {
            encodingsList.clearSelection();
        }
    }//GEN-LAST:event_selectAllButtonActionPerformed

    public void setEncodingList(List<String> list) {
        ((EncodingsListModel) encodingsList.getModel()).setCharsets(list);
        encodingsList.repaint();
    }

    public List<String> getEncodingList() {
        return ((EncodingsListModel) encodingsList.getModel()).getCharsets();
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new TextEncodingPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private javax.swing.JPanel encodingsControlPanel;
    private javax.swing.JList encodingsList;
    private javax.swing.JScrollPane encodingsListScrollPane;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setOptionsModifiedListener(OptionsModifiedListener optionsModifiedListener) {
        this.optionsModifiedListener = optionsModifiedListener;
    }

    public void wasModified() {
        if (optionsModifiedListener != null) {
            optionsModifiedListener.wasModified();
        }
    }

    public void setAddEncodingsOperation(AddEncodingsOperation addEncodingsOperation) {
        this.addEncodingsOperation = addEncodingsOperation;
    }

    public void addEncodings(List<String> encodings) {
        ((EncodingsListModel) encodingsList.getModel()).addAll(encodings, encodingsList.isSelectionEmpty() ? -1 : encodingsList.getSelectedIndex());
    }

    public static interface AddEncodingsOperation {

        List<String> run(List<String> usedEncodings);
    }

    @ParametersAreNonnullByDefault
    private class EncodingsListModel extends AbstractListModel {

        private final List<String> charsets = new ArrayList<String>();

        @Override
        public int getSize() {
            return charsets.size();
        }

        @Override
        public Object getElementAt(int index) {
            return charsets.get(index);
        }

        /**
         * @return the charsets
         */
        @Nonnull
        public List<String> getCharsets() {
            return charsets;
        }

        /**
         * @param charsets the charsets to set
         */
        public void setCharsets(@Nullable List<String> charsets) {
            this.charsets.clear();
            if (charsets != null) {
                this.charsets.addAll(charsets);
            }
            fireContentsChanged(this, 0, this.charsets.size());
        }

        public void addAll(List<String> list, int pos) {
            if (pos >= 0) {
                charsets.addAll(pos, list);
                fireIntervalAdded(this, pos, list.size() + pos);
            } else {
                charsets.addAll(list);
                fireIntervalAdded(this, charsets.size() - list.size(), charsets.size());
            }
        }

        public void removeIndices(int[] indices) {
            for (int i = indices.length - 1; i >= 0; i--) {
                charsets.remove(indices[i]);
            }
            fireContentsChanged(this, 0, charsets.size());
        }

        public void remove(int index) {
            charsets.remove(index);
            fireIntervalRemoved(this, index, index);
        }

        public void add(int index, String item) {
            charsets.add(index, item);
            fireIntervalAdded(this, index, index);
        }
    }
}
