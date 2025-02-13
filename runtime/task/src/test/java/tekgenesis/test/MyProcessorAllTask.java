
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/**
 * User class for Task: MyProcessorAllTask
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber", "JavaDoc" })
public class MyProcessorAllTask extends MyProcessorAllTaskBase<Object> {

    //~ Instance Fields ..............................................................................................................................

    public int      processed = 0;
    private boolean done      = false;

    private boolean error     = false;
    private boolean mustAbort = false;

    //~ Constructors .................................................................................................................................

    protected MyProcessorAllTask(@NotNull final ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public Seq<Object> enumerate() {
        return Colls.immutable(Arrays.asList(new Object[100]));
    }

    @Nullable @Override public Status process(@Nullable Object o) {
        if (mustAbort && processed == 55) return Status.abort("abort");

        processed++;
        setData(String.valueOf(processed));
        if (error && processed == 55) return Status.error("error");
        if (done && processed == 55) return Status.done();
        return Status.ok();
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    public void setMustAbort(final boolean mustAbort) {
        this.mustAbort = mustAbort;
    }
}  // end class MyProcessorAllTask
