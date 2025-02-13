
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
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.trace.Trace;
import tekgenesis.common.util.ProgressMeter;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.metric.core.CallMetric;
import tekgenesis.metric.core.MetricsFactory;
import tekgenesis.sg.TaskEntry;

import static tekgenesis.metric.core.MetricSources.TASK;

/**
 * Task Instance.
 */
public abstract class TaskInstance<This extends TaskInstance<This>> implements LoggableInstance {

    //~ Instance Fields ..............................................................................................................................

    private final String  name;
    private ProgressMeter progressMeter;

    private final Task<This> task;

    private TaskEntry taskEntry;

    //~ Constructors .................................................................................................................................

    TaskInstance(Task<This> task) {
        this.task     = task;
        progressMeter = null;
        taskEntry     = null;
        name          = getClass().getName();
    }

    //~ Methods ......................................................................................................................................

    /** @return  true/false if the task should run */
    public boolean shouldExecute() {
        return true;
    }

    /** Get Task Batch Size. */
    public abstract int getBatchSize();

    /** Returns Task data. */
    public String getData() {
        return taskEntry == null ? "" : taskEntry.getData();
    }

    /** Set Data.* */
    public void setData(@NotNull String data) {
        if (taskEntry != null) taskEntry.setData(data);
    }

    /** Returns Task data time. */
    @Nullable public DateTime getDataTime() {
        return taskEntry == null ? null : taskEntry.getDataTime();
    }

    /** Set the data time. */
    public void setDataTime(DateTime dataTime) {
        if (taskEntry != null) taskEntry.setDataTime(dataTime);
    }

    /** Get Task Exclusion Group. */
    @NotNull public abstract String getExclusionGroup();

    /** Get Mdc for the task. */
    public String getMdc() {
        return taskEntry == null ? "" : taskEntry.getMdc();
    }

    /** Returns the task name. */
    public String getName() {
        return name;
    }

    /** Returns the Progress Meter. */
    public ProgressMeter getProgressMeter() {
        if (progressMeter == null) initProgressMeter();

        return progressMeter;
    }

    /** Return task purge policy. */
    public abstract int getPurgePolicy();

    /** Get Task Transaction Mode. */
    @NotNull public abstract TransactionMode getTransactionMode();

    @NotNull
    @Trace(dispatcher = true)
    protected Status _run() {
        try(final CallMetric m = MetricsFactory.call(TASK, getClass(), "run")) {
            m.start();
            final Status status = run();
            m.mark(status.isSuccess());
            return status;
        }
    }
    @NotNull protected abstract Status run();

    boolean manageTransaction() {
        return getTransactionMode() == TransactionMode.NONE;
    }

    void updateTaskEntry() {
        if (taskEntry != null) taskEntry.update();
    }

    boolean isSuspend() {
        return TaskEntry.create(task.getFqn()).isSuspended();
    }

    TaskEntry getTaskEntry() {
        return taskEntry;
    }

    void setTaskEntry(TaskEntry taskEntry) {
        this.taskEntry = taskEntry;
    }

    private void initProgressMeter() {
        final ProgressMeter.Builder builder = new ProgressMeter.Builder(getClass().getName());

        progressMeter = builder.withLogger(task.getInstance().logger()).build();
    }
}  // end class TaskInstance
