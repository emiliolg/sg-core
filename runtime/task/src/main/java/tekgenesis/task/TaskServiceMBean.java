
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.service.ServiceMBean;
import tekgenesis.task.jmx.TaskEntryInfo;
import tekgenesis.task.jmx.TaskLogInfo;

/**
 * TaskServiceMBean.
 */
public interface TaskServiceMBean extends ServiceMBean {

    //~ Methods ......................................................................................................................................

    /** change cron expression. */
    void changeCron(String fqn, String cronExpression);

    /** Remove suspended mark to all task. */
    void resumeAllTask();

    /** Resume specified task. */
    boolean resumeTask(String taskName);

    /** schedule task. */
    void schedule(String taskFqn, String cronExpression);

    /** Run the start method of Life Cycle Tasks. */
    void startLifecycleTasks();

    /** Run the stop method of Life Cycle Tasks. */
    void stopLifecycleTasks();

    /** Stop task schedule. */
    void stopSchedule(String fqn);

    /** Stop task schedule. */
    void stopTask(String fqn);

    /** Submit for execution. */
    void submit(String task);

    /** Submit task and wait until finished. */
    Status submitSync(String taskName)
        throws ExecutionException, InterruptedException;

    /** Mark all task as suspended. */
    void suspendAllTask();

    /** Mark task as suspended. */
    boolean suspendTask(@NotNull String task);

    /** Return if true if the task is suspended. */
    boolean isSuspended(@NotNull String task);

    /** @return  TaskEntryInfo */
    @Nullable TaskEntryInfo getTask(String taskName);

    /** Update Task Data. */
    void setTaskData(String taskId, @Nullable String data, @Nullable DateTime dateTime);

    /** Return the history for a given task. */
    List<TaskLogInfo> getTaskLog(@NotNull String task);

    /** Returns the list of defined tasks. */
    List<TaskEntryInfo> getTasks();
}  // end interface TaskServiceMBean
