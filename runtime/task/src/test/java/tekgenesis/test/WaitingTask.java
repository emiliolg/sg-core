
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
 * User class for Task: WaitingTask
 */
public class WaitingTask extends WaitingTaskBase {

    //~ Constructors .................................................................................................................................

    private WaitingTask(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        try {
            synchronized (this) {
                wait();
            }
        }
        catch (final InterruptedException e) {
            // ignore
        }
        return Status.ok();
    }
}
