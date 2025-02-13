
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.model;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * KeyMap Serializer.
 */
@SuppressWarnings("WeakerAccess")  // GWT
public class KeyMap_CustomFieldSerializer extends CustomFieldSerializer<KeyMap> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, KeyMap instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public KeyMap instantiateInstance(SerializationStreamReader streamReader)
        throws SerializationException
    {
        return instantiate(streamReader);
    }

    @Override public void serializeInstance(SerializationStreamWriter streamWriter, KeyMap instance)
        throws SerializationException
    {
        serialize(streamWriter, instance);
    }

    //~ Methods ......................................................................................................................................

    /** Deserialize the values of the KeyMap. */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader r, KeyMap instance) {
        // nothing to do.
    }

    /** Instantiate an KeyMap from the SerializationStreamReader. */
    public static KeyMap instantiate(SerializationStreamReader r) {
        return KeyMap.instantiate(new GwtStreamReader(r));
    }

    /** Serialize an KeyMap. */
    public static void serialize(SerializationStreamWriter w, KeyMap instance) {
        instance.serialize(new GwtStreamWriter(w));
    }
}  // end class KeyMap_CustomFieldSerializer
