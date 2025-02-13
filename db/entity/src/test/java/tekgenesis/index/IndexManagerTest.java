
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

import io.searchbox.core.BulkResult;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.es.ElasticSearch;
import tekgenesis.persistence.Sql;
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.test.basic.*;
import tekgenesis.transaction.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.collections.ImmutableList.of;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.database.DbConstants.SCHEMA_SG;
import static tekgenesis.es.ElasticSearch.MAX_BULK_SIZE;
import static tekgenesis.index.IndexManager.INSERT_LOCK_STATEMENT;
import static tekgenesis.index.SearchableExpr.EMPTY_EXPR;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.SimpleEntitySearcherBase.SIMPLE_ENTITY_SEARCHER;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;
import static tekgenesis.test.basic.g.AuthorSearcherBase.AUTHOR_SEARCHER;
import static tekgenesis.test.basic.g.AuthorTable.AUTHOR;
import static tekgenesis.test.basic.g.CategorySearcherBase.CATEGORY_SEARCHER;
import static tekgenesis.test.basic.g.PaymentTypeSearcherBase.PAYMENT_TYPE_SEARCHER;
import static tekgenesis.test.basic.g.ProductSearcherBase.PRODUCT_SEARCHER;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * IndexConsumerTest.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "MagicNumber", "SpellCheckingInspection" })
public class IndexManagerTest extends IndexSuite {

    //~ Methods ......................................................................................................................................

    @Test public void testAccent()
        throws InterruptedException, IOException
    {
        runInTransaction(() -> {
            final Category category = Category.create(1).insert();
            Product.create("Calefón2").setCategory(category).setModel("Calefón 2").insert();
            Product.create("Calefon").setCategory(category).setModel("Calefon 1").insert();
        });

        waitForProcessing(3);

        final Collection<SearchResult> r = PRODUCT_SEARCHER.search("Calefon 1");
        assertThat(r.size()).isEqualTo(1);
        assertThat(r.iterator().next().getKey()).isEqualTo("Calefon");

        final Collection<SearchResult> r1 = PRODUCT_SEARCHER.search("Calefon");
        assertThat(r1.size()).isEqualTo(2);

        final Collection<SearchResult> r2 = PRODUCT_SEARCHER.search("Calefón");
        assertThat(r2.size()).isEqualTo(1);
        assertThat(r2.iterator().next().getKey()).isEqualTo("Calefón2");
    }

    @Test public void testAliasExists() {
        assertThat(indexManager.aliasExists(PRODUCT_SEARCHER.getAlias())).isTrue();

        assertThat(indexManager.aliasExists(PAYMENT_TYPE_SEARCHER.getAlias())).isFalse();

        indexManager.createIndicesForEntities(of(PAYMENT_TYPE_SEARCHER));

        assertThat(indexManager.aliasExists(PAYMENT_TYPE_SEARCHER.getAlias())).isTrue();
    }

    @Test public void testBulkLimit()
        throws InterruptedException
    {
        final List<Integer> bulkSizes = new ArrayList<>();
        indexManager.withCustomBulkListener(new ElasticSearch.Listener() {
                @Override public void afterBulk(ElasticSearch.JestBulk request, BulkResult response) {
                    synchronized (this) {
                        if (response.isSucceeded()) {
                            final int size = response.getItems().size();
                            bulkSizes.add(size);
                            indexRequests += size;
                        }
                        else fail("Bulk request failed.");

                        notify();
                    }
                }
            });

        runInTransaction(() -> {
            for (int i = 0; i < 1003; i++)
                Category.create(i).setName("Category " + i).insert();
        });

        waitForProcessing(1003);

        assertThat(CATEGORY_SEARCHER.search(EMPTY_EXPR, 1500)).hasSize(1003);

        assertThat(bulkSizes).containsExactly(MAX_BULK_SIZE, MAX_BULK_SIZE, 3);
    }

    @Test public void testDelete()
        throws InterruptedException, IOException
    {
        runInTransaction(() -> SimpleEntity.create("Diego").setDescription("Perez").insert());
        final SimpleEntity lucas = invokeInTransaction(() -> SimpleEntity.create("Lucas").setDescription("Luppani").insert());
        final SimpleEntity laura = invokeInTransaction(() -> SimpleEntity.create("Laura").setDescription("Luppani").insert());

        waitForProcessing(3);

        runInTransaction(() -> {
            laura.delete();
            lucas.delete();
        });

        waitForProcessing(2);

        final List<SearchResult> all = SIMPLE_ENTITY_SEARCHER.getSuggestions();

        assertThat(all).hasSize(1);
        assertThat(all).extracting("key").containsExactly("Diego");

        runInTransaction(() -> Sql.deleteFrom(SIMPLE_ENTITY).where(SIMPLE_ENTITY.NAME.eq("Diego")).execute());

        waitForProcessing(1);

        final List<SearchResult> allEmpty = SIMPLE_ENTITY_SEARCHER.getSuggestions();
        assertThat(allEmpty).hasSize(0);
    }

    @Test public void testEmbeddedLocalLock() {
        final String myNodeName = getContext().getSingleton(ClusterManager.class).getCurrentMember();

        assertThat(myNodeName).isEqualToIgnoringCase(IndexConstants.LOCAL_NODE_NAME);

        assertThat(indexManager.tryIndexLock(PRODUCT_SEARCHER, false)).isTrue();
        final String indexName = PRODUCT_SEARCHER.getAlias();

        assertThat(indexManager.getNodeLock(indexName, db.getDatabase())).isNull();
    }

    @Test public void testEmpty()
        throws InterruptedException, IOException
    {
        runInTransaction(() -> SimpleEntity.create("Samsung").setDescription("Samsung").insert());

        waitForProcessing(1);

        final Collection<SearchResult> r = SIMPLE_ENTITY_SEARCHER.search("diego");
        assertThat(r).isEmpty();
    }

    @Test public void testForcedIndex()
        throws InterruptedException
    {
        final SimpleEntity ent = invokeInTransaction(() -> SimpleEntity.create("Samsung").setDescription("Samsung").insert());

        waitForProcessing(1);

        assertThat(SIMPLE_ENTITY_SEARCHER.getSuggestions()).hasSize(1);

        indexManager.emptyIndex(SIMPLE_ENTITY_SEARCHER);

        waitForProcessing(1);

        assertThat(SIMPLE_ENTITY_SEARCHER.getSuggestions()).isEmpty();

        runInTransaction(ent::forcePersist);

        waitForProcessing(1);

        assertThat(SIMPLE_ENTITY_SEARCHER.getSuggestions()).hasSize(1);
    }

    @Test public void testFuzzyLong()
        throws InterruptedException
    {
        runInTransaction(() -> SimpleEntity.create("Samsung").setDescription("Samsung").insert());

        waitForProcessing(1);

        final Collection<SearchResult> r = SIMPLE_ENTITY_SEARCHER.search("sansun", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r.size()).isEqualTo(1);
        final Collection<SearchResult> r2 = SIMPLE_ENTITY_SEARCHER.search("sam", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r2.size()).isEqualTo(1);
        final Collection<SearchResult> r3 = SIMPLE_ENTITY_SEARCHER.search("samsu", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r3.size()).isEqualTo(1);

        final Collection<SearchResult> r4 = SIMPLE_ENTITY_SEARCHER.search("sung", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r4.size()).isEqualTo(0);
    }

    @Test public void testFuzzyMiddle()
        throws InterruptedException, IOException
    {
        runInTransaction(() -> SimpleEntity.create("Sany").setDescription("Sany").insert());
        runInTransaction(() -> SimpleEntity.create("Sony").setDescription("Sony").insert());

        waitForProcessing(2);

        final Collection<SearchResult> r = SIMPLE_ENTITY_SEARCHER.search("sony", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r.size()).isEqualTo(2);
        final Iterator<SearchResult> iterator   = r.iterator();
        final SearchResult           sonyResult = iterator.next();
        assertThat(sonyResult.getKey()).isEqualTo("Sony");
        final SearchResult SanyoResult = iterator.next();
        assertThat(SanyoResult.getKey()).isEqualTo("Sany");
        assertThat(sonyResult.getScore()).isGreaterThan(SanyoResult.getScore());
    }

    @Test public void testHypen()
        throws InterruptedException, IOException
    {
        runInTransaction(() -> {
            final Category category = Category.create(1).insert();
            Product.create("Sony40").setCategory(category).setModel("1410").insert();
            Product.create("Sony402").setCategory(category).setModel("18-12").insert();
        });

        waitForProcessing(3);

        final Collection<SearchResult> r1 = PRODUCT_SEARCHER.search("Sony40 1410");
        assertThat(r1.size()).isEqualTo(1);
        assertThat(r1.iterator().next().getKey()).isEqualTo("Sony40");

        assertThat(PRODUCT_SEARCHER.search("Sony", EMPTY_EXPR, 10).size()).isEqualTo(2);

        final Collection<SearchResult> r2 = PRODUCT_SEARCHER.search("Sony40 18");
        assertThat(r2.size()).isEqualTo(1);
        assertThat(r2.iterator().next().getKey()).isEqualTo("Sony402");

        final Collection<SearchResult> r3 = PRODUCT_SEARCHER.search("Sony40 18-");
        assertThat(r3.size()).isEqualTo(1);
        assertThat(r3.iterator().next().getKey()).isEqualTo("Sony402");

        final Collection<SearchResult> r4 = PRODUCT_SEARCHER.search("Sony40 18-12");
        assertThat(r4.size()).isEqualTo(1);
        assertThat(r4.iterator().next().getKey()).isEqualTo("Sony402");
    }

    @Test public void testIndex()
        throws InterruptedException, IOException
    {
        final Author author = invokeInTransaction(() -> Author.create().setName("Julio").setLastName("Verne").insert());

        waitForProcessing(1);

        final List<SearchResult> juli = AUTHOR_SEARCHER.search("Juli");
        assertThat(juli.size()).isEqualTo(1);
        assertThat(getFromKey(juli, author.keyAsString()).getField(AUTHOR_SEARCHER.EXTRA)).isEqualTo("groso");
        assertThat(getFromKey(juli, author.keyAsString()).getScore()).isGreaterThanOrEqualTo(0.068041384);
        assertThat(AUTHOR_SEARCHER.search("uuuuuuuu").size()).isEqualTo(0);

        runInTransaction(() -> author.setName("Julio").setLastName("Cortazar").update());

        waitForProcessing(1);

        assertThat(AUTHOR_SEARCHER.search("esta es la").size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("uuuuuuuu").size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("Verne").size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("Julio").size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Julio", AUTHOR_SEARCHER.LAST_NAME.eq("Cortazar").must()).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Julio", AUTHOR_SEARCHER.LAST_NAME.eq("Verne").must()).size()).isEqualTo(0);

        runInTransaction(author::delete);

        waitForProcessing(1);

        assertThat(AUTHOR_SEARCHER.search("Julio").size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("Cortazar").size()).isEqualTo(0);

        runInTransaction(() -> {
            Author.create().setName("Julio").setLastName("Verne").setNickName(null).insert();
            Transaction.getCurrent().ifPresent(Transaction::rollback);
        });

        assertThat(AUTHOR_SEARCHER.search("Julio").size()).isEqualTo(0);
    }  // end method testIndex

    @Test public void testIndexCount()
        throws InterruptedException
    {
        assertThat(indexManager.getIndexCount(PRODUCT_SEARCHER)).isZero();

        runInTransaction(() -> {
            final Category c = Category.create(1).insert();

            Product.create("Prod").setCategory(c).setModel("Some product").insert();
            Product.create("Prod2").setCategory(c).setModel("Some other product").insert();
        });

        waitForProcessing(3);

        assertThat(indexManager.getIndexCount(PRODUCT_SEARCHER)).isEqualTo(2);
    }

    @Test public void testIndexDeprecation() {
        indexManager.deprecateCurrentIndexMappings(PRODUCT_SEARCHER);

        assertThat(indexManager.getIndexMetadata(PRODUCT_SEARCHER, false)).extracting("deprecated").containsExactly(true);

        indexManager.deleteDeprecated(of(PRODUCT_SEARCHER));

        assertThat(indexManager.aliasExists(PRODUCT_SEARCHER.getAlias())).isFalse();
    }

    @Test public void testIndexLock()
        throws InterruptedException
    {
        getContext().setSingleton(ClusterManager.class, new LocalClusterManager() {
                @Override public String getCurrentMember() {
                    return "MySuperRandomNodeName";
                }
            });

        final String myNodeName = getContext().getSingleton(ClusterManager.class).getCurrentMember();
        final String indexName  = PRODUCT_SEARCHER.getAlias();

        runInTransaction(() -> {
            final Category c = Category.create(1).insert();

            Product.create("Prod").setCategory(c).setModel("Alto producto").insert();
            Product.create("Prod2").setCategory(c).setModel("Otro alto producto").insert();
        });

        waitForProcessing(3);

        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(2);

        indexManager.emptyIndex(PRODUCT_SEARCHER);
        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(0);

        assertThat(indexManager.tryIndexLock(PRODUCT_SEARCHER, false)).isTrue();
        assertThat(indexManager.getNodeLock(indexName, db.getDatabase())).isEqualTo(myNodeName);

        try {
            indexManager.tryIndexLock(PRODUCT_SEARCHER, false);
            fail("This should throw an exception because index is already locked by me.");
        }
        catch (final IllegalStateException ignored) {}

        try {
            indexManager.indexEntityTableSince(PRODUCT_SEARCHER, DateTime.EPOCH, false, true);
        }
        catch (final IllegalStateException ignored) {  /* Don't index anything, exception is thrown because it's locked by me. */
        }

        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(0);

        assertThat(indexManager.indexEntityTableSince(PRODUCT_SEARCHER, DateTime.EPOCH, false, false)).isEqualTo(2);
        waitForProcessing(2);
        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(2);

        indexManager.releaseIndexLock(PRODUCT_SEARCHER, false);
        assertThat(indexManager.getNodeLock(indexName, db.getDatabase())).isNull();
    }  // end method testIndexLock

    @Test public void testIndexModes()
        throws InterruptedException, IOException
    {
        final Author author = invokeInTransaction(() -> {
                Author.create().setName("Julio").setLastName("Cortazar").insert();
                return Author.create().setName("Julio").setLastName("Verne").insert();
            });

        waitForProcessing(2);

        final List<SearchResult> juli = AUTHOR_SEARCHER.search("Juli");
        assertThat(juli.size()).isEqualTo(2);
        assertThat(getFromKey(juli, author.keyAsString()).getField(AUTHOR_SEARCHER.EXTRA)).isEqualTo("groso");
        assertThat(getFromKey(juli, author.keyAsString()).getScore()).isGreaterThanOrEqualTo(0.068041384);
        assertThat(AUTHOR_SEARCHER.search("uuuuuuuu").size()).isEqualTo(0);

        assertThat(AUTHOR_SEARCHER.search("Julio", AUTHOR_SEARCHER.LAST_NAME.eq("Verne").must(), QueryMode.PREFIX, 10).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Cortazar", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.PREFIX, 10).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Corta", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.PREFIX, 10).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Cortazar", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.STRICT, 10).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Corta", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.STRICT, 10).size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("Corlazar", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.PREFIX, 10).size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("Corlazar", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.FUZZY, 10).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("CorlazarMuyLargo", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.FUZZY, 10).size()).isEqualTo(0);
        assertThat(AUTHOR_SEARCHER.search("ortazar", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.CONTAINS, 10).size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("orlazar", AUTHOR_SEARCHER.NAME.eq("Julio").must(), QueryMode.CONTAINS, 10).size()).isEqualTo(0);
    }

    @Test public void testIndexRebuildEntity()
        throws InterruptedException
    {
        final Author author = invokeInTransaction(() -> Author.create().setName("Julio").setLastName("Verne").insert());

        waitForProcessing(1);

        assertThat(AUTHOR_SEARCHER.search("Juli").size()).isEqualTo(1);

        runInTransaction(() ->
                db.getDatabase()
                  .sqlStatement(
                      "insert into QName(BASIC_TEST, AUTHOR) (ID, NAME, LAST_NAME, NICK_NAME, UPDATE_TIME)" +
                      "values (10,'Diego','Rubinstein','diegor', CurrentTime)")
                  .execute());

        assertThat(invokeInTransaction(() -> selectFrom(AUTHOR).count())).isEqualTo(2);

        assertThat(AUTHOR_SEARCHER.search("Dieg").size()).isEqualTo(0);

        runInTransaction(() -> indexManager.rebuildIndex(author.getClass().getName()));

        waitForProcessing(2);

        assertThat(AUTHOR_SEARCHER.search("Dieg").size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("").size()).isEqualTo(2);
    }  // end method testIndexRebuildEntity

    @Test public void testIndexRebuildModificationsEntity()
        throws InterruptedException
    {
        final Author author = invokeInTransaction(() -> Author.create().setName("Julio").setLastName("Verne").insert());

        waitForProcessing(1);

        assertThat(AUTHOR_SEARCHER.search("Juli").size()).isEqualTo(1);

        runInTransaction(() ->
                db.getDatabase()
                  .sqlStatement(
                      "insert into QName(BASIC_TEST, AUTHOR) (ID, NAME, LAST_NAME, NICK_NAME, UPDATE_TIME)" +
                      "values (10,'Diego','Rubinstein','diegor', CurrentTime)")
                  .execute());

        assertThat(invokeInTransaction(() -> selectFrom(AUTHOR).count())).isEqualTo(2);

        assertThat(AUTHOR_SEARCHER.search("Dieg").size()).isEqualTo(0);

        runInTransaction(() -> indexManager.rebuildIndex(author.getClass().getName()));

        waitForProcessing(2);

        runInTransaction(() -> Author.create().setName("Luis").setLastName("Borges").insert());

        waitForProcessing(1);

        assertThat(AUTHOR_SEARCHER.search("Dieg").size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Luis").size()).isEqualTo(1);
        assertThat(AUTHOR_SEARCHER.search("Juli").size()).isEqualTo(1);
    }  // end method testIndexRebuildModificationsEntity

    @Test public void testMute()
        throws InterruptedException
    {
        ElasticSearchIndexer.mute();

        runInTransaction(() -> {
            Author.create().setName("Julio").setLastName("Cortazar").insert();
            Author.create().setName("Julio").setLastName("Verne").insert();
        });
        assertThat(AUTHOR_SEARCHER.getSuggestions()).hasSize(0);

        ElasticSearchIndexer.unMute();

        runInTransaction(() -> Author.create().setName("Julio").setLastName("Peña").insert());
        waitForProcessing(1);

        assertThat(AUTHOR_SEARCHER.getSuggestions()).hasSize(1);
    }

    @Test public void testParallel()
        throws InterruptedException, ExecutionException
    {
        runInTransaction(() -> Author.create().setName("Julio").setLastName("Verne").insert());

        waitForProcessing(1);

        final int             nThreads = 500;
        final ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        final List<Future<Stats>> futures = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {
            final MyActor a = new MyActor();
            futures.add(executor.submit(a));
        }

        while (!filter(futures, FUTURE_PREDICATE).isEmpty())
            Thread.sleep(1000);

        final Stats stats = new Stats();
        for (final Future<Stats> future : futures)
            stats.add(future.get());

        executor.shutdownNow();

        assertThat(stats.failed).isEqualTo(0);
    }

    @Test public void testRebuildSteps()
        throws InterruptedException
    {
        final Category category = invokeInTransaction(() -> {
                final Category c = Category.create(1).insert();

                Product.create("Prod").setCategory(c).setModel("Alto producto").insert();
                Product.create("Prod2").setCategory(c).setModel("Otro alto producto").insert();
                return c;
            });

        waitForProcessing(3);

        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(2);

        // step 1: create rebuild index from scratch.
        final String newIndex = indexManager.createIndex(PRODUCT_SEARCHER, true);
        assertThat(newIndex).isNotNull();

        assertThat(indexManager.createIndex(PRODUCT_SEARCHER, true)).isNull();
        assertThat(indexManager.createIndex(PRODUCT_SEARCHER, false)).isNull();

        assertThat(indexManager.aliasExists(PRODUCT_SEARCHER.getAlias(true))).isTrue();
        assertThat(indexManager.aliasExists(PRODUCT_SEARCHER.getAlias(false))).isTrue();

        final IndexMetadata metadata = indexManager.getIndexMetadata(PRODUCT_SEARCHER, true);
        assertThat(metadata).extracting("deprecated").containsExactly(false);
        assertThat(metadata).extracting("updateTime").containsExactly(0L);

        // step 2: index entity table from updateTime and update updateTime.
        indexManager.indexEntityTable(PRODUCT_SEARCHER, true);

        waitForProcessing(2);

        final IndexMetadata afterIndexMetadata = indexManager.getIndexMetadata(PRODUCT_SEARCHER, true);
        assertThat(afterIndexMetadata).extracting("deprecated").containsExactly(false);
        assertThat(afterIndexMetadata).extracting("updateTime").doesNotContain(0L);

        // step 3: Get old index and alias to make the switch to the new rebuilt index.
        final String alias    = PRODUCT_SEARCHER.getAlias();
        final String oldIndex = indexManager.indexBehindAlias(alias);

        assertThat(oldIndex).isNotEqualTo(alias);
        assertThat(oldIndex).isNotEqualTo(newIndex);

        // assert that index behind each alias is correct.
        assertThat(newIndex).isEqualTo(indexManager.indexBehindAlias(PRODUCT_SEARCHER.getAlias(true)));
        assertThat(oldIndex).isEqualTo(indexManager.indexBehindAlias(PRODUCT_SEARCHER.getAlias(false)));

        // step 3.5: let's say an entity is indexed while rebuilding to the rebuild specific alias.
        runInTransaction(() -> Product.create("Prod3").setCategory(category).setModel("Alto producto intermedio").insert());

        waitForProcessing(1);

        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(3);

        // step 4: switch alias to new rebuilt index and delete old index.
        indexManager.switchIndexAlias(alias, oldIndex, newIndex);

        indexManager.deleteIndex(oldIndex);

        assertThat(indexManager.aliasExists(oldIndex)).isFalse();
        assertThat(indexManager.aliasExists(PRODUCT_SEARCHER.getAlias(true))).isFalse();

        assertThat(indexManager.aliasExists(PRODUCT_SEARCHER.getAlias(false))).isTrue();
        assertThat(indexManager.aliasExists(newIndex)).isTrue();

        assertThat(indexManager.indexBehindAlias(PRODUCT_SEARCHER.getAlias(false))).isEqualTo(newIndex);

        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(2);

        // step 5: build index from last updateTime, so that it's up to date (we should catch the last indexed entity)
        indexManager.indexEntityTable(PRODUCT_SEARCHER, false);

        waitForProcessing(1);

        assertThat(PRODUCT_SEARCHER.getSuggestions()).hasSize(3);

        assertThat(indexManager.getIndexMetadata(PRODUCT_SEARCHER, false)).extracting("updateTime").isNotEqualTo(0L);
    }  // end method testRebuildSteps

    @Test public void testShort()
        throws InterruptedException, IOException
    {
        runInTransaction(() -> {
            SimpleEntity.create("Diego").setDescription("Perez").insert();
            SimpleEntity.create("Laura").setDescription("Luppani").insert();
            SimpleEntity.create("Lucas").setDescription("Luppani").insert();
        });

        waitForProcessing(3);

        final Collection<SearchResult> r = SIMPLE_ENTITY_SEARCHER.search("l", EMPTY_EXPR, QueryMode.PREFIX, 10);
        assertThat(r.size()).isEqualTo(2);
        final Collection<SearchResult> r2 = SIMPLE_ENTITY_SEARCHER.search("lu", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r2.size()).isEqualTo(2);
        final Iterator<SearchResult> iterator    = r2.iterator();
        final SearchResult           lucasResult = iterator.next();
        assertThat(lucasResult.getKey()).isEqualTo("Lucas");
        final SearchResult lauraResult = iterator.next();
        assertThat(lauraResult.getKey()).isEqualTo("Laura");
        assertThat(lucasResult.getScore()).isGreaterThan(lauraResult.getScore());
        final Collection<SearchResult> r4 = SIMPLE_ENTITY_SEARCHER.search("l", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r4.size()).isEqualTo(2);
        final Collection<SearchResult> r5 = SIMPLE_ENTITY_SEARCHER.search("luta", EMPTY_EXPR, QueryMode.PREFIX, 10);
        assertThat(r5.size()).isEqualTo(0);
        final Collection<SearchResult> r6 = SIMPLE_ENTITY_SEARCHER.search("luttani", EMPTY_EXPR, QueryMode.FUZZY, 10);
        assertThat(r6.size()).isEqualTo(2);
    }

    @Test public void testStealIndexLock() {
        final String otherNodeName = "SomeRandomNodeName";
        getContext().setSingleton(ClusterManager.class, new LocalClusterManager() {
                @Override public boolean isAlive(@NotNull String nodeName) {
                    return !nodeName.equals(otherNodeName);
                }
                @Override public String getCurrentMember() {
                    return "MySuperRandomNodeName";
                }
            });

        final String myNodeName = getContext().getSingleton(ClusterManager.class).getCurrentMember();
        final String indexName  = PRODUCT_SEARCHER.getAlias();

        runInTransaction(() -> {
            db.sqlStatement(INSERT_LOCK_STATEMENT, SCHEMA_SG).onArgs(indexName, otherNodeName).execute();
            db.sqlStatement(INSERT_LOCK_STATEMENT, SCHEMA_SG).onArgs(indexName + "2", otherNodeName).execute();
        });

        assertThat(indexManager.getNodeLock(indexName, db.getDatabase())).isEqualTo(otherNodeName);

        assertThat(indexManager.tryIndexLock(PRODUCT_SEARCHER, false)).isTrue();
        assertThat(indexManager.getNodeLock(indexName, db.getDatabase())).isEqualTo(myNodeName);

        indexManager.releaseIndexLock(PRODUCT_SEARCHER, false);
        assertThat(indexManager.getNodeLock(indexName, db.getDatabase())).isNull();
    }

    private SearchResult getFromKey(List<SearchResult> l, String keyAsString) {  //
        return filter(l, s -> s.getKey().equals(keyAsString)).getFirst().orElseThrow(() -> new RuntimeException("Not found!"));
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    private static final Predicate<Future<Stats>> FUTURE_PREDICATE = statsFuture -> statsFuture != null && !statsFuture.isDone();

    //~ Inner Classes ................................................................................................................................

    class MyActor implements Callable<Stats> {
        MyActor() {}

        @Override public Stats call()
            throws Exception
        {
            final Stats stats = new Stats();
            try {
                assertThat(AUTHOR_SEARCHER.search("Juli", EMPTY_EXPR, 10).size()).isEqualTo(1);
                assertThat(AUTHOR_SEARCHER.search("uuuuuuuu", EMPTY_EXPR, 10).size()).isEqualTo(0);
                stats.success();
            }
            catch (final Exception e) {
                stats.fail();
            }

            return stats;
        }
    }

    class Stats {
        int failed  = 0;
        int succeed = 0;

        public void add(Stats stats) {
            succeed += stats.succeed;
            failed  += stats.failed;
        }

        @Override public String toString() {
            return "Success: " + succeed + " Failed: " + failed;
        }
        void fail() {
            failed += 1;
        }

        void success() {
            succeed += 1;
        }
    }
}  // end class IndexManagerTest
