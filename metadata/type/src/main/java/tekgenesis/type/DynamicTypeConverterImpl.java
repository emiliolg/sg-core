
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Conversions;
import tekgenesis.type.exception.TypeConversionException;

import static tekgenesis.common.Predefined.unreachable;

/**
 * An implementation off the DynamicType Converter.
 */
public class DynamicTypeConverterImpl implements DynamicTypeConverter {

    //~ Instance Fields ..............................................................................................................................

    private final Function<String, Object> fromString;
    private final Function<Object, String> toString;
    private final Type                     type;

    //~ Constructors .................................................................................................................................

    /** Create the Converter. */
    public DynamicTypeConverterImpl(final Type type) {
        fromString = value -> fromString(value, type);
        toString   = value -> toString(value, type);
        this.type  = type;
    }

    //~ Methods ......................................................................................................................................

    /** Type safe conversion from String values. */
    @NotNull public Iterable<Object> fromString(@NotNull final Iterable<String> values) {
        return Colls.map(values, fromString);
    }

    /** Type safe conversion from String value. */
    public Object fromString(@NotNull final String value) {
        return fromString.apply(value);
    }

    /** Type safe conversion to String values. */
    @NotNull public Iterable<String> toString(@NotNull final Iterable<Object> values) {
        return Colls.map(values, toString);
    }

    /** Type safe conversion to String value. */
    public String toString(@NotNull final Object value) {
        return toString.apply(value);
    }

    @Nullable private Object fromString(final String value, final Type t) {
        try {
            final Kind kind = t.getKind();
            if (kind == Kind.DATE || kind == Kind.DATE_TIME) return Long.valueOf(value);

            final Class<?> implementationClass = t.getImplementationClass();
            if (implementationClass == null) throw unreachable();
            return Conversions.fromString(value, implementationClass);
        }
        catch (final Exception e) {
            final Object defaultValue = type.getDefaultValue();
            LOGGER.error("Error converting value '" + value + "' to type " + type + ". Returning default value: '" + defaultValue + "'.", e);
            return defaultValue;
        }
    }  // end method fromString

    @Nullable private String toString(final Object value, final Type t) {
        final Kind kind = t.getKind();
        if (kind == Kind.DATE || kind == Kind.DATE_TIME) {
            if (!(value instanceof Long)) throw new TypeConversionException(value.toString(), Long.class, value.getClass());
            return String.valueOf(value);
        }

        final Class<?> implementationClass = t.getKind().getImplementationClass();
        if (implementationClass == null || !implementationClass.isInstance(value))
            throw new TypeConversionException(value.toString(), implementationClass, value.getClass());

        return Conversions.toString(value);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(DynamicTypeConverterImpl.class);
}  // end class DynamicTypeConverterImpl
