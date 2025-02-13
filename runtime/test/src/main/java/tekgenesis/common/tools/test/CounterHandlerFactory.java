
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.database.DatabaseFactory;
import tekgenesis.persistence.*;
import tekgenesis.persistence.sql.SqlStoreHandlerFactory;

/**
 * A Store handler that count the number of invocations.
 */
public class CounterHandlerFactory extends SqlStoreHandlerFactory {

    //~ Instance Fields ..............................................................................................................................

    private final Counter counters;

    //~ Constructors .................................................................................................................................

    /** Create the factory. */
    public CounterHandlerFactory(@NotNull final DatabaseFactory<?> databaseFactory) {
        super(databaseFactory);
        counters = new Counter();
    }

    //~ Methods ......................................................................................................................................

    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        try {
            final StoreHandler<I, K> handler = super.createHandler(storeType, dbTable);
            return handler == null ? null : new Handler<>(handler, counters);
        }
        catch (final IllegalStateException e) {
            return null;
        }
    }

    /** Return counters. */
    public Counter getCounters() {
        return counters;
    }

    //~ Inner Classes ................................................................................................................................

    public static class Counter {
        public int deletes = 0;
        public int finds   = 0;
        public int inserts = 0;
        public int merges;
        public int updates = 0;

        /** Reset the Counter. */
        public void reset() {
            deletes = 0;
            inserts = 0;
            updates = 0;
            finds   = 0;
            merges  = 0;
        }
    }

    private static class Handler<T extends EntityInstance<T, K>, K> extends DelegatingStoreHandler<T, K> {
        private final Counter c;

        private Handler(StoreHandler<T, K> delegate, Counter c) {
            super(delegate);
            this.c = c;
        }

        @Override public void delete(T instance) {
            c.deletes++;
            super.delete(instance);
        }
        @Override public void deleteWhere(Criteria... condition) {
            c.deletes++;
            super.deleteWhere(condition);
        }

        @Override public synchronized T find(K key) {
            c.finds += 1;
            return super.find(key);
        }

        @Nullable @Override public T findByKey(final int keyId, final Object key) {
            c.finds += 1;
            return super.findByKey(keyId, key);
        }

        @Override public void insert(T instance, boolean generateKey) {
            c.inserts += 1;
            super.insert(instance, generateKey);
        }

        @Override public synchronized ImmutableList<T> list(Iterable<K> keys) {
            for (final K ignored : keys)
                c.finds += 1;
            return super.list(keys);
        }

        @Override public void merge(@NotNull T instance) {
            c.merges += 1;
            super.merge(instance);
        }

        @Override public void update(T instance) {
            c.updates += 1;
            super.update(instance);
        }
        @Override public void updateLocking(T instance) {
            c.updates += 1;
            super.updateLocking(instance);
        }
    }  // end class Handler
}  // end class CounterHandlerFactory
