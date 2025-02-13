
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.jmx;

import org.jetbrains.annotations.NotNull;

/**
 * MBean Task Info Implementation.
 */
public final class Task implements TaskMBean {

    //~ Instance Fields ..............................................................................................................................

    private final tekgenesis.task.Task<?> task;

    //~ Constructors .................................................................................................................................

    /**
     * Default constructor.
     *
     * @param  task  Task
     */
    public Task(@NotNull tekgenesis.task.Task<?> task) {
        this.task = task;
    }

    //~ Methods ......................................................................................................................................

    /** Stops the current execution of the task. Nothing, If the task is not running */
    @Override public void stop() {
        task.cancel(true);
    }

    /** Returns the completion status.* */
    @Override public int getCompletion() {
        return task.getCompletion();
    }

    @Override public String getPhase() {
        return task.getPhase();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6064115612934683419L;
}
