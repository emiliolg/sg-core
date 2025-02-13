
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: NumbersForm
 */
@Generated(value = "tekgenesis/showcase/TextFieldShowcase.mm", date = "1374670338214")
public class NumbersForm extends NumbersFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action create() {
        super.create();
        return actions.navigate(NumbersForm.class, keyAsString());
    }
}
