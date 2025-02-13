
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.test.database;

import org.infinispan.Cache;

import tekgenesis.cache.database.infinispan.CacheManagerConfig.NoCluster;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.CounterHandlerFactory;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.persistence.StoreHandler;

/**
 * Rule for running Cache tests.
 */
public abstract class CacheRule extends DbRule {

    //~ Instance Fields ..............................................................................................................................

    CounterHandlerFactory.Counter counters = null;

    private InfinispanCacheManager cacheManager = null;

    //~ Methods ......................................................................................................................................

    /** Invalidate the specified map. */
    public void invalidateMap(final String mapName) {
        cacheManager.invalidateCache(mapName);
    }

    /** Returns a map with the specified name. */
    public Cache<Object, Object> getMap(final String mapName) {
        return cacheManager.getCache(mapName);
    }

    @Override protected void after() {
        counters.reset();
        super.after();
        if (cacheManager != null) cacheManager.close();
    }

    @Override protected StoreHandler.Factory createStoreHandlerFactory() {
        // final JGroupClusterManager clusterManager = new JGroupClusterManager();
        // getContext().setSingleton(ClusterManager.class, clusterManager);
        // final ModelRepositoryLoader loader = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
        // getContext().setSingleton(ModelRepository.class, loader.build());
        final CounterHandlerFactory sf = new CounterHandlerFactory(dbFactory);
        counters     = sf.getCounters();
        cacheManager = new InfinispanCacheManager(database.getName(), env, getTransactionManager(), sf, new LocalClusterManager(), new NoCluster());
        Context.getContext().setSingleton(InfinispanCacheManager.class, cacheManager);
        return cacheManager;
    }

    protected void initializeCache(final String dbName) {
        createDatabase(dbName);
    }
}  // end class CacheRule
