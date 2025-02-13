
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

import tekgenesis.common.core.DateOnly;
import tekgenesis.form.Action;

/**
 * User class for Form: EntityDateForm
 */
@SuppressWarnings("WeakerAccess")
public class DateEntityForm extends DateEntityFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeReturnDate() {
        if (isDefined(Field.LEAVE_DATE)) {
            final DateOnly returnDate = getReturnDate();
            if (returnDate != null) {
                if (returnDate.daysFrom(getLeaveDate()) < 1) setReturnDate(getLeaveDate());
            }
            else setReturnDate(getLeaveDate());
        }
        return actions.getDefault();
    }
}
