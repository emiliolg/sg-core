
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

import tekgenesis.form.Action;

/**
 * User class for Form: StudentForm
 */
@Generated(value = "tekgenesis/showcase/TableShowcase.mm", date = "1369845021463")
public class StudentForm extends StudentFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        for (int i = 0; i < 5; i++)
            getTable().add().setColumn(i);
    }

    @NotNull @Override public Action widgetsForm() {
        return actions.navigate(SimpleEntityForm.class).leaveWithConfirmation();
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}
