
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro.web;

import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.jetbrains.annotations.NonNls;

import tekgenesis.common.web.UserAgentUtil;
import tekgenesis.security.shiro.LDAPAccountAwareStrategy;

import static tekgenesis.authorization.AuthorizationHandler.SHIRO_INACTIVE_ACCOUNT_ATTR;
import static tekgenesis.authorization.AuthorizationHandler.SHIRO_PASSWORD_EXPIRED_ATTR;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.REDIRECTION;
import static tekgenesis.common.service.HeaderNames.*;
import static tekgenesis.security.shiro.web.URLConstants.SUCCESS_URI;

/**
 * Shiro Authentication filter that supports OU.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class ShiroAuthenticationFilter extends FormAuthenticationFilter {

    //~ Methods ......................................................................................................................................

    @Override protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        final String username = getUsername(request);
        final String password = request.getParameter(getPasswordParam());
        final String host     = getHost(request);

        return new UsernamePasswordToken(username, password, host);
    }

    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response)
        throws Exception
    {
        final String successUrl = notEmpty(request.getParameter(REDIRECTION), getSuccessUrl());
        WebUtils.redirectToSavedRequest(request, response, successUrl);
    }

    @Override protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                               ServletResponse response) {
        AuthenticationException exception = e;
        if (e.getCause() != null && e.getCause() instanceof NamingException) {
            try {
                LDAPAccountAwareStrategy.checkNamingException((NamingException) e.getCause());
            }
            catch (final AuthenticationException auth) {
                exception = auth;
            }
        }
        if (exception instanceof ExpiredPasswordException) request.setAttribute(SHIRO_PASSWORD_EXPIRED_ATTR, "true");
        else if (exception instanceof InactivatedAccountPasswordException) request.setAttribute(SHIRO_INACTIVE_ACCOUNT_ATTR, true);
        else return super.onLoginFailure(token, e, request, response);
        return true;
    }

    @Override protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
        throws Exception
    {
        final boolean            result      = super.onLoginSuccess(token, subject, request, response);
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String ip = notEmpty(httpRequest.getHeader(X_FORWARD_FOR), notEmpty(httpRequest.getHeader(X_REAL_IP), httpRequest.getRemoteAddr()));
        subject.getSession().setAttribute(CLIENT_IP, ip);

        final String userAgent = httpRequest.getHeader(USER_AGENT);
        subject.getSession().setAttribute(IS_MOBILE, UserAgentUtil.isMobile(notNull(userAgent)));

        subject.getSession().setAttribute(TIMEZONE_PARAM, request.getParameter(TIMEZONE_PARAM));
        return result;
    }

    @Override protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return !SUCCESS_URI.equals(request.getParameter(REDIRECTION)) && super.isAccessAllowed(request, response, mappedValue);
    }

    //~ Methods ......................................................................................................................................

    /** Create execption for password expired. */
    public static AuthenticationException expiredPassword() {
        return new ExpiredPasswordException();
    }
    /** Create exception for account inactivated. */
    public static AuthenticationException inactivatedAccount() {
        return new InactivatedAccountPasswordException();
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String IS_MOBILE      = "mobile";
    @NonNls public static final String  CLIENT_IP      = "client-ip";
    @NonNls public static final String  TIMEZONE_PARAM = "timezone";

    //~ Inner Classes ................................................................................................................................

    private static class ExpiredPasswordException extends AuthenticationException {
        private static final long serialVersionUID = 6633604402156071250L;
    }

    private static class InactivatedAccountPasswordException extends AuthenticationException {
        private static final long serialVersionUID = 6633604402156071251L;
    }
}  // end class ShiroAuthenticationFilter
