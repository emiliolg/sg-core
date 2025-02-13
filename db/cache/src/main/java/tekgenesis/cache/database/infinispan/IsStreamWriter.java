
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import java.io.IOException;
import java.io.ObjectOutput;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * An adapter from Output to an Stream Writer.
 */
public class IsStreamWriter extends StreamWriter.Default {

    //~ Instance Fields ..............................................................................................................................

    private final ObjectOutput output;

    //~ Constructors .................................................................................................................................

    /** Create the adapter. */
    public IsStreamWriter(ObjectOutput output) {
        this.output = output;
    }

    //~ Methods ......................................................................................................................................

    @Override public StreamWriter writeBoolean(boolean b) {
        try {
            output.writeBoolean(b);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeByte(byte value) {
        try {
            output.writeByte(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeChar(char value) {
        try {
            output.writeChar(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeDouble(double value) {
        try {
            output.writeDouble(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeFloat(float value) {
        try {
            output.writeFloat(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeInt(int value) {
        try {
            output.writeInt(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeLong(long value) {
        try {
            output.writeLong(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeObjectConst(final Object object) {
        return writeConst(this, object);
    }

    @Override public StreamWriter writeShort(short value) {
        try {
            output.writeShort(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override public StreamWriter writeString(String value) {
        try {
            output.writeUTF(value);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override protected void writeBytes(byte[] bytes)
        throws IOException
    {
        output.write(bytes);
    }

    //~ Methods ......................................................................................................................................

    /** Static method used also in DataOutputStreamWriter. */
    @NotNull public static StreamWriter writeConst(StreamWriter streamWriter, Object object) {
        if (object instanceof Long) {
            streamWriter.writeInt(-1);
            streamWriter.writeLong((Long) object);
        }
        else {
            final Type type = Types.typeOf(object);
            type.getKind().serializeValue(streamWriter, object);
        }
        return streamWriter;
    }
}  // end class IsStreamWriter
