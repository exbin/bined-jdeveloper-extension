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
package org.exbin.bined.swing.basic;

import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.UIManager;
import org.exbin.bined.BasicCodeAreaSection;
import org.exbin.bined.BasicCodeAreaZone;
import org.exbin.bined.CaretMovedListener;
import org.exbin.bined.DefaultCodeAreaCaretPosition;
import org.exbin.bined.CodeAreaSection;
import org.exbin.bined.CodeAreaUtils;
import org.exbin.bined.CodeAreaViewMode;
import org.exbin.bined.CodeCharactersCase;
import org.exbin.bined.CodeType;
import org.exbin.bined.EditationMode;
import org.exbin.bined.EditationModeChangedListener;
import org.exbin.bined.EditationOperation;
import org.exbin.bined.PositionOverflowMode;
import org.exbin.bined.ScrollBarVisibility;
import org.exbin.bined.ScrollingListener;
import org.exbin.bined.SelectionChangedListener;
import org.exbin.bined.SelectionRange;
import org.exbin.bined.basic.BasicBackgroundPaintMode;
import org.exbin.bined.basic.CodeAreaScrollPosition;
import org.exbin.bined.basic.HorizontalScrollUnit;
import org.exbin.bined.basic.MovementDirection;
import org.exbin.bined.basic.ScrollingDirection;
import org.exbin.bined.basic.VerticalScrollUnit;
import org.exbin.bined.swing.CodeAreaCommandHandler;
import org.exbin.bined.swing.CodeAreaCore;
import org.exbin.bined.swing.CodeAreaPainter;
import org.exbin.bined.swing.CodeAreaSwingControl;
import org.exbin.bined.swing.CodeAreaSwingUtils;
import org.exbin.bined.swing.basic.color.BasicCodeAreaColorsProfile;
import org.exbin.bined.swing.basic.color.BasicColorsCapableCodeAreaPainter;
import org.exbin.bined.CodeAreaCaretPosition;

/**
 * Code area component.
 *
 * @version 0.2.0 2019/07/07
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CodeArea extends CodeAreaCore implements DefaultCodeArea, CodeAreaSwingControl {

    @Nonnull
    private CodeAreaPainter painter;

    @Nonnull
    private final DefaultCodeAreaCaret caret;
    @Nonnull
    private final SelectionRange selection = new SelectionRange();
    @Nonnull
    private final CodeAreaScrollPosition scrollPosition = new CodeAreaScrollPosition();

    @Nonnull
    private Charset charset = Charset.forName(CodeAreaSwingUtils.DEFAULT_ENCODING);
    private boolean handleClipboard = true;

    @Nonnull
    private EditationMode editationMode = EditationMode.EXPANDING;
    @Nonnull
    private EditationOperation editationOperation = EditationOperation.OVERWRITE;
    @Nonnull
    private CodeAreaViewMode viewMode = CodeAreaViewMode.DUAL;
    @Nullable
    private Font font;
    @Nonnull
    private BasicBackgroundPaintMode borderPaintMode = BasicBackgroundPaintMode.STRIPED;
    @Nonnull
    private AntialiasingMode antialiasingMode = AntialiasingMode.AUTO;
    @Nonnull
    private CodeType codeType = CodeType.HEXADECIMAL;
    @Nonnull
    private CodeCharactersCase codeCharactersCase = CodeCharactersCase.UPPER;
    private boolean showMirrorCursor = true;
    @Nonnull
    private RowWrappingMode rowWrapping = RowWrappingMode.NO_WRAPPING;
    private int minRowPositionLength = 0;
    private int maxRowPositionLength = 0;
    private int wrappingBytesGroupSize = 0;
    private int maxBytesPerRow = 16;

    @Nonnull
    private ScrollBarVisibility verticalScrollBarVisibility = ScrollBarVisibility.IF_NEEDED;
    @Nonnull
    private VerticalScrollUnit verticalScrollUnit = VerticalScrollUnit.ROW;
    @Nonnull
    private ScrollBarVisibility horizontalScrollBarVisibility = ScrollBarVisibility.IF_NEEDED;
    @Nonnull
    private HorizontalScrollUnit horizontalScrollUnit = HorizontalScrollUnit.PIXEL;

    private final List<CaretMovedListener> caretMovedListeners = new ArrayList<CaretMovedListener>();
    private final List<ScrollingListener> scrollingListeners = new ArrayList<ScrollingListener>();
    private final List<SelectionChangedListener> selectionChangedListeners = new ArrayList<SelectionChangedListener>();
    private final List<EditationModeChangedListener> editationModeChangedListeners = new ArrayList<EditationModeChangedListener>();

    /**
     * Creates new instance with default command handler and painter.
     */
    public CodeArea() {
        super(DefaultCodeAreaCommandHandler.createDefaultCodeAreaCommandHandlerFactory());

        caret = new DefaultCodeAreaCaret(this);
        painter = new DefaultCodeAreaPainter(this);
        painter.attach();
        init();
    }

    /**
     * Creates new instance with provided command handler factory method.
     *
     * @param commandHandlerFactory command handler or null for default handler
     */
    public CodeArea(CodeAreaCommandHandler.CodeAreaCommandHandlerFactory commandHandlerFactory) {
        super(commandHandlerFactory);

        caret = new DefaultCodeAreaCaret(this);
        painter = new DefaultCodeAreaPainter(this);
        painter.attach();
        init();
    }

    private void init() {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                resetColors();
            }
        });
        caret.setSection(BasicCodeAreaSection.CODE_MATRIX);
    }

    @Nonnull
    public CodeAreaPainter getPainter() {
        return painter;
    }

    public void setPainter(CodeAreaPainter painter) {
        CodeAreaUtils.requireNonNull(painter);

        this.painter.detach();
        this.painter = painter;
        painter.attach();
        reset();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        painter.paintComponent(g);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        painter.rebuildColors();
    }

    @Nonnull
    @Override
    public DefaultCodeAreaCaret getCaret() {
        return caret;
    }

    @Override
    public boolean isShowMirrorCursor() {
        return showMirrorCursor;
    }

    @Override
    public void setShowMirrorCursor(boolean showMirrorCursor) {
        this.showMirrorCursor = showMirrorCursor;
        repaint();
    }

    @Override
    public int getMinRowPositionLength() {
        return minRowPositionLength;
    }

    @Override
    public void setMinRowPositionLength(int minRowPositionLength) {
        this.minRowPositionLength = minRowPositionLength;
        updateLayout();
    }

    @Override
    public int getMaxRowPositionLength() {
        return maxRowPositionLength;
    }

    @Override
    public void setMaxRowPositionLength(int maxRowPositionLength) {
        this.maxRowPositionLength = maxRowPositionLength;
        updateLayout();
    }

    public boolean isInitialized() {
        return painter.isInitialized();
    }

    public long getDataPosition() {
        return caret.getDataPosition();
    }

    public int getCodeOffset() {
        return caret.getCodeOffset();
    }

    @Nonnull
    public CodeAreaSection getActiveSection() {
        return caret.getSection();
    }

    @Nonnull
    public CodeAreaCaretPosition getCaretPosition() {
        return caret.getCaretPosition();
    }

    public void setCaretPosition(CodeAreaCaretPosition caretPosition) {
        caret.setCaretPosition(caretPosition);
        notifyCaretMoved();
    }

    public void setCaretPosition(long dataPosition) {
        caret.setCaretPosition(dataPosition);
        notifyCaretMoved();
    }

    public void setCaretPosition(long dataPosition, int codeOffset) {
        caret.setCaretPosition(dataPosition, codeOffset);
        notifyCaretMoved();
    }

    @Override
    public int getMouseCursorShape(int positionX, int positionY) {
        return painter.getMouseCursorShape(positionX, positionY);
    }

    @Override
    public BasicCodeAreaZone getPositionZone(int positionX, int positionY) {
        return painter.getPositionZone(positionX, positionY);
    }

    @Nonnull
    @Override
    public CodeCharactersCase getCodeCharactersCase() {
        return codeCharactersCase;
    }

    @Override
    public void setCodeCharactersCase(CodeCharactersCase codeCharactersCase) {
        this.codeCharactersCase = codeCharactersCase;
        updateLayout();
    }

    @Override
    public void resetColors() {
        painter.resetColors();
        repaint();
    }

    @Nonnull
    @Override
    public CodeAreaViewMode getViewMode() {
        return viewMode;
    }

    @Override
    public void setViewMode(CodeAreaViewMode viewMode) {
        if (viewMode != this.viewMode) {
            this.viewMode = viewMode;
            switch (viewMode) {
                case CODE_MATRIX:
                    getCaret().setSection(BasicCodeAreaSection.CODE_MATRIX);
                    reset();
                    notifyCaretMoved();
                    break;
                case TEXT_PREVIEW:
                    getCaret().setSection(BasicCodeAreaSection.TEXT_PREVIEW);
                    reset();
                    notifyCaretMoved();
                    break;
                default:
                    reset();
                    break;
            }
            updateLayout();
        }
    }

    @Nonnull
    @Override
    public CodeType getCodeType() {
        return codeType;
    }

    @Override
    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
        updateLayout();
    }

    @Override
    public void revealCursor() {
        revealPosition(caret.getCaretPosition());
    }

    @Override
    public void revealPosition(CodeAreaCaretPosition caretPosition) {
        if (!isInitialized()) {
            // Silently ignore if painter is not yet initialized
            return;
        }

        CodeAreaScrollPosition revealScrollPosition = painter.computeRevealScrollPosition(caretPosition);
        if (revealScrollPosition != null) {
            setScrollPosition(revealScrollPosition);
            resetPainter();
            updateScrollBars();
            notifyScrolled();
        }
    }

    public void revealPosition(long dataPosition, int dataOffset, CodeAreaSection section) {
        revealPosition(new DefaultCodeAreaCaretPosition(dataPosition, dataOffset, section));
    }

    @Override
    public void centerOnCursor() {
        centerOnPosition(caret.getCaretPosition());
    }

    @Override
    public void centerOnPosition(CodeAreaCaretPosition caretPosition) {
        if (!isInitialized()) {
            // Silently ignore if painter is not yet initialized
            return;
        }

        CodeAreaScrollPosition centerOnScrollPosition = painter.computeCenterOnScrollPosition(caretPosition);
        if (centerOnScrollPosition != null) {
            setScrollPosition(centerOnScrollPosition);
            resetPainter();
            updateScrollBars();
            notifyScrolled();
        }
    }

    public void centerOnPosition(long dataPosition, int dataOffset, CodeAreaSection section) {
        centerOnPosition(new DefaultCodeAreaCaretPosition(dataPosition, dataOffset, section));
    }

    @Nullable
    @Override
    public CodeAreaCaretPosition mousePositionToClosestCaretPosition(int positionX, int positionY, PositionOverflowMode overflowMode) {
        return painter.mousePositionToClosestCaretPosition(positionX, positionY, overflowMode);
    }

    @Nonnull
    @Override
    public CodeAreaCaretPosition computeMovePosition(CodeAreaCaretPosition position, MovementDirection direction) {
        return painter.computeMovePosition(position, direction);
    }

    @Nonnull
    @Override
    public CodeAreaScrollPosition computeScrolling(CodeAreaScrollPosition startPosition, ScrollingDirection scrollingShift) {
        return painter.computeScrolling(startPosition, scrollingShift);
    }

    @Override
    public void updateScrollBars() {
        painter.updateScrollBars();
        repaint();
    }

    @Nonnull
    @Override
    public CodeAreaScrollPosition getScrollPosition() {
        return scrollPosition;
    }

    @Override
    public void setScrollPosition(CodeAreaScrollPosition scrollPosition) {
        this.scrollPosition.setScrollPosition(scrollPosition);
        notifyScrolled();
    }

    @Nonnull
    @Override
    public ScrollBarVisibility getVerticalScrollBarVisibility() {
        return verticalScrollBarVisibility;
    }

    @Override
    public void setVerticalScrollBarVisibility(ScrollBarVisibility verticalScrollBarVisibility) {
        this.verticalScrollBarVisibility = verticalScrollBarVisibility;
        resetPainter();
        updateScrollBars();
    }

    @Nonnull
    @Override
    public VerticalScrollUnit getVerticalScrollUnit() {
        return verticalScrollUnit;
    }

    @Override
    public void setVerticalScrollUnit(VerticalScrollUnit verticalScrollUnit) {
        this.verticalScrollUnit = verticalScrollUnit;
        long linePosition = scrollPosition.getRowPosition();
        if (verticalScrollUnit == VerticalScrollUnit.ROW) {
            scrollPosition.setRowOffset(0);
        }
        resetPainter();
        scrollPosition.setRowPosition(linePosition);
        updateScrollBars();
        notifyScrolled();
    }

    @Nonnull
    @Override
    public ScrollBarVisibility getHorizontalScrollBarVisibility() {
        return horizontalScrollBarVisibility;
    }

    @Override
    public void setHorizontalScrollBarVisibility(ScrollBarVisibility horizontalScrollBarVisibility) {
        this.horizontalScrollBarVisibility = horizontalScrollBarVisibility;
        resetPainter();
        updateScrollBars();
    }

    @Nonnull
    @Override
    public HorizontalScrollUnit getHorizontalScrollUnit() {
        return horizontalScrollUnit;
    }

    @Override
    public void setHorizontalScrollUnit(HorizontalScrollUnit horizontalScrollUnit) {
        this.horizontalScrollUnit = horizontalScrollUnit;
        int bytePosition = scrollPosition.getCharPosition();
        if (horizontalScrollUnit == HorizontalScrollUnit.CHARACTER) {
            scrollPosition.setCharOffset(0);
        }
        resetPainter();
        scrollPosition.setCharPosition(bytePosition);
        updateScrollBars();
        notifyScrolled();
    }

    @Override
    public void reset() {
        painter.reset();
    }

    @Override
    public void updateLayout() {
        painter.resetLayout();
        repaint();
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void resetPainter() {
        painter.reset();
    }

    @Override
    public void notifyCaretChanged() {
        painter.resetCaret();
        repaint();
    }

    @Override
    public void notifyDataChanged() {
        super.notifyDataChanged();
        updateLayout();
    }

    @Nonnull
    @Override
    public AntialiasingMode getAntialiasingMode() {
        return antialiasingMode;
    }

    @Override
    public void setAntialiasingMode(AntialiasingMode antialiasingMode) {
        this.antialiasingMode = antialiasingMode;
        reset();
        repaint();
    }

    @Nonnull
    @Override
    public SelectionRange getSelection() {
        return selection;
    }

    @Override
    public void setSelection(SelectionRange selection) {
        CodeAreaUtils.requireNonNull(selection);

        this.selection.setSelection(selection);
        notifySelectionChanged();
        repaint();
    }

    @Override
    public void setSelection(long start, long end) {
        this.selection.setSelection(start, end);
        notifySelectionChanged();
        repaint();
    }

    @Override
    public void clearSelection() {
        this.selection.clearSelection();
        notifySelectionChanged();
        repaint();
    }

    @Override
    public boolean hasSelection() {
        return !selection.isEmpty();
    }

    @Nonnull
    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void setCharset(Charset charset) {
        CodeAreaUtils.requireNonNull(charset);

        this.charset = charset;
        reset();
        repaint();
    }

    @Override
    public boolean isEditable() {
        return editationMode != EditationMode.READ_ONLY;
    }

    @Nonnull
    @Override
    public EditationMode getEditationMode() {
        return editationMode;
    }

    @Override
    public void setEditationMode(EditationMode editationMode) {
        boolean changed = editationMode != this.editationMode;
        this.editationMode = editationMode;
        if (changed) {
            for (EditationModeChangedListener listener : editationModeChangedListeners) {
                listener.editationModeChanged(editationMode, getActiveOperation());
            }
            caret.resetBlink();
            notifyCaretChanged();
            repaint();
        }
    }

    @Override
    public EditationOperation getActiveOperation() {
        switch (editationMode) {
            case READ_ONLY:
                return EditationOperation.INSERT;
            case INPLACE:
                return EditationOperation.OVERWRITE;
            case CAPPED:
            case EXPANDING:
                return editationOperation;
            default:
                throw new IllegalStateException("Unexpected code type: " + editationMode.name());
        }
    }

    @Nonnull
    @Override
    public EditationOperation getEditationOperation() {
        return editationOperation;
    }

    @Override
    public void setEditationOperation(EditationOperation editationOperation) {
        EditationOperation previousOperation = getActiveOperation();
        this.editationOperation = editationOperation;
        EditationOperation currentOperation = getActiveOperation();
        boolean changed = previousOperation != currentOperation;
        if (changed) {
            for (EditationModeChangedListener listener : editationModeChangedListeners) {
                listener.editationModeChanged(editationMode, currentOperation);
            }
            caret.resetBlink();
            notifyCaretChanged();
            repaint();
        }
    }

    @Override
    public boolean isHandleClipboard() {
        return handleClipboard;
    }

    @Override
    public void setHandleClipboard(boolean handleClipboard) {
        this.handleClipboard = handleClipboard;
    }

    @Nullable
    @Override
    public Font getCodeFont() {
        return font;
    }

    @Override
    public void setCodeFont(@Nullable Font font) {
        this.font = font;
        painter.resetFont();
        repaint();
    }

    @Nonnull
    @Override
    public BasicBackgroundPaintMode getBackgroundPaintMode() {
        return borderPaintMode;
    }

    @Override
    public void setBackgroundPaintMode(BasicBackgroundPaintMode borderPaintMode) {
        this.borderPaintMode = borderPaintMode;
        updateLayout();
    }

    @Nonnull
    @Override
    public RowWrappingMode getRowWrapping() {
        return rowWrapping;
    }

    @Override
    public void setRowWrapping(RowWrappingMode rowWrapping) {
        this.rowWrapping = rowWrapping;
        updateLayout();
    }

    @Override
    public int getWrappingBytesGroupSize() {
        return wrappingBytesGroupSize;
    }

    @Override
    public void setWrappingBytesGroupSize(int groupSize) {
        wrappingBytesGroupSize = groupSize;
        updateLayout();
    }

    @Override
    public int getMaxBytesPerRow() {
        return maxBytesPerRow;
    }

    @Override
    public void setMaxBytesPerRow(int maxBytesPerRow) {
        this.maxBytesPerRow = maxBytesPerRow;
        updateLayout();
    }

    @Nullable
    @Override
    public BasicCodeAreaColorsProfile getBasicColors() {
        if (painter instanceof BasicColorsCapableCodeAreaPainter) {
            return ((BasicColorsCapableCodeAreaPainter) painter).getBasicColors();
        }
        return null;
    }

    @Override
    public void setBasicColors(BasicCodeAreaColorsProfile colors) {
        if (painter instanceof BasicColorsCapableCodeAreaPainter) {
            ((BasicColorsCapableCodeAreaPainter) painter).setBasicColors(colors);
        }
    }

    public void notifySelectionChanged() {
        for (SelectionChangedListener selectionChangedListener : selectionChangedListeners) {
            selectionChangedListener.selectionChanged(selection);
        }
    }

    @Override
    public void notifyCaretMoved() {
        for (CaretMovedListener caretMovedListener : caretMovedListeners) {
            caretMovedListener.caretMoved(caret.getCaretPosition());
        }
    }

    @Override
    public void notifyScrolled() {
        for (ScrollingListener scrollingListener : scrollingListeners) {
            scrollingListener.scrolled();
        }
    }

    @Override
    public void addSelectionChangedListener(SelectionChangedListener selectionChangedListener) {
        selectionChangedListeners.add(selectionChangedListener);
    }

    @Override
    public void removeSelectionChangedListener(SelectionChangedListener selectionChangedListener) {
        selectionChangedListeners.remove(selectionChangedListener);
    }

    @Override
    public void addCaretMovedListener(CaretMovedListener caretMovedListener) {
        caretMovedListeners.add(caretMovedListener);
    }

    @Override
    public void removeCaretMovedListener(CaretMovedListener caretMovedListener) {
        caretMovedListeners.remove(caretMovedListener);
    }

    @Override
    public void addScrollingListener(ScrollingListener scrollingListener) {
        scrollingListeners.add(scrollingListener);
    }

    @Override
    public void removeScrollingListener(ScrollingListener scrollingListener) {
        scrollingListeners.remove(scrollingListener);
    }

    @Override
    public void addEditationModeChangedListener(EditationModeChangedListener editationModeChangedListener) {
        editationModeChangedListeners.add(editationModeChangedListener);
    }

    @Override
    public void removeEditationModeChangedListener(EditationModeChangedListener editationModeChangedListener) {
        editationModeChangedListeners.remove(editationModeChangedListener);
    }
}
