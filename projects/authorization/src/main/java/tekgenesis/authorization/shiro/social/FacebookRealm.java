
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.social;

import java.net.URL;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.jetbrains.annotations.Nullable;

/**
 * Facebook Realm.
 */
public class FacebookRealm extends AuthenticatingRealm {

    //~ Methods ......................................................................................................................................

    @Override public boolean supports(AuthenticationToken token) {
        return token instanceof FacebookToken;
    }

    @Override public CredentialsMatcher getCredentialsMatcher() {
        return new FBCredentialsMatcher();
    }

    @Nullable @Override protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException
    {
        final FacebookToken facebookToken = (FacebookToken) token;

        try {
            final URL                 url        = new URL(
                    "https://graph.facebook.com/me?fields=" + FacebookUserDetails.EMAIL + "," + FacebookUserDetails.FIRST_NAME + "," +
                    FacebookUserDetails.LAST_NAME + "&access_token=" + facebookToken.getAccessToken());
            final String              fbResponse = FacebookToken.readURL(url);
            final FacebookUserDetails fud        = new FacebookUserDetails(fbResponse);
            facebookToken.setUsername(fud.getEmail());
            return new FacebookAuthenticationInfo(fud, getName(), facebookToken.getAccessToken());
        }
        catch (final Throwable e1) {
            throw new AuthenticationException(e1);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private class FBCredentialsMatcher implements CredentialsMatcher {
        @Override public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
            return true;
        }
    }
}  // end class FacebookRealm
