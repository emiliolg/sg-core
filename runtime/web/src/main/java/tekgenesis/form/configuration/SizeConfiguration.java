
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
 * Widget size configuration interface.
 */
interface SizeConfiguration<This extends SizeConfiguration<This>> extends WidgetConfiguration {

    //~ Methods ......................................................................................................................................

    /** Set widget width and height. */
    @NotNull This dimension(int w, int h);

    /** Set widget height. */
    @NotNull This height(int h);

    /** Set widget width. */
    @NotNull This width(int w);
}
