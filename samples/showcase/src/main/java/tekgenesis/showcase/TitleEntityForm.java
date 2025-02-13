
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
 * User class for Form: TitleEntityForm
 */
public class TitleEntityForm extends TitleEntityFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action saveAndRedirect() {
        setIdKey(ID_KEY);
        super.create();
        return actions.navigate(TitleEntityForm.class, Integer.toString(getIdKey()));
    }

    //~ Static Fields ................................................................................................................................

    private static final int ID_KEY = 830;
}
