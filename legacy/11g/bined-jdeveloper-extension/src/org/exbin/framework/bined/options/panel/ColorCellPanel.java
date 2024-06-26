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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.border.EtchedBorder;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.utils.WindowUtils;

/**
 * Color cell panel for color profile panel.
 *
 * @version 0.2.1 2019/07/17
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ColorCellPanel extends javax.swing.JPanel {

    private final ColorHandler colorHandler;

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(ColorCellPanel.class);

    public ColorCellPanel(ColorHandler colorHandler) {
        initComponents();
        this.colorHandler = colorHandler;
        setColor(colorHandler.getColor());
    }

    private void setColor(@Nullable Color color) {
        colorLabel.setBorder(color == null ? null : new EtchedBorder());
        colorLabel.setBackground(color);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorLabel = new javax.swing.JLabel();
        colorButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        colorLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        colorLabel.setOpaque(true);

        colorButton.setText(resourceBundle.getString("editButton.text")); // NOI18N
        colorButton.setAlignmentY(0.0F);
        colorButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        colorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButtonActionPerformed(evt);
            }
        });

        clearButton.setText(resourceBundle.getString("clearButton.text")); // NOI18N
        clearButton.setAlignmentY(0.0F);
        clearButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(colorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(clearButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(colorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void colorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
        final JColorChooser colorChooser = new javax.swing.JColorChooser();
        JDialog dialog = JColorChooser.createDialog(this, "Select Color", true, colorChooser, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = colorChooser.getColor();
                setColor(color);
                colorHandler.setColor(color);
            }
        }, null);
        dialog.setVisible(true);
    }//GEN-LAST:event_colorButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        setColor(null);
        colorHandler.setColor(null);
    }//GEN-LAST:event_clearButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new ColorCellPanel(new ColorHandler() {
            @Nullable
            @Override
            public Color getColor() {
                return Color.BLACK;
            }

            @Override
            public void setColor(@Nullable Color color) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearButton;
    private javax.swing.JButton colorButton;
    private javax.swing.JLabel colorLabel;
    // End of variables declaration//GEN-END:variables

    public interface ColorHandler {

        @Nullable
        Color getColor();

        void setColor(@Nullable Color color);
    }
}
