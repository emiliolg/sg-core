
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.properties.Column;

import static java.lang.String.valueOf;

/**
 * Table Column Builder.
 */
public class TableColumnB extends FoBuilder<TableColumnB, TableColumn> implements Column<TableColumnB> {

    //~ Methods ......................................................................................................................................

    public TableColumn build() {
        return new TableColumn().withProperties(getProperties());
    }

    @Override public TableColumnB columnWidth(String value) {
        return ColumnHelper.columnWidth(this, value);
    }

    /**
     * Specifies the number of columns spanned by table-cells that may use properties from this
     * fo:table-column formatting object using the from-table-column() function.
     */
    public TableColumnB span(int n) {
        return addProperty(Fo.NUMBER_COLUMNS_SPANNED, valueOf(n));
    }

    //~ Methods ......................................................................................................................................

    /** Returns a new table block. */
    public static TableColumnB tableColumn() {
        return new TableColumnB();
    }
}
