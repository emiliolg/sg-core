
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.util.Map;
import java.util.concurrent.*;

import javax.management.MalformedObjectNameException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MDC;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Maps;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.jmx.JmxHelper;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.util.ProgressMeter;
import tekgenesis.common.util.Reflection;
import tekgenesis.database.SqlStatement;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.persistence.IxService;
import tekgenesis.persistence.Sql;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.sg.g.TaskEntryTable;
import tekgenesis.transaction.Transaction;

import static java.util.Arrays.asList;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.LIFECYCLE_KEY;
import static tekgenesis.common.core.Constants.TASK_LIST_FILE;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.Reflection.construct;
import static tekgenesis.common.util.Resources.readResources;
import static tekgenesis.database.SqlStatement.addCurrentStatementListener;
import static tekgenesis.database.SqlStatement.removeCurrentStatementListeners;
import static tekgenesis.metadata.task.TaskType.RUNNABLE;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Base Task.
 */
public abstract class Task<K extends TaskInstance<K>> implements Future<Status>, Callable<Status> {

    //~ Instance Fields ..............................................................................................................................

    private SqlStatement.Terminator currentStatement = null;

    private String                      fqn;
    @Nullable private Future<Status>    future;
    @NotNull private final transient K  instance;
    private boolean                     stop        = false;
    @Nullable private TaskService       taskService;
    private String                      uuid        = null;

    //~ Constructors .................................................................................................................................

    /** Create a Task. */
    Task(@NotNull Class<? extends K> taskClass) {
        instance    = invokeInTransaction(() -> construct(taskClass, this));
        fqn         = instance.getName();
        future      = null;
        taskService = null;
        stop        = false;
    }

    /** Create a Task. */
    Task(@NotNull String taskFqn) {
        this(Reflection.findClass(taskFqn));
    }

    //~ Methods ......................................................................................................................................

    /** Run Task.* */
    @NotNull public Status call() {
        // todo use FutureTask ?
        Status status = Status.done();
        registerMBean();
        final String origName = Thread.currentThread().getName();
        Thread.currentThread().setName(origName + "-" + extractName(getFqn()));
        runInTransaction(() -> TaskExecutionLog.start(instance.getTaskEntry()));

        try {
            MDC.put(LIFECYCLE_KEY, instance.getMdc());
            instance.logInfo("Task execution started: '%s.", getFqn());
            SecurityUtils.setSystemSurrogate(getFqn());
            addCurrentStatementListener(statement -> currentStatement = statement);
            status = instance.shouldExecute() ? doRun() : status;
            closeTransaction(status);
        }
        catch (final Throwable e) {
            ensureCloseTransaction(false);
            status = Status.error(e.getMessage());
            instance.logError(e);
        }
        finally {
            IxService.ensureCloseBatch();
            endExecution(status);
            removeCurrentStatementListeners();
            // todo purge execution log
            if (taskService != null) taskService.notifyCompletion(this);
            else instance.logWarning("Task Service not set for current Task %s", getFqn());
            SecurityUtils.clearSystemSurrogate();
            JmxHelper.unregisterMBean(getFqn(), TEK_GENESIS_DAEMON, TASK);

            logState(status);

            MDC.remove(LIFECYCLE_KEY);
            Thread.currentThread().setName(origName);
            executeScheduleAfterTasks();
            Transaction.getCurrent().ifPresent(Transaction::close);
        }
        return status;
    }  // end method call

    @Override public boolean cancel(boolean mayInterruptIfRunning) {
        if (future == null) return true;
        if (currentStatement != null) currentStatement.cancel();
        stop = true;
        synchronized (getInstance()) {
            try {
                getInstance().notify();
            }
            catch (final Exception ignore) {}
        }
        return future.cancel(false);
    }

    /** Equals for class. */
    @Override public boolean equals(Object obj) {
        return obj == this || obj instanceof Task && ((Task<?>) obj).getFqn().equals(fqn);
    }

    @Override public Status get()
        throws InterruptedException, ExecutionException
    {
        return future == null ? Status.ok() : future.get();
    }

    @Override public Status get(long timeout, @NotNull TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException
    {
        return future == null ? Status.ok() : future.get(timeout, unit);
    }

    @Override public int hashCode() {
        return fqn.hashCode();
    }

    /** Initialize task. */
    public void initialize(TaskEntry taskEntry) {
        setUuid(taskEntry.getMdc());
        getInstance().setTaskEntry(taskEntry);
    }

    /** Resume execution of task. */
    public boolean resume() {
        return TaskEntry.resume(getFqn());
    }

    /** Mark as must stop the current task. */
    public void stop() {
        stop = true;
    }

    /** Suspend cron execution. */
    public boolean suspend() {
        return TaskEntry.suspend(getFqn());
    }

    /** @return  Get completion status. A number from 0 to 100 */
    public int getCompletion() {
        final ProgressMeter progressMeter = getInstance().getProgressMeter();
        return progressMeter == null ? 0 : (int) progressMeter.getTotalAdvance();
    }

    @Override public boolean isCancelled() {
        return future != null && future.isCancelled();
    }

    @Override public boolean isDone() {
        return future == null || future.isDone();
    }

    /** Get Task Instance. */
    @NotNull public <T extends K> T getInstance() {
        return cast(instance);
    }

    /** @return  The phase name */
    public String getPhase() {
        return getInstance().getProgressMeter().getCurrentPhaseName();
    }

    void endExecution(Status status) {
        instance.getTaskEntry().endExecution(status, null);
    }

    void initialize(TaskService current, TaskEntry taskEntry) {
        initialize(taskEntry);
        taskService = current;
    }

    boolean mustStop() {
        return stop;
    }

    String getFqn() {
        return fqn;
    }

    void setFqn(String fqn) {
        this.fqn = fqn;
    }

    void setFuture(@NotNull Future<Status> future) {
        this.future = future;
    }

    @NotNull TaskProps getTaskProperties(Environment env) {
        return env.get(idFromName(instance.getName()), TaskProps.class);
    }

    /** .* */
    String getUuid() {
        return uuid;
    }

    void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // Todo refactor
    private void closeTransaction(Status status) {
        Transaction.getCurrent().filter(Transaction::isActive).ifPresent(t -> {
            if (getInstance().getTransactionMode() == TransactionMode.NONE) {
                instance.logError("Task '%s' finished without closing Database Transaction. Current transaction is committed", getFqn());
                return;
            }
            manageStatus(t, status);
        });
    }  // end method closeTransaction

    @NotNull private Status doRun() {
        return instance.manageTransaction() ? instance._run() : invokeInTransaction(t -> manageStatus(t, instance._run()));
    }

    // Todo refactor
    private void ensureCloseTransaction(boolean shouldCommit) {
        Transaction.getCurrent().filter(Transaction::isActive).ifPresent(t -> {
            // Just check that the user commit/rollback their db stuffs
            if (shouldCommit) t.commit();
            else t.abort();

            // noinspection DuplicateStringLiteralInspection
            instance.logError("Task '%s' does not close Database Transaction. Current transaction is %s",
                getFqn(),
                shouldCommit ? "committed" : "rolled-back");
        });
    }

    private void executeScheduleAfterTasks() {
        if (taskService == null) throw new IllegalStateException("Task Service not initialized");
        final ImmutableList<TaskEntry> taskEntries = invokeInTransaction(() ->
                    Sql.selectFrom(TaskEntryTable.TASK_ENTRY)
                       .where(TaskEntryTable.TASK_ENTRY.SCHEDULE_AFTER.eq(getFqn()))
                       .list()
                       .toList());
        taskEntries.forEach(te -> taskService.submit(te.getName()));
    }

    private void logState(Status status) {
        if (status.isSuccess()) instance.logInfo("Task '%s' execution completed successfully", getFqn());
        else if (status.isIgnored()) instance.logWarning("Task '%s' execution completed with ignores: %s", getFqn(), status.getMessage());
        else instance.logError("Task '%s' execution completed with the errors: %s", getFqn(), status.getMessage());
    }

    private Status manageStatus(Transaction t, Status status) {
        if (status.isSuccess()) t.commit();
        else {
            if (status.isIgnored()) instance.logWarning("Task execution ignored '%s' (cause: %s )", getFqn(), status.getMessage());
            else instance.logError("Error executing task '%s' : %s", getFqn(), status.getMessage());
            t.rollback();
        }
        return status;
    }  // end method manageStatus

    private void registerMBean() {
        final tekgenesis.task.jmx.Task info = new tekgenesis.task.jmx.Task(this);
        try {
            final String resourceName = getFqn();
            if (JmxHelper.isRegistered(JmxHelper.createObjectName(resourceName, TEK_GENESIS_DAEMON, TASK)))
                JmxHelper.unregisterMBean(resourceName, TEK_GENESIS_DAEMON, TASK);
            JmxHelper.registerMBean(resourceName, TEK_GENESIS_DAEMON, TASK, info);
        }
        catch (final MalformedObjectNameException e) {
            instance.logError(e);
        }
    }

    //~ Methods ......................................................................................................................................

    /** List Scheduled tasks. */
    public static Seq<String> listScheduledTasks() {
        return filter(TaskHolder.tasks.entrySet(), e -> e != null && e.getValue().isScheduled()).map(Map.Entry::getKey);
    }

    /** List all Tasks. */
    public static Seq<String> listTasks() {
        return immutable(TaskHolder.tasks.keySet());
    }

    /** List tasks by type. */
    public static Seq<String> listTasks(final TaskType type) {
        return filter(TaskHolder.tasks.entrySet(), e -> e != null && e.getValue() == type).map(Map.Entry::getKey);
    }
    /** List tasks by type. */
    public static Seq<String> listTasks(final TaskType... type) {
        return filter(TaskHolder.tasks.entrySet(), e -> e != null && asList(type).contains(e.getValue())).map(Map.Entry::getKey);
    }

    /** Get Task Id based on the Name. */
    @NotNull static String idFromName(final String fqn) {
        return deCapitalizeFirst(extractName(fqn));
    }

    /** List Scheduled tasks. */
    static Seq<String> listScheduleNodeTasks() {
        return filter(TaskHolder.tasks.entrySet(), e -> e != null && e.getValue() == TaskType.NODE_RUNNABLE).map(Map.Entry::getKey);
    }

    //~ Static Fields ................................................................................................................................

    private static final String TASK = "task";

    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEK_GENESIS_DAEMON = "tekgenesis.daemon";

    //~ Inner Interfaces .............................................................................................................................

    // Lazy initialization holder class idiom for static fields
    private interface TaskHolder {
        Map<String, TaskType> tasks = Maps.map(readResources(TASK_LIST_FILE),
                line -> {
                    int p = line.indexOf(' ');
                    return p == -1 ? tuple(line, RUNNABLE) : tuple(line.substring(0, p).trim(), TaskType.valueOf(line.substring(p + 1).trim()));
                });
    }
}  // end class Task
