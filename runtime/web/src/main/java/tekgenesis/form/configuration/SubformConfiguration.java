
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

/**
 * Subform configuration interface.
 */
public interface SubformConfiguration extends WidgetConfiguration {

    //~ Methods ......................................................................................................................................

    /** Return subform visibility. */
    boolean isVisible();

    /** Set subform visibility. */
    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    SubformConfiguration setVisible(boolean visible);
}
