
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.util.CacheUtils;
import tekgenesis.admin.util.IndexUtils;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.Principal;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.SHIRO_ADMIN_ROLE;
import static tekgenesis.common.core.Constants.SHIRO_ADMIN_USER;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;

/**
 * User class for Handler: AdminHandler
 */
public class AdminHandler extends AdminHandlerBase {

    //~ Constructors .................................................................................................................................

    AdminHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> clearCache(@Nullable final String fqn) {
        if (!hasAdminRole()) return unauthorized();

        CacheUtils.clearEntityCache(notNull(fqn));

        final String msg = "Clear Cache Task submitted for " + fqn;
        logInfo(msg);
        return ok(msg);
    }

    @NotNull @Override public Result<String> clearUserCache(@NotNull String name) {
        if (!hasAdminRole()) return unauthorized();

        final InfinispanCacheManager cacheManager = Context.getSingleton(InfinispanCacheManager.class);
        cacheManager.invalidateCache(name);

        final String msg = "Clear User Cache Task submitted for " + name;
        logInfo(msg);
        return ok(msg);
    }

    @NotNull @Override public Result<String> rebuildIndex(@Nullable String fqn) {
        if (!hasAdminRole()) return unauthorized();

        IndexUtils.rebuildIndex(notNull(fqn));

        final String msg = "Rebuilding index for " + fqn;
        logInfo(msg);

        return ok(msg);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Returns if the current user has the assigned role Admin. todo this is a temporary solution to
     * avoid a potential security issue. Permanent solution will use a predefined "Admin" role.
     */
    public static boolean hasAdminRole() {
        final Principal principal = SecurityUtils.getSession().getPrincipal();
        return principal.getId().equals(SHIRO_ADMIN_USER) || principal.hasAnyRole(SHIRO_ADMIN_ROLE, AUTHORIZATION_ADMIN_ROLE_NAME);
    }

    /**
     * Returns if the current user has the assigned role Developer. todo this is a temporary
     * solution to avoid a potential security issue. Permanent solution will use a predefined
     * "Developer" role.
     */
    static boolean hasDeveloperRole() {
        return isDevMode() || hasAdminRole() || SecurityUtils.getSession().getPrincipal().hasAnyRole(AUTHORIZATION_DEVELOPER_ROLE_NAME);
    }

    static boolean isDevMode() {
        return getBoolean(SUIGEN_DEVMODE);
    }

    //~ Static Fields ................................................................................................................................

    private static final String AUTHORIZATION_ADMIN_ROLE_NAME     = "Admin";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String AUTHORIZATION_DEVELOPER_ROLE_NAME = "Developer";
}  // end class AdminHandler
