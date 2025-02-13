
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.authorization.social.SocialHandlerBase.Routes.login;

/**
 * Manage social provider login and user registration.
 */
public interface LoginManager {

    //~ Methods ......................................................................................................................................

    /** Return login failed url. */
    default String getLoginFailedUrl(@NotNull final WebContext context) {
        return login().getUrl();
    }

    /** Return default login url. */
    default String getLoginUrl(@NotNull final WebContext context) {
        return "/";
    }
}
