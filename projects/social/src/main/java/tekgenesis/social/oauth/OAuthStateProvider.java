
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.social.oauth;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.social.WebContext;

/**
 * Base OAuth with state parameter provider (supports only 2.0 clients).
 */
public abstract class OAuthStateProvider<U extends OAuthProfile> extends OAuthProvider<U> {

    //~ Methods ......................................................................................................................................

    /** Redirect to the authentication provider for a social client. */
    public String redirect(@NotNull final WebContext context, @NotNull final String state) {
        return createService(context, state).getAuthorizationUrl();
    }

    @NotNull protected OAuth20Service createService(@NotNull final WebContext context, @NotNull final String state) {
        final OAuthConfig config = createOAuthConfig(callbacks.computeCallback(context), state);
        return api().createService(config);
    }
}
