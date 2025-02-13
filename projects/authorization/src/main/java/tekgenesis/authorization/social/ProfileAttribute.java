
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.json.JsonMapping;

import static tekgenesis.common.Predefined.cast;

/**
 * Profile attribute.
 */
public interface ProfileAttribute<T> {

    //~ Instance Fields ..............................................................................................................................

    AttributeType<Boolean> BOOLEAN = a -> {
                                         final Boolean attempt = getPrimitive(Boolean.class, a);
                                         if (attempt == null && a instanceof String) return Boolean.valueOf((String) a);
                                         return attempt;
                                     };

    AttributeType<Integer> INTEGER = a -> {
                                         final Integer attempt = getPrimitive(Integer.class, a);
                                         if (attempt != null) return attempt;
                                         if (a instanceof String) return Integer.parseInt((String) a);
                                         if (a instanceof Number) return ((Number) a).intValue();
                                         return null;
                                     };

    AttributeType<Long> LONG = a -> {
                                   final Long attempt = getPrimitive(Long.class, a);
                                   if (attempt != null) return attempt;
                                   if (a instanceof String) return Long.parseLong((String) a);
                                   if (a instanceof Number) return ((Number) a).longValue();
                                   return null;
                               };

    AttributeType<String> STRING = a -> getPrimitive(String.class, a);

    //~ Methods ......................................................................................................................................

    /** Get attribute value in given context. */
    T get(@NotNull final Map<String, Object> values);

    /** Attribute name. */
    String name();

    /** Set attribute value on given context. */
    void set(@NotNull final Map<String, Object> values, @Nullable final Object value);

    /** Attribute type. */
    AttributeType<T> type();

    /** Set attribute value on given context (extract value from json node). */
    void setFromJson(@NotNull final Map<String, Object> values, @NotNull final JsonNode value);

    //~ Methods ......................................................................................................................................

    /** Return attribute primite value. */
    @Nullable static <T> T getPrimitive(@NotNull Class<T> type, @Nullable Object attribute) {
        return attribute != null && type.isInstance(attribute) ? cast(attribute) : null;
    }

    //~ Inner Interfaces .............................................................................................................................

    interface AttributeType<T> {
        /** Convert value into T. */
        @Nullable T convert(@Nullable Object attribute);
    }

    //~ Inner Classes ................................................................................................................................

    class Definition<T> implements ProfileAttribute<T> {
        private final String           name;
        private final AttributeType<T> type;

        private Definition(String name, AttributeType<T> type) {
            this.name = name;
            this.type = type;
        }

        @Override public T get(@NotNull Map<String, Object> values) {
            return type.convert(values.get(name));
        }

        @Override public String name() {
            return name;
        }

        @Override public void set(@NotNull Map<String, Object> values, @Nullable Object value) {
            values.put(name, value);
        }

        @Override public AttributeType<T> type() {
            return type;
        }

        @Override public void setFromJson(@NotNull Map<String, Object> values, @NotNull final JsonNode node) {
            final JsonNode value = getJsonNode(node);
            // noinspection IfStatementWithTooManyBranches
            if (value != null && !value.isNull()) {
                if (value.isBoolean()) set(values, value.asBoolean());
                else if (value.isNumber()) set(values, value.numberValue());
                else if (value.isTextual()) set(values, value.asText());
                else set(values, value);
            }
        }

        /** Return json node. */
        public JsonNode getJsonNode(@NotNull JsonNode node) {
            JsonNode       result = node;
            final String[] parts  = name.split("\\.");
            for (final String part : parts)
                result = result.get(part);
            return result;
        }

        /** Create attribute definition. */
        public static <T> Definition<T> define(@NotNull final String name, @NotNull final AttributeType<T> type) {
            return new Definition<>(name, type);
        }
    }  // end class Definition

    /**
     * Attribute of type list.
     */
    class ListAttribute<E> implements AttributeType<List<E>> {
        protected final Class<E> elementClass;

        /** Construct list attribute for specified element class. */
        public ListAttribute(@NotNull final Class<E> elementClass) {
            this.elementClass = elementClass;
        }

        @Nullable @Override public List<E> convert(@Nullable Object a) {
            if (a instanceof ArrayNode) {
                final ArrayNode    array  = (ArrayNode) a;
                final List<E>      result = new ArrayList<>();
                final ObjectMapper mapper = JsonMapping.json();
                array.forEach(n -> result.add(convertElement(mapper, n)));
                return result;
            }
            return Colls.emptyList();
        }

        protected E convertElement(ObjectMapper mapper, JsonNode n) {
            return mapper.convertValue(n, elementClass);
        }
    }
}  // end interface ProfileAttribute
