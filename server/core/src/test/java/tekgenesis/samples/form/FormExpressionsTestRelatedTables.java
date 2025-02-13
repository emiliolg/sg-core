
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.form;

import tekgenesis.form.FormTable;

@SuppressWarnings({ "WeakerAccess", "JavaDoc", "MagicNumber" })
public class FormExpressionsTestRelatedTables extends FormExpressionsTestRelatedTablesBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        final FormTable<FirstRow> first = getFirst();
        first.add().setC(10);

        final FormTable<SecondRow> second = getSecond();
        second.add();
        second.add().setY(20);
    }

    //~ Inner Classes ................................................................................................................................

    public class FirstRow extends FormExpressionsTestRelatedTablesBase.FirstRowBase {}

    public class SecondRow extends FormExpressionsTestRelatedTablesBase.SecondRowBase {}
}
