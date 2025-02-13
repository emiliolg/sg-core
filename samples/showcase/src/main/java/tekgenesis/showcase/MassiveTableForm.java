
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.FormTable;

/**
 * User class for Form: MassiveTableForm
 */
public class MassiveTableForm extends MassiveTableFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        final FormTable<ItemsRow> items = getItems();
        for (int i = 0; i < ROWS; i++) {
            final ItemsRow row = items.add();
            final int      r   = i + 1;
            row.setLabel("Row Label " + r);
            row.setNumber(r);
            row.setDate(DateOnly.current().addDays(i));
            row.setExpiration(DateTime.current().addHours(i * i));
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int ROWS = 200;

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {}
}
