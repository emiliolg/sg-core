
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
 * User class for Form: LoadDisplayForm
 */
public class LoadDisplayForm extends LoadDisplayFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override
    @SuppressWarnings("MagicNumber")
    public Action display() {
        System.out.println("DISPLAY!!! " + getString());
        try {
            Thread.sleep(2000);
        }
        catch (final InterruptedException e) {
            // ignore...
        }
        setString("string " + System.currentTimeMillis());
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        System.out.println("LOAD!!!");
    }
}
