
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

import tekgenesis.common.core.DateOnly;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static tekgenesis.showcase.NewTableBase.Permission.*;

/**
 * User class for Form: NewTable
 */
public class NewTable extends NewTableBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action addClicked() {
        for (final Integer index : getTable().getCurrentIndex()) {
            final TableRow row = getTable().getCurrent();
            row.setStrCol("str" + index);
            row.setIntCol(index);
            row.setDateCol(DateOnly.current().addDays(index));
        }
        return actions.getDefault();
    }

    @Override public void load() {
        final FormTable<TableRow> table = getTable();

        for (int i = 0; i < 10; i++) {
            final TableRow row = table.add();
            row.setStrCol("string" + i);
            row.setIntCol(i);
            row.setDateCol(DateOnly.current().addDays(i));
        }

        forms.hasPermission(ADD_ROW);
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}
