
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.util.ArrayList;
import java.util.List;

import org.infinispan.Cache;
import org.infinispan.commons.util.CloseableIteratorCollection;
import org.infinispan.configuration.cache.CacheMode;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.cluster.MessageHandler.CLEAR_CACHE_SCOPE;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.env.context.Context.getContext;

/**
 * Cache.
 */
public class Caches implements CachesMBean {

    //~ Methods ......................................................................................................................................

    @Override public void clearCache(String cacheName) {
        try {
            Context.getSingleton(ClusterManager.class).sendMessage(CLEAR_CACHE_SCOPE, cacheName);
        }
        catch (final Exception e) {
            logger.error(e);
        }
    }

    @Override public List<Tuple<String, String>> getEntityCaches() {
        final Seq<String>                 entitiesNames = getEntitiesNames();
        final List<Tuple<String, String>> caches        = getDefinedCaches();

        return filter(caches, tuple -> tuple != null && entitiesNames.contains(tuple.first())).toList();
    }

    @Override public List<Tuple<String, String>> getUserCaches() {
        final List<Tuple<String, String>> caches        = getDefinedCaches();
        final Seq<String>                 entitiesNames = getEntitiesNames();

        return filter(caches, tuple -> tuple != null && !entitiesNames.contains(tuple.first())).toList();
    }

    @Nullable @Override public <K, V> V getValue(String cacheName, K key) {
        final InfinispanCacheManager cacheManager = Context.getContext().getSingleton(InfinispanCacheManager.class);

        final Cache<K, V> cache = cacheManager.getCache(cacheName);
        return cache != null ? cache.get(key) : null;
    }

    @Override public <V> List<V> getValues(String cacheName) {
        final InfinispanCacheManager cacheManager = Context.getContext().getSingleton(InfinispanCacheManager.class);

        final Cache<?, V>                    cache  = cacheManager.getCache(cacheName);
        final CloseableIteratorCollection<V> values = cache.values();
        return Colls.toList(values);
    }

    private List<Tuple<String, String>> getDefinedCaches() {
        final InfinispanCacheManager cacheManager = Context.getContext().getSingleton(InfinispanCacheManager.class);
        final Seq<String>            cacheNames   = Colls.seq(cacheManager.getCacheNames());

        final List<Tuple<String, String>> caches = new ArrayList<>();

        for (final String cacheName : cacheNames) {
            final CacheMode cacheMode = cacheManager.getCache(cacheName).getCacheConfiguration().clustering().cacheMode();
            caches.add(Tuple.tuple(cacheName, cacheMode.name()));
        }
        return caches;
    }

    private Seq<String> getEntitiesNames() {
        final ModelRepository modelRepo = getContext().getSingleton(ModelRepository.class);

        return modelRepo.getModels()
               .filter(DbObject.class, e -> e != null && (e.isEntity() || (e.isView() && e.asView().isRemote())))
               .map(DbObject::getFullName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4533409930794130332L;

    private static final Logger logger = Logger.getLogger(Caches.class);
}  // end class Caches
