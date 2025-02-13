
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

import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cache.test.database.CacheRule;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.security.shiro.ShiroSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class AuthorizationTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final CacheRule db = new CacheRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    private final ExternalResource resource = new ExternalResource() {
            @Override protected void before()
                throws Throwable {}

            @Override protected void after() {
                ShiroSession.clear();
                Context.getContext().unbind(InfinispanCacheManager.class);
            }
        };

    @Rule public TestRule chain = TransactionalRule.into(db).around(new FormRule()).around(resource);

    //~ Methods ......................................................................................................................................

    @Test public void testOUs() {
        final Session session = SecurityUtils.getSession();
        session.logout();

        assertThat(session.isAuthenticated()).isFalse();

        session.authenticate("user1", "password");

        final OrgUnit ou1 = OrgUnit.findOrCreate("OU1");
        ou1.setDescription("OU 1");
        final OrgUnit rootOu = OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU);
        ou1.setParent(rootOu);
        ou1.persist();

        final OrgUnit ou2 = OrgUnit.findOrCreate("OU2");
        ou2.setDescription("OU 2");
        ou2.setParent(rootOu);
        ou2.persist();

        final OrgUnit ou3 = OrgUnit.findOrCreate("OU3");
        ou3.setDescription("OU 3");
        ou3.setParent(ou2);
        ou3.persist();

        final OrgUnit ou4 = OrgUnit.findOrCreate("OU4");
        ou4.setDescription("OU 4");
        ou4.setParent(rootOu);
        ou4.persist();

        final Role role = Role.findOrCreate("Role1");
        role.setName("Role1");
        role.persist();
        final Role role2 = Role.findOrCreate("Role2");
        role2.setName("Role2");
        role2.persist();
        final Role role3 = Role.findOrCreate("Role3");
        role3.setName("Role3");
        role3.persist();

        RoleAssignment.findOrCreate("user1", "Role1", SuiGenerisAuthorizingRealm.ROOT_OU).persist();
        RoleAssignment.findOrCreate("user1", "Role2", ou1.getName()).persist();
        RoleAssignment.findOrCreate("user1", "Role3", ou2.getName()).persist();
        RoleAssignment.findOrCreate("user1", "Role3", ou3.getName()).persist();

        assertThat(AuthorizationUtils.getUserOrgUnits()).containsOnly(rootOu, ou1, ou2, ou3);
    }  // end method testOUs

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class AuthorizationTest
