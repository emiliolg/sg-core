
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sg;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.database.Databases;
import tekgenesis.persistence.Sql;
import tekgenesis.sg.g.TaskExecutionLogBase;
import tekgenesis.sg.g.TaskExecutionLogTable;
import tekgenesis.task.Status;
import tekgenesis.task.TaskStatus;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.expr.Expr.CURRENT_TIME;
import static tekgenesis.sg.g.TaskExecutionLogTable.TASK_EXECUTION_LOG;
import static tekgenesis.task.TaskStatus.*;

/**
 * User class for Model: TaskExecutionLog
 */
public class TaskExecutionLog extends TaskExecutionLogBase implements AutoCloseable {

    //~ Methods ......................................................................................................................................

    @Override public void close() {
        if (getStatus() == TaskStatus.RUNNING) setStatus(TaskStatus.FINISHED);
        setEndTime(DateTime.current());
        update();
    }

    /** Log the end of an execution. */
    public void end(@NotNull TaskEntry taskEntry, Status status) {
        final Integer logId = taskEntry.getCurrentLogId();
        if (logId == null) return;
        // Set values in order to apply truncate to max size
        setErrorMessage(status.getMessage());
        setData(taskEntry.getData());
        Sql.update(TL)
            .set(TL.STATUS, status.isSuccess() ? FINISHED : FAILED)
            .set(TL.END_TIME, CURRENT_TIME)
            .set(TL.DATA_TIME, taskEntry.getDataTime())
            .set(TL.DATA, getData())
            .set(TL.ERROR_MESSAGE, getErrorMessage())
            .where(TL.ID.eq(logId))
            .execute();
    }

    //~ Methods ......................................................................................................................................

    /** List the instances of 'TaskExecutionLog' that matches the given parameters. */
    @NotNull public static ImmutableList<TaskExecutionLog> listByName(@NotNull String name) {
        return selectFrom(TASK_EXECUTION_LOG).where(TASK_EXECUTION_LOG.NAME.eq(name))
               .limit(100)
               .orderBy(TASK_EXECUTION_LOG.START_TIME.descending())
               .list();
    }

    /** Log the start of an execution. */
    public static TaskExecutionLog start(@NotNull TaskEntry te) {
        final TaskExecutionLog l = create().setName(te.getName())
                                   .setStatus(RUNNING)
                                   .setMdc(te.getMdc())
                                   .setStartTime(Databases.openDefault().currentTime())
                                   .setTaskUpdateTime(te.getUpdateTime())
                                   .setExpirationTime(te.getExpirationTime())
                                   .setMember(te.getMember())
                                   .setData(te.getData())
                                   .setDataTime(te.getDataTime())
                                   .insert();
        te.setStartTime(te.getUpdateTime());
        te.setCurrentLog(l);
        te.update();
        return l;
    }

    /**  */
    public static void updateCounts(@NotNull TaskEntry te, int totalItems, int successItem, int failedItems, int ignoredItems) {
        final Integer logId = te.getCurrentLogId();
        if (logId == null) return;

        Sql.update(TL)
            .set(TL.TOTAL_ITEMS, totalItems)
            .set(TL.SUCCESS_ITEMS_COUNT, successItem)
            .set(TL.ERROR_ITEMS_COUNT, failedItems)
            .set(TL.IGNORE_ITEMS_COUNT, ignoredItems)
            .where(TL.ID.eq(logId))
            .execute();
    }

    //~ Static Fields ................................................................................................................................

    public static final TaskExecutionLogTable TL = TaskExecutionLogTable.TASK_EXECUTION_LOG;
}  // end class TaskExecutionLog
