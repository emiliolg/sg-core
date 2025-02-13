
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.OrgUnit;
import tekgenesis.authorization.Role;
import tekgenesis.authorization.RoleAssignment;
import tekgenesis.authorization.User;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Strings;

import static java.util.concurrent.TimeUnit.MINUTES;

import static tekgenesis.authorization.g.PermissionsTable.PERMISSIONS;
import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.authorization.g.RolePermissionTable.ROLE_PERMISSION;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getBottomUpOrganizations;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Shiro AuthorizingReal implementation over the authorization project.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SuiGenerisAuthorizingRealm extends AuthorizingRealm implements SuiGenerisAuthorizer {

    //~ Instance Fields ..............................................................................................................................

    private final String                adminUser;
    private final boolean               caseInsensitive;
    private final ImmutableList<String> defaultRoles;

    //~ Constructors .................................................................................................................................

    /** Create a Shiro Realm. */
    public SuiGenerisAuthorizingRealm(String adminUser, String defaultRoles, boolean caseInsensitive) {
        this.adminUser       = isEmpty(adminUser) ? "admin" : caseInsensitive ? adminUser.toLowerCase() : adminUser;
        this.defaultRoles    = immutable(Strings.split(defaultRoles, ','));
        this.caseInsensitive = caseInsensitive;
    }

    //~ Methods ......................................................................................................................................

    public User findOrCreateUser(@NotNull final String username, @Nullable Consumer<User> filler) {
        return invokeInTransaction(() -> {
            final String id = caseInsensitive ? username.toLowerCase() : username;
            final User   u  = User.find(id);
            if (u != null) return u;

            final User    user    = User.create(id).setName(id);
            final OrgUnit orgUnit = ensureNotNull(getOu(ROOT_OU), "Not root ou");
            user.setDefaultOu(orgUnit);
            if (id.equals(adminUser)) user.setPassword(User.hashPassword(Constants.SHIRO_ADMIN_PASS, adminUser));
            else if (id.equals(Constants.SHIRO_GUEST_USER)) user.setPassword(User.hashPassword(Constants.SHIRO_GUEST_USER, id));
            user.insert();
            if (filler != null) {
                filler.accept(user);
                user.update();
            }
            if (!id.equals(adminUser)) assignDefaultRoles(user, orgUnit);
            return user;
        });
    }

    /** Return the Admin User. */
    public String getAdminUser() {
        return adminUser;
    }

    @Override public Class<UsernamePasswordToken> getAuthenticationTokenClass() {
        return UsernamePasswordToken.class;
    }

    @Override protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException
    {
        throw new UnsupportedOperationException("Realm should not be used for Authentication");
    }

    @Override protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final Collection<String> principalsList = principals.byType(String.class);

        if (principalsList.isEmpty()) throw new AuthorizationException("Empty principals list!");

        return invokeInTransaction(() -> {
            final OrgUnit orgUnit = getCurrentOu();

            final Set<String>     roles       = new HashSet<>();
            final Set<Permission> permissions = new HashSet<>();
            for (String userPrincipal : principalsList) {
                if (caseInsensitive) userPrincipal = userPrincipal.toLowerCase();
                final User user = User.find(userPrincipal);
                if (user != null) {
                    if (userPrincipal.equals(adminUser)) {
                        roles.add("*");
                        permissions.add(new WildcardPermission("*"));
                    }
                    else if (!user.isDeprecated()) { final List<String> organizations = getBottomUpOrganizations(orgUnit);
                    //J-
                        select(ROLE_ASSIGNMENT.ROLE_ID, ROLE_PERMISSION.APPLICATION, ROLE_PERMISSION.DOMAIN)
                                .andAllOf(PERMISSIONS)
                                .from(ROLE_ASSIGNMENT)
                                .leftOuterJoin(ROLE_PERMISSION, ROLE_ASSIGNMENT.ROLE_ID.eq(ROLE_PERMISSION.ROLE_ID))
                                .leftOuterJoin(PERMISSIONS, ROLE_PERMISSION.ID.eq(PERMISSIONS.ROLE_PERMISSION_ID))
                                .where(ROLE_ASSIGNMENT.USER_ID.eq(user.getId()),
                                        ROLE_ASSIGNMENT.OU_NAME.in(organizations))
                                .cache(5, MINUTES)
                                .forEach(qt -> {
                                    roles.add(qt.get(ROLE_ASSIGNMENT.ROLE_ID));
                                    final String app    = qt.get(ROLE_PERMISSION.APPLICATION);
                                    final String domain = qt.get(ROLE_PERMISSION.DOMAIN);
                                    if (app != null && domain != null && qt.get(PERMISSIONS.ROLE_PERMISSION_ID) != null)
                                        qt.get(PERMISSIONS).ifPresent(p -> permissions.add(p.createPermission(app, domain)));
                                });
                    //J+
                     }
                }
            }

            final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
            info.setRoles(roles);
            info.setObjectPermissions(permissions);

            return info;
        });
    }  // end method doGetAuthorizationInfo

    @Override protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        return info != null && info.getRoles() != null && (info.getRoles().contains(roleIdentifier) || info.getRoles().contains("*"));
    }

    private void assignDefaultRoles(User user, OrgUnit ou) {
        for (final String defaultRole : defaultRoles)
            assignRole(user, getOrCreateRole(defaultRole), ou);
    }

    private void assignRole(User user, Role role, OrgUnit ou) {
        RoleAssignment.findOrCreate(user.getId(), role.getId(), ou.getName()).persist();
    }

    private OrgUnit getCurrentOu() {
        final String currentOu = (String) SecurityUtils.getSubject().getSession().getAttribute(CURRENT_OU);
        return ensureNotNull(getOu(currentOu), "Not current OU");
    }

    private Role getOrCreateRole(String roleId) {
        Role role = Role.find(roleId);
        if (role == null) {
            role = Role.findOrCreate(roleId);
            role.setName(roleId);
            role.persist();
        }
        return role;
    }

    //~ Methods ......................................................................................................................................

    /** Return Root OU. */
    @NotNull public static OrgUnit getRootOu() {
        final OrgUnit orgUnit = OrgUnit.find(ROOT_OU);
        if (orgUnit != null) return orgUnit;

        final OrgUnit r = OrgUnit.findOrCreate(ROOT_OU);
        r.setDescription(ROOT_OU_DESCRIPTION);
        r.persist();
        return r;
    }

    @Nullable private static OrgUnit getOu(String ou) {
        if (!isEmpty(ou) && !ROOT_OU.equals(ou)) return OrgUnit.find(ou);
        else return getRootOu();
    }

    //~ Static Fields ................................................................................................................................

    public static final String  ROOT_OU             = "Root Ou";
    private static final String ROOT_OU_DESCRIPTION = "All";
    public static final String  CURRENT_OU          = "currentOu";
}  // end class SuiGenerisAuthorizingRealm
