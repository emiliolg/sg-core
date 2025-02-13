
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
import tekgenesis.form.FormTable;

import static java.lang.String.valueOf;

/**
 * User class for form: MultipleTablesSync
 */
@SuppressWarnings("MagicNumber")
public class MultipleTablesSync extends MultipleTablesSyncBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action loadTables() {
        final FormTable<PendingsRow> pendings = getPendings();
        pendings.clear();

        for (int i = 0; i < getRows(); i++) {
            final PendingsRow p = pendings.add();
            final String      s = valueOf(i);
            p.setCurly(s);
            p.setLarry(s);
            p.setMoe(s);
        }

        setTabs(getRows() % 2);

        return actions().getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class PendingsRow extends PendingsRowBase {
        @NotNull @Override public Action export() {
            return actions().getDefault();
        }
        @NotNull @Override public Action print() {
            return actions().getDefault();
        }
    }
}  // end class MultipleTablesSync
