
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

/**
 * User class for Form: PatenteForm
 */
public class CustomMaskForm extends CustomMaskFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void onLoad() {
        setS1("###-AA");
        setS2("AAA-##");
    }

    @NotNull @Override public Action validate() {
        setPatNew2("asd12");
        setPatNew3("asd123");
        return actions.getDefault();
    }
}
