
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

import tekgenesis.common.serializer.SerializerException;

/**
 * Re-creates a GWT {@link SerializationException}.
 */
public class GwtSerializationExceptionFactory {

    //~ Constructors .................................................................................................................................

    private GwtSerializationExceptionFactory() {}

    //~ Methods ......................................................................................................................................

    /** Creates the gwt exception from the custom {@link SerializerException}. */
    public static SerializationException create(SerializerException e) {
        if (e.getCause() instanceof SerializationException) return (SerializationException) e.getCause();
        else return new SerializationException(e.getMessage(), e.getCause());
    }
}
