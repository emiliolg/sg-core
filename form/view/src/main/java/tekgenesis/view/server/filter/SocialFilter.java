
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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.social.*;

import static tekgenesis.authorization.AuthorizationHandler.SHIRO_LOGIN_FAILURE_ATTR;
import static tekgenesis.authorization.social.SocialProviderService.getProviders;
import static tekgenesis.common.Predefined.notEmpty;

/**
 * Social authenticating filter.
 */
public class SocialFilter extends AuthenticatingFilter {

    //~ Constructors .................................................................................................................................

    /** Creates Social filter. */
    public SocialFilter() {
        getProviders().forEach(p -> processPathConfig(p.getCallbackManager().getCallbackPath(), p.getName()));
    }

    //~ Methods ......................................................................................................................................

    @Override protected AuthenticationToken createToken(ServletRequest req, ServletResponse resp)
        throws Exception
    {
        final HttpServletRequest request = (HttpServletRequest) req;

        final SocialProvider<SocialCredential, Profile> client = getSocialClient(request);
        if (client != null) {
            final WebContext       context     = WebContextImpl.create(request);
            final SocialCredential credentials = client.getCredentials(context);
            return SocialToken.create(client.getName(), credentials, context);
        }
        throw invalidSocialProvider();
    }

    @Override protected void issueSuccessRedirect(ServletRequest req, ServletResponse response)
        throws Exception
    {
        final HttpServletRequest                        request    = (HttpServletRequest) req;
        final SocialProvider<SocialCredential, Profile> provider   = getSocialClient(request);
        String                                          successUrl = getSuccessUrl();
        if (provider != null) {
            final WebContext context = WebContextImpl.create(request);
            successUrl = notEmpty(provider.getLoginManager().getLoginUrl(context), successUrl);
        }
        WebUtils.redirectToSavedRequest(req, response, successUrl);
    }

    @Override protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
        throws Exception
    {
        final AuthenticationToken token = createToken(request, response);
        try {
            final Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        }
        catch (final SocialException e) {
            return onLoginFailure(token, null, request, response);
        }
        catch (final AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    @Override protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest req, ServletResponse response) {
        req.setAttribute(SHIRO_LOGIN_FAILURE_ATTR, e.getMessage());
        final HttpServletRequest                        request = (HttpServletRequest) req;
        final SocialProvider<SocialCredential, Profile> client  = getSocialClient(request);

        if (client != null) {
            try {
                final WebContext context = WebContextImpl.create(request);
                WebUtils.redirectToSavedRequest(request, response, client.getLoginManager().getLoginFailedUrl(context));
                return false;
            }
            catch (final IOException ignore) {}
        }

        return true;
    }

    @Override protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
        throws Exception
    {
        issueSuccessRedirect(request, response);
        return false;  // We handled the success redirect directly, prevent the chain from continuing
    }

    @Override protected void redirectToLogin(ServletRequest req, ServletResponse resp)
        throws IOException
    {
        final HttpServletRequest  request  = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;

        final SocialProvider<SocialCredential, Profile> client = getSocialClient(request);
        if (client != null) {
            final WebContext context  = WebContextImpl.create(request);
            final String     redirect = client.redirect(context);
            WebUtils.issueRedirect(request, response, redirect);
        }
        else throw invalidSocialProvider();
    }

    /**
     * Returns false to always force authentication (user is never considered authenticated by this
     * filter).
     */
    @Override protected boolean isAccessAllowed(final ServletRequest request, final ServletResponse response, final Object values) {
        if (values instanceof String[]) {
            final String provider = ((String[]) values)[0];
            request.setAttribute(MATCHED_PROVIDER, provider);
        }
        return false;
    }

    private IllegalStateException invalidSocialProvider() {
        return new IllegalStateException("Invalid social provider");
    }

    @Nullable private SocialProvider<SocialCredential, Profile> getSocialClient(@NotNull final HttpServletRequest request) {
        final Object attribute = request.getAttribute(MATCHED_PROVIDER);
        return SocialProviderService.getProvider(String.valueOf(attribute));
    }

    //~ Static Fields ................................................................................................................................

    private static final String MATCHED_PROVIDER = "SocialFilter.MATCHED_PROVIDER";
}
