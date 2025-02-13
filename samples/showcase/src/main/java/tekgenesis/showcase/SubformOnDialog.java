
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
 * User class for Form: SubformOnDialog
 */
public class SubformOnDialog extends SubformOnDialogBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action doMergeReception() {
        System.out.println("Doing something...");
        return actions.getDefault();
    }

    @NotNull @Override public Action doShowMergeReception() {
        setMergeReceptionDialog(true);
        return actions.getDefault();
    }

    @Override public void load() {
        createMergeReceptionSubForm();

        getTableInDialog().add().setString("table in dialog 1");
        getTableInDialog().add().setString("table in dialog 2");
        getTableInDialog().add().setString("table in dialog 3");
    }

    //~ Inner Classes ................................................................................................................................

    public class TableInDialogRow extends TableInDialogRowBase {}
}
