
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.view.client.ui.TableTester;

import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;
import static tekgenesis.showcase.TabsForm.THIRD_TAB;

/**
 * Client side testing of the widget's field options.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiFieldOptionsTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCheckOnChangeForm() {
        load("tekgenesis.showcase.CheckOnChange",
                f -> {
                    final FormTester.FieldTester item    = f.getField("item");
                    final FormTester.FieldTester item2   = f.getField("item2");
                    final FormTester.FieldTester changed = f.getField("changed");

                    item.setValue(2);

                    assertEquals(false, changed.getValue());
                    assertEquals(2, item.getValue());
                    assertEquals(2, item2.getValue());

                    item.setValue(6);          // Will fire on_change
                }).sync(f -> {
                final FormTester.FieldTester item    = f.getField("item");
                final FormTester.FieldTester item2   = f.getField("item2");
                final FormTester.FieldTester changed = f.getField("changed");

                assertEquals(true, changed.getValue());
                assertEquals(6, item.getValue());
                assertEquals(6, item2.getValue());
            }).test();
    }

    @Test public void testFormCustomMask() {
        load("tekgenesis.showcase.CustomMaskTestForm",
                f -> {
                    final FormTester.FieldTester uniqueMask = f.getField("uniqueMask");
                    uniqueMask.setValue("123we");
                    assertEquals(uniqueMask.getValue(), "WE");
                    uniqueMask.setValue("aS32");
                    assertEquals(uniqueMask.getValue(), "AS32");

                    final FormTester.FieldTester threeMasks = f.getField("threeMasks");
                    threeMasks.setValue("fds");
                    assertEquals(threeMasks.getValue(), "FD");
                    threeMasks.setValue("fd12");
                    assertEquals(threeMasks.getValue(), "FD12");
                    threeMasks.setValue("fd123");
                    assertEquals(threeMasks.getValue(), "FD123");
                    threeMasks.setValue("f1d1A3");
                    assertEquals(threeMasks.getValue(), "F1D1A3");

                    final FormTester.FieldTester fromFieldMask = f.getField("fromFieldMask");
                    fromFieldMask.setValue("as32");
                    assertEquals(fromFieldMask.getValue(), "32");
                    fromFieldMask.setValue("43asrewrwe");
                    assertEquals(fromFieldMask.getValue(), "43AS");

                    final FormTester.FieldTester fromFieldMasks = f.getField("fromFieldMasks");
                    fromFieldMasks.setValue("as32");
                    assertEquals(fromFieldMasks.getValue(), "AS32");
                    fromFieldMasks.setValue("43asrewrwe");
                    assertEquals(fromFieldMasks.getValue(), "43AS");
                }).test();
    }

    @Test public void testFormExpressionShowcase() {
        load("tekgenesis.showcase.ExpressionShowcase",
                f -> {
                    assertEquals(-1, f.getField("ignoredItem").getValue());

                    final FormTester.FieldTester item1 = f.getField("item1");
                    assertEquals(20, item1.getValue());

                    item1.setValue(50);

                    final FormTester.FieldTester item2 = f.getField("item2");
                    assertEquals(100, item2.getValue());

                    final FormTester.FieldTester subtotal = f.getField("subtotal");
                    assertEquals(200, subtotal.getValue());

                    final FormTester.FieldTester first = f.getField("first");
                    first.setValue("santiago");
                }).sync(f -> {
                final FormTester.FieldTester last = f.getField("last");
                last.setValue("racca");
            }).sync(f -> {
                final FormTester.FieldTester nick = f.getField("nick");
                assertEquals("sracca", nick.getValue());

                assertFalse(nick.hasError());
                // Save!
                assertFalse(f.save());
                assertTrue(nick.hasError());
            }).test();
    }

    @Test public void testLabelExpressionInTabs() {
        load("tekgenesis.showcase.TabsForm",
                f -> {
                    final FormTester.TabTester tab = f.getTab("tabs");
                    assertTrue(tab.isVisible());
                    assertFalse(tab.isDisabled());

                    tab.clickTab("Second");
                    assertEquals(1, f.getField("tabs").getValue());
                    tab.clickTab("First");
                    assertEquals(0, f.getField("tabs").getValue());

                    assertEquals(THIRD_TAB, tab.getLabel(2));
                    tab.clickTab(THIRD_TAB);
                    assertEquals(2, f.getField("tabs").getValue());

                    f.getField("str").setValue("Fourth");

                    assertEquals("Fourth", tab.getLabel(2));
                    assertEquals(2, f.getField("tabs").getValue());
                }).test();
    }

    @Test public void testLinkShowcaseForm() {
        load("tekgenesis.showcase.LinkShowcaseForm",
                f -> {
                    final FormTester.FieldTester suggest = f.getField("sugguestWithLinkForm");
                    assertNull(suggest.getValue());
                    suggest.click();
                }).redirect("tekgenesis.showcase.TestForm",
                f -> {
                    f.getField("name").setValue("name");
                    f.getField("description").setValue("description");
                    assertTrue("TestForm save failed!", f.save());
                })
            .redirect("tekgenesis.showcase.LinkShowcaseForm",
                    f -> {
                        final FormTester.FieldTester sugguest = f.getField("sugguestWithLinkForm");
                        assertEquals("name", sugguest.getValue());
                        sugguest.click();
                    })
            .redirect("tekgenesis.showcase.TestForm",
                    f -> {
                        assertEquals("name", f.getField("name").getValue());
                        assertEquals("description", f.getField("description").getValue());
                        f.cancel();
                    })
            .redirect("tekgenesis.showcase.LinkShowcaseForm",
                    f -> {
                        assertEquals("name", f.getField("sugguestWithLinkForm").getValue());
                        f.cancel();
                    })
            .reload()
            .test();
    }

    @Test public void testNestedDisables() {
        load("tekgenesis.showcase.DropdownForm",
                f -> {
                    // table assertions
                    final FormTester.FieldTester nr1 = f.getField("nr1", 0);
                    assertFalse(nr1.isDisabled());
                    f.getField("b").setValue(true);

                    assertFalse(nr1.isDisabled());
                    assertTrue(f.getField("name", 0).isDisabled());

                    f.getField("b").setValue(false);
                    f.getField("available", 0).setValue(true);
                    f.getField("b").setValue(true);

                    assertFalse(f.getField("drop", 0).isDisabled());
                    assertTrue(f.getField("name", 0).isDisabled());
                }).test();
    }

    @Test public void testViewShowcaseForm() {
        load("tekgenesis.showcase.ViewShowcaseForm",
                f -> {
                    assertEquals(0, f.getField("id").getValue());
                    assertEquals(false, f.getField("bool").getValue());
                    assertEquals("12345678901234567890", f.getField("lon").getValue());
                    assertEquals(3, Colls.size(f.getField("multipleLon").getArrayValue()));
                    assertEquals(2, Colls.size(f.getField("multipleReal").getArrayValue()));

                    final TableTester table      = f.getTable("props");
                    final int         stringRoId = table.addRow();
                    f.getField("type", stringRoId).setValue("STRING");
                })  //J-
                .sync(f -> f.getField("type", 0).setValue("INT"))
                .sync(f -> f.getField("type", 0).setValue("REAL"))
                .sync(f -> f.getField("type", 0).setValue("BOOLEAN"))
                .sync(f -> f.getField("type", 0).setValue("DATE"))
        //J+
        .sync().test();
    }

    @NotNull @Override public String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS };
    }
}  // end class SuiFieldOptionsTest
