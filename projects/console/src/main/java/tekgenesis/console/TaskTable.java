
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.service.RemoteTaskService;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.Databases;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.task.TaskService;
import tekgenesis.task.TaskStatus;
import tekgenesis.task.exception.TaskExecutionException;
import tekgenesis.task.jmx.TaskEntryInfo;
import tekgenesis.type.permission.PredefinedPermission;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.core.Times.MINUTES_HOUR;
import static tekgenesis.common.core.Times.SECONDS_MINUTE;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.console.Messages.TASK_ACTION;
import static tekgenesis.console.Messages.TASK_ALREADY_SUSPENDED;
import static tekgenesis.task.jmx.JmxConstants.TEKGENESIS_DAEMON_NAME;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * User class for Form: TaskTable
 */
public class TaskTable extends TaskTableBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeCronScheduleTask() {
        final String taskFqn = getTaskFqnLabel();

        final String cronExpression = getCronExpression();

        final Option<RemoteTaskService> service = getService();
        if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);

        service.get().changeCron(taskFqn, cronExpression);

        loadInactiveTasks(null);
        logger.info(String.format("TASK_AUDIT: Cron Change (Task %s, cron: %s", taskFqn, cronExpression));
        return closeChangeCronTaskDialog();
    }

    @NotNull @Override public Action closeChangeCronTaskDialog() {
        setChangeCronTaskDialog(false);
        return actions.getDefault();
    }

    @NotNull @Override public Action closeSetDataDialog() {
        setSetDataDialog(false);
        return actions.getDefault();
    }

    /** Invoked when text_field(searchBox) value changes. */
    @NotNull @Override public Action filterTasks() {
        final TASK_TYPE tasksType = getTasksType();
        // noinspection EnumSwitchStatementWhichMissesCases
        switch (tasksType) {
        case SCHEDULE:
            loadScheduleTasks(getSearchBox());
            break;
        case INACTIVE:
            loadInactiveTasks(getSearchBox());
            break;
        }
        return actions.getDefault();
    }

    @Override public void loadTasks() {
        final TASK_TYPE tasksType = getTasksType();
        switch (tasksType) {
        case RUNNING:
            loadActiveTasks();
            break;
        case SCHEDULE:
            loadScheduleTasks(null);
            break;
        case INACTIVE:
            loadInactiveTasks(null);
            break;
        }
    }

    @NotNull @Override public Action onSchedule() {
        final TASK_TYPE tasksType = getTasksType();
        if (tasksType == TASK_TYPE.RUNNING) loadTasks();
        return actions.getDefault();
    }

    @NotNull @Override public Action saveTaskData() {
        final String   taskId       = getTaskDataFqnLabel();
        final String   taskData     = getNewTaskData();
        final DateTime taskDatetime = getNewTaskDataTime();

        final Option<RemoteTaskService> service = getService();
        if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);
        service.get().setTaskData(taskId, taskData, taskDatetime);
        logger.info(String.format("TASK_AUDIT: Task Data saved (Task %s, data: %s, datatime: %s", taskId, taskData, taskDatetime));
        return closeSetDataDialog();
    }

    private void loadActiveTasks() {
        final FormTable<TaskTableRow> list = getTaskTable();
        list.clear();

        final Option<RemoteTaskService> service = getService();
        if (service.isEmpty()) return;
        final DateTime           currentTime = invokeInTransaction(() -> Databases.openDefault().currentTime());
        final Seq<TaskEntryInfo> filter      = filter(service.get().getTasks(), f -> f != null && f.getStatus() == TaskStatus.RUNNING);

        for (final TaskEntryInfo taskEntryInfo : filter) {
            final DateTime startTime = taskEntryInfo.getStartTime();
            final String   time      = startTime == null ? "" : timeAsLabel(currentTime.toMilliseconds() - startTime.toMilliseconds());

            list.add().populate(taskEntryInfo, time);
        }

        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));
    }

    private void loadInactiveTasks(@Nullable String contains) {
        final FormTable<TaskTableRow> list = getTaskTable();

        final Option<RemoteTaskService> service = getService();
        if (service.isEmpty()) return;

        list.clear();
        final List<TaskEntryInfo> allTasks = service.get().getTasks();

        final Seq<TaskEntryInfo> inactiveTasks = filter(allTasks,
                f -> f != null &&
                    (f.getStatus() == TaskStatus.NOT_SCHEDULED ||
                        (f.getStatus() != TaskStatus.RUNNING && f.getStatus() != TaskStatus.SCHEDULED && f.getStatus() != TaskStatus.RETRYING)));

        inactiveTasks.filter(t -> contains == null || t != null && t.getName().toLowerCase().contains(contains.toLowerCase())).forEach(t ->
                list.add()
                    .populate(t, null));
    }

    private void loadScheduleTasks(@Nullable String contains) {
        final FormTable<TaskTableRow> list = getTaskTable();
        list.clear();

        final Option<RemoteTaskService> service = getService();
        if (service.isEmpty()) return;

        final Seq<TaskEntryInfo> filter = filter(service.get().getTasks(), f -> f != null && f.getStatus() == TaskStatus.SCHEDULED);

        filter.filter(t -> contains == null || t != null && t.getName().toLowerCase().contains(contains.toLowerCase())).forEach(t ->
                list.add()
                    .populate(t, null));
    }

    /**  */

    private Option<RemoteTaskService> getService() {
        return Context.getSingleton(Clusters.class).getService(RemoteTaskService.class, TaskService.SERVICE_NAME);
    }

    //~ Methods ......................................................................................................................................

    /** Format a time in an ugly way. */
    static String timeAsLabel(long uptime) {
        final long secs = uptime / 1_000;
        final long mins = secs / SECONDS_MINUTE;

        return format("%3dh %2dm %2d.%03ds", mins / MINUTES_HOUR, mins % MINUTES_HOUR, secs % SECONDS_MINUTE, uptime % 1_000);
    }

    private static int completionFor(TaskEntryInfo taskEntryInfo, JmxEndpoint conn) {
        try {
            final String beanName = TEKGENESIS_DAEMON_NAME + taskEntryInfo.getFullName() + ",type=task";
            return JmxInvokerImpl.invoker(conn).mbean(beanName).getAttribute("Completion");
        }
        catch (final InvokerConnectionException e) {
            logger.warning("Unable to retrieve task completion status", e);
            return 0;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String TASK_SERVICE_DISABLE_MSG = "Task Service not enabled";

    private static final Logger logger = getLogger(TaskTable.class);

    //~ Inner Classes ................................................................................................................................

    /**
     * Task Row.
     */
    public class TaskTableRow extends TaskTableRowBase {
        /** Invoked when label(schedule) is clicked. */
        @NotNull @Override public Action openScheduleTaskDialog() {
            final String selectedTaskFqn = getTaskFqn();
            setTaskFqnLabel(selectedTaskFqn);

            final Option<RemoteTaskService> service = getService();
            if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);
            final String cronExp = getCronExp();

            setCronExpression(cronExp);

            setChangeCronTaskDialog(true);
            return actions.getDefault();
        }

        /** Invoked when label(setData) is clicked. */
        @NotNull @Override public Action openSetDataDialog() {
            final String fqn = getTaskFqn();

            setTaskDataFqnLabel(fqn);

            final Option<RemoteTaskService> service = getService();
            if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);

            final TaskEntryInfo task = service.get().getTask(fqn);

            setNewTaskData(task.getData());
            setNewTaskDataTime(task.getDataTime());

            setSetDataDialog(true);
            return actions.getDefault();
        }

        /** Populate row. */
        public void populate(TaskEntryInfo taskEntryInfo, @Nullable String elapsedTime) {
            setTaskFqn(taskEntryInfo.getName());
            setTaskName(taskEntryInfo.getName());
            setTaskLabel(taskEntryInfo.getName());
            setHostMember(taskEntryInfo.getMember());
            final TaskStatus status = taskEntryInfo.getStatus();
            setStatus(status.label());
            setTaskData(taskEntryInfo.getData());
            setTaskDataDatetime(taskEntryInfo.getDataTime());
            setRunningTime(notEmpty(elapsedTime, ""));
            setNextExecution(taskEntryInfo.getDueTime());
            setCronExp(taskEntryInfo.getCronExpression());

            final DateTime cronExecution = taskEntryInfo.getCurrentCronExecution();
            if (cronExecution != null) setNextExecution(cronExecution);

            final String memberUuid = taskEntryInfo.getMember();
            if (isNotEmpty(memberUuid) && status == TaskStatus.RUNNING)

                filter(Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers(),  //
                        m -> m != null && m.getName().equals(memberUuid))  //
                .getFirst()            //
                .ifEmpty(() -> logger.warning(format("Not member %s found for running task %s", memberUuid, taskEntryInfo.getName())))  //
                .ifPresent(rm -> setCompletion(completionFor(taskEntryInfo, rm.getJmxEndpoint())));
        }

        /** Invoked when label(restartCron) is clicked. */
        @NotNull @Override public Action resumeSchedule() {
            final String taskFqn = getTaskFqn();

            final Option<RemoteTaskService> service = getService();
            if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);

            service.get().resumeTask(taskFqn);
            loadTasks();
            logger.info(String.format("TASK_AUDIT: Resume Task (Task %s", taskFqn));
            return actions.getDefault().withMessage(TASK_ACTION.label("resume"));
        }

        @NotNull @Override public Action runNow() {
            final String taskFqn = getTaskFqn();

            try {
                final Option<RemoteTaskService> service = getService();
                if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);
                service.get().runNow(taskFqn);
                logger.info(String.format("TASK_AUDIT: Run now (Task %s", taskFqn));
                return actions.getDefault();
            }
            catch (final TaskExecutionException e) {
                logger.warning(e);
                return actions.getError().withMessage(Messages.TASK_ALREADY_RUNNING.label(taskFqn));
            }
            catch (final IllegalArgumentException e) {
                logger.error(e);
                return actions.getError().withMessage(Messages.TASK_INSTANCE_FAILED.label());
            }
        }

        /** Invoked when label(stopNow) is clicked. */
        @NotNull @Override public Action stopNow() {
            final String                    taskFqn = getTaskFqn();
            final Option<RemoteTaskService> service = getService();
            if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);

            service.get().stopTask(taskFqn);

            logger.info(String.format("TASK_AUDIT: Stop now (Task %s", taskFqn));
            return actions.getDefault();
        }

        @NotNull @Override public Action stopSchedule() {
            final String taskFqn = getTaskFqn();

            try {
                final Option<RemoteTaskService> service = getService();
                if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);
                service.get().stopSchedule(taskFqn);
                logger.info(String.format("TASK_AUDIT: Stop schedule (Task %s", taskFqn));
                return actions.getDefault();
            }
            catch (final TaskExecutionException e) {
                logger.warning(e);
                return actions.getError().withMessage(Messages.TASK_ALREADY_RUNNING.label(taskFqn));
            }
            catch (final IllegalArgumentException e) {
                logger.error(e);
                return actions.getError().withMessage(Messages.TASK_INSTANCE_FAILED.label());
            }
        }

        /** Invoked when label(suspend) is clicked. */
        @NotNull @Override public Action suspendSchedule() {
            final String                    taskFqn = getTaskFqn();
            final Option<RemoteTaskService> service = getService();
            if (service.isEmpty()) return actions.getError().withMessage(TASK_SERVICE_DISABLE_MSG);

            logger.info(String.format("TASK_AUDIT: Suspend (Task %s", taskFqn));

            if (service.get().isSuspended(taskFqn)) return actions.getError().withMessage(TASK_ALREADY_SUSPENDED.label());
            else {
                service.get().suspendTask(taskFqn);
                loadTasks();

                return actions.getDefault().withMessage(TASK_ACTION.label(ConsoleConstants.SUSPEND));
            }
        }  // end method suspendSchedule

        /** Invoked when display(status) is clicked. */
        @NotNull @Override public Action viewHistory() {
            final String          selectedTaskName = getTaskName();
            final String          selectedTaskFqn  = getTaskFqn();
            final String          pk               = selectedTaskName + ":" + selectedTaskFqn;
            final TaskHistoryForm form             = forms.initialize(TaskHistoryForm.class, pk);
            return actions.navigate(form).dialog();
        }
    }  // end class TaskTableRow
}  // end class TaskTable
