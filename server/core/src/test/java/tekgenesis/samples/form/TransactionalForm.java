
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
 * User class for Form: TransactionalForm
 */
public class TransactionalForm extends TransactionalFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action create() {
        super.create();
        return action();
    }

    @NotNull @Override public Action createEntity() {
        TransactionalEntity.create(getKey()).insert();
        return action();
    }

    @NotNull @Override public Action delete() {
        super.delete();
        return action();
    }

    @NotNull private Action action() {
        if (isFail()) throw new RuntimeException("Fail!");
        return isError() ? actions.getError().withMessage("Error!") : actions.getDefault();
    }
}
