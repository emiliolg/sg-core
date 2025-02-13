
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import org.jetbrains.annotations.NotNull;

/**
 * Chart Type.
 */
public enum ChartType {

    //~ Enum constants ...............................................................................................................................

    COLUMN, LINE, PIE, BAR;

    //~ Methods ......................................................................................................................................

    /** Return is chart type is given. */
    public boolean is(@NotNull final ChartType type) {
        return this == type;
    }
}
