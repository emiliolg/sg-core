
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: GroupShowcaseForm
 */
public class GroupShowcaseForm extends GroupShowcaseFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action doStuff() {
        return actions.getDefault();
    }

    @Override public void load() {
        for (int i = 0; i < 5; i++) {
            final TableRow    add = getTable().add();
            final BigTableRow row = getBigTable().add();
            add.setDisp("Row " + i);
            row.setDisp2(String.valueOf(i));
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class BigTableRow extends BigTableRowBase {}

    public class TableRow extends TableRowBase {}
}
