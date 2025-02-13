
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;

/**
 * Scheduled Task Instance.
 */
public abstract class ScheduledTaskInstance extends TaskInstance<ScheduledTaskInstance> {

    //~ Instance Fields ..............................................................................................................................

    private final ScheduledTask task;

    //~ Constructors .................................................................................................................................

    protected ScheduledTaskInstance(final ScheduledTask task) {
        super(task);
        this.task = task;
    }

    //~ Methods ......................................................................................................................................

    /** Get Cron Expression. */
    @NotNull public abstract String getCronExpression();

    /** Return a fixed duetime for the current instance. */
    @Nullable public DateTime getNextDueTime() {
        return null;
    }

    /** Get Cron Expression. */
    @NotNull public abstract String getScheduleAfter();

    protected boolean mustStop() {
        return task.mustStop();
    }
}  // end class ScheduledTaskInstance
