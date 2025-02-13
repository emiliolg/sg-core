
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Tuple;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.HttpInvokers;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.invoker.exception.InvokerInvocationException;
import tekgenesis.common.invoker.metric.InvocationKeyGenerator.CamelCaseKeyGenerator;
import tekgenesis.common.service.Method;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.common.core.Tuple.tuple;

/**
 * User class for Handler: ExternalServicesHandler
 */
@SuppressWarnings("MagicNumber")
public class ExternalServicesHandler extends ExternalServicesHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final HttpInvoker compumundo;
    private final HttpInvoker garbarino;

    //~ Constructors .................................................................................................................................

    ExternalServicesHandler(@NotNull Factory factory) {
        super(factory);
        final CamelCaseWithServerKeyGenerator keyGenerator = new CamelCaseWithServerKeyGenerator();
        compumundo = HttpInvokers.invoker("https://computest.tekgenesis.net")
                     .withConnectTimeout(2000)
                     .withReadTimeout(4000)
                     .withMetrics(keyGenerator);
        garbarino  = HttpInvokers.invoker("https://garbatest.tekgenesis.net")
                     .withConnectTimeout(2000)
                     .withReadTimeout(8000)
                     .withMetrics(keyGenerator);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/invocations/compumundo/services". */
    @NotNull @Override public Result<String> compumundo() {
        return getBytesResult(compumundo);
    }

    /** Invoked for route "/invocations/garbarino/services". */
    @NotNull @Override public Result<String> garbarino() {
        return getBytesResult(garbarino);
    }

    private Result<String> getBytesResult(@NotNull HttpInvoker invoker) {
        final long us = getResource(invoker, "/services/prerender", null);
        final long cs = getResource(invoker, "/services/categories", null);
        long       ps = 0;
        for (int i = 0; i < 30; i++)
            ps = getResource(invoker, "/services/products", tuple("q", "asus"));
        return ok(String.format("Prerender bytes: %s\nCategories bytes: %s\nProducts bytes: %s", us, cs, ps));
    }

    private long getResource(@NotNull HttpInvoker invoker, @NotNull String path, Tuple<String, String> parameter) {
        try {
            final PathResource<?> resource = invoker.resource(path);
            if (parameter != null) resource.param(parameter.first(), parameter.second());
            return resource.invoke(Method.GET).execute().getHeaders().getContentLength();
        }
        catch (final InvokerConnectionException | InvokerInvocationException ignored) {}
        return -1;
    }

    //~ Inner Classes ................................................................................................................................

    private static class CamelCaseWithServerKeyGenerator extends CamelCaseKeyGenerator {
        @NotNull @Override public String key(@NotNull String server, @NotNull String path, @NotNull Method method) {
            return String.format("(%s) ", server.substring(server.lastIndexOf("/") + 1, server.indexOf("."))) + super.key(server, path, method);
        }
    }
}  // end class ExternalServicesHandler
