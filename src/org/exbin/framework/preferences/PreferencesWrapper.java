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
package org.exbin.framework.preferences;

import java.io.IOException;

import java.util.prefs.BackingStoreException;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.exbin.framework.api.Preferences;

/**
 * Wrapper for preferences.
 *
 * @version 0.2.0 2019/07/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PreferencesWrapper implements Preferences {

    private final oracle.ide.config.Preferences preferences;

    public PreferencesWrapper(oracle.ide.config.Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean exists(String key) {
        return preferences.getProperties().containsKey(key);
    }

    @Override
    public String get(String key) {
        return preferences.getProperties().getString(key, null);
    }

    @Override
    public String get(String key, @Nullable String def) {
        return preferences.getProperties().getString(key, def);
    }

    @Override
    public void put(String key, @Nullable String value) {
        if (value == null) {
            preferences.getProperties().remove(key);
        } else {
            preferences.getProperties().putString(key, value);
        }
    }

    @Override
    public void remove(String key) {
        preferences.getProperties().remove(key);
    }

    @Override
    public void putInt(String key, int value) {
        preferences.getProperties().putInt(key, value);
    }

    @Override
    public int getInt(String key, int def) {
        return preferences.getProperties().getInt(key, def);
    }

    @Override
    public void putLong(String key, long value) {
        preferences.getProperties().putLong(key, value);
    }

    @Override
    public long getLong(String key, long def) {
        return preferences.getProperties().getLong(key, def);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        preferences.getProperties().putBoolean(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return preferences.getProperties().getBoolean(key, def);
    }

    @Override
    public void putFloat(String key, float value) {
        preferences.getProperties().putFloat(key, value);
    }

    @Override
    public float getFloat(String key, float def) {
        return preferences.getProperties().getFloat(key, def);
    }

    @Override
    public void putDouble(String key, double value) {
        preferences.getProperties().putDouble(key, value);
    }

    @Override
    public double getDouble(String key, double def) {
        return preferences.getProperties().getDouble(key, def);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getByteArray(String key, byte[] def) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flush() {
        try {
            preferences.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sync() {
        try {
            preferences.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
