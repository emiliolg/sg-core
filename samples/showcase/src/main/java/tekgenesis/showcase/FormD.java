
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.form.Action;

/**
 * User class for Form: FormD
 */
@Generated(value = "tekgenesis/showcase/TypesShowcase.mm", date = "1367354621820")
@SuppressWarnings("WeakerAccess")
public class FormD extends FormDBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the 'Create new' options of the suggest_box(a) is clicked. */
    @NotNull @Override public Action some(@Nullable String text) {
        return actions.getDefault().withMessage("Attemptin to create new: " + text);
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}
