
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;

/**
 * Converter for Dynamic widget Types.
 */
public interface DynamicTypeConverter {

    //~ Methods ......................................................................................................................................

    /** Type safe conversion from String values. */
    @NotNull Iterable<Object> fromString(@NotNull final Iterable<String> values);

    /** Type safe conversion from String value. */
    Object fromString(@NotNull final String value);

    /** Type safe conversion to String values. */
    @NotNull Iterable<String> toString(@NotNull final Iterable<Object> values);

    /** Type safe conversion to String value. */
    String toString(@NotNull final Object value);
}
