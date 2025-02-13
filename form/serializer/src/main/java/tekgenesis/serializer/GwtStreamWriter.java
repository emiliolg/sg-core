
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
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * An implementation of the {@link StreamWriter} for Gwt.
 */
public class GwtStreamWriter extends StreamWriter.Default {

    //~ Instance Fields ..............................................................................................................................

    private final SerializationStreamWriter w;

    //~ Constructors .................................................................................................................................

    /** Creates a GwtStreamWriter. */
    public GwtStreamWriter(final SerializationStreamWriter w) {
        this.w = w;
    }

    //~ Methods ......................................................................................................................................

    @Override public StreamWriter writeBoolean(final boolean value) {
        try {
            w.writeBoolean(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeByte(final byte value) {
        try {
            w.writeByte(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeChar(final char value) {
        try {
            w.writeChar(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeDouble(final double value) {
        try {
            w.writeDouble(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeFloat(final float value) {
        try {
            w.writeFloat(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeInt(final int value) {
        try {
            w.writeInt(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeLong(final long value) {
        try {
            w.writeLong(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeObject(final Object value) {
        try {
            w.writeObject(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }
    @Override public StreamWriter writeObjectConst(final Object object) {
        if (object instanceof Long) {
            writeInt(-1);
            writeLong((Long) object);
        }
        else {
            final Type type = Types.typeOf(object);
            type.getKind().serializeValue(this, object);
        }
        return this;
    }

    @Override public StreamWriter writeShort(final short value) {
        try {
            w.writeShort(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeString(final String value) {
        try {
            w.writeString(value);
            return this;
        }
        catch (final SerializationException e) {
            throw new SerializerException(e);
        }
    }
}  // end class GwtStreamWriter
