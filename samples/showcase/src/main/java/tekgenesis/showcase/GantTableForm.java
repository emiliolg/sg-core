
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
 * User class for Form: GantTableForm
 */
public class GantTableForm extends GantTableFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<GantRow> gant = getGant();
        add(gant, "Chelen", "Martes 23:00 a Jueves 13:00", 43, 24);
        add(gant, "Salvador", "", 0, 50);
        add(gant, "Roberto", "", 15.8, 80);
        add(gant, "Lucas", "", 0, 89.99);
        add(gant, "Agus", "", 10, 100);
    }

    private void add(FormTable<GantRow> gant, String name, String description, double from, double to) {
        final GantRow activity = gant.add();
        activity.setDescription(description);
        activity.setName(name);
        activity.setFrom(from);
        activity.setAmount(to);
    }

    //~ Inner Classes ................................................................................................................................

    public class GantRow extends GantRowBase {}
}
