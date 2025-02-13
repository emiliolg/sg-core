
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.jetbrains.annotations.NotNull;

/**
 * Social login token.
 */
public class SocialToken implements RememberMeAuthenticationToken {

    //~ Instance Fields ..............................................................................................................................

    private final WebContext       context;
    private final SocialCredential credentials;

    private Profile principal = null;  // User id

    private final String provider;

    //~ Constructors .................................................................................................................................

    private SocialToken(@NotNull final String provider, @NotNull final SocialCredential credentials, @NotNull final WebContext context) {
        this.provider    = provider;
        this.credentials = credentials;
        this.context     = context;
    }

    //~ Methods ......................................................................................................................................

    /** Return associated web context. */
    public WebContext getContext() {
        return context;
    }

    @Override public SocialCredential getCredentials() {
        return credentials;
    }

    @Override public boolean isRememberMe() {
        return false;
    }

    @Override public Profile getPrincipal() {
        return principal;
    }

    /** Update principal id. */
    public void setPrincipal(Profile principal) {
        this.principal = principal;
    }

    /** Return social client that generated actual token. */
    public String getProvider() {
        return provider;
    }

    //~ Methods ......................................................................................................................................

    /** Create SocialToken with specified social client, credentials, and web context. */
    public static SocialToken create(@NotNull final String client, @NotNull final SocialCredential credentials, @NotNull final WebContext context) {
        return new SocialToken(client, credentials, context);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8683203254931072016L;
}  // end class SocialToken
