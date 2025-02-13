
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sg;

import java.util.UUID;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Either;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.CronExpression;
import tekgenesis.database.Databases;
import tekgenesis.database.exception.UniqueViolationException;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.QueryTuple;
import tekgenesis.persistence.Sql;
import tekgenesis.sg.g.TaskEntryBase;
import tekgenesis.sg.g.TaskEntryTable;
import tekgenesis.task.Status;
import tekgenesis.task.TaskProps;
import tekgenesis.task.TaskStatus;
import tekgenesis.task.exception.TaskAlreadyRunningException;

import static java.util.concurrent.TimeUnit.SECONDS;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Either.left;
import static tekgenesis.common.core.Either.right;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.metadata.task.TaskConstants.NEVER_CRON;
import static tekgenesis.persistence.Sql.insertInto;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.expr.Expr.CURRENT_TIME;
import static tekgenesis.sg.g.TaskEntryTable.TASK_ENTRY;
import static tekgenesis.task.TaskStatus.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * User class for Model: Task
 */
public class TaskEntry extends TaskEntryBase {

    //~ Methods ......................................................................................................................................

    /** End the execution of this TaskEntry todo move transaction handling here. */
    public void endExecution(Status status, @Nullable DateTime nextDueTime) {
        runInTransaction(() -> {
            // Reread in case of failure to refresh Data
            final TaskEntry te = status.isSuccess() ? this : notNull(find(getName()), this);
            te.doEndExecution(status, nextDueTime);
        });
    }

    @NotNull public TaskEntry setSuspended(boolean suspended) {
        super.setSuspended(suspended);
        if (!suspended) calculateSchedule(CronExpression.parse(getCronExpression()).getOrNull());
        return this;
    }

    private void calculateSchedule(@Nullable CronExpression cronExpression) {
        final CronExpression saveCron = CronExpression.parse(getCronExpression()).getOrNull();
        // If it was overwrite in the DB. If not, use defined in mm or env
        final CronExpression ce = saveCron == null ? cronExpression : saveCron;
        changeSchedule(ce);
    }

    /**  */
    private void changeSchedule(@Nullable CronExpression ce) {
        if (ce == null) {
            setCronExpression("");
            setDueTime(DateTime.MAX_VALUE);
            setStatus(NOT_SCHEDULED);
        }
        else {
            setStatus(SCHEDULED);

            setCronExpression(ce.toString());

            final DateTime timeAfter = ce.getTimeAfter(invokeInTransaction(() -> Databases.openDefault().currentTime()));
            if (timeAfter == null) calculateSchedule(null);
            else setDueTime(timeAfter);
        }
    }

    private void doEndExecution(Status status, @Nullable DateTime nextDueTime) {
        if (getCurrentLog() != null) getCurrentLog().end(this, status);

        final CronExpression c = isSuspended() ? null : CronExpression.parse(getCronExpression()).getOrNull();
        if (c == null) setStatus(status.isSuccess() || status.isIgnored() ? FINISHED : FAILED);
        else calculateSchedule(c);

        // Overwrite due time
        if (nextDueTime != null) setDueTime(nextDueTime);

        setExpirationTime(null);
        setMember("");
        setStartTime(null);
        // update();

        Sql.update(TE)
            .set(TE.MEMBER, "")
            .set(TE.DATA, getData())
            .set(TE.DATA_TIME, getDataTime())
            .setAsNull(TE.EXPIRATION_TIME)
            .setAsNull(TE.START_TIME)
            .set(TE.STATUS, getStatus())
            .set(TE.DUE_TIME, getDueTime())
            .set(TE.CRON_EXPRESSION, getCronExpression())
            .where(TE.NAME.eq(getName()))
            .execute();
    }

    //~ Methods ......................................................................................................................................

    /** Change cron expression. */
    public static TaskEntry changeCron(Environment env, String taskName, String scheduleAfter, @Nullable CronExpression cronExpression) {
        return invokeInTransaction(() -> {
            final TaskEntry schedule   = schedule(taskName, scheduleAfter, te -> te.changeSchedule(cronExpression));
            final String    taskPropId = deCapitalizeFirst(extractName(taskName));
            final TaskProps taskProps  = env.get(taskPropId, TaskProps.class);
            taskProps.cron = cronExpression == null ? NEVER_CRON : cronExpression.toString();
            env.put(taskPropId, taskProps);
            return schedule;
        });
    }

    /** Mark task as failed. */
    public static void markAsFailed(String taskName) {
        runInTransaction(() -> Sql.update(TE).set(TE.STATUS, TaskStatus.FAILED).where(TE.NAME.eq(taskName)).execute());
    }

    /**
     * Find the next task to execute and lease it. Return either the task name or the number of
     * milliseconds to wait for the first task to be executed.
     */
    @NotNull public static Either<Long, String> nextTaskToExecute(String member, Criteria criteria) {
        return invokeInTransaction(() -> {
            String taskName;
            int    maxRunningTime;
            do {
                final QueryTuple qt = select(TE.NAME, TE.DUE_TIME, CURRENT_TIME).from(TE)
                                                                                .where(criteria.and(TE.SUSPENDED.isFalse()))
                                                                                .orderBy(TE.DUE_TIME, TE.NAME)
                                                                                .get();

                if (qt == null) {
                    logger.debug("No tasks to run in SCHEDULE status. Waiting MAX_WAIT_TIME");
                    return left(MAX_WAIT_TIME);
                }

                final long waitTime = qt.getOrFail(TE.DUE_TIME).toMilliseconds() - qt.getOrFail(CURRENT_TIME).toMilliseconds();

                taskName = qt.getOrFail(TE.NAME);

                if (waitTime > 0) {
                    logger.debug(String.format("Waiting for task %s to execute in %d ", taskName, waitTime));
                    return left(waitTime);
                }

                final TaskProps taskProps = Context.getEnvironment().get(deCapitalizeFirst(extractName(taskName)), TaskProps.class);
                maxRunningTime = taskProps.maxRunningTime;
            }
            while (!readyForExecution(taskName, member, TE.STATUS.eq(SCHEDULED), maxRunningTime));
            return right(taskName);
        });
    }  // end method nextTaskToExecute
    /** Resume execution of specified Task Entry. */
    public static boolean resume(String taskName) {
        return invokeInTransaction(() -> {
            if (Sql.update(TE).set(TE.SUSPENDED, false).where(TE.NAME.eq(taskName), TE.SUSPENDED.isTrue()).execute() == 1) {
                final TaskEntry te = find(taskName);
                if (te != null) {
                    te.calculateSchedule(CronExpression.parse(te.getCronExpression()).getOrNull());
                    te.update();
                    return true;
                }
            }
            return false;
        });
    }

    /** Resume all tasks. */
    public static void resumeAll() {
        runInTransaction(() -> listWhere(TE.SUSPENDED.isTrue()).list().forEach(t -> resume(t.getName())));
    }

    /** Get the member the task is running in. */
    @Nullable public static String runningMember(String taskName) {
        return invokeInTransaction(() -> select(TE.MEMBER).from(TE).where(TE.NAME.eq(taskName), TE.STATUS.eq(RUNNING)).getOrElse(""));
    }

    /** Schedule the task. */
    @NotNull public static TaskEntry schedule(String taskName, String scheduleAfter, @Nullable CronExpression cronExpression) {
        return schedule(taskName, scheduleAfter, te -> te.calculateSchedule(cronExpression));
    }

    /** Schedule the task. */
    @NotNull public static TaskEntry schedule(String taskName, String scheduleAfter, Consumer<TaskEntry> calculateSchedule) {
        return invokeInTransaction(() -> {
            final TaskEntry te = findOrCreate(taskName);
            if (te.getStatus() == RUNNING) throw new TaskAlreadyRunningException(taskName, te.getMember());

            calculateSchedule.accept(te);

            //J-
            insertInto(TE)
                    .set(TE.NAME, te.getName())
                    .set(TE.MDC, "")
                    .setAsNull(TE.START_TIME)
                    .setAsNull(TE.EXPIRATION_TIME)
                    .setAsNull(TE.CURRENT_LOG_ID)
                    .set(TE.DUE_TIME, te.getDueTime())
                    .set(TE.SCHEDULE_AFTER,scheduleAfter)
                    .set(TE.STATUS, te.getStatus())
                    .set(TE.CRON_EXPRESSION, te.getCronExpression())
                    .onConflict(TE.NAME)
                .update()
                    .set(TE.MDC, "")
                    .setAsNull(TE.START_TIME)
                    .setAsNull(TE.EXPIRATION_TIME)
                    .setAsNull(TE.CURRENT_LOG_ID)
                    .set(TE.SCHEDULE_AFTER,scheduleAfter)
                    .set(TE.DUE_TIME, te.getDueTime())
                    .set(TE.STATUS, te.getStatus())
                    .set(TE.CRON_EXPRESSION, te.getCronExpression())
                    .where(TE.STATUS.ne(RUNNING))
                .execute();
            //J+
            return te;
        });
    }

    /** Mark the Task for execution. */
    @NotNull public static TaskEntry startExecution(String taskName, String member, int maxRunningTime) {
        return invokeInTransaction(() -> {
            final QueryTuple qt         = select(TE.STATUS, TE.MEMBER).from(TE).where(TE.NAME.eq(taskName)).get();
            final TaskStatus taskStatus = qt == null ? null : qt.get(TE.STATUS);

            if (taskStatus == RUNNING) throw new TaskAlreadyRunningException(taskName, notNull(qt.get(TE.MEMBER)));

            final boolean ok = taskStatus == null ? createForExecution(taskName, member, maxRunningTime)
                                                  : readyForExecution(taskName, member, TE.STATUS.ne(RUNNING), maxRunningTime);

            final TaskEntry taskEntry = ok ? find(taskName) : null;
            if (taskEntry == null) throw new TaskAlreadyRunningException(taskName, "");
            return taskEntry;
        });
    }

    /** Schedule the task. */
    @NotNull public static TaskEntry stopSchedule(String taskName) {
        return invokeInTransaction(() -> {
            final TaskEntry te = findOrCreate(taskName);
            //J-
            te.changeSchedule(null);
            Sql.update(TE)
                    .set(TE.MDC, "")
                    .setAsNull(TE.EXPIRATION_TIME)
                    .setAsNull(TE.CURRENT_LOG_ID)
                    .setAsNull(TE.START_TIME)
                    .set(TE.DUE_TIME, te.getDueTime())
                    .set(TE.STATUS, TaskStatus.NOT_SCHEDULED)
                    .set(TE.CRON_EXPRESSION, te.getCronExpression())
                    .where(TE.NAME.eq(taskName))
                    .execute();
            //J+
            return te;
        });
    }

    /** Suspend the specified Task Entry. */
    public static boolean suspend(String taskName) {
        return invokeInTransaction(() ->
                Sql.update(TE)
                   .set(TE.STATUS, TE.STATUS.ne(RUNNING).then(SUSPENDED).otherwise(RUNNING))
                   .set(TE.DUE_TIME, DateTime.MAX_VALUE)
                   .set(TE.SUSPENDED, true)
                   .where(TE.NAME.eq(taskName))
                   .execute() == 1);
    }
    /** Suspend all tasks. */
    public static void suspendAll() {
        runInTransaction(() ->
                Sql.update(TE)
                   .set(TE.STATUS, TE.STATUS.ne(RUNNING).then(SUSPENDED).otherwise(RUNNING))
                   .set(TE.DUE_TIME, DateTime.MAX_VALUE)
                   .set(TE.SUSPENDED, true)
                   .where(TE.SUSPENDED.isFalse())
                   .execute());
    }

    /** Update Task Data. */
    public static void setData(String taskName, @Nullable String taskData, @Nullable DateTime dTaskDataTime) {
        Sql.update(TE)
            .set(TE.DATA, notNull(taskData, " "))
            .set(TE.DATA_TIME, dTaskDataTime)
            .set(TE.UPDATE_TIME, CURRENT_TIME)
            .where(TE.NAME.eq(taskName))
            .execute();
    }

    /** Create the TaskEntry to mark it as ready for execution. */
    private static boolean createForExecution(String taskName, String member, int maxRunningTime) {
        try {
            return Sql.insertInto(TE)
                   .set(TE.NAME, taskName)
                   .set(TE.STATUS, RUNNING)
                   .set(TE.MDC, createMdc())
                   .set(TE.EXPIRATION_TIME, CURRENT_TIME.add(maxRunningTime, SECONDS))
                   .set(TE.MEMBER, member)
                   .set(TE.UPDATE_TIME, CURRENT_TIME)
                   .execute() == 1;
        }
        catch (final UniqueViolationException e) {
            return false;
        }
    }

    private static String createMdc() {
        return UUID.randomUUID().toString();
    }

    /** Update the TaskEntry to mark it as ready for execution. */
    private static boolean readyForExecution(String taskName, String member, Criteria criteria, int maxRunningTime) {
        return Sql.update(TE)
               .set(TE.STATUS, RUNNING)
               .set(TE.MDC, createMdc())
               .set(TE.EXPIRATION_TIME, CURRENT_TIME.add(maxRunningTime, SECONDS))
               .set(TE.MEMBER, member)
               .set(TE.UPDATE_TIME, CURRENT_TIME)
               .where(TE.NAME.eq(taskName), criteria)
               .execute() == 1;
    }

    //~ Static Fields ................................................................................................................................

    public static final long MAX_WAIT_TIME = 15_000;

    private static final TaskEntryTable TE     = TASK_ENTRY;
    private static final Logger         logger = Logger.getLogger(TaskEntry.class);
}  // end class TaskEntry
