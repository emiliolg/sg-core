
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import java.sql.SQLException;
import java.util.ServiceLoader;
import java.util.Set;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.SerializationConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheConnection;
import tekgenesis.cache.CacheManager;
import tekgenesis.cache.CacheProperties;
import tekgenesis.cache.CacheType;
import tekgenesis.cache.database.CacheSerializer;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.MessageHandler;
import tekgenesis.common.core.Times;
import tekgenesis.common.env.Environment;
import tekgenesis.persistence.*;
import tekgenesis.transaction.TransactionManager;

import static org.infinispan.configuration.cache.CacheMode.*;
import static org.infinispan.eviction.EvictionStrategy.LIRS;
import static org.infinispan.eviction.EvictionStrategy.NONE;

import static tekgenesis.cache.database.infinispan.Serializers.*;

/**
 * This class implements a CacheManager, used to cache Entities, the Session and other stuff.
 */
public class InfinispanCacheManager extends CacheManager implements StoreHandler.Factory, MessageHandler<String> {

    //~ Instance Fields ..............................................................................................................................

    private final CacheManagerConfig cacheConfig;

    private final EmbeddedCacheManager cacheManager;
    //
    private final Environment env;

    private final StoreHandler.Factory parentFactory;

    //~ Constructors .................................................................................................................................

    /** Create a CacheManager. */
    public InfinispanCacheManager(@NotNull String name, Environment env, @NotNull TransactionManager tm, @NotNull StoreHandler.Factory parentFactory,
                                  final ClusterManager<?> cluster, final CacheManagerConfig config) {
        super("IS:" + name.toUpperCase(), tm);
        this.env           = env;
        this.parentFactory = parentFactory;
        cacheConfig        = config;

        final GlobalConfiguration configuration = config.getConfiguration();

        configureSerializers(configuration);
        cacheManager = new DefaultCacheManager(configuration);
        cluster.registerMessageHandler(this);
        cacheManager.start();
    }

    //~ Methods ......................................................................................................................................

    /** Close cache manager. */
    public void close() {
        if (cacheManager.getStatus().allowInvocations()) cacheManager.stop();
    }

    /**
     * Configure the cache with the specified name. It will override the configuration based on the
     * properties read from the environment It returns false if the cache is disabled true otherwise
     */
    public void configureCache(final String cacheName, final CacheType cacheType) {
        final Configuration config =
            new ConfigurationBuilder()                                                                                        //
            .read(cacheManager.getDefaultCacheConfiguration())                                                                //
            .jmxStatistics().enable()                                                                                         //
            .clustering().cacheMode(getCacheMode(cacheType))                                                                  //
            .expiration().lifespan(cacheType.getLifespan()).maxIdle(cacheType.getMaxIdle())                                   //
            .eviction().maxEntries(cacheType.isFull() ? -1 : cacheType.getSize()).strategy(cacheType.isFull() ? NONE : LIRS)  //
            .build();

        cacheManager.defineConfiguration(cacheName, config);
    }

    @NotNull @Override public CacheConnection createConnection()
        throws SQLException
    {
        return new CacheConnection(this);
    }

    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        final StoreHandler<I, K> parent = getParentFactory().createHandler(storeType, dbTable);

        final TableMetadata<I, K> metadata = dbTable.metadata();
        if (!metadata.getCacheType().isDefined() || parent == null) return parent;

        final String entityName = metadata.getTypeName();

        final CacheType cacheType = overrideCacheProperties(entityName, metadata.getCacheType());
        if (!cacheType.isDefined()) return parent;

        configureCache(entityName, cacheType);

        final CacheStoreHandler<I, K> storeHandler = new IsStoreHandler<>(dbTable, parent, cacheType, this);
        handlers.put(entityName, storeHandler);
        return storeHandler;
    }

    /** Create the Sql Cache. */
    public Cache<String, Object> createSqlCache() {
        return getCache("sqlCache", CacheType.LOCAL.withLifespan(10 * Times.SECONDS_MINUTE));
    }

    @Override public void handle(String entityName) {
        invalidateCache(entityName);
    }

    /** Override the CacheType with the environment properties. */
    public CacheType overrideCacheProperties(final String cacheName, final CacheType cacheType) {
        return env.get(cacheName, CacheProperties.class).applyProperties(cacheType);
    }

    /** Return cache. */
    public <K, V> Cache<K, V> getCache(String mapName) {
        return cacheManager.getCache(mapName);
    }

    /** Returns a cache with the specified type. */
    public <K, V> Cache<K, V> getCache(String mapName, CacheType cacheType) {
        configureCache(mapName, cacheType);
        return cacheManager.getCache(mapName);
    }

    /** Return the list of defined caches.* */
    public Set<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    /** Get a Map from Infinispan. */
    public <K, T> Cache<K, T> getLocalMap(String mapName) {
        return cacheManager.getCache(mapName);
    }

    @NotNull @Override public StoreHandler.Factory getParentFactory() {
        return parentFactory;
    }

    @Override public Short getScope() {
        return MessageHandler.CACHE_SCOPE;
    }

    @Override protected void clearNamedCache(final String mapName) {
        getCache(mapName).clear();
    }

    private void configureSerializers(GlobalConfiguration configurationFile) {
        final SerializationConfiguration serialization = configurationFile.serialization();
        serialization.advancedExternalizers().put(DATE_ONLY_SERIALIZER_ID, DATE_ONLY_SERIALIZER);
        serialization.advancedExternalizers().put(DATE_TIME_SERIALIZER_ID, DATE_TIME_SERIALIZER);
        serialization.advancedExternalizers().put(RESOURCE_SERIALIZER_ID, RESOURCE_SERIALIZER);

        final ServiceLoader<CacheSerializer> serializers = ServiceLoader.load(CacheSerializer.class);
        for (final CacheSerializer serializer : serializers)
            serialization.advancedExternalizers().put(serializer.getId(), serializer.getExternalizer());
    }

    private CacheMode getCacheMode(final CacheType cacheType) {
        if (isLocal() || cacheType.isLocal()) return LOCAL;
        if (cacheType.isReplicated()) return cacheType.isSync() ? REPL_SYNC : REPL_ASYNC;
        if (cacheType.isFull()) return REPL_ASYNC;
        return INVALIDATION_ASYNC;
    }

    /** Reload all entries. */
    private boolean isLocal() {
        return cacheConfig.isLocal();
    }
}  // end class InfinispanCacheManager
