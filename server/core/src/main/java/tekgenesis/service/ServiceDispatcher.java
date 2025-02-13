
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.exception.ApplicationException;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.exception.InvokerInvocationException;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.InboundMessageReader;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.etl.MessageConverter;
import tekgenesis.common.service.exception.ServiceException;
import tekgenesis.common.service.server.Request;
import tekgenesis.common.util.Reflection;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Parameter;
import tekgenesis.metadata.handler.RouteMatch;
import tekgenesis.metadata.handler.Routing;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ArrayType;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.service.exception.ServiceException.*;
import static tekgenesis.common.util.Reflection.Instance;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.type.Kind.DATE_TIME;

/**
 * Service dispatcher default implementation.
 */
public class ServiceDispatcher implements Dispatcher {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Factory         factory;
    @NotNull private final ModelRepository repository;

    @NotNull private final Routing routing;

    //~ Constructors .................................................................................................................................

    /** Service dispatcher constructor. */
    public ServiceDispatcher(@NotNull final Routing routing, @NotNull final Factory factory, @NotNull final ModelRepository repository) {
        this.routing    = routing;
        this.factory    = factory;
        this.repository = repository;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<?> dispatch(@NotNull RouteMatch match, @NotNull Request request) {
        final Instance        instance = Handlers.initialize(match.getClassName(), factory);
        final HandlerInstance handler  = cast(instance.getInstance());

        try {
            return invokeInTransaction(t -> {  //
                final Result<?> result = dispatch(match, request, instance, handler);
                if (result.getStatus().isError()) t.abort();
                return result;
            });
        }
        catch (final ApplicationException exception) {
            return createExceptionResult(instance, exception);
        }
        catch (final IllegalArgumentException exception) {
            final Throwable cause = exception.getCause();
            if (cause instanceof ApplicationException) return createExceptionResult(instance, (ApplicationException) cause);
            if (cause instanceof InvokerInvocationException) return createInvocationExceptionResult(instance, (InvokerInvocationException) cause);
            throw exception;
        }
    }

    @NotNull @Override public Option<RouteMatch> route(@NotNull Method method, @NotNull String path, boolean external) {
        return routing.getRoute(method, path, external);
    }

    @NotNull @Override public List<String> getLastTriedRoutes() {
        return routing.getLastTriedRoutes();
    }

    @NotNull private String cacheHashKey(Instance instance, String methodName, RouteArguments args) {
        return instance.getInstance().getClass().getName() + ":" + methodName + ":" + args.hash();
    }

    private <E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<?> createApplicationExceptionResponse(
        @NotNull ApplicationException exception, @NotNull HandlerInstance instance) {
        final Handler           model       = repository.getModel(instance.getClass().getCanonicalName(), Handler.class).get();
        final Enumeration<?, ?> enumeration = exception.getEnumeration();
        final Results           results     = factory.results();

        final Option<EnumType> error = model.getErrorRaiseEnum();
        if (error.isPresent()) {
            // Check ApplicationException underlying enum matches expected enum type
            if (equal(enumeration.getClass().getCanonicalName(), error.get().getFullName())) {
                final E                e    = cast(enumeration);
                final Option<Object[]> args = exception.getArguments();
                return args.isPresent() ? results.exception(e, args.get()) : results.exception(e);
            }

            logger.warning(
                format(
                    "Expected ApplicationException with enum type %s for handler %s, but was %s. " +
                    "Returning default internal server error response instead of application exception result.",
                    error.get().getFullName(),
                    model.getFullName(),
                    enumeration.getClass().getCanonicalName()),
                exception);
        }
        else
            logger.warning(
                format("Unexpected ApplicationException with enum type %s . " +
                    "Handler %s does not defines any error handler enumeration at all.",
                    enumeration.getClass().getCanonicalName(),
                    model.getFullName()),
                exception);

        return results.internalServerError(exception.getMessage());
    }

    private Result<?> createExceptionResult(Instance instance, ApplicationException cause) {
        final HandlerInstance handler = (HandlerInstance) instance.getInstance();
        return createApplicationExceptionResponse(cause, handler);
    }

    private Result<?> createInvocationExceptionResult(Instance instance, InvokerInvocationException cause) {
        final HandlerInstance handler = (HandlerInstance) instance.getInstance();
        return handler.status(cause.getStatus(), cause.getMessage());
    }

    private Result<?> dispatch(final Instance instance, final String methodName, final RouteArguments args) {
        final int handlersCacheSize = Context.getProperties(ApplicationProps.class).handlersCacheSize;
        if (handlersCacheSize <= 0) return ensureNotNull(instance.invoke(methodName, args.toArray()));

        // look up in cache map first
        final String    cacheKey     = cacheHashKey(instance, methodName, args);
        final Result<?> cachedResult = getCache().get(cacheKey);
        if (cachedResult != null) return cachedResult;

        // use cache-control header to configure cache lifespan
        final Result<?> result = ensureNotNull(instance.invoke(methodName, args.toArray()));
        final long      maxAge = result.getHeaders().getCacheControlMaxAge();
        if (maxAge > 0) getCache().put(cacheKey, result, maxAge, TimeUnit.SECONDS);
        return result;
    }

    @NotNull private Result<?> dispatch(@NotNull RouteMatch match, @NotNull Request request, Instance instance, HandlerInstance handler) {
        final RouteArguments args   = resolveArguments(match, request, handler.getConverters());
        final Result<?>      result = dispatch(instance, match.getMethodName(), args);
        ((ResultImpl<?>) result).withResultType(match.getRoute().getFinalType());
        return result;
    }

    private ServiceException invalidBody(@NotNull final Type bodyType) {
        return invalidParameter("body", bodyType.getImplementationClassName(), "[message-body]");
    }

    private Object parameterConversion(final Parameter parameter, Seq<String> values) {
        try {
            final Seq<Object> typed = values.map(value -> TypeFieldConversions.fromString(parameter, value)).toList();
            return parameter.isMultiple() ? typed : typed.getFirst().get();
        }
        catch (final Exception e) {
            throw invalidParameter(parameter.getName(), parameter.getDeepestType().getKind().getText(), values.mkString(","));
        }
    }

    @Nullable private Object parameterDefaultValue(List<String> missing, Parameter parameter, Type type) {
        Object result = null;
        if (parameter.getDefaultValue().isNull()) {
            if (parameter.isRequired()) missing.add(parameter.getName());
        }
        else if (parameter.getDefaultValue().isConstant()) {
            result = parameter.getDefaultValue().evaluate(new Evaluator(), null);
            if (type.isTime()) {
                final Long milliseconds = (Long) result;
                result = type.getKind() == DATE_TIME ? DateTime.fromMilliseconds(milliseconds) : DateOnly.fromMilliseconds(milliseconds);
            }
            else if (type.isEnum()) result = Enumerations.valueOf(type.getImplementationClassName(), valueOf(result));
        }
        return result;
    }

    private RouteArguments resolveArguments(RouteMatch match, Request req, Iterable<MessageConverter<?>> converters) {
        final RouteArguments arguments = new RouteArguments(match.getDynamicArguments());
        if (req.getMethod().allowsMessageBody()) {
            final Type body = match.getRoute().getBodyType();
            if (!body.isNull()) {
                if (isValidBodyType(body)) arguments.withBody(getBodyFromRequest(req, body, converters));
                else throw invalidBody(body);
            }
        }
        final List<Parameter> parameters = match.getRoute().getParameters().toList();
        if (!parameters.isEmpty()) arguments.withParameters(getParametersAsArguments(parameters, req));
        return arguments;
    }

    @Nullable private Object getBodyFromRequest(@NotNull Request req, @NotNull Type type, @NotNull Iterable<MessageConverter<?>> converters)
        throws ServiceException
    {
        if (req.getContentLength() == -1) throw unspecifiedContentLength();

        final Option<Class<?>> bodyClass;

        if (type.isArray()) {
            final String className = ((ArrayType) type).getElementType().getImplementationClassName();
            bodyClass = Reflection.findClass(className, Object.class);
        }
        else if (type.isType()) {
            final String className = type.getImplementationClassName();
            bodyClass = Reflection.findClass(className, Object.class);
        }
        else {
            final Class<?> className = type.getImplementationClass();
            bodyClass = some(className == null ? String.class : className);
        }

        if (bodyClass.isEmpty()) throw invalidBody(type);

        final GenericType<Seq<?>> list = new GenericType<Seq<?>>() {};

        final InboundMessageReader<?> reader = new InboundMessageReader<>(bodyClass.get(), type.isArray() ? list.getType() : bodyClass.get());
        reader.setDefaultContentType(defaultMediaTypeFor(type));
        return reader.read(req, converters);
    }

    private Cache<String, Result<?>> getCache() {
        return Context.getSingleton(InfinispanCacheManager.class).getLocalMap(Constants.SUIGENERIS_HANDLERS_CACHE);
    }

    private boolean isValidBodyType(Type body) {
        return body.isType() || (body.isArray() && ((ArrayType) body).getElementType().isType()) || Types.basicTypes().contains(body);
    }

    private List<Object> getParametersAsArguments(List<Parameter> parameters, Request req) {
        final List<Object> arguments = new ArrayList<>(parameters.size());
        final List<String> missing   = new ArrayList<>(parameters.size());
        for (final Parameter parameter : parameters) {
            final Seq<String> values = req.getParameters().get(parameter.getName());
            arguments.add(getTypedValue(missing, parameter, values));
        }
        if (!missing.isEmpty()) throw requiredParameters(missing);
        return arguments;
    }

    @Nullable private Object getTypedValue(List<String> missing, Parameter parameter, Seq<String> values) {
        Object result = null;
        if (!values.isEmpty()) result = parameterConversion(parameter, values);
        else {
            if (!parameter.isMultiple()) result = parameterDefaultValue(missing, parameter, parameter.getType());
            else {
                if (parameter.isRequired()) missing.add(parameter.getName());
                else result = Colls.emptyList();
            }
        }
        return result;
    }  // end method getTypedValue

    //~ Methods ......................................................................................................................................

    @Nullable static MediaType defaultMediaTypeFor(@NotNull final Type type) {
        final Type t = type.isArray() ? ((ArrayType) type).getElementType() : type;
        switch (t.getKind()) {
        case BOOLEAN:
        case STRING:
        case REAL:
        case DECIMAL:
        case INT:
        case DATE_TIME:
        case DATE:
        case ENUM:
            return MediaType.TEXT_PLAIN;
        case TYPE:
            return MediaType.APPLICATION_JSON;
        case HTML:
            return MediaType.TEXT_HTML;
        case ANY:
            return MediaType.APPLICATION_OCTET_STREAM;
        case RESOURCE:
        case VOID:
            return null;
        default:
            throw new IllegalStateException("Undefined media type for " + t.getKind());
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(ServiceDispatcher.class);

    //~ Inner Classes ................................................................................................................................

    /**
     * Route match arguments. Including dynamic parts, body, and parameters.
     */
    private static class RouteArguments {
        private final List<Object> arguments = new ArrayList<>();

        private RouteArguments(@NotNull List<Object> dynamics) {
            arguments.addAll(dynamics);
        }

        public int hash() {
            return Objects.hash(arguments.toArray());
        }  // TODO cache feature: remove or do digest

        public Object[] toArray() {
            return arguments.toArray();
        }

        private void withBody(@Nullable Object body) {
            arguments.add(body);
        }

        private void withParameters(@NotNull List<Object> parameters) {
            arguments.addAll(parameters);
        }
    }
}  // end class ServiceDispatcher
