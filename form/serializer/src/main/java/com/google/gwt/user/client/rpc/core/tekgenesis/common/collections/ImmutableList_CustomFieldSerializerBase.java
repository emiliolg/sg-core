
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.rpc.core.tekgenesis.common.collections;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.collections.ImmutableList;

import static tekgenesis.common.collections.ImmutableList.builder;

/**
 * ImmutableList Serializer.
 */
public final class ImmutableList_CustomFieldSerializerBase {

    //~ Constructors .................................................................................................................................

    private ImmutableList_CustomFieldSerializerBase() {}

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("EmptyMethod")  // All is done at instantiate time.
    public static void deserialize(SerializationStreamReader streamReader, ImmutableList<?> instance) {
        // Nothing to do...
    }

    public static ImmutableList<?> instantiate(SerializationStreamReader r)
        throws SerializationException
    {
        final int                           size    = r.readInt();
        final ImmutableList.Builder<Object> builder = builder(size);

        for (int i = 0; i < size; i++)
            builder.add(r.readObject());

        return builder.build();
    }

    public static void serialize(SerializationStreamWriter w, ImmutableList<?> instance)
        throws SerializationException
    {
        w.writeInt(instance.size());
        for (final Object obj : instance)
            w.writeObject(obj);
    }
}
