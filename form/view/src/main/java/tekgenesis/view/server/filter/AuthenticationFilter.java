
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MDC;

import tekgenesis.SecurityMethodFactory;
import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.authorization.InvalidatedSession;
import tekgenesis.authorization.User;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.authorization.shiro.SuiGenerisToken;
import tekgenesis.authorization.shiro.sso.SSOMode;
import tekgenesis.authorization.shiro.sso.SSOProps;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.context.ContextImpl;
import tekgenesis.common.env.security.Principal;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.service.server.Request;
import tekgenesis.common.util.CalendarUtils;
import tekgenesis.common.util.LruCache;
import tekgenesis.metadata.handler.SecureMethod;
import tekgenesis.security.shiro.ShiroConfiguration;
import tekgenesis.security.shiro.web.ShiroClientTokenAuthenticationFilter;
import tekgenesis.security.shiro.web.TokenSession;
import tekgenesis.service.HttpRequest;

import static java.lang.String.format;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import static ch.qos.logback.classic.ClassicConstants.*;

import static org.apache.shiro.SecurityUtils.getSubject;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Constants.LOGIN_URI;
import static tekgenesis.common.core.Constants.REQUEST_METHOD;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.common.env.security.SecurityUtils.getSession;
import static tekgenesis.common.service.HeaderNames.*;
import static tekgenesis.metadata.form.model.FormConstants.CURRENT_FORM_ID;
import static tekgenesis.metadata.form.model.FormConstants.FORM_LIFECYCLE_COOKIE;
import static tekgenesis.security.shiro.web.ShiroAuthenticationFilter.TIMEZONE_PARAM;
import static tekgenesis.security.shiro.web.ShiroClientTokenAuthenticationFilter.decrypt;
import static tekgenesis.security.shiro.web.SuiGenerisClientSessionManager.SUIGEN_SESSION_TOKEN;
import static tekgenesis.security.shiro.web.URLConstants.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;
import static tekgenesis.view.server.servlet.Servlets.getCookie;

/**
 * Default to admin authentication if skip auth is set.
 */
public class AuthenticationFilter extends BaseFilter {

    //~ Instance Fields ..............................................................................................................................

    private boolean enforceInvalidation = false;

    private HashMap<String, List<RE>> restrictedPaths      = null;
    private LruCache<String, Boolean> restrictedPathsCache = null;
    private boolean                   useClientToken;

    //~ Methods ......................................................................................................................................

    @Override public void init(FilterConfig filterConfig)
        throws ServletException
    {
        calculateIpFilters();
        final SSOProps ssoProps = Context.getProperties(SSOProps.class);
        useClientToken      = ssoProps.mode == SSOMode.CLIENT;
        enforceInvalidation = ssoProps.enforceInvalidation;
    }

    @Override void doFilter(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull FilterChain chain)
        throws IOException, ServletException
    {
        final String uri = getUri(req);

        if (RESET_IP_FILTERS.equals(uri)) calculateIpFilters();

        if (!isAllowed(uri, getClientIp(req))) {
            logger.warning(format("Deny access to '%s' from %s", uri, req.getRemoteAddr()));
            resp.sendError(SC_NOT_FOUND);
        }
        else if (uri.contains(JSESSIONID))
        // Redirect to remove JSESSIONID from the browser url
        resp.sendRedirect(cleanJSessionId(req.getRequestURL().toString()));
        else {
            boolean authenticated = getSession().isAuthenticated();

            if (!authenticated && useClientToken) authenticated = checkSessionToken(req);

            if (hasAppToken(req)) {
                if (authenticated)
                // clean app-token if it is defined in the cookie, in order to use the sessionId
                new SimpleCookie(HeaderNames.TEK_APP_TOKEN).removeFrom(req, resp);
                else {
                    final Request request = new HttpRequest(req);
                    authenticated = SecurityMethodFactory.build(SecureMethod.APPLICATIONTOKEN).authenticate(request);
                }
            }

            // check for skipping auth and auto login
            if (!authenticated) {
                final String servletPath = req.getServletPath();
                authenticated = autoLogin(servletPath);

                if (!authenticated) {
                    final String referer = req.getHeader(REFERER);
                    if (referer != null) {
                        try {
                            authenticated = autoLogin(new URI(referer).getPath());
                        }
                        catch (final URISyntaxException ignored) {}  // not authenticated, nothing to do!
                    }
                }
            }

            if (authenticated && uri.startsWith(LOGIN_URI) && !SUCCESS_URI.equals(req.getParameter(REDIRECTION)))
            // hack to redirect to app if Shiro didn't (trying to access login page even when authenticated)
            resp.sendRedirect(INDEX_URI);
            else {
                setLocaleAndTimezone(req);

                putMdcProperties(req, authenticated);

                // Sets current form to Context.
                setCurrentHistoryForm(req);

                if (useClientToken) ShiroClientTokenAuthenticationFilter.getSuigenTokenCookie(getSubject()).saveTo(req, resp);
                // Sets current host to Context.
                getContext().setHost(getRemoteHost(req));

                SecurityUtils.getSession().setAuditUser();

                // continue chain with authentication
                try {
                    advance(req, resp, chain);
                }
                finally {
                    removeMdcProperties();
                }
            }
        }
    }  // end method doFilter

    private boolean autoLogin(@NotNull final String path) {
        final ShiroProps            shiroProps = Context.getEnvironment().get(ShiroProps.class);
        final Tuple<String, String> userPass   = shiroProps.getAutoLogin(path);
        if (userPass != null) {
            getSession().authenticate(userPass.first(), userPass.second());
            return getSession().isAuthenticated();
        }
        return false;
    }

    private void calculateIpFilters() {
        final HashMap<String, List<RE>> map   = new HashMap<>();
        final ApplicationProps          props = Context.getProperties(ApplicationProps.class);
        for (final String path : props.restrictedPaths.split(",")) {
            final ApplicationProps.PathFilter filter = Context.getProperties(path, ApplicationProps.PathFilter.class);
            if (filter.path != null) {
                final ArrayList<RE> allowed = new ArrayList<>();
                for (final String allowedIp : filter.allowed.split(",")) {
                    try {
                        if ("*".equals(allowedIp)) allowed.add(new RE(""));
                        else if (!isEmpty(allowedIp)) allowed.add(new RE(allowedIp));
                    }
                    catch (final RESyntaxException e) {
                        logger.warning("Invalid allowed pattern '" + allowedIp + "' for path '" + path + "'");
                    }
                }
                if (!allowed.isEmpty()) map.put(filter.path, allowed);
            }
        }
        restrictedPaths      = map;
        restrictedPathsCache = LruCache.createLruCache(100);
    }

    private boolean changingLocale(String userChooseLocale) {
        return isNotEmpty(userChooseLocale);
    }

    private boolean checkSessionToken(@NotNull HttpServletRequest req) {
        final Option<Cookie> suigenToken   = getCookie(req, SUIGEN_SESSION_TOKEN);
        boolean              authenticated = false;
        if (suigenToken.isPresent() && !req.getRequestURI().equals(LOGIN_URI)) {
            final SimpleSession session = getSimpleSession(suigenToken);
            if (session != null && !session.isExpired() && session.getAttributes() != null) {
                if (enforceInvalidation) {
                    final InvalidatedSession invalidatedSession = InvalidatedSession.find(session.getId().toString());
                    if (invalidatedSession != null) return false;
                }
                final SuiGenerisToken appToken = new SuiGenerisToken((String) session.getAttributes().get(ShiroConfiguration.USER_ID));
                if (appToken.getUsername() != null) {
                    SecurityUtils.getSession().authenticate(appToken);
                    authenticated = true;
                }
            }
        }
        return authenticated;
    }

    private String cleanJSessionId(final String url) {
        final String uri   = url.substring(0, url.indexOf(JSESSIONID));
        final int    query = url.indexOf("?");
        return query == -1 ? uri : uri + url.substring(query);
    }

    private boolean hasAppToken(@NotNull HttpServletRequest req) {
        return isNotEmpty(req.getHeader(TEK_APP_TOKEN)) || !fromArray(req.getCookies()).filter(cookie ->
                TEK_APP_TOKEN.equals(cookie.getName()))
               .isEmpty();
    }

    private void putMdcProperties(HttpServletRequest req, boolean authenticated) {
        MDC.put(USER_MDC_KEY, authenticated ? getSession().getPrincipal().getId() : "");

        MDC.put(REQUEST_REMOTE_HOST_MDC_KEY, req.getRemoteAddr());
        MDC.put(REQUEST_METHOD, req.getMethod());
        MDC.put(REQUEST_REQUEST_URL, req.getRequestURL().toString());
        MDC.put(REQUEST_REQUEST_URI, req.getRequestURI());
        MDC.put(REQUEST_X_FORWARDED_FOR, req.getHeader(X_FORWARD_FOR));
        MDC.put(REQUEST_QUERY_STRING, req.getQueryString());
        MDC.put(REQUEST_USER_AGENT_MDC_KEY, req.getHeader(USER_AGENT));

        final String lifecycle = getContext().getLifeCycleId().getOrNull();
        MDC.put(LIFECYCLE_KEY, lifecycle);
        MDC.put(REQ_UUID, UUID.randomUUID().toString());
    }

    private void removeMdcProperties() {
        MDC.clear();
    }

    private void setupContext(@NotNull final ServletRequest req, @NotNull final Locale locale) {
        final ContextImpl context = getContext();

        // Set locale on context
        context.setLocale(locale);

        // Set life cycle id on context
        for (final Cookie c : getCookie((HttpServletRequest) req, FORM_LIFECYCLE_COOKIE))
            context.setLifeCycleId(c.getValue());
    }

    private void updateUserLocale(String userChooseLocale) {
        final Principal principal = getSession().getPrincipal();
        runInTransaction(() ->
                ifPresent(User.find(principal.getId()),
                    user -> {
                        user.setLocale(userChooseLocale).update();
                        principal.setLocale(new Locale(userChooseLocale));
                    }));
    }

    @NotNull private String getClientIp(@NotNull final HttpServletRequest req) {
        return notEmpty(req.getHeader(HeaderNames.X_FORWARD_FOR), req.getRemoteAddr());
    }

    private void setCurrentHistoryForm(@NotNull HttpServletRequest req) {
        final String header = req.getHeader(CURRENT_FORM_ID);
        getContext().setCurrentHistoryForm(notNull(header));
    }

    private boolean isAllowed(String uri, String remoteAddr) {
        if (restrictedPaths.isEmpty() || !notNull(restrictedPathsCache.get(uri), true)) return true;

        boolean matchedUri = false;
        for (final String path : restrictedPaths.keySet()) {
            if (uri.matches(path)) {
                matchedUri = true;
                final List<RE> reList = restrictedPaths.get(path);
                restrictedPathsCache.put(uri, true);
                for (final RE re : reList) {
                    if (re.match(remoteAddr)) return true;
                }
            }
        }
        restrictedPathsCache.put(uri, matchedUri);
        return !matchedUri;
    }

    // Sets locale from User, browser or picker
    private void setLocaleAndTimezone(ServletRequest req) {
        Locale locale = null;
        if (getSession().isAuthenticated()) {
            // Locale
            final String userChooseLocale = req.getParameter(LOCALE_PARAM);

            if (changingLocale(userChooseLocale)) updateUserLocale(userChooseLocale);

            locale = getSession().getPrincipal().getLocale();
            if (locale == null) {
                final User user = invokeInTransaction(() -> User.find(getSession().getPrincipal().getId()));
                assert user != null;
                final String userLocale = user.getLocale();
                if (userLocale != null) {
                    locale = new Locale(userLocale);
                    getSession().getPrincipal().setLocale(locale);
                }
            }

            // Timezone
            final Object timeZoneAttr = getSubject().getSession().getAttribute(TIMEZONE_PARAM);
            if (timeZoneAttr != null) CalendarUtils.setSessionTimeZoneOffset(Integer.parseInt((String) timeZoneAttr));
        }

        setupContext(req, notNull(locale, req.getLocale()));
    }

    @NotNull private String getRemoteHost(@NotNull HttpServletRequest req) {
        return notEmpty(req.getHeader(X_FORWARDED_HOST), req.getHeader(HOST));
    }

    @Nullable private SimpleSession getSimpleSession(Option<Cookie> diegorToken) {
        final ObjectMapper objectMapper = new ObjectMapper();

        final TokenSession session;
        try {
            session = cast(
                    objectMapper.readValue(new String(decrypt(Base64.getDecoder().decode(diegorToken.get().getValue())), "UTF-8"),
                        TokenSession.class));
        }
        catch (final Exception e) {
            logger.error(e);
            return null;
        }
        return session.asSession();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);
}  // end class AuthenticationFilter
