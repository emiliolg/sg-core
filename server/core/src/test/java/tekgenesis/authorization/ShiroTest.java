
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.authorization.g.RoleAssignmentBase;
import tekgenesis.authorization.g.RoleBase;
import tekgenesis.authorization.g.RolePermissionBase;
import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.security.shiro.ShiroSession;
import tekgenesis.type.permission.PredefinedPermission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.authorization.shiro.AuthorizationUtils.hasPermission;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.type.permission.PredefinedPermission.*;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "OverlyLongMethod" })
public class ShiroTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    private final ExternalResource resource = new ExternalResource() {
            @Override protected void before()
                throws Throwable {}

            @Override protected void after() {
                ShiroSession.clear();
            }
        };

    @Rule public TestRule chain = TransactionalRule.into(db).around(new FormRule()).around(resource);

    //~ Methods ......................................................................................................................................

    @Test public void testShiroAuthentication() {
        final Session session = SecurityUtils.getSession();
        session.logout();

        assertThat(session.isAuthenticated()).isFalse();

        session.authenticate("admin", "password");

        assertThat(session.isAuthenticated()).isTrue();
        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        assertThat(session.getPrincipal().hasAllRoles("User")).isTrue();

        session.logout();

        assertThat(session.isAuthenticated()).isFalse();
        session.authenticate("user1", "password");

        assertThat(session.isAuthenticated()).isTrue();

        assertThat(session.getPrincipal().hasAllRoles("Developer")).isTrue();

        assertThat(userHasPermission(READ)).isFalse();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        addPerm("Developer", "tekgenesis.authorization", "UserForm", READ.name());

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        addPerm("Developer", "tekgenesis.authorization", "*", "*");

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        assertThat(rolesHasPermission(READ)).isTrue();
        assertThat(rolesHasPermission(CREATE)).isTrue();
        assertThat(rolesHasPermission(DELETE)).isTrue();
        assertThat(rolesHasPermission(UPDATE)).isTrue();

        RolePermission.forEach(RolePermission::delete);

        assertThat(userHasPermission(READ)).isFalse();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        assertThat(rolesHasPermission(READ)).isFalse();
        assertThat(rolesHasPermission(CREATE)).isFalse();
        assertThat(rolesHasPermission(DELETE)).isFalse();
        assertThat(rolesHasPermission(UPDATE)).isFalse();

        addPerm("Developer", "*", "*", READ.name());

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        assertThat(rolesHasPermission(READ)).isTrue();
        assertThat(rolesHasPermission(CREATE)).isFalse();
        assertThat(rolesHasPermission(DELETE)).isFalse();
        assertThat(rolesHasPermission(UPDATE)).isFalse();

        addPerm("Developer", "*", "*", "*");

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        assertThat(rolesHasPermission(READ)).isTrue();
        assertThat(rolesHasPermission(CREATE)).isTrue();
        assertThat(rolesHasPermission(DELETE)).isTrue();
        assertThat(rolesHasPermission(UPDATE)).isTrue();
    }  // end method testShiroAuthentication

    @Test public void testShiroAuthenticationWithOU() {
        final Session session = SecurityUtils.getSession();
        session.logout();

        assertThat(session.isAuthenticated()).isFalse();

        session.authenticate("admin", "password");

        assertThat(session.isAuthenticated()).isTrue();
        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        assertThat(session.getPrincipal().hasAllRoles("User"));
        session.logout();

        assertThat(session.isAuthenticated()).isFalse();

        final OrgUnit ou1 = OrgUnit.findOrCreate("OU1");
        ou1.setDescription("OU 1");
        ou1.setParent(OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU));
        ou1.persist();

        final OrgUnit ou2 = OrgUnit.findOrCreate("OU2");
        ou2.setDescription("OU 2");
        ou2.setParent(OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU));
        ou2.persist();

        session.authenticate("admin", "password");
        AuthorizationUtils.setCurrentOrgUnit(ou1);

        assertThat(session.isAuthenticated()).isTrue();
        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        session.authenticate("user1", "password");

        final Role role = Role.findOrCreate("Manager");
        role.setName("Manager");
        role.persist();

        RoleAssignmentBase.findOrCreate("user1", "Manager", "OU1").persist();

        assertThat(session.isAuthenticated()).isTrue();

        assertThat(session.getPrincipal().hasAllRoles("Developer")).isTrue();
        assertThat(session.getPrincipal().hasAllRoles("Manager")).isFalse();

        session.logout();

        session.authenticate("user1", "password");
        assertThat(session.isAuthenticated()).isTrue();

        assertThat(userHasPermission(READ)).isFalse();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        addPerm("Manager", "tekgenesis.authorization", "UserForm", READ.name());

        assertThat(userHasPermission(READ)).isFalse();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        session.logout();
        session.authenticate("user1", "password");
        AuthorizationUtils.setCurrentOrgUnit(ou1);
        assertThat(session.getPrincipal().hasAllRoles("Developer", "Manager")).isTrue();

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        final Role tester = Role.findOrCreate("Tester");
        tester.setName("Tester");
        tester.persist();

        RoleAssignmentBase.findOrCreate("user1", "Tester", "OU2").persist();

        assertThat(session.getPrincipal().hasAllRoles("Tester")).isFalse();

        addPerm("Tester", "tekgenesis.authorization", "*", "*");

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        assertThat(rolesHasPermission(READ)).isFalse();
        assertThat(rolesHasPermission(CREATE)).isFalse();
        assertThat(rolesHasPermission(DELETE)).isFalse();
        assertThat(rolesHasPermission(UPDATE)).isFalse();

        addPerm("Developer", "tekgenesis.authorization", "*", "*");

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        assertThat(rolesHasPermission(READ)).isTrue();
        assertThat(rolesHasPermission(CREATE)).isTrue();
        assertThat(rolesHasPermission(DELETE)).isTrue();
        assertThat(rolesHasPermission(UPDATE)).isTrue();

        RolePermission.forEach(RolePermission::delete);

        assertThat(userHasPermission(READ)).isFalse();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        assertThat(rolesHasPermission(READ)).isFalse();
        assertThat(rolesHasPermission(CREATE)).isFalse();
        assertThat(rolesHasPermission(DELETE)).isFalse();
        assertThat(rolesHasPermission(UPDATE)).isFalse();

        addPerm("Manager", "*", "*", READ.name());

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isFalse();
        assertThat(userHasPermission(DELETE)).isFalse();
        assertThat(userHasPermission(UPDATE)).isFalse();

        assertThat(rolesHasPermission(READ)).isTrue();
        assertThat(rolesHasPermission(CREATE)).isFalse();
        assertThat(rolesHasPermission(DELETE)).isFalse();
        assertThat(rolesHasPermission(UPDATE)).isFalse();

        addPerm("Manager", "*", "*", "*");

        assertThat(userHasPermission(READ)).isTrue();
        assertThat(userHasPermission(CREATE)).isTrue();
        assertThat(userHasPermission(DELETE)).isTrue();
        assertThat(userHasPermission(UPDATE)).isTrue();

        assertThat(rolesHasPermission(READ)).isTrue();
        assertThat(rolesHasPermission(CREATE)).isTrue();
        assertThat(rolesHasPermission(DELETE)).isTrue();
        assertThat(rolesHasPermission(UPDATE)).isTrue();
    }  // end method testShiroAuthenticationWithOU

    private void addPerm(String roleName, String domainName, String appName, String permission) {
        RolePermission rolePerm = RolePermission.findByApplication(roleName, domainName, appName);
        if (rolePerm == null) {
            rolePerm = RolePermissionBase.create();
            final Role role = RoleBase.find(roleName);
            if (role != null) rolePerm.setRole(role);
            rolePerm.setDomain(domainName);
            rolePerm.setApplication(appName);
        }

        rolePerm.getPermissions().add().setPermission(permission);
        rolePerm.persist();
    }

    private boolean rolesHasPermission(PredefinedPermission p) {
        return hasPermission(createQName("tekgenesis.authorization", "RolesForm"), p);
    }

    private boolean userHasPermission(PredefinedPermission p) {
        return hasPermission(createQName("tekgenesis.authorization", "UserForm"), p);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ShiroTest
