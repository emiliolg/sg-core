
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;

/**
 * On Change method handler.
 */
public interface OnChangeHandler {

    //~ Methods ......................................................................................................................................

    /** Handle widget on change method. */
    void handleOnChange(@NotNull final Widget widget, @NotNull final Option<ItemContext> section);

    //~ Inner Classes ................................................................................................................................

    class Default implements OnChangeHandler {
        // Empty default implementation
        @Override public void handleOnChange(@NotNull final Widget widget, @NotNull final Option<ItemContext> section) {}
    }
}  // end interface OnChangeHandler
