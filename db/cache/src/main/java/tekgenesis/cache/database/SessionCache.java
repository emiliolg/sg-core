
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database;

import tekgenesis.common.util.LruCache;
import tekgenesis.database.support.DummyConnection;

import static tekgenesis.common.Predefined.cast;

/**
 * Session Cache.
 */
public class SessionCache extends DummyConnection {

    //~ Instance Fields ..............................................................................................................................

    private final LruCache<SessionCacheFactory.Key<?>, ?> cache;

    //~ Constructors .................................................................................................................................

    SessionCache(int maxSize) {
        cache = LruCache.createLruCache(maxSize);
    }

    //~ Methods ......................................................................................................................................

    @Override public void close() {
        cache.evictAll();
    }

    @Override public void commit() {
        close();
    }

    @Override public void rollback() {
        close();
    }

    <K, T> LruCache<SessionCacheFactory.Key<K>, T> getCache() {
        return cast(cache);
    }

    String getStats() {
        return cache.stats();
    }
}
