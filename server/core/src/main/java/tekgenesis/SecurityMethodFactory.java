
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.shiro.ApplicationToken;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.cookie.Cookie;
import tekgenesis.common.service.server.Request;
import tekgenesis.metadata.handler.SecureMethod;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.service.HeaderNames.*;

/**
 * SecurityMethod factory.
 */
public final class SecurityMethodFactory {

    //~ Constructors .................................................................................................................................

    private SecurityMethodFactory() {}

    //~ Methods ......................................................................................................................................

    /** Build SecurityMethod.* */
    public static SecurityMethod build(@NotNull final SecureMethod method) {
        switch (method) {
        case UNRESTRICTED:
            return new UnrestrictedSecurityMethod();
        case APPLICATIONTOKEN:
            return new ApplicationTokenSecurityMethod();
        case USERNAMEPASSWORD:
            return new UsernamepasswordSecurityMethod();
        }
        throw unreachable();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(SecurityMethodFactory.class);

    //~ Inner Interfaces .............................................................................................................................

    public interface SecurityMethod {
        /** Authenticate request.* */
        boolean authenticate(@NotNull Request request);
    }

    //~ Inner Classes ................................................................................................................................

    static class ApplicationTokenSecurityMethod implements SecurityMethod {
        @Override public boolean authenticate(@NotNull Request request) {
            final Headers headers        = request.getHeaders();
            final String  tekAppToken    = headers.getOrEmpty(TEK_APP_TOKEN);
            final String  tokenSurrogate = headers.getOrEmpty(TEK_APP_TOKEN_SURROGATE);
            String        token          = tekAppToken == null ? headers.getOrEmpty(OLD_TEK_APP_TOKEN) : tekAppToken;

            if (isEmpty(token)) {
                final Option<Cookie> first = request.getCookies().filter(c -> c.getName().equals(TEK_APP_TOKEN)).getFirst();
                if (first.isPresent()) token = first.get().getValue();
            }

            final ApplicationToken appToken      = new ApplicationToken(token, tokenSurrogate);
            boolean                authenticated = false;
            try {
                SecurityUtils.getSession().authenticate(appToken);
                authenticated = true;
            }
            catch (final Exception e) {
                logger.debug(e);
            }
            return authenticated;
        }
    }

    static class UnrestrictedSecurityMethod implements SecurityMethod {
        @Override public boolean authenticate(@NotNull Request request) {
            return true;
        }
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    static class UsernamepasswordSecurityMethod implements SecurityMethod {
        @Override public boolean authenticate(@NotNull Request request) {
            // Just check that the rest was already authenticated passing trough the cookie
            return SecurityUtils.getSession().isAuthenticated();
        }
    }
}  // end class SecurityMethodFactory
