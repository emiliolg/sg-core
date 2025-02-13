
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;

import static java.net.URLDecoder.decode;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.notNull;

/**
 * Abstract base filter with some handy methods.
 */
abstract class BaseFilter implements Filter {

    //~ Methods ......................................................................................................................................

    @Override public void destroy() {}

    @Override public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException
    {
        doFilter((HttpServletRequest) req, (HttpServletResponse) resp, chain);
    }

    @Override public void init(FilterConfig filterConfig)
        throws ServletException {}

    /** Advance filter chaining. */
    void advance(@NotNull final HttpServletRequest req, @NotNull final HttpServletResponse resp, @NotNull final FilterChain chain)
        throws IOException, ServletException
    {
        chain.doFilter(req, resp);
    }

    abstract void doFilter(@NotNull final HttpServletRequest req, @NotNull final HttpServletResponse resp, @NotNull final FilterChain chain)
        throws IOException, ServletException;

    /** Get not null uri for request. */
    @NotNull String getUri(@NotNull final HttpServletRequest req)
        throws UnsupportedEncodingException
    {
        final String uri = notNull(req.getRequestURI(), "");
        return decode(uri, notEmpty(req.getCharacterEncoding(), Constants.UTF8));
    }
}
