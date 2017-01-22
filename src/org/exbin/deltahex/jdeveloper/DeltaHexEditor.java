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
