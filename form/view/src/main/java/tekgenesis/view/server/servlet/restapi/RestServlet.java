
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.restapi;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.*;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.routing.Route;
import tekgenesis.view.server.servlet.Servlets;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.TO_BE_IMPLEMENTED;
import static tekgenesis.common.util.Files.asUnchecked;
import static tekgenesis.common.util.Reflection.Instance.create;
import static tekgenesis.metadata.routing.Routes.route;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Servlet to handle REST operations.
 */
public class RestServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    @Override protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        doMethod(req, resp, HttpMethod.DELETE);
    }

    @Override protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws IOException
    {
        doMethod(req, resp, HttpMethod.GET);
    }

    @Override protected void doHead(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        doMethod(req, resp, HttpMethod.HEAD);
    }

    @Override protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        doMethod(req, resp, HttpMethod.OPTIONS);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        doMethod(req, resp, HttpMethod.POST);
    }

    @Override protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        doMethod(req, resp, HttpMethod.PUT);
    }

    /** Create custom handler instance for given route, or return default handler. */
    @NotNull private <T extends FormInstance<?>> HandlerInstance<T> createHandlerInstance(@NotNull HttpServletRequest req,
                                                                                          @NotNull HttpServletResponse resp, @NotNull Form form) {
        final String handlerClass = form.getHandlerClass();

        final HandlerInstance<T> instance;

        if (isNotEmpty(handlerClass)) {
            final Reflection.Instance user = create(handlerClass);    // User defined custom handler
            user.setPrivateField("req", req);
            user.setPrivateField("resp", resp);
            instance = cast(user.getInstance());
        }
        else instance = cast(new DefaultHandlerInstance(req, resp));  // Default handler

        return instance;
    }

    private void doMethod(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull HttpMethod method)
        throws IOException
    {
        try {
            if (isAuthenticated(resp)) {
                final Route<Form> route = routing(req);
                if (route.isDefined()) doMethod(req, resp, route, method);
                else resp.sendError(HTTP_NOT_FOUND);
            }
        }
        catch (final UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private <T extends FormInstance<?>> void doMethod(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull Route<Form> route,
                                                      @NotNull HttpMethod method)
        throws IOException
    {
        final Handler<T>         handler  = new HandlerImpl<>(route.getTarget(), route.getPath(), route.getKey());
        final HandlerInstance<T> instance = createHandlerInstance(req, resp, route.getTarget());

        try {
            runInTransaction(() -> asUnchecked(() -> method.invoke(instance, handler)));
        }
        catch (final EntityInstanceNotFoundException e) {
            resp.sendError(HTTP_NOT_FOUND, e.getMessage());
        }
    }

    /** Resolve route from request URI. */
    private Route<Form> routing(@NotNull final HttpServletRequest req) {
        return route(getPathForRouting(req));
    }

    private boolean isAuthenticated(HttpServletResponse resp)
        throws IOException
    {
        final boolean authenticated = SecurityUtils.getSession().isAuthenticated();
        if (!authenticated) resp.sendError(HTTP_UNAUTHORIZED, Servlets.UNAUTHORIZED_USER);
        return authenticated;
    }

    /** Resolve path for routing. */
    private String getPathForRouting(@NotNull final HttpServletRequest req) {
        return req.getPathInfo();  // Avoid '/api' servlet path that comes with getRequestURI.
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6422211503401041984L;

    //~ Enums ........................................................................................................................................

    private enum HttpMethod {
        GET {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
                throws IOException { instance.handleGet(handler); }},
        POST {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
                throws IOException { instance.handlePost(handler); }},
        HEAD {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
                throws IOException { instance.handleHead(handler); }},
        OPTIONS {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
                throws IOException { instance.handleOptions(handler); }},
        PUT {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
                throws IOException { instance.handlePut(handler); }},
        DELETE {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
                throws IOException { instance.handleDelete(handler); }},
        TRACE {
            @Override <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler) {
                throw new IllegalStateException(TO_BE_IMPLEMENTED);
            }};

        abstract <T extends FormInstance<?>> void invoke(@NotNull HandlerInstance<T> instance, @NotNull Handler<T> handler)
            throws IOException;
    }
}  // end class RestServlet
