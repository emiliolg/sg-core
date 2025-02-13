
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.DbTimeProviderRule;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.test.basic.*;
import tekgenesis.test.basic.g.CategoryTable;
import tekgenesis.test.basic.g.ProductTable;
import tekgenesis.test.basic.g.StoreTable;

import static java.lang.String.format;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Criteria.EMPTY;
import static tekgenesis.persistence.Sql.*;
import static tekgenesis.persistence.test.BasicTest.createCategory;
import static tekgenesis.test.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.test.basic.g.ProductTable.PRODUCT;
import static tekgenesis.test.basic.g.SaleTable.SALE;
import static tekgenesis.test.basic.g.StoreTable.STORE;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class QueriesTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    @Rule public DbRule      db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public final DbTimeProviderRule timeProvider = new DbTimeProviderRule();

    //~ Methods ......................................................................................................................................

    @Test public void booleanColumn() {
        runInTransaction(() -> {
            final Category category = createCategory(1, "Category 1", "Description 1");
            final Product  product1 = createProduct(1, "Model", "Product", State.ACTIVE, 100.0, category);
            product1.setActive(true);
            product1.persist();
        });

        runInTransaction(() -> {
            final Product product = selectFrom(PRODUCT).where(PRODUCT.ACTIVE.eq(true)).list().get(0);

            assertThat(product).isNotNull();
        });
    }

    @Test public void cachedQueries() {
        // Create Some categories;
        runInTransaction(() -> {
            for (int i = 1; i <= 10; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2'");
        });

        final CategoryTable C = CATEGORY;
        runInTransaction(() -> {
            assertThat(selectFrom(CATEGORY).cache(2).count()).isEqualTo(10);

            assertThat(select(C.NAME).from(CATEGORY).orderBy(C.ID_KEY.descending()).cache(2).list().get(0)).isEqualTo("Category 10");
        });

        // create an additional category */
        runInTransaction(() -> {
            createCategory(100, "Category 100", "The 100th one");
            // Queries must return the same values
            assertThat(selectFrom(CATEGORY).cache(2).count()).isEqualTo(10);
            assertThat(select(C.NAME).from(CATEGORY).orderBy(C.ID_KEY.descending()).cache(2).list().get(0)).isEqualTo("Category 10");

            // Now advance the time
            timeProvider.increment(10_000);

            // Queries now must return the new values
            assertThat(selectFrom(CATEGORY).cache(2).count()).isEqualTo(11);
            assertThat(select(C.NAME).from(CATEGORY).orderBy(C.ID_KEY.descending()).cache(2).list().get(0)).isEqualTo("Category 100");
        });
    }

    @Test public void caseExpression() {
        // Create Some categories;
        runInTransaction(() -> {
            for (int i = 0; i <= 6; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2");
        });

        runInTransaction(() -> {
            final ImmutableList<QueryTuple> simple = select(CATEGORY.ID_KEY,
                    CATEGORY.DESCR.when("Description 1").then(CATEGORY.NAME).otherwise("No Name")).from(CATEGORY).list();
            assertThat(simple.toStrings()).containsExactly("idKey=0,2=Category 0",
                "idKey=1,2=Category 1",
                "idKey=2,2=Category 2",
                "idKey=3,2=Category 3",
                "idKey=4,2=Category 4",
                "idKey=5,2=No Name",
                "idKey=6,2=No Name");

            final ImmutableList<QueryTuple> searched = select(CATEGORY.NAME,
                    CATEGORY.DESCR.eq("Description 2").and(CATEGORY.NAME.eq("Category 6")).then("Some new name").end()).from(CATEGORY).list();
            assertThat(searched.toStrings()).containsExactly("name=Category 0,2=null",
                "name=Category 1,2=null",
                "name=Category 2,2=null",
                "name=Category 3,2=null",
                "name=Category 4,2=null",
                "name=Category 5,2=null",
                "name=Category 6,2=Some new name");
        });
    }

    @Test public void emptyCriteriaStatement() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 0; i <= 11; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2");

            // Create Some Products;
            for (int i = 1; i <= 100; i++)
                createProduct(i,
                    "Model " + i % 5,
                    "Product " + i,
                    i % 7 == 0 ? State.DISCONTINUED : State.ACTIVE,
                    i * 100.0,
                    assertNotNull(Category.find(i % 10)));
        });
        runInTransaction(() -> {
            assertThat(selectFrom(PRODUCT).list()).hasSize(100);

            assertThat(selectFrom(PRODUCT).where(EMPTY).list()).hasSize(100);
            assertThat(selectFrom(PRODUCT).where(EMPTY).asSql()).isEqualTo(
                "select PRODUCT_ID,MODEL,DESCR,PRICE,STATE,ACTIVE,CAT," +
                "MAIN_IMAGE,BRAND_ID,UPDATE_TIME,CREATION_TIME,CREATION_USER,UPDATE_USER\n" +
                "from QName(BASIC_TEST, PROD)\n");
            deleteFrom(PRODUCT).execute();

            assertThat(selectFrom(PRODUCT).where(EMPTY).list()).hasSize(0);
        });
    }
    @Test public void inStatement() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 0; i <= 11; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2");

            // Create Some Products;
            for (int i = 1; i <= 2000; i++)
                createProduct(i,
                    "Model " + i % 5,
                    "Product " + i,
                    i % 7 == 0 ? State.DISCONTINUED : State.ACTIVE,
                    i * 100.0,
                    assertNotNull(Category.find(i % 10)));
        });

        final ProductTable P = PRODUCT;

        runInTransaction(() -> {
            assertThat(selectFrom(P).where(P.PRODUCT_ID.in(makeIds(1, 1500))).list()).hasSize(1500);

            assertThat(selectFrom(P).where(P.PRODUCT_ID.in(makeIds(1, 1000))).list()).hasSize(1000);

            assertThat(selectFrom(P).where(P.PRODUCT_ID.in(makeIds(1, 3500))).list()).hasSize(2000);

            assertThat(Product.list(makeIds(1800, 1900)).toList().size()).isEqualTo(101);
        });
    }

    @Test public void multiTableQueries() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 0; i <= 11; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : i == 11 ? "" : "Description 2");

            // Create Some Products;
            for (int i = 1; i <= 100; i++)
                createProduct(i,
                    "Model " + i % 5,
                    "Product " + i,
                    i % 7 == 0 ? State.DISCONTINUED : State.ACTIVE,
                    i * 100.0,
                    assertNotNull(Category.find(i % 10)));
        });

        final CategoryTable C = CATEGORY;
        final ProductTable  P = PRODUCT;

        runInTransaction(() -> {
            final ImmutableList<DN> l = select(P.DESCRIPTION, C.NAME).as(DN.class)
                                                                     .from(PRODUCT)
                                                                     .join(C, P.CATEGORY_ID_KEY.eq(C.ID_KEY))
                                                                     .where(P.PRICE.add(10).ge(1000).and(P.PRICE.le(1200.0)))
                                                                     .orderBy(P.DESCRIPTION)
                                                                     .list();

            assertThat(l.toStrings()).containsExactly("Product 10:Category 0", "Product 11:Category 1", "Product 12:Category 2");

            assertThat(selectFrom(CATEGORY).join(P, P.CATEGORY_ID_KEY.eq(C.ID_KEY)).where(C.ID_KEY.ge(9)).count()).isEqualTo(10);

            assertThat(selectFrom(CATEGORY).leftOuterJoin(P, P.CATEGORY_ID_KEY.eq(C.ID_KEY)).where(C.ID_KEY.ge(9)).count()).isEqualTo(12);

            final ImmutableList<Product> list = selectFrom(PRODUCT).join(C, P.CATEGORY_ID_KEY.eq(C.ID_KEY))
                                                                   .where(C.ID_KEY.ge(9))
                                                                   .orderBy(P.PRODUCT_ID)
                                                                   .list()
                                                                   .toList();

            assertThat(list).extracting("description")
                            .containsExactly("Product 19",
                                "Product 29",
                                "Product 39",
                                "Product 49",
                                "Product 59",
                                "Product 69",
                                "Product 79",
                                "Product 89",
                                "Product 9",
                                "Product 99");

            assertThat(select(C.ID_KEY).from(CATEGORY).leftOuterJoin(P, P.CATEGORY_ID_KEY.eq(C.ID_KEY)).where(P.PRODUCT_ID.isNull()).list())
            .containsExactly(10, 11);

            final ImmutableList<Category> cs = selectFrom(CATEGORY).join(P, P.CATEGORY_ID_KEY.eq(C.ID_KEY)).where(C.ID_KEY.ge(9)).list();
            assertThat(cs.size()).isEqualTo(10);

            final ImmutableList<String> pc1 = select(PRODUCT.PRODUCT_ID).from(PRODUCT)  //
                                                                        .where(
                                                                            PRODUCT.CATEGORY_ID_KEY.in(  //
                                                                                select(CATEGORY.ID_KEY)  //
                                                                                .from(CATEGORY)  //
                                                                                .where(CATEGORY.NAME.eq("Category 1")))  //
                    )
                                                                        .list();
            //
            assertThat(pc1).containsOnly("1", "11", "21", "31", "41", "51", "61", "71", "81", "91");

            final ImmutableList<Integer> l1 = select(CATEGORY.ID_KEY).from(CATEGORY).where(CATEGORY.DESCR.isEmpty()).list();
            assertThat(l1).contains(11);
            assertThat(selectFrom(CATEGORY).where(CATEGORY.DESCR.isNotEmpty()).count()).isEqualTo(11);
        });
    }  // end method multiTableQueries

    @Test public void nestedMaxFromRange() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 0; i <= 25; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2");

            // Create Some Products;
            for (int i = 1; i <= 100; i++)
                createProduct(i,
                    "Model " + i % 5,
                    "Product " + i,
                    i % 7 == 0 ? State.DISCONTINUED : State.ACTIVE,
                    i * 100.0,
                    assertNotNull(Category.find(i % 10)));
        });

        runInTransaction(() -> {
            final SubQuery<Integer>    RANGE = select(CATEGORY.ID_KEY).from(CATEGORY).where(CATEGORY.ID_KEY.between(5, 15)).as("RANGE");
            final SubQuery<QueryTuple> PRODS = select(PRODUCT.CATEGORY_ID_KEY, PRODUCT.PRICE).from(PRODUCT)
                                                                                             .where(PRODUCT.CATEGORY_ID_KEY.in(RANGE))
                                                                                             .as("PRODS");
            assertThat(select(PRODS.intColumn("PRICE").max()).from(PRODS).get()).isEqualTo(9900);
        });
    }

    @Test public void nestedQueries() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 0; i <= 19; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2");

            // Create Some Products;
            for (int i = 1; i <= 100; i++)
                createProduct(i,
                    "Model " + i % 5,
                    "Product " + i,
                    i % 7 == 0 ? State.DISCONTINUED : State.ACTIVE,
                    i * 100.0,
                    assertNotNull(Category.find(i % 10)));

            final Select<Category> inner = selectFrom(CATEGORY).where(CATEGORY.NAME.like("Category 1%"));

            assertThat(inner.count()).isEqualTo(11);
            final long count1 = selectFrom(inner).where(CATEGORY.ID_KEY.between(5, 15)).count();
            assertThat(count1).isEqualTo(6);

            final QueryTuple qt = assertNotNull(

                    // #select
                    select(CATEGORY.ID_KEY, CATEGORY.NAME, CATEGORY.DESCR)      //
                    .from(CATEGORY)                                             //
                    .where(CATEGORY.ID_KEY.eq(1))                               // #select
                    .get());
            assertThat(qt.get(CATEGORY).map(Category::getName).orElse("")).isEqualTo("Category 1");

            final QueryTuple qt2 = assertNotNull(

                    // #selectAndAllOf
                    select(CATEGORY.NAME.length()).andAllOf(CATEGORY)      //
                    .from(CATEGORY)                                        //
                    .where(CATEGORY.ID_KEY.eq(2))                          // #selectAndAllOf
                    .get());
            assertThat(qt2.get(CATEGORY).map(Category::getName).orElse("")).isEqualTo("Category 2");
            assertThat(qt2.getOrFail(CATEGORY.NAME.length())).isEqualTo(10);

            //
            // select GROUP.DESCR, GROUP.C
            // from (
            // select CATEGORY.DESCR, count(CATEGORY.DESCR) C
            // from  CATEGORY
            // group by CATEGORY_DESCR
            // ) GROUP
            // where GROUP.DESCR = 'Description 1'
            //

            final SubQuery<QueryTuple> GROUP =
                select(CATEGORY.DESCR, CATEGORY.DESCR.count().as("C"))  //
                .from(CATEGORY)                                         //
                .groupBy(CATEGORY.DESCR)                                //
                .as("GROUP");

            final Expr.Str   GROUP_DESCR = GROUP.strColumn("DESCR");
            final QueryTuple n           =
                select(GROUP_DESCR, GROUP.longColumn("C"))  //
                .from(GROUP)                                //
                .where(GROUP_DESCR.eq("Description 1"))     //
                .get();

            assertThat(assertNotNull(n).get(GROUP.longColumn("C"))).isEqualTo(5);
        });
    }  // end method nestedQueries

    @Test public void numericColumn() {
        runInTransaction(() -> {
            final Category category = createCategory(1, "Category 1", "Description 1");
            final Product  product1 = createProduct(1, "Model", "Product", State.ACTIVE, 123.63, category);
            product1.setActive(true);
            product1.persist();
        });

        //J-
        final QueryTuple            qt     = invokeInTransaction(() ->
                select( PRODUCT.PRICE,
                        PRODUCT.PRICE.negate(),
                        PRODUCT.PRICE.negate().abs(),
                        PRODUCT.PRICE.trunc(),
                        PRODUCT.PRICE.trunc(1),
                        PRODUCT.PRICE.trunc(-1),
                        PRODUCT.PRICE.round(),
                        PRODUCT.PRICE.round(1),
                        PRODUCT.PRICE.round(-1),
                        PRODUCT.PRICE.trunc().mod(2),
                        PRODUCT.PRICE.pow(2).round(2),
                        PRODUCT.PRICE.floor(),
                        PRODUCT.PRICE.ceil(),
                        PRODUCT.PRICE.ln().round(2),
                        Expr.constant(1.0).exp().round(10))
                .from(PRODUCT)
                .get());
        //J+
        final ImmutableList<Double> values = qt == null ? ImmutableList.empty() : qt.toList().map(o -> ((Number) o).doubleValue()).toList();
        assertThat(values).containsExactly(123.63,
            -123.63,
            123.63,
            123.0,
            123.6,
            120.0,
            124.0,
            123.6,
            120.0,
            1.0,
            15284.38,
            123.0,
            124.0,
            4.82,
            2.7182818285);
    }  // end method numericColumn

    @Test public void offset() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 1; i < 15; i++)
                createCategory(i, format("Category %s", i), "Description");
        });
        runInTransaction(() -> {
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).list()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).offset(0).limit(3).list()).containsExactly(1, 2, 3);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).offset(1).limit(3).list()).containsExactly(2, 3, 4);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).offset(2).limit(3).list()).containsExactly(3, 4, 5);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).offset(10).list()).containsExactly(11, 12, 13, 14);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).offset(0).limit(0).list()).isEmpty();
        });
    }

    @Test public void oneTableQueries() {
        // Create Some categories;
        runInTransaction(() -> {
            for (int i = 1; i <= 10; i++)
                createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2'");
        });

        final CategoryTable C = CATEGORY;
        runInTransaction(() -> {
            assertThat(selectFrom(CATEGORY).count()).isEqualTo(10);

            assertThat(selectFrom(CATEGORY).where(C.NAME.like("%y 1%")).count()).isEqualTo(2);

            assertThat(select(C.NAME).from(CATEGORY).orderBy(C.ID_KEY.descending()).list().get(0)).isEqualTo("Category 10");
            assertThat(select(C.NAME).from(CATEGORY).orderBy(C.ID_KEY).get()).isEqualTo("Category 1");

            final ImmutableList<QueryTuple> l = select(C.DESCR, C.DESCR.count()).from(CATEGORY).groupBy(C.DESCR).orderBy(C.DESCR).list();
            assertThat(l.get(0).get(C.DESCR)).isEqualTo("Description 1");
            assertThat(l.get(0).get(C.DESCR.count())).isEqualTo(4);
            assertThat(l.get(1).get(C.DESCR)).isEqualTo("Description 2'");
            assertThat(l.get(1).get(C.DESCR.count())).isEqualTo(6);

            assertThat(selectFrom(CATEGORY).where(C.DESCR.eq("Description 2'")).count()).isEqualTo(6);

            assertThat(select(C.DESCR).from(CATEGORY).groupBy(C.DESCR).having(C.DESCR.count().ge(6)).get()).isEqualTo("Description 2'");

            final ImmutableList<String> list = select(C.DESCR).from(CATEGORY).groupBy(C.DESCR, C.ID_KEY).list();
            assertThat(list).contains("Description 1").hasSize(10);

            assertThat(select(C.DESCR).from(CATEGORY).distinct().list()).containsOnly("Description 1", "Description 2'");

            final int m = update(CATEGORY).set(CATEGORY.DESCR, CATEGORY.DESCR.substr(1, 5)).where(CATEGORY.ID_KEY.ge(3)).execute();
            assertThat(m).isEqualTo(8);

            final ImmutableList<String> l2 = select(C.DESCR).from(CATEGORY).groupBy(C.DESCR, C.ID_KEY).list();
            assertThat(l2).contains("Descr").hasSize(10);
        });
        // Time arithmetic

        final TableField.DTime UT    = C.UPDATE_TIME;
        final Expr.DTime       added = UT.add(10, TimeUnit.MINUTES);
        runInTransaction(() -> {
            final QueryTuple t = assertNotNull(
                    select(UT, added, UT.year(), UT.month(), UT.day(), UT.hour(), UT.minute(), UT.second()).from(CATEGORY).get());

            assertThat(t.getOrFail(added).secondsFrom(t.getOrFail(UT))).isEqualTo(10 * 60);
            final String date = format("%04d-%02d-%02d %02d:%02d:%06.3f",
                    t.get(UT.year()),
                    t.get(UT.month()),
                    t.get(UT.day()),
                    t.get(UT.hour()),
                    t.get(UT.minute()),
                    t.get(UT.second()));
            assertThat(date).isEqualTo(t.getOrFail(UT).format("yyyy-MM-dd HH:mm:ss.sss"));

            assertThat(selectFrom(CATEGORY).where(CATEGORY.UPDATE_TIME.ge(DateTime.MIN_VALUE)).count()).isEqualTo(10);
            assertThat(selectFrom(CATEGORY).where(CATEGORY.UPDATE_TIME.le(DateTime.MAX_VALUE)).count()).isEqualTo(10);
        });
    }  // end method oneTableQueries

    @Test public void orderBy() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 2; i < 5; i++)
                createCategory(i, format("Category %s", i), "Description");
        });

        runInTransaction(() -> {
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).orderBy(CATEGORY.NAME).list()).contains(2, 3, 4);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).orderBy(CATEGORY.NAME.descending()).list()).contains(4, 3, 2);

            // Create 2 null names
            createCategory(1, null, "Null 1");
            createCategory(5, null, "Null 5");

            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).orderBy(CATEGORY.NAME.nullsFirst()).list()).contains(1, 5, 2, 3, 4);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).orderBy(CATEGORY.NAME.descending().nullsFirst()).list()).contains(1, 5, 4, 3, 2);

            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).orderBy(CATEGORY.NAME.nullsLast()).list()).contains(2, 3, 4, 1, 5);
            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).orderBy(CATEGORY.NAME.descending().nullsLast()).list()).contains(4, 3, 2, 1, 5);
        });
    }

    @Test public void testDistinct() {
        runInTransaction(() -> {
            final Category category = createCategory(1, "LCD", "LCD");
            createProduct(1, "S1", "S1", State.ACTIVE, 10d, category);
            createProduct(2, "S2", "S2", State.ACTIVE, 10d, category);
        });

        runInTransaction(() ->
                assertThat(selectFrom(CATEGORY).join(PRODUCT, CATEGORY.ID_KEY.eq(PRODUCT.CATEGORY_ID_KEY)).distinct().list().toList().size())
                .isEqualTo(1));
    }

    @Test public void testForUpdate() {
        runInTransaction(() -> {
            for (int i = 2; i < 5; i++)
                createCategory(i, format("Category %s", i), "Description");
        });

        runInTransaction(() -> selectFrom(CATEGORY).where(CATEGORY.ID_KEY.eq(1)).forUpdate().get());
    }

    @Test public void testQueryAliasSameTable() {
        runInTransaction(() -> {
            final Store store = Store.create();
            store.setName("G");
            store.setActiveModules(EnumSet.of(ActiveModule.SUPPLY));
            store.persist();

            final Sale pa = Sale.create(1);
            pa.setStore(store);
            pa.setDate(DateTime.current());
            pa.insert();
        });

        final StoreTable P1 = STORE.as("S1");
        final StoreTable P2 = STORE.as("S2");

        runInTransaction(() ->
                select(SALE.DATE).andAllOf(P1)
                                 .andAllOf(P2)
                                 .from(SALE)
                                 .leftOuterJoin(P1, P1.ID.eq(SALE.PICKUP_STORE_ID))
                                 .leftOuterJoin(P2, P2.ID.eq(SALE.STORE_ID))
                                 .forEach(qt -> {
                                     assertThat(qt.get(P1).isEmpty()).isTrue();
                                     assertThat(qt.get(P2).isPresent()).isTrue();
                                 }));
    }

    @Test public void testUnion() {
        runInTransaction(() -> {
            // Create Some categories;
            for (int i = 0; i < 10; i++)
                createCategory(i, format("Category %s", i), i % 2 == 0 ? "Description 1" : "Description 2");

            // Create Some Products;
            for (int i = 0; i < 20; i++)
                createProduct(i,
                    "Model " + i % 5,
                    i % 2 == 0 ? "Description 1" : "Description 2",
                    i % 7 == 0 ? State.DISCONTINUED : State.ACTIVE,
                    (double) i,
                    assertNotNull(Category.find(i % 10)));

            Store.create().setName("Store name").insert();
        });

        runInTransaction(() -> {
            final ImmutableList<String> union    = select(CATEGORY.DESCR).from(CATEGORY).union(select(PRODUCT.DESCRIPTION).from(PRODUCT)).toList();
            final ImmutableList<String> unionAll = select(CATEGORY.DESCR).from(CATEGORY).unionAll(select(PRODUCT.DESCRIPTION).from(PRODUCT)).toList();

            assertThat(union).contains("Description 1", "Description 2");
            assertThat(unionAll).hasSize(30);

            final ImmutableList<String> invertedUnion = select(CATEGORY.DESCR).from(CATEGORY)
                                                                              .union(select(PRODUCT.DESCRIPTION).from(PRODUCT))
                                                                              .orderBy(CATEGORY.DESCR.descending())
                                                                              .toList();

            assertThat(invertedUnion).containsExactly("Description 2", "Description 1");

            final ImmutableList<QueryTuple> doubleUnion = select(CATEGORY.DESCR, CATEGORY.ID_KEY).from(CATEGORY)
                                                                                                 .union(
                    select(PRODUCT.DESCRIPTION, PRODUCT.PRICE).from(PRODUCT))
                                                                                                 .toList();
            assertThat(doubleUnion).hasSize(20);

            final ImmutableList<String> tripleUnion = select(CATEGORY.DESCR).from(CATEGORY)
                                                                            .union(select(PRODUCT.DESCRIPTION).from(PRODUCT))
                                                                            .union(select(STORE.NAME).from(STORE))
                                                                            .toList();

            assertThat(tripleUnion).contains("Description 1", "Description 2", "Store name");
        });
    }  // end method testUnion

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    // private static Category createCategory(int code, String nm, String desc) {
    // final Category category = Category.create(code);
    // category.setName(nm);
    // category.setDescr(desc);
    // category.insert();
    // return category;
    // }

    private static Product createProduct(int code, String model, String description, State state, Double price, Category category) {
        final Product product = Product.create(String.valueOf(code));
        product.setModel(model);
        product.setDescription(description);
        product.setState(state);
        product.setPrice(BigDecimal.valueOf(price));
        product.setCategory(category);

        product.insert();
        return product;
    }

    private static List<String> makeIds(final int from, final int to) {
        final List<String> ids = new ArrayList<>();
        for (int i = from; i <= to; i++)
            ids.add(String.valueOf(i));
        return ids;
    }

    //~ Inner Classes ................................................................................................................................

    static class DN {
        String description = "";
        String name        = "";

        @Override public String toString() {
            return description + ":" + name;
        }
    }
}  // end class QueriesTest
