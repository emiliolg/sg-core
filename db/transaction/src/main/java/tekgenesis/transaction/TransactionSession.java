
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
import java.util.*;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.transaction.TransactionListener.Operation.*;

class TransactionSession implements Iterable<TransactionSession.ConnectionEntry<?>> {

    //~ Instance Fields ..............................................................................................................................

    private boolean                                                                          batch;
    private boolean                                                                          closed;
    private final Map<String, ConnectionEntry<?>>                                            connectionMap;
    private final Map<TransactionListener<? extends TransactionContext>, TransactionContext> context;
    private Transaction                                                                      current;
    private final JDBCTransactionManager                                                     tm;
    private boolean                                                                          transactionStarted;

    //~ Constructors .................................................................................................................................

    TransactionSession(JDBCTransactionManager tm) {
        this.tm       = tm;
        connectionMap = new LinkedHashMap<>();
        context       = new HashMap<>();
        current       = null;
        closed        = false;
    }

    //~ Methods ......................................................................................................................................

    @Override public Iterator<ConnectionEntry<?>> iterator() {
        return Colls.toList(connectionMap.values()).iterator();
    }

    void beginBatch() {
        batch = true;
    }

    void beginListener(TransactionListener<? extends TransactionContext> l) {
        context.put(l, l.invoke(BEGIN, null));
    }
    void close() {
        if (closed) return;
        try {
            for (final ConnectionEntry<?> cw : connectionMap.values()) {
                if (!cw.closeLast()) cw.close();
            }
        }
        finally {
            for (final ConnectionEntry<?> cw : connectionMap.values()) {
                if (cw.closeLast()) cw.close();
            }
        }
        connectionMap.clear();
        context.clear();
        transactionStarted = false;
        tm.removeSession();
        current = null;
        closed  = true;
    }

    void commit() {
        if (closed) return;
        invokeEndTransactionListeners(COMMIT, getListenersList());
        endBatch();
        for (final ConnectionEntry<?> e : this)
            e.commit();
        doClose(true);
    }

    void endBatch() {
        if (batch) {
            batch = false;
            for (final ConnectionEntry<?> e : this)
                e.endBatch();
        }
    }
    void logCommit(boolean v) {
        for (final ConnectionEntry<?> e : this)
            e.logCommit(v);
    }

    void removeListener(TransactionListener<? extends TransactionContext> listener) {
        context.remove(listener);
    }

    void rollback() {
        if (closed) return;
        if (transactionStarted) {
            invokeEndTransactionListeners(ROLLBACK, getListenersList());
            rollbackBatch();
            for (final ConnectionEntry<?> e : this)
                e.rollback();
        }
        doClose(false);
    }

    void rollbackBatch() {
        batch = false;
    }

    <T extends Connection> ConnectionEntry<T> getConnectionEntry(TransactionResource<T> resource)
        throws SQLException
    {
        if (current == null && !Boolean.getBoolean("transaction.unavailable.ignore")) {
            final TransactionUnavailableException e = new TransactionUnavailableException();
            // noinspection DuplicateStringLiteralInspection
            if (System.getenv("TRANS") != null) throw e;
            logger.error(e);
        }
        final ConnectionEntry<?> entry = connectionMap.get(resource.getName());
        if (entry != null) return cast(entry);

        final ConnectionEntry<T> r = new ConnectionEntry<>(resource);
        connectionMap.put(resource.getName(), r);
        return r;
    }

    <T extends TransactionContext> T getContext(TransactionListener<T> listener) {
        TransactionContext ctx = context.get(listener);
        if (ctx == null) {
            startTransaction();
            ctx = context.get(listener);
        }
        return cast(ctx);
    }

    Option<Transaction> getCurrentTransaction() {
        return Option.ofNullable(current != null ? current : transactionStarted ? createTransaction() : null);
    }

    boolean isClosed() {
        return closed;
    }

    boolean isTransactionStarted() {
        return transactionStarted;
    }

    boolean isBatchActive() {
        return batch;
    }

    @NotNull Transaction getOrCreateTransaction() {
        return getCurrentTransaction().orElseGet(this::createTransaction);
    }

    TransactionManager getTransactionManager() {
        return tm;
    }

    private Transaction createTransaction() {
        return current = new Transaction(this);
    }

    private void doClose(boolean commit) {
        final List<Consumer<Boolean>>                                               afterMethods = current.getAfterMethods();
        final LinkedList<Tuple<TransactionListener<?>, Option<TransactionContext>>> list         = getListenersList();
        close();
        invokeEndTransactionListeners(commit ? AFTER_COMMIT : AFTER_ROLLBACK, list);
        afterMethods.forEach(am -> tm.runInTransaction(t -> am.accept(commit)));
    }

    private void invokeEndTransactionListeners(TransactionListener.Operation                                         operation,
                                               LinkedList<Tuple<TransactionListener<?>, Option<TransactionContext>>> listeners) {
        final Iterator<Tuple<TransactionListener<?>, Option<TransactionContext>>> it = listeners.descendingIterator();
        while (it.hasNext()) {
            final Tuple<TransactionListener<?>, Option<TransactionContext>> next     = it.next();
            final TransactionListener<TransactionContext>                   listener = cast(next.first());
            final TransactionContext                                        ctx      = next.second().getOrNull();
            if (ctx != null) listener.invoke(operation, ctx);
        }
    }  // end method invokeEndTransactionListeners

    private void startTransaction() {
        if (!transactionStarted) {
            transactionStarted = true;
            for (final TransactionListener<? extends TransactionContext> l : tm.getListeners())
                beginListener(l);
        }
    }

    @NotNull private LinkedList<Tuple<TransactionListener<?>, Option<TransactionContext>>> getListenersList() {
        final LinkedList<TransactionListener<? extends TransactionContext>>         listeners = tm.getListeners();
        final LinkedList<Tuple<TransactionListener<?>, Option<TransactionContext>>> list      = new LinkedList<>();
        for (final TransactionListener<? extends TransactionContext> listener : listeners)
            list.add(Tuple.tuple(listener, option(context.get(listener))));
        return list;
    }

    //~ Methods ......................................................................................................................................

    private static void closeResource(AutoCloseable closeable) {
        try {
            closeable.close();
        }
        catch (final Exception e) {
            logger.warning(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(TransactionSession.class);

    //~ Inner Classes ................................................................................................................................

    class ConnectionEntry<T extends Connection> implements ConnectionReference<T> {
        private T                                                   connection;
        private boolean                                             logCommit;
        private int                                                 refCount;
        private final IdentityHashMap<AutoCloseable, AutoCloseable> resources;
        private final TransactionResource<T>                        transactionResource;
        private boolean                                             transactionStartedForConnection;

        ConnectionEntry(TransactionResource<T> transactionResource)
            throws SQLException
        {
            connection               = transactionResource.createConnection();
            this.transactionResource = transactionResource;
            resources                = new IdentityHashMap<>();
            refCount                 = 0;
        }

        @Override public void detach() {
            refCount--;
        }

        @Override public void detach(AutoCloseable resource) {
            detach();
            resources.remove(resource);
        }

        @Override public void ensureTransactionStarted() {
            transactionStartedForConnection = true;
            startTransaction();
        }

        @Override public T get() {
            return get(null);
        }

        public T get(@Nullable AutoCloseable r) {
            try {
                if (connection.isClosed()) connection = transactionResource.createConnection();
            }
            catch (final SQLException ignore) {}
            if (r != null) resources.put(r, r);
            refCount++;
            return connection;
        }

        public void logCommit(final boolean v) {
            logCommit = v;
        }

        @Override public String toString() {
            return transactionResource.getName();
        }

        void close() {
            if (transactionStartedForConnection) {
                logger.warning(new RuntimeException("Rolling back transaction on close. Do you forget to add a Commit ??"));
                rollback();
            }
            else if (transactionResource.commitOnClose()) doCommit();
            try {
                first(resources.keySet()).ifPresent(o -> {
                    new ArrayList<>(resources.values()).forEach(TransactionSession::closeResource);
                    resources.clear();
                    throw new LeakException(format("Resource %s not closed: %s", o.getClass(), o));
                });
                if (refCount > 0) throw new LeakException("Trying to close a connection with " + refCount + " live references");
            }
            catch (final NullPointerException npe) {
                final StackTraceElement[] stackTrace = npe.getStackTrace();
                if (stackTrace.length <= 0 || !stackTrace[0].toString().startsWith("org.hsqldb.Session.setReadOnlyDefault")) logger.error(npe);
            }
            finally {
                try {
                    refCount = 0;
                    connection.close();
                }
                catch (final SQLException e) {
                    logger.error(e);
                }
            }
        }

        boolean closeLast() {
            return transactionResource.closeLast();
        }

        void commit() {
            if (transactionStartedForConnection) {
                if (logCommit) {
                    logger.warning(new Throwable("Logging Commit"));
                    logCommit = false;
                }
                doCommit();
                transactionStartedForConnection = false;
            }
        }

        void endBatch() {
            transactionResource.endBatch(connection);
        }

        void rollback() {
            if (transactionStartedForConnection) {
                try {
                    connection.rollback();
                }
                catch (final SQLException e) {
                    logger.error(e);
                }
                finally {
                    transactionStartedForConnection = false;
                }
            }
        }

        private void doCommit() {
            try {
                connection.commit();
            }
            catch (final SQLException e) {
                logger.error(e);
            }
        }
    }  // end class ConnectionEntry
}  // end class TransactionSession
