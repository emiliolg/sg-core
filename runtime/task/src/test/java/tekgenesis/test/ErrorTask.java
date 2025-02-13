
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import org.jetbrains.annotations.NotNull;

import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/**
 * User class for Task: ErrorTask
 */
public class ErrorTask extends ErrorTaskBase {

    //~ Instance Fields ..............................................................................................................................

    private String errorMsg = null;

    //~ Constructors .................................................................................................................................

    private ErrorTask(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        return Status.error(errorMsg);
    }

    public void setErrorMsg(String s) {
        errorMsg = s;
    }
}
