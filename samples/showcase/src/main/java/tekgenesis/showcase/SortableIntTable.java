
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

import tekgenesis.form.FormTable;

/**
 * User class for Form: SortableIntTable
 */
public class SortableIntTable extends SortableIntTableBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        if (isAutoLoad()) {
            final Random              r     = new Random();
            final int                 rows  = 5 + r.nextInt(10);
            final FormTable<TableRow> table = getTable();

            for (int i = 0; i < rows; i++)
                table.add().populate();
        }
    }

    @NotNull @Override public Object populate() {
        return new Object();
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {
        @SuppressWarnings({ "MagicNumber", "JavaDoc" })
        public void populate() {
            final Random r = new Random();
            setInteger(r.nextInt(100));
            final char c = (char) (65 + r.nextInt(25));

            setString(r.nextBoolean() ? "" + c : null);
        }
    }
}
