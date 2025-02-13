
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import java.util.Arrays;
import java.util.Locale;

import org.apache.shiro.subject.Subject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.env.security.Principal;

import static tekgenesis.common.Predefined.notNull;

/**
 * Implements the principal over the Shiro subject.
 */
public class ShiroPrincipal implements Principal {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Subject subject;

    //~ Constructors .................................................................................................................................

    /** Creates a Principal over a Shiro Subject. */
    public ShiroPrincipal(@NotNull Subject subject) {
        this.subject = subject;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasAllPermissions(String... permissions) {
        return subject.isPermittedAll(permissions);
    }

    @Override public boolean hasAllRoles(String... roles) {
        return subject.hasAllRoles(Arrays.asList(roles));
    }

    @Override public boolean hasAnyPermissions(String... permissions) {
        for (final boolean permitted : subject.isPermitted(permissions))
            if (permitted) return true;

        return false;
    }

    @Override public boolean hasAnyRole(String... roles) {
        for (final String role : roles)
            if (subject.hasRole(role)) return true;

        return false;
    }

    @Override public boolean hasPermission(String permission) {
        return subject.isPermitted(permission);
    }

    @Override public boolean hasRole(String role) {
        return subject.hasRole(role);
    }

    @NotNull @Override public String getFacebookId() {
        return notNull(attr(ShiroConfiguration.FACEBOOK_ID));
    }

    @NotNull @Override public String getFacebookToken() {
        return notNull(attr(ShiroConfiguration.FACEBOOK_TOKEN));
    }

    @NotNull @Override public String getId() {
        return attr(ShiroConfiguration.USER_ID);
    }

    @Override public Locale getLocale() {
        return (Locale) subject.getSession().getAttribute(ShiroConfiguration.LOCALE);
    }

    @Override public void setLocale(@Nullable Locale locale) {
        subject.getSession().setAttribute(ShiroConfiguration.LOCALE, locale);
    }

    @Override public boolean isSystem() {
        return false;
    }

    @NotNull @Override public String getName() {
        return attr(ShiroConfiguration.USER_NAME);
    }

    @Override public String getOrgUnit() {
        return attr(SuiGenerisAuthorizingRealm.CURRENT_OU);
    }

    @NotNull @Override public String getSessionProperty(String property) {
        return attr(property);
    }

    @Override public String getSurrogate() {
        return attr(ShiroConfiguration.SURROGATE_USER);
    }

    private String attr(String key) {
        return (String) subject.getSession().getAttribute(key);
    }
}  // end class ShiroPrincipal
