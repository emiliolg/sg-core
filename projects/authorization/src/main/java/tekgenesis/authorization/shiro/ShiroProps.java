
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

import javax.inject.Named;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Times;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;
import tekgenesis.common.env.context.Context;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * Shiro configuration properties.
 */
@Mutable
@Named("shiro")
public class ShiroProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    @ShiroProperty(section = ShiroConfigSection.USERS, name = Constants.SHIRO_ADMIN_USER)
    public String adminUserConfig = null;

    /** Autologin user:password. */
    public String autoLogin = null;

    /** Use built in authentication mechanism. */
    public boolean builtInAuthentication = false;

    /** Make user ids case insensitive. */
    public boolean caseInsensitive = false;

    /** Enables the creation of users on sucessful login. */
    public boolean createUsers = true;

    /** Facebook App Id for Facebook login. */
    public String facebookAppId = null;

    /** Facebook App Secret for Facebook login. */
    public String facebookAppSecret = null;

    /** Force SSL for login/logout urls. */
    public boolean forceLoginSsl = false;

    /** Forgot link label to appear next to the login button. */
    public String forgotPasswordLabel = "";

    /** Forgot link to appear next to the login button. */
    public String forgotPasswordUrl = "";

    public String iniFile = "";

    @ShiroProperty(section = ShiroConfigSection.MAIN, name = "ldapRealm.contextFactory.url")
    public String ldapContextFactoryUrl = null;

    @ShiroProperty(section = ShiroConfigSection.MAIN, name = "ldapRealm")
    public String ldapRealm = null;

    @ShiroProperty(section = ShiroConfigSection.MAIN, name = "ldapRealm.userDnTemplate")
    public String ldapUserDnTemplate = null;

    /** Comma separated list of autologin urls to be configured instance. */
    @SuppressWarnings("WeakerAccess")
    public String logins = null;

    /** Max session idle timeout. */
    public int maxSessionTimeout = DEFAULT_MAX_IDLE_TIME;

    public int sessionCacheSize = DEFAULT_SESSION_CACHE_MAX_SIZE;

    /** Session timeout in seconds. */
    public int sessionTimeout = DEFAULT_TIMEOUT;

    /**
     * Comma separated list of users to be configured using their name as scope for a ShiroUserProps
     * instance.
     */
    public String users = null;

    //~ Methods ......................................................................................................................................

    /** Returns autologin info for url. */
    @Nullable public Tuple<String, String> getAutoLogin(String url) {
        if (!isEmpty(logins)) {
            final String[] loginList = logins.split(",");
            for (final String login : loginList) {
                final ShiroLoginProps shiroLoginProps = Context.getEnvironment().get(login, ShiroLoginProps.class);
                if (matches(shiroLoginProps.urlPattern, url)) return getUserPass(shiroLoginProps.autoLogin);
            }
        }
        return getUserPass(autoLogin);
    }

    private boolean matches(String urlPattern, String url) {
        return url.matches(urlPattern);
    }

    @Nullable private Tuple<String, String> getUserPass(String userPass) {
        if (isEmpty(userPass)) return null;

        final String[] split = userPass.split(":");
        if (split.length < 1) return null;
        final String user     = split[0];
        String       password = null;
        if (user.equals(Constants.SHIRO_GUEST_USER)) password = Constants.SHIRO_GUEST_USER;
        else {
            if (split.length == 2) password = split[1];
        }
        return password != null ? Tuple.tuple(user, password) : null;
    }  // end method getUserPass

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_SESSION_CACHE_MAX_SIZE = 100000;

    private static final int DEFAULT_TIMEOUT       = 1800;
    private static final int DEFAULT_MAX_IDLE_TIME = Times.SECONDS_DAY;

    //~ Inner Classes ................................................................................................................................

    /**
     * Shiro user properties.
     */
    @Mutable
    @Named("shiroLogin")
    public static class ShiroLoginProps implements Properties {
        /** Autologin user:password. */
        public String autoLogin = null;
        /** Url pattern. */
        public String urlPattern = "";
    }

    /**
     * Shiro user properties.
     */
    @Mutable
    @Named("shiroUser")
    public static class ShiroUserProps implements Properties {
        /** password, roleName1, roleName2, ..., roleNameN. */
        public String userConfig = "";
    }
}  // end class ShiroProps
