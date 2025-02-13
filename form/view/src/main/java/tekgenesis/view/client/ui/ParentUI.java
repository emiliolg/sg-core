
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

import tekgenesis.common.core.Option;

/**
 * Represents a reference to corresponding {@link ModelUI model ui} parent.
 */
public interface ParentUI {

    //~ Methods ......................................................................................................................................

    /** Return {@link WidgetUI anchor}. Maybe same as {@link #ui()}. */
    @NotNull WidgetUI anchor();

    /** Return {@link Option<Integer> optional item}. */
    @NotNull Option<Integer> item();

    /** Return {@link ModelUI ui}. Maybe same as {@link #anchor()}. */
    @NotNull ModelUI ui();
}
