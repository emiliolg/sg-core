
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import java.util.ArrayList;
import java.util.List;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.report.fo.properties.Border;

/**
 * Table Builder.
 */
public class TableB extends FoBuilder<TableB, Table> implements Border<TableB> {

    //~ Instance Fields ..............................................................................................................................

    private final List<TableColumnB> columns = new ArrayList<>();
    private final List<TableRowB>    rows    = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    @Override public TableB borderBeforeStyle(String value) {
        return BorderHelper.borderBeforeStyle(this, value);
    }

    @Override public TableB borderBeforeWidth(String value) {
        return BorderHelper.borderBeforeWidth(this, value);
    }

    @Override public TableB borderBottom(String value) {
        return BorderHelper.borderBottom(this, value);
    }

    @Override public TableB borderColor(String value) {
        return BorderHelper.borderColor(this, value);
    }

    @Override public TableB borderLeft(String value) {
        return BorderHelper.borderLeft(this, value);
    }

    @Override public TableB borderRight(String value) {
        return BorderHelper.borderRight(this, value);
    }

    @Override public TableB borderStyle(String value) {
        return BorderHelper.borderStyle(this, value);
    }

    @Override public TableB borderTop(String value) {
        return BorderHelper.borderTop(this, value);
    }

    @Override public TableB borderWidth(String value) {
        return BorderHelper.borderWidth(this, value);
    }

    public Table build() {
        return new Table(buildCols(), buildRows()).withProperties(getProperties());
    }

    /** Builds columns. */
    public List<TableColumn> buildCols() {
        final List<TableColumn> c = new ArrayList<>();
        for (final TableColumnB col : columns)
            c.add(col.build());
        return c;
    }

    /** Builds rows. */
    public List<TableRow> buildRows() {
        final List<TableRow> c = new ArrayList<>();
        for (final TableRowB row : rows)
            c.add(row.build());
        return c;
    }

    /** Bulk column add. */
    public TableB columns(TableColumnB... tableColumns) {
        columns.addAll(ImmutableList.fromArray(tableColumns));
        return this;
    }

    /** Bulk row add. */
    public TableB rows(TableRowB... tableRows) {
        rows.addAll(ImmutableList.fromArray(tableRows));
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** @return  create a new TableB */
    public static TableB table() {
        return new TableB();
    }
}  // end class TableB
