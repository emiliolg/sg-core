
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.jetbrains.annotations.NotNull;

/**
 * Social Realm for clients. It acts on credentials after the user authenticates at the provides and
 * finishes the authentication process by getting the user profile from the provider.
 */
public class SocialRealm extends AuthenticatingRealm {

    //~ Methods ......................................................................................................................................

    @Override public boolean supports(AuthenticationToken token) {
        return token instanceof SocialToken;
    }

    @Override protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException
    {
        try {
            return getSocialAuthenticationInfo((SocialToken) token);
        }
        catch (final SocialException e) {
            throw new AuthenticationException(e);
        }
    }

    private AuthenticationInfo getSocialAuthenticationInfo(@NotNull final SocialToken token) {
        final SocialProvider<SocialCredential, Profile> client = SocialProviderService.getProvider(token.getProvider());

        final SocialCredential credentials = token.getCredentials();
        final Profile          profile     = client.getUserProfile(credentials, token.getContext());

        if (profile == null) throw SocialException.cannotRetrieveProfile(client, credentials);

        token.setPrincipal(profile);

        return new SimpleAuthenticationInfo(profile, credentials, profile.getProvider());
    }
}
