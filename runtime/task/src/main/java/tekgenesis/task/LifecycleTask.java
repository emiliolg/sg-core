
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import org.jetbrains.annotations.NotNull;

/**
 * Scheduled Task Instance.
 */
public class LifecycleTask extends Task<LifecycleTaskInstance> {

    //~ Instance Fields ..............................................................................................................................

    private boolean onStartup = true;

    //~ Constructors .................................................................................................................................

    /** Create a Lifecycle Task. */
    public LifecycleTask(@NotNull final Class<? extends LifecycleTaskInstance> taskClass) {
        super(taskClass);
    }

    LifecycleTask(@NotNull final String taskFqn) {
        super(taskFqn);
    }

    //~ Methods ......................................................................................................................................

    /** Mark task as shutdown. */
    public LifecycleTask onShutdown() {
        onStartup = false;
        getInstance().asShutdown();
        return this;
    }

    /** Mark task as startup.* */
    public LifecycleTask onStartup() {
        onStartup = true;
        return this;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4917351947303131238L;
}  // end class LifecycleTask
