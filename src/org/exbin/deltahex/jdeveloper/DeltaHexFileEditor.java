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

import com.sun.istack.internal.NotNull;

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
import oracle.ide.config.Preferences;

import org.exbin.deltahex.*;
import org.exbin.deltahex.delta.DeltaDocument;
import org.exbin.deltahex.delta.FileDataSource;
import org.exbin.deltahex.delta.SegmentsRepository;
import org.exbin.deltahex.highlight.swing.HighlightCodeAreaPainter;
import org.exbin.deltahex.highlight.swing.HighlightNonAsciiCodeAreaPainter;
import org.exbin.deltahex.jdeveloper.panel.DeltaHexOptionsPanelBorder;
import org.exbin.deltahex.jdeveloper.panel.HexSearchPanel;
import org.exbin.deltahex.jdeveloper.panel.HexSearchPanelApi;
import org.exbin.deltahex.jdeveloper.panel.ValuesPanel;
import org.exbin.deltahex.operation.BinaryDataCommand;
import org.exbin.deltahex.operation.BinaryDataOperationException;
import org.exbin.deltahex.operation.swing.CodeAreaOperationCommandHandler;
import org.exbin.deltahex.operation.swing.CodeAreaUndoHandler;
import org.exbin.deltahex.operation.swing.command.InsertDataCommand;
import org.exbin.deltahex.operation.undo.BinaryDataUndoUpdateListener;
import org.exbin.deltahex.swing.CodeArea;
import org.exbin.deltahex.swing.CodeAreaSpace;
import org.exbin.framework.deltahex.CodeAreaPopupMenuHandler;
import org.exbin.framework.deltahex.HexStatusApi;
import org.exbin.framework.deltahex.panel.HexStatusPanel;
import org.exbin.framework.deltahex.panel.ReplaceParameters;
import org.exbin.framework.deltahex.panel.SearchCondition;
import org.exbin.framework.deltahex.panel.SearchParameters;
import org.exbin.framework.editor.text.TextEncodingStatusApi;
import org.exbin.framework.editor.text.panel.TextFontOptionsPanel;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.utils.handler.OptionsControlHandler;
import org.exbin.framework.gui.utils.panel.OptionsControlPanel;
import org.exbin.utils.binary_data.BinaryData;
import org.exbin.utils.binary_data.ByteArrayData;
import org.exbin.utils.binary_data.EditableBinaryData;
import org.exbin.utils.binary_data.PagedData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.Properties;

import oracle.javatools.data.HashStructure;

import org.exbin.utils.binary_data.BinaryData;
import org.exbin.utils.binary_data.ByteArrayData;

public class DeltaHexFileEditor extends Editor {

    public static final String PREFERENCES_MEMORY_DELTA_MODE = "deltaMode";
    public static final String PREFERENCES_CODE_TYPE = "codeType";
    public static final String PREFERENCES_LINE_WRAPPING = "lineWrapping";
    public static final String PREFERENCES_SHOW_UNPRINTABLES = "showNonpritables";
    public static final String PREFERENCES_ENCODING_SELECTED = "selectedEncoding";
    public static final String PREFERENCES_ENCODING_PREFIX = "textEncoding.";
    public static final String PREFERENCES_BYTES_PER_LINE = "bytesPerLine";
    public static final String PREFERENCES_SHOW_HEADER = "showHeader";
    public static final String PREFERENCES_HEADER_SPACE_TYPE = "headerSpaceType";
    public static final String PREFERENCES_HEADER_SPACE = "headerSpace";
    public static final String PREFERENCES_SHOW_LINE_NUMBERS = "showLineNumbers";
    public static final String PREFERENCES_LINE_NUMBERS_LENGTH_TYPE = "lineNumbersLengthType";
    public static final String PREFERENCES_LINE_NUMBERS_LENGTH = "lineNumbersLength";
    public static final String PREFERENCES_LINE_NUMBERS_SPACE_TYPE = "lineNumbersSpaceType";
    public static final String PREFERENCES_LINE_NUMBERS_SPACE = "lineNumbersSpace";
    public static final String PREFERENCES_VIEW_MODE = "viewMode";
    public static final String PREFERENCES_BACKGROUND_MODE = "backgroundMode";
    public static final String PREFERENCES_PAINT_LINE_NUMBERS_BACKGROUND = "showLineNumbersBackground";
    public static final String PREFERENCES_POSITION_CODE_TYPE = "positionCodeType";
    public static final String PREFERENCES_HEX_CHARACTERS_CASE = "hexCharactersCase";
    public static final String PREFERENCES_DECORATION_HEADER_LINE = "decorationHeaderLine";
    public static final String PREFERENCES_DECORATION_PREVIEW_LINE = "decorationPreviewLine";
    public static final String PREFERENCES_DECORATION_BOX = "decorationBox";
    public static final String PREFERENCES_DECORATION_LINENUM_LINE = "decorationLineNumLine";
    public static final String PREFERENCES_BYTE_GROUP_SIZE = "byteGroupSize";
    public static final String PREFERENCES_SPACE_GROUP_SIZE = "spaceGroupSize";
    public static final String PREFERENCES_CODE_COLORIZATION = "codeColorization";
    public static final String PREFERENCES_SHOW_VALUES_PANEL = "valuesPanel";

    private Preferences preferences;
    private JPanel editorPanel;
    private JPanel headerPanel;
    private static SegmentsRepository segmentsRepository = null;
    private final CodeArea codeArea;
    private final CodeAreaUndoHandler undoHandler;
    private final int metaMask;
    private final PropertyChangeSupport propertyChangeSupport;

    private HexStatusPanel statusPanel;
    private HexStatusApi hexStatus;
    private TextEncodingStatusApi encodingStatus;
    private CharsetChangeListener charsetChangeListener = null;
    private GoToHandler goToHandler;
    private EncodingsHandler encodingsHandler;
    private boolean findTextPanelVisible = false;
    private HexSearchPanel hexSearchPanel = null;
    private JScrollPane valuesPanelScrollPane = null;
    private ValuesPanel valuesPanel = null;
    private boolean valuesPanelVisible = false;

    private boolean opened = false;
    private boolean deltaMemoryMode = true;
    private String displayName;
    private long documentOriginalSize;

    public DeltaHexFileEditor() {
        // Project project
//        this.project = project;
        editorPanel = new JPanel();
        initComponents();

        preferences = getPreferences();

        codeArea = new CodeArea();
        codeArea.setEditable(false);
        codeArea.setPainter(new HighlightNonAsciiCodeAreaPainter(codeArea));
        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        codeArea.getCaret().setBlinkRate(300);
        statusPanel = new HexStatusPanel();
        registerEncodingStatus(statusPanel);
        encodingsHandler = new EncodingsHandler(new TextEncodingStatusApi() {
            @Override
            public String getEncoding() {
                return encodingStatus.getEncoding();
            }

            @Override
            public void setEncoding(String encodingName) {
                codeArea.setCharset(Charset.forName(encodingName));
                encodingStatus.setEncoding(encodingName);
                preferences.getProperties().putString(DeltaHexFileEditor.PREFERENCES_ENCODING_SELECTED, encodingName);
                try {
                    preferences.save();
                } catch (IOException e) {
                }
            }
        });

        propertyChangeSupport = new PropertyChangeSupport(this);
        // CodeAreaUndoHandler(codeArea);
        // undoHandler = new HexUndoIntelliJHandler(codeArea, project, this);
        undoHandler = new CodeAreaUndoHandler(codeArea);
        loadFromPreferences();

        undoHandler.addUndoUpdateListener(new BinaryDataUndoUpdateListener() {
            @Override
            public void undoCommandPositionChanged() {
                codeArea.repaint();
                updateUndoState();
                notifyModified();
            }

            @Override
            public void undoCommandAdded(final BinaryDataCommand command) {
                updateUndoState();
                notifyModified();
            }
        });
        updateUndoState();

        getSegmentsRepository();
        setNewData();
        CodeAreaOperationCommandHandler commandHandler = new CodeAreaOperationCommandHandler(codeArea, undoHandler);
        codeArea.setCommandHandler(commandHandler);
        editorPanel.add(codeArea, BorderLayout.CENTER);
        editorPanel.add(statusPanel, BorderLayout.SOUTH);
        registerHexStatus(statusPanel);
        goToHandler = new GoToHandler(codeArea);

        codeArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = createContextMenu();
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        codeTypeComboBox.setSelectedIndex(codeArea.getCodeType().ordinal());

        editorPanel.getActionMap().put("copy-to-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.copy();
            }
        });
        editorPanel.getActionMap().put("cut-to-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.cut();
            }
        });
        editorPanel.getActionMap().put("paste-from-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.paste();
            }
        });

        applyFromCodeArea();

        int metaMaskValue;
        try {
            metaMaskValue = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        } catch (java.awt.HeadlessException ex) {
            metaMaskValue = java.awt.Event.CTRL_MASK;
        }

        metaMask = metaMaskValue;

        codeArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int modifiers = keyEvent.getModifiers();
                if (modifiers == metaMask) {
                    int keyCode = keyEvent.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.VK_F: {
                            showSearchPanel(false);
                            break;
                        }
                        case KeyEvent.VK_G: {
                            goToHandler.getGoToLineAction().actionPerformed(null);
                            break;
                        }
                        case KeyEvent.VK_S: {
                            saveFileButtonActionPerformed(null);
                            break;
                        }
                    }
                }

                if (modifiers == InputEvent.CTRL_MASK && keyEvent.getKeyCode() == KeyEvent.VK_Z) {
                    try {
                        if (undoHandler.canUndo()) {
                            undoHandler.performUndo();
                        }
                    } catch (BinaryDataOperationException e) {
                        e.printStackTrace();
                    }
                } else if (modifiers == (InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK) && keyEvent.getKeyCode() == KeyEvent.VK_Z) {
                    try {
                        if (undoHandler.canRedo()) {
                            undoHandler.performRedo();
                        }
                    } catch (BinaryDataOperationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        MessageBus messageBus = project.getMessageBus();
//        MessageBusConnection connect = messageBus.connect();
//        connect.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
//            @Override
//            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//            }
//
//            @Override
//            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//                if (virtualFile != null) {
//                    if (!releaseFile()) {
//                        // TODO Intercept close event instead of editor recreation
//                        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, virtualFile, 0);
//                        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
//                        List<FileEditor> editors = fileEditorManager.openEditor(descriptor, true);
//                        fileEditorManager.setSelectedEditor(virtualFile, DeltaHexWindowProvider.DELTAHEX_EDITOR_TYPE_ID);
//                        for (FileEditor fileEditor : editors) {
//                            if (fileEditor instanceof DeltaHexFileEditor) {
//                                ((DeltaHexFileEditor) fileEditor).reopenFile(virtualFile, codeArea.getData(), undoHandler);
//                            }
//                        }
//                        closeData(false);
//                    } else {
//                        closeData(true);
//                    }
//                }
//
//                virtualFile = null;
//            }
//
//            @Override
//            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
//            }
//        });
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
                getGUI();
                codeArea.setData(data);
            } catch (IOException ex) {
                // TODO
            }
//        }
    }

    public Component getGUI() {
        return editorPanel;
    }

    @Override
    public void update(Object object, UpdateMessage updateMessage) {
    }

    public static Preferences getPreferences() {
        return Preferences.getPreferences();
    }

    private JComboBox<String> codeTypeComboBox;
    private javax.swing.JToolBar controlToolBar;
    private javax.swing.JPanel infoToolbar;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JButton saveFileButton;
    private javax.swing.JButton undoEditButton;
    private javax.swing.JButton redoEditButton;
    private javax.swing.JToggleButton lineWrappingToggleButton;
    private javax.swing.JToggleButton showUnprintablesToggleButton;

    private void initComponents() {
        infoToolbar = new javax.swing.JPanel();
        controlToolBar = new javax.swing.JToolBar();
        saveFileButton = new javax.swing.JButton();
        undoEditButton = new javax.swing.JButton();
        redoEditButton = new javax.swing.JButton();
        lineWrappingToggleButton = new javax.swing.JToggleButton();
        showUnprintablesToggleButton = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        codeTypeComboBox = new JComboBox<>();

        editorPanel.setLayout(new java.awt.BorderLayout());

        controlToolBar.setBorder(null);
        controlToolBar.setFloatable(false);
        controlToolBar.setRollover(true);

        saveFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/deltahex/jdeveloper/resources/icons/document-save.png")));
        saveFileButton.setToolTipText("Save current file");
        saveFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileButtonActionPerformed(evt);
            }
        });
        saveFileButton.setEnabled(false);
        controlToolBar.add(saveFileButton);
        controlToolBar.add(jSeparator1);

        undoEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/deltahex/jdeveloper/resources/icons/edit-undo.png")));
        undoEditButton.setToolTipText("Undo last operation");
        undoEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoEditButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(undoEditButton);

        redoEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/deltahex/jdeveloper/resources/icons/edit-redo.png")));
        redoEditButton.setToolTipText("Redo last undid operation");
        redoEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoEditButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(redoEditButton);
        controlToolBar.add(jSeparator2);

        lineWrappingToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/deltahex/jdeveloper/resources/icons/deltahex-linewrap.png")));
        lineWrappingToggleButton.setToolTipText("Wrap line to window size");
        lineWrappingToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineWrappingToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(lineWrappingToggleButton);

        showUnprintablesToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/deltahex/jdeveloper/resources/icons/insert-pilcrow.png")));
        showUnprintablesToggleButton.setToolTipText("Show symbols for unprintable/whitespace characters");
        showUnprintablesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showUnprintablesToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(showUnprintablesToggleButton);
        controlToolBar.add(jSeparator3);

        JPanel spacePanel = new JPanel();
        spacePanel.setLayout(new BorderLayout());
        codeTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"BIN", "OCT", "DEC", "HEX"}));
        codeTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeTypeComboBoxActionPerformed(evt);
            }
        });
        spacePanel.add(codeTypeComboBox, BorderLayout.WEST);
        controlToolBar.add(spacePanel);

        javax.swing.GroupLayout infoToolbarLayout = new javax.swing.GroupLayout(infoToolbar);
        infoToolbar.setLayout(infoToolbarLayout);
        infoToolbarLayout.setHorizontalGroup(
                infoToolbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoToolbarLayout.createSequentialGroup()
                                .addComponent(controlToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))
        );
        infoToolbarLayout.setVerticalGroup(
                infoToolbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(controlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, Short.MAX_VALUE)
        );

        headerPanel = new JPanel();
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(infoToolbar, java.awt.BorderLayout.CENTER);
        editorPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
    }

    public JComponent getComponent() {
        return editorPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return editorPanel;
    }

    public String getName() {
        return displayName;
    }

    public boolean isModified() {
        return undoHandler.getCommandPosition() != undoHandler.getSyncPoint();
    }

    private void setNewData() {
        if (deltaMemoryMode) {
            codeArea.setData(segmentsRepository.createDocument());
        } else {
            codeArea.setData(new PagedData());
        }
    }

    /**
     * Attempts to release current file and warn if document was modified.
     *
     * @return true if successful
     */
    public boolean releaseFile() {

//        if (virtualFile == null) {
//            return true;
//        }
//
//        while (isModified()) {
//            Object[] options = {
//                    "Save",
//                    "Discard",
//                    "Cancel"
//            };
//            int result = JOptionPane.showOptionDialog(editorPanel,
//                    "Document was modified! Do you wish to save it?",
//                    "Save File?",
//                    JOptionPane.YES_NO_CANCEL_OPTION,
//                    JOptionPane.QUESTION_MESSAGE,
//                    null, options, options[0]);
//            if (result == JOptionPane.NO_OPTION) {
//                return true;
//            }
//            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
//                return false;
//            }
//
//            try {
//                saveFile(virtualFile);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }

        return true;
    }

    private void applyFromCodeArea() {
        codeTypeComboBox.setSelectedIndex(codeArea.getCodeType().ordinal());
        showUnprintablesToggleButton.setSelected(codeArea.isShowUnprintableCharacters());
        lineWrappingToggleButton.setSelected(codeArea.isWrapMode());
    }

    public void registerHexStatus(HexStatusApi hexStatusApi) {
        this.hexStatus = hexStatusApi;
        codeArea.addCaretMovedListener(new CaretMovedListener() {
            @Override
            public void caretMoved(CaretPosition caretPosition, Section section) {
                String position = String.valueOf(caretPosition.getDataPosition());
                position += ":" + caretPosition.getCodeOffset();
                hexStatus.setCursorPosition(position);
            }
        });

        codeArea.addEditationModeChangedListener(new EditationModeChangedListener() {
            @Override
            public void editationModeChanged(EditationMode mode) {
                hexStatus.setEditationMode(mode);
            }
        });
        hexStatus.setEditationMode(codeArea.getEditationMode());

        hexStatus.setControlHandler(new HexStatusApi.StatusControlHandler() {
            @Override
            public void changeEditationMode(EditationMode editationMode) {
                codeArea.setEditationMode(editationMode);
            }

            @Override
            public void changeCursorPosition() {
                goToHandler.getGoToLineAction().actionPerformed(null);
            }

            @Override
            public void cycleEncodings() {
                if (encodingsHandler != null) {
                    encodingsHandler.cycleEncodings();
                }
            }

            @Override
            public void popupEncodingsMenu(MouseEvent mouseEvent) {
                if (encodingsHandler != null) {
                    encodingsHandler.popupEncodingsMenu(mouseEvent);
                }
            }

            @Override
            public void changeMemoryMode(HexStatusApi.MemoryMode memoryMode) {
                boolean newDeltaMode = memoryMode == HexStatusApi.MemoryMode.DELTA_MODE;
                switchDeltaMemoryMode(newDeltaMode);
                HashStructure properties = preferences.getProperties();
                properties.putBoolean(DeltaHexFileEditor.PREFERENCES_MEMORY_DELTA_MODE, deltaMemoryMode);
                try {
                    preferences.save();
                } catch (IOException e) {
                }
            }
        });
    }

    private void switchDeltaMemoryMode(boolean newDeltaMode) {
//        if (newDeltaMode != deltaMemoryMode) {
//            // Switch memory mode
//            if (virtualFile != null) {
//                // If document is connected to file, attempt to release first if modified and then simply reload
//                if (isModified()) {
//                    if (releaseFile()) {
//                        deltaMemoryMode = newDeltaMode;
//                        openFile(virtualFile);
//                        codeArea.clearSelection();
//                        codeArea.setCaretPosition(0);
//                    }
//                } else {
//                    deltaMemoryMode = newDeltaMode;
//                    openFile(virtualFile);
//                }
//            } else {
//                // If document unsaved in memory, switch data in code area
//                if (codeArea.getData() instanceof DeltaDocument) {
//                    PagedData data = new PagedData();
//                    data.insert(0, codeArea.getData());
//                    codeArea.setData(data);
//                    codeArea.getData().dispose();
//                } else {
//                    BinaryData oldData = codeArea.getData();
//                    DeltaDocument document = segmentsRepository.createDocument();
//                    document.insert(0, oldData);
//                    codeArea.setData(document);
//                    oldData.dispose();
//                }
//                undoHandler.clear();
//                codeArea.notifyDataChanged();
//                updateCurrentMemoryMode();
//                deltaMemoryMode = newDeltaMode;
//            }
//            deltaMemoryMode = newDeltaMode;
//        }
    }

    private void closeData(boolean closeFileSource) {
//        BinaryData data = codeArea.getData();
//        codeArea.setData(new ByteArrayData());
//        if (data instanceof DeltaDocument) {
//            FileDataSource fileSource = ((DeltaDocument) data).getFileSource();
//            data.dispose();
//            if (closeFileSource) {
//                segmentsRepository.detachFileSource(fileSource);
//                segmentsRepository.closeFileSource(fileSource);
//            }
//        } else {
//            data.dispose();
//        }
//
//        virtualFile = null;
    }

    public void registerEncodingStatus(TextEncodingStatusApi encodingStatusApi) {
        this.encodingStatus = encodingStatusApi;
        setCharsetChangeListener(new CharsetChangeListener() {
            @Override
            public void charsetChanged() {
                String selectedEncoding = codeArea.getCharset().name();
                encodingStatus.setEncoding(selectedEncoding);
            }
        });
    }

    public void setCharsetChangeListener(CharsetChangeListener charsetChangeListener) {
        this.charsetChangeListener = charsetChangeListener;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void notifyModified() {
        boolean modified = undoHandler.getCommandPosition() != undoHandler.getSyncPoint();
        // TODO: Trying to force "modified behavior"
    //        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
    //        if (document instanceof DocumentEx) {
    //            ((DocumentEx) document).setModificationStamp(LocalTimeCounter.currentTime());
    //        }
    //        propertyChangeSupport.firePropertyChange(FileEditor.PROP_MODIFIED, !modified, modified);

        saveFileButton.setEnabled(modified);
    }

    private void updateUndoState() {
        undoEditButton.setEnabled(undoHandler.canUndo());
        redoEditButton.setEnabled(undoHandler.canRedo());
    }

    private void saveFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
//        Application application = ApplicationManager.getApplication();
//        application.runWriteAction(new Runnable() {
//            @Override
//            public void run() {
//                BinaryData data = codeArea.getData();
//                if (data instanceof DeltaDocument) {
//                    try {
//                        segmentsRepository.saveDocument((DeltaDocument) data);
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                } else {
//                    try (OutputStream stream = virtualFile.getOutputStream(this)) {
//                        codeArea.getData().saveToStream(stream);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                undoHandler.setSyncPoint();
//                updateUndoState();
//                saveFileButton.setEnabled(false);
//            }
//        });
    }

    private void undoEditButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            undoHandler.performUndo();
            codeArea.repaint();
            updateUndoState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redoEditButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            undoHandler.performRedo();
            codeArea.repaint();
            updateUndoState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lineWrappingToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        codeArea.setWrapMode(lineWrappingToggleButton.isSelected());
        HashStructure properties = preferences.getProperties();
        properties.putBoolean(DeltaHexFileEditor.PREFERENCES_LINE_WRAPPING, lineWrappingToggleButton.isSelected());
        try {
            preferences.save();
        } catch (IOException e) {
        }
    }

    private void showUnprintablesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        codeArea.setShowUnprintableCharacters(showUnprintablesToggleButton.isSelected());
        HashStructure properties = preferences.getProperties();
        properties.putBoolean(DeltaHexFileEditor.PREFERENCES_SHOW_UNPRINTABLES, lineWrappingToggleButton.isSelected());
        try {
            preferences.save();
        } catch (IOException e) {
        }
    }

    private void codeTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        CodeType codeType = CodeType.values()[codeTypeComboBox.getSelectedIndex()];
        codeArea.setCodeType(codeType);
        HashStructure properties = preferences.getProperties();
        properties.putString(DeltaHexFileEditor.PREFERENCES_CODE_TYPE, codeType.name());
        try {
            preferences.save();
        } catch (IOException e) {
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

//    public void openFile(DeltaHexVirtualFile virtualFile) {
//        if (!virtualFile.isDirectory() && virtualFile.isValid()) {
//            this.virtualFile = virtualFile;
//            boolean editable = virtualFile.isWritable();
//            File file = new File(virtualFile.getPath());
//            if (file.isFile() && file.exists()) {
//                try {
//                    codeArea.setEditable(editable);
//                    BinaryData oldData = codeArea.getData();
//                    if (deltaMemoryMode) {
//                        FileDataSource fileSource = segmentsRepository.openFileSource(file, editable ? FileDataSource.EditationMode.READ_WRITE : FileDataSource.EditationMode.READ_ONLY);
//                        DeltaDocument document = segmentsRepository.createDocument(fileSource);
//                        codeArea.setData(document);
//                        oldData.dispose();
//                    } else {
//                        try (FileInputStream fileStream = new FileInputStream(file)) {
//                            BinaryData data = codeArea.getData();
//                            if (!(data instanceof PagedData)) {
//                                data = new PagedData();
//                                oldData.dispose();
//                            }
//                            ((EditableBinaryData) data).loadFromStream(fileStream);
//                            codeArea.setData(data);
//                        }
//                    }
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            } else {
//                try (InputStream stream = virtualFile.getInputStream()) {
//                    if (stream != null) {
//                        codeArea.setEditable(editable);
//                        if (codeArea.getData() instanceof DeltaDocument) {
//                            ((DeltaDocument) codeArea.getData()).dispose();
//                            codeArea.setData(new PagedData());
//                        }
//                        ((EditableBinaryData) codeArea.getData()).loadFromStream(stream);
//                    }
//                } catch (IOException ex) {
//                    // Exceptions.printStackTrace(ex);
//                }
//            }
//
//            opened = true;
//            documentOriginalSize = codeArea.getDataSize();
//            updateCurrentDocumentSize();
//            updateCurrentMemoryMode();
//            undoHandler.clear();
//        }
//    }

//    public void saveFile(DeltaHexVirtualFile virtualFile) throws IOException {
//        BinaryData data = codeArea.getData();
//        if (data instanceof DeltaDocument) {
//            segmentsRepository.saveDocument((DeltaDocument) data);
//            undoHandler.setSyncPoint();
//        } else {
//            try (OutputStream stream = virtualFile.getOutputStream(this)) {
//                codeArea.getData().saveToStream(stream);
//                stream.flush();
//                undoHandler.setSyncPoint();
//                updateUndoState();
//                saveFileButton.setEnabled(false);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        notifyModified();
//        documentOriginalSize = codeArea.getDataSize();
//        updateCurrentDocumentSize();
//        updateCurrentMemoryMode();
//    }
//
//    private void reopenFile(@NotNull DeltaHexVirtualFile virtualFile, @NotNull BinaryData data, @NotNull CodeAreaUndoHandler undoHandler) {
//        this.virtualFile = virtualFile;
//        boolean editable = virtualFile.isWritable();
//        codeArea.setEditable(editable);
//
//        switchDeltaMemoryMode(data instanceof DeltaDocument);
//        if (data instanceof DeltaDocument) {
//            DeltaDocument document = (DeltaDocument) codeArea.getData();
//            document.setFileSource(((DeltaDocument) data).getFileSource());
//        }
//
//        opened = true;
//        documentOriginalSize = codeArea.getDataSize();
//        updateCurrentDocumentSize();
//        updateCurrentMemoryMode();
//
//        this.undoHandler.clear();
//        // TODO migrate undo
//        try {
//            this.undoHandler.execute(new InsertDataCommand(codeArea, 0, (EditableBinaryData) data));
//        } catch (BinaryDataOperationException e) {
//            e.printStackTrace();
//        }
//    }

    private void updateCurrentDocumentSize() {
        long dataSize = codeArea.getData().getDataSize();
        long difference = dataSize - documentOriginalSize;
        hexStatus.setCurrentDocumentSize(dataSize + " (" + (difference > 0 ? "+" + difference : difference) + ")");
    }

    public boolean isDeltaMemoryMode() {
        return deltaMemoryMode;
    }

    public void setDeltaMemoryMode(boolean deltaMemoryMode) {
        this.deltaMemoryMode = deltaMemoryMode;
    }

    private void updateCurrentMemoryMode() {
        HexStatusApi.MemoryMode memoryMode = HexStatusApi.MemoryMode.RAM_MEMORY;
        if (codeArea.getEditationAllowed() == EditationAllowed.READ_ONLY) {
            memoryMode = HexStatusApi.MemoryMode.READ_ONLY;
        } else if (codeArea.getData() instanceof DeltaDocument) {
            memoryMode = HexStatusApi.MemoryMode.DELTA_MODE;
        }

        if (hexStatus != null) {
            hexStatus.setMemoryMode(memoryMode);
        }
    }

    public static synchronized SegmentsRepository getSegmentsRepository() {
        if (segmentsRepository == null) {
            segmentsRepository = new SegmentsRepository();
        }

        return segmentsRepository;
    }

    private JPopupMenu createContextMenu() {
        final JPopupMenu result = new JPopupMenu();

        final JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, metaMask));
        cutMenuItem.setEnabled(codeArea.hasSelection() && codeArea.isEditable());
        cutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.cut();
                result.setVisible(false);
            }
        });
        result.add(cutMenuItem);

        final JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, metaMask));
        copyMenuItem.setEnabled(codeArea.hasSelection());
        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.copy();
                result.setVisible(false);
            }
        });
        result.add(copyMenuItem);

        final JMenuItem copyAsCodeMenuItem = new JMenuItem("Copy as Code");
        copyAsCodeMenuItem.setEnabled(codeArea.hasSelection());
        copyAsCodeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.copyAsCode();
                result.setVisible(false);
            }
        });
        result.add(copyAsCodeMenuItem);

        final JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, metaMask));
        pasteMenuItem.setEnabled(codeArea.canPaste() && codeArea.isEditable());
        pasteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.paste();
                result.setVisible(false);
            }
        });
        result.add(pasteMenuItem);

        final JMenuItem pasteFromCodeMenuItem = new JMenuItem("Paste from Code");
        pasteFromCodeMenuItem.setEnabled(codeArea.canPaste() && codeArea.isEditable());
        pasteFromCodeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    codeArea.pasteFromCode();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(editorPanel, ex.getMessage(), "Unable to Paste Code", JOptionPane.ERROR_MESSAGE);
                }
                result.setVisible(false);
            }
        });
        result.add(pasteFromCodeMenuItem);

        final JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteMenuItem.setEnabled(codeArea.hasSelection() && codeArea.isEditable());
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.delete();
                result.setVisible(false);
            }
        });
        result.add(deleteMenuItem);
        result.addSeparator();

        final JMenuItem selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, metaMask));
        selectAllMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.selectAll();
                result.setVisible(false);
            }
        });
        result.add(selectAllMenuItem);
        result.addSeparator();

        final JMenuItem goToMenuItem = new JMenuItem("Go To...");
        goToMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, metaMask));
        goToMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToHandler.getGoToLineAction().actionPerformed(null);
            }
        });
        result.add(goToMenuItem);

        final JMenuItem findMenuItem = new JMenuItem("Find...");
        findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, metaMask));
        findMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchPanel(false);
            }
        });
        result.add(findMenuItem);

        final JMenuItem replaceMenuItem = new JMenuItem("Replace...");
        replaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, metaMask));
        replaceMenuItem.setEnabled(codeArea.isEditable());
        replaceMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchPanel(true);
            }
        });
        result.add(replaceMenuItem);
        result.addSeparator();
        final JMenuItem optionsMenuItem = new JMenuItem("Options...");
        optionsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final DeltaHexOptionsPanelBorder optionsPanel = new DeltaHexOptionsPanelBorder();
                optionsPanel.setFromCodeArea(codeArea);
                optionsPanel.setShowValuesPanel(valuesPanelVisible);
                optionsPanel.setPreferredSize(new Dimension(640, 480));
                OptionsControlPanel optionsControlPanel = new OptionsControlPanel();
                JPanel dialogPanel = WindowUtils.createDialogPanel(optionsPanel, optionsControlPanel);
                final Dialog dialog = DialogUtils.createDialog(editorPanel, dialogPanel, "Options");
                WindowUtils.assignGlobalKeyListener(dialogPanel, optionsControlPanel.createOkCancelListener());
                optionsControlPanel.setHandler(new OptionsControlHandler() {
                    @Override
                    public void controlActionPerformed(OptionsControlHandler.ControlActionType actionType) {
                        if (actionType == OptionsControlHandler.ControlActionType.SAVE) {
                            optionsPanel.store();
                        }
                        if (actionType != OptionsControlHandler.ControlActionType.CANCEL) {
                            optionsPanel.applyToCodeArea(codeArea);
                            boolean applyShowValuesPanel = optionsPanel.isShowValuesPanel();
                            if (applyShowValuesPanel) {
                                showValuesPanel();
                            } else {
                                hideValuesPanel();
                            }
                            applyFromCodeArea();
                            switchDeltaMemoryMode(optionsPanel.isDeltaMemoryMode());
                            codeArea.repaint();
                        }

                        dialog.setVisible(false);
                    }
                });
                dialog.setSize(650, 460);
                dialog.setVisible(true);
            }
        });
        result.add(optionsMenuItem);

        return result;
    }

    public void showSearchPanel(boolean replace) {
        if (hexSearchPanel == null) {
            hexSearchPanel = new HexSearchPanel(new HexSearchPanelApi() {
                @Override
                public void performFind(SearchParameters searchParameters) {
                    HighlightCodeAreaPainter painter = (HighlightCodeAreaPainter) codeArea.getPainter();
                    SearchCondition condition = searchParameters.getCondition();
                    hexSearchPanel.clearStatus();
                    if (condition.isEmpty()) {
                        painter.clearMatches();
                        codeArea.repaint();
                        return;
                    }

                    long position;
                    if (searchParameters.isSearchFromCursor()) {
                        position = codeArea.getCaretPosition().getDataPosition();
                    } else {
                        switch (searchParameters.getSearchDirection()) {
                            case FORWARD: {
                                position = 0;
                                break;
                            }
                            case BACKWARD: {
                                position = codeArea.getDataSize() - 1;
                                break;
                            }
                            default:
                                throw new IllegalStateException("Illegal search type " + searchParameters.getSearchDirection().name());
                        }
                    }
                    searchParameters.setStartPosition(position);

                    switch (condition.getSearchMode()) {
                        case TEXT: {
                            searchForText(searchParameters);
                            break;
                        }
                        case BINARY: {
                            searchForBinaryData(searchParameters);
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unexpected search mode " + condition.getSearchMode().name());
                    }
                }

                @Override
                public void setMatchPosition(int matchPosition) {
                    HighlightCodeAreaPainter painter = (HighlightCodeAreaPainter) codeArea.getPainter();
                    painter.setCurrentMatchIndex(matchPosition);
                    HighlightCodeAreaPainter.SearchMatch currentMatch = painter.getCurrentMatch();
                    codeArea.revealPosition(currentMatch.getPosition(), codeArea.getActiveSection());
                    codeArea.repaint();
                }

                @Override
                public void updatePosition() {
                    hexSearchPanel.updatePosition(codeArea.getCaretPosition().getDataPosition(), codeArea.getDataSize());
                }

                @Override
                public void performReplace(SearchParameters searchParameters, ReplaceParameters replaceParameters) {
                    SearchCondition replaceCondition = replaceParameters.getCondition();
                    HighlightCodeAreaPainter painter = (HighlightCodeAreaPainter) codeArea.getPainter();
                    HighlightCodeAreaPainter.SearchMatch currentMatch = painter.getCurrentMatch();
                    if (currentMatch != null) {
                        EditableBinaryData editableData = ((EditableBinaryData) codeArea.getData());
                        editableData.remove(currentMatch.getPosition(), currentMatch.getLength());
                        if (replaceCondition.getSearchMode() == SearchCondition.SearchMode.BINARY) {
                            editableData.insert(currentMatch.getPosition(), replaceCondition.getBinaryData());
                        } else {
                            editableData.insert(currentMatch.getPosition(), replaceCondition.getSearchText().getBytes(codeArea.getCharset()));
                        }
                        painter.getMatches().remove(currentMatch);
                        codeArea.repaint();
                    }
                }

                @Override
                public void clearMatches() {
                    HighlightCodeAreaPainter painter = (HighlightCodeAreaPainter) codeArea.getPainter();
                    painter.clearMatches();
                }
            });
            hexSearchPanel.setHexCodePopupMenuHandler(new CodeAreaPopupMenuHandler() {
                @Override
                public JPopupMenu createPopupMenu(CodeArea codeArea, String menuPostfix) {
                    return createCodeAreaPopupMenu(codeArea, menuPostfix);
                }

                @Override
                public void dropPopupMenu(String menuPostfix) {
                }
            });
            hexSearchPanel.setClosePanelListener(new HexSearchPanel.ClosePanelListener() {
                @Override
                public void panelClosed() {
                    hideSearchPanel();
                }
            });
        }

        if (!findTextPanelVisible) {
            headerPanel.add(hexSearchPanel, BorderLayout.SOUTH);
            headerPanel.revalidate();
            editorPanel.revalidate();
            editorPanel.repaint();
            findTextPanelVisible = true;
            hexSearchPanel.requestSearchFocus();
        }
        hexSearchPanel.switchReplaceMode(replace);
    }

    public void hideSearchPanel() {
        if (findTextPanelVisible) {
            hexSearchPanel.cancelSearch();
            hexSearchPanel.clearSearch();
            headerPanel.remove(hexSearchPanel);
            editorPanel.revalidate();
            editorPanel.repaint();
            findTextPanelVisible = false;
        }
    }

    public void showValuesPanel() {
        if (!valuesPanelVisible) {
            valuesPanelVisible = true;
            if (valuesPanel == null) {
                valuesPanel = new ValuesPanel();
                valuesPanel.setCodeArea(codeArea, undoHandler);
                valuesPanelScrollPane = new JScrollPane(valuesPanel);
            }
            editorPanel.add(valuesPanelScrollPane, BorderLayout.EAST);
            valuesPanel.enableUpdate();
            valuesPanel.updateValues();
            valuesPanelScrollPane.revalidate();
            valuesPanel.revalidate();
            editorPanel.revalidate();
        }
    }

    public void hideValuesPanel() {
        if (valuesPanelVisible) {
            valuesPanelVisible = false;
            valuesPanel.disableUpdate();
            editorPanel.remove(valuesPanelScrollPane);
            editorPanel.revalidate();
        }
    }

    private JPopupMenu createCodeAreaPopupMenu(final CodeArea codeArea, String menuPostfix) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem cutMenuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.cut();
            }

            @Override
            public boolean isEnabled() {
                return codeArea.hasSelection();
            }
        });
        cutMenuItem.setText("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, metaMask));
        popupMenu.add(cutMenuItem);
        JMenuItem copyMenuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.copy();
            }

            @Override
            public boolean isEnabled() {
                return codeArea.hasSelection();
            }
        });
        copyMenuItem.setText("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, metaMask));
        popupMenu.add(copyMenuItem);
        JMenuItem pasteMenuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.paste();
            }

            @Override
            public boolean isEnabled() {
                return codeArea.canPaste();
            }
        });
        pasteMenuItem.setText("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, metaMask));
        popupMenu.add(pasteMenuItem);
        JMenuItem deleteMenuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.delete();
            }

            @Override
            public boolean isEnabled() {
                return codeArea.hasSelection();
            }
        });
        deleteMenuItem.setText("Delete");
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        popupMenu.add(deleteMenuItem);
        JMenuItem selectAllMenuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.selectAll();
            }
        });
        selectAllMenuItem.setText("Select All");
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, metaMask));
        popupMenu.add(selectAllMenuItem);

        return popupMenu;
    }

    /**
     * Performs search by text/characters.
     */
    private void searchForText(SearchParameters searchParameters) {
        HighlightCodeAreaPainter painter = (HighlightCodeAreaPainter) codeArea.getPainter();
        SearchCondition condition = searchParameters.getCondition();

        long position = searchParameters.getStartPosition();
        String findText;
        if (searchParameters.isMatchCase()) {
            findText = condition.getSearchText();
        } else {
            findText = condition.getSearchText().toLowerCase();
        }
        BinaryData data = codeArea.getData();

        List<HighlightCodeAreaPainter.SearchMatch> foundMatches = new ArrayList<>();

        Charset charset = codeArea.getCharset();
        CharsetEncoder encoder = charset.newEncoder();
        int maxBytesPerChar = (int) encoder.maxBytesPerChar();
        byte[] charData = new byte[maxBytesPerChar];
        long dataSize = data.getDataSize();
        while (position <= dataSize - findText.length()) {
            int matchCharLength = 0;
            int matchLength = 0;
            while (matchCharLength < findText.length()) {
                long searchPosition = position + matchLength;
                int bytesToUse = maxBytesPerChar;
                if (searchPosition + bytesToUse > dataSize) {
                    bytesToUse = (int) (dataSize - searchPosition);
                }
                data.copyToArray(searchPosition, charData, 0, bytesToUse);
                char singleChar = new String(charData, charset).charAt(0);
                String singleCharString = String.valueOf(singleChar);
                int characterLength = singleCharString.getBytes(charset).length;

                if (searchParameters.isMatchCase()) {
                    if (singleChar != findText.charAt(matchCharLength)) {
                        break;
                    }
                } else if (singleCharString.toLowerCase().charAt(0) != findText.charAt(matchCharLength)) {
                    break;
                }
                matchCharLength++;
                matchLength += characterLength;
            }

            if (matchCharLength == findText.length()) {
                HighlightCodeAreaPainter.SearchMatch match = new HighlightCodeAreaPainter.SearchMatch();
                match.setPosition(position);
                match.setLength(matchLength);
                foundMatches.add(match);

                if (foundMatches.size() == 100 || !searchParameters.isMultipleMatches()) {
                    break;
                }
            }

            switch (searchParameters.getSearchDirection()) {
                case FORWARD: {
                    position++;
                    break;
                }
                case BACKWARD: {
                    position--;
                    break;
                }
                default:
                    throw new IllegalStateException("Illegal search type " + searchParameters.getSearchDirection().name());
            }
        }

        painter.setMatches(foundMatches);
        if (foundMatches.size() > 0) {
            painter.setCurrentMatchIndex(0);
            HighlightCodeAreaPainter.SearchMatch firstMatch = painter.getCurrentMatch();
            codeArea.revealPosition(firstMatch.getPosition(), codeArea.getActiveSection());
        }
        hexSearchPanel.setStatus(foundMatches.size(), 0);
        codeArea.repaint();
    }

    /**
     * Performs search by binary data.
     */
    private void searchForBinaryData(SearchParameters searchParameters) {
        HighlightCodeAreaPainter painter = (HighlightCodeAreaPainter) codeArea.getPainter();
        SearchCondition condition = searchParameters.getCondition();
        long position = codeArea.getCaretPosition().getDataPosition();
        HighlightCodeAreaPainter.SearchMatch currentMatch = painter.getCurrentMatch();

        if (currentMatch != null) {
            if (currentMatch.getPosition() == position) {
                position++;
            }
            painter.clearMatches();
        } else if (!searchParameters.isSearchFromCursor()) {
            position = 0;
        }

        BinaryData searchData = condition.getBinaryData();
        BinaryData data = codeArea.getData();

        List<HighlightCodeAreaPainter.SearchMatch> foundMatches = new ArrayList<>();

        long dataSize = data.getDataSize();
        while (position < dataSize - searchData.getDataSize()) {
            int matchLength = 0;
            while (matchLength < searchData.getDataSize()) {
                if (data.getByte(position + matchLength) != searchData.getByte(matchLength)) {
                    break;
                }
                matchLength++;
            }

            if (matchLength == searchData.getDataSize()) {
                HighlightCodeAreaPainter.SearchMatch match = new HighlightCodeAreaPainter.SearchMatch();
                match.setPosition(position);
                match.setLength(searchData.getDataSize());
                foundMatches.add(match);

                if (foundMatches.size() == 100 || !searchParameters.isMultipleMatches()) {
                    break;
                }
            }

            position++;
        }

        painter.setMatches(foundMatches);
        if (foundMatches.size() > 0) {
            painter.setCurrentMatchIndex(0);
            HighlightCodeAreaPainter.SearchMatch firstMatch = painter.getCurrentMatch();
            codeArea.revealPosition(firstMatch.getPosition(), codeArea.getActiveSection());
        }
        hexSearchPanel.setStatus(foundMatches.size(), 0);
        codeArea.repaint();
    }

    private void loadFromPreferences() {
        HashStructure properties = preferences.getProperties();
        deltaMemoryMode = properties.getBoolean(DeltaHexFileEditor.PREFERENCES_MEMORY_DELTA_MODE, true);
        CodeType codeType = CodeType.valueOf(properties.getString(DeltaHexFileEditor.PREFERENCES_CODE_TYPE, "HEXADECIMAL"));
        codeArea.setCodeType(codeType);
        codeTypeComboBox.setSelectedIndex(codeType.ordinal());
        String selectedEncoding = properties.getString(DeltaHexFileEditor.PREFERENCES_ENCODING_SELECTED, "UTF-8");
        statusPanel.setEncoding(selectedEncoding);
        codeArea.setCharset(Charset.forName(selectedEncoding));
        int bytesPerLine = properties.getInt(DeltaHexFileEditor.PREFERENCES_BYTES_PER_LINE, 16);
        codeArea.setLineLength(bytesPerLine);

        boolean showNonprintables = properties.getBoolean(DeltaHexFileEditor.PREFERENCES_SHOW_UNPRINTABLES, false);
        showUnprintablesToggleButton.setSelected(showNonprintables);
        codeArea.setShowUnprintableCharacters(showNonprintables);

        boolean lineWrapping = properties.getBoolean(DeltaHexFileEditor.PREFERENCES_LINE_WRAPPING, false);
        codeArea.setWrapMode(lineWrapping);
        lineWrappingToggleButton.setSelected(lineWrapping);

        encodingsHandler.loadFromPreferences(preferences);

        // Layout
        codeArea.setShowHeader(properties.getBoolean(DeltaHexFileEditor.PREFERENCES_SHOW_HEADER, true));
        String headerSpaceTypeName = properties.getString(DeltaHexFileEditor.PREFERENCES_HEADER_SPACE_TYPE, CodeAreaSpace.SpaceType.HALF_UNIT.name());
        codeArea.setHeaderSpaceType(CodeAreaSpace.SpaceType.valueOf(headerSpaceTypeName));
        codeArea.setHeaderSpaceSize(properties.getInt(DeltaHexFileEditor.PREFERENCES_HEADER_SPACE, 0));
        codeArea.setShowLineNumbers(properties.getBoolean(DeltaHexFileEditor.PREFERENCES_SHOW_LINE_NUMBERS, true));
        String lineNumbersSpaceTypeName = properties.getString(DeltaHexFileEditor.PREFERENCES_LINE_NUMBERS_SPACE_TYPE, CodeAreaSpace.SpaceType.ONE_UNIT.name());
        codeArea.setLineNumberSpaceType(CodeAreaSpace.SpaceType.valueOf(lineNumbersSpaceTypeName));
        codeArea.setLineNumberSpaceSize(properties.getInt(DeltaHexFileEditor.PREFERENCES_LINE_NUMBERS_SPACE, 8));
        String lineNumbersLengthTypeName = properties.getString(DeltaHexFileEditor.PREFERENCES_LINE_NUMBERS_LENGTH_TYPE, CodeAreaLineNumberLength.LineNumberType.SPECIFIED.name());
        codeArea.setLineNumberType(CodeAreaLineNumberLength.LineNumberType.valueOf(lineNumbersLengthTypeName));
        codeArea.setLineNumberSpecifiedLength(properties.getInt(DeltaHexFileEditor.PREFERENCES_LINE_NUMBERS_LENGTH, 8));
        codeArea.setByteGroupSize(properties.getInt(DeltaHexFileEditor.PREFERENCES_BYTE_GROUP_SIZE, 1));
        codeArea.setSpaceGroupSize(properties.getInt(DeltaHexFileEditor.PREFERENCES_SPACE_GROUP_SIZE, 0));

        // Mode
        codeArea.setViewMode(ViewMode.valueOf(properties.getString(DeltaHexFileEditor.PREFERENCES_VIEW_MODE, ViewMode.DUAL.name())));
        codeArea.setCodeType(CodeType.valueOf(properties.getString(DeltaHexFileEditor.PREFERENCES_CODE_TYPE, CodeType.HEXADECIMAL.name())));
        ((HighlightNonAsciiCodeAreaPainter) codeArea.getPainter()).setNonAsciiHighlightingEnabled(properties.getBoolean(DeltaHexFileEditor.PREFERENCES_CODE_COLORIZATION, true));
        // Memory mode handled from outside by isDeltaMemoryMode() method, worth fixing?

        // Decoration
        codeArea.setBackgroundMode(CodeArea.BackgroundMode.valueOf(properties.getString(DeltaHexFileEditor.PREFERENCES_BACKGROUND_MODE, CodeArea.BackgroundMode.STRIPPED.name())));
        codeArea.setLineNumberBackground(properties.getBoolean(DeltaHexFileEditor.PREFERENCES_PAINT_LINE_NUMBERS_BACKGROUND, true));
        int decorationMode = (properties.getBoolean(DeltaHexFileEditor.PREFERENCES_DECORATION_HEADER_LINE, true) ? CodeArea.DECORATION_HEADER_LINE : 0)
                + (properties.getBoolean(DeltaHexFileEditor.PREFERENCES_DECORATION_PREVIEW_LINE, true) ? CodeArea.DECORATION_PREVIEW_LINE : 0)
                + (properties.getBoolean(DeltaHexFileEditor.PREFERENCES_DECORATION_BOX, false) ? CodeArea.DECORATION_BOX : 0)
                + (properties.getBoolean(DeltaHexFileEditor.PREFERENCES_DECORATION_LINENUM_LINE, true) ? CodeArea.DECORATION_LINENUM_LINE : 0);
        codeArea.setDecorationMode(decorationMode);
        codeArea.setHexCharactersCase(HexCharactersCase.valueOf(properties.getString(DeltaHexFileEditor.PREFERENCES_HEX_CHARACTERS_CASE, HexCharactersCase.UPPER.name())));
        codeArea.setPositionCodeType(PositionCodeType.valueOf(properties.getString(DeltaHexFileEditor.PREFERENCES_POSITION_CODE_TYPE, PositionCodeType.HEXADECIMAL.name())));

        // Font
        Boolean useDefaultColor = properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_DEFAULT, true);

        if (!useDefaultColor) {
            String value;
            Map<TextAttribute, Object> attribs = new HashMap<>();
            value = properties.getString(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_FAMILY, "MONOSPACED");
            if (value != null) {
                attribs.put(TextAttribute.FAMILY, value);
            }
            value = properties.getString(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_SIZE, "12");
            if (value != null) {
                attribs.put(TextAttribute.SIZE, new Integer(value).floatValue());
            }
            if (properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_UNDERLINE, false)) {
                attribs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
            }
            if (properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_STRIKETHROUGH, false)) {
                attribs.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            }
            if (properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_STRONG, false)) {
                attribs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            }
            if (Boolean.valueOf(properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_ITALIC, false))) {
                attribs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
            }
            if (Boolean.valueOf(properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_SUBSCRIPT, false))) {
                attribs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
            }
            if (Boolean.valueOf(properties.getBoolean(TextFontOptionsPanel.PREFERENCES_TEXT_FONT_SUPERSCRIPT, false))) {
                attribs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
            }
            Font derivedFont = codeArea.getFont().deriveFont(attribs);
            codeArea.setFont(derivedFont);
        }
        boolean showValuesPanel = properties.getBoolean(DeltaHexFileEditor.PREFERENCES_SHOW_VALUES_PANEL, true);
        if (showValuesPanel) {
            showValuesPanel();
        }
    }

    public static interface CharsetChangeListener {

        public void charsetChanged();
    }
}
