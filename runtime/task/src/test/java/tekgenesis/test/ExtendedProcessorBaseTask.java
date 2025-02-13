
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
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.task.ProcessorTaskInstance;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * User class for Task: ExtendedProcessorTask
 */
public class ExtendedProcessorBaseTask extends ProcessorTaskInstance<Object> {

    //~ Instance Fields ..............................................................................................................................

    private Option<Status>      after  = Option.empty();
    private Option<Status>      before = Option.empty();
    private int                 id;
    private final ScheduledTask task;

    //~ Constructors .................................................................................................................................

    protected ExtendedProcessorBaseTask(ScheduledTask task) {
        super(task);
        this.task = task;
    }

    //~ Methods ......................................................................................................................................

    @Nullable public Status after() {
        after.ifPresent((s) -> persistIsolated(10));
        return after.getOrNull();
    }

    @Nullable public Status before() {
        before.ifPresent((s) -> persistIsolated(20));
        return before.getOrNull();
    }

    @Nullable public Seq<Object> enumerate() {
        return listOf(1, 2, 3);
    }

    public void init(int i) {
        id = i;
    }

    @Override public Logger logger() {
        return task.getInstance().logger();
    }

    @Nullable public Status process(@Nullable Object o) {
        final Isolated isolated = Isolated.find(id);
        assert isolated != null;
        persistIsolated(isolated.getCount() + 1);
        return Status.ok();
    }
    public void returnAtAfter(@Nullable Status s) {
        after = Option.option(s);
    }

    public void returnAtBefore(@Nullable Status s) {
        before = Option.option(s);
    }

    @Override public int getBatchSize() {
        return 0;
    }

    @NotNull @Override public String getCronExpression() {
        return "";
    }

    @NotNull @Override public String getExclusionGroup() {
        return "";
    }

    @Override public int getPurgePolicy() {
        return 0;
    }

    @NotNull @Override public String getScheduleAfter() {
        return "";
    }

    @NotNull @Override public TransactionMode getTransactionMode() {
        return TransactionMode.NONE;
    }

    private void persistIsolated(int c) {
        final Isolated isolated = Isolated.find(id);
        assert isolated != null;
        isolated.setCount(c);
        isolated.persist();
    }

    //~ Inner Interfaces .............................................................................................................................

    public interface ExtendedProcesorTask {
        ExtendedProcessorBaseTask getBase();
    }
}  // end class ExtendedProcessorBaseTask
