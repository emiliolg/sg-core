
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

import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;

@SuppressWarnings({ "JUnit4AnnotatedMethodInJUnit3TestCase", "DuplicateStringLiteralInspection" })
public class SuiFunctionsTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testReadOnlyFunc() {
        load("tekgenesis.showcase.ReadOnlyForm",
                f -> {
                    // everything is in readOnly

                    assertTrue(f.getField("input").isReadOnly());

                    final FormTester.FieldTester str = f.getField("str");
                    assertTrue(str.isReadOnly());
                    assertEquals("Read only", str.getValue());

                    assertTrue(f.getField("toggle").isReadOnly());
                    assertTrue(f.getField("sync").isReadOnly());
                    f.click("cancel");
                }).sync(f -> {
                // everything not read only, but str is disabled due to check
                final FormTester.FieldTester str = f.getField("str");
                assertFalse(str.isReadOnly());
                assertEquals("No read only", str.getValue());

                final FormTester.FieldTester input = f.getField("input");
                assertTrue(input.isDisabled());
                assertFalse(f.getField("toggle").isReadOnly());
                assertFalse(f.getField("sync").isReadOnly());

                // uncheck to see if disables ok
                f.getField("check").setValue(false);
                assertFalse(input.isDisabled());

                f.click("sync");
            }).sync(f -> {
                // everything keeps not read only
                final FormTester.FieldTester str = f.getField("str");
                assertFalse(str.isReadOnly());
                assertEquals("No read only", str.getValue());

                assertFalse(f.getField("input").isReadOnly());
                assertFalse(f.getField("toggle").isReadOnly());
                assertFalse(f.getField("sync").isReadOnly());

                f.click("toggle");
            }).sync(f -> {
                // everything read only again
                final FormTester.FieldTester str = f.getField("str");
                assertTrue(str.isReadOnly());
                assertEquals("Read only", str.getValue());

                assertTrue(f.getField("input").isReadOnly());
                assertTrue(f.getField("toggle").isReadOnly());
                assertTrue(f.getField("sync").isReadOnly());

                f.click("cancel");
            }).sync(f -> {
                // everything not read only again
                final FormTester.FieldTester str = f.getField("str");
                assertFalse(str.isReadOnly());
                assertEquals("No read only", str.getValue());

                assertFalse(f.getField("input").isReadOnly());
                assertFalse(f.getField("toggle").isReadOnly());
                assertFalse(f.getField("sync").isReadOnly());
            }).test();
    }  // end method testReadOnlyFunc

    @NotNull @Override protected String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS };
    }
}  // end class SuiFunctionsTest
