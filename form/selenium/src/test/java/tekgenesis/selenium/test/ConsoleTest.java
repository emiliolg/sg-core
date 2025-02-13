
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium.test;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.app.properties.JmxServiceProps;
import tekgenesis.app.service.JmxService;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.properties.LoggingProps;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumRule;
import tekgenesis.task.jmx.JmxConstants;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.core.Strings.deCapitalizeFirst;

/**
 * Cluster Console Test.
 */
@Category(AppTests.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class ConsoleTest {

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

    @Test public void cacheForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.CacheForm");

        final SeleniumDriverRule.TableDom table = form.table("cacheTab");
        assertThat(table.rowCount()).isGreaterThanOrEqualTo(4);

        form.radioGroup("searchCacheType").setText(1);
        driver.delay();
        assertThat(table.rowCount()).isGreaterThan(1);
    }

    @Test public void configurationForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.ConfigurationForm");

        form.selectTab(CONFIGURATION_TAB, 2);
        driver.delay();
        assertThat(form.textField(JmxConstants.LOG_FILENAME).getText()).isEqualTo(LoggingProps.LOG_FILE_NAME);
        assertThat(form.textField(JmxConstants.MAX_FILESIZE).getText()).isEqualTo("20MB");
        assertThat(form.textField(JmxConstants.MAX_DAYS).getText()).isEqualTo("7");
        assertThat(form.checkBox(JmxConstants.CONSOLE_OUTPUT).isSelected()).isTrue();
        assertThat(form.checkBox(JmxConstants.XML_OUTPUT).isSelected()).isFalse();
        assertThat(form.checkBox(JmxConstants.FILE_OUTPUT).isSelected()).isTrue();
        assertThat(form.checkBox(JmxConstants.DEBUG_ALL).isSelected()).isFalse();
        assertThat(form.checkBox(JmxConstants.DEBUG_TEK).isSelected()).isFalse();
        assertThat(form.textField(JmxConstants.CONTEXT_NAME).getText()).isEqualTo(Constants.DEFAULT);

        assertThat(form.textField(JmxConstants.GELF_SERVER).getText()).isEqualTo(Constants.LOCALHOST);
        assertThat(form.textField(JmxConstants.GELF_PORT).getText()).isEqualTo("12201");
        assertThat(form.textField(JmxConstants.GELF_FACILITY).getText()).isEqualTo(LoggingProps.GELF_FACILITY);
        assertThat(form.checkBox(JmxConstants.DEBUG_TEK).isSelected()).isFalse();
        assertThat(form.checkBox(JmxConstants.GELF_OUTPUT).isSelected()).isFalse();

        form.selectTab(CONFIGURATION_TAB, 4);

        final SeleniumDriverRule.TableDom table = form.table("servicesTab");

        assertThat(table.rowCount()).isEqualTo(8);

        table.row(0).click();

        final SeleniumDriverRule.TableDom attrTable = form.table("beansAtts");
        assertThat(attrTable.rowCount()).isEqualTo(7);
    }

    @Test public void createClusterForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.ClusterForm");

        form.textField("clusterName").setValue("new Cluster");
        final SeleniumDriverRule.TableDom table = form.table("discoveryEntryPoints");

        form.button("addRowBottom").click();

        final SeleniumDriverRule.RowDom row = table.row(table.rowCount() - 1);
        row.textField("ip").setValue("localhost");

        form.saveAndStay();

        form.search("new Cluster");
        driver.pressEnterAndDelay();
        form.delete();
    }

    @Test public void entityForm() {
        final SeleniumDriverRule.FormDom  form        = driver.formDom("tekgenesis.console.EntitiesForm");
        final SeleniumDriverRule.TableDom domainTable = form.table(ENTITIES_TABLE);
        assertThat(domainTable.rowCount()).isEqualTo(8);

        form.textField(SEARCH_BOX).setValue("User");

        driver.pressEnterAndDelay();

        final SeleniumDriverRule.TableDom table = form.table(ENTITIES_TABLE);
        assertThat(table.rowCount()).isGreaterThanOrEqualTo(2);
    }

    @Test public void metrics() {
        driver.formDom("tekgenesis.console.Metrics");
        driver.delay();
    }

    @Test public void searchClusterListForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.ClusterListForm");

        final SeleniumDriverRule.TableDom table = form.table("clusters");

        assertThat(table.rowCount()).isEqualTo(1);
    }

    @Test public void serverEmailForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.EmailForm");

        final SeleniumDriverRule.TableDom table = form.table("mailStatusTable");

        assertThat(table.rowCount()).isEqualTo(0);
    }

    @Test public void statusForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.StatusForm");
        assertThat(form.display("nodes").getText()).isEqualTo("1");
        assertThat(Integer.valueOf(form.display("loadedTasks").getText())).isGreaterThanOrEqualTo(3);
    }

    @Ignore public void taskForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.TaskDashboard");

        form.selectTab(FORM_TAB, 2);
        final SeleniumDriverRule.TableDom deactiveTaskTable = form.table("deactiveTask");
        assertThat(deactiveTaskTable.rowCount()).isGreaterThanOrEqualTo(0);

        form.selectTab(FORM_TAB, 1);

        final SeleniumDriverRule.TableDom activeTaskTable = form.table("activeTask");
        assertThat(activeTaskTable.rowCount()).isGreaterThanOrEqualTo(0);

        final SeleniumDriverRule.TableDom scheduleTaskTable = form.table("scheduleTask");
        assertThat(scheduleTaskTable.rowCount()).isGreaterThanOrEqualTo(1);

        form.selectTab(FORM_TAB, 2);

        // noinspection DuplicateStringLiteralInspection
        scheduleTaskTable.row(0).linkByText("Scheduled").click();
    }

    @Ignore public void userConsoleForm() {
        final SeleniumDriverRule.FormDom form = driver.formDom("tekgenesis.console.UsersForm");
        // noinspection DuplicateStringLiteralInspection
        form.textField(SEARCH_BOX).setValue("admin");
        driver.pressEnterAndDelay();
        final SeleniumDriverRule.TableDom table = form.table(USERS_TABLE);
        assertThat(table.rowCount()).isEqualTo(1);

        form.linkByText("1").click();
        driver.delay();
        driver.getKeyboard().pressKey(Keys.ESCAPE);
        form.textField(SEARCH_BOX).setValue("admin2");
        driver.pressEnterAndDelay();
        assertThat(form.table(USERS_TABLE).rowCount()).isZero();
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { SeleniumBrowser.CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    private static final String FORM_TAB          = "taskTab";
    private static final String SEARCH_BOX        = "searchBox";
    private static final String USERS_TABLE       = "usersTable";
    private static final String ENTITIES_TABLE    = "entitiesTable";
    private static final String CONFIGURATION_TAB = "configurationTab";

    @ClassRule public static SeleniumRule service = new SeleniumRule() {
            protected void resetEnv() {
                super.resetEnv();

                final JmxServiceProps props = new JmxServiceProps();
                props.enabled = true;
                env.put(deCapitalizeFirst(JmxService.SERVICE_NAME), props);
            }
        };
}  // end class ClusterConsoleTest
