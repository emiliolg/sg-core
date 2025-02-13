
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
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.cache.database.SessionCacheFactory;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.tools.test.CounterHandlerFactory;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.StoreHandler;
import tekgenesis.test.basic.Category;
import tekgenesis.test.basic.Product;
import tekgenesis.test.basic.State;
import tekgenesis.test.basic.Store;

import static org.assertj.core.api.Assertions.*;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class SessionCacheCacheFactoryTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public SessionCacheRule cache = new SessionCacheRule();

    @Parameter public String dbName = null;

    //~ Methods ......................................................................................................................................

    @Test public void testCacheList() {
        runInTransaction(() -> {
            for (int i = 1; i <= 100; i++)
                createCategory(i);
        });

        final List<String> keys   = new ArrayList<>();
        final StrBuilder   result = new StrBuilder().startCollection("\n");
        for (int i = 1; i <= 20; i++) {
            final String e = String.valueOf(i);
            keys.add(e);
            result.appendElement("Category-" + e + " Description");
        }
        final String str = result.toString();

        runInTransaction(() -> {
            assertThat(Category.list(keys).toList().mkString("\n")).isEqualTo(str);
            assertThat(Category.list(keys).toList().mkString("\n")).isEqualTo(str);
            assertThat(Category.list(keys).toList().mkString("\n")).isEqualTo(str);

            for (int i = 1; i <= 20; i++)
                assertThat(assertNotNull(Category.find(i)).getName()).isEqualTo("Category-" + i);

            assertThat(cache.getStats()).isEqualTo("maxSize=50,hits=60,misses=20,hitRate=75%");
        });
    }

    @Test public void testCacheMix() {
        runInTransaction(() -> {
            for (int i = 1; i <= 100; i++)
                createCategory(i);

            for (int i = 1; i <= 20; i++) {
                final Store store = Store.create();
                store.setName("Store-" + i);
                store.persist();
            }
        });

        runInTransaction(() -> {
            assertThat(assertNotNull(Category.find(1)).getName()).isEqualTo("Category-1");
            assertThat(assertNotNull(Store.find(1)).getName()).isEqualTo("Store-1");
            assertThat(assertNotNull(Category.find(1)).getName()).isEqualTo("Category-1");
            assertThat(assertNotNull(Store.find(1)).getName()).isEqualTo("Store-1");

            assertThat(cache.counters.finds).isEqualTo(2);

            for (int i = 1; i <= 10; i++)
                for (int j = i; j <= 100; j += i)
                    assertThat(assertNotNull(Category.find(j)).getName()).isEqualTo("Category-" + j);

            assertThat(cache.getStats()).isEqualTo("maxSize=50,hits=80,misses=215,hitRate=27%");
            assertThat(cache.counters.finds).isEqualTo(215);
        });
    }

    @Test public void testCacheReferences() {
        runInTransaction(() -> {
            final Category cat = createCategory(1);
            assertThat(cat.getProducts()).isEmpty();

            createProduct("100", cat).persist();
            createProduct("101", cat).persist();
            assertThat(cat.getProducts()).hasSize(2);
        });
    }

    @Test public void testCacheSimpleFind() {
        runInTransaction(() -> {
            for (int i = 1; i <= 100; i++)
                createCategory(i);
        });

        runInTransaction(() -> {
            for (int i = 1; i <= 10; i++)
                for (int j = i; j <= 100; j += i)
                    assertThat(assertNotNull(Category.find(j)).getName()).isEqualTo("Category-" + j);

            assertThat(cache.getStats()).isEqualTo("maxSize=50,hits=77,misses=214,hitRate=26%");
            assertThat(cache.counters.finds).isEqualTo(214);
        });
    }

    @Test public void testCacheUpdates() {
        runInTransaction(() -> {
            for (int i = 1; i <= 40; i++)
                createCategory(i);
        });
        runInTransaction(() -> {
            for (int i = 1; i <= 40; i++)
                assertThat(assertNotNull(Category.find(i)).getName()).isEqualTo("Category-" + i);

            for (int i = 1; i <= 40; i++)
                assertThat(assertNotNull(Category.find(i)).getName()).isEqualTo("Category-" + i);

            assertThat(cache.counters.finds).isEqualTo(40);

            for (int i = 1; i <= 20; i++)
                assertNotNull(Category.find(i)).setName("Cat " + i);

            for (int i = 21; i <= 25; i++)
                assertNotNull(Category.find(i)).delete();

            for (int i = 41; i <= 50; i++)
                createCategory(i);

            for (int i = 1; i <= 50; i++) {
                final Category cat = Category.find(i);
                if (i > 20 && i <= 25) assertThat(cat).isNull();
                else assertThat(assertNotNull(cat).getName()).isEqualTo((i <= 20 ? "Cat " : "Category-") + i);
            }

            assertThat(cache.getStats()).isEqualTo("maxSize=50,hits=110,misses=45,hitRate=70%");
            assertThat(cache.counters.finds).isEqualTo(45);
        });
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static Category createCategory(int id) {
        final Category category = Category.create(id);
        category.setName("Category-" + id);
        category.setDescr("Category-" + id + " Description");
        category.insert();
        return category;
    }

    private static Product createProduct(String id, Category category) {
        final Product product = Product.create(id);
        product.setModel("Model " + id);
        product.setDescription("Description " + id);
        product.setState(State.ACTIVE);
        product.setPrice(BigDecimal.valueOf(100));
        product.setCategory(category);
        product.insert();
        return product;
    }

    //~ Inner Classes ................................................................................................................................

    public class SessionCacheRule extends CacheRule {
        private SessionCacheFactory sessionCacheFactory = null;

        @Override protected void before() {
            createDatabase(dbName);
        }

        @Override protected StoreHandler.Factory createStoreHandlerFactory() {
            final CounterHandlerFactory cf = new CounterHandlerFactory(dbFactory);
            counters            = cf.getCounters();
            sessionCacheFactory = new SessionCacheFactory(50, getTransactionManager(), cf);
            return sessionCacheFactory;
        }

        String getStats() {
            return sessionCacheFactory.getStats();
        }
    }
}  // end class SessionCacheCacheFactoryTest
