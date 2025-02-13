
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

import tekgenesis.check.CheckType;
import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.MetadataFormMessages;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.showcase.NewSync;
import tekgenesis.view.client.ui.AnchoredSubformUI;
import tekgenesis.view.client.ui.TableTester;

import static java.util.Arrays.asList;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ESCAPE;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;
import static tekgenesis.view.client.FormTester.fireKeyEvent;

/**
 * Client side testing of form's functionality, such as actions, parameters, extensions, subforms,
 * validations, etc.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiFormsTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testExtendedForms() {
        load("tekgenesis.showcase.DynamicFormA",
                f -> {
                    assertTrue(f.getField("textField").isVisible());
                    assertTrue(f.getField("dynamicTextField").isVisible());

                    assertEquals("onLoad value for static field", ensureNotNull(f.getField("textField").getValue()));
                    assertEquals("onLoad value for dynamic field", ensureNotNull(f.getField("dynamicTextField").getValue()));

                    f.getField("textField").setValue("NEW VALUE");
                }).sync(f -> {
                // test sync continue working
                assertEquals("NEW VALUE", ensureNotNull(f.getField("textField").getValue()));
                assertEquals("onLoad value for dynamic field", ensureNotNull(f.getField("dynamicTextField").getValue()));

                assertTrue(f.save());
            })
            .redirect("tekgenesis.showcase.DynamicFormA",
                    f -> {
                        // test save continue working
                        assertTrue(f.getField("textField").isVisible());
                        assertTrue(f.getField("dynamicTextField").isVisible());

                        assertEquals("onLoad value for static field", ensureNotNull(f.getField("textField").getValue()));
                        assertEquals("onLoad value for dynamic field", ensureNotNull(f.getField("dynamicTextField").getValue()));

                        f.click("navigate");          // test custom on_click
                    })
            .redirect("tekgenesis.showcase.DynamicFormB",
                    f -> {
                        assertTrue(f.getField("textField").isVisible());
                        assertTrue(f.getField("dynamicTextField").isVisible());

                        assertEquals("onLoad value for static field", ensureNotNull(f.getField("textField").getValue()));
                        assertEquals("onLoad value for dynamic field", ensureNotNull(f.getField("dynamicTextField").getValue()));

                        f.getField("dynamicTextField").setValue("new tmp dynamic field value");
                    })
            .sync(f -> {
                    // test custom on_change
                    assertEquals("onChange value for dynamic field", ensureNotNull(f.getField("dynamicTextField").getValue()));
                })
            .test();
    }  // end method testExtendedForms

    @Test public void testExtendedTableForm() {
        load("tekgenesis.showcase.DynamicTableForm",
                f -> {
                    // check that the new column was added
                    assertTrue(f.getField("ixMapping", 0).isVisible());
                    // check that the new column has the expected model value
                    assertEquals("Ix Mapping for row 0", f.getTable("clientAddresses").getCellValue(0, 5));
                }).test();
    }

    @Test public void testFilterForm() {
        load("tekgenesis.showcase.CarFilterForm",
                f -> {
                    // as we seed db for tests, it should not be empty the cars table.
                    assertTrue(f.getTable("cars").size() > 0);
                    assertTrue(f.getSection("filters").size() > 0);

                    assertEquals("Origin", f.getField("title", 0).getValue());
                    assertEquals("Make", f.getField("title", 1).getValue());
                    assertEquals("Model", f.getField("title", 2).getValue());
                    assertEquals("Published", f.getField("title", 3).getValue());
                    assertEquals("Engine", f.getField("title", 4).getValue());
                    assertEquals("Year", f.getField("title", 5).getValue());
                    assertEquals("Price", f.getField("title", 6).getValue());
                }).test();
    }

    @Test public void testFormsWithParameters() {
        load("tekgenesis.showcase.CarForm",
                null,
                "year=1992&engine=GAS&air=true&bluetooth=false&price=32",
                f -> {
                    assertEquals(f.getField("year").getValue(), 1992);
                    assertEquals(f.getField("engine").getValue(), "GAS");
                    assertEquals(f.getField("air").getValue(), true);
                    assertEquals(f.getField("bluetooth").getValue(), false);
                    assertEquals(f.getField("price").getValue(), new BigDecimal("32"));
                }).test();
    }

    @Test public void testFormValidationShowcase() {
        load("tekgenesis.showcase.ValidationShowcase",
                f -> {
                    final FormTester.FieldTester item1 = f.getField("input1");
                    item1.setValue(5);

                    final FormTester.FieldTester item2 = f.getField("input4");
                    item2.setValue(2);

                    assertTrue(item1.hasError());
                    assertTrue(item2.hasError());

                    final FormTester.FieldTester item3 = f.getField("input2");
                    final FormTester.FieldTester item4 = f.getField("input6");
                    item3.setValue(5);
                    item4.setValue(5);

                    assertTrue(item3.getMsg().getType() == CheckType.WARNING && !item3.getMsg().isInline());
                    assertTrue(item4.getMsg().getType() == CheckType.INFO && item4.getMsg().isInline());
                }).test();
    }

    @Test public void testListingForm() {
        load("tekgenesis.showcase.ListingSimpleEntities",
                f -> {
                    final TableTester entities = f.getTable("entities");
                    assertEquals(9, entities.size());

                    final FormTester.FieldTester addRow = f.getField("addRowBottom");
                    addRow.click();

                    final int row10 = entities.visibleRows() - 1;
                    f.getField("name", row10).setValue("Name 0");
                    f.getField("desc", row10).setValue("Description 0");

                    addRow.click();
                }).sync(f -> {
                final TableTester entities = f.getTable("entities");
                assertEquals(11, entities.size());                      // The second one is empty.

                f.getField("name", 10).setValue("Name 1");
                f.getField("desc", 10).setValue("Description 1");

                f.getField("addRowBottom").click();
            }).sync(f -> {
                final TableTester entities = f.getTable("entities");
                assertEquals(12, entities.size());                  // The third one will be deleted.

                f.click("reload");
            })
            .redirect("tekgenesis.showcase.ListingSimpleEntities",
                    f -> {
                        final TableTester entities = f.getTable("entities");
                        assertEquals(11, entities.size());          // I told you so... :-)

                        assertEquals("Name 0", f.getField("name", 0).getValue());
                        assertEquals("Description 0", f.getField("desc", 0).getValue());
                        assertEquals("Name 1", f.getField("name", 1).getValue());
                        assertEquals("Description 1", f.getField("desc", 1).getValue());

                        entities.selectRow(0);
                        f.getField("removeRowBottom").click();
                    })
            .sync(f -> {
                    final TableTester entities = f.getTable("entities");
                    assertEquals(10, entities.size());

                    f.click("reload");
                })
            .redirect("tekgenesis.showcase.ListingSimpleEntities",
                    f -> {
                        final TableTester entities = f.getTable("entities");
                        assertEquals(10, entities.size());
                    })
            .test();
    }  // end method testListingForm

    @Test public void testNavigateWithParameters() {
        load("tekgenesis.showcase.NavigateWithParameters", f -> {
                    assertNull(f.getField("name").getValue());
                    f.click("navi");
                }).redirect("tekgenesis.showcase.NavigateWithParameters",
                f -> {
                    assertEquals("My Parameter Name", f.getField("name").getValue());
                    assertEquals("OPTION5", f.getField("oneOption").getValue());
                    assertEquals(asList("OPTION2", "OPTION4"), f.getField("someOptions").getArrayValue());
                    assertEquals(3, f.getField("someInt").getValue());
                    assertNotNull(f.getField("oneDateTime").getValue());
                    assertNotNull(f.getField("oneDate").getValue());
                    assertEquals("32.500", String.valueOf(f.getField("decimal").getValue()));
                    assertEquals("122", f.getField("oneEntity").getValue());
                    assertEquals(asList("120", "121", "122"), f.getField("moreEntity").getArrayValue());
                })
            .test();
    }

    @Test public void testNewSync() {
        load("tekgenesis.showcase.NewSync",
                f -> {
                    f.getField("test").setValue("test");
                    assertEquals("test", f.getField("test").getValue());

                    f.click("addRow");
                }).sync(f -> {
                final FormTester.FieldTester strCol = f.getField("strCol", f.getTable("table").size() - 1);
                assertEquals("str3", strCol.getValue());

                f.click("addMessageFirst");
            }).sync(f -> {
                assertEquals(NewSync.A_NEW_MESSAGE, f.getField("strCol", 0).getMsg().getText());

                f.click("setOptionsFirst");
            }).sync(f -> {
                assertCombos(f);

                f.click("setOptionsGlobal");
            }).sync(f -> {
                assertCombos(f);

                f.click("resetFirst");
            }).sync(f -> {
                assertEquals(null, f.getField("strCol", 0).getValue());
                assertEquals(null, f.getField("strCol", 0).getMsg());

                f.click("configFirst");
            }).sync(f -> {
                final FormTester.FieldTester dateCol = f.getField("dateCol", 0);
                dateCol.setValue(DateOnly.date(2014, 7, 21).toMilliseconds());

                assertEquals(MetadataFormMessages.MSGS.disabledDate(), dateCol.getMsg().getText());
            }).test();
    }  // end method testNewSync

    @Test public void testReverseAddressForm() {
        load("tekgenesis.showcase.ReverseAddressForm",
                f -> {
                    final FormTester.FieldTester country = f.getField("country");

                    assertEquals(country.getScalarValue(), "Argentina");
                    assertEquals(f.getField("city").getScalarValue(), "Argentina default city");
                    assertEquals(f.getField("state").getScalarValue(), "Argentina default state");

                    f.getField("street").setValue("Some 1234");
                    f.getField("zip").setValue("90210");

                    country.setValue("Brasil");
                }).sync(f -> {
                final FormTester.FieldTester country = f.getField("country");

                assertEquals(country.getScalarValue(), "Brasil");
                assertEquals(f.getField("city").getScalarValue(), "Brasil default city");
                assertEquals(f.getField("state").getScalarValue(), "Brasil default state");

                assertNull("Street should be empty!", f.getField("street").getScalarValue());
                assertNull("Zip should be empty!", f.getField("zip").getScalarValue());

                final FormModel model = f.getFormModel();

                assertFalse("Reset should be false on client!",
                    model.isReset(model.widgetByName("country").getFieldSlot()) || model.isReset(model.widgetByName("city").getFieldSlot()) ||
                    model.isReset(model.widgetByName("state").getFieldSlot()) || model.isReset(model.widgetByName("street").getFieldSlot()) ||
                    model.isReset(model.widgetByName("zip").getFieldSlot()));
            }).test();
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    @Test public void testSubforms() {
        load("tekgenesis.showcase.SubformsShowcase",
                f -> {
                    assertEquals("...", f.getField("address").asSubformUI().getLinkText());

                    f.click("syncSubForm");
                }).sync(f -> {
                assertFalse("It should say something!", "...".equals(f.getField("address").asSubformUI().getLinkText()));
                assertFalse("It shouldn't be empty", f.getField("last").getValue() == null);

                f.click("syncSubForm");
            }).sync(f -> {
                final String value  = cast(f.getField("last").getValue());
                final String suffix = value.substring(4, value.length());

                assertTrue("It should end with: " + suffix, f.getField("address").asSubformUI().getLinkText().endsWith(suffix));

                final TableTester t = f.getTable("table");

                final int rowIndex = t.addRow();
                f.getField("description", rowIndex).setValue("Address 1");
                final AnchoredSubformUI addressInSubform = f.getField("addressInSubform", rowIndex).asSubformUI();
                assertEquals("...", addressInSubform.getLinkText());

                addressInSubform.click();
            }).sync(f -> {
                final AnchoredSubformUI        addressInSubform = f.getField("addressInSubform", 0).asSubformUI();
                final FormTester.SubformTester subformTester    = f.getSubformTester(addressInSubform);
                final String                   tekStreet        = "Beliera 3025";

                subformTester.getFormTester().getField("street").setValue(tekStreet);
                subformTester.getFormTester().getField("city").setValue("Pilar");
                subformTester.getFormTester().getField("state").setValue("Buenos Aires");
                subformTester.getFormTester().getField("zip").setValue("1629");
                subformTester.getFormTester().getField("country").setValue("Argentina");

                assertEquals("Subform link text is wrong", tekStreet, addressInSubform.getLinkText());
            }).test();
    }  // end method testSubforms

    //J-
    @Test public void testSwipe() {
        load("tekgenesis.showcase.ClassroomForm",
                f -> f.getTable("students").rowClicked(0))
                .sync(f -> {
                    assertTrue(f.isSwipeShowing());
                    fireKeyEvent(KEY_ESCAPE);
                    assertFalse(f.isSwipeShowing());

                    f.getField("swipe").setValue(false);
                    f.getTable("students").rowClicked(0);
                }).sync(f -> {
            assertTrue(f.isDetailShowing());
            fireKeyEvent(KEY_ESCAPE);
            assertFalse(f.isDetailShowing());
        }).test();
    }
    //J+

    //J-
    @Test public void testActionError() {
        load("tekgenesis.showcase.SynchronousForm", f -> f.click("actionError"))
                .sync(f -> {
                    assertTrue(f.hasErrorMessage());
                    f.click("click");
                })
                .sync(f -> {
                    assertFalse(f.hasErrorMessage());
                    f.click("actionError");
                })
                .sync(f -> assertTrue(f.hasErrorMessage()))
                .test();
    }
    //J+

    //J-
    @Test public void testErrorShowcase() {
        load("tekgenesis.showcase.ErrorShowcase", f ->
                f.click("button"))
                .sync(f -> {
                    //We run in dev mode, we have to close dev error dialog.
                    fireKeyEvent(KEY_ESCAPE);

                    assertTrue("Error should be active.", f.hasErrorMessage());
                    f.click("logout");
                }).sync(f ->
                assertFalse("Error shouldn't be active.", f.hasErrorMessage()))
                .test();
    }
    //J+

    @NotNull @Override public String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS };
    }

    private void assertCombos(FormTester f) {
        final FormTester.FieldTester combo0 = f.getField("comboCol", 0);
        assertTrue(combo0.getOptions().containsKey("Luke"));
        assertFalse(combo0.getOptions().containsKey("Alf"));

        final FormTester.FieldTester combo1 = f.getField("comboCol", 1);
        assertFalse(combo1.getOptions().containsKey("Luke"));
        assertTrue(combo1.getOptions().containsKey("Alf"));
    }
}  // end class SuiFormsTest
