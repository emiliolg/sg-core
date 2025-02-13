
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for widget: OnChangesWidget
 */
public abstract class OnChangesWidget extends OnChangesWidgetBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when text_field(b) value changes. */
    @NotNull @Override public Action bValueChanged() {
        call("b");
        return actions().getDefault();
    }

    /** Invoked when text_field(c) value changes. */
    @NotNull @Override public Action cValueChanged() {
        call("c");
        return actions().getDefault();
    }

    abstract void call(@NotNull final String source);
}
