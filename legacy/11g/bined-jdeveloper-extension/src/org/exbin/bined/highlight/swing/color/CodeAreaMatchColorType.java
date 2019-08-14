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
package org.exbin.bined.highlight.swing.color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.color.CodeAreaBasicColors;
import org.exbin.bined.color.CodeAreaColorGroup;
import org.exbin.bined.color.CodeAreaColorType;

/**
 * Enumeration of search matching color types.
 *
 * @version 0.2.0 2018/11/30
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public enum CodeAreaMatchColorType implements CodeAreaColorType {

    MATCH_COLOR(CodeAreaBasicColors.TEXT_COLOR.getId(), MatchCodeAreaColorsGroup.MATCHING),
    MATCH_BACKGROUND(CodeAreaBasicColors.TEXT_BACKGROUND.getId(), MatchCodeAreaColorsGroup.MATCHING),
    ACTIVE_MATCH_COLOR(CodeAreaBasicColors.TEXT_COLOR.getId(), MatchCodeAreaColorsGroup.MATCHING),
    ACTIVE_MATCH_BACKGROUND(CodeAreaBasicColors.TEXT_BACKGROUND.getId(), MatchCodeAreaColorsGroup.MATCHING);

    @Nonnull
    private final String typeId;
    @Nullable
    private final CodeAreaColorGroup group;

    private CodeAreaMatchColorType(String typeId, @Nullable CodeAreaColorGroup group) {
        this.typeId = typeId;
        this.group = group;
    }

    @Nonnull
    @Override
    public String getId() {
        return typeId;
    }

    @Nullable
    @Override
    public CodeAreaColorGroup getGroup() {
        return group;
    }
}