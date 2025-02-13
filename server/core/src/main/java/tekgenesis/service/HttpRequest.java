
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.cookie.Cookie;
import tekgenesis.common.service.cookie.Cookies;
import tekgenesis.common.service.server.Request;

import static java.util.Arrays.asList;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.service.cookie.Cookies.hash;

/**
 * Http servlet request extending service request.
 */
public class HttpRequest implements Request {

    //~ Instance Fields ..............................................................................................................................

    private Seq<Cookie> cookies;

    private final Headers            headers;
    private final Method             method;
    private MultiMap<String, String> parameters;

    private final HttpServletRequest req;

    //~ Constructors .................................................................................................................................

    /** Request constructor receiving servlet request. */
    public HttpRequest(final HttpServletRequest req) {
        this.req   = req;
        headers    = createHeadersFromRequest();
        method     = createMethodFromRequest();
        parameters = null;
        cookies    = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void close() {}

    @Override public Object getAttribute(@NotNull String name) {
        return req.getAttribute(name);
    }

    @Override public InputStream getContent()
        throws IOException
    {
        return req.getInputStream();
    }

    @Override public int getContentLength() {
        return req.getContentLength();
    }

    @Override public Seq<Cookie> getCookies() {
        if (cookies == null) {
            // Decode Cookies from headers
            final javax.servlet.http.Cookie[] cs = req.getCookies();
            cookies = adaptCookies(cs != null ? asList(cs) : Collections.emptyList());
        }
        return cookies;
    }

    @NotNull @Override public Headers getHeaders() {
        return headers;
    }

    @Override public Method getMethod() {
        return method;
    }

    @Override public MultiMap<String, String> getParameters() {
        if (parameters == null) parameters = createParametersFromRequest();
        return parameters;
    }

    @Override public String getPath() {
        return req.getServletPath();
    }

    @Override public String getQueryString() {
        return req.getQueryString();
    }

    @Override public String getScheme() {
        return req.getScheme();
    }

    @Override public String getUri() {
        return req.getRequestURI();
    }

    @Override public String getUrl() {
        return req.getRequestURL().toString();
    }

    private ImmutableList<Cookie> adaptCookies(@NotNull List<javax.servlet.http.Cookie> cs) {
        return map(cs, (cookie) -> (Cookie) new ServletRequestCookieAdapter(cookie)).toList();
    }

    private Headers createHeadersFromRequest() {
        final Headers             result = new Headers();
        final Enumeration<String> names  = req.getHeaderNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            result.putAll(name, req.getHeaders(name));
        }
        return result;
    }

    private Method createMethodFromRequest() {
        try {
            return Method.valueOf(req.getMethod());
        }
        catch (final IllegalArgumentException e) {
            logger.debug("Unsupported method " + req.getMethod() + " for request " + req.getRequestURI());
        }
        return Method.GET;
    }

    private MultiMap<String, String> createParametersFromRequest() {
        final MultiMap<String, String> result = MultiMap.createMultiMap();
        for (final Map.Entry<String, String[]> parameter : req.getParameterMap().entrySet())
            result.putAll(parameter.getKey(), asList(parameter.getValue()));
        return result;
    }

    //~ Static Fields ................................................................................................................................

    protected static final String EXPECTED_PAYLOAD      = "Post on path '%s' expected payload of type '%s' but request was empty. ";
    protected static final String DEFAULT_INSTANTIATION = "Default instantiation was returned.";

    private static final Logger logger = Logger.getLogger(HttpRequest.class);

    //~ Inner Classes ................................................................................................................................

    private static class ServletRequestCookieAdapter implements Cookie {
        private final javax.servlet.http.Cookie cookie;

        private ServletRequestCookieAdapter(javax.servlet.http.Cookie cookie) {
            this.cookie = cookie;
        }

        @Override public boolean equals(Object o) {
            return this == o || o instanceof Cookie && Cookies.equal(this, (Cookie) o);
        }

        @Override public int hashCode() {
            return hash(this);
        }

        @Override public String toString() {
            return Cookies.toString(this);
        }

        @Nullable @Override public String getDomain() {
            return cookie.getDomain();
        }

        @Override public boolean isSecure() {
            return cookie.getSecure();
        }

        @Override public long getMaxAge() {
            return cookie.getMaxAge();
        }

        @NotNull @Override public String getName() {
            return cookie.getName();
        }

        @Nullable @Override public String getPath() {
            return cookie.getPath();
        }

        @NotNull @Override public String getValue() {
            return cookie.getValue();
        }

        @Override public boolean isHttpOnly() {
            return cookie.isHttpOnly();
        }
    }  // end class ServletRequestCookieAdapter
}  // end class BaseHttpRequest
