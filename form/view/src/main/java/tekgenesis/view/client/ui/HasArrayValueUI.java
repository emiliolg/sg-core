
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.NotNull;

/**
 * UI interface for array valued widgets.
 */
public interface HasArrayValueUI extends HasValueUI {

    //~ Methods ......................................................................................................................................

    /** Returns the array values. */
    @NotNull Iterable<?> getValues();

    /** Sets the array values. */
    void setValues(@NotNull final Iterable<Object> values);

    /** Sets the array values and fire the event. JUST FOR TESTING */
    void setValues(@NotNull final Iterable<Object> values, boolean fireEvents);
}
