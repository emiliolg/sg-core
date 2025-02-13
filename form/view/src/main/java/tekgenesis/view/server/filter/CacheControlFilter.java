
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.view.server.servlet.Servlets;

import static tekgenesis.view.server.servlet.Servlets.HALF_YEAR;

/**
 * Filter to handle expiration headers for SuiGeneris related content.
 */
@SuppressWarnings("WeakerAccess")
public class CacheControlFilter extends BaseFilter {

    //~ Methods ......................................................................................................................................

    @Override void doFilter(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull FilterChain chain)
        throws IOException, ServletException
    {
        final String uri = getUri(req);

        if (uri.contains(CACHE) || uri.contains(EXTERNAL)) Servlets.addCacheHeaders(resp, HALF_YEAR);
        else if (uri.contains(NO_CACHE)) Servlets.addNoCacheHeaders(resp);

        advance(req, resp, chain);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String EXTERNAL = "/external/";
    @NonNls private static final String CACHE    = ".cache.";
    @NonNls private static final String NO_CACHE = ".nocache.";
}
