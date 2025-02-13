
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.service.HeaderNames;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Times.MILLIS_DAY;
import static tekgenesis.common.core.Times.SECONDS_DAY;
import static tekgenesis.common.service.HeaderNames.CACHE_CONTROL;
import static tekgenesis.common.service.HeaderNames.EXPIRES;
import static tekgenesis.common.service.HeaderNames.LAST_MODIFIED;
import static tekgenesis.common.service.HeaderNames.PRAGMA;

/**
 * Servlets utility class.
 */
public class Servlets {

    //~ Constructors .................................................................................................................................

    private Servlets() {}

    //~ Methods ......................................................................................................................................

    /** Add cache headers. */
    public static void addCacheHeaders(@NotNull HttpServletResponse resp, int days) {
        if (days == NEVER || (days < HALF_YEAR && getBoolean(SUIGEN_DEVMODE))) addNoCacheHeaders(resp);
        else {
            final long now = System.currentTimeMillis();
            resp.addHeader(CACHE_CONTROL, "public, max-age=" + days * SECONDS_DAY);
            resp.setDateHeader(LAST_MODIFIED, now);
            resp.setDateHeader(EXPIRES, now + (days * MILLIS_DAY));
        }
    }

    /** Add keep alive header. */
    public static void addKeepAlive(@NotNull HttpServletResponse resp) {
        resp.addHeader(HeaderNames.CONNECTION, "keep-alive");
    }

    /** Adds 'no cache' headers to a response. */
    public static void addNoCacheHeaders(@NotNull HttpServletResponse resp) {
        final long now = System.currentTimeMillis();
        resp.addHeader(CACHE_CONTROL, "private, max-age=0, no-cache, no-store");
        resp.addHeader(PRAGMA, NO_CACHE);
        resp.setDateHeader(EXPIRES, now);
    }

    /** Returns a {@link Cookie} with teh given name or null if there isn't one. */
    @NotNull public static Option<Cookie> getCookie(@NotNull final HttpServletRequest req, @NotNull final String cookieName) {
        final Cookie[] cookies = req.getCookies();

        // By specification, if request has no cookies, the array is null (for example, when in incognito mode).
        if (cookies != null) {
            for (final Cookie cookie : cookies)
                if (cookie.getName().equals(cookieName)) return some(cookie);
        }

        return Option.empty();
    }

    //~ Static Fields ................................................................................................................................

    @NonNls static final String SHA_SERVLET_PATH = "/sha";

    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    static final String         NO_CACHE = "no-cache";

    private static final int           NEVER             = 0;
    @SuppressWarnings("MagicNumber")
    public static final int            HALF_YEAR         = 365 / 2;
    @NonNls public static final String UNAUTHORIZED_USER = "Unauthorized user";

    //J+
}  // end class Servlets
