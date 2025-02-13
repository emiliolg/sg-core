
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.type.DynamicTypeConverter;
import tekgenesis.type.Type;

/**
 * Type Converter.
 */
public interface TypeConverters {

    //~ Methods ......................................................................................................................................

    /** Return a new DynamicTypeConverter for a given Type. */
    @NotNull DynamicTypeConverter converterForType(@NotNull final Type type);
}
