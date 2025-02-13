
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.Factory;
import org.apache.shiro.web.config.WebIniSecurityManagerFactory;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.authorization.*;
import tekgenesis.authorization.g.UserBase;
import tekgenesis.authorization.shiro.*;
import tekgenesis.authorization.shiro.social.FacebookAuthenticationInfo;
import tekgenesis.authorization.shiro.social.FacebookRealm;
import tekgenesis.authorization.shiro.sso.DynamoSessionDAO;
import tekgenesis.authorization.shiro.sso.RedisSessionDAO;
import tekgenesis.authorization.shiro.sso.SSOMode;
import tekgenesis.authorization.shiro.sso.SSOProps;
import tekgenesis.authorization.social.*;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterProps;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.parser.ASTNode;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.security.shiro.web.ShiroAuthenticationFilter;
import tekgenesis.security.shiro.web.ShiroClientTokenAuthenticationFilter;
import tekgenesis.security.shiro.web.SuiGenerisClientSessionManager;

import static tekgenesis.authorization.Messages.EMAIL_ALREADY_IN_USE;
import static tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm.CURRENT_OU;
import static tekgenesis.authorization.social.SocialProviderService.getProvider;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.DateTime.current;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.security.shiro.web.URLConstants.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Util to configure Shiro security.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class ShiroConfiguration {

    //~ Instance Fields ..............................................................................................................................

    private final ApplicationProps appProps;
    private final Environment      env;

    private final SecurityManager securityManager;
    private final ShiroProps      shiroProps;
    private final SSOProps        ssoProps;
    private final boolean         webEnvironment;

    //~ Constructors .................................................................................................................................

    /** Create a Shiro Configuration. */
    public ShiroConfiguration(boolean web, Environment env) {
        shiroProps     = env.get(ShiroProps.class);
        ssoProps       = env.get(SSOProps.class);
        appProps       = env.get(ApplicationProps.class);
        webEnvironment = web;
        this.env       = env;
        final SuiGenerisAuthorizingRealm realm = new SuiGenerisAuthorizingRealm(appProps.adminUser,
                appProps.defaultRoles,
                shiroProps.caseInsensitive);

        final Ini shiroIni = shiroProps.iniFile.isEmpty() ? getIni() : Ini.fromResourcePath(shiroProps.iniFile);

        final Factory<SecurityManager> factory = web ? new WebIniSecurityManagerFactory(shiroIni) : new IniSecurityManagerFactory(shiroIni);

        securityManager = factory.getInstance();
        if (securityManager instanceof DefaultSecurityManager) {
            final DefaultSecurityManager defaultSecurityManager = (DefaultSecurityManager) securityManager;
            final List<Realm>            realms                 = new ArrayList<>();

            final ApplicationSuigenRealm applicationRealm = createApplicationRealm();

            realms.add(applicationRealm);

            if (ssoProps.mode == SSOMode.CLIENT) realms.add(createTokenRealm());

            if (shiroProps.facebookAppId != null) realms.add(new FacebookRealm());
            if (SocialProviderService.isActive()) realms.add(new SocialRealm());
            if (shiroProps.builtInAuthentication) realms.add(new SuigenAuthenticatingRealm());
            for (final Realm r : defaultSecurityManager.getRealms()) {
                if (r instanceof JndiLdapRealm) ((JndiLdapRealm) r).setAuthenticationTokenClass(UsernamePasswordToken.class);
                realms.add(r);
            }
            defaultSecurityManager.setRealms(realms);
            if (!realms.isEmpty())
                ((ModularRealmAuthenticator) defaultSecurityManager.getAuthenticator()).setAuthenticationStrategy(new FirstSuccessfulStrategy());

            final Authenticator origAuth = defaultSecurityManager.getAuthenticator();
            defaultSecurityManager.setAuthenticator(new SuiGenerisAuthenticator(origAuth, realm, shiroProps.createUsers, appProps.adminUser));
            defaultSecurityManager.setAuthorizer(realm);
            configureCaches(defaultSecurityManager, realm);
        }
        SecurityUtils.setSecurityManager(securityManager);
    }  // end ctor ShiroConfiguration

    //~ Methods ......................................................................................................................................

    /** return the Ini. */
    public Ini getIni() {
        final Ini                      ini         = new Ini();
        final Map<String, Ini.Section> iniSections = new HashMap<>();

        for (final Field field : shiroProps.getClass().getFields()) {
            final ShiroProperty annotation = field.getAnnotation(ShiroProperty.class);
            if (annotation != null) {
                final String      section     = annotation.section().getName();
                final Ini.Section iniSection  = getOrCreateIniSection(ini, iniSections, section);
                final Object      objectValue;
                try {
                    objectValue = field.get(shiroProps);
                }
                catch (final IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (objectValue != null) iniSection.put(annotation.name(), objectValue.toString());
            }
        }
        if (!Predefined.isEmpty(shiroProps.users)) {
            final String[]    userList     = shiroProps.users.split(",");
            final Ini.Section usersSection = getOrCreateIniSection(ini, iniSections, USERS_SECTION);
            for (final String userString : userList) {
                final String userName   = userString.trim();
                final String userConfig = env.get(userName, ShiroProps.ShiroUserProps.class).userConfig;
                if (!userConfig.isEmpty()) usersSection.put(userName, userConfig);
            }
        }

        if (webEnvironment) setWebConfiguration(ini);

        return ini;
    }

    /** Returns the configured Shiro Ini. */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    private void configureCache(DefaultSecurityManager defaultSecurityManager, SuiGenerisAuthorizingRealm realm) {
        if (getContext().hasBinding(InfinispanCacheManager.class)) {
            final InfinispanCacheManager cm           = Context.getSingleton(InfinispanCacheManager.class);
            final CacheManager           cacheManager = new CacheManager() {
                    public <K, V> Cache<K, V> getCache(String name)
                        throws CacheException
                    {
                        final ConcurrentMap<K, V> map = cm.getCache(name);
                        return new MapCache<>(name, map);
                    }
                };

            realm.setAuthorizationCachingEnabled(true);
            realm.setAuthorizationCacheName(AuthorizationUtils.SUIGENERIS_AUTHORIZATION_CACHE);
            realm.setCacheManager(cacheManager);

            defaultSecurityManager.setCacheManager(cacheManager);
        }
    }

    private void configureCaches(DefaultSecurityManager defaultSecurityManager, SuiGenerisAuthorizingRealm realm) {
        if (webEnvironment) {
            configureCache(defaultSecurityManager, realm);
            // shiro expire first
            final String                   cookieDomain   = ssoProps.domain;
            final DefaultWebSessionManager sessionManager = ssoProps.mode == SSOMode.CLIENT
                                                            ? new SuiGenerisClientSessionManager(cookieDomain, ssoProps.enforceInvalidation)
                                                            : new SuiGenerisSessionManager(cookieDomain);
            defaultSecurityManager.setSessionManager(sessionManager);
            final RememberMeManager rememberMeManager = defaultSecurityManager.getRememberMeManager();
            ((CookieRememberMeManager) rememberMeManager).getCookie().setDomain(cookieDomain);
            sessionManager.setGlobalSessionTimeout(shiroProps.sessionTimeout * MILLISECONDS);
            final ClusterProps clusterProps = env.get(ClusterProps.class);
            switch (ssoProps.mode) {
            case DYNAMO:
                sessionManager.setSessionDAO(new DynamoSessionDAO(shiroProps.sessionTimeout, ssoProps, clusterProps));
                break;
            case REDIS:
                sessionManager.setSessionDAO(new RedisSessionDAO(ssoProps));
                break;
            case LOCAL:
                sessionManager.setSessionDAO(new SuiGenerisCacheSessionDAO());
                break;
            case CLIENT:
                break;
            }
        }
        else {
            final DefaultSessionManager sessionManager = new DefaultSessionManager();
            defaultSecurityManager.setSessionManager(sessionManager);
            sessionManager.setGlobalSessionTimeout(-1);
        }
    }

    @NotNull private ApplicationSuigenRealm createApplicationRealm() {
        final ApplicationSuigenRealm applicationRealm = new ApplicationSuigenRealm();
        applicationRealm.setAuthenticationCachingEnabled(false);
        applicationRealm.setCachingEnabled(false);
        applicationRealm.setCacheManager(null);
        applicationRealm.setAuthenticationCache(null);
        return applicationRealm;
    }

    @NotNull private SuiGenerisTokenRealm createTokenRealm() {
        final SuiGenerisTokenRealm tokenRealm = new SuiGenerisTokenRealm();
        tokenRealm.setAuthenticationCachingEnabled(false);
        tokenRealm.setCachingEnabled(false);
        tokenRealm.setCacheManager(null);
        tokenRealm.setAuthenticationCache(null);
        return tokenRealm;
    }

    @NotNull private String forceSsl() {
        return shiroProps.forceLoginSsl ? ",ssl" : "";
    }

    private void setWebConfiguration(Ini ini) {
        Ini.Section mainSection = ini.getSection(MAIN_SECTION);
        if (mainSection == null) mainSection = ini.addSection(MAIN_SECTION);

        mainSection.putAll(
            getWebMainConfiguration(
                ssoProps.mode == SSOMode.CLIENT ? ShiroClientTokenAuthenticationFilter.class.getCanonicalName()
                                                : ShiroAuthenticationFilter.class.getCanonicalName()));

        Ini.Section users = ini.getSection(USERS_SECTION);
        if (users == null) users = ini.addSection(USERS_SECTION);

        users.putAll(getWebUsersConfiguration(users));

        Ini.Section roles = ini.getSection(ROLES_SECTION);
        if (roles == null) roles = ini.addSection(ROLES_SECTION);

        if (!roles.containsKey(Constants.SHIRO_ADMIN_ROLE)) roles.putAll(getWebRolesConfiguration());

        Ini.Section urls = ini.getSection(URLS_SECTION);
        if (urls == null) urls = ini.addSection(URLS_SECTION);

        urls.putAll(getWebUrlsConfiguration());
    }

    /** Returns Shiro URLs paths. */
    private Map<String, String> getWebUrlsConfiguration() {
        final Map<String, String> urls = new HashMap<>();

        // servlets
        urls.put("/sg/upload/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/io/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/download/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/admin**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/help/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/view/source/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/remote_logging/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/camera/**", REQUIRE_AUTHENTICATION);
        urls.put("/sg/swagger.json", REQUIRE_AUTHENTICATION);
        // urls.put("/sg/sync/**", REQUIRE_AUTHENTICATION);

        // jsp
        // urls.put("/", REQUIRE_AUTHENTICATION);
        urls.put(REST_API_URI, REQUIRE_AUTHENTICATION);
        urls.put(LOGIN_URI, REQUIRE_AUTHENTICATION + forceSsl());
        // urls.put(AuthorizationHandler.Routes.login().getUrl(), REQUIRE_AUTHENTICATION + forceSsl());
        urls.put(LOGOUT_URI, REQUIRE_LOGOUT + (forceSsl()));

        return urls;
    }

    private Map<String, String> getWebUsersConfiguration(Ini.Section iniUsers) {
        final Map<String, String> users = new HashMap<>();

        if (!isEmpty(shiroProps.autoLogin) && (Constants.SHIRO_GUEST_USER).equals(shiroProps.autoLogin.split(":")[0]))
            users.put(Constants.SHIRO_GUEST_USER, Constants.SHIRO_GUEST_USER + ", " + Constants.SHIRO_GUEST_USER);
        if (appProps.adminUser.equals(Constants.SHIRO_ADMIN_USER) && !iniUsers.containsKey(Constants.SHIRO_ADMIN_USER))
            users.put(Constants.SHIRO_ADMIN_USER, Constants.SHIRO_ADMIN_PASS + ", " + Constants.SHIRO_ADMIN_ROLE);
        return users;
    }

    //~ Methods ......................................................................................................................................

    private static Ini.Section getOrCreateIniSection(Ini ini, Map<String, Ini.Section> iniSections, String section) {
        return iniSections.computeIfAbsent(section, k -> ini.addSection(section));
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static Map<String, String> getWebMainConfiguration(String authFilterName) {
        final Map<String, String> main = new HashMap<>();

        main.put("authc", authFilterName);
        main.put("authc.loginUrl", LOGIN_URI);
        main.put("authc.successUrl", INDEX_URI);
        main.put("logout.redirectUrl", INDEX_URI);
        main.put("roles.unauthorizedUrl", UNAUTHORIZED_URI);
        final String httpsPort = System.getProperty(Constants.HTTPS_PORT);
        if (isNotEmpty(httpsPort)) main.put("ssl.port", httpsPort);

        return main;
    }

    private static Map<String, String> getWebRolesConfiguration() {
        final Map<String, String> roles = new HashMap<>();

        roles.put(Constants.SHIRO_ADMIN_ROLE, "*");

        return roles;
    }

    //~ Static Fields ................................................................................................................................

    static final String SG_REALM       = "sg";
    static final String USERNAME_REALM = "username";

    public static final String LOCALE = "locale";

    private static final long MILLISECONDS = 1000L;

    private static final String MAIN_SECTION  = "main";
    private static final String USERS_SECTION = "users";
    private static final String ROLES_SECTION = "roles";
    private static final String URLS_SECTION  = "urls";

    @SuppressWarnings("SpellCheckingInspection")
    private static final String REQUIRE_AUTHENTICATION = "authc";
    private static final String REQUIRE_LOGOUT         = "logout";

    public static final String FACEBOOK_ID    = "facebookId";
    public static final String FACEBOOK_TOKEN = "facebookToken";
    public static final String USER_ID        = "userId";
    public static final String SURROGATE_USER = "surrogate";
    public static final String USER_NAME      = "userName";

    //~ Inner Classes ................................................................................................................................

    private class ApplicationSuigenRealm extends AuthenticatingRealm {
        private final HashMap<String, DateTime> bannedTokens = new HashMap<>();

        @Override public Class<ApplicationToken> getAuthenticationTokenClass() {
            return ApplicationToken.class;
        }

        @Nullable @Override protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException
        {
            final ApplicationToken appToken = (ApplicationToken) token;

            final String appTokenCode = appToken.getCode();
            if (appTokenCode == null) return null;

            final DateTime bannedSince = bannedTokens.get(appTokenCode);
            if (bannedSince != null) {
                if (DateTime.current().minutesFrom(bannedSince) <= 5)
                    throw new AuthenticationException(
                        "Token " + appTokenCode + " does not exists and is banned since " + bannedSince + ". Please check the token value");
                else bannedTokens.remove(appTokenCode);
            }
            // Get associated user by Token
            final Application app = Application.findByToken(appTokenCode);

            if (app == null) {
                bannedTokens.put(appTokenCode, DateTime.current());
                return null;
            }

            final String username        = app.getUserId();
            final String applicationName = app.getId();
            if (isEmpty(appToken.getSurrogate())) appToken.setSurrogate(applicationName);
            ((ApplicationToken) token).setUsername(username);  // inject associated username to token

            return new ApplicationAuthenticationInfo(applicationName, appTokenCode, username);
        }
    }

    private class SuigenAuthenticatingRealm extends AuthenticatingRealm {
        private HashedCredentialsMatcher hashedCredentialsMatcher = null;

        @Override public CredentialsMatcher getCredentialsMatcher() {
            if (hashedCredentialsMatcher == null) {
                hashedCredentialsMatcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
                hashedCredentialsMatcher.setStoredCredentialsHexEncoded(false);
                hashedCredentialsMatcher.setHashIterations(HASH_ITERATIONS);
            }
            return hashedCredentialsMatcher;
        }

        @Nullable @Override protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException
        {
            final UsernamePasswordToken upToken  = (UsernamePasswordToken) token;
            final String                username = upToken.getUsername();

            if (username == null) return null;

            User user = UserBase.find(username);

            if (user == null && shiroProps.builtInAuthentication && username.equals(appProps.adminUser)) {
                user = User.create(username);
                user.setPassword(User.hashPassword(appProps.adminPassword, username));
            }

            SimpleAuthenticationInfo info = null;
            if (user != null && !user.isDeprecated()) {
                final String password = user.getPassword();
                info = new SimpleAuthenticationInfo(username, password == null ? "" : password.toCharArray(), USERNAME_REALM);

                final String salt;
                if (UserPasswordHashDelimiterService.getDelimiter().isEmpty()) salt = username;
                else salt = UserPasswordHashDelimiterService.getDelimiter().get().getFirstToken(username);
                info.setCredentialsSalt(ByteSource.Util.bytes(salt));
            }
            return info;
        }

        private static final int HASH_ITERATIONS = 512;
    }  // end class SuigenAuthenticatingRealm

    private static class SuiGenerisAuthenticator extends AbstractAuthenticator {
        private final String               adminUser;
        private final Authenticator        authenticator;
        private final SuiGenerisAuthorizer authorizer;
        private final boolean              createUsers;

        private SuiGenerisAuthenticator(Authenticator authenticator, SuiGenerisAuthorizer authorizer, boolean createUsers, String adminUser) {
            this.authenticator = authenticator;
            this.authorizer    = authorizer;
            this.createUsers   = createUsers;
            this.adminUser     = adminUser;
        }

        @Override public void onLogout(PrincipalCollection principals) {
            if (authenticator instanceof LogoutAware) ((LogoutAware) authenticator).onLogout(principals);
            authorizer.onLogout(principals);
        }

        @Override protected AuthenticationInfo doAuthenticate(AuthenticationToken token)
            throws AuthenticationException
        {
            return invokeInTransaction(() -> runAuthentication(token));
        }  // end method doAuthenticate

        private void assertUniqueSocialEmail(Profile profile) {
            final User user = User.findByEmail(profile.getEmail());
            if (user != null) throw new EmailAlreadyInUseException(profile.getProvider());
        }

        @NotNull private User createUser(@NotNull final AuthenticationToken token, @NotNull final AuthenticationInfo authenticate) {
            if (token instanceof SocialToken) {
                final Profile profile = ((SocialToken) token).getPrincipal();
                assertUniqueSocialEmail(profile);
                final SocialProvider<SocialCredential, Profile> provider = getProvider(profile.getProvider());
                return authorizer.findOrCreateUser(UUID.randomUUID().toString().substring(0, 6), user -> provider.userCreated(profile, user));
            }
            if (token instanceof SuiGenerisToken) {
                final SuiGenerisToken suiGenerisToken = (SuiGenerisToken) token;
                return authorizer.findOrCreateUser(suiGenerisToken.getUsername(), null);
            }
            if (!(token instanceof UsernamePasswordToken || token instanceof ApplicationToken))
                throw new AuthenticationException(ASTNode.Utils.INVALID_TOKEN_TYPE + token);

            final String username = token instanceof UsernamePasswordToken ? ((UsernamePasswordToken) token).getUsername()
                                                                           : ((ApplicationToken) token).getUsername();

            if (authenticate instanceof FacebookAuthenticationInfo)

                return authorizer.findOrCreateUser(username,
                    user -> {
                        final FacebookAuthenticationInfo info = (FacebookAuthenticationInfo) authenticate;
                        user.setName(info.getName());
                        user.setEmail(info.getEmail());

                        final Resource picture = Context.getSingleton(ResourceHandler.class)
                                                        .create()
                                                        .upload(info.getName(), "https://graph.facebook.com" + "/" + info.getId() + "/" + "picture");
                        user.setPicture(picture);

                        final Session session = SecurityUtils.getSubject().getSession();
                        session.setAttribute(FACEBOOK_ID, info.getId());
                        session.setAttribute(FACEBOOK_TOKEN, info.getAccessToken());
                    });
            else return authorizer.findOrCreateUser(username, null);
        }

        @Nullable private User findUser(@NotNull final AuthenticationToken token) {
            if (token instanceof ApplicationToken) {
                final String name = ((ApplicationToken) token).getUsername();
                return User.find(name);
            }
            if (token instanceof UsernamePasswordToken) {
                final String name = ((UsernamePasswordToken) token).getUsername();
                return User.find(name);
            }
            if (token instanceof SocialToken) {
                final Profile profile = ((SocialToken) token).getPrincipal();
                return SocialProfile.find(profile);
            }
            if (token instanceof SuiGenerisToken) {
                final String name = ((SuiGenerisToken) token).getUsername();
                return User.find(name);
            }

            throw new AuthenticationException(ASTNode.Utils.INVALID_TOKEN_TYPE + token);
        }

        @NotNull private AuthenticationInfo runAuthentication(AuthenticationToken token) {
            final AuthenticationInfo authenticate = getAuthenticationInfo(token);

            User user = findUser(token);
            if (user == null) {
                if (createUsers || adminUser.equals(token.getPrincipal())) user = createUser(token, authenticate);
                else throw new AuthenticationException("User does not exists!");
            }

            final PrincipalCollection principals = authenticate.getPrincipals();
            if (principals instanceof SimplePrincipalCollection) {
                final SimplePrincipalCollection ps = (SimplePrincipalCollection) principals;
                ps.add(user.getName(), SG_REALM);
                if (token instanceof SocialToken) ps.add(user.getId(), USERNAME_REALM);
            }

            if (token instanceof ApplicationToken)
                tekgenesis.common.env.security.SecurityUtils.setSurrogate(((ApplicationToken) token).getSurrogate());
            else updateLastLogin(user);

            final Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute(USER_ID, user.getId());
            session.setAttribute(USER_NAME, user.getName());
            session.setAttribute(LOCALE, isNotEmpty(user.getLocale()) ? new Locale(user.getLocale()) : null);
            final OrgUnit defaultOu = user.getDefaultOu();
            session.setAttribute(CURRENT_OU, defaultOu != null ? defaultOu.getName() : SuiGenerisAuthorizingRealm.ROOT_OU);
            return authenticate;
        }

        private void updateLastLogin(User user) {
            runInTransaction(() -> user.setLastLogin(current()).persist());
        }

        private AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) {
            return authenticator.authenticate(token);
        }

        private class EmailAlreadyInUseException extends AuthenticationException {
            private EmailAlreadyInUseException(String provider) {
                super(EMAIL_ALREADY_IN_USE.label(provider));
            }

            private static final long serialVersionUID = 6633604402131072016L;
        }
    }  // end class SuiGenerisAuthenticator

    private class SuiGenerisTokenRealm extends AuthenticatingRealm {
        @Override public boolean supports(AuthenticationToken token) {
            return token instanceof SuiGenerisToken;
        }

        @Override protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
            throws AuthenticationException {}
        @Nullable @Override protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException
        {
            final SuiGenerisToken appToken = (SuiGenerisToken) token;

            final String username = appToken.getUsername();
            if (username == null) throw new AuthenticationException("No user in token");

            return new SuiGenerisTokenAuthenticationInfo(username);
        }
    }
}  // end class ShiroConfiguration
