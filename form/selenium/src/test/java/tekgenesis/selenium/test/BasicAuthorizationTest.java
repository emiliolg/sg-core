
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium.test;

import java.util.Random;

import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumRule;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.selenium.test.AuthorizationTest.createProperty;
import static tekgenesis.selenium.test.AuthorizationTest.createUser;

@Category(AppTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "OverlyLongMethod" })
public class BasicAuthorizationTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public SeleniumBrowser browserName = SeleniumBrowser.CHROME;

    @Rule public SeleniumDriverRule driver = new SeleniumDriverRule() {
            @Override public DriverService getService() {
                return service.getService();
            }
            @Override protected SeleniumBrowser getBrowser() {
                return browserName;
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void test01PropertyTest() {
        createProperty(driver, "User", "id1");
        createProperty(driver, "Company", "id2");
        createProperty(driver, "Organizational Unit", "id3");
        createProperty(driver, "Domain", "id4");
    }

    @Test public void test02OrgUnitTest() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.authorization.OrgUnitForm");

        final String id = "OrgId" + new Random(System.currentTimeMillis()).nextInt(100);
        form.textField("name").setValue(id);
        form.textField("description").setValue("Scale");
        form.suggestBox("parent").setValue("All");

        final SeleniumDriverRule.TableDom table = form.table("props");
        final SeleniumDriverRule.RowDom   row   = table.row(0);
        row.textArea("value").setValue("scale1");

        form.saveAndStay();

        driver.formDom("tekgenesis.authorization.OrgUnitForm", id);
    }

    @Test public void test03RolesTest() {
        final SeleniumDriverRule.FormDom  form  = driver.formDom("tekgenesis.authorization.RolesForm");
        final SeleniumDriverRule.TableDom table = form.table("roles");

        form.button("addRowBottom").click();

        final SeleniumDriverRule.RowDom row = table.row(table.rowCount() - 1);
        row.textField("id").setValue("MagicId");
        row.textField("name").setValue("Administrator");

        form.button("addRowBottom").click();
    }

    @Test public void test04UserFormTest() {
        final String basicId = "imanzano";
        createUser(driver, basicId, "Ignacio Manzano", listOf("x"), "English");

        final SeleniumDriverRule.FormDom load = driver.formDom("tekgenesis.authorization.UserForm", basicId);

        assertThat(load.textField("id").getText()).isEqualTo(basicId);
        assertThat(load.textField("name").getText()).isEqualTo("Ignacio Manzano");

        load.delete();

        final SeleniumDriverRule.FormDom reload = driver.formDom("tekgenesis.authorization.UserForm", basicId);

        assertThat(reload.feedback()).contains("User with id imanzano not found.");

        driver.getKeyboard().pressKey(Keys.ESCAPE);
    }

    @Test public void test05UserIdWithBlanks() {
        final String complexId = "nacho \\ $ma nz";
        createUser(driver, complexId, "Nachomanz", listOf("x"), "English");

        final SeleniumDriverRule.FormDom form2 = driver.formDom("tekgenesis.authorization.UserForm", complexId);
        assertThat(form2.textField("id").getText()).isEqualTo(complexId);
        assertThat(form2.textField("name").getText()).isEqualTo("Nachomanz");

        form2.delete();
    }

    @Test public void test06RoleAssignmentTest() {
        createUser(driver, "DemoUser", "Demo User", listOf("x"), "English");
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.authorization.RoleAssignmentForm");

        form.suggestBox("user").setValue("Demo User (DemoUser)");
        form.suggestBox("role").setValue("Administrator");
        form.saveAndStay();

        driver.formDom("tekgenesis.authorization.RoleAssignmentForm", "DemoUser:MagicId:Root Ou");

        final SeleniumDriverRule.FormDom  usersRolesForm = driver.formDom("tekgenesis.authorization.UserForm", "DemoUser");
        final SeleniumDriverRule.TableDom table          = usersRolesForm.table("roles");
        assertThat(table.rowCount()).isEqualTo(1);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        final Object[] b1 = { SeleniumBrowser.CHROME };
        final Object[] b2 = { SeleniumBrowser.FIREFOX };
        return ImmutableList.<Object[]>of(b1, b2);
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule() {
            @Override protected void resetEnv() {
                super.resetEnv();
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.builtInAuthentication = true;
                env.put("", shiroProps);
            }
        };
}  // end class BasicAuthorizationTest
