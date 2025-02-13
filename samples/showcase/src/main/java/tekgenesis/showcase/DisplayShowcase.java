
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
 * Display Showcase form class.
 */
@SuppressWarnings("WeakerAccess")
public class DisplayShowcase extends DisplayShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action addRow() {
        getItems().add();
        return actions.getDefault().withMessage("Row added!");
    }

    @NotNull @Override public Action simpleSync() {
        return actions.getDefault().withMessage("sync");
    }

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {}
}
