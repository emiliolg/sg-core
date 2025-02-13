
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import tekgenesis.common.util.Reflection;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Strings.getterName;

/**
 * Ix Serialization class.
 */
class IxInstanceJsonSerializer<T extends IxInstance<T, K>, K> extends JsonSerializer<T> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    IxInstanceJsonSerializer() {}

    //~ Methods ......................................................................................................................................

    @Override public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException
    {
        gen.writeStartObject();
        for (final TableField<?> tf : value.metadata().getFields()) {
            final TableField<Object> tableField = cast(tf);
            final String             fldName    = tableField.getFieldName();

            final Class<Object> type = tableField.getType();
            gen.writeFieldName(fldName);

            final Object obj = Reflection.invoke(value, getterName(fldName, type.getSimpleName()));
            if (obj == null) gen.writeNull();
            else {
                final JsonSerializer<Object> serializer = resolveSerializer(serializers, tableField);
                serializer.serialize(obj, gen, serializers);
            }
        }

        gen.writeEndObject();
    }

    private JsonSerializer<Object> resolveSerializer(SerializerProvider serializers, TableField<Object> tableField)
        throws JsonMappingException
    {
        Class<?> type = tableField.getType();

        if (tableField.isPrimaryKey() && useCustomSerializerFor(type)) {
            // We have to redefined the type when
            if (type.isAssignableFrom(Integer.class)) type = int.class;
            if (type.isAssignableFrom(Double.class)) type = double.class;
        }

        return serializers.findValueSerializer(type, null);
    }

    private boolean useCustomSerializerFor(Class<?> type) {
        return type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Double.class);
    }
}
