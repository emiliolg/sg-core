
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

/**
 * User class for Form: SortableTable
 */
public class SortableTable extends SortableTableBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeScalar() {
        System.out.println("getTable().getCurrentIndex().get() = " + getTable().getCurrentIndex().getOrNull());
        return actions.getDefault();
    }

    @Override public void load() {
        final Random              r     = new Random();
        final int                 rows  = BASE + r.nextInt(10);
        final FormTable<TableRow> table = getTable();
        // final FormTable<Table2Row> table2 = getTable2();

        for (int i = 0; i < rows; i++)
            table.add().populate(i);

        /*for (int i = 0; i < rows; i++)
         *  table2.add().setString2("string" + i);*/
    }

    @NotNull @Override public Action select() {
        if (getTable().current().isPresent()) System.out.println(GET_CURRENT_INDEX + getTable().getCurrentIndex().get());
        else System.out.println(NO_CURRENT);

        if (getTable().previous().isPresent()) System.out.println(GET_PREVIOUS_INDEX + getTable().getPreviousIndex().get());
        else System.out.println("Sin last.");

        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final int BASE = 200;

    @NonNls private static final String NO_CURRENT = "No current.";

    @NonNls private static final String GET_CURRENT_INDEX  = "getTable().getCurrentIndex() = ";
    @NonNls private static final String GET_PREVIOUS_INDEX = "getTable().getPreviousIndex() = ";

    //~ Inner Classes ................................................................................................................................

    // public static class Table2Row extends Table2RowBase {}

    @SuppressWarnings("MagicNumber")
    public class TableRow extends TableRowBase {
        @NotNull @Override public Action change() {
            if (isDefined()) System.out.println(GET_CURRENT_INDEX + getTable().indexOf(this));
            else System.out.println(NO_CURRENT);

            if (getTable().previous().isPresent()) System.out.println(GET_PREVIOUS_INDEX + getTable().getPreviousIndex().get());
            else System.out.println("Sin last.");

            return actions.getDefault();
        }

        /** Populate a rows. */
        public void populate(int index) {
            final Random r = new Random();
            setKey(index);
            setInteger(index);
            setCurrency(r.nextInt(1000) * r.nextDouble());
            final char c = (char) (65 + r.nextInt(25));
            setString("" + c);
        }

        @NotNull @Override public Iterable<SimpleEntity> suggest(@Nullable String query) {
            if (isDefined()) System.out.println(GET_CURRENT_INDEX + getTable().indexOf(this));
            else System.out.println(NO_CURRENT);

            if (getTable().previous().isPresent()) System.out.println(GET_PREVIOUS_INDEX + getTable().getPreviousIndex().get());
            else System.out.println("Sin last.");

            final List<SimpleEntity> r = new ArrayList<>();
            SimpleEntity.forEach(r::add);
            return r;
        }
    }
}  // end class SortableTable
