
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

@SuppressWarnings({ "WeakerAccess", "JavaDoc" })
public class FormExpressionsTestDefaults extends FormExpressionsTestDefaultsBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action setItem2Value() {
        setItem2(getItem1() * 2);
        return actions.getDefault();
    }
}
