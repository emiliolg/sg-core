
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.type.Kind;

/**
 * An adapter from Input to Stream Reader.
 */
public class IsStreamReader extends StreamReader.Default {

    //~ Instance Fields ..............................................................................................................................

    private final ObjectInput input;

    //~ Constructors .................................................................................................................................

    /** Create the adapter. */
    public IsStreamReader(ObjectInput input) {
        this.input = input;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean readBoolean() {
        try {
            return input.readBoolean();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public byte readByte() {
        try {
            return input.readByte();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public char readChar() {
        try {
            return input.readChar();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public double readDouble() {
        try {
            return input.readDouble();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public float readFloat() {
        try {
            return input.readFloat();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public int readInt() {
        try {
            return input.readInt();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public long readLong() {
        try {
            return input.readLong();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public Object readObject() {
        try {
            final byte[] object = new byte[input.readInt()];
            input.read(object);
            final ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(object));
            return stream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new SerializerException(e);
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
            return input.readShort();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public String readString() {
        try {
            return input.readUTF();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}  // end class IsStreamReader
