
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.service.RemoteTaskService;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.sg.ClusterConf;
import tekgenesis.task.TaskService;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.console.TASK_TYPE.*;

/**
 * User class for Form: TaskDashboard
 */
public class TaskDashboard extends TaskDashboardBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(close) is clicked. */
    @NotNull @Override public Action closeSuspendTaskDialog() {
        setConfirmSuspendTasks(false);
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void initForm() {
        createActiveTaskForm();
        createInactiveTaskForm();
        createScheduleTaskForm();

        final ServiceStatus statusForm = createStatusForm();
        statusForm.setService(TaskService.SERVICE_NAME);
        statusForm.setServiceClass(RemoteTaskService.class.getName());
        statusForm.configure();

        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());
    }

    /** Invoked when button(suspendAll) is clicked. */
    @NotNull @Override public Action openSuspendAllDialog() {
        setConfirmSuspendTasks(true);
        return actions.getDefault();
    }

    /** Invoked when button(ResumeAll) is clicked. */
    @NotNull @Override public Action resumeAll() {
        getService().resumeAllTask();
        tabTaskSelection();
        return actions.getDefault();
    }

    /** Invoked when button(restartC) is clicked. */
    @NotNull @Override public Action suspendAll() {
        getService().suspendAllTask();
        return closeSuspendTaskDialog();
    }

    /** Invoked when tabs(taskTab) value changes. */
    @NotNull @Override public Action tabTaskSelection() {
        final int tab = getTaskTab();
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));
        switch (tab) {
        case 0:
            final TaskTable activeTaskForm = getActiveTaskForm();
            loadTaskTable(activeTaskForm, RUNNING, true);
            break;
        case 1:
            // loadScheduleTasks(null);
            final TaskTable scheduleTaskForm = getScheduleTaskForm();
            loadTaskTable(scheduleTaskForm, SCHEDULE, false);
            break;
        case 2:
            final TaskTable inactiveTaskForm = getInactiveTaskForm();
            loadTaskTable(inactiveTaskForm, INACTIVE, false);
            break;
        }

        return actions.getDefault();
    }

    private void loadTaskTable(@Nullable TaskTable scheduleTaskForm, TASK_TYPE schedule, boolean notSearcheable) {
        if (scheduleTaskForm != null) {
            scheduleTaskForm.setTasksType(schedule);
            scheduleTaskForm.setNotSearcheable(notSearcheable);
            scheduleTaskForm.setDisable(isDisable());
            scheduleTaskForm.loadTasks();
        }
    }

    /**  */
    private RemoteTaskService getService() {
        final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();
        return activeCluster.get().getMembers().get(0).getService(RemoteTaskService.class, TaskService.SERVICE_NAME);
    }
}  // end class TaskDashboard
