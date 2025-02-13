
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

import tekgenesis.view.client.ui.tablefilters.Comparison;

/**
 * Filter Lens Event.
 */
public class FilterEvent implements LensEvent {

    //~ Instance Fields ..............................................................................................................................

    private final String     column;
    private final Comparison comparison;
    private final String     value;

    //~ Constructors .................................................................................................................................

    public FilterEvent(String col, Comparison c, String v) {
        column     = col;
        comparison = c;
        value      = v;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean refresh() {
        return true;
    }

    /** Returns the column name over the filter is applied. */
    public String getColumn() {
        return column;
    }

    /** Returns the comparison that the filter is using. */
    public Comparison getComparison() {
        return comparison;
    }

    /** Returns the value that the user has set. */
    public String getValue() {
        return value;
    }
}
