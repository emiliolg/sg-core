
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.social;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * Facebook Authentication Info.
 */
public class FacebookAuthenticationInfo implements AuthenticationInfo {

    //~ Instance Fields ..............................................................................................................................

    private final String accessToken;

    private final String email;
    private final String id;
    private final String name;

    private final PrincipalCollection principalCollection;

    //~ Constructors .................................................................................................................................

    /** Constructor with user details and realm name. */
    public FacebookAuthenticationInfo(FacebookUserDetails facebookUserDetails, String realmName, String accessToken) {
        name  = facebookUserDetails.getFirstName() + " " + facebookUserDetails.getLastName();
        email = facebookUserDetails.getEmail();
        id    = facebookUserDetails.getId();
        final Collection<String> principals = new ArrayList<>();
        principals.add(email);
        principalCollection = new SimplePrincipalCollection(principals, realmName);
        this.accessToken    = accessToken;
    }

    //~ Methods ......................................................................................................................................

    /** Return access token. */
    public String getAccessToken() {
        return accessToken;
    }

    @Override public Object getCredentials() {
        return null;  // no credentials required
    }

    /** Facebook email. */
    public String getEmail() {
        return email;
    }

    /** Facebook id. */
    public String getId() {
        return id;
    }

    /** Facebook name. */
    public String getName() {
        return name;
    }

    @Override public PrincipalCollection getPrincipals() {
        return principalCollection;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}  // end class FacebookAuthenticationInfo
