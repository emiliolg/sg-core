
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.serializer;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInputStream;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.util.Files;
import tekgenesis.type.Kind;

/**
 * Implementation of the {@link StreamReader} for DataInput.
 */
public class DataInputStreamReader extends StreamReader.Default {

    //~ Instance Fields ..............................................................................................................................

    private final DataInput r;

    //~ Constructors .................................................................................................................................

    /** Receives the reader to wrap. */
    public DataInputStreamReader(final DataInput r) {
        this.r = r;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean readBoolean() {
        try {
            return r.readBoolean();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public byte readByte() {
        try {
            return r.readByte();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public char readChar() {
        try {
            return r.readChar();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public double readDouble() {
        try {
            return r.readDouble();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public float readFloat() {
        try {
            return r.readFloat();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public int readInt() {
        try {
            return r.readInt();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public long readLong() {
        try {
            return r.readLong();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public Object readObject() {
        ObjectInputStream stream = null;
        try {
            final byte[] object = new byte[r.readInt()];
            r.readFully(object);
            stream = new ObjectInputStream(new ByteArrayInputStream(object));
            return stream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new SerializerException(e);
        }
        finally {
            Files.close(stream);
        }
    }

    @Override public Object readObjectConst() {
        final int i = readInt();
        if (i == -1) return readLong();

        final Kind k = Kind.valueOf(i);
        return k.readValue(this);
    }

    @Override public short readShort() {
        try {
            return r.readShort();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public String readString() {
        try {
            return r.readUTF();
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }
}  // end class DataInputStreamReader
