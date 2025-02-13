
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.serializer;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.type.Kind;

/**
 * This is a wrapper of the GWT {@link SerializationStreamReader}.
 */
public class GwtStreamReader extends StreamReader.Default {

    //~ Instance Fields ..............................................................................................................................

    private final SerializationStreamReader r;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the {@link SerializationStreamReader} to wrap */
    public GwtStreamReader(final SerializationStreamReader r) {
        this.r = r;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean readBoolean() {
        try {
            return r.readBoolean();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public byte readByte() {
        try {
            return r.readByte();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public char readChar() {
        try {
            return r.readChar();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public double readDouble() {
        try {
            return r.readDouble();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public float readFloat() {
        try {
            return r.readFloat();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public int readInt() {
        try {
            return r.readInt();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public long readLong() {
        try {
            return r.readLong();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public Object readObject() {
        try {
            return r.readObject();
        }
        catch (final SerializationException e) {
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
            return r.readShort();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public String readString() {
        try {
            return r.readString();
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }
}  // end class GwtStreamReader
