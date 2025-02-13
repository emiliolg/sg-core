
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Application login token.
 */
public class SuiGenerisToken implements AuthenticationToken {

    //~ Instance Fields ..............................................................................................................................

    private final String user;

    //~ Constructors .................................................................................................................................

    /** Constructor with facebook oauth code and redirect uri. */
    public SuiGenerisToken(String user) {
        this.user = user;
    }

    //~ Methods ......................................................................................................................................

    @Override public Object getCredentials() {
        return "";
    }

    @Override public Object getPrincipal() {
        return user;
    }

    /** Return username. */
    public String getUsername() {
        return user;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}
