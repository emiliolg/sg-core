
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import java.io.Serializable;
import java.util.Locale;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.util.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.env.security.Principal;
import tekgenesis.common.env.security.Session;
import tekgenesis.security.shiro.web.ShiroAuthenticationFilter;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notEmpty;

/**
 * Session implementation over the Shiro session.
 */
public class ShiroSession implements Session {

    //~ Instance Fields ..............................................................................................................................

    private String auditUser = null;

    //~ Constructors .................................................................................................................................

    /** Creates a new Session. */
    public ShiroSession() {}

    //~ Methods ......................................................................................................................................

    @Override public void authenticate(Object authenticationToken) {
        final AuthenticationToken shiroAuthenticationToken = cast(authenticationToken);
        SecurityUtils.getSubject().login(shiroAuthenticationToken);
    }

    @Override public void authenticate(String user, String password) {
        authenticate(new UsernamePasswordToken(user, password));
    }

    @Override public void authenticate(String user, String password, boolean remember) {
        authenticate(new UsernamePasswordToken(user, password, remember));
    }

    @Override public void logout() {
        SecurityUtils.getSubject().logout();
    }

    @Override public String getAuditUser() {
        return notEmpty(auditUser, "System");
    }

    @Override public void setAuditUser() {
        auditUser = getPrincipal().getId();
    }

    @Nullable @Override public String getClientIp() {
        final Object attribute = SecurityUtils.getSubject().getSession(false).getAttribute(ShiroAuthenticationFilter.CLIENT_IP);
        return attribute != null ? String.valueOf(attribute) : null;
    }

    @Override public boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    @Override public boolean isRemembered() {
        return SecurityUtils.getSubject().isRemembered();
    }

    @Override public Serializable getId() {
        return SecurityUtils.getSubject().getSession().getId();
    }

    @Override public Principal getPrincipal() {
        try {
            if (!SecurityUtils.getSubject().isAuthenticated()) return SYSTEM_PRINCIPAL;
            else return new ShiroPrincipal(SecurityUtils.getSubject());
        }
        catch (final UnavailableSecurityManagerException e) {
            return SYSTEM_PRINCIPAL;
        }
    }

    @Override public int getTimeout() {
        final org.apache.shiro.session.Session session = SecurityUtils.getSubject().getSession(false);
        if (session != null) return (int) (session.getTimeout() / 1000);
        return -1;
    }

    @Override public void setTimeout(int seconds) {
        final org.apache.shiro.session.Session session = SecurityUtils.getSubject().getSession(false);
        if (session != null) session.setTimeout(seconds * 1000);
    }

    //~ Methods ......................................................................................................................................

    /** Clears the thread context. */
    public static void clear() {
        ThreadContext.unbindSubject();
        ThreadContext.unbindSecurityManager();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String     SYSTEM           = "system";
    private static final Principal SYSTEM_PRINCIPAL = new Principal() {
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
                return SYSTEM;
            }

            @NotNull @Override public String getSessionProperty(String property) {
                return "";
            }

            @Override public String getSurrogate() {
                return (String) SecurityUtils.getSubject().getSession().getAttribute(ShiroConfiguration.SURROGATE_USER);
            }

            @NotNull @Override public String getFacebookToken() {
                return "";
            }

            @NotNull @Override public String getFacebookId() {
                return "";
            }

            @Override public boolean isSystem() {
                return true;
            }

            @Override public Locale getLocale() {
                return Locale.getDefault();
            }

            @Override public void setLocale(@Nullable Locale locale) {}

            @NotNull @Override public String getName() {
                return SYSTEM;
            }

            @Override public String getOrgUnit() {
                return SuiGenerisAuthorizingRealm.ROOT_OU;
            }
        };
}  // end class ShiroSession
