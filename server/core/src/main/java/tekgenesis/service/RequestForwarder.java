
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.server.Request;
import tekgenesis.metadata.handler.RouteMatch;

import static tekgenesis.common.core.Constants.LOCALHOST;
import static tekgenesis.common.core.Strings.split;

/**
 * Service forwarder.
 */
public class RequestForwarder implements Forwarder {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Dispatcher dispatcher;
    private Forwarder                 fallback;
    @NotNull private final Request    request;

    //~ Constructors .................................................................................................................................

    /** Request forwarder constructor. */
    public RequestForwarder(@NotNull Dispatcher dispatcher, @NotNull Request request) {
        this.dispatcher = dispatcher;
        this.request    = request;
        fallback        = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void forward(@NotNull String url, @NotNull ResultHandler resultHandler, boolean routing) {
        // Attempt to match any specified route
        final Uri                uri   = new Uri(url);
        final Option<RouteMatch> match = routing ? dispatcher.route(request.getMethod(), uri.getPath(), false) : Option.empty();

        if (match.isPresent()) {
            updateRequestParams(request.getParameters(), uri.getParams());
            final Result<?> result = dispatcher.dispatch(match.get(), request);
            resultHandler.handle(result, this);
        }
        else if (fallback != null) fallback.forward(url, resultHandler, false);
        else logger.warning("FORWARD WARNING 404: Not Found. Path: [" + uri.getPath() + "] Method : [" + request.getMethod() + "]");
    }

    @Override public String url(@NotNull String uri) {
        return url(request.getScheme(), request.getDomain(), request.getPort(), uri);
    }

    /** Set forwarder fallback. */
    public Forwarder withFallback(@NotNull Forwarder f) {
        fallback = f;
        return this;
    }

    private void updateRequestParams(@NotNull MultiMap<String, String> parameters, @Nullable String params) {
        for (final String tuple : split(params, '&')) {
            final List<String> parameter = split(tuple, '=');
            if (parameter.size() == 2) parameters.put(parameter.get(0), parameter.get(1));
        }
    }

    private boolean isLocalUrl(String url) {
        return url.startsWith("/") || url.startsWith(request.getHost());
    }

    //~ Methods ......................................................................................................................................

    /** Return absolute url. */
    public static String url(String scheme, String domain, String port, String uri) {
        final String serverName = "127.0.0.1".equals(domain) ? LOCALHOST : domain;
        return scheme + "://" + serverName + ":" + port + uri;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(RequestForwarder.class);

    //~ Inner Classes ................................................................................................................................

    private class Uri {
        private final String params;

        private final String path;

        public Uri(String uri) {
            final int paramIndex = uri.indexOf("?");
            if (paramIndex > -1) {
                path   = uri.substring(0, paramIndex).trim();
                params = uri.substring(paramIndex + 1).trim();
            }
            else {
                path   = uri;
                params = null;
            }
        }

        public String getParams() {
            return params;
        }

        public String getPath() {
            return path;
        }
    }
}  // end class RequestForwarder
