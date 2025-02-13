
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

/**
 * Something that has a FieldOption.
 */
public interface HasFieldOption {

    //~ Methods ......................................................................................................................................

    /** Return the default value of the option. */
    @Nullable Object getDefaultValue();
    /** If the FieldOption is of type ENUM_T return the associated Enum Class. */
    @Nullable <T extends Enum<T>> Class<T> getEnumClass();
    /** Return the fieldOption. */
    @NotNull FieldOption getFieldOption();
}
