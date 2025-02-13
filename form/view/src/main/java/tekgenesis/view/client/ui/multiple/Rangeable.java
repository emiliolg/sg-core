
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

import com.google.gwt.event.shared.HandlerRegistration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.view.client.ui.ItemsRange;

/**
 * Allows visible range over generic domain.
 */
public interface Rangeable {

    //~ Methods ......................................................................................................................................

    /** Add a {@link LensRefreshEvent.Handler}. */
    @NotNull HandlerRegistration addLensRefreshHandler(LensRefreshEvent.Handler handler);

    /** Add a {@link RangeChangeEvent.Handler}. */
    @NotNull HandlerRegistration addRangeChangeHandler(@NotNull final RangeChangeEvent.Handler handler);

    /** Get the multiple items count. How many items are there in the model. */
    int getItemsCount();

    /** Get the multiple items range: which items are there in the ui. */
    @NotNull ItemsRange getVisibleRange();

    /** Set the multiple items range: which items to be there in the ui. */
    void setVisibleRange(@NotNull ItemsRange range);
}
