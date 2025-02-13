
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntriesEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.CacheType;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.*;
import tekgenesis.persistence.CachedEntityInstanceImpl.AbstractData;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.md.MdConstants.DATA_FIELD_NAME;

/**
 * Infinispan handler.
 */
class IsStoreHandler<T extends EntityInstance<T, K>, K> extends CacheStoreHandler<T, K> {

    //~ Instance Fields ..............................................................................................................................

    private AdvancedCache<K, AbstractData<T, K>> cache        = null;
    private final InfinispanCacheManager         cacheManager;

    //~ Constructors .................................................................................................................................

    /** Default constructor.* */
    IsStoreHandler(DbTable<T, K> dbTable, @NotNull StoreHandler<T, K> parent, CacheType cacheType, InfinispanCacheManager cacheManager) {
        super(dbTable, parent, cacheType, cacheManager);
        this.cacheManager = cacheManager;
        // secondaryMaps     = cast(new ConcurrentHashMap<?, ?>[et.getSecondaryKeys().size()]);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void addToCache(final K key, final T instance) {
        final AbstractData<T, K> data = getData(instance);

        if (data != null) getCache().putForExternalRead(key, data);
    }

    protected void clearCache() {
        getCache().clear();
    }

    // todo keep most modern one
    @Override protected T putInCacheIfAbsent(final K key, final AbstractData<T, K> t1) {
        final AbstractData<T, K> t = getCache().withFlags(Flag.CACHE_MODE_LOCAL, Flag.SKIP_REMOTE_LOOKUP).putIfAbsent(key, t1);

        final T                  instance = createInstance(key);
        final AbstractData<T, K> data     = notNull(t, t1);
        setData(instance, data);
        if (t == null) data.onLoad(instance);
        return instance;
    }

    @Override protected void removeFromCache(final K k) {
        getCache().withFlags(Flag.IGNORE_RETURN_VALUES, Flag.FORCE_ASYNCHRONOUS).remove(k);
    }

    protected AdvancedCache<K, AbstractData<T, K>> getCache() {
        return cache == null ? loadCache() : cache;
    }

    @NotNull protected Set<K> getCacheKeys() {
        return getCache().keySet();
    }

    protected T getFromCache(final K key) {
        final AbstractData<T, K> data = getCache().get(key);
        if (data == null) return null;
        final T instance = createInstance(key);
        setData(instance, data);
        return instance;
    }

    private T createInstance(K key) {
        final TableMetadata<T, K> md = getMetadata();
        return md.hasGeneratedKey() ? md.createInstance() : md.createInstance(key);
    }

    private synchronized AdvancedCache<K, AbstractData<T, K>> loadCache() {
        if (cache == null) {
            final AdvancedCache<K, AbstractData<T, K>> ac = cast(cacheManager.getCache(getEntityName()).getAdvancedCache());
            ac.addListener(new IsListener());
            cache = ac;
        }
        return cache;
    }

    @Nullable private AbstractData<T, K> getData(T instance) {
        try {
            final Method declaredMethod = instance.getClass().getSuperclass().getDeclaredMethod("data");
            declaredMethod.setAccessible(true);
            return cast(declaredMethod.invoke(instance));
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.debug(e);
        }
        return null;
    }

    private void setData(T instance, AbstractData<T, K> data) {
        setField(instance, data, DATA_FIELD_NAME);
    }

    private void setField(T instance, Object value, String dataFieldName) {
        try {
            final Field declaredField = instance.getClass().getSuperclass().getDeclaredField(dataFieldName);
            declaredField.setAccessible(true);
            declaredField.set(instance, value);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            logger.debug(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(IsStoreHandler.class);
    // @NotNull private final ConcurrentHashMap<Object, T>[] secondaryMaps;

    //~ Inner Classes ................................................................................................................................

    // private synchronized Map<Object, T> loadSecondaryMap(final int keyId) {
    // final ConcurrentHashMap<Object, T> secondaryMap = secondaryMaps[keyId];
    // if (secondaryMap != null) return secondaryMap;
    // final ConcurrentHashMap<Object, T> map = new ConcurrentHashMap<>();
    // for (final T t : getCache().values()) {
    // final Object key = getEntityTable().keyObject(keyId, t);
    // if (key != null) map.put(key, t);
    // }
    // secondaryMaps[keyId] = map;
    // return map;
    // }

    // @Nullable private Map<Object, T> getSecondaryKeyMap(final int keyId) {
    // final Map<Object, T> secondaryMap = secondaryMaps[keyId];
    // return secondaryMap != null ? secondaryMap : loadSecondaryMap(keyId);
    // }

    @Listener
    @SuppressWarnings("unused")
    private class IsListener {
        @CacheEntriesEvicted public void evict(CacheEntriesEvictedEvent<K, AbstractData<T, K>> event) {
            for (final AbstractData<T, K> instance : event.getEntries().values())
                remove(instance);
        }

        @CacheEntryModified public void put(CacheEntryModifiedEvent<K, AbstractData<T, K>> event) {
            if (event.isPre()) remove(event.getValue());
            else put(event.getValue());
        }

        @CacheEntryCreated public void put(CacheEntryCreatedEvent<K, AbstractData<T, K>> event) {
            if (!event.isPre()) put(event.getValue());
        }
        @CacheEntryRemoved public void remove(CacheEntryRemovedEvent<K, AbstractData<T, K>> event) {
            if (event.isPre()) remove(event.getValue());
        }

        private void put(final AbstractData<T, K> data) {
            setCached(data, true);
        }

        private void remove(final AbstractData<T, K> data) {
            if (data == null) return;
            setCached(data, false);
        }
    }  // end class IsListener
}  // end class IsStoreHandler
