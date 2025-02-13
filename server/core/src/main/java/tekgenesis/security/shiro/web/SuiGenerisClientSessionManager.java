
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro.web;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.util.WebUtils;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.InvalidatedSession;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Times;
import tekgenesis.security.shiro.SuiGenerisSessionManager;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * DefaultWebSessionManager implementation that creates an AppTokenSession if X-Tek-App-Token header
 * is present.
 */
public class SuiGenerisClientSessionManager extends SuiGenerisSessionManager {

    //~ Instance Fields ..............................................................................................................................

    private boolean      enforceInvalidation = false;
    private final Cookie tokenCookie;

    //~ Constructors .................................................................................................................................

    /** Default constructor. Set domain for cookie for SSO. */
    public SuiGenerisClientSessionManager(@Nullable String cookieDomain, boolean enforceInvalidation) {
        super(cookieDomain);

        tokenCookie              = createSessionCookie(cookieDomain);
        this.enforceInvalidation = true;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        removeToken(session, key);
    }

    @Override protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
        removeToken(session, key);
    }

    private Cookie createSessionCookie(@Nullable String cookieDomain) {
        final Cookie cookie = new SimpleCookie(SUIGEN_SESSION_TOKEN);
        cookie.setHttpOnly(true);  // more secure, protects against XSS attacks
        if (isNotEmpty(cookieDomain)) {
            getSessionIdCookie().setDomain(cookieDomain);
            cookie.setDomain(cookieDomain);
        }
        getSessionIdCookie().setMaxAge(Times.SECONDS_YEAR);
        return cookie;
    }

    private void removeToken(Session session, SessionKey key) {
        if (WebUtils.isHttp(key)) {
            tokenCookie.removeFrom(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key));
            if (enforceInvalidation) runInTransaction(() ->
                    InvalidatedSession.create(session.getId().toString())
                                      .setExpired(DateTime.current())
                                      .insert());
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String SUIGEN_SESSION_TOKEN = "SuiGeneris-Token";
}  // end class SuiGenerisClientSessionManager
