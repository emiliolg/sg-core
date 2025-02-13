
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.core;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * BitArray Serializer.
 */
@SuppressWarnings("WeakerAccess")  // GWT
public class BitArray_CustomFieldSerializer extends CustomFieldSerializer<BitArray> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, BitArray instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public BitArray instantiateInstance(SerializationStreamReader r)
        throws SerializationException
    {
        return instantiate(r);
    }

    @Override public void serializeInstance(SerializationStreamWriter w, BitArray e)
        throws SerializationException
    {
        serialize(w, e);
    }

    //~ Methods ......................................................................................................................................

    /** De-serialize the object.. Not implemented because the code is already in instantiate */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader streamReader, BitArray instance) {
        // nothing more to do
    }

    /** Instantiate an Expression from the stream. */
    public static BitArray instantiate(SerializationStreamReader reader)
        throws SerializationException
    {
        try {
            return BitArray.initialize(new GwtStreamReader(reader));
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    /** Serialize an Expression to the stream. */
    public static void serialize(SerializationStreamWriter w, BitArray e)
        throws SerializationException
    {
        try {
            e.serialize(new GwtStreamWriter(w));
        }
        catch (final SerializerException ex) {
            throw GwtSerializationExceptionFactory.create(ex);
        }
    }
}  // end class BitArray_CustomFieldSerializer
