
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
 * Lifecycle Task Instance.
 */
public abstract class LifecycleTaskInstance extends TaskInstance<LifecycleTaskInstance> {

    //~ Instance Fields ..............................................................................................................................

    private boolean isStartup = true;

    //~ Constructors .................................................................................................................................

    protected LifecycleTaskInstance(final LifecycleTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    /**  */
    @NotNull public abstract Status onShutdown();
    /**  */
    @NotNull public abstract Status onStartup();

    @NotNull @Override protected Status run() {
        return isStartup ? onStartup() : onShutdown();
    }

    void asShutdown() {
        isStartup = false;
    }
}
