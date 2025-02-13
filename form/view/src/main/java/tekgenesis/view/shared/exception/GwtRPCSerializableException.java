
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.exception;

import org.jetbrains.annotations.NonNls;

import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * Simple Exception to be thrown over RPC communications that triggers an onFailure() on
 * client-side.
 */
public class GwtRPCSerializableException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    public GwtRPCSerializableException() {
        this("Unexpected error!");
    }

    public GwtRPCSerializableException(@NonNls String message) {
        super(message);
    }

    //~ Methods ......................................................................................................................................

    public void serialize(GwtStreamWriter writer) {
        writer.writeString(getMessage());
    }

    //~ Methods ......................................................................................................................................

    public static GwtRPCSerializableException initialize(GwtStreamReader reader) {
        return new GwtRPCSerializableException(reader.readString());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1425901820223124378L;
}
