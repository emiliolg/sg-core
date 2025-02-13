
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.UnknownSessionException;
import org.infinispan.Cache;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.OrgUnit;
import tekgenesis.authorization.RoleAssignment;
import tekgenesis.authorization.User;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.PermissionException;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.FormUtils;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.permission.Permission;

import static java.lang.Boolean.getBoolean;
import static java.lang.Integer.getInteger;

import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm.CURRENT_OU;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.env.security.SecurityUtils.getSession;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Utils to work with Shiro objects.
 */
public class AuthorizationUtils {

    //~ Constructors .................................................................................................................................

    private AuthorizationUtils() {}

    //~ Methods ......................................................................................................................................

    /** Check permission for current user and selected form. */
    public static void checkPermission(@NotNull Form form, @NotNull Permission permission)
        throws PermissionException
    {
        if (!form.isUnrestricted() && !hasPermission(form.getKey(), permission))
            throw new PermissionException(getSession().getPrincipal().getName(), form.getKey().toString(), permission.getName());
    }

    /** Check permission for current user and selected form. */
    public static void checkPermission(@NotNull QName name, @NotNull Permission permission)
        throws PermissionException
    {
        final Form form = FormUtils.findForm(name.getFullName()).getOrFail(String.format("Form '%s' not found", name));
        checkPermission(form, permission);
    }

    /** Clear authorization cache. */
    public static void clearAuthorizationCache() {
        Context.getSingleton(InfinispanCacheManager.class).invalidateCache(SUIGENERIS_AUTHORIZATION_CACHE);
    }

    /** Clear authorization cache. */
    public static void clearAuthorizationCache(String userId) {
        final Cache<Object, Object> map      = Context.getSingleton(InfinispanCacheManager.class).getCache(SUIGENERIS_AUTHORIZATION_CACHE);
        boolean                     notFound = true;
        for (final Object o : map.keySet()) {
            if (userId.equals(o.toString())) {
                map.remove(o);
                notFound = false;
                break;
            }
        }
        if (notFound) map.clear();
    }

    /** Clear user cache. */
    public static void clearUserCache(String id) {
        Context.getSingleton(InfinispanCacheManager.class).getCache(SUIGENERIS_ORG_UNITS_CACHE).remove(id);
    }

    /** Return true if the current OU is set. */
    public static boolean hasCurrentOrgUnit() {
        return SecurityUtils.getSubject().getSession().getAttribute(CURRENT_OU) != null;
    }

    /** Check permission for current user and selected form. */
    public static boolean hasPermission(@NotNull QName name, @NotNull Permission permission) {
        try {
            return getSession().getPrincipal().hasPermission(name.getName() + ":" + name.getQualification() + ":" + permission.getName());
        }
        catch (final UnknownSessionException e) {
            logger.debug(e);
            return false;
        }
    }

    /** Returns context's current Org Unit. */
    @NotNull public static OrgUnit getCurrentOrgUnit() {
        final String name = getSession().getPrincipal().getOrgUnit();
        if (isEmpty(name)) throw new IllegalStateException("Missing Organizational Unit in session.");
        return ensureNotNull(invokeInTransaction(() -> OrgUnit.find(name)), String.format("Organizational Unit %s not found.", name));
    }

    /** Changes context's current Org Unit to the given one. */
    public static void setCurrentOrgUnit(OrgUnit orgUnit) {
        SecurityUtils.getSubject().getSession().setAttribute(CURRENT_OU, orgUnit.getName());
        if (Context.getContext().hasBinding(InfinispanCacheManager.class))
        // clearAuthorizationCache(getUserId());
        clearAuthorizationCache();
    }

    /** Returns current logged User. */
    public static User getCurrentUser() {
        final String userId = getSession().getPrincipal().getId();
        return ensureNotNull(invokeInTransaction(() -> User.find(userId)), "User not logged in.");
    }

    /** Returns true if session is authenticated. */
    public static boolean isAuthenticated() {
        return tekgenesis.common.env.security.SecurityUtils.getSession().isAuthenticated();
    }

    /** Returns whether the user has permission over form. */
    public static boolean isPermitted(Form form, Permission permission) {
        return form.isUnrestricted() || hasPermission(createQName(form.getDomain(), form.getName()), permission);
    }

    /** Returns the model repository. */
    public static ModelRepository getModelRepository() {
        return Context.getSingleton(ModelRepository.class);
    }

    /**
     * Given a default, first we check if there is a global one set. If there is one, it uses that
     * one. If not overrides it to 1 (lowest-score) if Sui Generis is in Dev Mode or to the
     * specified one.
     */
    public static int getPasswordStrengthScore(final int defaultValue) {
        final Integer userDefinedScore = getInteger(PASSWORD_STRENGTH_SCORE);

        if (userDefinedScore != null) {
            if (userDefinedScore < 1 || userDefinedScore > 5) {
                logger.warning("Invalid configured password strength, will use the most secure one.");
                return 5;
            }
            return userDefinedScore;
        }

        return getBoolean(SUIGEN_DEVMODE) ? 1 : defaultValue;
    }

    /** Returns the permissions that the current subject has over a form. */
    public static Set<Permission> getPermissions(@NotNull Form form) {
        final Set<Permission> result    = new HashSet<>();
        final String          firstPart = form.getName() + ":" + form.getDomain() + ":";

        for (final Permission permission : form.getPermissions()) {
            if (form.isUnrestricted() || SecurityUtils.getSubject().isPermitted(firstPart + permission)) result.add(permission);
        }

        return result;
    }

    /** Get all role assignments for a given user, organization and its ancestors. */
    @NotNull public static Seq<RoleAssignment> getRoleAssignments(@NotNull final String user, @NotNull final OrganizationalUnit organization) {
        final List<String> organizations = getBottomUpOrganizations(organization);
        return invokeInTransaction(() ->
                selectFrom(ROLE_ASSIGNMENT).where(ROLE_ASSIGNMENT.USER_ID.eq(user), ROLE_ASSIGNMENT.OU_NAME.in(organizations)).list());
    }

    /** Returns the Org Units where the current User has Roles assigned to. */
    public static Set<OrgUnit> getUserOrgUnits() {
        return invokeInTransaction(() -> {
            final Set<OrgUnit> orgUnits = new HashSet<>();
            for (final User user : option(User.find(getSession().getPrincipal().getId()))) {
                final ConcurrentMap<Object, Object> orgUnitsMaps = Context.getSingleton(InfinispanCacheManager.class)
                                                                          .getCache(SUIGENERIS_ORG_UNITS_CACHE);
                final Object                        cached       = orgUnitsMaps.get(user.getId());
                if (cached != null) return cast(cached);

                selectFrom(ROLE_ASSIGNMENT)                       //
                .where(ROLE_ASSIGNMENT.USER_ID.eq(user.getId()))  //
                                               .forEach(ra -> orgUnits.add(ra.getOu()));

                orgUnitsMaps.put(user.getId(), orgUnits);
            }
            return orgUnits;
        });
    }

    /** Get a list of current organization and its ancestors. */
    @NotNull static List<String> getBottomUpOrganizations(@NotNull OrganizationalUnit organization) {
        final List<String> organizations = new ArrayList<>(5);
        OrganizationalUnit current       = organization;
        do {
            organizations.add(current.getName());
            current = current.getParent();
        }
        while (current != null);

        return organizations;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(AuthorizationUtils.class);

    @SuppressWarnings("WeakerAccess")
    public static final String PASSWORD_STRENGTH_SCORE = "suigeneris.passwordStrengthScore";

    @NonNls private static final String SUIGENERIS_ORG_UNITS_CACHE     = "suigeneris.orgUnitsCache";
    public static final String          SUIGENERIS_AUTHORIZATION_CACHE = "suigeneris.authorizationCache";
}  // end class AuthorizationUtils
