
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.authorization.*;
import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.form.ReflectedFormInstance;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.Widget.ElemType;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.sales.basic.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.tools.test.Tests.checkDiff;
import static tekgenesis.form.ServerUiModelRetriever.getRetriever;
import static tekgenesis.form.etl.FormEtlImpl.FormInput;
import static tekgenesis.form.etl.FormEtlImpl.FormOutput;
import static tekgenesis.sales.basic.g.PaymentTypeTable.PAYMENT_TYPE;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class FormJsonSerializerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    public DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION, DbRule.BASIC) {
            @Override protected void before() {
                createDatabase(dbName);
                System.setProperty(SUIGEN_DEVMODE, "false");
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    //~ Methods ......................................................................................................................................

    @Test public void jsonSerializationDeserializationForCustomerForm() {
        final String customerKey = invokeInTransaction(() ->
                    Customer.create(DocType.DNI, new BigDecimal(28808366L), Sex.M)
                            .setFirstName("John")
                            .setLastName("Doe")
                            .setNickname("Jdoe")
                            .insert()
                            .keyAsString());

        runInTransaction(() -> {
            final FormTest form = new FormTest("tekgenesis.sales.basic.CustomerForm", customerKey);
            form.serializeAndAssert();
            form.deserializeAndAssert();
        });
    }

    @Test public void jsonSerializationDeserializationForInvoiceForm() {
        final String invoiceKey = invokeInTransaction(() -> {
                final Category category = Category.create(1);
                category.setName("Category 1");
                category.setDescr("Some Description");
                category.insert();

                final Product product = Product.create("Nexus7");
                product.setCategory(category);
                product.setDescription("A description");
                product.setPrice(new BigDecimal(2048));
                product.setState(State.CREATED);
                product.setModel("Tablet");
                product.insert();

                final Customer customer = Customer.create(DocType.DNI, new BigDecimal(28808366L), Sex.M);
                customer.setFirstName("John");
                customer.setLastName("Doe");
                customer.setNickname("Jdoe");
                customer.insert();

                final PaymentType paymentType = PaymentType.create();
                PAYMENT_TYPE.ID.setValue(paymentType, 1);
                paymentType.setType(PaymentOption.CASH);
                paymentType.setDescription("Desc");
                paymentType.setSubtype("ARS");

                paymentType.insert(1);

                final Invoice invoice = Invoice.create(1);
                invoice.setCustomer(customer);
                invoice.setInvoiceDate(DateOnly.date(2013, 1, 1));

                final Item item = invoice.getItems().add();
                item.setDiscount(0);
                item.setQuantity(1);
                item.setProductProductId("Nexus7");

                final InnerEntitySeq<Payment> payments = invoice.getPayments();
                final Payment                 payment  = payments.add();
                payment.setAmount(new BigDecimal(2048));
                payment.setPayment(paymentType);

                return invoice.insert().keyAsString();
            });
        runInTransaction(() -> {
            final FormTest form = new FormTest("tekgenesis.sales.basic.InvoiceForm", invoiceKey);
            form.serializeAndAssert();
            form.deserializeAndAssert();
        });
    }  // end method jsonSerializationDeserializationForInvoiceForm

    // CustomerService
    @Test public void jsonSerializationDeserializationForOptionalValues() {
        runInTransaction(() -> {
            final FormTest form = new FormTest("tekgenesis.sales.cart.CustomerService", null);
            form.goldenModel.setValue(0, "28808366");
            form.goldenModel.setValue(1, "juan");
            form.goldenModel.setValue(2, "Demo User");

            form.serializeAndAssert();
            form.deserializeAndAssert();
        });
    }

    @Test public void jsonSerializationDeserializationForProductForm() {
        final String productKey = invokeInTransaction(() -> {
                final Category category = Category.create(1).setName("Category 1").setDescr("Some Description").insert();
                return Product.create("Nexus7")
                              .setCategory(category)
                              .setDescription("A description")
                              .setPrice(new BigDecimal(2048))
                              .setState(State.CREATED)
                              .setModel("Tablet")
                              .insert()
                              .keyAsString();
            });

        runInTransaction(() -> {
            final FormTest form = new FormTest("tekgenesis.sales.basic.ProductForm", productKey);
            form.serializeAndAssert();
            form.deserializeAndAssert();
        });
    }

    @Test public void jsonSerializationDeserializationForUserFormWtables()
        throws IOException
    {
        final String userKey = invokeInTransaction(() -> {
                final OrgUnit ou = OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU);

                final Property prop = Property.create("prop1", "customize", PropertyType.BOOLEAN)
                                              .setRequired(true)
                                              .setScope(PropertyScope.USER)
                                              .insert();

                Role.create("role1").setName("Role A").insert();

                final User user = User.create("demouser").setLocale("EN").setName("Demo User").setDefaultOu(ou);
                user.getProps().add().setProperty(prop).setValue("true");

                return user.insert().keyAsString();
            });

        runInTransaction(() -> new FormTest("tekgenesis.authorization.UserForm", userKey).serializeAndAssert());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static void assertFormModel(FormModel oldModel, FormModel newModel) {
        for (final Widget w : oldModel.metadata()) {
            if (w.getElemType() == ElemType.MULTIPLE) {
                final MultipleWidget multiple    = Predefined.cast(w);
                final MultipleModel  value       = newModel.getMultiple(multiple);
                final MultipleWidget oldMultiple = Predefined.cast(w);
                final MultipleModel  oldValue    = oldModel.getMultiple(oldMultiple);

                assertThat(oldValue.size()).isEqualTo(value.size());

                final Collection<Widget> columns = value.getMultipleWidget().getTableElements();

                final Iterator<RowModel> rowsFromTableA = value.iterator();

                final Iterator<RowModel> rowsFromTableB = oldValue.iterator();

                while (rowsFromTableA.hasNext() && rowsFromTableB.hasNext()) {
                    final RowModel r1 = rowsFromTableA.next();
                    final RowModel r2 = rowsFromTableB.next();
                    for (final Widget c : columns)
                        assertThat(r1.get(c)).isEqualTo(r2.get(c));
                }
            }
            else if (w.getElemType() == ElemType.ARRAY) {
                final Iterable<Object> value    = newModel.getArray(w);
                final Iterable<Object> oldValue = oldModel.getArray(w);

                assertThat(Colls.size(oldValue)).isEqualTo(Colls.size(value));

                final Iterator<Object> valuesFromA = value.iterator();
                final Iterator<Object> valuesFromB = oldValue.iterator();

                while (valuesFromA.hasNext() && valuesFromB.hasNext()) {
                    final Object v1 = valuesFromA.next();
                    final Object v2 = valuesFromB.next();
                    assertThat(v1).isEqualTo(v2);
                }
            }
            else {
                // SubForms are not handle it;
                final Object value    = newModel.get(w);
                final Object oldValue = oldModel.get(w);
                assertThat(value).isEqualTo(oldValue);
            }
        }
    }  // end method assertFormModel

    //~ Inner Classes ................................................................................................................................

    private static class FormTest {
        private String     fqn         = "";
        private final File goldenDir   = new File("server/core/src/test/resources/json/goldenFiles");
        private FormModel  goldenModel = null;

        private final File outputDir = new File("target/server/core/test-output/json");

        private FormTest(String fqn, @Nullable String pk) {
            this.fqn    = fqn;
            goldenModel = build(fqn, pk);
            outputDir.mkdirs();
        }

        void deserializeAndAssert() {
            // Default Deserialization;
            final FormModel defaultInputModel = readInput(false);
            assertFormModel(goldenModel, defaultInputModel);
        }

        void serializeAndAssert() {
            // Default Serialization;
            checkDiff(writeOutput(false), goldenFile(false));
            // Pretty Serialization;
            checkDiff(writeOutput(true), goldenFile(true));
        }

        private FormModel build(String currentFqn, @Nullable String currentPk) {
            SecurityUtils.getSession().authenticate("admin", "password");
            final Form form = getRetriever().getForm(createQName(currentFqn));
            return ReflectedFormInstance.createFormInstance(form, currentPk).getModel();
        }

        private JsonIOBuilder createIOBuilder(Boolean pretty) {
            final JsonIOBuilder builder = new JsonIOBuilder();
            if (pretty) builder.configure().includeNullFields().prettyPrinting();
            return builder;
        }

        private String fileName(Boolean pretty) {
            return fqn + (pretty ? ".pretty.json" : ".json");
        }

        private File goldenFile(Boolean pretty) {
            return new File(goldenDir, fileName(pretty));
        }

        private File outputFile(Boolean pretty) {
            return new File(outputDir, fileName(pretty));
        }

        private FormModel readInput(boolean pretty) {
            try {
                final JsonIOBuilder builder    = createIOBuilder(pretty);
                final File          result     = outputFile(pretty);
                final FormModel     blankModel = build(fqn, null);
                final FormInput     output     = builder.createInput(new FileReader(result), Charset.defaultCharset(), blankModel);
                output.read();
                return blankModel;
            }
            catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private File writeOutput(Boolean pretty) {
            try {
                final JsonIOBuilder builder = createIOBuilder(pretty);
                final File          result  = outputFile(pretty);
                final FormOutput    output  = builder.createOutput(new FileWriter(result), Charset.defaultCharset());
                output.write(goldenModel, Colls.emptyIterable());
                return result;
            }
            catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }  // end class FormTest
}  // end class FormJsonSerializerTest
