package org.exbin.deltahex.jdeveloper;

import javax.swing.JPanel;

public class DeltaHexEditorPanel extends JPanel {

    public DeltaHexEditorPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout( null );
    }
}
