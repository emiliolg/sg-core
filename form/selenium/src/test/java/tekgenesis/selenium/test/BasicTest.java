
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium.test;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumRule;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.persistence.Sql.deleteFrom;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.selenium.SeleniumBrowser.CHROME;
import static tekgenesis.transaction.Transaction.runInTransaction;

@Category(AppTests.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class BasicTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public SeleniumBrowser browserName = CHROME;

    @Rule public SeleniumDriverRule driver = new SeleniumDriverRule() {
            @Override public DriverService getService() {
                return service.getService();
            }

            @Override protected SeleniumBrowser getBrowser() {
                return browserName;
            }
        };

    //~ Methods ......................................................................................................................................

    @Before public void beforeEach() {
        runInTransaction(() -> deleteFrom(CATEGORY).execute());
    }

    @Test public void createCategoryTest() {
        final SeleniumDriverRule.FormDom category = driver.formDom("tekgenesis.sales.basic.CategoryForm");
        category.textField("id").setValue("1");
        category.textField("name").setValue("Televisores");
        assertThat(category.textArea("descr").getText()).isEqualTo("Televisores");

        category.saveAndStay();

        final SeleniumDriverRule.FormDom load = driver.formDom("tekgenesis.sales.basic.CategoryForm", "1");
        assertThat(load.textField("id").getText()).isEqualTo("1");
    }

    @Test public void testSuggestBox() {
        final SeleniumDriverRule.FormDom suggest = driver.formDom("tekgenesis.showcase.SuggestedForm");

        suggest.textField("name").setValue("Lucas");
        suggest.textField("lastName").setValue("Luppani");

        suggest.saveAndStay();

        final SeleniumDriverRule.FormDom search = driver.formDom("tekgenesis.showcase.SuggestedForm");
        search.search("Lucas Luppani");
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class BasicTest
