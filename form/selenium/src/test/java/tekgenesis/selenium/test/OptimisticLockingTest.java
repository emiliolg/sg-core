
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

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.metadata.form.MetadataFormMessages;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumRule;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.selenium.SeleniumBrowser.CHROME;

@Category(AppTests.class)
public class OptimisticLockingTest {

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

    @SuppressWarnings("OverlyLongMethod")
    @Test public void optimisticLockingTest() {
        final String                     formFqn         = "tekgenesis.sales.basic.CategoryForm";
        final SeleniumDriverRule.FormDom categoryStarter = driver.formDom(formFqn);

        categoryStarter.textField("name").setValue("test");
        categoryStarter.textArea("descr").setValue("test description");
        final String idValue = "1";
        categoryStarter.textField("id").setValue(idValue);

        final String urlPrefix   = driver.getBaseUrl() + "/#form/";
        final String categoryUrl = urlPrefix + formFqn;
        // created category and saved
        categoryStarter.saveAndExpect(categoryUrl);

        driver.delay();
        driver.delay();

        // open category form bound to the category just created in the current tab and in a new one.
        final SeleniumDriverRule.FormDom category1 = driver.formDom(formFqn, idValue);

        driver.delay();

        final SeleniumDriverRule.FormDom category2 = driver.formDomInNewTab(formFqn, idValue);
        final ImmutableSet<String>       immutable = Colls.immutable(driver.getDriver().getWindowHandles());
        final String                     window2   = immutable.revert().getFirst().get();
        driver.getDriver().switchTo().window(window2);
        driver.delay();

        final SeleniumDriverRule.FieldFinder.ElementType name = category2.textField("name");
        name.setValue("newvalue");
        category2.saveAndExpect(categoryUrl);

        driver.delay();
        driver.closeCurrentTab();

        category1.textField("name").setValue("tryOtherName");
        category1.saveAndExpect(categoryUrl + "/" + idValue);
        // changed and updated the category in the new tab, closed it and now we change the first tab expecting the error cause that category
        // instance was edited.
        category1.feedback().getFirst().map(feedback -> assertThat(feedback).startsWith(MetadataFormMessages.MSGS.changedEntity("Category")));

        final SeleniumDriverRule.FormDom category3 = driver.formDom(formFqn, idValue);
        assertThat(category3.textField("name").getText()).isEqualTo("newvalue");
    }  // end method optimisticLockingTest

    @Test public void updateFormModelsTimestampManually() {
        final String                     formFqn = "tekgenesis.showcase.DNIForm";
        final SeleniumDriverRule.FormDom dniForm = driver.formDom(formFqn);

        final String number = "374171";
        dniForm.textField("number").setValue(number);
        dniForm.textField("descr").setValue("dni description");

        final String urlPrefix = driver.getBaseUrl() + "/#form/";
        final String dniUrl    = urlPrefix + formFqn;
        // created dni and saved
        dniForm.saveAndExpect(dniUrl);

        driver.delay();
        driver.delay();

        // open dni form bound to the dni just created.
        final SeleniumDriverRule.FormDom dni1 = driver.formDom(formFqn, number);

        driver.delay();

        dni1.textField("descr").setValue("number 1 descritpion");
        dni1.button("manual").click();

        driver.delay();

        dni1.saveAndExpect(dniUrl + "/" + number);
        // changed and updated the category in the new tab, closed it and now we change the first tab expecting the error cause that category
        // instance was edited.
        dni1.feedback().getFirst().map(feedback -> assertThat(feedback).startsWith(MetadataFormMessages.MSGS.changedEntity("DNI")));

        // open dni form bound to the dni just manually updated.
        final SeleniumDriverRule.FormDom dni2 = driver.formDom(formFqn, number);

        driver.delay();

        dni2.textField("descr").setValue("number 2 descritpion");
        dni2.button("manualUpdating").click();

        driver.delay();

        dni2.saveAndExpect(dniUrl);
    }  // end method updateFormModelsTimestampManually

    @Test public void updateTimeUpdatedOnDeprecate() {
        // test deprecable, create one, then open it deprecate it , undeprecate it and try to save. It should work
        final String                     deprecableFqn  = "tekgenesis.showcase.AnotherDeprecableEntityForm";
        final SeleniumDriverRule.FormDom deprecableForm = driver.formDom(deprecableFqn);
        deprecableForm.textField("name").setValue("name");

        final String urlPrefix     = driver.getBaseUrl() + "/#form/";
        final String deprecableUrl = urlPrefix + deprecableFqn;
        deprecableForm.saveAndExpect(deprecableUrl);

        driver.delay();

        final SeleniumDriverRule.FormDom existingDep = driver.formDom(deprecableFqn);
        existingDep.search("name");
        existingDep.toggleDeprecate();
        existingDep.toggleDeprecate();
        existingDep.textField("name").setValue("other name");
        final String currentUrl = driver.getDriver().getCurrentUrl();
        final String depId      = currentUrl.substring(currentUrl.lastIndexOf('/') + 1);
        existingDep.saveAndExpect(deprecableUrl);

        driver.delay();

        // open deprecable form bound to the deprecable just created in the current tab and in a new one.
        final SeleniumDriverRule.FormDom deprecable1 = driver.formDom(deprecableFqn, depId);

        driver.delay();

        final SeleniumDriverRule.FormDom deprecable2            = driver.formDomInNewTab(deprecableFqn, depId);
        final ImmutableSet<String>       immutableDeprecatables = Colls.immutable(driver.getDriver().getWindowHandles());
        final String                     deprecableWindow2      = immutableDeprecatables.revert().getFirst().get();
        driver.getDriver().switchTo().window(deprecableWindow2);
        driver.delay();

        deprecable2.textField("name").setValue("newvalue");
        deprecable2.saveAndExpect(deprecableUrl);

        driver.delay();
        driver.closeCurrentTab();

        deprecable1.toggleDeprecate();
        // changed and updated the deprecable in the new tab, closed it and now we deprecate the first tab expecting the error cause that deprecable
        // instance was edited.
        deprecable1.feedback().getFirst().map(feedback ->
                assertThat(feedback).startsWith(MetadataFormMessages.MSGS.changedEntity("AnotherDeprecableEntity")));
    }  // end method updateTimeUpdatedOnDeprecate

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class OptimisticLockingTest
