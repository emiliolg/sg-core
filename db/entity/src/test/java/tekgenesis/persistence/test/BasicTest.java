
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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.Sql;
import tekgenesis.persistence.exception.EntityNotFoundException;
import tekgenesis.test.basic.*;
import tekgenesis.test.basic.g.CategoryBase;
import tekgenesis.test.basic.g.CategoryTable;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.ifPresent;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.test.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.test.basic.g.CustomerTable.CUSTOMER;
import static tekgenesis.test.basic.g.ProductTable.PRODUCT;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class BasicTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DbRule db = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void basicApp() {
        // Create 2 categories;
        runInTransaction(() -> {
            createCategory(1, "Category-1", "Category-1 Description");
            createCategory(2, "Category-2", "Category-2 Description");
        });

        runInTransaction(() -> {
            // Check that they are there, delete the first;
            assertThat(Category.list().count()).isEqualTo(2);

            assertThat(db.sqlStatement("select * from QName(BASIC_TEST, CATEGORY)").list(Category.class))  //
            .extracting("name").containsExactly("Category-1", "Category-2");

            assertThat(selectFrom(CATEGORY).count()).isEqualTo(2);
            assertThat(selectFrom(CATEGORY).where(CATEGORY.NAME.eq("Category-1")).count()).isEqualTo(1);
        });
        // delete the first;

        runInTransaction(() -> {
            final Category c2 = Category.find(2);
            if (c2 != null) c2.delete();
        });

        // Check again;
        runInTransaction(() -> {
            assertThat(selectFrom(CATEGORY).exists()).isTrue();
            assertThat(selectFrom(CATEGORY).count()).isEqualTo(1);
        });

        // Create a product;
        final String productId = "12345678";

        runInTransaction(() ->
                Product.create(productId)
                       .setModel("Product 12345678")
                       .setDescription("Product 12345678 Description")
                       .setState(State.DISCONTINUED)
                       .setPrice(BigDecimal.valueOf(5.5))
                       .setCategory(assertNotNull(Category.find(1)))
                       .insert());

        // Find it check it, and change the state;
        runInTransaction(() -> {
            final Product p1 = assertNotNull(Product.find(productId));
            assertThat(p1.getModel()).isEqualTo("Product 12345678");
            assertThat(p1.getDescription()).isEqualTo("Product 12345678 Description");
            assertThat(p1.getState()).isEqualTo(State.DISCONTINUED);
            assertThat(p1.getCategoryIdKey()).isEqualTo(1);
            assertThat(p1.getCategory().getName()).isEqualTo("Category-1");
            p1.setState(State.ACTIVE);

            p1.update();
        });

        // Check it again;
        runInTransaction(() -> {
            final Product p2 = assertNotNull(Product.find(productId));
            assertThat(p2.getModel()).isEqualTo("Product 12345678");
            assertThat(p2.getDescription()).isEqualTo("Product 12345678 Description");
            assertThat(p2.getState()).isEqualTo(State.ACTIVE);
        });

        // Test Check and lock
        runInTransaction(() -> {
            final Product  p3 = assertNotNull(Product.findPersisted(productId));
            final DateTime d  = p3.getUpdateTime();
            assertThat(p3.et().checkAndLock(productId, d.addSeconds(1))).isFalse();
            assertThat(p3.et().checkAndLock(productId, d)).isTrue();
        });
    }  // end method basicApp
    @Test public void changeCategory() {
        // Create 2 categories;
        runInTransaction(() -> {
            createCategory(1, "Category-1", "Category-1 Description");
            createCategory(2, "Category-2", "Category-2 Description");
        });

        // Check that they are there, delete the first;
        runInTransaction(() -> assertThat(selectFrom(CATEGORY).count()).isEqualTo(2));
        final Category category = invokeInTransaction(() -> assertNotNull(Category.find(1)));

        // Create a product;
        final Product p1 = invokeInTransaction(() -> {
                final Product p = Product.create("12345678")
                                         .setModel("Product 12345678")
                                         .setDescription("Product 12345678 Description")
                                         .setState(State.DISCONTINUED)
                                         .setPrice(BigDecimal.valueOf(5.5))
                                         .setCategory(category)
                                         .insert();
                Product.create("1234567")
                       .setModel("Product 1234567")
                       .setDescription("Product 1234567 Description")
                       .setState(State.DISCONTINUED)
                       .setPrice(BigDecimal.valueOf(5.5))
                       .setCategory(category)
                       .insert();
                return p;
            });

        runInTransaction(() -> {
            assertThat(category.getProducts()).hasSize(2);
            // Find it check it, and change the state;
            // final Product p1 = assertNotNull(Product.find("12345678"));
            assertThat(p1.getModel()).isEqualTo("Product 12345678");
            assertThat(p1.getDescription()).isEqualTo("Product 12345678 Description");
            assertThat(p1.getState()).isEqualTo(State.DISCONTINUED);
            assertThat(p1.getCategoryIdKey()).isEqualTo(1);
            assertThat(p1.getCategory().getName()).isEqualTo("Category-1");

            p1.setState(State.ACTIVE);
            p1.setCategory(assertNotNull(Category.find(2)));
            p1.update();
            // db.commitTransaction();

            assertThat(category.getProducts()).hasSize(1);
            assertThat(assertNotNull(Category.find(2)).getProducts()).hasSize(1);
            // Check it again;
        });
    }  // end method changeCategory

    @Test public void emptyKey() {
        runInTransaction(() -> {
            final Category category = createCategory(1, "Category-1", "Category-1 Description");
            category.persist();
            Product.create("  ")
                   .setModel("Product 12345678")
                   .setDescription("Product 12345678 Description")
                   .setState(State.DISCONTINUED)
                   .setPrice(BigDecimal.valueOf(5.5))
                   .setCategory(category)
                   .insert()
                   .persist();
        });

        runInTransaction(() -> {
            final Product product = assertNotNull(selectFrom(PRODUCT).get());
            product.setPrice(BigDecimal.ONE);
            product.persist();
        });
    }

    @Test public void emptyStrings() {
        runInTransaction(() -> createCategory(1, "Category-1", ""));

        final Category category = invokeInTransaction(() -> assertNotNull(Category.find(1)));

        assertThat(category.getDescr()).isEmpty();

        runInTransaction(category::update);

        runInTransaction(() -> assertThat(assertNotNull(Category.find(1)).getDescr()).isEmpty());
    }

    @Test public void enumKey() {
        // Create 2 Customers;
        runInTransaction(() -> {
            createCustomer(DocType.DNI, 100, Sex.F, "Mary", "Smith", "Mary");
            createCustomer(DocType.DNI, 101, Sex.M, "John", "Smith", "J");
            createCustomer(DocType.PASS, 101, Sex.M, "Roger", "Penrose", "Roger");
        });

        runInTransaction(() -> {
            final Customer c = Customer.find(DocType.DNI, BigDecimal.valueOf(101), Sex.M);
            assertThat(assertNotNull(c).getFirstName()).isEqualTo("John");

            final ImmutableList<Customer> customers = Customer.list(asList("DNI:100:F", "DNI:101:M", "DNI:900:M")).toList();

            assertThat(customers).hasSize(2);

            assertThat(selectFrom(CUSTOMER).count()).isEqualTo(3);
            assertThat(selectFrom(CUSTOMER).where(CUSTOMER.DOCUMENT_TYPE.eq(DocType.DNI)).list()).hasSize(2);
        });
    }

    @Test public void innerEntities() {
        // Create a Category and a product;
        final Product product = invokeInTransaction(() -> {
                final Category category = createCategory(1, "Category-1", "Category-1 Description");

                final Product p = Product.create("12345678")
                                         .setModel("Product 12345678")
                                         .setDescription("Product 12345678 Description")
                                         .setState(State.DISCONTINUED)
                                         .setPrice(BigDecimal.valueOf(5.5))
                                         .setCategory(category)
                                         .insert();

                // Create a Customer;
                createCustomer(DocType.DNI, 123456789, Sex.M, "First Name One", "Last Name One", "Nick Name One");
                return p;
            });

        // Check it;
        runInTransaction(() -> {
            final Customer customer = assertNotNull(Customer.find(DocType.DNI, BigDecimal.valueOf(123456789), Sex.M));
            assertThat(customer.getFirstName()).isEqualTo("First Name One");
            assertThat(customer.getLastName()).isEqualTo("Last Name One");
            assertThat(customer.getNickname()).isEqualTo("Nick Name One");

            // Create an Invoice. Add an item;
            final Invoice invoice = Invoice.create(1).setInvoiceDate(DateOnly.current()).setCustomer(customer).insert();

            final Item item = invoice.getItems().add();
            item.setProduct(product);
            item.setQuantity(1);
            item.setDiscount(0);
            invoice.update();
        });

        runInTransaction(() -> assertThat(assertNotNull(Invoice.find(1)).getItems()).hasSize(1));
    }

    @Test public void listKeys() {
        // Use sql;
        final Set<Integer> keys = new LinkedHashSet<>();
        runInTransaction(() -> {
            for (int i = 1; i <= 10030; i++) {
                createCategory(i, "Category-" + i, "Category-" + i + " Description");
                keys.add(i);
            }
        });

        runInTransaction(() -> {
            // Check that they are there, delete the first;
            assertThat(selectFrom(CATEGORY).count()).isEqualTo(10030);

            assertThat(Category.list(keys)).hasSize(10030);

            final Predicate<Category> is523 = c -> c != null && c.getIdKey() == 523;

            assertThat(Category.list(keys).getFirst(is523).get().getIdKey()).isEqualTo(523);

            keys.clear();
            keys.add(1);
            keys.add(10);
            assertThat(CategoryBase.list(keys)).hasSize(2);
        });
    }

    @Test public void sequence() {
        runInTransaction(() -> {
            // Create 2 customers;
            createCustomer(DocType.DNI, 123456789, Sex.M, "First Name One", "Last Name One", "Nick Name One");
            createCustomer(DocType.DNI, 987654321, Sex.M, "First Name Two", "Last Name Two", "Nick Name Two");
        });

        // Check them;
        runInTransaction(() -> {
            // Check it;
            final Customer c = assertNotNull(Customer.find(DocType.DNI, BigDecimal.valueOf(123456789), Sex.M));
            assertThat(c.getFirstName()).isEqualTo("First Name One");
            assertThat(c.getLastName()).isEqualTo("Last Name One");

            assertThat(c.getNickname()).isEqualTo("Nick Name One");
            final Customer customerOne = ensureNotNull(Customer.find(DocType.DNI, BigDecimal.valueOf(123456789), Sex.M));
            final Customer customerTwo = ensureNotNull(Customer.find(DocType.DNI, BigDecimal.valueOf(987654321), Sex.M));

            // Create a couple of preferences;
            final Preferences preferences1 = createPreferences(customerOne, "mail1@host.com", "#mail1");
            final Preferences preferences2 = createPreferences(customerTwo, "mail2@host.com", "#mail2");
            assertThat(preferences1.getId()).isLessThan(preferences2.getId());
        });
    }

    @Test public void testFinds()
        throws Exception
    {
        // integer key

        final Category category = invokeInTransaction(() -> createCategory(1, "Category-1", "Category-1 Description"));

        runInTransaction(() -> {
            assertThat(Category.find(category.keyObject())).isEqualTo(category);
            assertThat(Category.findOrFail(category.keyObject())).isEqualTo(category);
            assertThat(Category.findPersisted(category.keyObject())).isEqualTo(category);
            assertThat(Category.findPersistedOrFail(category.keyObject())).isEqualTo(category);

            failedFind(() -> Category.findOrFail(category.keyObject() + 1));
            failedFind(() -> Category.findPersistedOrFail(category.keyObject() + 1));
        });

        // String key
        final Product product = invokeInTransaction(() ->
                    Product.create("  ")
                           .setModel("Product 12345678")
                           .setDescription("Product 12345678 Description")
                           .setState(State.DISCONTINUED)
                           .setPrice(BigDecimal.valueOf(5.5))
                           .setCategory(category)
                           .insert());

        runInTransaction(() -> {
            assertThat(Product.find(product.keyObject())).isEqualTo(product);
            assertThat(Product.findOrFail(product.keyObject())).isEqualTo(product);
            assertThat(Product.findPersisted(product.keyObject())).isEqualTo(product);
            assertThat(Product.findPersistedOrFail(product.keyObject())).isEqualTo(product);

            failedFind(() -> Product.findOrFail("pepe"));
            failedFind(() -> Product.findPersistedOrFail("pepe"));
        });
        // Composed key
        final Customer customer = invokeInTransaction(() ->
                    createCustomer(DocType.DNI, 987654321, Sex.M, "First Name Two", "Last Name Two", "Nick Name Two"));

        runInTransaction(() -> {
            assertThat(Customer.find(customer.keyObject())).isEqualTo(customer);
            assertThat(Customer.findOrFail(customer.keyObject())).isEqualTo(customer);
            assertThat(Customer.findPersisted(customer.keyObject())).isEqualTo(customer);
            assertThat(Customer.findPersistedOrFail(customer.keyObject())).isEqualTo(customer);
        });

        failedFind(() -> Customer.findOrFail(Tuple.tuple(DocType.DNI, BigDecimal.valueOf(99888777), Sex.M)));
        failedFind(() -> Customer.findPersistedOrFail(Tuple.tuple(DocType.DNI, BigDecimal.valueOf(99888777), Sex.M)));
    }  // end method testFinds
    @Test public void testOptimistickLock() {
        runInTransaction(() -> createCategory(1, "Category-1", "Category-1 Description"));

        final Category category = invokeInTransaction(() -> {
                final Category cat = Category.find(1);

                assert cat != null;
                cat.setDescr("AAAA");
                cat.forceUpdate();
                return cat;
            });

        runInTransaction(() ->
                Sql.update(CategoryTable.CATEGORY)
                   .set(CATEGORY.INSTANCE_VERSION, category.getInstanceVersion() + 1)
                   .where(CATEGORY.ID_KEY.eq(category.getIdKey()))
                   .execute());

        try {
            runInTransaction(() -> {
                category.setDescr("BBBB");
                category.update();
            });

            failBecauseExceptionWasNotThrown(RuntimeException.class);
        }
        catch (final RuntimeException ignore) {}

        runInTransaction(() -> {
            final Category category1 = Category.find(1);
            assert category1 != null;
            assertThat(category1.getDescr()).isEqualTo("AAAA");
        });
    }

    @SuppressWarnings("Duplicates")
    @Test public void testOptimistickLockBatchOk() {
        runInTransaction(() -> {
            createCategory(1, "Category-1", "Category-1 Description");
            createCategory(3, "Category-3", "Category-3 Description");
        });

        runInTransaction(t -> {
            t.beginBatch();
            Category.findOrFail(1).setDescr("AAAA").forceUpdate();
            ifPresent(Category.find(3), Category::delete);
            Sql.update(CategoryTable.CATEGORY)
               .set(CATEGORY.INSTANCE_VERSION, Category.findOrFail(1).getInstanceVersion() + 1)
               .where(CATEGORY.ID_KEY.eq(100))
               .execute();
            createCategory(2, "Category-2", "Category-2 Description").setDescr("BBBB").update();
        });
    }

    //~ Methods ......................................................................................................................................

    public static Category createCategory(int code, @Nullable String nm, String desc) {
        final Category category = Category.create(code);
        category.setName(nm);
        category.setDescr(desc);
        category.insert();
        return category;
    }

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
    private static Customer createCustomer(DocType dt, int docno, Sex sex, String firstName, String lastName, String nickName) {
        final Customer customer = Customer.create(dt, BigDecimal.valueOf(docno), sex);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setNickname(nickName);

        customer.insert();
        return customer;
    }

    private static Preferences createPreferences(Customer customer, String mail, String tweeter) {
        final Preferences preferences = Preferences.create();
        preferences.setCustomer(customer);
        preferences.setMail(mail);
        preferences.setTwitter(tweeter);
        preferences.setDigest(MailDigest.DAILY);
        preferences.insert();
        return preferences;
    }

    private static void failedFind(@NotNull Runnable test) {
        try {
            runInTransaction(test);
            failBecauseExceptionWasNotThrown(EntityNotFoundException.class);
        }
        catch (final EntityNotFoundException ignore) {}
    }
}  // end class BasicTest
