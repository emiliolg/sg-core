
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
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.searchbox.client.JestResult;

import org.elasticsearch.index.query.QueryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.database.Database;
import tekgenesis.database.Databases;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.exception.UniqueViolationException;
import tekgenesis.es.ElasticSearch;
import tekgenesis.es.IndexProperties;
import tekgenesis.persistence.*;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.common.core.DateTime.fromMilliseconds;
import static tekgenesis.common.core.UUID.generateUUIDString;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.database.DbConstants.SCHEMA_SG;
import static tekgenesis.es.ElasticSearch.indexName;
import static tekgenesis.index.IndexConstants.LOCAL_NODE_NAME;
import static tekgenesis.index.Mappings.createMapping;
import static tekgenesis.index.Mappings.entityJson;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.TableMetadata.searchableEntities;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Handle elastic search creation and operations.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class IndexManager {

    //~ Instance Fields ..............................................................................................................................

    private boolean embedded = false;

    private ElasticSearch               es        = null;
    private final IndexProperties       props;
    private ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(POOL_SIZE);

    //~ Constructors .................................................................................................................................

    /** Index manager constructor. */
    public IndexManager(IndexProperties props) {
        this.props = props;
    }

    //~ Methods ......................................................................................................................................

    /** Create indices for all given entity tables. */
    public void createIndicesForEntities(Seq<IndexSearcher> entities) {
        entities.forEach(this::createIndex);
    }

    /** Delete all deprecated indexes from entity fqn. */
    public void deleteDeprecated(ImmutableList<IndexSearcher> searchers) {
        searchers.filter(searcher -> getIndexMetadata(searcher, false).isDeprecated()).forEach(searcher ->
                deleteIndex(indexBehindAlias(searcher.getAlias())));
    }

    /** Create embedded instance of elastic search in run dir. */
    public void embeddedElasticSearch(String runDir) {
        checkRunDir(runDir, false);
        es       = ElasticSearch.createEmbeddedServer(new File(runDir, "es"), props.port);
        embedded = true;
    }

    /** Index or update all documents of each table from last indexUpdateTime. */
    public void indexEntities(Seq<IndexSearcher> entities) {
        entities.forEach(searcher -> scheduler.submit(() -> indexEntity(searcher)));
    }

    /** Create jest instance of elastic search. */
    public void jestElasticSearch() {
        es       = ElasticSearch.jestRemoteServer(props.url);
        embedded = false;
    }

    /** Rebuild all searchable entities. */
    public void rebuildAll() {
        rebuildAll(false);
    }

    /** Rebuild all searchable entities. */
    public void rebuildAll(boolean force) {
        searchableEntities(getEnvironment()).forEach(fqn -> scheduler.submit(() -> rebuildIndex(fqn, force)));
    }

    /** Rebuild index. */
    public void rebuildIndex(String entityFqn) {
        rebuildIndex(entityFqn, false);
    }

    /** Rebuild index. */
    public void rebuildIndex(@NotNull final IndexSearcher searcher) {
        rebuildIndex(searcher.getEntityFqn());
    }

    /** Rebuild index. */
    public void rebuildIndex(String entityFqn, boolean force) {
        try {
            final Option<IndexSearcher> searcherOption = TableMetadata.forName(entityFqn).getIndexSearcher();

            if (searcherOption.isEmpty()) {
                logger.warning("Entity with fqn " + entityFqn + " is not searchable.");
                return;
            }

            final IndexSearcher searcher = searcherOption.get();

            if (es.isRebuilding(searcher.getAlias())) {
                if (force) deleteIndex(indexBehindAlias(searcher.getAlias(true)));
                else logger.warning("Index for entity '" + entityFqn + "' is already being rebuilt");
            }
            else rebuild(searcher);
        }
        catch (final Exception e) {
            logger.error("Error rebuilding index for entity " + entityFqn, e);
        }
    }

    /** Force refresh in indices. */
    public void refresh() {
        es.refresh();
    }

    /** Search! */
    public io.searchbox.core.SearchResult search(String indexName, QueryBuilder query, int limit) {
        return es.search(indexName, query, limit);
    }

    /** Close client connection with elastic search server. */
    public void shutdown() {
        scheduler.shutdownNow();
        es.close();
    }

    /** Wait for all threads that are building indexes to finish and return. */
    public void waitForIndexingThreads()
        throws InterruptedException
    {
        scheduler.shutdown();
        scheduler.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        scheduler = new ScheduledThreadPoolExecutor(POOL_SIZE);
    }

    /** Delete all deprecated indexes from entity fqn. */
    public double getIndexCount(IndexSearcher searcher) {
        return es.getIndexCount(searcher.getAlias());
    }

    boolean aliasExists(String alias) {
        return es.aliasExists(alias);
    }

    /**
     * Creates index as 'entityFqn_UUID' and aliased as 'entityFqn_mappingID' or
     * 'entityFqn_mappingID_rebuild' (if is a rebuild creation). Reindex index creation can only be
     * triggered when no other reindex is running.
     */
    @Nullable String createIndex(@NotNull final IndexSearcher searcher, boolean isRebuild) {
        final String entityFqn = searcher.getEntityFqn();
        final String indexName = searcher.getPrefix() + indexName(entityFqn) + "_" + generateUUIDString();

        final String alias = searcher.getAlias(isRebuild);

        if (aliasExists(alias)) return null;

        final boolean mustLock = !embedded;
        if (mustLock && !tryIndexLock(searcher, isRebuild)) return null;

        try {
            logger.info("Creating index for entity: " + searcher.getEntityFqn() + " (" + indexName + ")");
            if (!isRebuild) deprecateCurrentIndexMappings(searcher);

            es.createIndex(indexName, entityFqn, createMapping(searcher, entityFqn, alias, embedded), alias);

            return indexName;
        }
        catch (final IOException e) {
            logger.error("Error creating index for entity " + entityFqn);
            return null;
        }
        finally {
            if (mustLock) releaseIndexLock(searcher, isRebuild);
        }
    }

    JestResult deleteIndex(String oldIndex) {
        return es.deleteIndex(oldIndex);
    }

    void deleteInstanceDocs(@NotNull List<EntityInstance<?, ?>> deletions) {
        final ElasticSearch.JestBulk bulk = es.bulk();
        for (final EntityInstance<?, ?> instance : deletions) {
            final String                entityFqn      = instance.getClass().getName();
            final Option<IndexSearcher> searcherOption = instance.metadata().getIndexSearcher();
            if (searcherOption.isEmpty())
                logger.warning("Could not delete instance " + instance.keyAsString() + " from entity with fqn " + entityFqn);
            else es.deleteDocument(bulk, searcherOption.get().getAlias(), entityFqn, instance.keyAsString());
            es.checkBulk(bulk);
        }
        es.execute(bulk);
    }  // end method deleteInstanceDocs

    void deprecateCurrentIndexMappings(IndexSearcher searcher) {
        final String      entityFqn = searcher.getEntityFqn();
        final Set<String> indices   = es.indicesFromAlias(searcher.getPrefix() + indexName(entityFqn) + "-*");

        for (final String index : indices)
            updateMappingMetadata(index, entityFqn, getIndexMetadata(index, entityFqn).setDeprecated(true));
    }

    void emptyIndex(IndexSearcher searcher) {
        final List<SearchResult>     search = searcher.search(SearchableExpr.EMPTY_EXPR, 1000);
        final ElasticSearch.JestBulk bulk   = es.bulk();
        search.forEach(s -> es.deleteDocument(bulk, searcher.getAlias(), searcher.getEntityFqn(), s.getKey()));
        es.execute(bulk);
    }

    /** Get the only index name behind the alias. */
    @NotNull String indexBehindAlias(@NotNull final String alias) {
        final Set<String> indexNames = es.indicesFromAlias(alias);
        if (indexNames.size() != 1) logger.error("ElasticSearch alias '" + alias + "' should have only one index pointing to it!");
        return indexNames.iterator().next();
    }

    /** Index entire entity table from index's last update time. */
    void indexEntityTable(IndexSearcher searcher, boolean isRebuild) {
        final String entityFqn = searcher.getEntityFqn();
        logger.info("Indexing " + entityFqn + " (reIndex: " + isRebuild + ")");
        final long startTime = System.currentTimeMillis();

        try {
            final Long indexUpdateTime = getIndexMetadata(searcher, isRebuild).getUpdateTime();
            final int  count           = indexEntityTableSince(searcher, fromMilliseconds(indexUpdateTime), isRebuild, false);
            logger.info(
                "Index for entity " + entityFqn + " updated in " + (System.currentTimeMillis() - startTime) + " ms. Indexed " + count +
                " instances.");
        }
        catch (final Exception e) {
            logger.error("Error while indexing entity " + entityFqn, e);
        }
    }

    int indexEntityTableSince(IndexSearcher searcher, @NotNull DateTime since, boolean isRebuild, boolean lock) {
        if (lock && !tryIndexLock(searcher, isRebuild)) return 0;

        final AtomicInteger count = new AtomicInteger(0);

        try {
            runInTransaction(() -> {
                final String entityFqn = searcher.getEntityFqn();
                logger.info("Indexing instances for entity " + entityFqn);

                final DateTime               currentTime = Databases.openDefault().currentTime();
                final ElasticSearch.JestBulk bulk        = es.bulk();
                final DbTable<?, ?>          table       = DbTable.forName(searcher.getEntityFqn());

                final TableField.DTime updateTimeField = table.metadata().getUpdateTimeField();

                if (updateTimeField == null) return;

                final Select<EntityInstance<?, ?>> where = cast(
                        selectFrom(table).where(updateTimeField.ge(since).and(searcher.allow())).orderBy(updateTimeField));
                where.forEach(instance -> indexEntityInstance(bulk, instance, table, searcher, entityFqn, isRebuild, count.addAndGet(1)));

                es.execute(bulk);

                if (searcher.allow() != Criteria.TRUE) {
                    final Select<EntityInstance<?, ?>> notAllowed = cast(
                            selectFrom(table).where(updateTimeField.ge(since).and(searcher.allow().not())));
                    deleteInstanceDocs(notAllowed.list());
                }

                updateMappingMetadata(searcher, isRebuild, getIndexMetadata(searcher, isRebuild).setUpdateTime(currentTime.toMilliseconds()));
            });
        }
        catch (final Exception e) {
            logger.error("Exception thrown while indexing " + searcher.getAlias(isRebuild), e);
        }
        finally {
            if (lock) releaseIndexLock(searcher, isRebuild);
        }

        return count.get();
    }  // end method indexEntityTableSince

    void indexEntityTablesSince(@NotNull final Set<IndexSearcher> tables, final DateTime since) {
        fromIterable(tables).filter(s -> aliasExists(s.getAlias())).forEach(table -> indexEntityTableSince(table, since, false, false));
    }

    void releaseIndexLock(IndexSearcher searcher, boolean isRebuild) {
        final String alias = searcher.getAlias(isRebuild);

        runInTransaction(() -> {
            final Database db = Databases.openDefault();
            db.sqlStatement("delete from %s.INDEX_LOCK where INDEX_NAME = ?", SCHEMA_SG).onArgs(alias).execute();
            logger.info("Released lock on index " + alias);
        });
    }

    void switchIndexAlias(String alias, String oldIndex, String newIndex) {
        es.switchIndexAlias(alias, oldIndex, newIndex);
    }

    boolean tryIndexLock(IndexSearcher searcher, boolean isRebuild) {
        final String myNodeName = getContext().getSingleton(ClusterManager.class).getCurrentMember();

        if (embedded && LOCAL_NODE_NAME.equalsIgnoreCase(myNodeName)) return true;

        final String   indexName = searcher.getAlias(isRebuild);
        final Database db        = Databases.openDefault();
        try {
            runInTransaction(() -> {
                final SqlStatement insertMyLock = db.sqlStatement(INSERT_LOCK_STATEMENT, SCHEMA_SG);
                insertMyLock.onArgs(indexName, myNodeName).execute();
                logger.info(LOCKED_INDEX + indexName + " by node " + myNodeName);
            });
            return true;
        }
        catch (final UniqueViolationException ignored) {
            final String storedNodeName = getNodeLock(indexName, db);

            if (isNotEmpty(storedNodeName)) {
                if (storedNodeName.equals(myNodeName)) {
                    final String duplicateNode = format("Index for %s is already locked by node %s. This can't happen!",
                            searcher.getEntityFqn(),
                            myNodeName);
                    logger.error(duplicateNode);
                    throw new IllegalStateException(duplicateNode);
                }

                if (!getContext().getSingleton(ClusterManager.class).isAlive(storedNodeName)) return invokeInTransaction(() -> {
                    if (db.sqlStatement("update %s.INDEX_LOCK set UPDATING_NODE = ? where INDEX_NAME = ? and UPDATING_NODE = ?", SCHEMA_SG)
                          .onArgs(myNodeName, indexName, storedNodeName)
                          .executeDml() == 1)
                    {
                        logger.info("Taking lock for index " + indexName + " by node " + myNodeName + " from node " + storedNodeName);return true;}
                    return false;});
            }
            return false;
        }
    }

    /** Custom Bulk Processor. */
    void withCustomBulkListener(@NotNull final ElasticSearch.Listener listener) {
        es.withCustomBulkListener(listener);
    }

    @NotNull IndexMetadata getIndexMetadata(IndexSearcher searcher, boolean isRebuild) {
        return getIndexMetadata(searcher.getAlias(isRebuild), searcher.getEntityFqn());
    }

    @Nullable String getNodeLock(String indexName, Database db) {
        return invokeInTransaction(() -> {
            final SqlStatement nodeStatement = db.sqlStatement("select UPDATING_NODE from %s.INDEX_LOCK where INDEX_NAME = ?", SCHEMA_SG);

            final String storedNodeName = nodeStatement.onArgs(indexName).get(String.class);
            logger.info("Index with alias " + indexName + " is locked by node " + storedNodeName);
            return storedNodeName;
        });
    }

    /** Check if run dir must be removed. */
    private void checkRunDir(String runDir, boolean resetES) {
        final File esFile = new File(runDir, "es");
        if (esFile.exists() && resetES) Files.remove(esFile);
    }

    /** Creates an index. */
    @Nullable private String createIndex(IndexSearcher searcher) {
        return createIndex(searcher, false);
    }

    /** Trigger entity indexing from last update time until now. */
    private void indexEntity(IndexSearcher searcher) {
        final IndexMetadata metadata = getIndexMetadata(searcher, false);
        indexEntityTableSince(searcher, fromMilliseconds(metadata.getUpdateTime()), false, true);
    }

    /** Index an entity instance (adds to ES bulk request). */
    private void indexEntityInstance(ElasticSearch.JestBulk bulk, @NotNull EntityInstance<?, ?> instance, DbTable<?, ?> table, IndexSearcher searcher,
                                     String entityFqn, boolean isRebuild, int count) {
        try {
            es.prepareIndex(bulk, searcher.getAlias(isRebuild), entityFqn, instance.keyAsString(), entityJson(instance, searcher));
            if (es.checkBulk(bulk)) {
                logger.info("Indexed  " + count + " instances of " + entityFqn);
                final IndexMetadata metadata = getIndexMetadata(searcher, isRebuild).setUpdateTime(instance.getUpdateTime().toMilliseconds());
                updateMappingMetadata(searcher, isRebuild, metadata);
            }
        }
        catch (final Exception e) {
            // catch general exception so that the iteration over instances is not interrupted.
            logger.error("Error indexing entity instance " + instance.keyAsString() + " from entity " + entityFqn, e);
        }
    }  // end method indexEntityInstance

    private void rebuild(@NotNull final IndexSearcher searcher) {
        final String newIndex = createIndex(searcher, true);

        if (newIndex != null) {
            indexEntityTable(searcher, true);
            final String alias    = searcher.getAlias();
            final String oldIndex = indexBehindAlias(alias);

            switchIndexAlias(alias, oldIndex, newIndex);
            deleteIndex(oldIndex);
        }
        else logger.error("Already rebuilding index for entity " + searcher.getEntityFqn());

        indexEntityTable(searcher, false);
    }

    private void updateMappingMetadata(IndexSearcher searcher, boolean isRebuild, IndexMetadata metadata) {
        updateMappingMetadata(searcher.getAlias(isRebuild), searcher.getEntityFqn(), metadata);
    }
    private void updateMappingMetadata(String alias, String entity, IndexMetadata metadata) {
        try {
            es.updateMapping(alias, entity, metadata.toJson());
        }
        catch (final IOException e) {
            logger.warning("Error updating index metadata.", e);
        }
    }

    @NotNull private IndexMetadata getIndexMetadata(String alias, String entityFqn) {
        return IndexMetadata.fromJson(es.getMappingMetadata(alias, entityFqn));
    }

    //~ Static Fields ................................................................................................................................

    static final String INSERT_LOCK_STATEMENT = "insert into %s.INDEX_LOCK values (?,?)";

    private static final String LOCKED_INDEX = "Locked index ";

    private static final int POOL_SIZE = 4;

    private static final Logger logger = getLogger(IndexManager.class);
}  // end class IndexManager
