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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JScrollPane;
import javax.swing.UIManager;

import oracle.ide.Context;
import oracle.ide.editor.Editor;
import oracle.ide.model.Node;
import oracle.ide.model.UpdateMessage;

import org.exbin.utils.binary_data.BinaryData;
import org.exbin.utils.binary_data.ByteArrayData;

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

    @Override
    public void open() {
        final Context context = getContext();
        final Node genericNode = context.getNode();
/*        if (genericNode instanceof TextNode) {
            final TextNode node = (TextNode)genericNode;
            try {
                Reader reader = node.getReader();
                reader.read
            } catch (IOException ex) {
                // TODO
            }
/ *            final XmlModel xmlModel = TextNode.getXmlContext(context).getModel();
            try {
                xmlModel.acquireReadLock();
                final Document document = xmlModel.getDocument();
                ExecuteQueryUtils.registerDocumentNamespacesAndPrefixes(model,
                                                                        document);
                context.setProperty(XMLQueryModel.class.getName(), model);
            } finally {
                xmlModel.releaseReadLock();
            } * /
        } else { */
            try {
                InputStream stream = genericNode.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                int nRead;
                byte[] buffer = new byte[16384];

                while ((nRead = stream.read(buffer, 0, buffer.length)) != -1) {
                    outputStream.write(buffer, 0, nRead);
                }

                outputStream.flush();

                BinaryData data = new ByteArrayData(outputStream.toByteArray());
                _panel.setData(data);
            } catch (IOException ex) {
                // TODO
            }
            
//        }
    }

    public Component getGUI() {
        if (_panel == null)
            _panel = new DeltaHexEditorPanel();
        return _panel;
    }

    public void update(Object object, UpdateMessage updateMessage) {
        System.out.println("TEST");
    }
    
    
}
