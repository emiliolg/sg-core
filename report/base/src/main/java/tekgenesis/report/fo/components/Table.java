
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import java.util.List;

import tekgenesis.report.fo.FoContainer;

/**
 * Table fo element.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class Table extends FoContainer {

    //~ Constructors .................................................................................................................................

    protected Table(List<TableColumn> columns, List<TableRow> rows) {
        super("table");
        children.addAll(columns);
        children.add(new TableBody(rows));
    }

    //~ Inner Classes ................................................................................................................................

    protected class TableBody extends FoContainer {
        protected TableBody(List<TableRow> rows) {
            super("table-body");
            children.addAll(rows);
        }
    }
}
