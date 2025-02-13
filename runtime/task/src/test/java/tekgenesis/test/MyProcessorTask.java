
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

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/**
 * User class for Task: MyProcessorTask
 */
@SuppressWarnings("JavaDoc")
public class MyProcessorTask extends MyProcessorTaskBase<Object> {

    //~ Instance Fields ..............................................................................................................................

    public int processed;

    private boolean mustAbort  = false;
    private boolean mustIgnore = false;
    private boolean mustUndo   = false;

    //~ Constructors .................................................................................................................................

    protected MyProcessorTask(@NotNull final ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public Seq<Object> enumerate() {
        return ImmutableList.fromArray(new Object[100]);
    }

    @Nullable @Override public Status process(@Nullable Object o) {
        if (mustAbort && processed == 55) return Status.abort("abort");
        if (mustIgnore && processed == 60) return Status.ignore("ignore");
        if (mustUndo && processed == 70) return Status.undo("undo");
        processed++;
        setData(String.valueOf(processed));
        return Status.ok();
    }

    public void setMustAbort(final boolean mustAbort) {
        this.mustAbort = mustAbort;
    }
    public void setMustIgnore(final boolean mustIgnore) {
        this.mustIgnore = mustIgnore;
    }
    public void setMustUndo(boolean mustUndo) {
        this.mustUndo = mustUndo;
    }
}  // end class MyProcessorTask
