
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.io.File;

import io.searchbox.core.BulkResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;
import org.junit.Rule;
import org.junit.runners.Parameterized;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.util.Files;
import tekgenesis.database.DatabaseFactory;
import tekgenesis.database.DatabaseType;
import tekgenesis.es.ElasticSearch;
import tekgenesis.es.IndexProperties;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.persistence.*;
import tekgenesis.persistence.sql.SqlStoreHandlerFactory;
import tekgenesis.repository.ModelRepository;
import tekgenesis.transaction.TransactionManager;

import static org.junit.Assert.fail;

import static tekgenesis.cache.database.infinispan.CacheManagerConfig.NoCluster;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.showcase.g.SimpleEntitySearcherBase.SIMPLE_ENTITY_SEARCHER;
import static tekgenesis.test.basic.g.AuthorSearcherBase.AUTHOR_SEARCHER;
import static tekgenesis.test.basic.g.CategorySearcherBase.CATEGORY_SEARCHER;
import static tekgenesis.test.basic.g.DeprecableEntitySearcherBase.DEPRECABLE_ENTITY_SEARCHER;
import static tekgenesis.test.basic.g.ProductSearcherBase.PRODUCT_SEARCHER;
import static tekgenesis.test.basic.g.SearchableTypesSearcherBase.SEARCHABLE_TYPES_SEARCHER;

/**
 * IndexSuite.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "AssignmentToStaticFieldFromInstanceMethod" })
public class IndexSuite {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName         = null;
    IndexManager                           indexManager   = null;
    int                                    indexRequests;
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private InfinispanCacheManager         cacheManager;
    private ClusterManager<Address>        clusterManager = null;
    private final File                     indexDir       = new File("target/db/index/test-run/indexes");

    @Rule public DbRule db = new DbRule(DbRule.SG,
            DbRule.BASIC_TEST,
            DbRule.TEST,
            DbRule.BASIC,
            DbRule.SHOWCASE,
            DbRule.AUTHORIZATION,
            DbRule.FORM,
            DbRule.VIEWS) {
            @Override protected void before() {
                Files.remove(indexDir);
                // noinspection ResultOfMethodCallIgnored
                indexDir.mkdirs();
                final ModelRepositoryLoader loader = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
                getContext().setSingleton(ModelRepository.class, loader.build());
                createDatabase(dbName);

                indexManager.createIndicesForEntities(ENTITIES);
                indexRequests = 0;
                indexManager.withCustomBulkListener(new BulkListener());
            }

            @Override protected void doCreateDatabase() {
                if (db.getDatabaseType() != DatabaseType.POSTGRES) super.doCreateDatabase();
                else {
                    final DatabaseType t = getDatabaseType();
                    try {
                        t.createDatabase(getDatabase());
                    }
                    catch (final Exception ignore) {}
                }
            }

            @Override protected void after() {
                if (cacheManager != null) cacheManager.close();
                if (clusterManager != null) clusterManager.stop();
                if (indexManager != null) indexManager.shutdown();
            }

            @Override protected void initTableFactory() {
                final StoreHandlerFactory factory = new StoreHandlerFactory(dbFactory);
                clusterManager = new LocalClusterManager();
                Context.getContext().setSingleton(ClusterManager.class, clusterManager);
                cacheManager = new InfinispanCacheManager(Constants.DEFAULT_SCOPE,
                        env,
                        getTransactionManager(),
                        factory,
                        clusterManager,
                        new NoCluster());
                startIndexManager();
                final ElasticSearchIndexer indexFactory = new ElasticSearchIndexer(database, cacheManager, indexManager);
                TableFactory.setFactory(new TableFactory(indexFactory));
                try {
                    clusterManager.start();
                }
                catch (final Exception e) {
                    fail(e.getMessage());
                }
                Context.getContext().setSingleton(TransactionManager.class, getTransactionManager());
                Context.getContext().setSingleton(IndexManager.class, indexManager);
            }
        };

    //~ Methods ......................................................................................................................................

    void waitForProcessing(int count)
        throws InterruptedException
    {
        synchronized (this) {
            while (indexRequests < count)
                wait();

            indexRequests = 0;
            indexManager.refresh();
        }
    }

    private void startIndexManager() {
        final IndexProperties props = IndexProperties.DEFAULT;
        props.port   = 9500;
        indexManager = new IndexManager(props);
        indexManager.embeddedElasticSearch(indexDir.getPath());
    }

    //~ Static Fields ................................................................................................................................

    private static final Seq<IndexSearcher> ENTITIES = listOf(DEPRECABLE_ENTITY_SEARCHER,
            PRODUCT_SEARCHER,
            SIMPLE_ENTITY_SEARCHER,
            AUTHOR_SEARCHER,
            CATEGORY_SEARCHER,
            SEARCHABLE_TYPES_SEARCHER);

    //~ Inner Classes ................................................................................................................................

    private class BulkListener implements ElasticSearch.Listener {
        @Override public void afterBulk(ElasticSearch.JestBulk request, BulkResult response) {
            synchronized (IndexSuite.this) {
                if (response.isSucceeded()) indexRequests += response.getItems().size();
                else fail("Bulk request failed.");

                IndexSuite.this.notify();
            }
        }

        @Override public void afterBulk(ElasticSearch.JestBulk request, Throwable failure) {
            fail(failure.getMessage());
        }
    }

    // Override to ignore errors caused by 'no-mm' classes when rebinding
    static class StoreHandlerFactory extends SqlStoreHandlerFactory {
        /**
         * Create a Factory.
         *
         * @param  databaseFactory
         */
        StoreHandlerFactory(@NotNull DatabaseFactory<?> databaseFactory) {
            super(databaseFactory);
        }

        @Nullable @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String        storeType,
                                                                                                        final DbTable<I, K> dbTable) {
            try {
                return super.createHandler(storeType, dbTable);
            }
            catch (final IllegalStateException e) {
                // ignore it
            }
            return null;
        }
    }
}  // end class IndexSuite
