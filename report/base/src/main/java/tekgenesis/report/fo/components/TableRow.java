
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
 * Table Row fo element.
 */
public class TableRow extends FoContainer {

    //~ Constructors .................................................................................................................................

    protected TableRow(List<TableCell> cells) {
        super("table-row");
        children.addAll(cells);
    }
}
