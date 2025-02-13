
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.util.Files;
import tekgenesis.common.util.Reflection;

import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;

import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.common.util.Conversions.fromString;
import static tekgenesis.common.util.Files.asUnchecked;
import static tekgenesis.common.util.Reflection.setPrivateField;

/**
 * Custom IxInstance Deserializer.
 */
public final class IxInstanceJsonDeserializer<T extends IxInstance<T, K>, K> extends JsonDeserializer<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Class<T> clazz;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public IxInstanceJsonDeserializer(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("AssignmentToNull")
    public T deserialize(JsonParser jp, DeserializationContext context) {
        final T                   instance = Reflection.construct(clazz);
        final TableMetadata<T, K> md       = instance.metadata();
        JsonToken                 token;

        while ((token = nextToken(jp)) != null && token != END_OBJECT) {
            final String fieldName = asUnchecked(jp::getCurrentName);
            nextToken(jp);

            md.getField(fieldName).ifPresent(field -> {
                final Class<?> type = field.getType();

                final boolean isNullValue = jp.getCurrentToken() == JsonToken.VALUE_NULL;

                final Object value;
                if (field.isPrimaryKey()) {
                    if (isNullValue && Number.class.isAssignableFrom(type)) value = Integer.MAX_VALUE;
                    else value = isNullValue ? null : fromString(text(jp), type);

                    setPrivateField(instance, fieldName, value);
                }
                else {
                    value = isNullValue ? null : fromString(text(jp), type);
                    // Invoke setMethod
                    Reflection.invoke(instance, setterName(fieldName), value);
                }
            });
        }
        return instance;
    }

    private JsonToken nextToken(JsonParser jp) {
        return asUnchecked(jp::nextToken);
    }

    private String text(JsonParser jp) {
        return asUnchecked((Files.IOSupplier<String>) jp::getText);
    }
}  // end class IxInstanceJsonDeserializer
