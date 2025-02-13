
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.FoContainer;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Table Cell fo element.
 */
public class TableCell extends FoContainer {

    //~ Constructors .................................................................................................................................

    protected TableCell(Block child) {
        super("table-cell");
        children = listOf(child);
    }
}
