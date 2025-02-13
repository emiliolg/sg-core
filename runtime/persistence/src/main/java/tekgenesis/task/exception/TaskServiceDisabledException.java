
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.exception;

/**
 * Raised whrn trying to run a task and the service is disabled.
 */
public class TaskServiceDisabledException extends TaskExecutionException {

    //~ Constructors .................................................................................................................................

    /** Construct exception. */
    public TaskServiceDisabledException() {
        super("Task Service is disabled");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4813335741119696741L;
}
