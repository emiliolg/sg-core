
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

import static tekgenesis.selenium.SeleniumBrowser.CHROME;

@Category(AppTests.class)
public class SwipeTest {

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

    @Test public void swipeTest() {
        final SeleniumDriverRule.FormDom classroomForm = driver.formDom("tekgenesis.showcase.ClassroomForm");

        classroomForm.table("students").row(0).button("view").click();
        final SeleniumDriverRule.SwipeDom swipeDoms = driver.swipeDom().getOrFail("There is no swipe opened.");

        swipeDoms.dropdown("saleDropDown2").openMenu();
        swipeDoms.dropdown("saleDropDown2").clickMenuItem(1);

        driver.delay();

        final String currentUrl = driver.getDriver().getCurrentUrl();

        assertThat(currentUrl).isEqualTo(driver.getBaseUrl() + "/#form/tekgenesis.showcase.SimpleEntityForm");
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class SwipeTest
