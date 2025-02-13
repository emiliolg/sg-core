
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import org.infinispan.Cache;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cache.test.database.CacheRule;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.exception.MessageConversionException;
import tekgenesis.common.service.exception.ServiceException;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.metadata.handler.RouteMatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.service.Status.BAD_REQUEST;
import static tekgenesis.common.service.Status.OK;
import static tekgenesis.samples.service.ServiceHandlerBase.Routes;
import static tekgenesis.service.ServiceTests.*;

@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class ServiceDispatcherTest {

    //~ Instance Fields ..............................................................................................................................

    public CacheRule cache = new CacheRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    public final MockResponseRule response = new MockResponseRule();

    @Rule public TestRule chain = cache.around(response);

    @Parameter public String dbName = null;

    //~ Methods ......................................................................................................................................

    @Test public void testDispatchForCachedGetMethod() {
        Context.getProperties(ApplicationProps.class).handlersCacheSize = 10;

        final Dispatcher         dispatcher = createDispatcher();
        final Option<RouteMatch> route      = route(dispatcher, Routes.list("popular"));
        assertThat(route.isPresent()).isTrue();

        final Cache<String, Result<?>> cacheMap = Context.getSingleton(InfinispanCacheManager.class).getLocalMap(Constants.SUIGENERIS_HANDLERS_CACHE);
        assertThat(cacheMap).isEmpty();

        final Result<?> result = dispatcher.dispatch(route.get(), createMockGetRequest());
        assertThat(result).isNotNull();
        response.handle(result);
        assertThat(response.getStatus()).isEqualTo(OK);

        assertThat(cacheMap).hasSize(1);

        final Result<?> cachedResult = dispatcher.dispatch(route.get(), createMockGetRequest());
        assertThat(cachedResult).isNotNull();
        response.handle(cachedResult);
        assertThat(response.getStatus()).isEqualTo(OK);

        assertThat(cacheMap).hasSize(1);

        cacheMap.clear();
        Context.getProperties(ApplicationProps.class).handlersCacheSize = 0;
    }

    @Test public void testDispatchForCachedGetMethodWithParameters() {
        Context.getProperties(ApplicationProps.class).handlersCacheSize = 10;

        final Dispatcher         dispatcher = createDispatcher();
        final Option<RouteMatch> route      = route(dispatcher, Routes.all());
        assertThat(route.isPresent()).isTrue();

        final Cache<String, Result<?>> cacheMap = Context.getSingleton(InfinispanCacheManager.class).getLocalMap(Constants.SUIGENERIS_HANDLERS_CACHE);
        assertThat(cacheMap).isEmpty();

        final MockServerRequest request = createMockGetRequest();
        request.putParameter("passPhrase", "none");
        request.putParameter("currencies", "ARS");

        final Result<?> result = dispatcher.dispatch(route.get(), request);
        assertThat(result).isNotNull();
        response.handle(result);
        assertThat(response.getStatus()).isEqualTo(OK);

        assertThat(cacheMap).hasSize(1);

        final Result<?> cachedResult = dispatcher.dispatch(route.get(), request);
        assertThat(cachedResult).isNotNull();
        response.handle(cachedResult);
        assertThat(response.getStatus()).isEqualTo(OK);

        assertThat(cacheMap).hasSize(1);

        cacheMap.clear();
        Context.getProperties(ApplicationProps.class).handlersCacheSize = 0;
    }

    @Test public void testDispatchForGetMethod() {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> route = route(dispatcher, Routes.list("popular"));
        assertThat(route.isPresent()).isTrue();

        final Result<?> result = dispatcher.dispatch(route.get(), createMockGetRequest());
        assertThat(result).isNotNull();

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(OK);
    }

    @Test public void testDispatchForGetMethodWithOptionalParameter() {
        final MockServerRequest request = createMockGetRequest();
        // Parameter 'from' is optional
        // Parameter 'currencies' is optional
        // Parameter 'range' has default value of 10
        request.putParameter("passPhrase", "abc123");
        testWithParameters(request, "Result for parameters: from 'null', range '10', passPhrase 'abc123', currencies '()'");
    }

    @Test public void testDispatchForGetMethodWithParameterDefaultValue() {
        final MockServerRequest request = createMockGetRequest();
        request.putParameter("from", "scolunga");
        // Parameter 'range' has default value of 10
        request.putParameter("passPhrase", "abc123");
        request.putParameter("currencies", "ARS");
        request.putParameter("currencies", "USD");
        testWithParameters(request, "Result for parameters: from 'scolunga', range '10', passPhrase 'abc123', currencies '(ARS,USD)'");
    }

    @Test public void testDispatchForGetMethodWithParameters() {
        final MockServerRequest request = createMockGetRequest();
        request.putParameter("from", "scolunga");
        request.putParameter("range", "2");
        request.putParameter("passPhrase", "abc123");
        request.putParameter("currencies", "ARS");
        request.putParameter("currencies", "USD");
        testWithParameters(request, "Result for parameters: from 'scolunga', range '2', passPhrase 'abc123', currencies '(ARS,USD)'");
    }

    @Test public void testDispatchForGetMethodWithRequiredParameter() {
        final MockServerRequest request = createMockGetRequest();
        request.putParameter("from", "scolunga");
        request.putParameter("range", "2");
        // Parameter 'passPhrase' is required
        try {
            testWithParameters(request, "...");
            failBecauseExceptionWasNotThrown(ServiceException.class);
        }
        catch (final ServiceException e) {
            assertThat(e).hasMessageContaining("Missing required parameter: passPhrase");
            assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
        }
    }

    @Test public void testDispatchForPostMethodWithArrayBody() {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> route = route(dispatcher, Routes.arrayCreate());
        assertThat(route.isPresent()).isTrue();

        final MockServerRequest request = createMockPostRequest(getProductArrayAsBytes());
        final Result<?>         result  = dispatcher.dispatch(route.get(), request);
        assertThat(result).isNotNull();

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(OK);
        assertThat(response.getBodyAsString()).isEqualTo("Created 3 products");
    }

    @Test public void testDispatchForPostMethodWithBody() {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> route = route(dispatcher, Routes.create("a"));
        assertThat(route.isPresent()).isTrue();

        final MockServerRequest request = createMockPostRequest(getProductAsBytes());
        final Result<?>         result  = dispatcher.dispatch(route.get(), request);
        assertThat(result).isNotNull();

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(OK);
        assertThat(response.getBodyAsString()).isEqualTo("Some model");
    }

    @Test public void testDispatchForPostMethodWithEmptyBody() {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> route = route(dispatcher, Routes.create("a"));
        assertThat(route.isPresent()).isTrue();

        final MockServerRequest request = createMockPostRequest(null);

        try {
            dispatcher.dispatch(route.get(), request);
            failBecauseExceptionWasNotThrown(ServiceException.class);
        }
        catch (final ServiceException e) {
            assertThat(e).hasMessageContaining("Unspecified Content-Length.");
        }
    }

    @Test public void testDispatchForPostMethodWithInvalidBody() {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> route = route(dispatcher, Routes.create("a"));
        assertThat(route.isPresent()).isTrue();

        final MockServerRequest request = createMockPostRequest(getProductInvalidAsBytes());

        try {
            dispatcher.dispatch(route.get(), request);
            failBecauseExceptionWasNotThrown(MessageConversionException.class);
        }
        catch (final MessageConversionException e) {
            assertThat(e).hasMessageContaining("Unrecognized field \"invalid\"");
        }
    }

    @Test public void testRouteMethod() {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> list = route(dispatcher, Routes.list("popular"));
        assertThat(list.isPresent()).isTrue();
        assertThat(list.get().getMethodName()).isEqualTo("list");

        final Option<RouteMatch> wrong = dispatcher.route(Method.POST, "/services/list/popular", true);
        assertThat(wrong.isPresent()).isFalse();

        final Option<RouteMatch> none = dispatcher.route(Method.GET, "/services/none/popular", true);
        assertThat(none.isPresent()).isFalse();
    }

    private Option<RouteMatch> route(Dispatcher dispatcher, Call call) {
        return dispatcher.route(call.getMethod(), call.getUrl(), true);
    }

    private void testWithParameters(MockServerRequest request, String expected) {
        final Dispatcher dispatcher = createDispatcher();

        final Option<RouteMatch> route = route(dispatcher, Routes.all());
        assertThat(route.isPresent()).isTrue();

        final Result<?> result = dispatcher.dispatch(route.get(), request);
        assertThat(result).isNotNull();

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(OK);
        assertThat(response.getBodyAsString()).isEqualTo(expected);
    }

    private byte[] getProductArrayAsBytes() {
        return ImmutableList.of(createProduct(), createProduct(), createProduct()).mkString("[", ",", "]").getBytes();
    }

    private byte[] getProductAsBytes() {
        return createProduct().toJson().getBytes();
    }

    private byte[] getProductInvalidAsBytes() {
        return "{\"invalid\" : \"mapping\"}".getBytes();
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ServiceDispatcherTest
