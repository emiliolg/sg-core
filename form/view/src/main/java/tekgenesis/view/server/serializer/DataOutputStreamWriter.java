
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.serializer;

import java.io.DataOutput;
import java.io.IOException;

import tekgenesis.cache.database.infinispan.IsStreamWriter;
import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamWriter;

/**
 * Implementation of the {@link StreamWriter} for DataOutput.
 */
public class DataOutputStreamWriter extends StreamWriter.Default {

    //~ Instance Fields ..............................................................................................................................

    private final DataOutput w;

    //~ Constructors .................................................................................................................................

    /** Receives the writer to wrap. */
    public DataOutputStreamWriter(final DataOutput w) {
        this.w = w;
    }

    //~ Methods ......................................................................................................................................

    @Override public StreamWriter writeBoolean(final boolean value) {
        try {
            w.writeBoolean(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeByte(final byte value) {
        try {
            w.writeByte(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeChar(final char value) {
        try {
            w.writeChar(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeDouble(final double value) {
        try {
            w.writeDouble(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeFloat(final float value) {
        try {
            w.writeFloat(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeInt(final int value) {
        try {
            w.writeInt(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeLong(final long value) {
        try {
            w.writeLong(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeObjectConst(final Object object) {
        return IsStreamWriter.writeConst(this, object);
    }

    @Override public StreamWriter writeShort(final short value) {
        try {
            w.writeShort(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override public StreamWriter writeString(final String value) {
        try {
            w.writeUTF(value);
            return this;
        }
        catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override protected void writeBytes(byte[] bytes)
        throws IOException
    {
        w.write(bytes);
    }
}  // end class DataOutputStreamWriter
