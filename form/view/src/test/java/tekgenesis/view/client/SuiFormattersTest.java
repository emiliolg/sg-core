
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

import tekgenesis.metadata.form.widget.InputHandlerMetadata;
import tekgenesis.type.Types;
import tekgenesis.view.client.formatter.InputHandler;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.ImmutableList.empty;
import static tekgenesis.metadata.form.widget.InputHandlerMetadata.inheritFromType;
import static tekgenesis.metadata.form.widget.PredefinedMask.RAW;
import static tekgenesis.view.client.formatter.InputHandlerFactory.create;

/**
 * Client side testing of widget's formatters and inputHandlers.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiFormattersTest extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testFormatterBoolean() {
        final InputHandler<Boolean> inputHandler = cast(create(inheritFromType(), Types.booleanType(), false));

        assertEquals("True", inputHandler.format(true));
        assertEquals("False", inputHandler.format(false));

        assertEquals("True", inputHandler.toString(true));
        assertEquals("False", inputHandler.toString(false));

        final Boolean eTrue = inputHandler.fromString("True");
        assertNotNull(eTrue);
        assertTrue(eTrue);
        final Boolean eFalse = inputHandler.fromString("False");
        assertNotNull(eFalse);
        assertFalse(eFalse);
    }

    @Test public void testFormatterDecimal() {
        final InputHandler<BigDecimal> handler = cast(create(inheritFromType(), Types.decimalType(10, 2), true));
        // enter edition mode: show the display value
        assertEquals("10.2", handler.toString(new BigDecimal("10.2")));
        // filter new user input
        assertEquals("-1022.", handler.filter("10,22.,A-"));
        // leave edition mode: get the model value again
        assertEquals(new BigDecimal("-10.22"), handler.fromString("-10.22"));
        // leave edition mode: format the value to display it to the user
        assertEquals("-10.22", handler.format(new BigDecimal("-10.22")));
    }

    @Test public void testFormatterInt() {
        final InputHandler<Integer> handler = cast(create(inheritFromType(), Types.intType(), false));
        testIntField(handler);
    }

    @Test public void testFormatterRaw() {
        final InputHandler<Integer> intHandler = cast(create(InputHandlerMetadata.mask(RAW, empty()), Types.intType(), false));
        testIntField(intHandler);

        assertEquals("2016", intHandler.toString(2016));
        assertEquals("2016", intHandler.format(2016));

        final InputHandler<BigDecimal> decimalHandler = cast(create(InputHandlerMetadata.mask(RAW, empty()), Types.decimalType(), false));

        assertEquals("1022.", decimalHandler.filter("10,22.,A"));

        assertEquals("2016.32", decimalHandler.toString(new BigDecimal("2016.32")));
        assertEquals("2016.32", decimalHandler.format(new BigDecimal("2016.32")));

        assertEquals("-2016.32", decimalHandler.toString(new BigDecimal("-2016.32")));
        assertEquals("-2016.32", decimalHandler.format(new BigDecimal("-2016.32")));
    }

    @NotNull @Override public String[] getProjectPaths() {
        return EMPTY_ARRAY;
    }

    private void testIntField(InputHandler<Integer> handler) {
        // enter edition mode: show the display value
        assertEquals("5", handler.toString(5));
        // filter new user input
        assertEquals("56", handler.filter("5 R., 6"));
        // leave edition mode: get the model value again
        assertEquals(new Integer(56), handler.fromString("56"));
        // leave edition mode: format the value to display it to the user
        assertEquals("56", handler.format(56));
    }

    //~ Static Fields ................................................................................................................................

    private static final String[] EMPTY_ARRAY = {};
}  // end class SuiFormattersTest
