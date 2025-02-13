
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.*;
import tekgenesis.common.invoker.exception.InvokerInvocationException;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.media.Mime;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.cookie.Cookie;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.sales.basic.service.Product;
import tekgenesis.sales.basic.service.SiteHandler;
import tekgenesis.sales.basic.service.State;

import static java.lang.String.valueOf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.core.Decimals.scaleAndCheck;
import static tekgenesis.common.core.Times.SECONDS_HOUR;
import static tekgenesis.common.invoker.HttpInvokers.invoker;
import static tekgenesis.common.media.MediaType.CHARSET_PARAMETER;
import static tekgenesis.common.service.HeaderNames.LOCATION;
import static tekgenesis.common.service.HeaderNames.TEK_APP_TOKEN;
import static tekgenesis.common.service.Status.*;
import static tekgenesis.sales.basic.service.InternalProductHandlerBase.Routes.internal;
import static tekgenesis.sales.basic.service.ProductHandlerBase.Routes.*;
import static tekgenesis.sales.basic.service.ProductHandlerBase.Routes.redirection;
import static tekgenesis.sales.basic.service.ReturnTypesHandlerBase.Routes.*;
import static tekgenesis.sales.basic.service.SiteHandlerBase.Routes.home;
import static tekgenesis.sales.basic.service.SiteHandlerBase.Routes.search;

/**
 * Test SuiGeneris http service stack.
 */
@Category(AppTests.class)
@SuppressWarnings({ "MagicNumber", "JavaDoc", "DuplicateStringLiteralInspection", "ClassWithTooManyMethods" })
public class HttpTest {

    //~ Methods ......................................................................................................................................

    @Test public void testFailureGet() {
        final InvokerCommand<?> resource = createInvoker().resource(failure()).invoke();
        try {
            resource.execute();
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
        }
    }

    @Test public void testFailurePost()
        throws IOException
    {
        final InvokerCommand<?> resource = createInvoker().resource(failure().getUrl()).invoke(Method.POST, new Product());
        try {
            resource.execute();
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
        }
    }

    @Test public void testGetGenericStruct() {
        final List<Product> content = createInvoker().resource(featured()).invoke(new GenericType<List<Product>>() {}).execute().get();
        assertThat(content).isNotNull();
        assertThat(content).hasSize(3);
    }

    @Test public void testGetHtml() {
        final HttpInvokerResult<byte[]> result = createInvoker().resource(home()).invoke(byte[].class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(new String(result.get())).contains("This message was retrieved through an internal call");
    }

    @Test public void testGetStruct() {
        final Product content = createInvoker().resource(show("1")).invoke(Product.class).execute().get();
        assertThat(content).isNotNull();
        assertThat(content.getProductId()).isEqualTo("1");
    }

    @Test public void testHandlerInboundCookies() {
        final CallResource<?> resource = createInvoker().resource(inboundCookies()).withCookie("A", "a").withCookie("B", "b");

        final InvokerCommand<String> command = resource.invoke(String.class);
        assertThat(command.get()).isEqualTo("[a,b]");
    }

    @Test public void testHandlerOutboundCookies() {
        final CallResource<?>      resource = createInvoker().resource(outboundCookies());
        final HttpInvokerResult<?> command  = resource.invoke().execute();
        final Seq<Cookie>          cookies  = command.getCookies();

        final Cookie session = cookies.getFirst(c -> c != null && "SESSION".equals(c.getName())).getOrFail("Expected SESSION cookie");
        assertThat(session.getName()).isEqualTo("SESSION");
        assertThat(session.getValue()).isEqualTo("uuid");
        assertThat(session.getDomain()).isEqualTo("tekgenesis.com");
        assertThat(session.getPath()).isEqualTo("/path");
        assertThat(session.getMaxAge()).isEqualTo(SECONDS_HOUR);
        assertThat(session.isHttpOnly()).isTrue();
        assertThat(session.isSecure()).isTrue();

        final Cookie node = cookies.getFirst(c -> c != null && "NODE".equals(c.getName())).getOrFail("Expected NODE cookie");
        assertThat(node.getName()).isEqualTo("NODE");
        assertThat(node.getValue()).isEqualTo("xyz");
        assertThat(node.getDomain()).isNull();
        assertThat(node.getPath()).isNull();
        assertThat(node.getMaxAge()).isEqualTo(-1);
        assertThat(node.isHttpOnly()).isFalse();
        assertThat(node.isSecure()).isFalse();
    }

    @Test public void testHeaders() {
        testHeaders(Mime.TEXT_PLAIN, Charset.forName(Constants.UTF8), 13);
        testHeaders(Mime.TEXT_PLAIN, Charset.forName("UTF-16"), 26);
    }

    @Test public void testHtmlIncludeWithFailures() {
        final Status[]    statuses = { BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED };
        final HttpInvoker invoker  = createInvoker();
        for (final Status status : statuses) {
            try {
                invoker.resource(SiteHandler.Routes.failure()).param("method", String.valueOf(status.code())).invoke(byte[].class).execute();
                failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
            }
            catch (final InvokerInvocationException e) {
                assertThat(e.getStatus()).isEqualTo(status);
            }
        }
    }

    @Test public void testInternalNotFound() {
        final InvokerCommand<?> resource = createInvoker().resource(internal()).invoke();
        try {
            resource.execute();
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(NOT_FOUND);
        }
    }

    @Test public void testJsonEscaping()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(search());
        resource.param("q", "'';!--\"<XSS>=&{()}");

        final HttpInvokerResult<byte[]> result = resource.invoke(byte[].class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(new String(result.get())).doesNotContain("<XSS>");
    }

    @Test public void testLocalizedMessage() {
        assertLocalizedMessageForLocale(new Locale("es"), "Activo");
        assertLocalizedMessageForLocale(Locale.ENGLISH, "Active");
    }

    @Test public void testNotFound() {
        final InvokerCommand<?> resource = createInvoker().resource("/unreachable").invoke(Method.GET);
        try {
            resource.execute();
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(NOT_FOUND);
        }
    }

    @Test public void testParameters()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(parameters(2014));
        resource.param("from", "10");
        resource.param("to", "20");
        resource.param("step", "1");

        final HttpInvokerResult<?> result = resource.invoke(String.class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.get()).isEqualTo("Id 2014 from 10 to 20 step 1");
    }

    @Test public void testParametersConversion() {
        final CallResource<?> resource = createInvoker().resource(conversions());
        resource.param("a", "1");
        resource.param("b", "3.14");
        resource.param("c", "4.14");
        resource.param("d", DateOnly.date(2014, 1, 31).toString());
        resource.param("e", DateTime.dateTime(2014, 1, 31, 6, 47).toString());
        resource.param("f", "value");
        resource.param("g", "true");
        resource.param("h", "ACTIVE");
        final String s = resource.invoke(String.class).get();
        assertThat(s).isEqualTo("a:'1', b:'3.14', c:'4.1400', d:'2014-01-31', e:'2014-01-31T09:47:00Z', " +
            "f:'value', g:'true', h:'ACTIVE'");
    }

    @Test public void testParametersConversionException() {
        final CallResource<?> resource = createInvoker().resource(conversions());
        resource.param("b", "-3.14");
        try {
            resource.invoke(String.class).get();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e).hasMessageContaining("Invalid parameter 'b' of type 'Real' with value: -3.14");
        }
    }

    @Test public void testParametersConversions()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(conversions());
        resource.param("a", "1");
        resource.param("b", "1.61803");
        resource.param("c", "1.618");
        resource.param("d", valueOf(DateOnly.date(1984, 4, 1)));
        resource.param("e", valueOf(DateTime.dateTime(1984, 4, 1, 12, 0)));
        resource.param("f", "Hola Mundo!");
        resource.param("g", "true");
        resource.param("h", valueOf(State.DEPRECATED));

        final HttpInvokerResult<?> result = resource.invoke(String.class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.get()).isEqualTo(
            "a:'1', b:'1.61803', c:'1.6180', d:'1984-04-01', " +
            "e:'1984-04-01T15:00:00Z', f:'Hola Mundo!', g:'true', h:'DEPRECATED'");
    }

    @Test public void testParametersConversionsDefault()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(conversions());

        final HttpInvokerResult<?> result = resource.invoke(String.class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.get()).isEqualTo(
            "a:'3', b:'3.1415', c:'3.1415', d:'2014-01-31', " +
            "e:'2014-01-31T06:47:00Z', f:'Hello World!', g:'false', h:'ACTIVE'");
    }

    @Test public void testParametersMultipleConversion() {
        final CallResource<?> resource = createInvoker().resource(multiples());
        resource.param("a", "1");
        resource.param("b", "3.14");
        resource.param("c", "3.14");
        resource.param("c", "4.14");
        resource.param("d", DateOnly.date(2014, 1, 31).toString());
        resource.param("e", DateTime.dateTime(2014, 1, 31, 6, 47).toString());
        resource.param("f", "value");
        resource.param("g", "true");
        resource.param("g", "false");
        resource.param("h", "ACTIVE");
        resource.param("h", "DEACTIVE");
        final String s = resource.invoke(String.class).get();
        assertThat(s).isEqualTo(
            "a:'1', b:'3.14', c:'3.1400|4.1400', d:'2014-01-31', e:'2014-01-31T09:47:00Z', " +
            "f:'value', g:'true|false', h:'ACTIVE|DEACTIVE'");
    }

    @Test public void testParametersMultipleConversionException() {
        final CallResource<?> resource = createInvoker().resource(multiples());
        resource.param("a", "1");
        resource.param("a", "2");
        resource.param("b", "3.14");
        resource.param("b", "4.14");
        resource.param("c", "3.14");
        resource.param("c", "4.14");
        resource.param("d", DateOnly.date(2014, 1, 31).toString());
        resource.param("e", DateTime.dateTime(2014, 1, 31, 6, 47).toString());
        resource.param("f", "value");
        resource.param("g", "true");
        resource.param("h", "ACTIVE");
        resource.param("h", "INVALID_ENUM_VALUE");
        try {
            resource.invoke(String.class).get();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e).hasMessageContaining("Invalid parameter 'h' of type 'Enum' with value: ACTIVE,INVALID_ENUM_VALUE");
        }
    }

    @Test public void testParametersMultipleWithRequired() {
        final CallResource<?> resource = createInvoker().resource(multiples());
        resource.param("a", "1");
        resource.param("c", "3.14");
        resource.param("f", "value");
        resource.param("g", "true");
        try {
            resource.invoke(String.class).get();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e).hasMessageContaining("Missing required parameters: d,e,h");
        }
    }

    @Test public void testParametersWithDefault()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(parameters(2014));
        resource.param("from", "10");
        resource.param("to", "20");

        final HttpInvokerResult<?> result = resource.invoke(String.class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.get()).isEqualTo("Id 2014 from 10 to 20 step 10");
    }

    @Test public void testParametersWithInvalidValue()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(parameters(2014));
        resource.param("from", "10");
        resource.param("to", "20");
        resource.param("step", "NotANumber");

        try {
            resource.invoke(String.class).execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e).hasMessageContaining("Invalid parameter 'step' of type 'Int' with value: NotANumber");
        }
    }

    @Test public void testParametersWithOptional()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(parameters(2014));
        resource.param("from", "10");
        resource.param("step", "1");

        final HttpInvokerResult<?> result = resource.invoke(String.class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.get()).isEqualTo("Id 2014 from 10 to null step 1");
    }

    @Test public void testParametersWithRequired()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(parameters(2014));
        resource.param("step", "1");

        try {
            resource.invoke(String.class).execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e).hasMessageContaining("Missing required parameter: from");
        }
    }

    @Test public void testPostWithBody()
        throws IOException
    {
        final CallResource<?> resource = createInvoker().resource(related("1"));
        final Product         payload  = new Product();
        payload.setModel("Model");

        final HttpInvokerResult<Product> invoke = resource.invoke(Product.class, payload).execute();
        assertThat(invoke.getStatus()).isEqualTo(OK);
        assertThat(invoke.get()).isNotNull();
        assertThat(invoke.get().getModel()).isEqualTo(payload.getModel());
    }

    @Test public void testPostWithBodyMultiple() {
        final CallResource<?> resource = createInvoker().resource(bodyMultiple());
        final Product         a        = new Product();
        a.setModel("A Model");

        final List<Product> payload = new ArrayList<>();
        payload.add(a);

        final Product b = new Product();
        b.setModel("B Model");
        payload.add(b);

        final HttpInvokerResult<String> invoke = resource.invoke(String.class, payload).execute();
        assertThat(invoke.getStatus()).isEqualTo(OK);
        assertThat(invoke.get()).isEqualTo("2");
    }

    @Test public void testPostWithoutBody()
        throws IOException
    {
        try {
            createInvoker().resource(related("1")).invoke(Product.class).execute();
            failBecauseExceptionWasNotThrown(InvokerInvocationException.class);
        }
        catch (final InvokerInvocationException e) {
            assertThat(e.getStatus()).isEqualTo(Status.LENGTH_REQUIRED);
        }
    }

    @Test public void testRedirection() {
        testRedirection(MOVED_PERMANENTLY);
        testRedirection(FOUND);
        testRedirection(SEE_OTHER);
        testRedirection(NOT_MODIFIED);
    }

    @Test public void testResourcesConcat()
        throws IOException
    {
        final PathResource<?> resource = createInvoker().resource(
                "/sg" + "/concat?" +
                "/sha/css/alpha.css/38177af2f47ed7abaa4e30e2f23edea1814580c995ff6af26ab606014cc63da7&" +
                "/sha/css/beta.css/bcc1bd99ab4c4af849cf47698a3509b630662d382b45482ffca25e35448414fa");

        final HttpInvokerResult<byte[]> result = resource.invoke(Method.GET, byte[].class).execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(new String(result.get())).contains("/css/alpha.css", ".alpha", "/css/beta.css", ".beta");
    }

    @Test public void testResultBasicTypes() {
        final HttpInvoker invoker = createInvoker();
        assertBasicTypeResult(invoker.resource(returnBoolean()).invoke(Boolean.class), true);
        assertBasicTypeResult(invoker.resource(returnReal()).invoke(Double.class), .99);
        assertBasicTypeResult(invoker.resource(returnDecimal()).invoke(BigDecimal.class), scaleAndCheck("d", new BigDecimal(.99), false, 10, 2));
        assertBasicTypeResult(invoker.resource(returnInt()).invoke(Integer.class), 1);
        assertBasicTypeResult(invoker.resource(returnDateTime()).invoke(DateTime.class), DateTime.dateTime(2014, 1, 31, 6, 47, 0));
        assertBasicTypeResult(invoker.resource(returnDate()).invoke(DateOnly.class), DateOnly.date(2014, 1, 31));
    }

    private <T> void assertBasicTypeResult(InvokerCommand<T> command, T expected) {
        final HttpInvokerResult<T> result = command.execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_PLAIN);
        assertThat(result.get()).isEqualTo(expected);
    }

    private void assertLocalizedMessageForLocale(Locale locale, String expected) {
        final CallResource<?>        resource = createInvoker().resource(localizedMessage()).acceptLanguage(locale);
        final InvokerCommand<String> command  = resource.invoke(String.class);
        final String                 result   = command.get();
        assertThat(result).isEqualTo(expected);
    }

    private HttpInvoker createInvoker() {
        return invoker(app.getServerUrl());
    }

    private void testHeaders(@NotNull final Mime mime, @NotNull final Charset charset, int expectedBytesCount) {
        final HttpInvokerResult<byte[]> result = createInvoker().resource(headers())
                                                 .param("contentType", mime.getMime())
                                                 .param(CHARSET_PARAMETER, charset.name())
                                                 .invoke(byte[].class)
                                                 .execute();
        assertThat(result.getStatus()).isEqualTo(OK);
        final MediaType contentType = result.getHeaders().getContentType();
        assertThat(contentType).isNotNull();
        assertThat(contentType.getCharset()).isEqualTo(charset);
        assertThat(contentType.getMime()).isEqualTo(mime);
        assertThat(result.get().length).isEqualTo(expectedBytesCount);
        assertThat(new String(result.get(), charset)).isEqualTo("<abc>á</abc>");
    }

    private void testRedirection(Status redirection) {
        final String url = app.getServerUrl() + redirection(redirection.code()).getUrl();
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(Method.GET.name());
            connection.connect();
            assertThat(connection.getResponseCode()).isEqualTo(redirection.code());
            assertThat(connection.getHeaderField(LOCATION)).contains("/products/ipad/details");
            assertThat(connection.getHeaderField(TEK_APP_TOKEN)).isEqualTo("HMpRgckBMeo6ipblp4ETCAA12JF2JzU+MfrjtY/Rp5w=");
            connection.disconnect();
        }
        catch (final IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
}  // end class HttpTest
