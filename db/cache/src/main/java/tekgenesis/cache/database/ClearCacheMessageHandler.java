
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.MessageHandler;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * Clear Cache.
 */
public class ClearCacheMessageHandler implements MessageHandler<String> {

    //~ Methods ......................................................................................................................................

    @Override public void handle(String cacheName) {
        if (isNotEmpty(cacheName)) {
            logger.info("Clear cache for " + cacheName);
            final InfinispanCacheManager cacheManager = Context.getSingleton(InfinispanCacheManager.class);
            cacheManager.getCache(cacheName).clear();
        }
        else logger.warning("Trying to clear a cache without specifying name");
    }

    @Override public Short getScope() {
        return MessageHandler.CLEAR_CACHE_SCOPE;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(RebuildCacheMessageHandler.class);
}
