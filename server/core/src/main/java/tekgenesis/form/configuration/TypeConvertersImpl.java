
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.TypeConverters;
import tekgenesis.type.DynamicTypeConverter;
import tekgenesis.type.DynamicTypeConverterImpl;
import tekgenesis.type.Type;

/**
 * Type Converter.
 */
public class TypeConvertersImpl implements TypeConverters {

    //~ Constructors .................................................................................................................................

    /** Public constructor for injection. */
    public TypeConvertersImpl() {}

    //~ Methods ......................................................................................................................................

    /** Return a new DynamicTypeConverter for a given Type. */
    @NotNull @Override public DynamicTypeConverter converterForType(@NotNull Type type) {
        return new DynamicTypeConverterImpl(type);
    }
}
