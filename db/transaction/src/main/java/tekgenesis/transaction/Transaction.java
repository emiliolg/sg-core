
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.env.context.Context.getSingleton;

/**
 * A Transaction class.
 */
public class Transaction implements AutoCloseable {

    //~ Instance Fields ..............................................................................................................................

    private boolean abort;

    private final List<Consumer<Boolean>> afterMethods;

    private final TransactionSession session;

    //~ Constructors .................................................................................................................................

    Transaction(TransactionSession session) {
        this.session = session;
        abort        = false;
        afterMethods = new LinkedList<>();
    }

    //~ Methods ......................................................................................................................................

    /**
     * Set the mark for rollback status for the current transaction. And transaction will be
     * rolled-back on close.
     */
    public Transaction abort() {
        abort = true;
        return this;
    }

    /** Begin a batch ver the current transaction. */
    public void beginBatch() {
        session.beginBatch();
    }

    @Override public void close() {
        session.rollback();
    }

    /** Commit the transaction. */
    public void commit() {
        doCommit();
    }

    /** Ends the batch over the current transaction. */
    public void endBatch() {
        session.endBatch();
    }
    /**
     * Run the specified Function (that receives a transaction as a parameter) inside a new
     * Transaction. If a current transaction is present it will be suspended and restore after the
     * nested one is closed Beware! You can lock yourself using this method
     */
    public <T> T invokeInNestedTransaction(Function<Transaction, T> fn) {
        return getTransactionManager().invokeInNestedTransaction(fn);
    }

    /** Log the commit. */
    public void logCommit(boolean v) {
        session.logCommit(v);
    }

    /** Rollback the transaction. (Avoid to be deprecated) Use @link {@link Transaction#abort} */
    public void rollback() {
        if (checkOpenSession(Transaction::rollback)) session.rollback();
    }

    /**
     * Invoke the specified Consumer after the Transaction is committed or rolled-back. True is
     * passed as parameter if the transaction was committed
     */
    public void runAfter(Consumer<Boolean> runAfterWithTrueIfCommitted) {
        afterMethods.add(runAfterWithTrueIfCommitted);
    }
    /**
     * Run the specified Consumer (that receives a transaction as a parameter) inside a new
     * Transaction. If a current transaction is present it will be suspended and restore after the
     * nested one is closed Beware! You can lock yourself using this method
     */
    public void runInNestedTransaction(Consumer<Transaction> fn) {
        getTransactionManager().runInTransaction(fn);
    }

    /** Get The context for the specified Listener. */
    public <T extends TransactionContext> T getContext(TransactionListener<T> l) {
        return session.getContext(l);
    }

    /** Returns true if the current transaction is active. */
    public boolean isActive() {
        return session.isTransactionStarted();
    }

    /** Package private commit. */
    void doCommit() {
        if (checkOpenSession(Transaction::doCommit)) {
            if (abort) session.rollback();
            else session.commit();
        }
    }

    List<Consumer<Boolean>> getAfterMethods() {
        return afterMethods;
    }

    TransactionSession getSession() {
        return session;
    }

    private boolean checkOpenSession(Consumer<Transaction> action) {
        if (!session.isClosed()) return true;
        session.getTransactionManager().getCurrentTransaction().ifPresent(t -> {
            logger.warning("The current transaction was spuriously commit and a new one was created. Closing the new one");
            action.accept(t);
        });
        return false;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Sets a Transaction listener. The listener will be invoked before and after the transaction is
     * created
     */
    public static void addListener(TransactionListener<? extends TransactionContext> listener) {
        getTransactionManager().addListener(listener);
    }
    /** Invoke the specified Supplier inside a Transaction. */
    public static <T> T invokeInTransaction(Supplier<T> s) {
        return getTransactionManager().invokeInTransaction(t -> s.get());
    }
    /**
     * Invoke the specified Function (that receives a transaction as a parameter) inside a
     * Transaction.
     */
    public static <T> T invokeInTransaction(Function<Transaction, T> fn) {
        return getTransactionManager().invokeInTransaction(fn);
    }

    /** Remove a Transaction Listener. */
    public static void removeListener(final TransactionListener<? extends TransactionContext> listener) {
        getTransactionManager().removeListener(listener);
    }

    /** Run the specified block inside a Transaction. */
    public static void runInTransaction(Runnable r) {
        getTransactionManager().runInTransaction(t -> r.run());
    }

    /**
     * Run the specified Consumer (that receives a transaction as a parameter) inside a Transaction.
     */
    public static void runInTransaction(Consumer<Transaction> fn) {
        getTransactionManager().runInTransaction(fn);
    }

    /** Get the current Transaction. */
    public static Option<Transaction> getCurrent() {
        return getTransactionManager().getCurrentTransaction();
    }

    /** Get the current Transaction or fail. */
    public static Transaction getCurrentOrFail() {
        return getCurrent().orElseThrow(TransactionUnavailableException::new);
    }

    static TransactionManager getTransactionManager() {
        return getSingleton(TransactionManager.class);
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(Transaction.class);
}  // end class Transaction
