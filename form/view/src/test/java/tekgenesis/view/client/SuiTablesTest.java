
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
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.metadata.form.MetadataFormMessages;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.view.client.ui.TableTester;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Constants.AUTHORIZATION_MODELS;
import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.view.client.ui.multiple.SorterLens.SortType.ASCENDING;
import static tekgenesis.view.client.ui.multiple.SorterLens.SortType.DESCENDING;

/**
 * Client side testing of table widget.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiTablesTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    /** This tests onchange expressions computation. */
    @Test public void testChangeTableSample() {
        load("tekgenesis.showcase.Table",
                tester -> {
                    final FormTester.FieldTester tax = tester.getField("tax");
                    tax.setValue(new BigDecimal("0.21"));

                    final TableTester table = tester.getTable("table");

                    table.addRow();
                    tester.getField("product", 0).setValue("LCD Sony");
                    tester.getField("unitPrice", 0).setValue(new BigDecimal("10.0"));
                    tester.getField("quantity", 0).setValue(10);

                    table.addRow();
                    tester.getField("product", 1).setValue("LCD Sony");
                    tester.getField("unitPrice", 1).setValue(new BigDecimal("20.0"));
                    tester.getField("quantity", 1).setValue(20);

                    final int lastRow = table.addRow();
                    tester.getField("product", lastRow).setValue("Mac book pro");
                    tester.getField("unitPrice", lastRow).setValue(new BigDecimal("10.0"));
                    tester.getField("quantity", lastRow).setValue(6);

                    tax.setValue(new BigDecimal("0.1"));      // IVA 10%
                    assertEquals(new BigDecimal("0.10"), tax.getValue());

                    // Dependency taxPrice <-- tax.
                    assertEquals(new BigDecimal("1.00"), tester.getField("taxPrice", 0).getValue());

                    // Dependency taxPrice_0 <-- price_0 && unitPrice_0 <-- price_0.
                    final FormTester.FieldTester price_0 = tester.getField("price", 0);
                    assertEquals(new BigDecimal("110.00"), price_0.getValue());

                    // Dependency total <-- price.
                    assertEquals(new BigDecimal("616.00"), tester.getField("total").getValue());

                    // Dependency row <-- quantity.
                    final FormTester.FieldTester quantity_0 = tester.getField("quantity", 0);

                    assertEquals(10, quantity_0.getValue());
                    assertEquals("yellow", table.getRowStyle(0));

                    quantity_0.setValue(15);

                    assertEquals(15, quantity_0.getValue());
                    assertEquals(new BigDecimal("165.00"), price_0.getValue());
                    assertEquals("red", table.getRowStyle(0));
                }).test();
    }  // end method testChangeTableSample

    @Test public void testDisabledTable() {
        load("tekgenesis.showcase.DisabledTable",
                f -> {
                    f.getField("field3", 0).setValue(true);

                    // check that one column is disabled due to its disable condition
                    assertTrue(f.getField("field2", 0).isDisabled());
                    assertFalse(f.getField("field1", 0).isDisabled());

                    // secondTable must be all disabled except for 1st column
                    secondTableSecondColumnDisabled(f);

                    f.getField("firstBool").setValue(true);

                    // check that in the first table is disabled all the first row, and not the second one
                    assertTrue(f.getField("field1", 0).isDisabled());
                    assertFalse(f.getField("field1", 1).isDisabled());
                    assertFalse(f.getField("field2", 1).isDisabled());

                    // secondTable must be all disabled except for 1st column
                    secondTableDisabled(f);

                    f.getField("firstBool").setValue(false);

                    // check that one column is disabled due to its disable condition
                    assertTrue(f.getField("field2", 0).isDisabled());
                    assertFalse(f.getField("field1", 0).isDisabled());

                    // secondTable must be all disabled except for 1st column
                    secondTableSecondColumnDisabled(f);
                }).test();
    }

    @Test public void testFormTableShowcase() {
        load("tekgenesis.showcase.ClassroomForm",
                f -> {
                    final TableTester students = f.getTable("students");
                    assertEquals(10, students.size());

                    students.nextPage();

                    f.click("clear");
                }).sync(f -> {
                final TableTester students = f.getTable("students");
                assertEquals(0, students.size());

                f.click("random");
            }).sync(f -> {
                final TableTester students = f.getTable("students");
                final Integer     count    = ensureNotNull((Integer) f.getField("rows").getValue());
                assertEquals(count.intValue(), students.size());

                f.click("clear");
            }).sync(f -> {
                f.getField("room").setValue("4B");
                // Id change will trigger a on_change!!
                f.getField("idKey").setValue(1);
            }).sync(tester -> {
                final FormTester.FieldTester id   = tester.getField("idKey");
                final FormTester.FieldTester room = tester.getField("room");

                assertEquals(1, id.getValue());
                assertEquals("4B", room.getValue());
            }).test();
    }  // end method testFormTableShowcase

    @Test public void testFormTableShowcaseAddRemove() {
        load("tekgenesis.showcase.ClassroomForm",
                f -> {
                    final TableTester students = f.getTable("students");
                    students.removeAll();

                    addRow(f, students, 0);

                    checkRow(f, 0, 0);

                    for (int i = 1; i <= 6; i++)
                        addRow(f, students, i);

                    assertEquals(7, students.size());
                    assertEquals(5, students.visibleRows());
                    checkRow(f, 4, 6);

                    students.nextPage();
                    assertEquals("MALE", f.getField("gender", 4).getValue());
                    students.previousPage();

                    students.removeLast();

                    assertEquals(6, students.size());
                    assertEquals(5, students.visibleRows());
                    checkRow(f, 4, 5);

                    students.removeLast();

                    assertEquals(5, students.size());
                    assertEquals(5, students.visibleRows());
                    checkRow(f, 4, 6);

                    students.removeLast();

                    assertEquals(4, students.size());
                    assertEquals(4, students.visibleRows());
                    checkRow(f, 3, 3);

                    assertEquals(7, students.getVisibleColumns());
                    f.getField("showFirstName").setValue(false);
                    assertEquals(6, students.getVisibleColumns());
                }).test();
    }  // end method testFormTableShowcaseAddRemove

    @Test public void testGroupHideShowcase() {
        load("tekgenesis.showcase.TableGroupHideShowcase",
                f -> {
                    final TableTester table = f.getTable("table");
                    assertEquals("Table must be loaded", table.size(), 3);

                    assertEquals("Default external value", f.getField("external").getValue(), "A");

                    // Row 0 [A, A]
                    assertTrue("(A,0) == A", f.getField("a", 0).isVisible());
                    assertTrue("(B,0) != B", !f.getField("b", 0).isVisible());
                    assertTrue("(C,0) != C", !f.getField("c", 0).isVisible());
                    assertTrue("(X,0) == A", f.getField("x", 0).isVisible());
                    assertTrue("(Y,0) != B", !f.getField("y", 0).isVisible());
                    assertTrue("(Z,0) != C", !f.getField("z", 0).isVisible());

                    // Row 1 [B, A]
                    assertTrue("(A,1) != A", !f.getField("a", 1).isVisible());
                    assertTrue("(B,1) == B", f.getField("b", 1).isVisible());
                    assertTrue("(C,1) != C", !f.getField("c", 1).isVisible());
                    assertTrue("(X,1) == A", f.getField("x", 1).isVisible());
                    assertTrue("(Y,1) != B", !f.getField("y", 1).isVisible());
                    assertTrue("(Z,1) != C", !f.getField("z", 1).isVisible());

                    f.getField("external").setValue("B");

                    // Row 2 [C, B]
                    assertTrue("(A,2) != A", !f.getField("a", 1).isVisible());
                    assertTrue("(B,2) != B", !f.getField("b", 2).isVisible());
                    assertTrue("(C,2) == C", f.getField("c", 2).isVisible());
                    assertTrue("(X,2) != A", !f.getField("x", 2).isVisible());
                    assertTrue("(Y,2) == B", f.getField("y", 2).isVisible());
                    assertTrue("(Z,2) != C", !f.getField("z", 2).isVisible());
                }).test();
    }

    /** This tests load expressions computation. */
    @Test public void testLoadTableSample() {
        load("tekgenesis.showcase.Table",
                tester -> {
                    final FormTester.FieldTester tax = tester.getField("tax");
                    tax.setValue(new BigDecimal("0.21"));

                    final TableTester table = tester.getTable("table");

                    table.addRow();
                    tester.getField("product", 0).setValue("LCD Sony");
                    tester.getField("unitPrice", 0).setValue(new BigDecimal("10.0"));
                    tester.getField("quantity", 0).setValue(10);

                    table.addRow();
                    tester.getField("product", 1).setValue("LCD Sony");
                    tester.getField("unitPrice", 1).setValue(new BigDecimal("20.0"));
                    tester.getField("quantity", 1).setValue(20);

                    final int lastRow = table.addRow();
                    tester.getField("product", lastRow).setValue("Mac book pro");
                    tester.getField("unitPrice", lastRow).setValue(new BigDecimal("10.0"));
                    tester.getField("quantity", lastRow).setValue(6);

                    table.firstPage();

                    assertEquals(new BigDecimal("0.21"), tester.getField("tax").getValue());

                    assertEquals("LCD Sony", tester.getField("product", 0).getValue());

                    assertEquals(new BigDecimal("10.00"), tester.getField("unitPrice", 0).getValue());

                    assertEquals(10, tester.getField("quantity", 0).getValue());

                    // Dependency row <-- quantity.
                    assertEquals("yellow", table.getRowStyle(0));
                    assertEquals("red", table.getRowStyle(1));

                    // Dependency taxPrice <-- tax.
                    assertEquals(new BigDecimal("2.10"), tester.getField("taxPrice", 0).getValue());

                    // Dependency taxPrice_0 <-- price_0 && unitPrice_0 <-- price_0.
                    assertEquals(new BigDecimal("121.00"), tester.getField("price", 0).getValue());

                    // Dependency total <-- price.
                    assertEquals(new BigDecimal("677.60"), tester.getField("total").getValue());

                    // Add row dependency check.
                    final int last = table.addRow();
                    tester.getField("unitPrice", last).setValue(new BigDecimal("15.0"));
                    tester.getField("quantity", last).setValue(2);
                    assertEquals(new BigDecimal("713.90"), tester.getField("total").getValue());

                    // Remove row dependency check.
                    table.removeLast();
                    assertEquals(new BigDecimal("677.60"), tester.getField("total").getValue());

                    // Remove row dependency check.
                    table.removeRow(0);
                    assertEquals(new BigDecimal("556.60"), tester.getField("total").getValue());
                }).test();
    }  // end method testLoadTableSample

    @Test public void testMultipleOnChange() {
        load("tekgenesis.showcase.TableMultipleOnChange",
                f -> {
                    final TableTester items = f.getTable("items");
                    f.getField("column1", items.addRow()).setValue(1);
                    f.getField("column1", items.addRow()).setValue(2);
                    f.getField("column1", items.addRow()).setValue(3);

                    f.getField("field").setValue(1);
                }).sync(f -> {
                assertEquals(2, f.getField("twoTimesField").getValue());

                assertEquals(3, f.getField("column2", 0).getValue());
                assertEquals(4, f.getField("column2", 1).getValue());
                assertEquals(5, f.getField("column2", 2).getValue());

                assertEquals(12, f.getField("sum").getValue());
                assertEquals(3, f.getField("otherField").getValue());

                assertEquals(3, f.getField("otherField").getValue());

                final String result = "twoTimesField changed, hasCurrent(): false\n" +
                    "column2 changed, hasCurrent(): true, getCurrentIndex(): 0\n" +
                    "otherField changed, hasCurrent(): false\n" +
                    "sum changed, hasCurrent(): false\n" +
                    "column2 changed, hasCurrent(): true, getCurrentIndex(): 1\n" +
                    "otherField changed, hasCurrent(): false\n" +
                    "column2 changed, hasCurrent(): true, getCurrentIndex(): 2\n" +
                    "otherField changed, hasCurrent(): false\n";

                assertEquals(result, f.getField("progressDebug").getValue());

                f.field("any").click();
            }).sync(f -> {
                final String result = "twoTimesField changed, hasCurrent(): false\n" +
                    "column2 changed, hasCurrent(): true, getCurrentIndex(): 0\n" +
                    "otherField changed, hasCurrent(): false\n" +
                    "sum changed, hasCurrent(): false\n" +
                    "column2 changed, hasCurrent(): true, getCurrentIndex(): 1\n" +
                    "otherField changed, hasCurrent(): false\n" +
                    "column2 changed, hasCurrent(): true, getCurrentIndex(): 2\n" +
                    "otherField changed, hasCurrent(): false\n" +
                    "anyClick clicked! changed, hasCurrent(): false\n";

                assertEquals(result, f.getField("progressDebug").getValue());
            }).test();
    }  // end method testMultipleOnChange

    @Test public void testPermissionsFormTable() {
        load("tekgenesis.authorization.PermissionsForm", f -> f.getSuggestBox("role").suggest("Bot", 0))  //
        .sync(f -> f.field("domain").setValue("tekgenesis.showcase")).sync(f -> {
                final TableTester table = f.table("applications");
                assertTrue("Applications should not be empty", table.visibleRows() > 0);
            }).test();
    }

    @Test public void testRelatedTablesShowcase() {
        load("tekgenesis.showcase.RelatedTablesForm",
                f -> {
                    final TableTester firstTable = f.getTable("first");
                    firstTable.addRow();
                    f.getField("a", 0).setValue(3);
                    firstTable.addRow();
                    f.getField("a", 1).setValue(2);

                    assertTrue("Should have 'Must be greater than ten' error.", f.getField("ftot").hasError());

                    firstTable.addRow();
                    f.getField("a", 2).setValue(3);

                    assertTrue("Should not have 'Must be greater than ten' error.", !f.getField("ftot").hasError());

                    final TableTester secondTable = f.getTable("second");

                    secondTable.addRow();
                    f.getField("y", 0).setValue(2);

                    final int actual = ensureNotNull((Integer) f.getField("stot").getValue());
                    assertEquals(18, actual);      // Related tables total value should be 18.

                    firstTable.removeRow(0);      // On row deleted, check should fail again.
                    assertTrue("Should have 'Must be greater than ten' error.", f.getField("ftot").hasError());
                }).test();
    }

    /** This tests onchange expressions computation. */
    @Test public void testRemovesTableSample() {
        load("tekgenesis.showcase.Table",
                tester -> {
                    final FormTester.FieldTester tax = tester.getField("tax");
                    tax.setValue(new BigDecimal("0.21"));

                    final TableTester table = tester.getTable("table");

                    table.addRow();
                    tester.getField("product", 0).setValue("LCD Sony");
                    tester.getField("unitPrice", 0).setValue(new BigDecimal("10.0"));
                    tester.getField("quantity", 0).setValue(10);

                    table.addRow();
                    tester.getField("product", 1).setValue("LCD Sony");
                    tester.getField("unitPrice", 1).setValue(new BigDecimal("20.0"));
                    tester.getField("quantity", 1).setValue(20);

                    final Random random = new Random(1);
                    for (int i = 0; i < 3; i++)
                        addRandomRows(tester, table, random);

                    final int lastRow = table.addRow();
                    tester.getField("product", lastRow).setValue("Mac book pro");
                    tester.getField("unitPrice", lastRow).setValue(new BigDecimal("10.0"));
                    tester.getField("quantity", lastRow).setValue(6);

                    table.firstPage();

                    table.removeRow(0);

                    assertEquals(new BigDecimal("20.00"), tester.getField("unitPrice", 0).getValue());
                    assertEquals("red", table.getRowStyle(0));
                    assertEquals("yellow", table.getRowStyle(1));
                    assertEquals(new BigDecimal("254.00"), tester.getField("unitPrice", 2).getValue());

                    table.removeRow(2);

                    assertEquals(new BigDecimal("606.00"), tester.getField("unitPrice", 2).getValue());
                }).test();
    }  // end method testRemovesTableSample

    @Test public void testSortableTable() {
        load("tekgenesis.showcase.SortableIntTable/false",
                f -> {
                    final TableTester widgets = f.getTable("table");

                    widgets.addRow();
                    f.getField("integer", 0).setValue(9);
                    f.getField("string", 0).setValue("A");

                    widgets.addRow();
                    f.getField("integer", 1).setValue(0);
                    f.getField("string", 1).setValue("Z");

                    widgets.addRow();
                    f.getField("integer", 2).setValue(9);
                    f.getField("string", 2).setValue("Z");

                    widgets.addRow();
                    f.getField("integer", 3).setValue(9);
                    f.getField("string", 3).setValue("L");

                    widgets.sort(singletonList(tuple(0, ASCENDING)));

                    assertEquals(0, f.getField("integer", 0).getValue());
                    assertEquals("Z", f.getField("string", 0).getValue());

                    assertEquals(9, f.getField("integer", 1).getValue());
                    assertEquals("A", f.getField("string", 1).getValue());

                    assertEquals(9, f.getField("integer", 2).getValue());
                    assertEquals("Z", f.getField("string", 2).getValue());

                    assertEquals(9, f.getField("integer", 3).getValue());
                    assertEquals("L", f.getField("string", 3).getValue());

                    widgets.sort(asList(tuple(0, ASCENDING), tuple(1, DESCENDING)));

                    assertEquals(0, f.getField("integer", 0).getValue());
                    assertEquals("Z", f.getField("string", 0).getValue());

                    assertEquals(9, f.getField("integer", 1).getValue());
                    assertEquals("Z", f.getField("string", 1).getValue());

                    assertEquals(9, f.getField("integer", 2).getValue());
                    assertEquals("L", f.getField("string", 2).getValue());

                    assertEquals(9, f.getField("integer", 3).getValue());
                    assertEquals("A", f.getField("string", 3).getValue());

                    widgets.sort(singletonList(tuple(1, ASCENDING)));

                    assertEquals(9, f.getField("integer", 0).getValue());
                    assertEquals("A", f.getField("string", 0).getValue());

                    assertEquals(9, f.getField("integer", 1).getValue());
                    assertEquals("L", f.getField("string", 1).getValue());

                    assertEquals(0, f.getField("integer", 2).getValue());
                    assertEquals("Z", f.getField("string", 2).getValue());

                    assertEquals(9, f.getField("integer", 3).getValue());
                    assertEquals("Z", f.getField("string", 3).getValue());
                }).test();
    }  // end method testSortableTable

    /*@Test public void testFilterableTable() {
     *  load("tekgenesis.showcase.FilterableTable",
     *          f -> {
     *              final TableTester items = f.getTable("items");
     *              items.filter();
     *              items.getCellValue(3, 0);
     *              assertEquals(items.size(), 2);
     *          }).test();
     *}*/

    @Test public void testTableKeysNavigation() {
        load("tekgenesis.showcase.ClassroomForm",
                f -> {
                    final TableTester students = f.getTable("students");

                    assertTrue(students.getPage() == 0);
                    students.keyDown(0);

                    assertTrue(students.getPage() == 0);
                    students.keyDown(1);

                    assertTrue(students.getPage() == 0);
                    students.keyDown(2);

                    assertTrue(students.getPage() == 0);
                    students.keyDown(3);

                    assertTrue(students.getPage() == 0);
                    students.keyDown(4);

                    assertTrue(students.getPage() == 1);
                    students.firstPage();

                    assertTrue(students.getPage() == 0);
                }).test();
    }

    @SuppressWarnings("OverlyLongMethod")
    @Test public void testTableLocalGlobalOptionsForm() {
        load("tekgenesis.showcase.TableLocalGlobalOptionsShowcase",
                f -> {
                    final TableTester table = f.getTable("table");
                    assertEquals(10, table.size());

                    final FormTester.FieldTester stringOptional = f.getField("stringOptional");
                    stringOptional.setValue("One");
                    assertEquals("One", stringOptional.getValue());
                    assertTrue(stringOptional.hasNullOption());
                    final FormTester.FieldTester stringRequired = f.getField("stringRequired");
                    stringRequired.setValue("One");
                    assertEquals("One", stringRequired.getValue());
                    assertFalse(stringRequired.hasNullOption());
                    final FormTester.FieldTester intCombos = f.getField("intCombos");
                    assertEquals(1, intCombos.getValue());
                    assertFalse(intCombos.hasNullOption());
                    final FormTester.FieldTester intCombosOptional = f.getField("intCombosOptional");
                    assertEquals(1, intCombosOptional.getValue());
                    assertTrue(intCombosOptional.hasNullOption());
                    final FormTester.FieldTester stringCombos = f.getField("stringCombos");
                    assertEquals("Two", stringCombos.getValue());
                    final MultipleModel model = f.getFormModel().getMultiple("table");

                    final FormTester.FieldTester oneOption = f.getField("oneOptionCombo");
                    assertNull(oneOption.getValue());
                    assertTrue(oneOption.hasNullOption());
                    oneOption.setValue(1);
                    assertFalse(oneOption.hasNullOption());

                    final FormTester.FieldTester threeOptions = f.getField("threeOptions");
                    assertNull(threeOptions.getValue());
                    assertTrue(threeOptions.hasNullOption());
                    threeOptions.setValue(3);
                    assertFalse(threeOptions.hasNullOption());

                    for (int i = 0; i < 10; i++) {
                        final RowModel row = model.getRow(i);
                        // Check default options
                        assertEquals(6, row.getOptions(row.widgetByName("b")).size());
                        // Check valid options
                        assertEquals(6, row.getOptions(row.widgetByName("b1")).size());
                        // Check table default options
                        assertEquals(2, row.getOptions(row.widgetByName("c")).size());
                        // Check specific row overridden options
                        if (i % 3 == 0) assertEquals(2, row.getOptions(row.widgetByName("d")).size());
                        else assertEquals(6, row.getOptions(row.widgetByName("d")).size());
                    }

                    f.click("resetCombos");
                }).sync(f -> {
                final FormTester.FieldTester oneOption    = f.getField("oneOptionCombo");
                final FormTester.FieldTester threeOptions = f.getField("threeOptions");
                assertNull(oneOption.getValue());
                assertTrue(oneOption.hasNullOption());
                assertNull(threeOptions.getValue());
                assertTrue(threeOptions.hasNullOption());

                f.click("change");
            }).sync(f -> {
                // After change options execution

                final TableTester table = f.getTable("table");
                assertEquals(10, table.size());

                final MultipleModel model = f.getFormModel().getMultiple("table");

                for (int i = 0; i < 10; i++) {
                    final RowModel row = model.getRow(i);
                    // Check default options still the same
                    assertEquals(6, row.getOptions(row.widgetByName("b")).size());
                    // Check valid options still the same
                    assertEquals(2, row.getOptions(row.widgetByName("b1")).size());
                    // Check table default options are UPDATED
                    assertEquals(3, row.getOptions(row.widgetByName("c")).size());
                    // Check specific row overridden options are UPDATED
                    assertEquals(i % 3 == 0 ? 3 : 6, row.getOptions(row.widgetByName("d")).size());
                }

                f.click("button", 0);
            }).sync(f -> {
                // After trying to set A to b1 whose options are B and D execution
                assertEquals(f.getFormModel().getMultiple("table").getRow(0).get("b1"), "A");
                final FormTester.FieldTester b1 = f.getField("b1", 0);
                assertTrue(b1.hasError());
                assertEquals(b1.getMsg().getText(), MetadataFormMessages.MSGS.invalidValueForOptionWidget("A", "b1"));
            }).test();
    }  // end method testTableLocalGlobalOptionsForm

    @Test public void testTablesSyncTest() {
        load("tekgenesis.showcase.MultipleTablesSync",
                f -> {
                    // Assert initial load
                    final TableTester pendings = f.table("pendings");
                    assertEquals(0, pendings.visibleRows());
                    assertEquals(0, f.field("tabs").getScalarValue());
                    assertEquals("0 pendings.", f.field("summary").getScalarValue());

                    f.field("rows").setValue(3);
                }).sync(f -> {
                // Assert sync
                final TableTester pendings = f.table("pendings");
                assertEquals(3, pendings.visibleRows());

                // Assert tab changed
                assertEquals(1, f.field("tabs").getScalarValue());
                assertEquals("3 pendings.", f.field("summary").getScalarValue());
            }).test();
    }

    @NotNull @Override public String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS, AUTHORIZATION_MODELS };
    }

    private void addRandomRows(FormTester tester, TableTester table, Random random) {
        final int row = table.addRow();
        tester.getField("product", row).setValue("LCD " + ((char) random.nextInt(256)));
        tester.getField("unitPrice", row).setValue(new BigDecimal(random.nextInt(1000)));
        tester.getField("quantity", row).setValue(random.nextInt(10));
    }

    private void addRow(FormTester tester, TableTester students, int i) {
        final int row = students.addRow();
        tester.getField("dni", row).setValue(i);
        tester.getField("firstName", row).setValue("first" + i);
        tester.getField("lastName", row).setValue("last" + i);
        tester.getField("age", row).setValue(i);
        tester.getField("gender", row).setValue("MALE");
    }

    private void checkRow(FormTester tester, int row, int value) {
        assertEquals(value, tester.getField("dni", row).getValue());
        assertEquals("first" + value, tester.getField("firstName", row).getValue());
        assertEquals("last" + value, tester.getField("lastName", row).getValue());
        assertEquals(value, tester.getField("age", row).getValue());
        assertEquals("MALE", tester.getField("gender", row).getValue());
    }

    private void secondTableDisabled(FormTester f) {
        assertTrue(f.getField("field4", 0).isDisabled());
        assertTrue(f.getField("field5", 0).isDisabled());
        assertTrue(f.getField("field4", 1).isDisabled());
        assertTrue(f.getField("field5", 1).isDisabled());
    }

    private void secondTableSecondColumnDisabled(FormTester f) {
        assertFalse(f.getField("field4", 0).isDisabled());
        assertFalse(f.getField("field4", 1).isDisabled());
        assertTrue(f.getField("field5", 0).isDisabled());
        assertTrue(f.getField("field5", 1).isDisabled());
    }
}  // end class SuiTablesTest
