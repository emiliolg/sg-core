
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: StoreForm
 */
public class StoreForm extends StoreFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action cancel() {
        final Action action = super.cancel();
        action.withInvocation("storeCancelled");
        return action;
    }

    @NotNull @Override public Action create() {
        final Action action = super.create();
        action.withInvocation("storeCreated").arg(keyAsString());
        return action;
    }

    @NotNull @Override public Action update() {
        final Action action = super.update();
        action.withInvocation("storeUpdated").arg(keyAsString());
        return action;
    }
}
