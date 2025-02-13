
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.common.Predefined.cast;

/**
 * A FieldOption with an Enum class.
 */
public class ExtendedFieldOption implements HasFieldOption {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private final Object defaultValue;

    @Nullable private final Class<?> enumClass;

    @NotNull private final FieldOption option;

    //~ Constructors .................................................................................................................................

    ExtendedFieldOption(@NotNull FieldOption option, @Nullable Class<?> enumClass, @Nullable Object defaultValue) {
        this.option       = option;
        this.enumClass    = enumClass;
        this.defaultValue = defaultValue;
    }

    //~ Methods ......................................................................................................................................

    /** Returns an ExtendedFieldOption with the specified Field Value. */
    @NotNull public ExtendedFieldOption withDefault(Object value) {
        return new ExtendedFieldOption(option, enumClass, value);
    }

    @Nullable public Object getDefaultValue() {
        return defaultValue;
    }

    @Nullable @Override public <T extends Enum<T>> Class<T> getEnumClass() {
        return cast(enumClass);
    }

    @NotNull @Override public FieldOption getFieldOption() {
        return option;
    }
}
