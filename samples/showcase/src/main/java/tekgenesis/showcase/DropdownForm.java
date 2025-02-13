
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
 * User class for form: DropdownForm
 */
public class DropdownForm extends DropdownFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when label(first) is clicked Invoked when label(second) is clicked. */
    @NotNull @Override public Action firstClick() {
        return actions().getDefault();
    }

    @Override public void load() {
        getTable().add();
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {
        @NotNull @Override public Action any() {
            setAvailable(!isAvailable());
            return actions().getDefault();
        }
    }
}
