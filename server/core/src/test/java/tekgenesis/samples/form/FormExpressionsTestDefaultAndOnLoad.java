
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
 * Test class.
 */
@SuppressWarnings("WeakerAccess")
public class FormExpressionsTestDefaultAndOnLoad extends FormExpressionsTestDefaultAndOnLoadBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        setCredentials("user:password");
    }

    /** Invoked when text_field(credentials) value changes. */
    @NotNull @Override public Action updateAvailability() {
        if (isDefined(Field.CREDENTIALS)) setAvailable(5);
        return actions.getDefault();
    }
}
