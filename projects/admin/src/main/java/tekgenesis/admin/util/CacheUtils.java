
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.util;

import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.MessageHandler;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;

/**
 * Entity Cache utilities.
 */
public final class CacheUtils {

    //~ Constructors .................................................................................................................................

    private CacheUtils() {}

    //~ Methods ......................................................................................................................................

    /** Clear cache for entities. */
    public static void clearCache(@NotNull final String cacheName) {
        try {
            Context.getSingleton(ClusterManager.class).sendMessage(MessageHandler.CLEAR_CACHE_SCOPE, cacheName);
        }
        catch (final Exception e) {
            logger.error(e);
        }
    }

    /**
     * Remove instanceId from cache.
     *
     * @param  entityFqn  entities fqn
     */
    public static <T> void clearCache(@NotNull final String entityFqn, @NotNull final T instanceId) {
        final InfinispanCacheManager cacheManager = Context.getSingleton(InfinispanCacheManager.class);
        final Cache<Object, Object>  map          = cacheManager.getCache(entityFqn);
        if (map == null) throw new IllegalStateException(MAP_NOT_FOUND + entityFqn + "'");
        map.remove(instanceId);
    }

    /**
     * Clear cache for entities.
     *
     * @param  entityFqn  entities fqn
     */
    public static void clearEntityCache(@NotNull final String entityFqn) {
        try {
            final InfinispanCacheManager cacheManager = Context.getSingleton(InfinispanCacheManager.class);
            final Cache<Object, Object>  map          = cacheManager.getCache(entityFqn);
            if (map == null) throw new IllegalStateException(MAP_NOT_FOUND + entityFqn + "'");
            // final CacheConfig cacheConfig = cacheManager.getCacheConfig(entityFqn);
            // if (cacheConfig != null && cacheConfig.cacheMode == CacheMode.LOCAL)
            // Context.getSingleton(ClusterManager.class).sendMessage(MessageHandler.CLEAR_CACHE_SCOPE, entityFqn);
            cacheManager.invalidateCache(entityFqn);
        }
        catch (final Exception e) {
            logger.error(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String MAP_NOT_FOUND = "Map not found '";

    private static final Logger logger = Logger.getLogger(CacheUtils.class);
}  // end class CacheUtils
