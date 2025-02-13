
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.service.Method;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Route;
import tekgenesis.metadata.handler.Routing;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.samples.service.Product;
import tekgenesis.service.html.HtmlBuilderImpl;

/**
 * Internal utility class for service package.
 */
@SuppressWarnings({ "MagicNumber", "JavaDoc" })
public class ServiceTests {

    //~ Constructors .................................................................................................................................

    private ServiceTests() {}

    //~ Methods ......................................................................................................................................

    public static Dispatcher createDispatcher() {
        final ModelRepository repository = buildRepositoryFromClassLoader();
        final Routing         result     = createRoutingFromRepository(repository);
        return new ServiceDispatcher(result, createMockFactory(), repository);
    }

    public static Factory createMockFactory() {
        return new FactoryImpl(new HtmlBuilderImpl(),
            new InterfaceTypeProxy(),
            createMockGetRequest(),
            new MockServerResponse(),
            new ReferenceSupplier<>());
    }

    public static MockServerRequest createMockGetRequest() {
        return createMockRequest(Method.GET, null);
    }

    public static Product createProduct() {
        final Product product = new Product("product.id");
        product.setCreated(DateTime.dateTime(2014, 1, 31));
        // noinspection DuplicateStringLiteralInspection
        product.setModel("Some model");
        product.setDescription("Some description with \"quoted\" words.");
        product.setPrice(new BigDecimal(89.99));
        return product;
    }

    static ModelRepository buildRepositoryFromClassLoader() {
        return new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader()).build();
    }

    static MockServerRequest createMockPostRequest(@Nullable byte[] content) {
        return createMockRequest(Method.POST, content);
    }

    static MockServerRequest createMockRequest(@NotNull Method method, @Nullable byte[] content) {
        final MockServerRequest request = new MockServerRequest(method, content);
        request.setHost("tekgenesis.net:8080");
        return request;
    }

    static Routing createRoutingFromRepository(ModelRepository repository) {
        Context.getContext().setSingleton(ModelRepository.class, repository);
        final Routing result = Routing.create();
        for (final Handler h : repository.getModels().filter(Handler.class)) {
            for (final Route route : h.getChildren())
                result.addRoute(route);
        }
        return result;
    }
}  // end class ServiceTests
