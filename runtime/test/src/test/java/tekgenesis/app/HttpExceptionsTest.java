
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.invoker.exception.InvokerApplicationException;
import tekgenesis.common.invoker.exception.InvokerInvocationException;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.sales.basic.State;
import tekgenesis.sales.basic.service.ExceptionHandlerRemote;
import tekgenesis.sales.basic.service.HandlerError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.invoker.HttpInvokers.invoker;
import static tekgenesis.common.service.Status.BAD_REQUEST;
import static tekgenesis.common.service.Status.INTERNAL_SERVER_ERROR;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.exception;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.exceptionForVoid;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.exceptionWithMsg;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.returnBadRequest;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.returnBadRequestWithObject;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.subroutineIntInvalidException;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.subroutineIntValidException;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.subroutineInvalidException;
import static tekgenesis.sales.basic.service.ExceptionHandlerBase.Routes.subroutineValidException;

/**
 * Test SuiGeneris http service stack.
 */
@Category(AppTests.class)
@SuppressWarnings({ "MagicNumber", "JavaDoc", "DuplicateStringLiteralInspection" })
public class HttpExceptionsTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCommandInvocationAcceptEither() {
        final InvokerCommand<String> fail = createInvoker().resource(exception()).invoke(String.class);

        fail.acceptEither(s -> Assertions.fail("No element should be submitted! Command must fail."),
            e -> assertThat(e.getStatus()).isEqualTo(BAD_REQUEST));

        final InvokerCommand<String> success = createInvoker().resource(exception()).invoke(String.class).onErrorFallback(value -> "Success");

        success.acceptEither(s -> assertThat(s).isEqualTo("Success"),
            e -> Assertions.fail("No exception should be submitted! Command must succeed.", e));
    }

    @Test public void testCommandInvocationOnErrorFallbackFunction() {
        testRemoteCommandInvocationOnErrorFallbackFunction(createInvoker().resource(returnBadRequestWithObject()).invoke(String.class));
    }

    @Test public void testCommandInvocationOnExceptionFallbackFunction() {
        final InvokerCommand<String> command = createInvoker().resource(exception()).invoke(String.class).onExceptionFallback(e -> {
                assertError(e);
                return "Fallback";
            });

        final String result = command.get();
        assertThat(result).isEqualTo("Fallback");
    }

    @Test public void testHandlerApplicationException() {
        assetErrorCommand(createInvoker().resource(exception()).invoke(String.class));
    }

    @Test public void testHandlerApplicationExceptionCausedByValidException() {
        assetErrorCommand(createInvoker().resource(subroutineValidException()).invoke());
    }

    @Test public void testHandlerApplicationExceptionCausedByValidExceptionWithIntReturnType() {
        assetErrorCommand(createInvoker().resource(subroutineIntValidException()).invoke());
    }

    @Test public void testHandlerApplicationExceptionWithMsg() {
        final String            message  = "Parametrized message!";
        final InvokerCommand<?> resource = createInvoker().resource(exceptionWithMsg()).param("msg", message).invoke();
        testHandlerApplicationExceptionWithMsg(message, resource);
    }

    @Test public void testHandlerApplicationExceptionWithVoidReturnType() {
        assetErrorCommand(createInvoker().resource(exceptionForVoid()).invoke());
    }

    @Test public void testHandlerInvocationException() {
        testHandlerInvocationException(createInvoker().resource(returnBadRequest()).invoke());
    }

    @Test public void testHandlerInvocationExceptionCausedByInvalidException() {
        assertInvalidError(createInvoker().resource(subroutineInvalidException()).invoke());
    }

    @Test public void testHandlerInvocationExceptionWithObject() {
        testHandlerInvocationExceptionWithObject(createInvoker().resource(returnBadRequestWithObject()).invoke());
    }

    @Test public void testIntHandlerInvocationExceptionCausedByInvalidExceptionWithIntReturnType() {
        assertInvalidError(createInvoker().resource(subroutineIntInvalidException()).invoke());
    }

    @Test public void testRemoteCommandInvocationAcceptEither() {
        final InvokerCommand<String> fail = createExceptionRemoteHandler().exception();

        fail.acceptEither(s -> Assertions.fail("No element should be submitted! Command must fail."),
            e -> assertThat(e.getStatus()).isEqualTo(BAD_REQUEST));

        final InvokerCommand<String> success = createExceptionRemoteHandler().exception().onErrorFallback(value -> "Success");

        success.acceptEither(s -> assertThat(s).isEqualTo("Success"),
            e -> Assertions.fail("No exception should be submitted! Command must succeed.", e));
    }

    @Test public void testRemoteCommandInvocationOnErrorFallbackFunction() {
        final InvokerCommand<String> stringInvokerCommand = createExceptionRemoteHandler().returnBadRequestWithObject();
        testRemoteCommandInvocationOnErrorFallbackFunction(stringInvokerCommand);
    }

    @Test public void testRemoteCommandInvocationOnExceptionFallbackFunction() {
        final InvokerCommand<String> command = createExceptionRemoteHandler().exception().onExceptionFallback(e -> {
                assertError(e);
                return "Fallback";
            });

        final String result = command.get();
        assertThat(result).isEqualTo("Fallback");
    }

    @Test public void testRemoteHandlerApplicationException() {
        final InvokerCommand<String> command = createExceptionRemoteHandler().exception();
        assetErrorCommand(command);
    }

    @Test public void testRemoteHandlerApplicationExceptionCausedByValidException() {
        assetErrorCommand(createExceptionRemoteHandler().subroutineValidException());
    }

    @Test public void testRemoteHandlerApplicationExceptionCausedByValidExceptionWithIntReturnType() {
        assetErrorCommand(createExceptionRemoteHandler().subroutineIntValidException());
    }

    @Test public void testRemoteHandlerApplicationExceptionWithMsg() {
        final String            message  = "Parametrized message!";
        final InvokerCommand<?> resource = createExceptionRemoteHandler().exceptionWithMsg(message);
        testHandlerApplicationExceptionWithMsg(message, resource);
    }

    @Test public void testRemoteHandlerApplicationExceptionWithVoidReturnType() {
        assetErrorCommand(createExceptionRemoteHandler().exceptionForVoid());
    }

    @Test public void testRemoteHandlerInvocationException() {
        testHandlerInvocationException(createExceptionRemoteHandler().returnBadRequest());
    }

    @Test public void testRemoteHandlerInvocationExceptionCausedByInvalidException() {
        assertInvalidError(createExceptionRemoteHandler().subroutineInvalidException());
    }

    @Test public void testRemoteHandlerInvocationExceptionWithObject() {
        testHandlerInvocationExceptionWithObject(createExceptionRemoteHandler().returnBadRequestWithObject());
    }

    @Test public void testRemoteIntHandlerInvocationExceptionCausedByInvalidExceptionWithIntReturnType() {
        assertInvalidError(createExceptionRemoteHandler().subroutineIntInvalidException());
    }

    private void assertError(final InvokerApplicationException e) {
        assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
        assertThat(e.getEnum()).isEqualTo(HandlerError.ERROR_MESSAGE);
        assertThat(e.getMsg()).isEqualTo(HandlerError.ERROR_MESSAGE.label());
        assertThat(e.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(e.getMessage()).isEqualTo(
            "{ \"status\":\"" + e.getStatus() + "\", \"enumeration\":\"" + e.getEnum() + "\", \"msg\":\"" + e.getMsg() + "\" }");
    }

    private void assertInvalidError(final InvokerCommand<?> command) {
        try {
            command.execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
            assertThat(e.getData()).isEqualTo(State.ACTIVE.label());  // Any other enum submitted
            assertThat(e.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_PLAIN);
            assertThat(e.getMessage()).isEqualTo("{ status=" + e.getStatus() + (isNotEmpty(e.getData()) ? ", data=" + e.getData() : "") + " }");
        }
    }

    private void assetErrorCommand(final InvokerCommand<?> command) {
        try {
            command.execute();
            failBecauseExceptionWasNotThrown(InvokerApplicationException.class);
        }
        catch (final InvokerApplicationException e) {
            assertError(e);
        }
    }

    private ExceptionHandlerRemote createExceptionRemoteHandler() {
        return new ExceptionHandlerRemote(invoker(app.getServerUrl()));
    }

    private HttpInvoker createInvoker() {
        return invoker(app.getServerUrl());
    }

    private void testHandlerApplicationExceptionWithMsg(final String message, final InvokerCommand<?> resource) {
        try {
            resource.execute();
            failBecauseExceptionWasNotThrown(InvokerApplicationException.class);
        }
        catch (final InvokerApplicationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
            assertThat(e.getEnum()).isEqualTo(HandlerError.QUOTE_MESSAGE);
            assertThat(e.getMsg()).isEqualTo(message);
            assertThat(e.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        }
    }

    private void testHandlerInvocationException(final InvokerCommand<?> command) {
        try {
            command.execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
            assertThat(e.getData()).isEmpty();
            assertThat(e.getHeaders().getContentType()).isNull();  // todo is this ok?
        }
    }

    private void testHandlerInvocationExceptionWithObject(final InvokerCommand<?> command) {
        try {
            command.execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
            // noinspection SpellCheckingInspection
            assertThat(e.getData()).isEqualTo("Ooooops!");
        }
    }

    private void testRemoteCommandInvocationOnErrorFallbackFunction(final InvokerCommand<String> stringInvokerCommand) {
        final InvokerCommand<String> command = stringInvokerCommand.onErrorFallback(t -> {
                assertThat(t).isInstanceOf(InvokerInvocationException.class);
                final InvokerInvocationException e = (InvokerInvocationException) t;
                assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
                assertThat(e.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_PLAIN);
                return "Fallback";
            });

        final String result = command.get();
        assertThat(result).isEqualTo("Fallback");
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
}  // end class HttpExceptionsTest
