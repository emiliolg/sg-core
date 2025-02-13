
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * A Store Handler that delegates the operations on a delegate.
 */
public class DelegatingStoreHandler<T extends EntityInstance<T, K>, K> extends StoreHandler<T, K> {

    //~ Constructors .................................................................................................................................

    /** Create a Delegating StoreHandler. */
    public DelegatingStoreHandler(@NotNull StoreHandler<T, K> delegate) {
        super(delegate.getDbTable(), delegate);
    }

    //~ Methods ......................................................................................................................................

    @Override public void delete(T instance) {
        getDelegate().delete(instance);
    }

    @Override public void delete(Iterable<K> keys) {
        getDelegate().delete(keys);
    }

    @Override public void deleteWhere(Criteria... condition) {
        getDelegate().deleteWhere(condition);
    }

    @Override public T find(K key) {
        return getDelegate().find(key);
    }

    @Override public T findPersisted(K key) {
        return getDelegate().findPersisted(key);
    }

    @Override public int insert(List<SetClause<?>> setClauses) {
        return getDelegate().insert(setClauses);
    }

    @Override public void insert(T instance, boolean generateKey) {
        getDelegate().insert(instance, generateKey);
    }

    @Override public int insertOrUpdate(List<SetClause<?>> insertSetClauses, TableField<?>[] keyColumns, List<SetClause<?>> setClauses,
                                        Criteria[] criteria) {
        return getDelegate().insertOrUpdate(insertSetClauses, keyColumns, setClauses, criteria);
    }

    @Override public ImmutableList<T> list(Iterable<K> keys) {
        return getDelegate().list(keys);
    }

    @Override public void merge(@NotNull T instance) {
        getDelegate().merge(instance);
    }

    @Override public <E> Select.Handler<E> select(final Select<E> select) {
        return getDelegate().select(select);
    }

    @Override public void update(T instance) {
        getDelegate().update(instance);
    }

    @Override public int update(final List<SetClause<?>> setClauses, final Criteria[] criteria) {
        return getDelegate().update(setClauses, criteria);
    }
    @Override public void updateLocking(T instance) {
        getDelegate().updateLocking(instance);
    }

    @Override protected T cache(final T instance, boolean cached) {
        return getDelegate().cache(instance, cached);
    }

    @NotNull protected StoreHandler<T, K> getDelegate() {
        return ensureNotNull(getParent());
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A delegating Store handler that can store a different delegate by thread.
     */
    public static class ByThread<T extends EntityInstance<T, K>, K> extends DelegatingStoreHandler<T, K> {
        private StoreHandler<T, K>                    defaultDelegate;
        private final ThreadLocal<StoreHandler<T, K>> delegate;

        /** Create a by thread Delegating StoreHandler. */
        public ByThread(@NotNull StoreHandler<T, K> parent) {
            super(parent);
            defaultDelegate = parent;
            delegate        = ThreadLocal.withInitial(() -> defaultDelegate);
        }

        /** Reset the delegate to the default value. */
        public void resetDelegate() {
            delegate.remove();
        }

        /** Set the default delegate. */
        public void setDefaultDelegate(@NotNull StoreHandler<T, K> delegate) {
            defaultDelegate = delegate;
        }

        /** Set a delegate for the current thread. */
        public void setDelegate(StoreHandler<T, K> delegate) {
            this.delegate.set(delegate);
        }

        @NotNull @Override protected StoreHandler<T, K> getDelegate() {
            return delegate.get();
        }
    }
}  // end class DelegatingStoreHandler
