
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.CacheManager;
import tekgenesis.cache.CacheType;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.CachedEntityInstanceImpl.AbstractData;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.persistence.CachedEntityInstanceImpl.dataField;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * A Cached store handler.
 */
public abstract class CacheStoreHandler<T extends EntityInstance<T, K>, K> extends StoreHandler<T, K> {

    //~ Instance Fields ..............................................................................................................................

    private final CacheManager cacheManager;

    private final CacheType                  cacheType;
    private final ThreadLocal<EvictedSet<K>> toBeEvicted = ThreadLocal.withInitial(EvictedSet::new);

    //~ Constructors .................................................................................................................................

    protected CacheStoreHandler(DbTable<T, K> dbTable, final StoreHandler<T, K> parent, CacheType cacheType, CacheManager cacheManager) {
        super(dbTable, parent);
        this.cacheType    = cacheType;
        this.cacheManager = cacheManager;
    }

    //~ Methods ......................................................................................................................................

    /** Clean entries mark for eviction. */
    public void cleanEviction() {
        toBeEvicted.remove();
    }

    @Override public void delete(T instance) {
        cacheManager.ensureTransactionStarted();
        getParent().delete(instance);
        markForEviction(instance.keyObject());
    }

    @Override public void delete(Iterable<K> keys) {
        cacheManager.ensureTransactionStarted();
        getParent().delete(keys);
        keys.forEach(this::markForEviction);
    }

    @Override public void deleteWhere(Criteria... condition) {
        cacheManager.ensureTransactionStarted();
        getParent().deleteWhere(condition);
        toBeEvicted.get().all = true;
    }

    /** Evict all marked entries. */
    public void evictPending() {
        try {
            final EvictedSet<K> evicted = toBeEvicted.get();
            if (evicted.all) clearCache();
            else if (evicted.set != null) evicted.set.forEach(this::removeFromCache);
        }
        finally {
            cleanEviction();
        }
    }  // end method evictPending

    @Override public T find(K key) {
        if (toBeEvicted.get().isMarked(key)) return getParent().find(key);

        T t = getFromCache(key);
        if (t == null) {
            t = getParent().find(key);
            if (t != null) addToCache(key, t);
        }
        else t.invalidate();
        return t;
    }  // end method find

    // @Nullable @Override public T findByKey(final int keyId, final Object key) {
    // final T t = getFromCache(keyId, key);
    // if (t != null) return toBeEvicted.get().isMarked(t.keyObject()) ? getParent().findByKey(keyId, key) : t;
    //
    // final T t1 = getParent().findByKey(keyId, key);
    // if (t1 != null) addToCache(t1.keyObject(), t1);
    // return t1;
    // }

    @Override public void insert(T instance, boolean generateKey) {
        getParent().insert(instance, generateKey);
    }

    /** invalidate the cache. */
    public void invalidateCache() {
        clearCache();
        preload();
    }

    @Override public ImmutableList<T> list(Iterable<K> keys) {
        return immutable(keys).map(this::find).toList();
    }

    @Override public void merge(@NotNull T instance) {
        cacheManager.ensureTransactionStarted();
        getParent().merge(instance);
        markForEviction(instance.keyObject());
    }

    @Override public void update(T instance) {
        cacheManager.ensureTransactionStarted();
        getParent().update(instance);
        markForEviction(instance.keyObject());
    }

    @Override public int update(final List<SetClause<?>> setClauses, final Criteria[] criteria) {
        cacheManager.ensureTransactionStarted();
        final int n = getParent().update(setClauses, criteria);
        if (n != 0) toBeEvicted.get().all = true;
        return n;
    }
    @Override public void updateLocking(T instance) {
        cacheManager.ensureTransactionStarted();
        getParent().updateLocking(instance);
        markForEviction(instance.keyObject());
    }

    protected abstract void addToCache(K key, T instance);

    @Override protected T cache(final T instance, boolean cached) {
        final K key = instance.keyObject();
        final T t1  = cast(instance);
        if (cached) return putInCacheIfAbsent(key, dataField(t1));
        removeFromCache(key);
        return t1;
    }

    protected abstract void clearCache();
    protected abstract T putInCacheIfAbsent(K key, AbstractData<T, K> t1);
    protected abstract void removeFromCache(K k);
    @NotNull protected abstract Set<K> getCacheKeys();
    @Nullable protected abstract T getFromCache(K key);
    // @Nullable protected abstract T getFromCache(final int keyId, final Object key);

    private void markForEviction(@NotNull final K key) {
        toBeEvicted.get().mark(key);
    }

    private void preload() {
        if (!cacheType.preload()) return;

        logger.info("Pre loading entity " + getEntityName() + " in cache");
        final List<K> oldKeys = new ArrayList<>(getCacheKeys());

        selectFrom(getDbTable())  //
        .forEach(instance -> {
            final K key = instance.keyObject();
            addToCache(key, instance);
            oldKeys.remove(key);
        });
        oldKeys.forEach(this::removeFromCache);

        logger.info(format("Entity %s Loaded", getEntityName()));
    }

    //~ Static Fields ................................................................................................................................

    private static final int    MAX_EVICTION_SIZE = 100;
    private static final Logger logger            = Logger.getLogger(CacheStoreHandler.class);

    //~ Inner Classes ................................................................................................................................

    private static class EvictedSet<K> {
        boolean all = false;
        Set<K>  set = null;

        void mark(K k) {
            if (all) return;
            if (set == null) set = new HashSet<>(MAX_EVICTION_SIZE);
            set.add(k);
        }
        boolean isMarked(K k) {
            return all || set != null && set.contains(k);
        }
    }  // end class EvictedSet
}  // end class CacheStoreHandler
