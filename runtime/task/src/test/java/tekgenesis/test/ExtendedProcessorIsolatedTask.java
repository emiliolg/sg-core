
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

import static tekgenesis.common.Predefined.notNull;

/**
 * User class for Task: ExtendedProcessorIsolatedTask
 */
public class ExtendedProcessorIsolatedTask extends ExtendedProcessorIsolatedTaskBase<Object>
    implements ExtendedProcessorBaseTask.ExtendedProcesorTask
{

    //~ Instance Fields ..............................................................................................................................

    final ExtendedProcessorBaseTask processorTask;

    //~ Constructors .................................................................................................................................

    private ExtendedProcessorIsolatedTask(@NotNull ScheduledTask task) {
        super(task);
        processorTask = new ExtendedProcessorBaseTask(task);
    }

    //~ Methods ......................................................................................................................................

    @Override public Status after() {
        return notNull(processorTask.after(), super.after());
    }

    @Override public Status before() {
        return notNull(processorTask.before(), super.before());
    }

    @Nullable @Override public Seq<Object> enumerate() {
        return processorTask.enumerate();
    }

    public void init(int i) {
        processorTask.init(i);
    }

    @Nullable @Override public Status process(@Nullable Object o) {
        return processorTask.process(o);
    }
    public void returnAtAfter(Status s) {
        processorTask.returnAtAfter(s);
    }

    public void returnAtBefore(Status s) {
        processorTask.returnAtBefore(s);
    }

    @Override public ExtendedProcessorBaseTask getBase() {
        return processorTask;
    }
}  // end class ExtendedProcessorIsolatedTask
