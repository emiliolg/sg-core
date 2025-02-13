
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
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.MetadataFormMessages;
import tekgenesis.showcase.SuggestBoxShowcase;
import tekgenesis.showcase.TableSubform;
import tekgenesis.view.client.ui.InlineSubformUI;
import tekgenesis.view.client.ui.TableTester;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;

/**
 * Client side testing of general widgets.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiWidgetsTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testChartShowcase() {
        load("tekgenesis.showcase.ChartShowcase",
                f -> {
                    assertTrue("Line chart should be visible", f.getField("line").isVisible());
                    assertTrue("Column chart should be visible", f.getField("column").isVisible());
                    assertTrue("Bar chart should be visible", f.getField("bar").isVisible());
                    assertTrue("Pie chart should be visible", f.getField("pie").isVisible());
                }).test();
    }

    @Test public void testDynamicShowcase() {
        load("tekgenesis.showcase.DynamicForm",
                f -> {
                    final TableTester widgets = f.getTable("widgets");
                    widgets.addRow();

                    final FormTester.FieldTester field = f.getField("property", 0);
                    field.setValue("Age:INT");
                }).sync(f -> {
                // Get dynamic widget on first row
                final FormTester.FieldTester field = f.getField("value", 0);
                assertTrue("Empty values.", isEmpty(field.getArrayValue()));
                assertNull("Required value message should not be displayed.", field.getMsg());

                // Attempt to save
                assertFalse("Cannot save, required value missing.", f.save());

                assertNotNull("Required value message should be displayed.", field.getMsg());

                f.getField("combo").setValue("STRING_SECRET");
            }).sync(f -> {
                final FormTester.FieldTester dynamicValue = f.getField("dynimicValue");
                dynamicValue.setValue("secret");
                assertEquals("secret", dynamicValue.getValue());
                f.getField("combo").setValue("INT");
            }).sync(f -> {
                final FormTester.FieldTester dynamicValue = f.getField("dynimicValue");
                assertEquals(null, dynamicValue.getValue());
            }).test();
    }

    @Test public void testDynamicsInSection() {
        load("tekgenesis.showcase.SuperNestedMultiples", f -> {
                    testDynamicValueAndDisabled(f, 0);
                    testDynamicValueAndDisabled(f, 1);
                }).test();
    }

    @Test public void testFormDisplayShowcase() {
        load("tekgenesis.showcase.DisplayShowcase",
                f -> {
                    final FormTester.FieldTester text1 = f.getField("text1");
                    final FormTester.FieldTester text2 = f.getField("text2");

                    final FormTester.FieldTester disableCheck = f.getField("disableCheck");
                    final FormTester.FieldTester hideCheck    = f.getField("hideCheck");

                    final FormTester.FieldTester combo     = f.getField("combo");
                    final FormTester.FieldTester textField = f.getField("input");

                    assertEquals("disable", text1.getValue());
                    assertTrue(text2.isDisabled());

                    text1.setValue(" ");
                    assertTrue(!text2.isDisabled());

                    disableCheck.setValue(true);
                    hideCheck.setValue(true);

                    assertTrue(!combo.isVisible());
                    assertTrue(textField.isVisible());
                    assertTrue(textField.isDisabled());
                }).test();
    }

    @Test public void testFormMailFieldShowcase() {
        load("tekgenesis.showcase.MailFieldShowcaseForm",
                f -> {
                    final FormTester.FieldTester mail = f.getField("mail");
                    mail.setValue("mail1@");
                    assertEquals("mail1@", mail.getValue());
                    assertTrue(mail.hasError());
                    assertEquals(mail.getMsg().getText(), FormViewMessages.MSGS.wrongMailSyntax());
                    mail.setValue("mail1@gmail.com");
                    assertEquals("mail1@gmail.com", mail.getValue());
                    assertFalse(mail.hasError());

                    mail.setValue("mail1@");
                    assertEquals("mail1@", mail.getValue());
                    assertTrue(mail.hasError());
                    assertEquals(mail.getMsg().getText(), FormViewMessages.MSGS.wrongMailSyntax());
                    mail.setValue("");
                    assertEquals(null, mail.getValue());
                    assertFalse(mail.hasError());
                }).test();
    }  // end method testFormMailFieldShowcase

    @Test public void testFormSuggestBoxShowcase() {
        load("tekgenesis.showcase.SuggestBoxShowcase",
                f -> {
                    final FormTester.FieldTester options = f.getField("options");
                    options.setValue("Option1");
                    assertEquals("Option1", options.getValue());

                    final FormTester.FieldTester colors = f.getField("strings");
                    colors.setValue(SuggestBoxShowcase.BLACK);
                    assertEquals(SuggestBoxShowcase.BLACK, colors.getValue());

                    final FormTester.FieldTester optionTags = f.getField("optionTags");
                    if (optionTags.getArrayValue().iterator().hasNext()) {
                        final String option = (String) optionTags.getArrayValue().iterator().next();
                        assertEquals("OPTION1", option);
                    }

                    final FormTester.FieldTester entityTags = f.getField("entityTags");
                    assertEquals(false, entityTags.getArrayValue().iterator().hasNext());

                    final FormTester.FieldTester stringTags = f.getField("stringTags");
                    if (stringTags.getArrayValue().iterator().hasNext()) {
                        final String red = (String) stringTags.getArrayValue().iterator().next();
                        assertEquals("red", red);
                    }
                }).test();
    }

    @Test public void testFormWidgetShowcase() {
        load("tekgenesis.showcase.WidgetShowcase",
                f -> {
                    final FormTester.FieldTester radio = f.getField("radiogroup");
                    assertTrue(radio.isVisible());
                }).test();
    }

    @Test public void testHideDropdown() {
        load("tekgenesis.showcase.DropdownForm",
                f -> {
                    // table assertions
                    final FormTester.FieldTester nr1  = f.getField("nr1", 0);
                    final FormTester.FieldTester drop = f.getField("drop", 0);

                    assertTrue(nr1.isVisible());
                    assertFalse(drop.isVisible());
                    f.getField("available", 0).setValue(true);
                    assertFalse(nr1.isVisible());
                    assertTrue(drop.isVisible());

                    // assertions outside table
                    assertTrue("Dropdown should be visible", f.getField("ddown").isVisible());
                    f.getField("a").setValue(true);
                    final FormTester.FieldTester b = f.getField("b");
                    b.setValue(true);
                    assertFalse("Dropdown should be hidden", f.getField("ddown").isVisible());

                    // table assertions again
                    assertFalse(nr1.isVisible());
                    assertFalse(drop.isVisible());

                    b.setValue(false);

                    assertFalse(nr1.isVisible());
                    assertTrue(drop.isVisible());

                    f.getField("available", 0).setValue(false);
                    b.setValue(true);
                    b.setValue(false);

                    assertTrue(nr1.isVisible());
                    assertFalse(drop.isVisible());
                }).test();
    }

    //J-
    @Test public void testCheckGroupForm() {
        load("tekgenesis.showcase.OptionWidgetsForm", f -> f.click("checkButton")).sync(f -> {
            final FormTester.FieldTester check = f.getField("checkSimpE");
            assertTrue(check.hasError());
            assertEquals(check.getMsg().getText(), MetadataFormMessages.MSGS.invalidValueForOptionWidget("Name Principalities", "checkSimpE"));
            assertEquals(check.getOptions().size(), 3);
            f.click("checkOptionButton");
        }).sync(f -> {
            final FormTester.CheckGroupTester check = f.getCheckGroup("checkSimpE");
            assertFalse(check.hasError());
            assertNull(check.getMsg());
            assertTrue(Colls.exists(check.getValues(), s -> "Name Principalities".equals(s.toString())));
        }).test();
    }
    //J+

    //J-
    @Test public void testComboForm() {
        load("tekgenesis.showcase.OptionWidgetsForm", f -> f.click("simpleButton")).sync(f -> {
            final FormTester.FieldTester simpleE = f.getField("simpleE");
            assertTrue(simpleE.hasError());
            assertEquals(simpleE.getMsg().getText(), MetadataFormMessages.MSGS.invalidValueForOptionWidget("Name Principalities", "simpleE"));
            assertEquals(simpleE.getOptions().size(), 3);
            f.click("simpleOptionButton");
        }).sync(f -> {
            final FormTester.FieldTester simpleE = f.getField("simpleE");
            assertFalse(simpleE.hasError());
            assertNull(simpleE.getMsg());
            final Object simpleEValue = simpleE.getValue();
            assertNotNull(simpleEValue);
            assertEquals(simpleEValue.toString(), "Name Principalities");
        }).test();
    }
    //J+

    @Test public void testImageForm() {
        load("tekgenesis.showcase.SimpleImageForm",
                f -> {
                    assertTrue("imgMultiple should be visible.", f.getField("imgMultiple").isVisible());
                    assertTrue("imgEnum should be visible.", f.getField("imgEnum").isVisible());
                    assertTrue("imgResource should be visible.", f.getField("imgResource").isVisible());
                    assertTrue("gallery should be visible.", f.getField("gallery").isVisible());
                    assertTrue("imgString should be visible.", f.getField("imgString").isVisible());

                    f.click("imgEnum");
                }).test();
    }

    @Test public void testLabelShowcase() {
        load("tekgenesis.showcase.LabelShowcase", f -> {
                    label(f, "withLabel1").ifPresentOrElse(l -> assertTrue(l.contains("Entity Label")),
                        () -> fail("Must have 'Entity Label' as label"));
                    label(f, "noLabel1").ifPresentOrElse(l -> assertTrue(l.contains("No Label1")), () -> fail("Must have 'No Label1' as label"));
                    label(f, "noLabel2").ifPresentOrElse(l -> assertTrue(l.contains("Form Label")), () ->
                            fail("Must have 'Form Label' as label"));

                    assertFalse(label(f, "withLabel2").isPresent());
                    assertFalse(label(f, "noLabel3").isPresent());
                    assertFalse(label(f, "noLabel4").isPresent());

                    label(f, "formLabel1").ifPresentOrElse(l -> assertTrue(l.contains("Form Label")), () ->
                            fail("Must have 'Form Label' as label"));
                    assertFalse(label(f, "formLabel3").isPresent());
                    assertFalse(label(f, "formLabel5").isPresent());

                    assertFalse(label(f, "formLabel2").isPresent());
                    assertFalse(label(f, "formLabel4").isPresent());
                    assertFalse(label(f, "formLabel6").isPresent());

                    label(f, "inputGroupWithLabels").ifPresentOrElse(l -> assertTrue(l.contains("Input Group With Labels")),
                        () -> fail("Must have 'Input Group With Labels' as label"));

                    assertFalse(label(f, "formLabel12").isPresent());
                    assertFalse(label(f, "formLabel32").isPresent());
                    assertFalse(label(f, "formLabel52").isPresent());

                    label(f, "inputGroupNoLabels").ifPresentOrElse(l -> assertTrue(l.contains("InputGroup No Labels")),
                        () -> fail("Must have 'InputGroup No Labels' as label"));

                    assertFalse(label(f, "formLabel22").isPresent());
                    assertFalse(label(f, "formLabel42").isPresent());
                    assertFalse(label(f, "formLabel62").isPresent());
                }).test();
    }

    @Test public void testMapForm() {
        load("tekgenesis.showcase.MapForm", f -> assertTrue("Map should be visible.", f.getField("map").isVisible())).test();
    }

    @Test public void testMessageUI() {
        load("tekgenesis.showcase.TitleEntityForm",
                f -> {
                    final FormTester.FieldTester title             = f.getField("title");
                    final String                 nonPopulatedTitle = (String) title.getValue();
                    assertEquals("Title Entity Form", nonPopulatedTitle);

                    final FormTester.FieldTester name = f.getField("name");
                    name.setValue("Porsche");

                    final FormTester.FieldTester color = f.getField("color");
                    color.setValue("911");

                    f.click("submit");
                }).redirect("tekgenesis.showcase.TitleEntityForm",
                f -> {
                    final FormTester.FieldTester title          = f.getField("title");
                    final String                 populatedTitle = (String) title.getValue();      // "Porsche"
                    assertEquals("Porsche", populatedTitle);

                    final FormTester.FieldTester name = f.getField("name");
                    assertEquals("Porsche", name.getValue());
                })
            .test();
    }

    @Test public void testNumbersForm() {
        load("tekgenesis.showcase.NumbersForm",
                f -> {
                    f.getField("name").setValue("test");
                    assertEquals("test", f.getField("name").getValue());

                    f.getField("unsignedInt4").setValue(1234);
                    assertEquals(1234, f.getField("unsignedInt4").getValue());

                    f.getField("signedInt5").setValue("-");
                    assertEquals(null, f.getField("signedInt5").getValue());
                    f.getField("signedInt5").setValue(-12345);
                    assertEquals(-12345, f.getField("signedInt5").getValue());

                    f.getField("unsignedInteger").setValue(12345678);
                    assertEquals(12345678, f.getField("unsignedInteger").getValue());

                    f.getField("signedInteger").setValue("-");
                    assertEquals(null, f.getField("signedInteger").getValue());
                    f.getField("signedInteger").setValue(-1234567890);
                    assertEquals(-123456789, f.getField("signedInteger").getValue());

                    f.getField("unsignedDecimal52").setValue(new BigDecimal("123.45"));
                    assertEquals(new BigDecimal("123.45"), f.getField("unsignedDecimal52").getValue());

                    f.getField("signedDecimal52").setValue("-");
                    assertEquals(null, f.getField("signedDecimal52").getValue());
                    f.getField("signedDecimal52").setValue(new BigDecimal("-123.45"));
                    assertEquals(new BigDecimal("-123.45"), f.getField("signedDecimal52").getValue());

                    f.getField("unsignedReal").setValue(123.001);
                    assertEquals(123.001, f.getField("unsignedReal").getValue());

                    f.getField("signedReal").setValue("-");
                    assertEquals(null, f.getField("signedReal").getValue());
                    f.getField("signedReal").setValue(-123.001);
                    assertEquals(-123.001, f.getField("signedReal").getValue());
                    f.getField("signedToBeUnsigned").setValue(12);
                    assertEquals(12, f.getField("signedToBeUnsigned").getValue());

                    f.click("save");
                }).redirect("tekgenesis.showcase.NumbersForm",
                f -> {
                    assertEquals("test", f.getField("name").getValue());
                    assertEquals(1234, f.getField("unsignedInt4").getValue());
                    assertEquals(-12345, f.getField("signedInt5").getValue());
                    assertEquals(12345678, f.getField("unsignedInteger").getValue());
                    assertEquals(-123456789, f.getField("signedInteger").getValue());
                    assertEquals(new BigDecimal("123.45"), f.getField("unsignedDecimal52").getValue());
                    assertEquals(new BigDecimal("-123.45"), f.getField("signedDecimal52").getValue());
                    assertEquals(123.001, f.getField("unsignedReal").getValue());
                    assertEquals(-123.001, f.getField("signedReal").getValue());
                    assertEquals(12, f.getField("signedToBeUnsigned").getValue());
                })
            .test();
    }  // end method testNumbersForm

    @Test public void testRatingForm() {
        load("tekgenesis.showcase.RatingForm",
                f -> {
                    final FormTester.FieldTester stars = f.getField("stars");
                    assertEquals(4, stars.getValue());
                    final FormTester.FieldTester hearts = f.getField("hearts");
                    assertEquals(3, hearts.getValue());
                }).test();
    }

    @Test public void testRemovingRowWithSubform() {
        load("tekgenesis.showcase.SuperNestedMultiples",
                f -> {
                    final InlineSubformUI          subf          = f.getField("subf", 0).asInlineSubformUI();
                    final FormTester.SubformTester subformTester = f.getSubformTester(subf);
                    subformTester.getFormTester().click("delete", 0);
                }).sync(f -> {
                assertEquals(1, f.getSection("sections").size());
                final InlineSubformUI          subf          = f.getField("subf", 0).asInlineSubformUI();
                final FormTester.SubformTester subformTester = f.getSubformTester(subf);
                subformTester.getFormTester().getField("someName", 0).setValue("asdasd");
                assertEquals("asdasd", subformTester.getFormTester().getField("someName", 0).getValue());
            }).test();
    }

    @Test public void testSectionForm() {
        load("tekgenesis.showcase.SectionForm",
                f -> {
                    final FormTester.SectionTester rooms = f.getSection("rooms");
                    assertEquals(5, rooms.size());

                    f.getField("remove").click();
                }).sync(f -> {
                final FormTester.SectionTester rooms = f.getSection("rooms");
                assertEquals(4, rooms.size());

                f.click("clear");
            }).sync(f -> {
                final FormTester.SectionTester rooms = f.getSection("rooms");
                assertEquals(0, rooms.size());
            }).test();
    }

    @Test public void testSuggestions() {
        load("tekgenesis.showcase.SuggestBoxShowcase",
                f -> {
                    final FormTester.SuggestBoxTester stringsSync = f.getSuggestBox("stringsSync");
                    assertEquals("6", stringsSync.getValue());
                    assertEquals("red", stringsSync.getSuggestLabel());
                    final FormTester.SuggestBoxTester strs = f.getSuggestBox("strings");
                    assertEquals("cyan", strs.getValue());
                    assertEquals("cyan", strs.getSuggestLabel());
                    f.click("resetStrings");          // reset strings to see if null value resets the label
                }).sync(f -> {
                final FormTester.SuggestBoxTester strings = f.getSuggestBox("strings");
                assertNull(strings.getValue());
                assertEquals("", strings.getSuggestLabel());
            }).test();
    }

    @Test public void testTabForm() {
        load("tekgenesis.showcase.GroupShowcase",
                f -> {
                    final FormTester.TabTester tab = f.getTab("tab1");
                    assertTrue(tab.isVisible());
                    assertFalse(tab.isDisabled());

                    final FormTester.GroupTester t1 = f.getGroup("t1");
                    final FormTester.GroupTester t2 = f.getGroup("t2");

                    assertTrue(t1.isVisible());
                    assertFalse(t2.isVisible());

                    assertNotNull(f.getField("tf1").getValue());
                    assertNotNull(f.getField("tf2").getValue());
                    assertNull(f.getField("tf3").getValue());
                    assertNull(f.getField("tf4").getValue());

                    tab.clickTab("Tab 2");
                }).sync(f -> {
                final FormTester.GroupTester t1 = f.getGroup("t1");
                final FormTester.GroupTester t2 = f.getGroup("t2");
                assertFalse(t1.isVisible());
                assertTrue(t2.isVisible());

                assertNotNull(f.getField("tf1").getValue());
                assertNotNull(f.getField("tf2").getValue());
                assertNotNull(f.getField("tf3").getValue());
                assertNotNull(f.getField("tf4").getValue());
            }).test();
    }

    @Test public void testTagsSuggest() {
        load("tekgenesis.showcase.SuggestBoxShowcase",
                f -> {
                    assertFalse(f.getField("entityTags").getArrayValue().iterator().hasNext());
                    f.click("click");
                }).sync(f -> {
                assertTrue(f.getField("entityTags").getArrayValue().iterator().hasNext());
                final List<String> entityTags = f.getTagsSuggest("entityTags").getTagsText();
                assertFalse(entityTags.isEmpty());
            }).test();
    }

    @NotNull @Override public String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS };
    }

    private Option<String> label(FormTester f, String id) {
        return f.getField(id).getLabel();
    }

    private void testDynamicValueAndDisabled(FormTester f, int row) {
        final InlineSubformUI          subf          = f.getField("subf", row).asInlineSubformUI();
        final FormTester.SubformTester subformTester = f.getSubformTester(subf);
        final FormTester.FieldTester   dynamicW      = subformTester.getFormTester().getField("dynamicW", 0);
        assertEquals(TableSubform.DYNAMIC_VALUE + (row + 1), dynamicW.getValue());
        assertFalse(dynamicW.isDisabled());
        subformTester.getFormTester().getField("bool", 0).setValue(true);
        assertTrue(dynamicW.isDisabled());
    }
}  // end class SuiWidgetsTest
