
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.serializer;

import java.util.concurrent.ConcurrentMap;

import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.cache.database.infinispan.CacheManagerConfig.NoCluster;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.StoreHandler;
import tekgenesis.transaction.JDBCTransactionManager;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.tools.test.StatementBuilder.create;

/**
 * Infinispan plain rule.
 */
@SuppressWarnings("WeakerAccess")  // Test rule.
public class ISRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    private InfinispanCacheManager  cacheManager = null;
    private final MemoryEnvironment env;

    private final JDBCTransactionManager tm;

    //~ Constructors .................................................................................................................................

    /** Create default rule. */
    public ISRule() {
        env = new MemoryEnvironment();
        tm  = new JDBCTransactionManager();
    }

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement statement, Description description) {
        return create(statement, this::after, this::before);
    }

    /** Invalidate the specified map. */
    public void invalidateMap(final String mapName) {
        cacheManager.invalidateCache(mapName);
    }

    /** Returns a map with the specified name. */
    public Cache<Object, Object> getMap(final String mapName) {
        return cacheManager.getCache(mapName);
    }

    /** Returns a user map with the specified name. */
    public ConcurrentMap<Object, Object> getUserMap(final String mapName) {
        return cacheManager.getCache(mapName);
    }

    protected void after() {
        if (cacheManager != null) cacheManager.close();
        env.dispose();
    }

    protected void before() {
        cacheManager = initialize();
    }

    @NotNull protected InfinispanCacheManager initialize() {
        return new InfinispanCacheManager("manager", env, tm, new StubFactory(), new LocalClusterManager(), new NoCluster());
    }

    //~ Inner Classes ................................................................................................................................

    static class StubFactory implements StoreHandler.Factory {
        @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
            throw unreachable();
        }
    }
}  // end class ISRule
