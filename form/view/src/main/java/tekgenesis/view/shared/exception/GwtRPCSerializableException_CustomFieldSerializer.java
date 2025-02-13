
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.exception;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * {@link GwtRPCSerializableException} custom serializer.
 */
@SuppressWarnings("WeakerAccess")
public class GwtRPCSerializableException_CustomFieldSerializer extends CustomFieldSerializer<GwtRPCSerializableException> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader r, GwtRPCSerializableException e)
        throws SerializationException
    {
        deserialize(r, e);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public GwtRPCSerializableException instantiateInstance(SerializationStreamReader r)
        throws SerializationException
    {
        return instantiate(r);
    }

    @Override public void serializeInstance(SerializationStreamWriter w, GwtRPCSerializableException e)
        throws SerializationException
    {
        serialize(w, e);
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader r, GwtRPCSerializableException e) {
        // nothing more to do
    }

    public static GwtRPCSerializableException instantiate(SerializationStreamReader r)
        throws SerializationException
    {
        try {
            return GwtRPCSerializableException.initialize(new GwtStreamReader(r));
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    public static void serialize(SerializationStreamWriter w, GwtRPCSerializableException e)
        throws SerializationException
    {
        try {
            e.serialize(new GwtStreamWriter(w));
        }
        catch (final SerializerException ex) {
            throw GwtSerializationExceptionFactory.create(ex);
        }
    }
}  // end class GwtRPCSerializableException_CustomFieldSerializer
