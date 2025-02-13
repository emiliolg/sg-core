
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

@SuppressWarnings({ "WeakerAccess", "JavaDoc", "MagicNumber" })
public class FormExpressionsTestTables extends FormExpressionsTestTablesBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        final FormTable<TableRow> table = getTable();
        table.add().setCol1(10);
        table.add().setCol1(20);
        table.add().setCol1(30);
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends FormExpressionsTestTablesBase.TableRowBase {
        /** Invoked when text_field(col3) value changes. */
        @NotNull @Override public Action setCol2Value() {
            setCol2(getCol1() * 2);
            return actions.getDefault();
        }
    }
}
