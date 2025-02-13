
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
import static tekgenesis.selenium.SeleniumBrowser.FIREFOX;

@Category(AppTests.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class SubformTest {

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

    @Test public void cancelSubformTest() {
        final SeleniumDriverRule.FormDom             formDom = driver.formDom("tekgenesis.showcase.SubformActions");
        final SeleniumDriverRule.FieldFinder.Subform subform = formDom.subform("someSubform", "SomeSubform");

        subform.open();

        subform.subformDom().checkBox("someCheckbox").setSelected(true);
        subform.subformDom().checkBox("someOtherCheckbox").setSelected(false);
        subform.subformDom().textField("someOtherText").setValue("Mmmm Some text...");
        subform.subformDom().textField("someNumber").setValue("31071992");
        subform.subformDom().textField("someDate").setValue("31/07/1992");

        subform.cancel();

        subform.open();

        assertThat(subform.subformDom().checkBox("someCheckbox").isSelected()).isEqualTo(false);
        assertThat(subform.subformDom().checkBox("someOtherCheckbox").isSelected()).isEqualTo(false);
        assertThat(subform.subformDom().textField("someOtherText").getText()).isEmpty();
        assertThat(subform.subformDom().textField("someNumber").getText()).isEmpty();
        assertThat(subform.subformDom().textField("someDate").getText()).isEmpty();

        subform.cancel();
    }

    @Test public void okSubformTest() {
        final SeleniumDriverRule.FormDom             formDom = driver.formDom("tekgenesis.showcase.SubformActions");
        final SeleniumDriverRule.FieldFinder.Subform subform = formDom.subform("someSubform", "SomeSubform");

        subform.open();

        subform.subformDom().checkBox("someCheckbox").setSelected(true);
        subform.subformDom().checkBox("someOtherCheckbox").setSelected(false);
        subform.subformDom().textField("someOtherText").setValue("Mmmm Some text...");
        subform.subformDom().textField("someNumber").setValue("22");
        subform.subformDom().textField("someDate").setValue("31/07/1992");

        subform.ok();

        subform.open();

        assertThat(subform.subformDom().checkBox("someCheckbox").isSelected()).isEqualTo(true);
        assertThat(subform.subformDom().checkBox("someOtherCheckbox").isSelected()).isEqualTo(false);
        assertThat(subform.subformDom().textField("someText").getText()).isEmpty();
        assertThat(subform.subformDom().textField("someOtherText").getText()).isEqualTo("Mmmm Some text...");
        assertThat(subform.subformDom().textField("someNumber").getText()).isEqualTo("22");
        assertThat(subform.subformDom().textField("someDate").getText()).isEqualTo("31/07/1992");

        subform.cancel();
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { CHROME }, new Object[] { FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class SubformTest
