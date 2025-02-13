
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.Database;
import tekgenesis.persistence.*;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionContext;
import tekgenesis.transaction.TransactionListener;

/**
 * Index Handler Factory.
 */
public class ElasticSearchIndexer extends StoreHandler.DefaultFactory implements TransactionListener<ElasticSearchIndexer.IdxTransactionCtx> {

    //~ Instance Fields ..............................................................................................................................

    private final Database     db;
    private final IndexManager manager;

    //~ Constructors .................................................................................................................................

    /** Create the factory. */
    public ElasticSearchIndexer(final Database db, StoreHandler.Factory parent, @NotNull final IndexManager manager) {
        super(parent);
        this.db      = db;
        this.manager = manager;
        Transaction.addListener(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        final StoreHandler<I, K> parent = getParentFactory().createHandler(storeType, dbTable);
        return parent == null ? null : dbTable.metadata()
                                       .getIndexSearcher()
                                       .map(s -> (StoreHandler<I, K>) new SessionIndexHandler<>(s, parent))
                                       .orElse(parent);
    }

    @NotNull @Override public IdxTransactionCtx invoke(Operation operation, @Nullable IdxTransactionCtx ctx) {
        if (ctx == null) return new IdxTransactionCtx();
        if (operation == Operation.AFTER_COMMIT && (ctx.since != null || !ctx.deletions.isEmpty())) {
            manager.deleteInstanceDocs(ctx.deletions);
            manager.indexEntityTablesSince(ctx.searchers, ctx.since);
            ctx.clear();
        }
        return ctx;
    }

    //~ Methods ......................................................................................................................................

    /** Mute index events. */
    public static void mute() {
        muted.set(Boolean.TRUE);
    }

    /** Mute index events. */
    public static void refreshingView(boolean b) {
        refreshingView.set(b);
    }

    /** Un-mute index events. */
    public static void unMute() {
        muted.set(Boolean.FALSE);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(ElasticSearchIndexer.class);

    private static final ThreadLocal<Boolean> muted          = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private static final ThreadLocal<Boolean> refreshingView = ThreadLocal.withInitial(() -> Boolean.FALSE);

    //~ Inner Classes ................................................................................................................................

    public class IdxTransactionCtx implements TransactionContext {
        private final List<EntityInstance<?, ?>> deletions = new LinkedList<>();
        private final Set<IndexSearcher>         searchers = new LinkedHashSet<>();
        private DateTime                         since     = null;

        /** clear context. */
        public void clear() {
            since = null;
            searchers.clear();
            deletions.clear();
        }

        void add(EntityInstance<?, ?> instance, boolean useInstanceTime) {
            if (since == null) since = useInstanceTime ? instance.getUpdateTime() : db.currentTime();
            final Option<IndexSearcher> searcher = instance.metadata().getIndexSearcher();
            searcher.ifPresent(searchers::add);
        }

        void delete(EntityInstance<?, ?> instance) {
            // if (since == null) since = db.currentTime();
            deletions.add(instance);
        }
    }

    private class SessionIndexHandler<T extends EntityInstance<T, K>, K> extends DelegatingStoreHandler<T, K> {
        private final IndexSearcher searcher;

        public SessionIndexHandler(IndexSearcher searcher, StoreHandler<T, K> parent) {
            super(parent);
            this.searcher = searcher;
        }

        @Override public void delete(T instance) {
            addEvent(instance, true, refreshingView.get());
            super.delete(instance);
        }

        @Override public void deleteWhere(Criteria... condition) {
            final ImmutableList<T> toDelete = Sql.selectFrom(getDbTable()).where(condition).list();
            for (final T t : toDelete)
                addEvent(t, true, refreshingView.get());
            super.deleteWhere(condition);
        }

        @Override public void insert(T instance, boolean generateKey) {
            addEvent(instance);
            super.insert(instance, generateKey);
        }

        @Override public void merge(@NotNull T instance) {
            addEvent(instance);
            super.merge(instance);
        }

        @Override public void update(T instance) {
            addEvent(instance);
            super.update(instance);
        }

        private void addEvent(final T instance) {
            addEvent(instance, false, refreshingView.get());
        }

        private void addEvent(T instance, boolean delete, boolean useInstanceTime) {
            if (muted.get() || instance.metadata().getSearcher().isEmpty()) return;
            final IdxTransactionCtx context = TransactionContext.getCurrent(ElasticSearchIndexer.this);

            if (delete) context.delete(instance);
            else context.add(instance, useInstanceTime);
        }
    }  // end class SessionIndexHandler
}  // end class ElasticSearchIndexer
