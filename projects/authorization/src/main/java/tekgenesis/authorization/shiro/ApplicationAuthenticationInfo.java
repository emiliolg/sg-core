
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.jetbrains.annotations.NotNull;

/**
 * Application Authentication Info Every Application is associated with a user, who generate the
 * token.
 */
public class ApplicationAuthenticationInfo implements AuthenticationInfo {

    //~ Instance Fields ..............................................................................................................................

    private final PrincipalCollection principalCollection;

    private final String token;
    private final String username;

    //~ Constructors .................................................................................................................................

    /** Default consturctor.* */
    public ApplicationAuthenticationInfo(@NotNull String realmName, @NotNull String token, @NotNull String username) {
        this.token    = token;
        this.username = username;

        final Collection<String> principals = new ArrayList<>();

        principals.add(username);
        principalCollection = new SimplePrincipalCollection(principals, realmName);
    }

    //~ Methods ......................................................................................................................................

    @Override public Object getCredentials() {
        return "";  // We dont know them
    }

    @Override public PrincipalCollection getPrincipals() {
        return principalCollection;
    }

    /** Application token.* */
    public String getToken() {
        return token;
    }

    /** Username associated with the application token.* */
    public String getUsername() {
        return username;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4389585772755686298L;
}  // end class ApplicationAuthenticationInfo
