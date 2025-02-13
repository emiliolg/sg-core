
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.io.Closeable;
import java.sql.SQLTimeoutException;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.trace.Trace;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.metric.core.CallMetric;
import tekgenesis.metric.core.Metric;
import tekgenesis.metric.core.MetricsFactory;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionManager;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.metadata.task.TransactionMode.*;
import static tekgenesis.metric.core.MetricSources.TASK;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Processor Task.
 *
 * <p>It takes a list of T a process each item.</p>
 */
public abstract class ProcessorTaskInstance<T> extends ScheduledTaskInstance {

    //~ Instance Fields ..............................................................................................................................

    private int failedItems  = 0;
    private int ignoredItems = 0;

    private Option<T> item         = Option.empty();
    private int       succeedItems = 0;
    private int       totalItems   = 0;

    //~ Constructors .................................................................................................................................

    protected ProcessorTaskInstance(final ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Called after processing all items. It is independent of the Task's transaction and It has his
     * own transaction and it is automatically committed/rolled-back.
     */
    public Status after() {
        return Status.ok();
    }

    /** Called after roll-backing because a Status.abort has been found. */
    @SuppressWarnings("EmptyMethod")
    public void afterAbort() {}

    /**
     * Called before starting processing items.* It is independent of the Task's transaction and It
     * has his own transaction and it is automatically committed/rolled-back.
     */
    public Status before() {
        return Status.ok();
    }

    /** Return elements to process. */
    public abstract Seq<T> enumerate();
    /** Process element. */
    public abstract Status process(T t);

    /** Called after starting a transaction. */
    @SuppressWarnings("EmptyMethod")
    protected void onBegin() {}

    /** Called before a transaction is committed. */
    @SuppressWarnings("EmptyMethod")
    protected void onCommit() {}

    /** Called before a transaction is rolled-back. */
    @SuppressWarnings("EmptyMethod")
    protected void onRollback() {}

    @NotNull @Override protected final Status run() {
        Status status = Status.ok();

        final Seq<T> values = item.isPresent() ? listOf(item.get()) : invokeInTransaction(this::_enumerate);
        // todo Warning this consumes the whole stream if it is a Lazy one....
        totalItems = values.size();

        if (!values.isEmpty()) {
            values.getSize().ifPresent(n -> getProgressMeter().setItemsToProcess(n));
            status = getTransactionMode() == ALL ? processAll(values) : processEach(values);
        }

        getProgressMeter().endTask();
        return status;
    }  // end method run

    /** Return elements to process. */
    @Trace(dispatcher = true)
    Seq<T> _enumerate() {
        // noinspection DuplicateStringLiteralInspection
        final Metric m = MetricsFactory.time(TASK, getClass(), "enumerate");
        try {
            m.start();
            return enumerate();
        }
        finally {
            m.stop();
        }
    }

    /** Process element. */
    @Trace(dispatcher = true)
    Status _process(T t) {
        // noinspection DuplicateStringLiteralInspection
        try(final CallMetric m = MetricsFactory.call(TASK, getClass(), "process")) {
            m.start();
            final Status process = process(t);
            m.mark(process.isSuccess());
            return process;
        }
    }

    @Override boolean manageTransaction() {
        return true;
    }

    void setItem(@Nullable T item) {
        this.item = Option.option(item);
    }

    private Status doProcess(T t) {
        if (mustStop()) return stopTask();
        try {
            return _process(t);
        }
        catch (final Exception e) {
            if (isInterruption(e)) return stopTask();
            if (getTransactionMode() != ISOLATED) throw e;
            logWarning(e);
            return Status.error(e.getMessage());
        }
    }

    private Status doRun(MyTransaction transaction, final Seq<T> items) {
        Status status = Status.ok();

        if (!items.isEmpty()) {
            for (final T t : items) {
                status = doProcess(t);
                if (status.isSuccess()) succeedItems++;
                else {
                    final boolean hasMsg = isNotEmpty(status.getMessage());
                    if (status.isIgnored()) {
                        ignoredItems++;
                        if (hasMsg) logWarning(status.getMessage());
                    }
                    else {
                        failedItems++;
                        if (hasMsg) logError(status.getMessage());
                    }
                }
                transaction.commitIfNecessary(status.isSuccess());
                getProgressMeter().advance();

                if (!status.mustContinue()) break;
            }
        }
        return status;
    }  // end method doRun

    /** Evaluate the supplier inside a db transaction. */
    private Status evaluate(Supplier<Status> supplier) {
        return invokeInTransaction(t -> {
            final Status status = supplier.get();
            if (!status.isSuccess()) t.abort();
            return status;
        });
    }

    /** Process All elements in the same transaction, including after() and before(). */
    @NotNull private Status processAll(Seq<T> values) {
        Status status;

        try(final MyTransaction transaction = new MyTransaction()) {
            status = before();

            if (status.isSuccess() && status.mustContinue()) {
                status = doRun(transaction, values);
                if (status.isSuccess()) {
                    status = after();
                    if (status.isSuccess()) transaction.commit();
                }
            }  // It is done()
            else if (status.isSuccess()) transaction.commit();
        }

        if (!status.isSuccess()) runInTransaction(this::afterAbort);

        runInTransaction(() -> TaskExecutionLog.updateCounts(getTaskEntry(), totalItems, succeedItems, failedItems, ignoredItems));
        return status;
    }

    /** Process element each by one. ISOLATED and EACH transaction applies on this context */
    @NotNull private Status processEach(Seq<T> values) {
        Status status = evaluate(this::before);
        if (status.mustContinue()) {
            try(final MyTransaction transaction = new MyTransaction()) {
                status = doRun(transaction, values);
                if (status.isSuccess()) transaction.commit();
            }
            if (status.isSuccess()) status = evaluate(this::after);
        }
        if (!status.isSuccess()) runInTransaction(() -> {
            afterAbort();                                                                                            //
            TaskExecutionLog.updateCounts(getTaskEntry(), totalItems, succeedItems, failedItems, ignoredItems);      //
        });

        // SUI-2133. When it is none or isolated no matter how was the last item processed the task is marked as ok
        if ((!status.isSuccess() || status.isIgnored()) && !status.mustContinue() &&
            (getTransactionMode() == NONE || getTransactionMode() == ISOLATED)) return Status.ok();
        else return status;
    }

    @NotNull private Status stopTask()
    {
        logWarning("Stopping task '%s'", getName());
        return Status.abort(STOPPED);
    }

    //~ Methods ......................................................................................................................................

    private static boolean isInterruption(Exception e) {
        return e instanceof InterruptedException || e instanceof SQLTimeoutException;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String STOPPED = "Stopped";

    //~ Inner Classes ................................................................................................................................

    private class MyTransaction implements Closeable {
        private final TransactionMode mode;
        private int                   processed;
        @Nullable private Transaction t;

        MyTransaction() {
            mode = getTransactionMode();
            if (mode == NONE) t = null;
            else beginTransaction();
            processed = 0;
        }

        @Override public void close() {
            if (t != null) {
                if (t.isActive()) {
                    onRollback();
                    t.abort();
                }
                t.close();
            }
        }

        void commit() {
            if (t != null && !t.isActive() && Transaction.getCurrent().isEmpty()) {
                logWarning("Transaction Task with no transaction. Make sure you didn't commit/rollback inside your task implementation.");
                t.close();
                t = getSingleton(TransactionManager.class).getOrCreateTransaction();
            }

            if (t != null) {
                onCommit();
                updateTaskEntry();
                TaskExecutionLog.updateCounts(getTaskEntry(), totalItems, succeedItems, failedItems, ignoredItems);
                t.commit();
            }
        }

        void commitIfNecessary(boolean commit) {
            ++processed;
            if (t != null && (mode == ISOLATED || mode == EACH && getBatchSize() == processed)) {
                if (commit) commit();
                else close();
                beginTransaction();
                processed = 0;
            }
        }

        private void beginTransaction() {
            onBegin();
            t = getSingleton(TransactionManager.class).getOrCreateTransaction();
        }
    }  // end class MyTransaction
}  // end class ProcessorTaskInstance
