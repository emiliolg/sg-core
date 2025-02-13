
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
 * User class for Form: DisabledForm
 */
public class DisabledForm extends DisabledFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clicked() {
        sleep();
        System.out.println("Clicked!");
        return actions.getDefault();
    }

    /** Invoked when button(mainAction) is clicked. */
    @NotNull @Override public Action mainAction() {
        sleep();
        System.out.println("Main Action");
        return actions.getDefault();
    }

    /** Invoked when label(secondaryAction) is clicked. */
    @NotNull @Override public Action secondaryAction() {
        sleep();
        System.out.println("Secondary Action");
        return actions.getDefault();
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            // ignore
        }
    }
}
