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

import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.swing.Icon;

import oracle.ide.model.Node;

/**
 * Node used for binary data.
 * 
 * Not sure how to force JDeveloper to ignore Node so let's use fake URL.
 *
 * @author ExBin Project (http://exbin.org)
 * @version 0.2.0 2019/08/14
 */
public class BinaryNode extends Node {

    private static final String FAKE_PROTOCOL = "orgexbinbined";
    private static final URLStreamHandler FAKE_PROTOCOL_HANDLER = new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL u) {
            return FAKE_PROTOCOL.equals(u.getProtocol()) ? new FakeURLStreamHandler(u) : null;
        }
    };

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

        return wrappedNode.getIcon();
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
            return new URL(FAKE_PROTOCOL, wrappedUrl.getHost(), wrappedUrl.getPort(), wrappedUrl.getProtocol() + "/" + wrappedUrl.getFile(), FAKE_PROTOCOL_HANDLER);
        } catch (MalformedURLException e) {
            return super.getURL();
        }
    }
    
    private static class FakeURLStreamHandler extends URLConnection {
        public FakeURLStreamHandler(URL url) {
            super(url);
        }

        @Override
        public void connect() {
        }
    }
}