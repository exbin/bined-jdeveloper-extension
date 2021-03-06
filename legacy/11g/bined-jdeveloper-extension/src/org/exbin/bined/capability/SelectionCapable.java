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
package org.exbin.bined.capability;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.SelectionChangedListener;
import org.exbin.bined.SelectionRange;

/**
 * Support for selection capability.
 *
 * @version 0.2.0 2019/07/07
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface SelectionCapable {

    @Nonnull
    SelectionRange getSelection();

    void setSelection(SelectionRange selection);

    void setSelection(long start, long end);

    void clearSelection();

    /**
     * Returns true if there is active selection for clipboard handling.
     *
     * @return true if non-empty selection is active
     */
    boolean hasSelection();

    void addSelectionChangedListener(SelectionChangedListener selectionChangedListener);

    void removeSelectionChangedListener(SelectionChangedListener selectionChangedListener);
}
