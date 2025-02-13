
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.time.DayOfWeek;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.DateConfiguration;

/**
 * User class for Form: NewSync
 */
public class NewSync extends NewSyncBase {

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

    @NotNull @Override public Action addMessageToFirst() {
        getTable().get(0).message(Field.STR_COL, A_NEW_MESSAGE);
        return actions.getDefault();
    }

    @NotNull @Override public Action configFirst() {
        final DateConfiguration config = getTable().get(0).configuration(Field.DATE_COL);
        config.disable(DayOfWeek.MONDAY);
        return actions.getDefault();
    }

    @Override public void load() {
        setComboColOptions(ImmutableList.of("Alf", "Beto"));

        final FormTable<TableRow> table = getTable();

        for (int i = 0; i < 3; i++) {
            final TableRow row = table.add();
            row.setStrCol("str" + i);
            row.setIntCol(i);
            row.setDateCol(DateOnly.current().addDays(i));
        }
    }

    @NotNull @Override public Action resetFirst() {
        getTable().get(0).reset(Field.STR_COL);
        return actions.getDefault();
    }

    @NotNull @Override public Action setOptionsGlobal() {
        setComboColOptions(ImmutableList.of("Alf", "Beto"));
        return actions.getDefault();
    }

    @NotNull @Override public Action setOptionsToFirst() {
        getTable().get(0).setComboColOptions(ImmutableList.of("Pey", "Luke", "Polez"));
        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    public static final String A_NEW_MESSAGE = "A new message";

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}  // end class NewSync
