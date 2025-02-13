
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.view.client.FormTester.FieldTester;
import tekgenesis.view.client.FormTester.WidgetDefTester;
import tekgenesis.view.client.ui.TableTester;

import static com.google.gwt.user.client.Event.as;

import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;

/**
 * Client side testing of widget definitions.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiWidgetDefsTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCoordinateTypeWidget() {
        load("tekgenesis.showcase.CoordinatesForm",
                f -> {
                    f.field("from.lat").setValue(5);
                    f.field("to.lat").setValue(7);
                    assertEquals(2, f.field("latDiff").getScalarValue());
                    f.field("from.lng").setValue(5);
                }).sync(f -> {
                assertEquals(1, f.field("fromCount").getScalarValue());
                f.field("to.lng").setValue(9);
            }).sync(f -> {
                assertEquals(1, f.field("toCount").getScalarValue());
                assertEquals(4, f.field("lngDiff").getScalarValue());
                f.field("to.lng").setValue(7);
            }).sync(f -> {
                assertEquals(2, f.field("toCount").getScalarValue());
                assertEquals(1, f.field("fromCount").getScalarValue());
                assertEquals(2, f.field("lngDiff").getScalarValue());
            }).test();
    }

    @Test public void testFindingButtonsInWidgetDef() {
        load("tekgenesis.showcase.FlightChooserForm", f -> f.widget("chooser").field("button").click()).sync(f -> {
                // Assert that after sync the buttton has been enabled again.
                assertTrue(f.button("chooser.button").isButtonEnabled());
            }).test();
    }

    @Test public void testMultipleInWidget() {
        load("tekgenesis.showcase.MultipleInWidgetForm",
                f -> {
                    final WidgetDefTester widget = f.widget("multiple");

                    // Assert widget initial load, client-side row add, and client-side remove
                    assertMultipleWidgetClient(widget);

                    // Add row on server side
                    widget.field("add").click();
                }).sync(f -> {
                final WidgetDefTester widget = f.widget("multiple");
                // Assert row added on server
                assertMultipleWidgetServer(widget);
            }).test();
    }  // end method testMultipleInWidget

    @Test public void testNestedMultiples() {
        load("tekgenesis.showcase.NestedMultiples",
                f -> {
                    // Assert initial load
                    final TableTester widgets = f.table("widgets");
                    assertEquals(0, widgets.visibleRows());
                    final FieldTester sum = f.field("sum");
                    assertEquals(null, sum.getScalarValue());

                    // Add row
                    widgets.addRow();

                    final WidgetDefTester widget_0 = f.widget("multiple#0");

                    // Assert widget on row #0 initial load, client-side row add, and client-side remove
                    assertMultipleWidgetClient(widget_0);

                    // Add nested row on server side
                    widget_0.field("add").click();
                }).sync(f -> {
                final WidgetDefTester widget_0 = f.widget("multiple#0");
                // Assert the button is now enabled
                assertTrue(f.button("multiple#0.add").isButtonEnabled());
                // Assert nested row added on server
                assertMultipleWidgetServer(widget_0);

                final TableTester widgets = f.table("widgets");
                assertEquals(1, widgets.visibleRows());
                final FieldTester sum = f.field("sum");
                assertEquals(30, sum.getScalarValue());

                // Hack tab event to test fqn ids
                assertFireTabEvent();
            }).test();
    }

    @Test public void testNestedMultiplesOnClick() {
        load("tekgenesis.showcase.NestedMultiples",
                f -> {          //
                    // Assert initial load
                    final TableTester widgets = f.table("widgets");
                    assertEquals(0, widgets.visibleRows());
                    final FieldTester sum = f.field("sum");
                    assertEquals(null, sum.getScalarValue());

                    // Add row
                    widgets.addRow();

                    final WidgetDefTester widget_0 = f.widget("multiple#0");

                    // Assert widget on row #0 initial load, client-side row add, and client-side remove
                    assertMultipleWidgetClient(widget_0);

                    assertEquals(10, sum.getScalarValue());

                    // Assert nested multiples button on_click
                    widget_0.field("click#0").click();
                }).sync(f -> {  // Expect on click sync
                // Assert updated values
                final FieldTester sum = f.field("sum");
                assertEquals(16, sum.getScalarValue());

                final FieldTester label = f.field("multiple#0.label#0");
                assertEquals("Click Value", label.getScalarValue());
            }).test();
    }

    @Test public void testNestedOnChanges() {
        load("tekgenesis.showcase.NestedOnChangesWidgetForm",
                f -> {
                    // Assert initial load
                    final TableTester widgets = f.table("widgets");
                    assertEquals(1, widgets.visibleRows());
                    final TableTester nested = f.table("changes#0.widgets");
                    assertEquals(1, nested.visibleRows());
                    final FieldTester a = f.field("changes#0.nested#0.a");
                    assertEquals(1, a.getScalarValue());
                    assertEquals(1000, f.field("sum").getScalarValue());

                    final FieldTester calls = f.field("calls");
                    assertEquals("d,e,f,sum,c,b", calls.getScalarValue());

                    // Value change should trigger six on_change field-dependant methods
                    a.setValue(2);
                }).sync(f -> {
                final FieldTester calls = f.field("calls");
                assertEquals("d,e,f,sum,c,b,c,b,d,e,f,sum", calls.getScalarValue());
            }).test();
    }

    @Test public void testNestedWidgets() {
        load("tekgenesis.showcase.NestedWidgets",
                f -> {      //
                    f.field("father.id").setValue("Alpha");
                    f.field("father.child.id").setValue("Theta");
                    assertEquals("Alpha Theta", f.field("father.both").getScalarValue());
                    assertEquals("Alpha Theta", f.field("both").getScalarValue());
                    assertEquals("Alpha Theta", f.field("copy").getScalarValue());
                }).test();
    }

    @Test public void testRecursiveWidget() {
        load("tekgenesis.showcase.BinaryTree",
                f -> {
                    // Assert initial load
                    assertEquals(" A  E  F  H  M  R  U  W ", f.field("inorder").getScalarValue());
                    assertEquals("M", f.button("head.button").text());
                    assertEquals("F", f.button("head.left.button").text());
                    assertEquals("E", f.button("head.left.left.button").text());
                    assertEquals("H", f.button("head.left.right.button").text());
                    assertEquals("A", f.button("head.left.left.left.button").text());
                    assertEquals("U", f.button("head.right.button").text());
                    assertEquals("W", f.button("head.right.right.button").text());
                    assertEquals("R", f.button("head.right.left.button").text());

                    final FormTester.ButtonTester next = f.button("element#0");
                    assertEquals("B", next.text());
                    next.click();          // Add 'B' node server side
                }).sync(f -> {
                // Assert element 'B' was consumed
                assertEquals("C", f.button("element#0").text());

                // Assert node 'B' was inserted
                assertEquals(" A  B  E  F  H  M  R  U  W ", f.field("inorder").getScalarValue());
                assertEquals("B", f.button("head.left.left.left.right.button").text());
            }).test();
    }

    @Test public void testSyncInNestedMultiples() {
        load("tekgenesis.showcase.AirportForm",
                f -> {
                    final WidgetDefTester flight0 = f.widget("flight#0");
                    assertEquals(0, flight0.table("sections").size());
                    flight0.field("add").click();
                }).sync(f -> {
                final WidgetDefTester flight0 = f.widget("flight#0");
                assertEquals(1, flight0.table("sections").size());
                assertEquals("300", flight0.field("fromSchedule#0").getScalarValue());
                assertEquals("400", flight0.field("toSchedule#0").getScalarValue());
            }).test();
    }

    @Test public void testWidgetDefFieldOnChange() {
        load("tekgenesis.showcase.CustomerForm",
                f -> {          // Set widget field value
                    f.field("homeAddress.state").setValue("Ciudad Autónoma de Buenos Aires");
                }).sync(f -> {  // Expect on change sync
                assertEquals("Ciudad Autónoma de Buenos Aires", f.field("homeAddress.state").getScalarValue());
                assertEquals("homeChanged", f.field("feedback").getScalarValue());

                // Expect parent reference expression
                f.field("homeAddress.street").setValue("Cucha cucha 222");
                assertEquals("Cucha cucha 222 Ciudad Autónoma de Buenos Aires ", f.field("home").getScalarValue());
            }).test();
    }

    @Test public void testWidgetInMultiple() {
        load("tekgenesis.showcase.WidgetInMultiple",
                f -> {
                    // Assert initial load
                    final TableTester widgets = f.getTable("widgets");
                    assertEquals(3, widgets.visibleRows());

                    // Assert values;
                    assertEquals("Alpha0 Theta0", f.field("father#0.both").getScalarValue());
                    assertEquals(13, f.field("letters#0").getScalarValue());
                    assertEquals("Alpha1 Theta1", f.field("father#1.both").getScalarValue());
                    assertEquals(13, f.field("letters#1").getScalarValue());
                    assertEquals("Alpha2 Theta2", f.field("father#2.both").getScalarValue());
                    assertEquals(13, f.field("letters#2").getScalarValue());
                    assertEquals(39, f.field("sum").getScalarValue());

                    // Update widget def (father) id on row #0
                    f.field("father#0.id").setValue("Mu0");
                    assertEquals("Mu0 Theta0", f.field("father#0.both").getScalarValue());
                    assertEquals(10, f.field("letters#0").getScalarValue());
                    assertEquals(36, f.field("sum").getScalarValue());

                    // Update widget def nested (child) id on row #0
                    f.field("father#0.child.id").setValue("Epsilon0");
                    assertEquals("Mu0 Epsilon0", f.field("father#0.both").getScalarValue());
                    assertEquals(12, f.field("letters#0").getScalarValue());
                    assertEquals(38, f.field("sum").getScalarValue());

                    // Add row
                    widgets.addRow();

                    // Assert values on added row #3
                    assertEquals(4, widgets.visibleRows());
                    assertEquals(" ", f.field("father#3.both").getScalarValue());
                    assertEquals(39, f.field("sum").getScalarValue());

                    // Update widget def values on added row #3
                    f.field("father#3.id").setValue("Alpha3");
                    f.field("father#3.child.id").setValue("Theta3");
                    assertEquals("Alpha3 Theta3", f.field("father#3.both").getScalarValue());
                    assertEquals(51, f.field("sum").getScalarValue());

                    // Add row on server side
                    f.field("add").click();
                }).sync(f -> {
                final TableTester widgets = f.getTable("widgets");
                assertEquals(5, widgets.visibleRows());

                // Assert values on server-added row #4
                assertEquals("Server Alpha Server Theta", f.field("father#4.both").getScalarValue());
                assertEquals(76, f.field("sum").getScalarValue());

                // Update widget def values on server-added row #4
                f.field("father#4.id").setValue("Alpha4");
                f.field("father#4.child.id").setValue("Theta4");
                assertEquals("Alpha4 Theta4", f.field("father#4.both").getScalarValue());
                assertEquals(64, f.field("sum").getScalarValue());
            }).test();
    }  // end method testWidgetInMultiple

    @Test public void testWidgetInMultipleChainedOnChanges() {
        load("tekgenesis.showcase.WidgetInMultipleWithChainedOnChanges",
                f -> {
                    // Assert initial load
                    final TableTester widgets = f.getTable("widgets");
                    assertEquals(1, widgets.visibleRows());

                    final FieldTester field_changes_0_a = f.field("changes#0.a");
                    assertEquals(1, field_changes_0_a.getScalarValue());
                    assertEquals(1, f.field("a#0").getScalarValue());
                    assertEquals(1, f.field("sum").getScalarValue());
                    assertEquals("c,b", f.field("calls").getScalarValue());

                    // Value change should trigger two on_change field-dependant methods
                    field_changes_0_a.setValue(2);
                }).sync(f -> {
                final FieldTester field_changes_0_a = f.field("changes#0.a");
                assertEquals(2, field_changes_0_a.getScalarValue());
                assertEquals(2, f.field("a#0").getScalarValue());
                assertEquals(2, f.field("sum").getScalarValue());
                assertEquals("c,b,c,b", f.field("calls").getScalarValue());

                // Add row client side and expect on_change to be called
                final TableTester widgets = f.getTable("widgets");
                widgets.addRow();
            }).sync(f -> {
                // Assert row added values
                final TableTester widgets = f.getTable("widgets");
                assertEquals(2, widgets.visibleRows());
                assertEquals(3, f.field("sum").getScalarValue());
                assertEquals("c,b,c,b,c,b", f.field("calls").getScalarValue());
            }).test();
    }

    @Test public void testWidgetInMultipleOnClick() {
        load("tekgenesis.showcase.WidgetInMultiple",
                f -> {
                    // Assert initial load
                    final TableTester widgets = f.getTable("widgets");
                    assertEquals(3, widgets.visibleRows());

                    assertEquals("Alpha0 Theta0", f.field("father#0.both").getScalarValue());
                    assertEquals("Alpha1 Theta1", f.field("father#1.both").getScalarValue());
                    assertEquals("Alpha2 Theta2", f.field("father#2.both").getScalarValue());
                    assertEquals(39, f.field("sum").getScalarValue());

                    f.field("father#0.child.click").click();
                }).sync(f -> {
                assertEquals("Alpha0 Click Id", f.field("father#0.both").getScalarValue());
                assertEquals(41, f.field("sum").getScalarValue());
            }).test();
    }

    @Test public void testWidgetTypeDefFieldOnChange() {
        load("tekgenesis.showcase.CustomerTypeForm",
                f -> {                  // Set widget field value
                    f.field("homeAddress.state").setValue("Ciudad Autónoma de Buenos Aires");
                }).sync(f -> {          // Expect on change sync
                assertEquals("Ciudad Autónoma de Buenos Aires", f.field("homeAddress.state").getScalarValue());
                assertEquals("homeChanged", f.field("feedback").getScalarValue());

                // Expect parent reference expression
                f.field("homeAddress.street").setValue("Cucha cucha 222");
                assertEquals("Cucha cucha 222 Ciudad Autónoma de Buenos Aires ", f.field("home").getScalarValue());

                f.click("createWorkAddress");
            }).sync(f -> {      // Expect on click sync
                f.field("workAddress.state").setValue("Ciudad Autónoma de Buenos Aires");
            }).sync(f -> {      // Expect on change sync
                assertEquals("Ciudad Autónoma de Buenos Aires", f.field("workAddress.state").getScalarValue());
                assertEquals("workChanged", f.field("feedback").getScalarValue());

                // Expect parent reference expression
                f.field("workAddress.street").setValue("Cucha cucha 222");
                assertEquals("Cucha cucha 222 Ciudad Autónoma de Buenos Aires ", f.field("work").getScalarValue());
            }).test();
    }

    @NotNull @Override protected String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS };
    }

    private void assertFireTabEvent() {
        final NativeEvent event = Document.get().createKeyDownEvent(false, false, false, false, KeyCodes.KEY_TAB);
        RootInputHandler.getInstance().handleTableSelection(as(event));
    }

    private void assertMultipleWidgetClient(WidgetDefTester widget) {
        // Assert initial load
        final TableTester choices = widget.table("choices");
        assertEquals(0, choices.visibleRows());

        // Add row
        choices.addRow();

        final FieldTester label_0  = widget.field("label#0");
        final FieldTester length_0 = widget.field("length#0");
        final FieldTester sum      = widget.field("sum");

        // Assert values on added row
        assertEquals("Value", label_0.getScalarValue());
        assertEquals(5, length_0.getScalarValue());
        assertEquals(5, sum.getScalarValue());

        // Update added row values
        label_0.setValue("Updated");

        // Assert values
        assertEquals("Updated", label_0.getScalarValue());
        assertEquals(7, length_0.getScalarValue());
        assertEquals(7, sum.getScalarValue());

        // Add rows to test remove
        choices.addRow();
        choices.addRow();
        assertEquals(3, choices.visibleRows());
        assertEquals(17, sum.getScalarValue());

        // Remove row
        choices.removeRow(0);

        // Assert values
        assertEquals("Value", label_0.getScalarValue());
        assertEquals(5, length_0.getScalarValue());
        assertEquals(2, choices.visibleRows());
        assertEquals(10, sum.getScalarValue());
    }  // end method assertMultipleWidgetClient

    private void assertMultipleWidgetServer(WidgetDefTester widget) {
        final TableTester choices = widget.table("choices");
        assertEquals(3, choices.visibleRows());

        // Assert values on server-added row #3
        final FieldTester label_2  = widget.field("label#2");
        final FieldTester length_2 = widget.field("length#2");
        assertEquals("Server Value", label_2.getScalarValue());
        assertEquals(12, length_2.getScalarValue());
        assertEquals(22, widget.field("sum").getScalarValue());

        // Update values on server-added row #3
        label_2.setValue("Server Value Updated");
        assertEquals("Server Value Updated", label_2.getScalarValue());
        assertEquals(20, length_2.getScalarValue());
        assertEquals(30, widget.field("sum").getScalarValue());
    }
}  // end class SuiWidgetDefsTest
