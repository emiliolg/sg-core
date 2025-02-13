
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * Form MetaModel Serializer.
 */
@SuppressWarnings("WeakerAccess")  // GWT
public class FormModel_CustomFieldSerializer extends CustomFieldSerializer<FormModel> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, FormModel instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public FormModel instantiateInstance(SerializationStreamReader streamReader)
        throws SerializationException
    {
        return instantiate(streamReader);
    }

    @Override public void serializeInstance(SerializationStreamWriter streamWriter, FormModel instance)
        throws SerializationException
    {
        serialize(streamWriter, instance);
    }

    //~ Methods ......................................................................................................................................

    /** Deserialize the values of the MetaModel. */
    public static void deserialize(SerializationStreamReader r, FormModel formModel)
        throws SerializationException
    {
        try {
            formModel.deserialize(new GwtStreamReader(r), false);
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    /** Instantiate a MetaModel from the SerializationStreamReader. */
    public static FormModel instantiate(SerializationStreamReader r)
        throws SerializationException
    {
        try {
            return FormModel.instantiate(new GwtStreamReader(r));
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    /** Serialize a MetaModel. */
    public static void serialize(SerializationStreamWriter w, FormModel formModel)
        throws SerializationException
    {
        try {
            formModel.serialize(new GwtStreamWriter(w), false);
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }
}  // end class FormModel_CustomFieldSerializer
