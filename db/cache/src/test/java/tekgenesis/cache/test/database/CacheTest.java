
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.test.database;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.infinispan.Cache;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.resource.DbResourceHandler;
import tekgenesis.test.basic.*;
import tekgenesis.test.basic.g.CategoryTable;
import tekgenesis.transaction.Transaction;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.test.BasicTest.createCategory;
import static tekgenesis.test.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.test.basic.g.ProductTable.PRODUCT;
import static tekgenesis.test.basic.g.StoreTable.STORE;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class CacheTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public CacheRule cache = new CacheRule() {
            @Override protected void before() {
                initializeCache(dbName);
                rh = new DbResourceHandler(env, database);
            }
        };

    private DbResourceHandler rh = null;

    //~ Methods ......................................................................................................................................

    @Test public void testCacheDefaultKey() {
        // Create a Store;
        runInTransaction(() -> Store.create().setName("Compumundo").insert());

        assertThat(selectFrom(STORE).count()).isEqualTo(1);
        // Find it and update inners;
        runInTransaction(() -> {
            final Store s1 = Store.findOrFail(1);
            s1.getLocations().add().setAddress("Alem 123");
            s1.persist();
        });

        // Read and check the delete and check again;
        runInTransaction(() -> {
            assertNotNull(Store.find(1));
            assertNotNull(Store.find(1));
        });

        runInTransaction(() -> {
            final Store st1 = assertNotNull(Store.find(1));
            assertThat(st1.getLocations()).hasSize(1).extracting("address").containsExactly("Alem 123");
            st1.delete();
        });
        runInTransaction(() -> {
            assertThat(selectFrom(STORE).count()).isEqualTo(0);

            assertThat(cache.counters.deletes).isEqualTo(2);
            assertThat(cache.counters.inserts).isEqualTo(2);
            assertThat(cache.counters.finds).isEqualTo(2);
        });
    }

    @Test public void testCacheDefaultKeyInners() {
        final Store store = Store.create();
        store.setName("Compumundo");

        store.getLocations().merge(listOf("Alem 123"), Location::setAddress);

        store.persist();
        commit();

        final Store s1 = assertNotNull(Store.find(1));
        assertThat(s1.getLocations()).hasSize(1).extracting("address").containsExactly("Alem 123");

        s1.delete();
        commit();

        assertThat(selectFrom(STORE).exists()).isFalse();
    }
    @Test public void testCategories() {
        final Category c1 = createCategory(1, "Category-1", "Category-1 Description");
        final Category c2 = createCategory(2, "Category-2", "Category-2 Description");
        createCategory(3, null, "Category-3 Description");
        commit();

        assertNotNull(Category.find(1));
        assertNotNull(Category.find(1));
        assertNotNull(Category.find(2));
        assertNotNull(Category.find(2));
        assertNotNull(Category.find(3));

        // final Category cat1 = assertNotNull(Category.findByName("Category-1"));
        final Category cat1 = assertNotNull(Category.find(1));

        assertThat(cache.counters.finds).isEqualTo(3);
        cat1.setName("Cat1");

        assertNotNull(Category.findByName("Category-1"));
        cat1.update();

        assertThat(Category.findByName("Category-1")).isNull();

        assertThat(cache.counters.finds).isEqualTo(5);
        commit();

        assertThat(Category.findByName("Category-1")).isNull();
        // assertNotNull(Category.findByName("Cat1"));
        assertNotNull(Category.find(1));

        assertThat(cache.counters.finds).isEqualTo(7);

        // Full list;
        assertThat(selectFrom(CATEGORY).count()).isEqualTo(3);

        // list with limit;
        assertThat(selectFrom(CATEGORY).limit(1).list().toList()).containsExactly(c1);
        assertThat(selectFrom(CATEGORY).offset(1).limit(1).list().toList()).containsExactly(c2);

        // Select single value;

        final CategoryTable C = CATEGORY;
        final Category      c = assertNotNull(selectFrom(CATEGORY).where(C.DESCR.eq("Category-2 Description").and(C.NAME.eq("Category-2"))).get());
        assertThat(c.getIdKey()).isEqualTo(2);

        assertThat(Category.listWhere(C.DESCR.eq("Category-2 Description").or(C.NAME.eq("Cat1"))).count()).isEqualTo(2);
        assertThat(Category.listWhere(C.DESCR.eq("Category-2 Description").or(C.NAME.eq("Category-2"))).count()).isEqualTo(1);
        assertThat(Category.listWhere(C.DESCR.eq("Category-2 Description").or(C.NAME.eq("Cat1")).not()).count()).isEqualTo(0);

        final Category cat = assertNotNull(selectFrom(CATEGORY).where(C.DESCR.ne("Category-2 Description")).get());
        assertThat(cat.getIdKey()).isEqualTo(1);

        assertThat(Category.listWhere(C.DESCR.like("Category-%")).count()).isEqualTo(3);

        assertThat(Category.list(asList("1", "2")).toList().toStrings()).containsExactly("Category-1 Description", "Category-2 Description");

        assertNotNull(Category.find(2)).delete();
        commit();

        assertThat(selectFrom(CATEGORY).count()).isEqualTo(2);

        assertThat(cache.counters.deletes).isEqualTo(1);
        assertThat(cache.counters.inserts).isEqualTo(3);
        assertThat(cache.counters.updates).isEqualTo(1);
    }  // end method testCategories

    @Test public void testCategoriesInvalidate() {
        final Category c1 = createCategory(1, "Category-1", "Category-1 Description");
        commit();
        final Category category = Category.find(1);
        assert category != null;
        assertThat(category.getProducts().size()).isEqualTo(0);

        assertThat(c1.getProducts().size()).isEqualTo(0);
        createProduct("1", "RX21", "Tablet RX 21", State.ACTIVE, BigDecimal.valueOf(1000), c1);
        commit();
        final Category category2 = Category.find(1);
        assert category2 != null;
        assertThat(category2.getProducts().size()).isEqualTo(1);
    }

    @Test public void testInvalidate() {
        createCategory(1, "Category-1", "Category-1 Description");
        createCategory(2, "Category-2", "Category-2 Description");
        commit();

        // Full list;
        assertThat(selectFrom(CATEGORY).count()).isEqualTo(2);

        assertThat(Category.list(asList("1", "2")).toList().toStrings()).containsExactly("Category-1 Description", "Category-2 Description");

        cache.sqlStatement("delete from QName(BASIC_TEST, CATEGORY) where ID_KEY = '1'").execute();
        cache.sqlStatement("update QName(BASIC_TEST, CATEGORY) set DESCR ='New Desc' where ID_KEY = '2'").execute();
        cache.sqlStatement("insert into QName(BASIC_TEST, CATEGORY) (ID_KEY, NAME, DESCR) values ('3','Cat 3'," +
            "'Cat 3')").execute();

        commit();

        assertNotNull(Category.find(1));
        assertNotNull(Category.find(3));
        assertThat(assertNotNull(Category.find(2)).getDescr()).isEqualTo("Category-2 Description");

        cache.invalidateMap(Category.class.getName());

        assertThat(Category.find(1)).isNull();
        assertNotNull(Category.find(3));

        assertThat(assertNotNull(Category.find(2)).getDescr()).isEqualTo("New Desc");
    }

    @Test public void testOnLoad() {
        // Create a Store;
        final Store s = Store.create();
        s.setName("Compumundo");
        s.insert();
        commit();

        // Read and check
        final Store s0 = Store.find(1);
        assertNotNull(s0);
        final Store s1 = assertNotNull(Store.find(1));
        assertNotNull(s);

        assertThat(s1.getId10()).isEqualTo(10);

        final Store s2 = assertNotNull(selectFrom(STORE).get());
        assertThat(s1).isNotSameAs(s2);
        assertThat(s1.dataForTests()).isNotSameAs(s2.dataForTests());

        assertThat(s2.getId10()).isEqualTo(10);
        assertThat(s1.dataForTests()).isSameAs(s2.dataForTests());

        assertThat(cache.counters.finds).isEqualTo(1);
    }

    @Test public void testOptimistickLock()
        throws ExecutionException, InterruptedException
    {
        createCategory(1, "Category-1", "Category-1 Description");
        commit();

        final Category category = Category.find(1);

        final Callable<Boolean> callable = () -> {
                                               final Category category2 = Category.find(1);
                                               assert category2 != null;
                                               System.out.println(category2.getInstanceVersion());
                                               category2.setDescr("BBBB");
                                               category2.forceUpdate();
                                               commit();
                                               return true;
                                           };

        final Future<Boolean> future   = Executors.newSingleThreadExecutor().submit(callable);
        final Boolean         aBoolean = future.get();
        assertThat(aBoolean).isTrue();
        assert category != null;
        try {
            category.setDescr("AAAA");
            category.update();
            commit();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        }
        catch (final RuntimeException ignore) {}

        final Category category1 = Category.find(1);
        assert category1 != null;
        assertThat(category1.getDescr()).isEqualTo("BBBB");
    }
    @Test public void testProduct() {
        createCategory(1, "Category-1", "Category-1 Description");
        final Category cat1 = assertNotNull(Category.find(1));

        final BigDecimal FIVE = BigDecimal.valueOf(5.55);

        createProduct("12345678", "Product 12345678", "Product 12345678 Description", State.ACTIVE, FIVE, cat1);
        createProduct("87654321", "Product 87654321", "Product 87654321 Description", State.ACTIVE, BigDecimal.valueOf(100), cat1);
        commit();

        final Product p1 = assertNotNull(Product.find("12345678"));
        assertThat(p1.getModel()).isEqualTo("Product 12345678");
        assertThat(p1.getState()).isEqualTo(State.ACTIVE);

        // Several queries by price;

        assertThat(Product.listWhere(PRODUCT.PRICE.ge(FIVE)).count()).isEqualTo(2);
        assertThat(Product.listWhere(PRODUCT.PRICE.gt(FIVE)).count()).isEqualTo(1);
        assertThat(Product.listWhere(PRODUCT.PRICE.le(FIVE)).count()).isEqualTo(1);
        assertThat(Product.listWhere(PRODUCT.PRICE.lt(FIVE)).count()).isEqualTo(0);

        final BigDecimal ONE = BigDecimal.valueOf(1);
        final BigDecimal TEN = BigDecimal.valueOf(10);
        assertThat(Product.listWhere(PRODUCT.PRICE.between(ONE, TEN)).count()).isEqualTo(1);
        assertThat(Product.listWhere(PRODUCT.PRICE.in(asList(ONE, FIVE))).count()).isEqualTo(1);

        p1.setState(State.DISCONTINUED);
        p1.update();
        commit();

        final Product p2 = assertNotNull(Product.find("12345678"));
        assertThat(p2.getModel()).isEqualTo("Product 12345678");
        assertThat(p2.getState()).isEqualTo(State.DISCONTINUED);

        final Category cat = assertNotNull(Product.find("12345678")).getCategory();
        assertThat(cat.getIdKey()).isEqualTo(1);
        assertThat(cat.getName()).isEqualTo("Category-1");
        assertThat(cat.getDescr()).isEqualTo("Category-1 Description");

        assertThat(cache.counters.deletes).isEqualTo(0);
        assertThat(cache.counters.inserts).isEqualTo(3);
        assertThat(cache.counters.updates).isEqualTo(1);
    }  // end method testProduct

    @Test public void testRollBack() {
        runInTransaction(t -> {
            createCategory(1, "Category-1", "Category-1 Description");
            createCategory(2, "Category-2", "Category-2 Description");
            t.abort();
        });

        runInTransaction(() -> {
            assertThat(Category.find(1)).isNull();
            createCategory(1, "Category-1", "Category-1 Description");
            createCategory(2, "Category-2", "Category-2 Description");
        });

        runInTransaction(t -> {
            final Category c1 = assertNotNull(Category.find(1));
            assertThat(c1.getName()).isEqualTo("Category-1");
            c1.delete();
            assertThat(Category.find(1)).isNull();
            t.abort();
        });

        runInTransaction(() -> assertThat(assertNotNull(Category.find(1)).getName()).isEqualTo("Category-1"));

        assertThat(cache.counters.deletes).isEqualTo(1);
        assertThat(cache.counters.inserts).isEqualTo(4);
        assertThat(cache.counters.finds).isEqualTo(4);
    }

    @Test public void testUserCache() {
        final Cache<Object, Object> userMap = cache.getMap("pepe");

        userMap.put("1", "Pepe");
        userMap.put("2", "Josefina");
        assertThat(userMap.values().size()).isEqualTo(2);
        assertThat(userMap.get("1")).isEqualTo("Pepe");
    }

    private void commit() {
        Transaction.getCurrent().ifPresent(Transaction::commit);
    }

    private Product createProduct(String id, String model, String descr, State state, BigDecimal price, Category category) {
        final Product product = Product.create(id);
        product.setModel(model);
        product.setDescription(descr);
        product.setState(state);
        product.setPrice(price);
        product.setCategory(category);

        final Option<Resource> rs = rh.findResource("000000");
        product.setMainImage(rs.isPresent() ? rs.get() : rh.create("000000").upload("testName", "hhtp://test"));
        product.insert();
        return product;
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    // seems to be a bug in hazelcast;
    // @Test public void testCachelist() {
    //
    // listDslHelper.runInContext("cacheTestQ", DatabaseType.HSQLDB, CacheLevel.FULL_WITH_INNERS) {
    //
    // final Category category = Category.create(1);
    // category.setName("Category-1");
    // category.setDescr("Category-1 Description");
    //
    // final Category category2 = Category.create(2);
    // category2.setName("Category-2");
    // category2.setDescr("Category-2 Description");
    //
    //
    // category.insert();
    // category2.insert();
    // cache.commitTransaction();
    //
    // CloseableSeq<Category> categories = selectFrom(CATEGORY);
    // assertThat(categories.size).isEqualTo(2);
    //
    // int table = Category.TABLE;
    //
    //
    //
    // categories = table.listWhere(table.active.isFalse);
    // assertThat(categories.size).isEqualTo(2);
    //
    //
    // category.setActive(true);
    // category.persist();
    // categories = table.listWhere(table.active.isFalse);
    // assertThat(categories.size).isEqualTo(1);
    // cache.commitTransaction();
    // }
    //
    //
    // }

    // @Test public void testCacheMaxIdle() {
    //
    // final CacheConfig cc = new CacheConfig;
    // cc.cache = true;
    // cc.size = 50000;
    // cc.maxIdle = 1;
    //
    // env.put("tekgenesis.test.basic.Product",cc);
    // env.put("tekgenesis.test.basic.Category",cc);
    // env.put("tekgenesis.test.basic.Store",cc);
    // env.put("tekgenesis.test.basic.Location",cc);
    //
    // // Create a Store;
    // int store = createStore();
    // store.setName("Compumundo");
    // store.insert();
    // cache.commitTransaction();
    //
    // Tests.sleep(1000);
    // // Find it and update inners;
    // store = Store.find(1);
    // store should not be null;
    //
    // assertThat(cache.counters.finds).isEqualTo(1);
    // }
    // @Test public void testIndexesKey() {
    //
    // createCategory(1, "Cat", "Cat");
    // int amount  = 5000;
    // int cat = Category.find(1);
    // createProduct("disc", "disc", "disc", State.DISCONTINUED, new BigDecimal(10), cat, amount +1);
    // 0 until amount foreach(i => createProduct(i.toString(),i.toString,i.toString,State.ACTIVE,new BigDecimal(0),cat, i))
    // cache.commitTransaction();
    //
    // println("-------- after init ---------");
    // int t0 = System.currentTimeMillis();
    // int table = Product.TABLE;
    // int result = Product.list();
    // result.toList;
    ////    result = Product.list().where(table.state.eq(State.DISCONTINUED)).list();
    // assertThat(result).hasSize(1);
    // int t1 = System.currentTimeMillis();
    // assertThat(result).hasSize(1);
    // println("Elapsed time: " + (t1 - t0) + "ms");
    // t0 = System.currentTimeMillis();
    // int product = Product.findByCodigo(amount+1);
    // assertThat(product).isNotNull();
    // t1 = System.currentTimeMillis();
    // println("Elapsed time: " + (t1 - t0) + "ms");
    //
    //
    //
    //
    // }
}  // end class CacheTest
