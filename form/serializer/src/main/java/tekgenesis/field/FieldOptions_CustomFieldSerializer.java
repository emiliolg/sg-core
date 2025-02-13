
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * FieldOptions Serializer.
 */
@SuppressWarnings("WeakerAccess")
public class FieldOptions_CustomFieldSerializer extends CustomFieldSerializer<FieldOptions> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, FieldOptions instance)
        throws SerializationException
    {
        // Nothing to do
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public FieldOptions instantiateInstance(SerializationStreamReader reader)
        throws SerializationException
    {
        return instantiate(reader);
    }

    @Override public void serializeInstance(SerializationStreamWriter writer, FieldOptions options)
        throws SerializationException
    {
        serialize(writer, options);
    }

    //~ Methods ......................................................................................................................................

    /** Deserialize the object.. Not implemented because the code is already in instantiate */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader streamReader, FieldOptions instance) {
        // nothing more to do
    }

    /** Instantiate FiledOptions from the SerializationStreamReader. */
    public static FieldOptions instantiate(SerializationStreamReader reader)
        throws SerializationException
    {
        try {
            return FieldOptions.instantiate(new GwtStreamReader(reader));
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    /** Serialize to a Stream. */
    public static void serialize(SerializationStreamWriter writer, FieldOptions options)
        throws SerializationException
    {
        try {
            options.serialize(new GwtStreamWriter(writer));
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }
}  // end class FieldOptions_CustomFieldSerializer
