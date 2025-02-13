
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Enumeration;
import tekgenesis.common.service.ApplicationExceptionResult;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.server.Request;
import tekgenesis.common.util.Reflection;
import tekgenesis.service.html.HtmlBuilderImpl;

import static tekgenesis.common.Predefined.cast;

/**
 * Utility class to deal with Handlers on test environments.
 */
public class HandlerTests {

    //~ Constructors .................................................................................................................................

    private HandlerTests() {}

    //~ Methods ......................................................................................................................................

    /** Returns the exception wrapped in the result. It might return null if it hasn't one. */
    @Nullable public static ApplicationExceptionResult exception(Result<?> result) {
        if (result instanceof ResultImpl.ObjectResult) {
            final ResultImpl.ObjectResult objectResult = cast(result);
            return cast(objectResult.content);
        }
        return null;
    }
    /** Returns true if @ex matches with exception wrapped in the result. */
    public static boolean hasException(Result<?> result, Enum<?> ex, Object... args) {
        final ApplicationExceptionResult exception = exception(result);
        return exception != null && exception.getEnumClass().equals(ex.getDeclaringClass().getCanonicalName()) &&
               exception.getEnumName().equals(ex.name()) && (args.length == 0 || ((Enumeration<?, ?>) ex).label(args).equals(exception.getMsg()));
    }

    /** Initialize a given Handler class. */
    @NotNull public static <T extends HandlerInstance> T initialize(@NotNull final Class<T> clazz) {
        return initialize(clazz, request(Method.GET));
    }

    /** Initialize a given Handler class with a given Request. */
    @NotNull public static <T extends HandlerInstance> T initialize(@NotNull final Class<T> handlerClass, @NotNull final Request req) {
        final FactoryImpl         factory  = new FactoryImpl(new HtmlBuilderImpl(),
                new InterfaceTypeProxy(),
                req,
                new MockServerResponse(),
                new ReferenceSupplier<>());
        final Reflection.Instance instance = Handlers.initialize(handlerClass.getName(), factory);
        return cast(instance.getInstance());
    }

    /** Create mock request. */
    @NotNull public static Request request(@NotNull final Method method) {
        return request(method, null);
    }

    /** Create mock request. */
    @NotNull public static Request request(@NotNull final Method method, @Nullable byte[] content) {
        return new MockServerRequest(method, content);
    }
}  // end class HandlerTests
