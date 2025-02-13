
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.service.ServiceMBean;
import tekgenesis.task.jmx.TaskEntryInfo;
import tekgenesis.task.jmx.TaskLogInfo;

import static tekgenesis.metadata.task.TaskConstants.GET_CRON_EXPRESSION;
import static tekgenesis.task.jmx.JmxConstants.EMPTY_ARGS;
import static tekgenesis.task.jmx.JmxConstants.EMPTY_SIGNATURE;

/**
 * TaskServiceInstance.
 */
public class RemoteTaskService extends RemoteService implements ServiceMBean {

    //~ Constructors .................................................................................................................................

    /**  */
    public RemoteTaskService(String name, JmxEndpoint c) {
        super(name, c);
    }

    //~ Methods ......................................................................................................................................

    /** Restart cron schedule. */
    public void changeCron(String taskFqn, String cronExpression) {
        // noinspection DuplicateStringLiteralInspection
        JmxInvokerImpl.invoker(conn)
            .mbean(jmxServiceName)
            .invoke("changeCron",
                new String[] {
                        String.class.getName(),
                        String.class.getName()
                    },
                new Object[] { taskFqn, cronExpression });
    }

    /** Remove suspended mark to all task. */
    public void resumeAllTask() {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("resumeAllTask", EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    /** Remove suspended mark. */
    public void resumeTask(String taskId) {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("resumeTask", new String[] { String.class.getName() }, new Object[] { taskId });
    }

    /** Run now. */
    public void runNow(String taskId) {
        // noinspection DuplicateStringLiteralInspection
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("submit", new String[] { String.class.getName() }, new Object[] { taskId });
    }

    /** Restart cron schedule. */
    public void schedule(String taskFqn, String cronExpression) {
        // noinspection DuplicateStringLiteralInspection
        JmxInvokerImpl.invoker(conn)
            .mbean(jmxServiceName)
            .invoke("schedule",
                new String[] {
                        String.class.getName(),
                        String.class.getName()
                    },
                new Object[] { taskFqn, cronExpression });
    }

    /** Stop cron schedule. */
    public void stopSchedule(String taskId) {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("stopSchedule", new String[] { String.class.getName() }, new Object[] { taskId });
    }

    /** Stop task schedule. */
    public void stopTask(String fqn) {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("stopTask", new String[] { String.class.getName() }, new Object[] { fqn });
    }

    /** Mark all task as suspended. */
    public void suspendAllTask() {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("suspendAllTask", EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    /** Mark task as suspended. */
    public void suspendTask(@NotNull String taskId) {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("suspendTask", new String[] { String.class.getName() }, new Object[] { taskId });
    }

    /**  */
    public String getCronExpression(String fqn) {
        return JmxInvokerImpl.invoker(conn)
               .mbean(jmxServiceName)
               .invoke(GET_CRON_EXPRESSION, new String[] { String.class.getName() }, new Object[] { fqn });
    }

    /**  */
    public boolean isSuspended(String taskId) {
        return JmxInvokerImpl.invoker(conn)
               .mbean(jmxServiceName)
               .invoke("isSuspended", new String[] { String.class.getName() }, new Object[] { taskId });
    }

    /**  */
    public TaskEntryInfo getTask(String taskName) {
        return JmxInvokerImpl.invoker(conn)
               .mbean(jmxServiceName)
               .invoke("getTask", new String[] { String.class.getName() }, new Object[] { taskName });
    }

    /** Set Task data. */
    public void setTaskData(String taskId, @Nullable String data, @Nullable DateTime dateTime) {
        JmxInvokerImpl.invoker(conn)
            .mbean(jmxServiceName)
            .invoke("setTaskData",
                new String[] { String.class.getName(), String.class.getName(), DateTime.class.getName() },
                new Object[] { taskId, data, dateTime });
    }

    /**  */
    public List<TaskLogInfo> getTaskLog(String taskId) {
        return JmxInvokerImpl.invoker(conn)
               .mbean(jmxServiceName)
               .invoke("getTaskLog", new String[] { String.class.getName() }, new Object[] { taskId });
    }

    /**  */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public List<TaskEntryInfo> getTasks() {
        return JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).getAttribute("Tasks");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -672700130668749173L;
}  // end class RemoteTaskService
