
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
public class ApplicationToken implements AuthenticationToken {

    //~ Instance Fields ..............................................................................................................................

    private final String code;
    private String       surrogate;
    private String       username = null;

    //~ Constructors .................................................................................................................................

    /** Constricutor with facebook oauth code and redirect uri. */
    public ApplicationToken(String code, String surrogate) {
        this.code      = code;
        this.surrogate = surrogate;
    }

    //~ Methods ......................................................................................................................................

    /** Returns code. */
    public String getCode() {
        return code;
    }

    @Override public Object getCredentials() {
        return "";  // we don't need them
    }

    @Override public Object getPrincipal() {
        return code;
    }

    /** Get surrogate. */
    public String getSurrogate() {
        return surrogate;
    }

    /** set Surrogate user. */
    public void setSurrogate(String surrogate) {
        this.surrogate = surrogate;
    }

    /** Set user name to token. */
    public String getUsername() {
        return username;
    }

    /** Return user name. */
    public void setUsername(String username) {
        this.username = username;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}  // end class ApplicationToken
