
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * OnSuggestParams Serializer.
 */
@SuppressWarnings("WeakerAccess")  // GWT
public class OnSuggestParams_CustomFieldSerializer extends CustomFieldSerializer<OnSuggestParams> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, OnSuggestParams instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public OnSuggestParams instantiateInstance(SerializationStreamReader streamReader)
        throws SerializationException
    {
        return instantiate(streamReader);
    }

    @Override public void serializeInstance(SerializationStreamWriter streamWriter, OnSuggestParams instance)
        throws SerializationException
    {
        serialize(streamWriter, instance);
    }

    //~ Methods ......................................................................................................................................

    /** Deserialize the values of the OnSuggestParams. */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader r, OnSuggestParams instance) {
        // nothing to do.
    }

    /** Instantiate an OnSuggestParams from the SerializationStreamReader. */
    public static OnSuggestParams instantiate(SerializationStreamReader r)
        throws SerializationException
    {
        final String  query      = r.readString();
        final Object  arg        = r.readObject();
        final boolean deprecated = r.readBoolean();
        return new OnSuggestParams(query, arg, deprecated);
    }

    /** Serialize an OnSuggestParams. */
    public static void serialize(SerializationStreamWriter w, OnSuggestParams instance)
        throws SerializationException
    {
        w.writeString(instance.getQuery());
        w.writeObject(instance.getArg());
        w.writeBoolean(instance.isDeprecated());
    }
}  // end class OnSuggestParams_CustomFieldSerializer
