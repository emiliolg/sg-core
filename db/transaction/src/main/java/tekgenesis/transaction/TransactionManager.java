
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

/**
 * The TransactionManager abstracts the management of Transactions and Connections.
 */
public abstract class TransactionManager {

    //~ Methods ......................................................................................................................................

    /** Add a listener in the first position to the transaction context. */
    public abstract void addFirstListener(TransactionListener<? extends TransactionContext> listener);

    /**
     * Run the specified Function (that receives a transaction as a parameter) inside a new
     * Transaction. If a current transaction is present it will be suspended and restore after the
     * nested one is closed Beware! You can lock yourself using this method
     */
    public <T> T invokeInNestedTransaction(Function<Transaction, T> fn) {
        final Option<Transaction> ct = getCurrentTransaction();
        if (ct.isEmpty()) return doInvoke(fn);
        final Transaction t = ct.get();
        try {
            suspend(t);
            return doInvoke(fn);
        }
        finally {
            restore(t);
        }
    }

    /**
     * Run the specified Function (that receives a transaction as a parameter) inside a Transaction.
     */
    public <T> T invokeInTransaction(Function<Transaction, T> fn) {
        final Option<Transaction> ct = getCurrentTransaction();
        return ct.isPresent() ? fn.apply(ct.get()) : doInvoke(fn);
    }
    /**
     * Run the specified Consumer (that receives a transaction as a parameter) inside a new
     * Transaction. If a current transaction is present it will be suspended and restore after the
     * nested one is closed Beware! You can lock yourself using this method
     */
    public void runInNestedTransaction(Consumer<Transaction> fn) {
        invokeInTransaction(t -> {
            fn.accept(t);
            return 0;
        });
    }

    /**
     * Run the specified Consumer (that receives a transaction as a parameter) inside a Transaction.
     */
    public void runInTransaction(Consumer<Transaction> consumer) {
        getCurrentTransaction().ifPresentOrElse(consumer,
            () -> {
                try(Transaction t = getOrCreateTransaction()) {
                    consumer.accept(t);
                    t.doCommit();
                }
            });
    }

    /** Find or create a connection in the specified {@link TransactionResource}. */
    public abstract <T extends Connection> ConnectionReference<T> getConnectionRef(TransactionResource<T> resource)
        throws SQLException;

    /** Returns current Transaction, if there is one opened. */
    public abstract Option<Transaction> getCurrentTransaction();

    /** Returns true if a batch is active. */
    public abstract boolean isBatchActive();

    /** Create a transaction. */
    @NotNull public abstract Transaction getOrCreateTransaction();

    /** Add a listener to the transaction context. */
    abstract void addListener(TransactionListener<? extends TransactionContext> listener);

    /** Remove a listener from the transaction context. */
    abstract void removeListener(final TransactionListener<? extends TransactionContext> listener);
    abstract void restore(Transaction t);
    abstract void suspend(Transaction t);
    abstract TransactionSession getSession();

    private <T> T doInvoke(Function<Transaction, T> fn) {
        try(Transaction t = getOrCreateTransaction()) {
            final T r = fn.apply(t);
            t.doCommit();
            return r;
        }
    }
}  // end interface TransactionManager
