
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.test.database;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.cache.CacheProperties;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.test.basic.Store;

import static org.assertj.core.api.Assertions.*;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class CacheEvictionTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public CacheRule cache = new CacheRule() {
            @Override protected void before() {
                final CacheProperties cp = new CacheProperties();
                cp.lifespan = 1;
                env.put("tekgenesis.test.basic.Store", cp);
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void testCacheEviction() {
        // Create 10 Stores
        runInTransaction(() -> {
            for (int i = 1; i <= 10; i++)
                createStore("Store " + i);
        });

        runInTransaction(() -> {
            for (int i = 1; i <= 10; i++)
                assertNotNull(Store.find(i));
            for (int i = 1; i <= 10; i++)
                assertNotNull(Store.find(i));

            assertThat(cache.counters.finds).isBetween(10, 19);
        });
    }

    @Test public void testCacheTtl()
        throws InterruptedException
    {
        // Create a Store
        runInTransaction(() -> createStore("Compumundo"));
        runInTransaction(() -> {
            assertNotNull(Store.find(1));
            assertNotNull(Store.find(1));

            assertThat(cache.counters.finds).isEqualTo(1);
            Tests.sleep(2000);
            // Find it AFTER TTL
            assertNotNull(Store.find(1));
            assertThat(cache.counters.finds).isEqualTo(2);

            // UPDATE IT
            final Store st = assertNotNull(Store.find(1));
            st.setName("Pepe");
            st.update();
        });

        runInTransaction(() -> {
            assertThat(assertNotNull(Store.find(1)).getName()).isEqualTo("Pepe");
            assertThat(cache.counters.finds).isEqualTo(3);
        });

        // UPDATE AND ROLLBACK
        runInTransaction(t -> {
            final Store st2 = assertNotNull(Store.find(1));
            st2.setName("Jose");
            st2.update();
            assertThat(assertNotNull(Store.find(1)).getName()).isEqualTo("Jose");
            t.abort();
        });
        runInTransaction(t -> {
            assertThat(assertNotNull(Store.find(1)).getName()).isEqualTo("Pepe");
            assertThat(cache.counters.finds).isEqualTo(5);

            // Delete and Rollback
            assertNotNull(Store.find(1)).delete();
            assertThat(Store.find(1)).isNull();
            t.abort();
        });
        runInTransaction(() -> {
            assertNotNull(Store.find(1));
            assertThat(cache.counters.finds).isEqualTo(7);

            // Delete and commit
            assertNotNull(Store.find(1)).delete();
            assertThat(Store.find(1)).isNull();
        });
        runInTransaction(() -> assertThat(Store.find(1)).isNull());
        assertThat(cache.counters.finds).isEqualTo(9);
    }  // end method testCacheTtl

    private void createStore(final String name) {
        final Store store = Store.create();
        store.setName(name);
        store.insert();
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class CacheEvictionTest
