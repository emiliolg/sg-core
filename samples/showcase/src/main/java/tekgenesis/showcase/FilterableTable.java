
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;

/**
 * User class for Form: FilterableTable
 */
public class FilterableTable extends FilterableTableBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action add10() {
        add(10);
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        add(ROWS);
    }

    @NotNull @Override public Action nextPage() {
        getItems().setCurrentPage(getItems().getCurrentPage() + 1);
        return actions.getDefault();
    }

    @NotNull @Override public Action remove10() {
        final FormTable<ItemsRow> items = getItems();
        for (int i = 0; i < 10; i++)
            items.remove(0);
        return actions.getDefault();
    }

    private void add(int rows) {
        final ImmutableList<SimpleEntity> entities = selectFrom(SIMPLE_ENTITY).toList();
        final Random                      r        = new Random(1);
        final FormTable<ItemsRow>         items    = getItems();
        for (int i = 0; i < rows; i++) {
            final ItemsRow row = items.add();
            row.setNumber(r.nextInt(BASE));
            row.setLabel("rowlabel" + i);
            row.setDate(DateOnly.current().addDays(i));
            if (!entities.isEmpty()) row.setEntity(entities.get(r.nextInt(entities.size())));
            row.setOption(Options.values()[r.nextInt(Options.values().length)]);
            row.setValid(r.nextInt(2) == 0);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int BASE = 50;

    private static final int ROWS = 15;

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {}
}  // end class FilterableTable
