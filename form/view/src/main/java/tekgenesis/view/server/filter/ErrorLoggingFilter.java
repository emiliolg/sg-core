
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

import javax.servlet.*;

import tekgenesis.common.logging.Logger;

/**
 * Filter to catch uncaught errors.
 */
public class ErrorLoggingFilter implements Filter {

    //~ Methods ......................................................................................................................................

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws ServletException, IOException
    {
        try {
            chain.doFilter(req, resp);
        }
        catch (final Throwable e) {
            logger.error(e);
            throw e;
        }
    }

    public void init(FilterConfig config)
        throws ServletException {}

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(ErrorLoggingFilter.class);
}
