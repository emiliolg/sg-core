
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.io.Serializable;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.env.security.Principal;
import tekgenesis.common.env.security.Session;

/**
 * A dummy session for tests.
 */
public class TestSession implements Session {

    //~ Methods ......................................................................................................................................

    @Override public void authenticate(Object authenticationToken) {}
    @Override public void authenticate(String user, String password) {}
    @Override public void authenticate(String user, String password, boolean rememberMe) {}
    @Override public void logout() {}

    @Override public String getAuditUser() {
        return TEST_USER;
    }

    @Override public void setAuditUser() {}

    @Override public String getClientIp() {
        return null;
    }
    @Override public boolean isAuthenticated() {
        return true;
    }
    @Override public boolean isRemembered() {
        return true;
    }
    @Override public Serializable getId() {
        return null;
    }

    @Override public Principal getPrincipal() {
        return new Principal() {
            @Override public boolean hasAllPermissions(String... permissions) {
                return false;
            }
            @Override public boolean hasAllRoles(String... roles) {
                return false;
            }
            @Override public boolean hasAnyPermissions(String... permissions) {
                return false;
            }
            @Override public boolean hasAnyRole(String... roles) {
                return false;
            }
            @Override public boolean hasPermission(String permission) {
                return false;
            }
            @Override public boolean hasRole(String role) {
                return false;
            }
            @NotNull @Override public String getId() {
                return TEST_USER;
            }
            @NotNull @Override public String getFacebookToken() {
                return "";
            }
            @NotNull @Override public String getFacebookId() {
                return "";
            }
            @NotNull @Override public String getSessionProperty(String property) {
                return "";
            }
            @Override public String getSurrogate() {
                return null;
            }
            @Override public boolean isSystem() {
                return false;
            }
            @Override public Locale getLocale() {
                return Locale.ENGLISH;
            }
            @Override public void setLocale(@Nullable Locale locale) {}
            @NotNull @Override public String getName() {
                return TEST_USER;
            }
            @Override public String getOrgUnit() {
                return "";
            }
        };
    }  // end method getPrincipal

    @Override public int getTimeout() {
        return -1;
    }

    @Override public void setTimeout(int seconds) {}

    //~ Static Fields ................................................................................................................................

    public static final String TEST_USER = "testUser";
}  // end class TestSession
