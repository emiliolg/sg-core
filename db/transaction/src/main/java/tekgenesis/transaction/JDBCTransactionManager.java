
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
import java.util.LinkedList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

/**
 * An Implementation of a Transaction Manager over a JDBC connection using a Connection pool for
 * each DataSource. This Class DOES NOT implement a 2PC and does not use JTA/ It just creates
 * separated transactions in each DataSource and sequentially commit all of them
 */
public class JDBCTransactionManager extends TransactionManager {

    //~ Instance Fields ..............................................................................................................................

    private final LinkedList<TransactionListener<? extends TransactionContext>> listeners;

    private final ThreadLocal<TransactionSession> session;

    //~ Constructors .................................................................................................................................

    /** Create a JDBCTransactionManager. */
    public JDBCTransactionManager() {
        listeners = new LinkedList<>();
        session   = ThreadLocal.withInitial(() -> new TransactionSession(this));
    }

    //~ Methods ......................................................................................................................................

    @Override public void addFirstListener(final TransactionListener<? extends TransactionContext> listener) {
        if (!listeners.isEmpty() && listeners.getFirst() == listener) return;
        synchronized (listeners) {
            if (listeners.contains(listener)) return;
            listeners.addFirst(listener);
        }
    }

    @Override public <T extends Connection> ConnectionReference<T> getConnectionRef(TransactionResource<T> resource)
        throws SQLException
    {
        return getSession().getConnectionEntry(resource);
    }

    public Option<Transaction> getCurrentTransaction() {
        return getSession().getCurrentTransaction();
    }

    @Override public boolean isBatchActive() {
        return getSession().isBatchActive();
    }

    @NotNull @Override public Transaction getOrCreateTransaction() {
        return getSession().getOrCreateTransaction();
    }

    @Override void addListener(final TransactionListener<? extends TransactionContext> listener) {
        if (listeners.contains(listener)) return;
        synchronized (listeners) {
            if (listeners.contains(listener)) return;
            listeners.addLast(listener);
            getCurrentTransaction().ifPresent(t -> t.getSession().beginListener(listener));
        }
    }
    @Override void removeListener(final TransactionListener<? extends TransactionContext> listener) {
        if (!listeners.contains(listener)) return;
        synchronized (listeners) {
            if (!listeners.contains(listener)) return;
            listeners.remove(listener);
            getCurrentTransaction().ifPresent(t -> t.getSession().removeListener(listener));
        }
    }

    void removeSession() {
        session.remove();
    }

    @Override void restore(Transaction t) {
        session.set(t.getSession());
    }

    @Override void suspend(Transaction t) {
        removeSession();
    }

    LinkedList<TransactionListener<? extends TransactionContext>> getListeners() {
        return listeners;
    }

    @Override TransactionSession getSession() {
        return session.get();
    }
}  // end class JDBCTransactionManager
