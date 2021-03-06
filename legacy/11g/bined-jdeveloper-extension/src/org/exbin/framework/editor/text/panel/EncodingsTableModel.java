/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.framework.editor.text.panel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for encoding / character sets.
 *
 * @version 0.2.1 2019/07/19
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EncodingsTableModel extends AbstractTableModel {

    private final Map<String, EncodingRecord> encodings = new HashMap<String, EncodingRecord>();
    private final List<String> filtered = new ArrayList<String>();
    private final Set<String> usedEncodings = new HashSet<String>();
    private String nameFilter = "";
    private String countryFilter = "";

    public EncodingsTableModel() {
        initEncodings();
    }

    private void initEncodings() {
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        for (Map.Entry<String, Charset> entry : charsets.entrySet()) {
            Charset charset = entry.getValue();
            EncodingRecord record = new EncodingRecord();
            record.name = charset.name();
            try {
                record.maxBytes = (int) charset.newEncoder().maxBytesPerChar();
            } catch (UnsupportedOperationException ex) {
                // ignore
            }
            encodings.put(charset.name().toLowerCase(), record);
        };

        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/org/exbin/framework/editor/text/resources/encodingsMap.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String encoding;
            do {
                encoding = reader.readLine();
                if (encoding != null) {
                    EncodingRecord record = encodings.get(encoding.toLowerCase());
                    if (record != null) {
                        record.description = reader.readLine();
                        record.countries = reader.readLine();
                    } else {
                        reader.readLine();
                        reader.readLine();
                    }
                }
            } while (encoding != null);
        } catch (IOException ex) {
            Logger.getLogger(EncodingsTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Nullable
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Description";
            case 2:
                return "Country Codes";
            case 3:
                return "Max Bytes";
        }

        return null;
    }

    private void rebuildFiltered() {
        filtered.clear();
        for (Map.Entry<String, EncodingRecord> record: encodings.entrySet()) {
            String name = record.getKey();
            if (!(usedEncodings.contains(name))) {
                if (!(!nameFilter.isEmpty() && !name.contains(nameFilter.toLowerCase()))) {
                    if (!(!countryFilter.isEmpty() && (record.getValue().countries == null || !record.getValue().countries.toLowerCase().contains(countryFilter.toLowerCase())))) {
                        filtered.add(name);
                    }
                }
            }
        }
        Collections.sort(filtered);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return filtered.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Nullable
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String name = filtered.get(rowIndex);
        EncodingRecord record = encodings.get(name);
        switch (columnIndex) {
            case 0: {
                return record.name;
            }
            case 1: {
                return record.description;
            }
            case 2: {
                return record.countries;
            }
            case 3: {
                return record.maxBytes;
            }
        }

        return null;
    }

    public void setUsedEncodings(List<String> encodings) {
        usedEncodings.clear();
        for (String name : encodings) {
            usedEncodings.add(name.toLowerCase());
        }
        rebuildFiltered();
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
        rebuildFiltered();
    }

    public void setCountryFilter(String countryFilter) {
        this.countryFilter = countryFilter;
        rebuildFiltered();
    }

    /**
     * POJO structure for single record.
     */
    private static class EncodingRecord {

        String name;
        String description;
        /**
         * Space separated country codes.
         */
        String countries;
        int maxBytes;
    }
}
