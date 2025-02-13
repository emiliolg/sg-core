
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.inbox;

import java.math.BigDecimal;

import org.assertj.core.api.AbstractIntegerAssert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.authorization.OrgUnit;
import tekgenesis.authorization.RoleAssignment;
import tekgenesis.authorization.g.OrgUnitBase;
import tekgenesis.authorization.g.RoleAssignmentBase;
import tekgenesis.authorization.g.RoleBase;
import tekgenesis.authorization.g.UserBase;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.form.ApplicationContext;
import tekgenesis.form.ApplicationContextImpl;
import tekgenesis.metadata.authorization.Assignee;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.Role;
import tekgenesis.metadata.authorization.User;
import tekgenesis.sales.basic.Category;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductDataWorkItem;
import tekgenesis.sales.basic.g.CategoryBase;
import tekgenesis.sales.basic.g.ProductBase;
import tekgenesis.security.shiro.ShiroConfiguration;
import tekgenesis.security.shiro.ShiroSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.env.context.Context.bind;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.ProductDataWorkItemTable.PRODUCT_DATA_WORK_ITEM;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class CasesTests {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DbRule db = new DbRule(DbRule.SG, DbRule.BASIC, DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(dbName);
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.builtInAuthentication = true;
                env.put(shiroProps);
                new ShiroConfiguration(true, env);
                // Register Session
                bind(Session.class, ShiroSession.class);
                // Register ApplicationContext
                bind(ApplicationContext.class, ApplicationContextImpl.class);
                SecurityUtils.getSession().authenticate("admin", "password");
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void organizationalUnitsHierarchy() {
        runInTransaction(() -> {
            final OrgUnit root = OrgUnit.getRoot();

            final OrgUnit ba = organization("BsAs", root);

            final OrgUnit pilar = organization("Pilar", ba);
            final OrgUnit lujan = organization("Luján", ba);

            final User peter = user("peter", "password");
            final User luke  = user("luke", "password");

            final Role auditor   = role("Auditor");
            final Role developer = role("Developer");

            assign(peter, auditor, ba);
            assign(peter, developer, pilar);
            assign(luke, developer, lujan);

            product("2", "Bravia 40");

            final ImmutableList<ProductDataWorkItem> items = selectFrom(PRODUCT_DATA_WORK_ITEM).list();

            assertThat(items.size()).isEqualTo(1);

            final ItemAssert itemAssert = new ItemAssert(items.get(0));

            // Set luke -> Check peter with root organization;
            itemAssert.with(luke).assertNothingOnInbox(root, peter);

            // Set developer and lujan -> Check peter with ba organization;
            itemAssert.with(developer, lujan).assertNothingOnInbox(ba, peter);

            // Set developer and lujan -> Check peter with pilar organization;
            itemAssert.with(developer, lujan).assertNothingOnInbox(pilar, peter);

            // Set auditor and lujan -> Check peter with ba organization;
            itemAssert.with(auditor, lujan).assertItemOnInbox(ba, peter);

            // Set auditor and pilar -> Check peter with ba organization;
            itemAssert.with(auditor, lujan).assertItemOnInbox(ba, peter);

            // Set auditor and ba -> Check peter with ba organization;
            itemAssert.with(auditor, lujan).assertItemOnInbox(ba, peter);

            // Set auditor and ba -> Check peter with pilar organization;
            itemAssert.with(auditor, lujan).assertItemOnInbox(pilar, peter);

            // Set developer -> Check peter with pilar organization;
            itemAssert.with(developer).assertItemOnInbox(pilar, peter);

            // Set developer -> Check luke with lujan organization;
            itemAssert.with(developer).assertItemOnInbox(lujan, luke);

            // Set developer -> Check peter with ba organization;
            itemAssert.with(developer).assertNothingOnInbox(ba, peter);
        });
    }  // end method organizationalUnitsHierarchy

    @SuppressWarnings("SpellCheckingInspection")
    @Test public void workItems() {
        runInTransaction(() -> {
            final OrgUnit root = OrgUnit.getRoot();

            final User peter = user("peter", "password");
            final User john  = user("john", "password");

            final Role developer = role("Developer");
            final Role manager   = role("Manager");
            final Role tester    = role("Tester");
            final Role auditor   = role("Auditor");

            final OrganizationalUnit fuego = organization("Fuego", root);
            final OrganizationalUnit agua  = organization("Agua", root);

            assign(peter, developer, agua);
            assign(peter, developer, fuego);
            assign(peter, manager, fuego);
            assign(peter, auditor, root);

            product("1", "Bravia 40");

            final ImmutableList<ProductDataWorkItem> items = ProductDataWorkItem.list().list();

            assertThat(items.size()).isEqualTo(1);

            final ItemAssert itemAssert = new ItemAssert(items.get(0));

            // Set john -> Check peter with root organization;
            itemAssert.with(john).assertNothingOnInbox(root, peter);

            // Set peter -> Check peter with root organization;
            itemAssert.with(peter).assertItemOnInbox(root, peter);

            // Set developer -> Check peter with root organization;
            itemAssert.with(developer).assertNothingOnInbox(root, peter);

            // Set manager -> Check peter with root organization;
            itemAssert.with(manager).assertNothingOnInbox(root, peter);

            // Set tester -> Check peter with root organization;
            itemAssert.with(tester).assertNothingOnInbox(root, peter);

            // Set auditor -> Check peter with root organization;
            itemAssert.with(auditor).assertItemOnInbox(root, peter);

            // Set peter -> Check peter with fuego organization;
            itemAssert.with(peter).assertItemOnInbox(fuego, peter);

            // Set developer -> Check peter with fuego organization;
            itemAssert.with(developer).assertItemOnInbox(fuego, peter);

            // Set manager -> Check peter with fuego organization;
            itemAssert.with(manager).assertItemOnInbox(fuego, peter);

            // Set tester -> Check peter with fuego organization;
            itemAssert.with(tester).assertNothingOnInbox(fuego, peter);

            // Set auditor -> Check peter with fuego organization;
            itemAssert.with(auditor).assertItemOnInbox(fuego, peter);

            // Set peter -> Check peter with agua organization;
            itemAssert.with(peter).assertItemOnInbox(agua, peter);

            // Set developer -> Check peter with agua organization;
            itemAssert.with(developer).assertItemOnInbox(agua, peter);

            // Set manager -> Check peter with agua organization;
            itemAssert.with(manager).assertNothingOnInbox(agua, peter);

            // Set tester -> Check peter with agua organization;
            itemAssert.with(tester).assertNothingOnInbox(agua, peter);

            // Set auditor -> Check peter with agua organization;
            itemAssert.with(auditor).assertItemOnInbox(agua, peter);

            // Set manager and fuego -> Check peter with fuego organization;
            itemAssert.with(manager, fuego).assertItemOnInbox(fuego, peter);

            // Set manager and fuego -> Check peter with agua organization;
            itemAssert.with(manager, fuego).assertNothingOnInbox(agua, peter);

            // Set auditor and fuego -> Check peter with fuego organization;
            itemAssert.with(auditor, fuego).assertItemOnInbox(fuego, peter);

            // Set auditor and fuego -> Check peter with root organization;
            itemAssert.with(auditor, fuego).assertItemOnInbox(root, peter);

            // Set auditor and root -> Check peter with fuego organization;
            itemAssert.with(auditor, root).assertItemOnInbox(fuego, peter);
        });
    }  // end method workItems

    private void assign(User u, Role r, OrganizationalUnit ou) {
        final RoleAssignment a = RoleAssignmentBase.create(u.getId(), r.getId(), ou.getName());
        a.insert();
    }

    private Category category() {
        final Category c = CategoryBase.create(1);
        c.setName("Tv");
        c.setDescr("Tv description");
        c.persist();
        return c;
    }

    private OrgUnit organization(String name, OrgUnit root) {
        final OrgUnit ou = OrgUnitBase.create(name);
        ou.setDescription(name);
        ou.setParent(root);
        ou.persist();
        return ou;
    }

    private Product product(String id, String name) {
        final Product p = ProductBase.create(id);
        p.setModel(name);
        p.setDescription(name);
        p.setCategory(category());
        p.setPrice(new BigDecimal(499));
        p.persist();
        return p;
    }

    private Role role(String id) {
        final tekgenesis.authorization.Role r = RoleBase.create(id);
        r.setName(id);
        r.persist();
        return r;
    }

    private User user(String id, String password) {
        final tekgenesis.authorization.User u = UserBase.create(id);
        u.setPassword(tekgenesis.authorization.User.hashPassword(password, id));
        u.setName("peter");
        u.insert();
        return u;
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Inner Classes ................................................................................................................................

    private static class ItemAssert {
        @NotNull private final ProductDataWorkItem item;

        private ItemAssert(@NotNull ProductDataWorkItem item) {
            this.item = item;
        }

        private void assertItemOnInbox(OrganizationalUnit ou, User u) {
            itemsOnInbox(ou, u).isEqualTo(1);
        }

        private void assertNothingOnInbox(OrganizationalUnit ou, User u) {
            itemsOnInbox(ou, u).isZero();
        }

        private AbstractIntegerAssert<?> itemsOnInbox(OrganizationalUnit ou, User u) {
            return assertThat(WorkItems.getWorkItems(u, ou).size());
        }

        private ItemAssert persist(@NotNull final Assignee assignee, @Nullable final String ouName) {
            item.setAssignee(assignee);
            item.setOuName(ouName);
            item.persist();
            return this;
        }

        private ItemAssert with(@NotNull final Assignee assignee) {
            return persist(assignee, null);
        }

        private ItemAssert with(@NotNull final Assignee assignee, @NotNull final OrganizationalUnit ou) {
            return persist(assignee, ou.getName());
        }
    }
}  // end class CasesTests
