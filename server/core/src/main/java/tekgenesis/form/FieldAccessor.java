
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.widget.Widget;

/**
 * Field accessor used for wrapping forms and table instances.
 */
public interface FieldAccessor {

    //~ Methods ......................................................................................................................................

    /** Set a field value using class setter. */
    void setField(@NotNull final Widget field, @Nullable final Object value);

    /** Set a field value without using class setter (~ under the hood). */
    void setSlot(@NotNull final Widget field, @Nullable final Object value);
}
