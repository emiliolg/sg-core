
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Constants;
import tekgenesis.common.util.LruCache;
import tekgenesis.persistence.*;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.transaction.TransactionResource;

/**
 * Manages a Session Cache.
 */
public class SessionCacheFactory extends TransactionResource.Default<SessionCache> implements StoreHandler.Factory {

    //~ Instance Fields ..............................................................................................................................

    private final int                  maxSize;
    private final StoreHandler.Factory parentFactory;

    //~ Constructors .................................................................................................................................

    /** Create the factory. */
    public SessionCacheFactory(int cacheSize, TransactionManager tm, StoreHandler.Factory parent) {
        super(SessionCache.class.getName(), tm);
        maxSize       = cacheSize;
        parentFactory = parent;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public SessionCache createConnection() {
        return new SessionCache(maxSize);
    }

    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        final StoreHandler<I, K> parent = getParentFactory().createHandler(storeType, dbTable);
        // Disable SessionCache for Ideafix
        return parent == null ? null : storeType.isEmpty() ? new SessionStoreHandler<>(this, parent) : parent;
    }

    @NotNull @Override public StoreHandler.Factory getParentFactory() {
        return parentFactory;
    }

    /** Return cache statistics. */
    @NotNull public String getStats() {
        return getConnection(false).getStats();
    }

    //~ Inner Classes ................................................................................................................................

    static class Key<K> {
        private final K                         key;
        private final SessionStoreHandler<?, K> parent;

        public Key(SessionStoreHandler<?, K> parent, K key) {
            this.parent = parent;
            this.key    = key;
        }

        @Override public boolean equals(Object obj) {
            if (obj instanceof Key) {
                final Key<?> that = (Key<?>) obj;
                return that.parent == parent && key.equals(that.key);
            }
            return false;
        }

        @Override public int hashCode() {
            return Constants.HASH_SALT * parent.hashCode() + key.hashCode();
        }
    }

    /**
     * Implements a Simple Cache over the current Session.
     */
    private static class SessionStoreHandler<T extends EntityInstance<T, K>, K> extends DelegatingStoreHandler<T, K> {
        private final SessionCacheFactory sessionCacheFactory;

        /** Create a Delegating StoreHandler. */
        private SessionStoreHandler(SessionCacheFactory sessionCacheFactory, @NotNull StoreHandler<T, K> delegate) {
            super(delegate);
            this.sessionCacheFactory = sessionCacheFactory;
        }

        @Override public void delete(T instance) {
            super.delete(instance);
            getCache().remove(key(instance.keyObject()));
        }

        @Override public void deleteWhere(Criteria... condition) {
            super.deleteWhere(condition);
            getCache().evictAll();
        }

        @Override public T find(final K key) {
            return getCache().find(key(key), k -> super.find(k.key));
        }

        @Override public void insert(T instance, boolean generateKey) {
            super.insert(instance, generateKey);
            updateCache(instance);
        }

        @Override public ImmutableList<T> list(Iterable<K> keys) {
            final LruCache<Key<K>, T> cache = getCache();
            return ImmutableList.build(b -> {
                final List<K> misses = new ArrayList<>();
                for (final K key : keys) {
                    final T t = cache.get(key(key));
                    if (t != null) b.add(t);
                    else misses.add(key);
                }
                if (!misses.isEmpty()) {
                    for (final T t : super.list(misses)) {
                        b.add(t);
                        cache.put(key(t.keyObject()), t);
                    }
                }
            });
        }

        @Override public void update(T instance) {
            super.update(instance);
            updateCache(instance);
        }
        @Override public void updateLocking(T instance) {
            super.updateLocking(instance);
            updateCache(instance);
        }

        private Key<K> key(K key) {
            return new Key<>(this, key);
        }

        private void updateCache(T instance) {
            getCache().put(key(instance.keyObject()), instance);
        }

        private LruCache<Key<K>, T> getCache() {
            return sessionCacheFactory.getConnection(false).getCache();
        }
    }  // end class SessionStoreHandler
}  // end class SessionCacheFactory
