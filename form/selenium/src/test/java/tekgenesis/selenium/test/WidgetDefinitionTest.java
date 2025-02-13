
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

@Category(AppTests.class)
@SuppressWarnings("DuplicateStringLiteralInspection")
public class WidgetDefinitionTest {

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

    @Test public void testMaxWidthInWidgetDefinitions() {
        final SeleniumDriverRule.FormDom                 formDom     = driver.formDom("tekgenesis.showcase.CustomerTypeForm");
        final SeleniumDriverRule.WidgetDefDom            homeAddress = formDom.widgetDef("homeAddress");
        final SeleniumDriverRule.FieldFinder.ElementType homeStreet  = homeAddress.textField("street");
        assertThat(homeStreet.getStyle()).isEqualTo("max-width: 20em;");
        homeStreet.setValue("America 2005");
        homeAddress.textField("state").setValue("Buenos Aires");
        driver.delay();
        driver.delay();
        driver.delay();
        homeAddress.textField("city").setValue("Don Torcuato");

        assertThat(formDom.display("home").getText()).isEqualTo("America 2005 Buenos Aires Don Torcuato");

        formDom.button("createWorkAddress").click();

        final SeleniumDriverRule.WidgetDefDom            workAddress = formDom.widgetDef("workAddress");
        final SeleniumDriverRule.FieldFinder.ElementType workStreet  = workAddress.textField("street");
        assertThat(workStreet.getStyle()).isEqualTo("max-width: 20em;");
        workStreet.setValue("Av. Cayetano Beliera 3025");
        workAddress.textField("state").setValue("Buenos Aires");
        driver.delay();
        driver.delay();
        driver.delay();
        workAddress.textField("city").setValue("Pilar");

        assertThat(formDom.display("work").getText()).isEqualTo("Av. Cayetano Beliera 3025 Buenos Aires Pilar");
    }

    @Test public void testWidgetDefStylingExpressions() {
        final SeleniumDriverRule.FormDom formDom = driver.formDom("tekgenesis.showcase.CoordinatesForm");

        final SeleniumDriverRule.WidgetDefDom from = formDom.widgetDef("from");
        from.textField("lat").setValue("1");
        from.textField("lng").setValue("1");

        final SeleniumDriverRule.WidgetDefDom to = formDom.widgetDef("to");
        to.textField("lat").setValue("2");
        to.textField("lng").setValue("2");
        driver.delay();

        // from shouldn't have label
        assertThat(from.hasLabel()).isFalse();
        assertThat(to.hasLabel()).isTrue();
        assertThat(to.getLabel()).isEqualTo("Not 4");
        assertThat(to.getIcon()).isEqualTo("android");
        assertThat(to.isHidden()).isFalse();

        // set lat and lngs , so lng diff its 2 so TO is hidden and from color is danger.
        from.textField("lng").setValue("4");
        driver.delay();
        driver.delay();
        driver.delay();
        assertThat(to.isHidden()).isTrue();
        assertThat(from.isHidden()).isFalse();
        assertThat(from.getStyleClasses()).contains("text-danger");

        // change lng to diff 3 so from text is primary color and T) is shownn again, it label should be Not four and the icon android
        from.textField("lng").setValue("5");
        driver.delay();
        driver.delay();
        driver.delay();
        assertThat(to.isHidden()).isFalse();
        assertThat(from.isHidden()).isFalse();
        assertThat(from.getStyleClasses()).contains("text-primary");
        assertThat(to.getLabel()).isEqualTo("Not 4");
        assertThat(to.getIcon()).isEqualTo("android");

        // change lng to diff 4 so text danger again , TO still shown , label exactly four and icon star
        from.textField("lng").setValue("6");
        driver.delay();
        driver.delay();
        driver.delay();
        assertThat(to.isHidden()).isFalse();
        assertThat(from.isHidden()).isFalse();
        assertThat(from.getStyleClasses()).contains("text-danger");
        assertThat(to.getLabel()).isEqualTo("Exactly 4");
        assertThat(to.getIcon()).isEqualTo("star");
    }  // end method testWidgetDefStylingExpressions

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { SeleniumBrowser.CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class WidgetDefinitionTest
