
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
import tekgenesis.common.core.QName;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;

import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Rebuild cache.
 */
public class RebuildCacheMessageHandler implements MessageHandler<String> {

    //~ Methods ......................................................................................................................................

    @Override public void handle(String fqn) {
        if (fqn == null) {
            logger.warning("Trying to rebuild a cache index without specify fqn");
            return;
        }
        runInTransaction(() -> {
            logger.info("Rebuild cache for " + fqn);
            final QName                  entityName   = QName.createQName(fqn);
            final InfinispanCacheManager cacheManager = Context.getSingleton(InfinispanCacheManager.class);

            if (entityName.isEmpty()) cacheManager.invalidateAllCaches();
            else cacheManager.invalidateCache(entityName.getFullName());
        });
    }

    @Override public Short getScope() {
        return MessageHandler.REBUILD_CACHE_SCOPE;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(RebuildCacheMessageHandler.class);
}
