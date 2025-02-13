
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.SecurityMethodFactory;
import tekgenesis.admin.IndexHandler;
import tekgenesis.authorization.AuthorizationHandler;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.exception.ServiceException;
import tekgenesis.common.service.server.Request;
import tekgenesis.metadata.handler.Route;
import tekgenesis.metadata.handler.RouteMatch;
import tekgenesis.metadata.handler.SecureMethod;
import tekgenesis.repository.ModelRepository;
import tekgenesis.service.*;
import tekgenesis.view.server.routes.ModelRepositoryRouting;

import static java.net.URLDecoder.decode;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.env.security.SecurityUtils.getSession;
import static tekgenesis.common.service.Status.INTERNAL_SERVER_ERROR;
import static tekgenesis.common.service.Status.NOT_FOUND;
import static tekgenesis.security.shiro.web.URLConstants.INDEX_URI;
import static tekgenesis.security.shiro.web.URLConstants.LOGIN_URI;
import static tekgenesis.service.InterfaceTypeProxy.RoutingContext;
import static tekgenesis.view.server.servlet.DispatcherServletError.errorPage;
import static tekgenesis.view.shared.response.ResponseError.deepest;

/**
 * Dispatches requests to a handlers.
 */
public class DispatcherServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    @Override protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        dispatch(req, resp);
    }

    @Override protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        dispatch(req, resp);
    }

    @Override protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        dispatch(req, resp);
    }

    @Override protected void doPut(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        dispatch(req, resp);
    }

    private Result<?> defaultPage(final Factory factory, final Dispatcher dispatcher, final Results results, final String uri) {
        // Attempt default index
        if (INDEX_URI.equals(uri)) return getSession().isAuthenticated() ? results.forward(IndexHandler.Routes.index()) : results.login();

        // Attempt default login
        if (LOGIN_URI.equals(uri)) return results.forward(AuthorizationHandler.Routes.login());

        // Attempt default favicon
        if (uri.startsWith("/favicon.")) return results.forward("/public/img" + uri);  // use sg forward?

        // Resource not found
        return errorPage(factory, dispatcher, NOT_FOUND);
    }

    private void dispatch(@NotNull final HttpServletRequest req, @NotNull final HttpServletResponse resp)
        throws UnsupportedEncodingException
    {
        final Request                      request       = new HttpRequest(req);
        final HttpResponse                 response      = new HttpResponse(resp);
        final ReferenceSupplier<Forwarder> supplier      = new ReferenceSupplier<>();
        final Factory                      factory       = Handlers.factory(request, response, supplier);                               // Unbind SUI-1036 :)
        final ModelRepository              repository    = Context.getSingleton(ModelRepository.class);
        final Dispatcher                   dispatcher    = new ServiceDispatcher(ModelRepositoryRouting.routing, factory, repository);  // Unbind SUI-1036 :)
        final ResultHandler                resultHandler = new HttpResultHandler(response);
        final Forwarder                    forwarder     = new RequestForwarder(dispatcher, request).withFallback(new HttpForwarder(req, resp));
        supplier.setValue(forwarder);

        // write result
        resultHandler.handle(dispatch(req, request, factory, dispatcher), forwarder);
    }

    private Result<?> dispatch(@NotNull final HttpServletRequest req, final Request request, final Factory factory, final Dispatcher dispatcher)
        throws UnsupportedEncodingException
    {
        final Results results = factory.results();

        // Attempt to match any specified route
        final String             uri   = getUri(req);
        final Option<RouteMatch> match = dispatcher.route(request.getMethod(), uri, true);

        // Default sg page
        if (match.isEmpty()) return defaultPage(factory, dispatcher, results, uri);

        // Defined route found
        final RouteMatch routeMatch = match.get();
        req.setAttribute(Constants.NEWRELIC_AGENT_TRANSACTION_NAME, routeMatch.getRoute().getPath());
        return dispatchToRoute(request, factory, dispatcher, results, routeMatch);
    }

    private Result<?> dispatchToRoute(final Request request, final Factory factory, final Dispatcher dispatcher, final Results results,
                                      final RouteMatch routeMatch) {
        factory.context(RoutingContext.class).setRouteMatch(routeMatch);

        final Route                                route     = routeMatch.getRoute();
        final boolean                              htmlRoute = route.getType().isHtml();
        final SecureMethod                         method    = route.getSecureMethod();
        final SecurityMethodFactory.SecurityMethod security  = SecurityMethodFactory.build(method);
        Result<?>                                  result    = null;

        final HttpMetric metric = new HttpMetric(route.getMethodClass(), route.getPath());
        try {
            metric.start();
            if (security.authenticate(request)) result = dispatcher.dispatch(routeMatch, request);
            else if (method == SecureMethod.USERNAMEPASSWORD && htmlRoute)
                result = results.login(request.getUri() + (isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString()));
            else result = results.unauthorized();
        }
        catch (final ServiceException e) {
            result = error(factory, dispatcher, results, htmlRoute, e, e.getStatus());
        }
        catch (final Throwable e) {
            result = error(factory, dispatcher, results, htmlRoute, e, INTERNAL_SERVER_ERROR);
        }
        finally {
            if (result != null) metric.mark(result.getStatus());
            else metric.mark(Status.INTERNAL_SERVER_ERROR);
            metric.stop();
        }

        return result;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private Result<?> error(final Factory factory, final Dispatcher dispatcher, final Results results, final boolean htmlRoute, final Throwable e,
                            final Status status) {
        logger.error(e);
        return htmlRoute ? errorPage(factory, dispatcher, status, e) : results.status(status, deepest(e).getMessage());
    }

    /** Get not null uri for request. */
    @NotNull private String getUri(@NotNull final HttpServletRequest req)
        throws UnsupportedEncodingException
    {
        final String uri = notNull(req.getRequestURI(), "");
        return decode(uri, notEmpty(req.getCharacterEncoding(), Constants.UTF8));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3963245509363094958L;

    private static final Logger logger = Logger.getLogger(DispatcherServlet.class);
}  // end class DispatcherServlet
