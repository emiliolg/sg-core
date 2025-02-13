
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.view.client.ui.AnchoredSubformUI;

import static tekgenesis.common.core.Constants.BASIC_MODELS;
import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;
import static tekgenesis.metadata.form.model.FormConstants.IMAGE;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Client side testing of Forms of Basic module.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiBasicFormsTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testClientForm() {
        load("tekgenesis.showcase.ClientForm",
                f -> {
                    f.getField("name").setValue("name1");
                    assertFalse("Failed Subform Validation", f.save());
                    assertTrue("Subform should be required", f.getField("address").hasError());

                    final AnchoredSubformUI subformUI = f.getField("address").asSubformUI();
                    subformUI.click();
                }).sync(f -> {
                final AnchoredSubformUI        subformUI      = f.getField("address").asSubformUI();
                final FormTester.SubformTester firstBoxTester = f.getSubformTester(subformUI);

                assertFalse("Validation for Street, not yet initialized", firstBoxTester.getFormTester().getField("street").hasError());
                assertFalse("Validation for City, not yet initialized", firstBoxTester.getFormTester().getField("city").hasError());
                assertFalse("Validation for State, not yet initialized", firstBoxTester.getFormTester().getField("state").hasError());
                assertFalse("Validation for Country, not yet initialized", firstBoxTester.getFormTester().getField("country").hasError());

                assertFalse("Failed Subform Validation", f.save());

                assertFalse("Subform is now initialized", f.getField("address").hasError());

                firstBoxTester.getFormTester().getField("street").setValue("Beliera 3025");
                firstBoxTester.getFormTester().getField("city").setValue("Pilar");
                firstBoxTester.getFormTester().getField("state").setValue("Buenos Aires");
                firstBoxTester.getFormTester().getField("zip").setValue("1629");
                firstBoxTester.getFormTester().getField("country").setValue("Argentina");
            }).test();
    }  // end method testClientForm

    @Test public void testCustomerForm() {
        load("tekgenesis.sales.basic.CustomerForm",
                f -> {
                    // Fill Customer values
                    f.getField("documentType").setValue("DNI");
                    f.getField("documentId").setValue(new BigDecimal(1234567890));

                    f.getField("firstName").setValue("Victor");
                    f.getField("lastName").setValue("Frankl");
                    assertEquals("Nickname should have a composed default value", f.getField("nickname").getValue(), "V" + "Frankl");

                    f.getField("sex").setValue("M");

                    // Save!
                    assertTrue("Customer form save failed!", f.save());
                }).redirect("tekgenesis.sales.basic.PreferencesForm",
                f -> {
                    // Fill preferences values
                    assertEquals("Previously saved Customer should be set", "DNI:1234567890:M", f.getField("customer").getValue());

                    f.getField("mail").setValue("test@tekgenesis.com");
                    f.getField(IMAGE).setValue("image.png");
                    f.getField("digest").setValue("WEEKLY");
                    f.getField("twitter").setValue("@tekgenesis");
                })
            .sync(f -> {
                    // Save!
                    assertTrue("Customer preferences save failed!", f.save());
                })
            .reload()
            .test();
    }

    @Test public void testProductForm() {
        load("tekgenesis.sales.basic.ProductForm",
                f -> {
                    // Fill some Product values
                    f.getField("productId").setValue("1");
                    f.getField(MODEL).setValue("SONY-32-0001");
                    f.getField("description").setValue("Sony 32'");
                    f.getField("price").setValue(new BigDecimal(499));
                    f.getField("state").setValue("ACTIVE");

                    assertNull("Product should have no category", f.getField("category").getValue());

                    // Suggest -no option available-, choose "Create New..." and navigate to create a Category
                    f.getSuggestBox("category").suggest("Televisores", 0, MSGS.createNew());
                }).redirect("tekgenesis.sales.basic.CategoryForm",
                f -> {
                    // Fill category values
                    f.getField("id").setValue(1L);

                    final FormTester.FieldTester name = f.getField("name");
                    assertEquals("Category name should be Televisores", "Televisores", name.getValue());
                    assertEquals("Description not using name value as default", f.getField("descr").getValue(), name.getValue());

                    // Save!
                    assertTrue("Category save failed!", f.save());
                })
            .redirect("tekgenesis.sales.basic.ProductForm",
                    f -> {
                        // Ensure category is set
                        assertEquals("Category should have been set after callback", f.getField("category").getValue(), "1");

                        // Save!
                        assertTrue("Product save failed!", f.save());
                    })
            .redirect("tekgenesis.sales.basic.ProductForm",
                    f -> {
                        // After Product creation, return to Product Form and keep only the Category set
                        assertNull(f.getField("productId").getValue());
                        assertNull(f.getField(MODEL).getValue());

                        // Ensure category is set
                        assertEquals("Category should still be set", f.getField("category").getValue(), "1");
                    })
            .test();
    }  // end method testProductForm

    @NotNull @Override public String[] getProjectPaths() {
        return new String[] { BASIC_MODELS, SHOWCASE_MODELS };
    }

    //~ Static Fields ................................................................................................................................

    private static final String MODEL = FormConstants.MODEL_FIELD_NAME;
}  // end class SuiBasicFormsTest
