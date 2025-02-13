
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.test;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.persistence.QueryTuple;
import tekgenesis.persistence.Select;
import tekgenesis.persistence.nomm.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.nomm.model.CategoryTable.CATEGORY;
import static tekgenesis.persistence.nomm.model.g.CategoryForUpdateBase.categoryForUpdate;
import static tekgenesis.persistence.nomm.model.g.ProductForUpdateBase.productForUpdate;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ReferenceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DatabaseRule db = new NoMmDbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void referenceMapTest() {
        final Category mainCategory = addCategory("1234", "main");
        final Category optCategory  = addCategory("4321", "optional");

        final Product product = ProductForUpdate.create("1234")
                                .setName("product")
                                .setMainCategory(mainCategory)
                                .setOptCategory(optCategory)
                                .setColor(Color.RED)
                                .insert();

        final Category category = assertNotNull(Category.find(mainCategory.getCode()));
        assertThat(category.getProducts().contains(product)).isTrue();

        final Product newProduct = ProductForUpdate.create("1111").setName("newProduct").setMainCategory(category).setColor(Color.BLUE).insert();

        assertThat(category.getProducts()).contains(newProduct);

        final Product storedNewProduct = assertNotNull(Product.find("1111"));

        assertThat(storedNewProduct.getCode()).isEqualTo("1111");
        assertThat(storedNewProduct.getName()).isEqualTo("newProduct");
        assertThat(storedNewProduct.getColor()).isEqualTo(Color.BLUE);

        final Category mc = storedNewProduct.getMainCategory();
        assertNotNull(mc);
        // noinspection ConstantConditions
        assertThat(mc.getCode()).isEqualTo(category.getCode());
        assertThat(storedNewProduct.getOptCategory()).isNull();

        assertThat(optCategory.getProducts()).isEmpty();
        productForUpdate(newProduct).setMainCategory(optCategory).update();

        assertThat(category.getProducts()).contains(product);
        assertThat(optCategory.getProducts()).contains(newProduct);
    }  // end method referenceMapTest

    @Test public void selfReferenceTest() {
        final Category parentCategory = addCategory("9999", "parent");
        for (int n = 10; n <= 20; n++)
            addCategory(String.valueOf(n), "Cat" + n, parentCategory);

        for (int n = 10; n <= 20; n++) {
            final Category c = assertNotNull(Category.find(String.valueOf(n)));
            assertThat(c.getParentCode()).isEqualTo("9999");
            assertThat(c.getName()).isEqualTo("Cat" + n);
        }
        int n = 10;

        for (final Category c : parentCategory.getChildren()) {
            assertThat(c.getParentCode()).isEqualTo("9999");
            assertThat(c.getName()).isEqualTo("Cat" + n);
            n += 1;
        }

        final String maxCode = select(CATEGORY.CODE.max()).from(CATEGORY).get();
        assertThat(maxCode).isEqualTo("9999");

        assertThat(String.valueOf(select(CATEGORY.CODE.min(), CATEGORY.CODE.count()).from(CATEGORY).get())).isEqualTo("1=10,2=12");

        final CategoryTable P = CATEGORY.as("P");

        final Select<QueryTuple> p = select(P.NAME.as("p"), CATEGORY.NAME).from(CATEGORY)
                                     .join(P, CATEGORY.PARENT_CODE.eq(P.CODE))
                                     .where(CATEGORY.CODE.between("10", "12"));
        assertThat(p.list().toStrings()).containsOnly("p=parent,name=Cat10", "p=parent,name=Cat11", "p=parent,name=Cat12");

        final ImmutableList<C> list = select(P.NAME.as("parentName"), CATEGORY.NAME).as(C.class)
                                      .from(CATEGORY)
                                      .join(P, CATEGORY.PARENT_CODE.eq(P.CODE))
                                      .where(CATEGORY.CODE.between("10", "12"))
                                      .list();

        assertThat(list.toStrings()).containsOnly("parent:Cat10", "parent:Cat11", "parent:Cat12");

        final Map<String, String> parents = new HashMap<>();
        select(P.NAME, CATEGORY.NAME).from(CATEGORY)
            .join(P, CATEGORY.PARENT_CODE.eq(P.CODE))
            .where(CATEGORY.CODE.between("10", "12"))
            .orderBy(CATEGORY.NAME)
            .forEachWhile(t -> {
                final String key = t.get(CATEGORY.NAME);
                parents.put(key, t.get(P.NAME));
                return !"Cat11".equals(key);
            });

        assertThat(parents).contains(entry("Cat10", "parent"), entry("Cat11", "parent"));

        final Option<String> name = select(P.NAME, CATEGORY.NAME).from(CATEGORY)
                                    .join(P, CATEGORY.PARENT_CODE.eq(P.CODE))
                                    .where(CATEGORY.CODE.between("10", "12"))
                                    .orderBy(CATEGORY.NAME)
                                    .forEachReturning(t -> {
                final String key = t.get(CATEGORY.NAME);
                return "Cat11".equals(key) ? StepResult.done(notNull(t.get(P.NAME))) : StepResult.next();
            });

        assertThat(name.orElse("")).isEqualTo("parent");

        final Map<String, String> parents2 = new HashMap<>();

        select(P.NAME, CATEGORY.NAME).from(CATEGORY).join(P, CATEGORY.PARENT_CODE.eq(P.CODE)).where(CATEGORY.CODE.between("10", "12")).forEach(t ->
                parents2.put(t.get(CATEGORY.NAME), t.get(P.NAME)));

        assertThat(parents2).contains(entry("Cat10", "parent"), entry("Cat11", "parent"), entry("Cat12", "parent"));

        final Category category = parentCategory.getChildren().get(0);
        categoryForUpdate(category).setName("xx").persist();
        assertThat(Category.findOrFail(category.getCode()).getName()).isEqualTo("xx");
    }  // end method selfReferenceTest

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static Category addCategory(String code, String name) {
        return addCategory(code, name, null);
    }
    private static Category addCategory(String code, String name, @Nullable Category parent) {
        return categoryForUpdate(code).setName(name).setParent(parent).insert();
    }

    //~ Inner Classes ................................................................................................................................

    static class C {
        String name       = "";
        String parentName = "";

        public String toString() {
            return parentName + ":" + name;
        }
    }
}  // end class ReferenceTest
