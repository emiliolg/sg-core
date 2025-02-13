
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

@SuppressWarnings({ "JavaDoc", "WeakerAccess" })
public class ValueSupplier_CustomFieldSerializer extends CustomFieldSerializer<ValueSupplier<Object>> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, ValueSupplier<Object> instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public ValueSupplier<Object> instantiateInstance(SerializationStreamReader streamReader)
        throws SerializationException
    {
        return instantiate(streamReader);
    }

    @Override public void serializeInstance(SerializationStreamWriter streamWriter, ValueSupplier<Object> instance)
        throws SerializationException
    {
        serialize(streamWriter, instance);
    }

    //~ Methods ......................................................................................................................................

    /** Deserialize the object.. Not implemented because the code is already in instantiate */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader streamReader, ValueSupplier<Object> instance) {
        // nothing more to do
    }

    public static ValueSupplier<Object> instantiate(SerializationStreamReader r)
        throws SerializationException
    {
        try {
            final StreamReader reader = new GwtStreamReader(r);
            return new ValueSupplier<>(reader.readObjectConst());
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public static void serialize(SerializationStreamWriter w, ValueSupplier<Object> valueSupplier)
        throws SerializationException
    {
        try {
            final StreamWriter writer = new GwtStreamWriter(w);
            writer.writeObjectConst(valueSupplier.get());
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }
}  // end class ValueSupplier_CustomFieldSerializer
