
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import javax.naming.NamingException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

import tekgenesis.security.shiro.web.ShiroAuthenticationFilter;

/**
 * Strategy that is aware of password expiration and disabled accounts in LDAP.
 */

public class LDAPAccountAwareStrategy extends FirstSuccessfulStrategy {

    //~ Methods ......................................................................................................................................

    @Override public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo,
                                                     AuthenticationInfo aggregateInfo, Throwable t)
        throws AuthenticationException
    {
        if (t != null && t.getCause() instanceof NamingException) checkNamingException((NamingException) t.getCause());

        return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
    }

    //~ Methods ......................................................................................................................................

    /** Check for LDAP expiration or disabled account. */
    public static void checkNamingException(NamingException authenticationException) {
        final String explanation = authenticationException.getExplanation();
        // noinspection DuplicateStringLiteralInspection
        if (explanation.contains("expired")) throw ShiroAuthenticationFilter.expiredPassword();
        else if (explanation.contains("inactivated")) throw ShiroAuthenticationFilter.inactivatedAccount();
    }
}
