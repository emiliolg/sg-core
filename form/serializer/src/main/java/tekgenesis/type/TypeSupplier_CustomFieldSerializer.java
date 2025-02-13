
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
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

@SuppressWarnings({ "JavaDoc", "WeakerAccess" })
public class TypeSupplier_CustomFieldSerializer extends CustomFieldSerializer<TypeSupplier> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, TypeSupplier instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public TypeSupplier instantiateInstance(SerializationStreamReader streamReader)
        throws SerializationException
    {
        return instantiate(streamReader);
    }

    @Override public void serializeInstance(SerializationStreamWriter streamWriter, TypeSupplier instance)
        throws SerializationException
    {
        serialize(streamWriter, instance);
    }

    //~ Methods ......................................................................................................................................

    /** Deserialize the object.. Not implemented because the code is already in instantiate */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader streamReader, TypeSupplier instance) {
        // nothing more to do
    }

    public static TypeSupplier instantiate(SerializationStreamReader r)
        throws SerializationException
    {
        try {
            final Type type = Kind.instantiateType(new GwtStreamReader(r));
            return new TypeSupplier(type);
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    public static void serialize(SerializationStreamWriter w, TypeSupplier typeSupplier)
        throws SerializationException
    {
        try {
            final Type type = typeSupplier.get();
            Kind.serializeType(new GwtStreamWriter(w), type);
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }
}  // end class TypeSupplier_CustomFieldSerializer
