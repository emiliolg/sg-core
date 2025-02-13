
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.exception;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

/**
 * Task Already running exception.
 */
public class TaskAlreadyRunningException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Create exception. */
    public TaskAlreadyRunningException(@NotNull String taskName) {
        super(format("Task '%s' already running.", taskName));
    }

    /** Create exception. */
    public TaskAlreadyRunningException(@NotNull String taskName, @NotNull String member) {
        super(format("Task '%s' already running in member '%s'.", taskName, member));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3918043235045394383L;
}
