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
package org.exbin.utils.binary_data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Basic implementation of editable binary data interface using byte array.
 *
 * This implementation is highly inefficient with resources and is not
 * recommended for frequently changed data as each change creates copy of data.
 *
 * @version 0.1.3 2019/07/16
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ByteArrayEditableData extends ByteArrayData implements EditableBinaryData {

    public static final int BUFFER_SIZE = 1024;
    public static final int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 5;

    public ByteArrayEditableData() {
        this(new byte[0]);
    }

    public ByteArrayEditableData(byte[] data) {
        super(data);
    }

    @Override
    public void setDataSize(long size) {
        if (size < 0) {
            throw new InvalidParameterException("Size cannot be negative");
        }

        if (data.length != size) {
            if (size < data.length) {
                data = Arrays.copyOfRange(data, 0, (int) size);
            } else {
                byte[] newData = new byte[(int) size];
                System.arraycopy(data, 0, newData, 0, data.length);
                data = newData;
            }
        }
    }

    @Override
    public void setByte(long position, byte value) {
        try {
            data[(int) position] = value;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    @Override
    public void insertUninitialized(long startFrom, long length) {
        if (startFrom > data.length) {
            throw new OutOfBoundsException("Data can be inserted only inside or at the end");
        }
        if (length > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (length > 0) {
            byte[] newData = new byte[(int) (data.length + length)];
            System.arraycopy(data, 0, newData, 0, (int) startFrom);
            System.arraycopy(data, (int) (startFrom), newData, (int) (startFrom + length), (int) (data.length - startFrom));
            data = newData;
        }
    }

    @Override
    public void insert(long startFrom, long length) {
        if (startFrom > data.length) {
            throw new OutOfBoundsException("Data can be inserted only inside or at the end");
        }
        if (length > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (length > 0) {
            byte[] newData = new byte[(int) (data.length + length)];
            System.arraycopy(data, 0, newData, 0, (int) startFrom);
            System.arraycopy(data, (int) (startFrom), newData, (int) (startFrom + length), (int) (data.length - startFrom));
            data = newData;
        }
    }

    @Override
    public void insert(long startFrom, byte[] insertedData) {
        if (startFrom > data.length) {
            throw new OutOfBoundsException("Data can be inserted only inside or at the end");
        }
        if (insertedData.length > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        int length = insertedData.length;
        if (length > 0) {
            byte[] newData = new byte[data.length + length];
            System.arraycopy(data, 0, newData, 0, (int) startFrom);
            try {
                System.arraycopy(insertedData, 0, newData, (int) startFrom, length);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OutOfBoundsException(ex);
            }
            System.arraycopy(data, (int) (startFrom), newData, (int) (startFrom + length), (int) (data.length - startFrom));
            data = newData;
        }
    }

    @Override
    public void insert(long startFrom, byte[] insertedData, int insertedDataOffset, int length) {
        if (startFrom > data.length) {
            throw new OutOfBoundsException("Data can be inserted only inside or at the end");
        }
        if (length > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (length > 0) {
            byte[] newData = new byte[data.length + length];
            System.arraycopy(data, 0, newData, 0, (int) startFrom);
            try {
                System.arraycopy(insertedData, insertedDataOffset, newData, (int) startFrom, length);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OutOfBoundsException(ex);
            }
            System.arraycopy(data, (int) (startFrom), newData, (int) (startFrom + length), (int) (data.length - startFrom));
            data = newData;
        }
    }

    @Override
    public void insert(long startFrom, BinaryData insertedData) {
        if (startFrom > data.length) {
            throw new OutOfBoundsException("Data can be inserted only inside or at the end");
        }
        if (insertedData.getDataSize() > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (insertedData instanceof ByteArrayData) {
            insert(startFrom, ((ByteArrayData) insertedData).data);
        } else {
            insert(startFrom, insertedData, 0, insertedData.getDataSize());
        }
    }

    @Override
    public void insert(long startFrom, BinaryData insertedData, long insertedDataOffset, long insertedDataLength) {
        if (startFrom > data.length) {
            throw new OutOfBoundsException("Data can be inserted only inside or at the end");
        }
        if (insertedDataLength > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (insertedData instanceof ByteArrayData) {
            if (insertedDataOffset > Integer.MAX_VALUE || insertedDataLength > Integer.MAX_VALUE) {
                throw new OutOfBoundsException("Out of range");
            }
            insert(startFrom, ((ByteArrayData) insertedData).data, (int) insertedDataOffset, (int) insertedDataLength);
        } else {
            long length = insertedDataLength;
            if (length > 0) {
                byte[] newData = new byte[(int) (data.length + length)];
                System.arraycopy(data, 0, newData, 0, (int) startFrom);
                for (int i = 0; i < length; i++) {
                    newData[(int) (startFrom + i)] = insertedData.getByte(insertedDataOffset + i);
                }
                System.arraycopy(data, (int) (startFrom), newData, (int) (startFrom + length), (int) (data.length - startFrom));
                data = newData;
            }
        }
    }

    @Override
    public long insert(long startFrom, InputStream inputStream, long dataSize) throws IOException {
        if (dataSize > MAX_ARRAY_LENGTH - data.length) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (inputStream.available() > 0 && dataSize > 0) {
                int toRead = buffer.length;
                if (toRead > dataSize) {
                    toRead = (int)dataSize;
                }
                int red = inputStream.read(buffer, 0, toRead);
                if (red > 0) {
                    output.write(buffer, 0, red);
                    dataSize -= red;
                } else {
                    break;
                }
            }
            byte[] newData = output.toByteArray();
            if (startFrom + newData.length > getDataSize()) {
                setDataSize(startFrom + newData.length);
            }
            replace(startFrom, newData);
            return newData.length;
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    @Override
    public void fillData(long startFrom, long length) {
        fillData(startFrom, length, (byte) 0);
    }

    @Override
    public void fillData(long startFrom, long length, byte fill) {
        if (length > 0) {
            try {
                Arrays.fill(data, (int) startFrom, (int) (startFrom + length), fill);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OutOfBoundsException(ex);
            }
        }
    }

    @Override
    public void replace(long targetPosition, @Nonnull BinaryData sourceData) {
        replace(targetPosition, sourceData, 0, sourceData.getDataSize());
    }

    @Override
    public void replace(long targetPosition, BinaryData sourceData, long startFrom, long length) {
        if (targetPosition + length > getDataSize()) {
            throw new OutOfBoundsException("Data can be replaced only inside or at the end");
        }

        if (sourceData instanceof ByteArrayData) {
            replace(targetPosition, ((ByteArrayData) sourceData).data, (int) startFrom, (int) length);
        } else {
            while (length > 0) {
                setByte(targetPosition, sourceData.getByte(startFrom));
                targetPosition++;
                startFrom++;
                length--;
            }
        }
    }

    @Override
    public void replace(long targetPosition, byte[] replacingData) {
        replace(targetPosition, replacingData, 0, replacingData.length);
    }

    @Override
    public void replace(long targetPosition, byte[] replacingData, int replacingDataOffset, int length) {
        if (targetPosition + length > getDataSize()) {
            throw new OutOfBoundsException("Data can be replaced only inside or at the end");
        }

        try {
            System.arraycopy(replacingData, replacingDataOffset, data, (int) targetPosition, length);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    @Override
    public void remove(long startFrom, long length) {
        if (startFrom + length > data.length) {
            throw new OutOfBoundsException("Cannot remove from " + startFrom + " with length " + length);
        }
        if (length > 0) {
            byte[] newData = new byte[(int) (data.length - length)];
            System.arraycopy(data, 0, newData, 0, (int) startFrom);
            System.arraycopy(data, (int) (startFrom + length), newData, (int) startFrom, (int) (data.length - startFrom - length));
            data = newData;
        }
    }

    @Nonnull
    @Override
    public BinaryData copy() {
        byte[] copy = Arrays.copyOf(data, data.length);
        return new ByteArrayEditableData(copy);
    }

    @Nonnull
    @Override
    public BinaryData copy(long startFrom, long length) {
        if (startFrom + length > data.length) {
            throw new OutOfBoundsException("Attemt to copy outside of data");
        }

        byte[] copy = Arrays.copyOfRange(data, (int) startFrom, (int) (startFrom + length));
        return new ByteArrayEditableData(copy);
    }

    @Override
    public void clear() {
        data = new byte[0];
    }

    @Override
    public void loadFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (inputStream.available() > 0) {
                int read = inputStream.read(buffer);
                if (read > 0) {
                    output.write(buffer, 0, read);
                }
            }
            data = output.toByteArray();
        }
        finally {
            if (output != null) {
                output.close();
            }
        }
    }

    @Nonnull
    @Override
    public OutputStream getDataOutputStream() {
        return new ByteArrayDataOutputStream(this);
    }
}
