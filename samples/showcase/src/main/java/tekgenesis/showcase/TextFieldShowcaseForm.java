
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
 * Text Field Showcase Form class.
 */
@SuppressWarnings("WeakerAccess")
public class TextFieldShowcaseForm extends TextFieldShowcaseFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changedText() {
        return actions.getError().withMessage("Text changed");
    }

    @NotNull @Override public Action createNewRow() {
        getTable().add();
        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}
