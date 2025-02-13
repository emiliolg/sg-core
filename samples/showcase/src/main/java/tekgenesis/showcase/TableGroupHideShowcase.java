
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.form.FormTable;

/**
 * User class for Form: TableGroupHideShowcase
 */
public class TableGroupHideShowcase extends TableGroupHideShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<TableRow> table = getTable();
        table.add();
        table.add().setType(Type.B);
        table.add().setType(Type.C);
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}
