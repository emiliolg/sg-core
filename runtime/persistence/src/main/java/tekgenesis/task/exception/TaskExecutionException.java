
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

/**
 * Base Class for Task Exceptions.
 */
public class TaskExecutionException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** TaskExecution Exception. */
    public TaskExecutionException(@NotNull Throwable e) {
        super(e);
    }

    /** TaskExecution Exception. */
    public TaskExecutionException(@NotNull String msg) {
        super(msg);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8851450253968269035L;
}
