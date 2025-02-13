
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import java.util.UUID;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.AppTokenSession;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Times;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.service.HeaderNames.TEK_APP_TOKEN;

/**
 * DefaultWebSessionManager implementation that creates an AppTokenSession if X-Tek-App-Token header
 * is present.
 */
public class SuiGenerisSessionManager extends DefaultWebSessionManager {

    //~ Constructors .................................................................................................................................

    /** Default constructor. Set domain for cookie for SSO. */
    public SuiGenerisSessionManager(@Nullable String cookieDomain) {
        super();
        if (isNotEmpty(cookieDomain)) getSessionIdCookie().setDomain(cookieDomain);
        getSessionIdCookie().setMaxAge(Times.SECONDS_YEAR);
    }

    //~ Methods ......................................................................................................................................

    @Override protected Session doCreateSession(SessionContext context) {
        if (context instanceof DefaultWebSessionContext) {
            final ServletRequest servletRequest = ((DefaultWebSessionContext) context).getServletRequest();
            if (servletRequest instanceof ShiroHttpServletRequest) {
                final ShiroHttpServletRequest request = (ShiroHttpServletRequest) servletRequest;
                if (isNotEmpty(request.getHeader(TEK_APP_TOKEN))) {
                    final SimpleSession simpleSession = new AppTokenSession();

                    final boolean cookieFound = !ImmutableList.fromArray(request.getCookies())
                                                .filter(c ->
                                c.getName().equals(TEK_APP_TOKEN))
                                                .isEmpty();

                    // noinspection DuplicateStringLiteralInspection
                    simpleSession.setAttribute("cache", cookieFound);

                    new SimpleCookie(TEK_APP_TOKEN).removeFrom(request,
                        ((ShiroHttpServletResponse) ((DefaultWebSessionContext) context).getServletResponse()));

                    simpleSession.setId(UUID.randomUUID().toString());
                    return simpleSession;
                }
            }
        }
        return super.doCreateSession(context);
    }

    @Override protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
        removeAppToken(session, key);
    }

    private void removeAppToken(Session session, SessionKey key) {
        if (WebUtils.isHttp(key)) new SimpleCookie(TEK_APP_TOKEN).removeFrom(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key));
    }
}  // end class SuiGenerisSessionManager
