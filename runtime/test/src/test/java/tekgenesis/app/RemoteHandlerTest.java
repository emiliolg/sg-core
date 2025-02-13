
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.math.BigDecimal;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.exception.FieldValueException;
import tekgenesis.common.invoker.exception.InvokerInvocationException;
import tekgenesis.common.service.Status;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.sales.basic.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.DateOnly.date;
import static tekgenesis.common.core.DateTime.dateTime;
import static tekgenesis.common.core.Decimals.scaleAndCheck;
import static tekgenesis.common.invoker.HttpInvokers.invoker;
import static tekgenesis.common.service.Status.*;
import static tekgenesis.sales.basic.service.ReturnTypesHandler.IMAGE_BASE_64;

/**
 * Test SuiGeneris http service stack.
 */
@Category(AppTests.class)
@SuppressWarnings({ "MagicNumber", "JavaDoc", "DuplicateStringLiteralInspection" })
public class RemoteHandlerTest {

    //~ Methods ......................................................................................................................................

    @Test public void testEmptyBody() {
        final String s = createProductRemoteHandler().bodyEmpty().get();
        assertThat(s).isEqualTo("empty");
    }

    @Test public void testFailureGet() {
        try {
            createProductRemoteHandler().failure().execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
        }
    }

    @Test public void testFailureMessage() {
        final Status[] statuses = { BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED };
        for (final Status status : statuses) {
            try {
                createProductRemoteHandler().failureWithMessage(status.code()).execute();
                failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
            }
            catch (final InvokerInvocationException e) {
                assertThat(e.getStatus()).isEqualTo(status);
                assertThat(e.getData()).isEqualTo(status.name());
            }
        }
    }

    @Test public void testFailureMessageStr() {
        final Status[] statuses = { BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED };
        for (final Status status : statuses) {
            try {
                createProductRemoteHandler().failureWithMessageStr(status.code()).execute();
                failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
            }
            catch (final InvokerInvocationException e) {
                assertThat(e.getStatus()).isEqualTo(status);
                assertThat(e.getData()).isEqualTo(status.name());
            }
        }
    }

    @Test public void testFailurePost() {
        try {
            createProductRemoteHandler().failure(new Product()).execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
        }
    }

    @Test public void testGetGenericStruct() {
        final Seq<Product> products = createProductRemoteHandler().featured().get();
        assertThat(products).isNotNull();
        assertThat(products).hasSize(3);
    }

    @Test public void testGetStruct() {
        final Product content = createProductRemoteHandler().show("1").get();
        assertThat(content).isNotNull();
        assertThat(content.getProductId()).isEqualTo("1");
    }

    @Test public void testMultipleBody() {
        final Seq<Product> products = listOf(new Product(), new Product(), new Product());
        final String       s        = createProductRemoteHandler().bodyMultiple(products).get();
        assertThat(s).isEqualTo("3");
    }

    @Test public void testParameters() {
        final String parameters = createProductRemoteHandler().parameters(2014, "10", "20", 5).get();
        assertThat(parameters).isNotNull();
        assertThat(parameters).isEqualTo("Id 2014 from 10 to 20 step 5");
    }

    @Test public void testParametersConversions() {
        final String parameters = createProductRemoteHandler().conversions(2014,
                    3.1415926535,
                    new BigDecimal(3.1415),
                    DateOnly.date(2014, 1, 31),
                    DateTime.dateTime(2014, 1, 31, 6, 47),
                    "SC",
                    true,
                    State.ACTIVE).get();
        assertThat(parameters).isNotNull();
        assertThat(parameters).isEqualTo(
            "a:'2014', b:'3.1415926535', c:'3.1415', d:'2014-01-31', " +
            "e:'2014-01-31T09:47:00Z', f:'SC', g:'true', h:'ACTIVE'");
    }

    @Test public void testParametersConversionsException() {
        // Remote handler should throw exception for invalid values
        try {
            final double illegalSignValue = -3.1415926535;
            createProductRemoteHandler().conversions(2014,
                    illegalSignValue,
                    new BigDecimal(3.1415),
                    DateOnly.date(2014, 1, 31),
                    DateTime.dateTime(2014, 1, 31, 6, 47),
                    "SC",
                    true,
                    State.ACTIVE)
                .get();
            failBecauseExceptionWasNotThrown(FieldValueException.class);
        }
        catch (final FieldValueException e) {
            assertThat(e).hasMessageEndingWith("cannot be negative.");
        }
    }

    @Test public void testParametersMultipleConversions() {
        final String parameters = createProductRemoteHandler().multiples(listOf(2014, 2015),
                    listOf(3.1415926535, 2.14),
                    listOf(new BigDecimal(3.1415), new BigDecimal(2.14)),
                    listOf(DateOnly.date(2014, 1, 31), DateOnly.date(2014, 1, 31)),
                    listOf(DateTime.dateTime(2014, 1, 31, 6, 47), DateTime.dateTime(2014, 1, 31, 6, 48)),
                    listOf("SC", "CG"),
                    listOf(true, false),
                    listOf(State.ACTIVE, State.DEACTIVE))
                                  .get();
        assertThat(parameters).isEqualTo(
            "a:'2014|2015', b:'3.1415926535|2.14', c:'3.1415|2.1400', d:'2014-01-31', " +
            "e:'2014-01-31T09:47:00Z|2014-01-31T09:48:00Z', f:'SC|CG', g:'true|false', h:'ACTIVE|DEACTIVE'");
    }

    @Test public void testParametersMultipleConversionsException() {
        // Remote handler should throw exception for invalid values
        try {
            final BigDecimal exceededPrecision = new BigDecimal(1234.5678);
            createProductRemoteHandler().multiples(listOf(2014),
                    listOf(2.14),
                    listOf(exceededPrecision),
                    listOf(DateOnly.date(2014, 1, 31)),
                    listOf(DateTime.dateTime(2014, 1, 31, 6, 47)),
                    listOf("SC"),
                    listOf(true),
                    listOf(State.ACTIVE))
                .get();
            failBecauseExceptionWasNotThrown(FieldValueException.class);
        }
        catch (final FieldValueException e) {
            assertThat(e).hasMessageContaining("precision 8 exceeds 6");
        }
    }

    @Test public void testPostWithBody() {
        final Product payload = new Product();
        payload.setModel("Model");
        final Product related = createProductRemoteHandler().related("1", payload).get();
        assertThat(related).isNotNull();
        assertThat(related.getModel()).isEqualTo(payload.getModel());
    }

    @Test public void testReturnTypes() {
        final ReturnTypesHandlerRemote handler = createReturnTypesRemoteHandler();

        assertThat(handler.returnBoolean().get()).isTrue();
        assertThat(handler.returnReal().get()).isEqualTo(.99);
        assertThat(handler.returnDecimal().get()).isEqualTo(scaleAndCheck("d", new BigDecimal(.99), false, 10, 2));
        assertThat(handler.returnInt().get()).isEqualTo(1);
        assertThat(handler.returnDateTime().get()).isEqualTo(dateTime(2014, 1, 31, 6, 47));
        assertThat(handler.returnDate().get()).isEqualTo(date(2014, 1, 31));
        assertThat(handler.returnType().get().getProductId()).isEqualTo("xyz");
        assertThat(handler.returnEnum().get()).isEqualTo(State.ACTIVE);
        assertThat(handler.returnAny().get()).isEqualTo(IMAGE_BASE_64.getBytes());
        assertThat(handler.returnHtml().getStatus()).isEqualTo(Status.CREATED);
    }

    @Test public void testSimpleBody() {
        final String s = createProductRemoteHandler().bodySimple("echo mamacita").get();
        assertThat(s).isEqualTo("echo mamacita");
    }

    private ProductHandlerRemote createProductRemoteHandler() {
        return new ProductHandlerRemote(invoker(app.getServerUrl()));
    }

    private ReturnTypesHandlerRemote createReturnTypesRemoteHandler() {
        return new ReturnTypesHandlerRemote(invoker(app.getServerUrl()));
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static ApplicationRule app = new ApplicationRule() {
            @Override protected void before()
                throws Exception
            {
                super.before();
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.autoLogin = "admin:password";
                Context.getEnvironment().put(shiroProps);
            }
        };
}  // end class RemoteHandlerTest
