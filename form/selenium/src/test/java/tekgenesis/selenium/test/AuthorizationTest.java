
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium.test;

import java.util.List;

import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumDriverRule.FormDom;
import tekgenesis.selenium.SeleniumDriverRule.RowDom;
import tekgenesis.selenium.SeleniumDriverRule.TableDom;
import tekgenesis.selenium.SeleniumRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;

@Category(AppTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "OverlyLongMethod" })
public class AuthorizationTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public SeleniumBrowser browserName = SeleniumBrowser.CHROME;

    @Rule public SeleniumDriverRule driver = new SeleniumDriverRule() {
            @Override public DriverService getService() {
                return service.getService();
            }
            @Override protected SeleniumBrowser getBrowser() {
                return browserName;
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void test01CreateRole() {
        final FormDom  roles = driver.formDom("tekgenesis.authorization.RolesForm");
        final TableDom table = roles.table("roles");
        driver.delay();
        // Adding PermissionsTester role to use it in PermissionsForm;
        roles.button("addRowBottom").click();
        driver.delay();
        final RowDom row = table.row(table.rowCount() - 1);
        row.textField("id").setValue("PermissionsTester");
        row.textField("name").setValue("Permissions Tester");

        roles.button("addRowBottom").click();
    }

    @Test public void test02SetPermissions() {
        final FormDom permissions = driver.formDom("tekgenesis.authorization.PermissionsForm");
        permissions.suggestBox("role").setValue("Permissions Tester");
        driver.delay();  // wait until domains are loaded
        driver.delay();
        permissions.comboBox("domain").setValue("tekgenesis.showcase");

        final TableDom applications = permissions.table("applications");

        // First row with CRUD, then click Update.
        applications.row(0).tagComboBox("permissions").setValues(CRUD);
        applications.row(0).button("update").click();

        // Second row with All, then click Update.
        applications.row(1).checkBox("all").setSelected(true);
        applications.row(1).button("update").click();

        // Third row with Create & Read, then click Cancel.
        applications.row(2).tagComboBox("permissions").setValues(listOf("create", "read"));
        applications.row(2).button(Constants.CANCEL).click();
    }

    @Test public void test03CheckPermissions() {
        final FormDom permissions = driver.formDom("tekgenesis.authorization.PermissionsForm");
        permissions.suggestBox("role").setValue("Permissions Tester");
        permissions.comboBox("domain").setValue("tekgenesis.showcase");

        final TableDom applications = permissions.table("applications");

        // Checking that everything is like was before
        assertThat(applications.row(0).tagComboBox("permissions").getList().toList()).isEqualTo(CRUD);
        assertThat(applications.row(1).checkBox("all").isSelected()).isTrue();
    }

    @Test public void test04SetCrudToAllAndCancel() {
        final FormDom permissions = driver.formDom("tekgenesis.authorization.PermissionsForm");
        permissions.suggestBox("role").setValue("Permissions Tester");
        permissions.comboBox("domain").setValue("tekgenesis.showcase");

        final TableDom applications = permissions.table("applications");

        // Adding CRUD to all.
        permissions.tagComboBox("massPermissions").setValues(CRUD);
        permissions.checkBox("toAll").setSelected(true);
        permissions.button("addToSelected").click();

        // Mass permissions chooser should be reset after 'Add to seleced' click.
        assertThat(permissions.tagComboBox("massPermissions").getList().toList().isEmpty()).isTrue();
        assertThat(permissions.checkBox("toAll").isSelected()).isFalse();

        // Cancel last massive CRUD addition.
        permissions.button("cancelChanges").click();

        // Everything should be as it was before.
        assertThat(applications.row(0).tagComboBox("permissions").getList().toList()).isEqualTo(CRUD);
        assertThat(applications.row(1).checkBox("all").isSelected()).isTrue();
        assertThat(applications.row(2).checkBox("all").isSelected()).isFalse();
        assertThat(applications.row(2).tagComboBox("permissions").getList().toList().isEmpty()).isTrue();
    }

    @Test public void test05SetCrudToAllAndApply() {
        final FormDom permissions = driver.formDom("tekgenesis.authorization.PermissionsForm");
        permissions.suggestBox("role").setValue("Permissions Tester");
        permissions.comboBox("domain").setValue("tekgenesis.showcase");

        final TableDom applications = permissions.table("applications");

        // Finally, re-adding CRUD to all, but this time clicking 'Apply changes'.
        permissions.tagComboBox("massPermissions").setValues(CRUD);
        permissions.checkBox("toAll").setSelected(true);
        permissions.button("addToSelected").click();
        permissions.button("applyChanges").click();

        driver.delay();

        // Everything should be as it was before.
        assertThat(applications.row(0).tagComboBox("permissions").getList().toList()).isEqualTo(CRUD);
        assertThat(applications.row(1).checkBox("all").isSelected()).isTrue();

        // And all the other rows should have CRUD set.
        // for (int i = 2; i <= applications.rowCount() - 1; i++)
        // assertThat(applications.row(i).tagComboBox("permissions").getList()).isEqualTo(CRUD);

        // Instead of checking all, that takes time & produce random results, checking only third and last one rows.
        assertThat(applications.row(2).tagComboBox("permissions").getList().toList()).isEqualTo(CRUD);
        assertThat(applications.row(applications.rowCount() - 1).tagComboBox("permissions").getList().toList()).isEqualTo(CRUD);
    }

    @Test public void test06DeprecationUserAndRole() {
        // Create some user.
        createUser(driver, "lucas", "Lucas Luppani", Colls.emptyList(), "English");

        // Create a role.
        final FormDom  roles = driver.formDom("tekgenesis.authorization.RolesForm");
        final TableDom table = roles.table("roles");
        roles.button("addRowBottom").click();

        final RowDom row = table.row(table.rowCount() - 1);
        row.textField("id").setValue("showcase");
        row.textField("name").setValue("Showcase");

        // hack to deselect the row;
        roles.button("addRowBottom").click();

        // Assign 'showcase' role to user 'lucas'.
        final FormDom roleAssignment = row.button("assign").clickAndExpect("tekgenesis.authorization.RoleAssignmentForm");
        roleAssignment.suggestBox("user").setValue("Lucas Luppani (lucas)");
        roleAssignment.saveAndExpect(driver.getBaseUrl() + "/#form/tekgenesis.authorization.RolesForm");
    }

    @Test public void test07PermissionsForDeprecation() {
        // Add all permissions to showcase domain (sans handle_deprecated).
        final FormDom permissions = driver.formDom("tekgenesis.authorization.PermissionsForm");
        permissions.suggestBox("role").setValue("Showcase");
        permissions.comboBox("domain").setValue("tekgenesis.showcase");
        permissions.tagComboBox("massPermissions").setValues(listOf("create", "read", "update", "delete"));
        permissions.checkBox("toAll").setSelected(true);
        permissions.button("addToSelected").click();
        permissions.button("applyChanges").click();
    }

    @SuppressWarnings("ReuseOfLocalVariable")
    @Test public void test08Deprecation() {
        // Create a simple entity instance.
        final FormDom simpleEntityForm = driver.formDom("tekgenesis.showcase.SimpleEntityForm");
        simpleEntityForm.textField("name").setValue("simple1");
        simpleEntityForm.textField("description").setValue("Simple1");
        simpleEntityForm.saveAndStay();

        // Create another deprecable entity instance;
        FormDom      anotherDeprecable = driver.formDom("tekgenesis.showcase.AnotherDeprecableEntityForm");
        final String instanceId        = "another1";
        anotherDeprecable.textField("name").setValue(instanceId);
        anotherDeprecable.saveAndStay();

        // Load it so we can deprecate it.
        anotherDeprecable = driver.formDom("tekgenesis.showcase.AnotherDeprecableEntityForm");
        anotherDeprecable.search(instanceId);

        anotherDeprecable.button(MMCodeGenConstants.DEPRECATE_METHOD).click();
        assertThat(anotherDeprecable.button(MMCodeGenConstants.DEPRECATE_METHOD).getText()).isEqualTo("Activate");
        anotherDeprecable.cancel();

        anotherDeprecable = driver.formDom("tekgenesis.showcase.AnotherDeprecableEntityForm");

        try {
            anotherDeprecable.search(instanceId);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertThat(e).hasMessage("Trying to set 'another1'. But Suggest element was not found");
        }

        driver.logout();
        driver.login();

        anotherDeprecable = driver.formDom("tekgenesis.showcase.AnotherDeprecableEntityForm");
        anotherDeprecable.search(instanceId, true);

        driver.logout();

        // A user without permission cannot see it.
        driver.login("lucas", "correcthorsebatterystaple");

        anotherDeprecable = driver.formDom("tekgenesis.showcase.AnotherDeprecableEntityForm");
        try {
            anotherDeprecable.search(instanceId);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertThat(e).hasMessage("Trying to set 'another1'. But Suggest element was not found");
        }

        driver.logout();
        driver.login();

        // Trying to reference it, it will fail.
        FormDom deprecable = driver.formDom("tekgenesis.showcase.DeprecableEntityForm");
        try {
            deprecable.suggestBox("anotherDeprecableEntity").setValue(instanceId);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertThat(e).hasMessage("Trying to set 'another1'. But Suggest element was not found");
        }

        // Undeprecating it.
        anotherDeprecable = driver.formDom("tekgenesis.showcase.AnotherDeprecableEntityForm");
        anotherDeprecable.search(instanceId, true);
        anotherDeprecable.button(MMCodeGenConstants.DEPRECATE_METHOD).click();
        assertThat(anotherDeprecable.button(MMCodeGenConstants.DEPRECATE_METHOD).getText()).isEqualTo("Deactivate");
        anotherDeprecable.saveAndStay();

        // Now it will work.
        deprecable = driver.formDom("tekgenesis.showcase.DeprecableEntityForm");
        deprecable.textField("name").setValue("test1");
        deprecable.suggestBox("simpleEntity").setValue("simple1 Simple1");
        deprecable.suggestBox("anotherDeprecableEntity").setValue(instanceId);
        deprecable.saveAndStay();
    }  // end method test08Deprecation

    @Test public void test09UserProfile() {
        final FormDom form = driver.formDom("tekgenesis.authorization.UserProfileForm");

        assertThat(form.message("id").getText()).isEqualTo("admin");
        assertThat(form.textField("name").getText()).isEqualTo("admin");
        assertThat(form.message("defaultOu").getText()).isEqualTo("All");
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { SeleniumBrowser.CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    static void createProperty(SeleniumDriverRule driver, String scope, String id) {
        final FormDom form = driver.formDom("tekgenesis.authorization.PropertyForm");

        form.textField("id").setValue(id);
        final String name = "Scale";
        form.textField("name").setValue(name);
        form.comboBox("type").setValue("String");
        form.checkBox("required").setSelected(true);
        form.comboBox("scope").setValue(scope);

        form.saveAndStay();

        driver.formDom("tekgenesis.authorization.PropertyForm", id + ":" + name + ":STRING");
    }

    static void createUser(SeleniumDriverRule driver, String id, String name, List<String> props, String locale) {
        final FormDom userF = driver.formDom("tekgenesis.authorization.UserForm");
        userF.textField("id").setValue(id);
        userF.textField("name").setValue(name);
        userF.textField("password").setValue("correcthorsebatterystaple");
        userF.textField("confirmPassword").setValue("correcthorsebatterystaple");
        userF.comboBox("locale").setValue(locale);

        final TableDom table = userF.table("props");

        assertThat(table.rowCount()).isEqualTo(props.size());

        if (table.rowCount() > 0) {
            for (int i = 0; i < table.rowCount(); i++)
                table.row(i).textArea("value").setValue(props.get(i));
        }

        userF.saveAndStay();
    }

    //~ Static Fields ................................................................................................................................

    private static final Iterable<String> CRUD = listOf("read", "create", "update", "delete");

    @ClassRule public static SeleniumRule service = new SeleniumRule() {
            @Override protected void resetEnv() {
                super.resetEnv();
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.builtInAuthentication = true;
                env.put("", shiroProps);
            }
        };
}  // end class AuthorizationTest
