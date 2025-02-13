
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.util.List;

import org.infinispan.Cache;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.index.IndexManager;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.properties.SchemaProps;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ModelType;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Entities.
 */
public class Entities implements EntitiesMBean {

    //~ Methods ......................................................................................................................................

    @Override public void clearCache(String entityFqn) {
        try {
            final InfinispanCacheManager cacheManager = Context.getSingleton(InfinispanCacheManager.class);
            final Cache<Object, Object>  map          = cacheManager.getCache(entityFqn);
            if (map == null) throw new IllegalStateException(format("Map '%s'not found", entityFqn));
            // final CacheConfig cacheConfig = cacheManager.getCacheConfig(entityFqn);
            // if (cacheConfig != null && cacheConfig.cacheMode == CacheMode.LOCAL)
            // Context.getSingleton(ClusterManager.class).sendMessage(MessageHandler.CLEAR_CACHE_SCOPE, entityFqn);
            cacheManager.invalidateCache(entityFqn);
        }
        catch (final Exception e) {
            logger.error(e);
        }
    }

    @Override public void rebuildIndex(String entityFqn) {
        runInTransaction(() -> {
            try {
                final IndexManager indexManager = Context.getSingleton(IndexManager.class);
                indexManager.rebuildIndex(entityFqn);
            }
            catch (final Exception e) {
                logger.error(e);
            }
        });
    }

    public List<String> getEntities() {
        final ModelRepository modelRepo = getContext().getSingleton(ModelRepository.class);
        return modelRepo.getModels().filter(DbObject.class, this::isMaterialized).map(ModelType::getFullName).toList();
    }

    private boolean isMaterialized(@Nullable final DbObject dbObject) {
        if (dbObject == null) return false;
        final SchemaProps schemaProps = Context.getProperties(dbObject.getSchema(), SchemaProps.class);
        return isEmpty(schemaProps.remoteUrl) && (dbObject.isEntity() || (dbObject.isView() && dbObject.asView().isRemote()));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6011548418453368776L;

    private static final Logger logger = Logger.getLogger(Entities.class);
}  // end class Entities
