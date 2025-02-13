
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

/**
 * User class for Form: InternalValidations
 */
public class InternalValidations extends InternalValidationsBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button($B3) is clicked. */
    @NotNull @Override public Action clicked() {
        System.out.println("InternalValidations.clicked");
        return actions.getDefault();
    }
}
