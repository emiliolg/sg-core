
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
 * User class for Form: SynchronousForm
 */
public class SynchronousForm extends SynchronousFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clicked() {
        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException ignored) {}
        System.out.println("SynchronousForm.clicked");
        return actions.getDefault();
    }

    @NotNull @Override public Action error() {
        System.out.println("SynchronousForm.error");
        return actions.getError();
    }
}
