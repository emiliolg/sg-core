
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxException;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.util.CronExpression;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.task.exception.InvalidTaskException;
import tekgenesis.task.exception.TaskAlreadyRunningException;
import tekgenesis.task.exception.TaskExecutionException;
import tekgenesis.task.exception.TaskServiceDisabledException;
import tekgenesis.task.jmx.EndpointResolver;
import tekgenesis.task.jmx.TaskEntryInfo;
import tekgenesis.task.jmx.TaskLogInfo;

import static java.lang.String.format;
import static java.lang.Thread.NORM_PRIORITY;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.metadata.task.TaskType.*;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sg.TaskEntry.*;
import static tekgenesis.sg.g.TaskEntryTable.TASK_ENTRY;
import static tekgenesis.task.ScheduledTask.createFromFqn;
import static tekgenesis.task.Task.listTasks;
import static tekgenesis.task.TaskStatus.SCHEDULED;
import static tekgenesis.task.jmx.JmxConstants.TEKGENESIS_DAEMON_NAME;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * TaskService.
 */
public class TaskService extends Service implements TaskServiceMBean {

    //~ Instance Fields ..............................................................................................................................

    private final int concurrentTasks;

    private ExecutorService         executor;
    private boolean                 running;
    private final Set<Task<?>>      runningTasks  = new HashSet<>();
    private boolean                 sleeping;
    private final ThreadPoolFactory threadFactory;

    //~ Constructors .................................................................................................................................

    /** Default Constructor. */
    public TaskService(final ServiceManager sm) {
        super(sm, SERVICE_NAME, SERVICE_START_ORDER, TaskServiceProps.class);
        executor        = null;
        running         = false;
        threadFactory   = new ThreadPoolFactory();
        sleeping        = false;
        concurrentTasks = getProperties().concurrentTasks;
    }

    //~ Methods ......................................................................................................................................

    /** Ensure the task is scheduled for execution. */
    public final void changeCron(String fqn, String cronExpression) {
        final Option<ScheduledTask> scheduledTask = createFromFqn(fqn);
        if (scheduledTask.isEmpty()) throw new InvalidTaskException(fqn);
        changeCron(fqn, scheduledTask.get().getScheduleAfter(), CronExpression.parse(cronExpression).getOrNull());
    }

    @Override public void resumeAllTask() {
        TaskEntry.resumeAll();
    }

    @Override public boolean resumeTask(String taskName) {
        return TaskEntry.resume(resolveFqn(taskName));
    }

    /** Ensure the task is scheduled for execution. */
    public final void schedule(ScheduledTask task) {
        schedule(task.getFqn(), task.getScheduleAfter(), task.getCronExpression(getEnv()));
    }

    public void schedule(String fqn, String cronExpression) {
        final Option<ScheduledTask> scheduledTask = createFromFqn(fqn);
        if (scheduledTask.isEmpty()) throw new InvalidTaskException(fqn);
        schedule(fqn, scheduledTask.get().getScheduleAfter(), CronExpression.parse(cronExpression).getOrNull());
    }

    /** Run the stop method of Life Cycle Tasks. */
    @Override public void startLifecycleTasks() {
        executeLifeCycleTask(listTasks(CLUSTER_LIFECYCLE), true, true);
        executeLifeCycleTask(listTasks(NODE_LIFE_CYCLE), true, false);
    }

    /** Run the stop method of Life Cycle Tasks. */
    @Override public void stopLifecycleTasks() {
        executeLifeCycleTask(listTasks(NODE_LIFE_CYCLE).revert(), false, false);
        executeLifeCycleTask(listTasks(CLUSTER_LIFECYCLE).revert(), false, true);
    }

    public void stopSchedule(String fqn) {
        TaskEntry.stopSchedule(fqn);
    }

    @Override public void stopTask(final String fqn) {
        createFromFqn(fqn).ifPresent(this::stopTask);
    }

    @Override public void submit(String taskName) {
        createFromFqn(taskName).ifPresent(t -> submit(t, null));
    }

    /** submit a task for immediate execution. */
    public final Future<Status> submit(@NotNull Task<?> task) {
        return submit(task, null);
    }

    /**
     * submit a task for immediate execution.
     *
     * @param  instanceValue  an instance value to use. This is only valid for processor tasks.
     */
    public final <T> Future<Status> submit(@NotNull Task<?> task, @Nullable T instanceValue) {
        checkServiceEnabled();
        checkTaskIsNotRunning(task);

        final Option<String> taskType = listTasks(PROCESSOR).filter(p -> p.equals(task.getFqn())).getFirst();

        if (taskType.isEmpty() && instanceValue != null)
            throw new IllegalArgumentException("Unable to specify instanceValue for a no processor task");

        taskType.ifPresent(c -> {
            final ProcessorTaskInstance<T> instance = cast(task.getInstance());
            instance.setItem(instanceValue);
        });

        final TaskProps taskProps = task.getTaskProperties(getEnv());
        return runTask(task, startExecution(task.getFqn(), getCurrentMember(), taskProps.maxRunningTime));
    }  // end method submit

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Status submitSync(String taskName)
        throws ExecutionException, InterruptedException
    {
        final Option<ScheduledTask> scheduledTask = createFromFqn(taskName);
        return scheduledTask.isPresent() ? submit(scheduledTask.get(), null).get() : Status.error("Unable to find task");
    }

    @Override public void suspendAllTask() {
        TaskEntry.suspendAll();
    }

    @Override public boolean suspendTask(@NotNull String taskName) {
        return invokeInTransaction(() -> TaskEntry.suspend(resolveFqn(taskName)));
    }

    @Override public boolean isSuspended(@NotNull String taskName) {
        final TaskEntry te = invokeInTransaction(() -> TaskEntry.find(resolveFqn(taskName)));
        return te != null && te.isSuspended();
    }

    @Override public final TaskServiceProps getProperties() {
        return (TaskServiceProps) super.getProperties();
    }

    @Nullable @Override public TaskEntryInfo getTask(String taskName) {
        return invokeInTransaction(() -> TaskEntryInfo.create(selectFrom(TASK_ENTRY).where(TASK_ENTRY.NAME.eq(taskName)).get()));
    }

    public void setTaskData(String taskName, @Nullable String data, @Nullable DateTime dateTime) {
        runInTransaction(() -> TaskEntry.setData(taskName, data, dateTime));
    }

    @Override public List<TaskLogInfo> getTaskLog(@NotNull String taskName) {
        return invokeInTransaction(() -> TaskExecutionLog.listByName(taskName).toList().map(TaskLogInfo::create).toList());
    }

    @Override public List<TaskEntryInfo> getTasks() {
        //J-
        return invokeInTransaction(() -> {
            final ImmutableList<String> task = Task.listTasks(TaskType.CLUSTER_LIFECYCLE, TaskType.NODE_LIFE_CYCLE).toList();
            return selectFrom(TASK_ENTRY)
                    .list()
                    .filter(t -> {
                        if ( t == null) return false;
                        final String name = t.getName();
                        final int colon = name.indexOf(":");
                        return colon <= 0 || !task.contains(name.substring(0, colon));
                    })
                    .map(TaskEntryInfo::create)
                    .toList();
        });
        //J+
    }

    @NotNull protected Option<ScheduledTask> createScheduledTask(String taskName) {
        return createFromFqn(taskName);
    }

    @Override protected final void doShutdown() {
        running = false;
        if (executor != null) {
            shutdownExecutor();
            executor = null;
        }
    }

    protected synchronized void doSleep(long ms) {
        try {
            sleeping = true;
            wait(ms);
        }
        catch (final InterruptedException ignore) {}
        sleeping = false;
    }

    @Override protected final void doStart() {
        runningTasks.clear();
        running = true;
        createExecutor();
        if (getProperties().enableSchedule) {
            try {
                submitSync("tekgenesis.task.SuiGenerisTaskPurge");
            }
            catch (final InterruptedException | ExecutionException e) {
                logger.warning("Unable to execute SuiGenerisTaskPurge when Task Service is started.", e);
            }

            Task.listScheduledTasks().forEach(fqn -> createFromFqn(fqn).ifPresent(this::scheduleTask));
            Task.listScheduleNodeTasks().forEach(fqn -> createFromFqn(fqn).ifPresent(this::scheduleNodeTask));
            threadFactory.newThread(this::runTasks).start();
            threadFactory.newThread(this::runMonitorTask).start();
        }
    }

    protected synchronized void notifyScheduler() {
        if (sleeping) notify();
    }

    protected void runMonitorTask() {
        while (running) {
            try {
                nextTaskToExecute(getCurrentMember(), TASK_ENTRY.STATUS.eq(SCHEDULED).and(TASK_ENTRY.NAME.like("%:" + getCurrentMember())))  //
                .ifRight(this::runScheduledTask)                                                                                             //
                .ifLeft(this::sleep);
            }
            catch (final Throwable e) {
                logger.error("Unexpected error running Monitor Tasks", e);
            }
        }
    }

    /** Create Task Executor. */
    final void createExecutor() {
        if (executor == null) executor = Executors.newFixedThreadPool(concurrentTasks, threadFactory);
    }

    final void notifyCompletion(Task<?> task) {
        runningTasks.remove(task);
        notifyScheduler();
    }

    Set<Task<?>> runningTasks() {
        return runningTasks;
    }

    private boolean awaitTermination(long milliseconds) {
        try {
            return executor.awaitTermination(milliseconds, TimeUnit.SECONDS);
        }
        catch (final InterruptedException e) {
            return false;
        }
    }

    private void cancelTasks() {
        for (final Iterator<Task<?>> iterator = runningTasks.iterator(); iterator.hasNext();) {
            final Task<?> task = iterator.next();
            task.cancel(true);
            if (task.isDone()) iterator.remove();
        }
    }

    private void changeCron(String fqn, String scheduleAfter, @Nullable CronExpression cron) {
        if (!Task.listTasks(TaskType.CLUSTER_LIFECYCLE, TaskType.NODE_LIFE_CYCLE).filter(resolveFqn(fqn)::equals).isEmpty())
            // noinspection DuplicateStringLiteralInspection
            throw new TaskExecutionException("Unable to schedule a lifecycle task.");
        TaskEntry.changeCron(getEnv(), resolveFqn(fqn), scheduleAfter, cron);
    }

    private void checkServiceEnabled() {
        if (executor == null) throw new TaskServiceDisabledException();
    }

    private void checkTaskIsNotRunning(Task<?> task) {
        final String taskName = task.getFqn();
        final String member   = !task.isDone() ? getCurrentMember() : runningMember(taskName);
        if (isNotEmpty(member)) {
            final TaskEntry taskEntry = invokeInTransaction(() -> TaskEntry.find(task.getFqn()));
            if (taskEntry != null && taskEntry.getStatus() == TaskStatus.RUNNING) {
                final String runningMember = taskEntry.getMember();

                if (!getCurrentMember().equals(runningMember) && clusterManager.getMembersAddressNames().contains(runningMember))
                    throw new TaskAlreadyRunningException(taskName, member);
                TaskEntry.markAsFailed(task.getFqn());
            }
        }
    }

    private void executeLifeCycleTask(final Seq<String> taskList, final boolean startup, boolean clusterTask) {
        taskList.toList().forEach(task -> run(startup, task, clusterTask));
        logger.info("Lifecycle tasks execution finished");
    }  // end method executeLifeCycleTask

    private void fail(ScheduledTask t) {
        TaskEntry.markAsFailed(t.getFqn());
        scheduleTask(t);
    }

    private String resolveFqn(String fqn) {
        return fqn.contains(":") ? fqn.split(":")[0] : fqn;
    }

    private void run(boolean startup, @NotNull final String taskName, boolean clusterTask) {
        final boolean isSlaveNode = !isMaster();

        if (isSlaveNode && clusterTask) return;  // I'm not the master and Server Tasks can not be executed by me ...I'm just a simple slave.. node :(

        logger.info(format("Running task %s", taskName));

        final LifecycleTask taskAction = new LifecycleTask(taskName);

        TaskEntry       taskEntry = null;
        final String    fqn       = taskName + ":" + getAddress();
        final TaskProps taskProps = getEnv().get(taskName, TaskProps.class);
        try {
            taskEntry = TaskEntry.startExecution(fqn, getCurrentMember(), taskProps.maxRunningTime);
        }
        catch (final TaskAlreadyRunningException e) {
            // It means two server in the same machine...or a fail start on the same server
            final TaskEntry entry = invokeInTransaction(() -> TaskEntry.find(fqn));
            if (entry != null) {
                runInTransaction(entry::delete);
                taskEntry = TaskEntry.startExecution(fqn, getCurrentMember(), taskProps.maxRunningTime);
            }
        }
        if (taskEntry != null) {
            taskAction.initialize(this, taskEntry);

            if (startup) taskAction.onStartup();
            else taskAction.onShutdown();

            taskAction.call();
        }
        else logger.error(format("Unable to find lifecycle task %s", taskName));
    }

    private void runScheduledTask(String taskName) {
        final String fqn = taskName.contains(":") ? taskName.substring(0, taskName.indexOf(":")) : taskName;

        final Option<ScheduledTask> st = createScheduledTask(fqn);
        final TaskEntry             te = invokeInTransaction(() -> TaskEntry.find(taskName));

        if (st.isEmpty()) {
            final String msg = "Task class not found: " + taskName;
            logger.error(msg);
            if (te != null) te.endExecution(Status.abort(msg), null);
        }
        else if (te == null) logger.error("Unexpected error. Cannot find TaskEntry for task: " + taskName);
        else runTask(st.get(), te);
    }

    @NotNull private Future<Status> runTask(@NotNull Task<?> task, TaskEntry taskEntry) {
        task.initialize(this, taskEntry);

        final Future<Status> future = executor.submit(task);
        task.setFuture(future);
        runningTasks.add(task);
        return task;
    }

    private void runTasks() {
        while (running) {
            try {
                nextTaskToExecute(getCurrentMember(), TASK_ENTRY.STATUS.eq(SCHEDULED).and(TASK_ENTRY.NAME.notLike("%:%")))  //
                .ifRight(this::runScheduledTask)                                                                            //
                .ifLeft(this::sleep);
                waitForAvailableThread();
            }
            catch (final Throwable e) {
                logger.error("Unexpected error running Tasks", e);
            }
        }
    }

    private void schedule(@NotNull String fqn, @NotNull String scheduleAfter, @Nullable CronExpression cron) {
        if (!Task.listTasks(TaskType.CLUSTER_LIFECYCLE, TaskType.NODE_LIFE_CYCLE).filter(fqn::equals).isEmpty())
            // noinspection DuplicateStringLiteralInspection
            throw new TaskExecutionException("Unable to schedule a lifecycle task.");
        TaskEntry.schedule(fqn, scheduleAfter, cron);
    }

    private void scheduleNodeTask(ScheduledTask scheduledTask) {
        final String fqn         = scheduledTask.getFqn();
        final String taskNodeFqn = fqn + ":" + getCurrentMember();
        try {
            scheduledTask.setFqn(taskNodeFqn);
            schedule(scheduledTask);
        }
        catch (final TaskAlreadyRunningException e) {
            TaskEntry.markAsFailed(taskNodeFqn);
            scheduleNodeTask(scheduledTask);
        }
        catch (final TaskExecutionException e) {
            logger.error(format("Unable to schedule node task %s in %s", fqn, getCurrentMember()), e);
        }
    }

    private void scheduleTask(ScheduledTask task) {
        try {
            schedule(task);
        }
        catch (final TaskAlreadyRunningException e) {
            final TaskEntry taskEntry = invokeInTransaction(() -> TaskEntry.find(task.getFqn()));
            if (taskEntry != null && taskEntry.getStatus() == TaskStatus.RUNNING) {
                final String member = taskEntry.getMember();
                if (getCurrentMember().equals(member) || !clusterManager.getMembersAddressNames().contains(member)) fail(task);
            }
        }
        catch (final TaskExecutionException e) {
            logger.error(format("Unable to schedule task %s", task.getFqn()), e);
        }
    }

    private void shutdownExecutor() {
        executor.shutdown();

        if (executor.isTerminated() || runningTasks.isEmpty()) return;

        final long shutdownTimeout = getProperties().shutdownTimeout;
        logger.warning(String.format("Waiting %d ms for Shutting down Task Service. Tasks running %d", shutdownTimeout, runningTasks.size()));

        if (awaitTermination(shutdownTimeout)) return;

        logger.error("Cancel running Tasks");
        cancelTasks();

        // Wait one additional second to give time for the tasks to write the final status
        if (awaitTermination(1_000)) return;
        executor.shutdownNow();

        if (!runningTasks.isEmpty())
            logger.error(String.format("Task shutdown timeout. These task are yet running:\n%s", Colls.mkString(runningTasks, "\n")));
    }

    private void sleep(long milliseconds) {
        doSleep(Math.min(milliseconds, MAX_WAIT_TIME));
    }

    private void stopTask(ScheduledTask t) {
        final TaskEntry taskEntry = invokeInTransaction(() -> TaskEntry.find(t.getFqn()));
        if (taskEntry == null) return;

        final String member = taskEntry.getMember();

        clusterManager.getMembersAddresses()                    //
        .filter(a -> a != null && member.equals(a.toString()))  //
        .getFirst()                                             //
        .ifEmpty(() -> fail(t))                                 //
        .ifPresent(address -> {
            final JmxEndpoint endpoint   = EndpointResolver.resolve(address);
            final String      objectName = TEKGENESIS_DAEMON_NAME + taskEntry.getName() + ",type=task";

            try {
                JmxInvokerImpl.invoker(endpoint).mbean(objectName).invoke("stop", null, null);
            }
            catch (final JmxException e) {
                throw new TaskExecutionException(e);
            }
        });
    }

    private void waitForAvailableThread() {
        while (runningTasks.size() >= concurrentTasks) {
            logger.debug(String.format("Tasks running %d, concurrent %d", runningTasks.size(), concurrentTasks));
            doSleep(MAX_WAIT_TIME);
        }
    }

    private boolean isMaster() {
        return clusterManager.isMaster();
    }

    //~ Methods ......................................................................................................................................

    private static String getAddress() {
        try {
            final Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (final InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if (f.getAddress().isSiteLocalAddress()) return f.getAddress().toString();
            }
            return InetAddress.getLocalHost().toString();
        }
        catch (SocketException | UnknownHostException e) {
            return "NA";
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String SERVICE_NAME        = TaskService.class.getSimpleName();
    private static final int   SERVICE_START_ORDER = 3;

    //~ Inner Classes ................................................................................................................................

    private class ThreadPoolFactory implements ThreadFactory {
        private final ThreadGroup   group        = new ThreadGroup(SERVICE_NAME);
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public Thread newThread(@NotNull Runnable r) {
            final Thread t = new Thread(group, r, format("TaskThread-%04d", threadNumber.getAndIncrement()), 0);
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != NORM_PRIORITY) t.setPriority(NORM_PRIORITY);
            return t;
        }
    }
}  // end class TaskService
