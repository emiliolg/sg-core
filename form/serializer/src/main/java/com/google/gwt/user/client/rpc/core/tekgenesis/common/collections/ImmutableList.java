
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.rpc.core.tekgenesis.common.collections;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Dummy class for nesting the custom serializer.
 */
public final class ImmutableList {

    //~ Inner Classes ................................................................................................................................

    /**
     * ImmutableList.Array Serializer.
     */
    public static final class Array_CustomFieldSerializer extends CustomFieldSerializer<tekgenesis.common.collections.ImmutableList<?>> {
        @Override public void deserializeInstance(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            deserialize(streamReader, instance);
        }

        @Override public boolean hasCustomInstantiateInstance() {
            return true;
        }

        @Override public tekgenesis.common.collections.ImmutableList<?> instantiateInstance(SerializationStreamReader r)
            throws SerializationException
        {
            return instantiate(r);
        }

        @Override public void serializeInstance(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            serialize(streamWriter, instance);
        }

        public static void deserialize(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            ImmutableList_CustomFieldSerializerBase.deserialize(streamReader, instance);
        }

        public static tekgenesis.common.collections.ImmutableList<?> instantiate(SerializationStreamReader r)
            throws SerializationException
        {
            return ImmutableList_CustomFieldSerializerBase.instantiate(r);
        }

        public static void serialize(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            ImmutableList_CustomFieldSerializerBase.serialize(streamWriter, instance);
        }
    }

    /**
     * ImmutableList.EmptyList Serializer.
     */
    public static final class EmptyList_CustomFieldSerializer extends CustomFieldSerializer<tekgenesis.common.collections.ImmutableList<?>> {
        @Override public void deserializeInstance(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            deserialize(streamReader, instance);
        }

        @Override public boolean hasCustomInstantiateInstance() {
            return true;
        }

        @Override public tekgenesis.common.collections.ImmutableList<?> instantiateInstance(SerializationStreamReader r)
            throws SerializationException
        {
            return instantiate(r);
        }

        @Override public void serializeInstance(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            serialize(streamWriter, instance);
        }

        public static void deserialize(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            ImmutableList_CustomFieldSerializerBase.deserialize(streamReader, instance);
        }

        public static tekgenesis.common.collections.ImmutableList<?> instantiate(SerializationStreamReader r)
            throws SerializationException
        {
            return ImmutableList_CustomFieldSerializerBase.instantiate(r);
        }

        public static void serialize(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            ImmutableList_CustomFieldSerializerBase.serialize(streamWriter, instance);
        }
    }

    /**
     * ImmutableList.Singleton Serializer.
     */
    public static final class Singleton_CustomFieldSerializer extends CustomFieldSerializer<tekgenesis.common.collections.ImmutableList<?>> {
        @Override public void deserializeInstance(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            deserialize(streamReader, instance);
        }

        @Override public boolean hasCustomInstantiateInstance() {
            return true;
        }

        @Override public tekgenesis.common.collections.ImmutableList<?> instantiateInstance(SerializationStreamReader r)
            throws SerializationException
        {
            return instantiate(r);
        }

        @Override public void serializeInstance(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            serialize(streamWriter, instance);
        }

        public static void deserialize(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            ImmutableList_CustomFieldSerializerBase.deserialize(streamReader, instance);
        }

        public static tekgenesis.common.collections.ImmutableList<?> instantiate(SerializationStreamReader r)
            throws SerializationException
        {
            return ImmutableList_CustomFieldSerializerBase.instantiate(r);
        }

        public static void serialize(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            ImmutableList_CustomFieldSerializerBase.serialize(streamWriter, instance);
        }
    }

    /**
     * ImmutableList.Wrapper Serializer.
     */
    public static final class Wrapper_CustomFieldSerializer extends CustomFieldSerializer<tekgenesis.common.collections.ImmutableList<?>> {
        @Override public void deserializeInstance(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            deserialize(streamReader, instance);
        }

        @Override public boolean hasCustomInstantiateInstance() {
            return true;
        }

        @Override public tekgenesis.common.collections.ImmutableList<?> instantiateInstance(SerializationStreamReader r)
            throws SerializationException
        {
            return instantiate(r);
        }

        @Override public void serializeInstance(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            serialize(streamWriter, instance);
        }

        public static void deserialize(SerializationStreamReader streamReader, tekgenesis.common.collections.ImmutableList<?> instance) {
            ImmutableList_CustomFieldSerializerBase.deserialize(streamReader, instance);
        }

        public static tekgenesis.common.collections.ImmutableList<?> instantiate(SerializationStreamReader r)
            throws SerializationException
        {
            return ImmutableList_CustomFieldSerializerBase.instantiate(r);
        }

        public static void serialize(SerializationStreamWriter streamWriter, tekgenesis.common.collections.ImmutableList<?> instance)
            throws SerializationException
        {
            ImmutableList_CustomFieldSerializerBase.serialize(streamWriter, instance);
        }
    }
}  // end class ImmutableList
