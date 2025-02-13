
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.cache.CacheConnection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.util.MemoEntry;
import tekgenesis.database.support.NotifyOnCloseConnection;
import tekgenesis.persistence.*;
import tekgenesis.test.basic.CachedCategory;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.transaction.TransactionResource;

import static java.util.concurrent.TimeUnit.MINUTES;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.test.basic.g.CachedCategoryForUpdateBase.cachedCategoryForUpdate;
import static tekgenesis.test.basic.g.CachedCategoryTable.CACHED_CATEGORY;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class CacheTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DbRule db = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }

            @Override protected StoreHandler.Factory createStoreHandlerFactory() {
                final StoreHandler.Factory sh = super.createStoreHandlerFactory();
                return new CacheFactory(getTransactionManager(), sh);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void basicTest() {
        // Create 2 categories;
        runInTransaction(() -> {
            assertThat(CachedCategory.find(2)).isNull();
            cachedCategoryForUpdate(1).setName("Category-1").setDescr("Category-1 Description").insert();
            cachedCategoryForUpdate(2).setName("Category-2").setDescr("Category-2 Description").insert();
            assertThat(CachedCategory.find(2)).isNotNull();
        });

        runInTransaction(() -> {
            assertThat(CachedCategory.findOrFail(2).getName()).isEqualTo("Category-2");
            // Check that they are there, delete the first;
            assertThat(CachedCategory.list().count()).isEqualTo(2);

            assertThat(db.sqlStatement("select * from QName(BASIC_TEST, CACHED_CATEGORY)").list(CachedCategory.class))  //
            .extracting("name").containsExactly("Category-1", "Category-2");

            assertThat(selectFrom(CACHED_CATEGORY).count()).isEqualTo(2);
            assertThat(selectFrom(CACHED_CATEGORY).where(CACHED_CATEGORY.NAME.eq("Category-1")).count()).isEqualTo(1);
        });
        // delete the first;

        runInTransaction(() -> {
            final CachedCategory c2 = CachedCategory.find(2);
            assertThat(c2).isNotNull();
            cachedCategoryForUpdate(c2).delete();
        });

        // Check again;
        runInTransaction(() -> {
            assertThat(CachedCategory.find(2)).isNull();
            assertThat(selectFrom(CACHED_CATEGORY).exists()).isTrue();
            assertThat(selectFrom(CACHED_CATEGORY).count()).isEqualTo(1);
        });
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Inner Classes ................................................................................................................................

    static class CacheFactory extends TransactionResource.Default<NotifyOnCloseConnection> implements StoreHandler.Factory {
        private final StoreHandler.Factory parentFactory;

        /** Create the factory. */
        public CacheFactory(TransactionManager tm, StoreHandler.Factory parent) {
            super(CacheConnection.class.getName(), tm);
            parentFactory = parent;
        }

        @NotNull @Override public NotifyOnCloseConnection createConnection() {
            return new NotifyOnCloseConnection();
        }

        @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
            final StoreHandler<I, K> parent = getParentFactory().createHandler(storeType, dbTable);
            // Disable CacheConnection for Ideafix
            if (parent == null || !storeType.isEmpty()) return parent;
            final DelegatingStoreHandler.ByThread<I, K> sh = new DelegatingStoreHandler.ByThread<>(parent);
            sh.setDefaultDelegate(new InMemoryStoreHandler<>(sh));
            return sh;
        }

        @NotNull @Override public StoreHandler.Factory getParentFactory() {
            return parentFactory;
        }

        private class InMemoryStoreHandler<T extends EntityInstance<T, K>, K> extends DelegatingStoreHandler<T, K> {
            private final MemoEntry<TableData<T, K>> memo;
            private final ByThread<T, K>             tsh;

            /** Create a Delegating StoreHandler. */
            public InMemoryStoreHandler(@NotNull ByThread<T, K> tsh) {
                super(tsh.getParent());
                this.tsh = tsh;
                memo     = new MemoEntry<>();
            }

            @Override public T find(K key) {
                return getData().itemsByPk.get(key);
            }

            @NotNull @Override protected StoreHandler<T, K> getDelegate() {
                getConnection(false).addListener(() -> {
                    memo.clear();
                    tsh.resetDelegate();
                });
                tsh.setDelegate(getParent());
                return getParent();
            }
            private TableData<T, K> getData() {
                return memo.get(MINUTES.toMillis(10), (l, t) -> new TableData<>(getDbTable(), listAll()));
            }
        }

        static class TableData<T extends EntityInstance<T, K>, K> {
            private final TreeMap<K, T>       itemsByPk;
            private final TableMetadata<T, K> metadata;

            TableData(DbTable<T, K> dbTable, ImmutableList<T> items) {
                metadata  = dbTable.metadata();
                itemsByPk = new TreeMap<>();
                items.forEach(t -> itemsByPk.put(t.keyObject(), t));
            }
        }
    }  // end class CacheFactory
}  // end class CacheTest
