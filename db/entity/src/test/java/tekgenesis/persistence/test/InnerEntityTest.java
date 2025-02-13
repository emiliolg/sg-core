
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.TableField;
import tekgenesis.persistence.TableMetadata;
import tekgenesis.test.basic.*;

import static java.util.Comparator.comparingInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.test.basic.g.CartTable.CART;
import static tekgenesis.test.basic.g.FeatureByCatTable.FEATURE_BY_CAT;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class InnerEntityTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    @Rule public DbRule      db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void copy() {
        runInTransaction(() -> {
            final IntIntTuple t                = initializeEntities();
            final int         code             = t.first();
            final int         classificationId = t.second();
            final Categ       category         = assertNotNull(Categ.find(code, classificationId));

            assertThat(category.getFeatures()).hasSize(1);

            final Categ newCategory = Categ.create(1111, classificationId);
            assertThat(equalFields(category, newCategory)).isFalse();
            TableMetadata.copyFields(category, newCategory);
            TableMetadata.copyInners(category, newCategory);
            newCategory.persist();

            final Categ storedCategory = assertNotNull(Categ.find(1111, classificationId));
            assertThat(equalFields(newCategory, storedCategory)).isTrue();

            assertThat(storedCategory.getName()).isEqualTo(category.getName());

            final InnerEntitySeq<FeatureByCat> features = storedCategory.getFeatures();
            assertThat(features).hasSize(1);
            final FeatureByCat f1 = features.get(0);
            assertThat(f1.getFeature().getName()).isEqualTo("Feature");

            final InnerEntitySeq<FeatureValue> validValues = f1.getValidValues();

            assertThat(validValues).hasSize(1);
            assertThat(validValues.get(0).getValue()).isEqualTo("Feature Value");
        });
    }
    @Test public void persistTwice() {
        runInTransaction(() -> {
            final Classification classification = Classification.create().setName("Classification").insert();

            final Feature feature = Feature.create().setName("Feature").insert();

            final Categ category = Categ.create(1234, classification.getId()).setName("Categ").insert();

            FEATURE_BY_CAT.metadata().createInstance(Tuple.createAppending(category.keyObject(), 1)).setFeature(feature).persist();

            assertThat(category.getFeatures().get(0).seqId()).isEqualTo(1);
            category.persist();

            final Feature feature2 = Feature.create().setName("Feature2").insert();

            category.getFeatures().add().setFeature(feature2).persist();
            assertThat(FeatureByCat.list().count()).isEqualTo(2);
            category.persist();
            assertThat(category.getFeatures().map(FeatureByCat::seqId)).containsExactly(1, 2);
        });
    }

    @Test public void referenceSequence() {
        runInTransaction(() -> {
            final IntIntTuple t                = initializeEntities();
            final int         code             = t.first();
            final int         classificationId = t.second();

            final Categ storedCategory = Categ.find(code, classificationId);
            assert storedCategory != null;
            final InnerEntitySeq<FeatureByCat> storedCategoryFeatures = storedCategory.getFeatures();
            assertThat(storedCategoryFeatures).hasSize(1);

            final FeatureByCat                 storedCategoryFeature = storedCategoryFeatures.getFirst().get();
            final InnerEntitySeq<FeatureValue> storedFeatureValues   = storedCategoryFeature.getValidValues();
            assertThat(storedFeatureValues).hasSize(1);

            final FeatureValue storedFeatureValue = storedFeatureValues.getFirst().get();
            assertThat(storedFeatureValue.getValue()).isEqualTo("Feature Value");
        });
    }

    @Test public void removeOneOfOne() {
        runInTransaction(() -> {
            final IntIntTuple t                = initializeEntities();
            final int         code             = t.first();
            final int         classificationId = t.second();
            final Categ       storedCategory   = assertNotNull(Categ.find(code, classificationId));
            assertThat(storedCategory.getFeatures()).hasSize(1);

            storedCategory.getFeatures().filter(f -> "Feature".equals(f.getFeature().getName())).toList().forEach(FeatureByCat::delete);
            storedCategory.getFeatures().persist();

            assertThat(storedCategory.getFeatures().size()).isZero();
        });
    }

    @Test public void removeTwoOfThree() {
        runInTransaction(() -> {
            final IntIntTuple t                = initializeEntities();
            final int         code             = t.first();
            final int         classificationId = t.second();

            final Categ category = assertNotNull(Categ.find(code, classificationId));

            final Feature feature = Feature.create();
            feature.setName("Another Feature");
            feature.insert();

            category.getFeatures().add().setFeature(feature);

            final Feature feature2 = Feature.create();
            feature2.setName("Another Feature 2");
            feature2.insert();

            final FeatureByCat featureByCat2 = category.getFeatures().add();
            featureByCat2.setFeature(feature2);
            category.getFeatures().persist();

            assertThat(category.getFeatures().size()).isEqualTo(3);

            category.getFeatures().filter(f -> "Another Feature".equals(f.getFeature().getName())).toList().forEach(FeatureByCat::delete);
            category.persist();

            final Categ                        find     = assertNotNull(Categ.find(code, classificationId));
            final InnerEntitySeq<FeatureByCat> features = find.getFeatures();
            assertThat(features.size()).isEqualTo(2);
            final FeatureByCat cat = features.get(1);
            assertThat(cat.getFeature().getName()).isEqualTo("Another Feature 2");

            final Feature newFeature = Feature.create();
            newFeature.setName("New Feature");
            features.add().setFeature(newFeature);
            newFeature.insert();

            assertThat(features.size()).isEqualTo(3);
            assertThat(features.get(2).getSeqId()).isEqualTo(4);
        });
    }  // end method removeTwoOfThree

    @Test public void removeTwoOfTwo() {
        runInTransaction(() -> {
            final IntIntTuple t                = initializeEntities();
            final int         code             = t.first();
            final int         classificationId = t.second();

            final Feature feature = Feature.create();
            feature.setName("Another Feature");
            final InnerEntitySeq<FeatureByCat> features = assertNotNull(Categ.find(code, classificationId)).getFeatures();
            features.add().setFeature(feature);
            feature.insert();

            assertThat(features.size()).isEqualTo(2);
            assertThat(features.get(1).getSeqId()).isEqualTo(2);

            features.filter(f -> "Another Feature".equals(f.getFeature().getName())).toList().forEach(FeatureByCat::delete);
            features.persist();
            assertThat(assertNotNull(Categ.find(code, classificationId)).getFeatures()).hasSize(1);
        });
    }
    @Test public void testInnerUniqueness() {
        final Category category = invokeInTransaction(() -> {
                final Category cat = Category.create(1).setName("LED").setDescr("LED").insert();

                final Cart cart = Cart.create();

                final Product p1 = createProduct("1", cat);
                final Product p2 = createProduct("2", cat);
                final Product p3 = createProduct("3", cat);

                assertThat(cart.getItems().isUndefined()).isTrue();
                assertThat(cart.getItems().toString()).startsWith("undef");
                assertThat(cart.getItems().isEmpty()).isTrue();

                assertThat(cart.getItems().isUndefined()).isFalse();

                cart.getItems().merge(listOf(p1, p2, p3), (c, p) -> c.setQuantity(1).setSalableProduct(p));
                assertThat(cart.getItems().toString()).isEqualTo("(-1 1, -1 2, -1 3)");

                cart.persist();
                return cat;
            });

        final Cart cart = invokeInTransaction(t -> {
                final Cart c = assertNotNull(selectFrom(CART).get());

                final Product p2 = Product.findOrFail("2");
                final Product p3 = Product.findOrFail("3");
                final Product p4 = createProduct("4", category);

                c.getItems()
                 .mergeMatching(listOf(tuple(p3, 1), tuple(p2, 100), tuple(p4, 2)),  //
                     (ci, v) -> ci.getSalableProduct().equals(v._1()),
                     (ci, v) -> ci.setQuantity(v._2()).setSalableProduct(v._1()));

                c.update();

                final InnerEntitySeq<CartItem> cartItems = assertNotNull((selectFrom(CART).get())).getItems();
                assertThat(cartItems.get(0).getSalableProductProductId()).isEqualTo("Product2");
                assertThat(cartItems.get(0).getQuantity()).isEqualTo(100);
                assertThat(cartItems.get(1).getSalableProductProductId()).isEqualTo("Product3");
                assertThat(cartItems.get(1).getQuantity()).isEqualTo(1);
                assertThat(cartItems.get(2).getSalableProductProductId()).isEqualTo("Product4");
                assertThat(cartItems.get(2).getQuantity()).isEqualTo(2);
                t.abort();
                return c;
            });
        runInTransaction(() -> {
            final Product p2 = Product.findOrFail("2");
            final Product p3 = Product.findOrFail("3");
            final Product p4 = Product.findOrFail("4");
            cart.getItems()
                .updateMatching(listOf(tuple(p3, 1), tuple(p2, 100), tuple(p4, 2)),  //
                    (ci, v) -> ci.getSalableProduct().equals(v._1()),
                    (ci, v) -> ci.setQuantity(v._2()).setSalableProduct(v._1()));

            cart.update();

            final InnerEntitySeq<CartItem> cartItems2 = assertNotNull((selectFrom(CART).get())).getItems();
            assertThat(cartItems2.get(0).getSalableProductProductId()).isEqualTo("Product1");
            assertThat(cartItems2.get(0).getQuantity()).isEqualTo(1);
            assertThat(cartItems2.get(1).getSalableProductProductId()).isEqualTo("Product2");
            assertThat(cartItems2.get(1).getQuantity()).isEqualTo(100);
            assertThat(cartItems2.get(2).getSalableProductProductId()).isEqualTo("Product3");
            assertThat(cartItems2.get(2).getQuantity()).isEqualTo(1);
        });
    }  // end method testInnerUniqueness
    @Test public void testMergeAndDelete() {
        runInTransaction(() -> {
            final Category category = Category.create(1);
            category.setName("LED");
            category.setDescr("LED");
            category.insert();

            final Cart cart1 = Cart.create();
            cart1.persist();
            final Product p1 = createProduct("1", category);
            final Product p2 = createProduct("2", category);
            final Product p3 = createProduct("3", category);

            cart1.getItems().merge(listOf(p1, p2, p3), (c, p) -> c.setQuantity(1).setSalableProduct(p));
            cart1.getItems().get(1).delete();
            cart1.persist();
            assertThat(CartItem.list().map(CartItem::getQuantity).toList()).containsExactly(1, 1);
        });
    }
    @Test public void testMergeAndInsert() {
        runInTransaction(() -> {
            final Category category = Category.create(1);
            category.setName("LED");
            category.setDescr("LED");
            category.insert();

            final Cart cart1 = Cart.create();
            cart1.persist();
            final Product p1 = createProduct("1", category);
            final Product p2 = createProduct("2", category);
            final Product p3 = createProduct("3", category);

            cart1.getItems().merge(listOf(p1, p2, p3), (c, p) -> c.setQuantity(1).setSalableProduct(p));
            final CartItem item = cart1.getItems().add().setSalableProduct(p1).setQuantity(10).insert();
            assertThat(CartItem.list().map(CartItem::getQuantity).toList()).containsExactly(10);
            cart1.persist();
            assertThat(CartItem.list().sorted(comparingInt(CartItem::seqId)).map(CartItem::getQuantity)).containsExactly(1, 1, 1, 10);
            try {
                item.insert();
                failBecauseExceptionWasNotThrown(IllegalStateException.class);
            }
            catch (final IllegalStateException e) {
                assertThat(e.toString()).contains("Inserting existing");
            }
        });
    }
    @Test public void testMergeAndPersist() {
        runInTransaction(() -> {
            final Category category = Category.create(1);
            category.setName("LED");
            category.setDescr("LED");
            category.insert();

            final Cart cart1 = Cart.create();
            cart1.persist();
            final Product p1 = createProduct("1", category);
            final Product p2 = createProduct("2", category);
            final Product p3 = createProduct("3", category);

            cart1.getItems().merge(listOf(p1, p2, p3), (c, p) -> c.setQuantity(1).setSalableProduct(p));
            final CartItem item = cart1.getItems().add().setSalableProduct(p1).setQuantity(10).persist();
            assertThat(CartItem.list().map(CartItem::getQuantity).toList()).containsExactly(10);
            cart1.persist();
            assertThat(CartItem.list().sorted(comparingInt(CartItem::seqId)).map(CartItem::getQuantity)).containsExactly(1, 1, 1, 10);
            item.setQuantity(100).persist();
            assertThat(CartItem.list().sorted(comparingInt(CartItem::seqId)).map(CartItem::getQuantity)).containsExactly(1, 1, 1, 100);
            cart1.getItems().merge(listOf(p1, p2, p3), (c, p) -> c.setQuantity(1000));
            assertThat(cart1.getItems().map(CartItem::getQuantity)).containsExactly(1000, 1000, 1000);
            cart1.invalidate();
            assertThat(cart1.getItems().map(CartItem::getQuantity)).containsExactly(1, 1, 1, 100);
        });
    }
    @Test public void testRepeatedMerge() {
        runInTransaction(() -> {
            final Category category = Category.create(1);
            category.setName("LED");
            category.setDescr("LED");
            category.insert();

            final Cart cart1 = Cart.create();

            final Product p1 = createProduct("1", category);
            final Product p2 = createProduct("2", category);
            final Product p3 = createProduct("3", category);
            final Product p4 = createProduct("4", category);
            final Product p5 = createProduct("5", category);

            cart1.getItems().merge(listOf(p1, p2, p3), (c, p) -> c.setQuantity(1).setSalableProduct(p));

            cart1.getItems().merge(listOf(p1, p2), (c, p) -> c.setQuantity(c.getQuantity() + 1));
            assertThat(cart1.getItems().map(CartItem::getQuantity).toList()).containsExactly(2, 2);

            cart1.getItems().merge(listOf(p1, p2, p3, p4), (c, p) -> c.setQuantity(c.getQuantity() + 1).setSalableProduct(p));
            assertThat(cart1.getItems().map(CartItem::getSeqId)).containsExactly(1, 2, 3, 4);

            cart1.persist();
            assertThat(CartItem.list().map(CartItem::getQuantity).toList()).containsExactly(3, 3, 1, 1);
            cart1.getItems()
                 .mergeMatching(listOf(p3, p4, p5),
                     (c, p) -> c.getSalableProduct().equals(p),
                     (c, p) -> c.setQuantity(c.getQuantity() + 1).setSalableProduct(p));
            assertThat(cart1.getItems().map(CartItem::getQuantity)).containsExactly(2, 2, 1);
            assertThat(cart1.getItems().map(CartItem::getSeqId)).containsExactly(3, 4, 5);
            cart1.persist();
            assertThat(cart1.getItems().map(CartItem::getQuantity)).containsExactly(2, 2, 1);
            assertThat(cart1.getItems().map(CartItem::getSeqId)).containsExactly(3, 4, 5);

            cart1.getItems()
                 .mergeMatching(listOf(p1, p2, p4),
                     (cartItem, product) -> cartItem.getSalableProduct().equals(product),
                     (cartItem, product) -> cartItem.setQuantity(9),
                     cartItem -> p3.equals(cartItem.getSalableProduct()),
                     (cartItemSupplier, product) -> {
                         if (product.equals(p1)) cartItemSupplier.get().setSalableProduct(p1).setQuantity(99);
                     });
            assertThat(cart1.getItems().map(item -> item.getSalableProductProductId() + ":" + item.getQuantity())).containsExactly("Product4:9",
                "Product5:1",
                "Product1:99");
        });
    }  // end method testRepeatedMerge

    private IntIntTuple initializeEntities() {
        final Classification classification = Classification.create();
        classification.setName("Classification");
        classification.insert();

        final Feature feature = Feature.create();
        feature.setName("Feature");
        feature.insert();

        final Categ category = Categ.create(1234, classification.getId());
        category.setName("Categ");

        final FeatureByCat categoryFeature = category.getFeatures().add();
        categoryFeature.setFeature(feature);
        categoryFeature.getValidValues().add().setValue("Feature Value");

        category.insert();

        final int code             = category.getCode();
        final int classificationId = category.getClassificationId();
        return tuple(code, classificationId);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
    private static Product createProduct(String id, Category category) {
        final Product product = Product.create("Product" + id);
        product.setModel("model");
        product.setDescription("description");
        product.setState(State.ACTIVE);
        product.setPrice(BigDecimal.valueOf(100));
        product.setCategory(category);
        product.insert();
        return product;
    }
    /** Compare all fields from one instance to a another. Returns true if all are equal */
    private static <T extends EntityInstance<T, K>, K> boolean equalFields(T from, T to) {
        final TableMetadata<T, K> md = from.metadata();
        for (final TableField<?> f : md.getFields()) {
            if (md.getUpdateTimeField() != f && !equal(f.getValue(from), f.getValue(to))) return false;
        }
        return true;
    }
}  // end class InnerEntityTest
