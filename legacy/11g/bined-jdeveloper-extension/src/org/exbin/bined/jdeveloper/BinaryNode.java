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

import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.swing.Icon;

import oracle.ide.model.Node;

import oracle.javatools.icons.OracleIcons;

/**
 * Node used for binary data.
 *
 * Report as data: instead of file: so that no additional tabs will be opened.
 *
 * @author ExBin Project (http://exbin.org)
 * @version 0.2.0 2019/08/15
 */
public class BinaryNode extends Node {

    private static final String WRAPPED_PREFIX = "orgexbinbined::/"; // Prevent existence of the real file URL

    private Node wrappedNode;

    public BinaryNode(Node wrappedNode) {
        super();
        this.wrappedNode = wrappedNode;
    }
    
    public Node getWrappedNode() {
        return wrappedNode;
    }

    @Override
    public String getShortLabel() {
        if (wrappedNode == null) return super.getShortLabel();

        return wrappedNode.getShortLabel();
    }

    @Override
    public String getLongLabel() {
        if (wrappedNode == null) return super.getLongLabel();

        return wrappedNode.getLongLabel();
    }

    @Override
    public Icon getIcon() {
        if (wrappedNode == null) return super.getIcon();

        return new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/jdeveloper/resources/icons/icon.png"));
    }

    @Override
    public String getToolTipText() {
        if (wrappedNode == null) return super.getToolTipText();

        return wrappedNode.getToolTipText();
    }
    
    @Override
    public URL getURL() {
        if (wrappedNode == null) return super.getURL();

        URL wrappedUrl = wrappedNode.getURL();
        try {
            return new URL(wrappedUrl.getProtocol(), wrappedUrl.getHost(), wrappedUrl.getPort(), WRAPPED_PREFIX + wrappedUrl.getFile());
        } catch (MalformedURLException e) {
            return super.getURL();
        }
    }
}
 