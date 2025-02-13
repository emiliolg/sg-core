
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
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumDriverRule.FormDom;
import tekgenesis.selenium.SeleniumDriverRule.RowDom;
import tekgenesis.selenium.SeleniumDriverRule.TableDom;
import tekgenesis.selenium.SeleniumRule;
import tekgenesis.showcase.DialogsForm;
import tekgenesis.showcase.ErrorShowcase;

import static org.assertj.core.api.Assertions.assertThat;

@Category(AppTests.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "ReuseOfLocalVariable" })
public class ShowcaseTest {

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

    @Test public void datesTest() {
        final FormDom dates = driver.formDom("tekgenesis.showcase.SimpleDate");

        testDateBox(dates.dateBox("dateBox"));

        testDateBox(dates.dateBox("doubleDateBox"));

        final SeleniumDriverRule.FieldFinder.DateBox dateTimeBox = dates.dateBox("dateTimeBox");
        dateTimeBox.setValue("12/23/2014 00:00:00");
        assertThat(dateTimeBox.getText()).isEqualTo("12/23/2014 00:00:00");
    }

    @Test public void listingFormTest() {
        FormDom  listing  = driver.formDom("tekgenesis.showcase.ListingSimpleEntities");
        TableDom entities = listing.table("entities");

        listing.button("addRowBottom").click();

        final RowDom row0 = entities.row(entities.rowCount() - 1);

        row0.textField("name").setValue("Name 0");
        row0.textField("desc").setValue("Description 0");

        listing.button("addRowBottom").click();
        final RowDom row1 = entities.row(entities.rowCount() - 1);

        row1.textField("name").setValue("Name 1");
        row1.textField("desc").setValue("Description 1");

        listing.button("addRowBottom").click();

        listing = driver.formDom("tekgenesis.showcase.ListingSimpleEntities");

        entities = listing.table("entities");

        assertThat(entities.row(0).textField("name").getText()).isEqualTo("Name 0");
        assertThat(entities.row(0).textField("desc").getText()).isEqualTo("Description 0");
        assertThat(entities.row(1).textField("name").getText()).isEqualTo("Name 1");
        assertThat(entities.row(1).textField("desc").getText()).isEqualTo("Description 1");

        entities.row(0).click();
        listing.button("removeRowBottom").click();

        assertThat(entities.row(0).textField("name").getText()).isEqualTo("Name 1");
        assertThat(entities.row(0).textField("desc").getText()).isEqualTo("Description 1");

        listing  = driver.formDom("tekgenesis.showcase.ListingSimpleEntities");
        entities = listing.table("entities");
        assertThat(entities.row(0).textField("name").getText()).isEqualTo("Name 1");
        assertThat(entities.row(0).textField("desc").getText()).isEqualTo("Description 1");
    }

    @Test public void mapConfigurationTest() {
        final FormDom maps = driver.formDom("tekgenesis.showcase.MapConfigurationForm");
        maps.button("zoomIn").click();
    }

    @Ignore public void notFound() {
        final String  notFoundFqn = "tekgenesis.showcase.NotFoundForm";
        final FormDom form        = driver.formDom(notFoundFqn);

        driver.waitForFeedback();

        final Option<String> feedback = form.feedback().getFirst();

        assertThat(feedback.isPresent());
        assertThat(feedback.get()).isEqualTo(notFoundFqn + " not found.");

        driver.formDom("tekgenesis.showcase.SimpleEntityForm");
    }

    @Ignore public void notFoundWithKey() {
        final FormDom form = driver.formDom("tekgenesis.showcase.SimpleEntityForm", "notvalidkey");

        driver.waitForFeedback();

        final Option<String> feedback = form.feedback().getFirst();

        assertThat(feedback.isPresent());
        assertThat(form.feedback()).contains("SimpleEntity with id notvalidkey not found.");

        driver.formDom("tekgenesis.showcase.SimpleEntityForm");
    }

    @Test public void resetGroupTest() {
        final SeleniumDriverRule.FormDom resetForm = driver.formDom("tekgenesis.showcase.ResetGroupForm");

        resetForm.button("addButton").click();
        final String                                     intValue = "123";
        final SeleniumDriverRule.FieldFinder.ElementType horInt   = resetForm.textField("horInt");
        horInt.setValue(intValue);
        final SeleniumDriverRule.FieldFinder.ElementType horVertInt = resetForm.textField("horVertInt");
        horVertInt.setValue(intValue);
        final SeleniumDriverRule.FieldFinder.ElementType mail = resetForm.textField("mail");
        mail.setValue("asdsad@gmail.com");
        final SeleniumDriverRule.FieldFinder.ElementType row0Int = resetForm.table("horTable").row(0).textField("horTableInt");
        row0Int.setValue(intValue);
        final SeleniumDriverRule.FieldFinder.ElementType verInt = resetForm.textField("verInt");
        verInt.setValue(intValue);
        final SeleniumDriverRule.FieldFinder.ElementType footInt = resetForm.textField("footInt");
        footInt.setValue(intValue);

        resetForm.checkBox("horButt").setSelected(true);
        assertThat("").isEqualTo(horInt.getText());
        assertThat("").isEqualTo(horVertInt.getText());
        assertThat("").isEqualTo(mail.getText());
        assertThat(intValue).isEqualTo(row0Int.getText());
        assertThat(intValue).isEqualTo(verInt.getText());
        assertThat(intValue).isEqualTo(footInt.getText());

        resetForm.checkBox("verButt").setSelected(true);
        assertThat("").isEqualTo(horInt.getText());
        assertThat("").isEqualTo(horVertInt.getText());
        assertThat("").isEqualTo(mail.getText());
        assertThat(intValue).isEqualTo(row0Int.getText());
        assertThat("").isEqualTo(verInt.getText());
        assertThat(intValue).isEqualTo(footInt.getText());

        resetForm.checkBox("footButt").setSelected(true);
        assertThat("").isEqualTo(horInt.getText());
        assertThat("").isEqualTo(horVertInt.getText());
        assertThat("").isEqualTo(mail.getText());
        assertThat(intValue).isEqualTo(row0Int.getText());
        assertThat("").isEqualTo(verInt.getText());
        assertThat("").isEqualTo(footInt.getText());
    }  // end method resetGroupTest

    @Test public void tagsSuggestBoxTest() {
        final FormDom                                       suggests   = driver.formDom("tekgenesis.showcase.SuggestBoxShowcase");
        final SeleniumDriverRule.FieldFinder.TagsSuggestBox optionTags = suggests.tagsSuggestBox("optionTags");

        driver.delay();

        assertThat(optionTags.getTagsText()).containsExactly("Option1");
        optionTags.addTag("Option2");
        assertThat(optionTags.getTagsText()).containsExactly("Option1", "Option2");
    }

    @Test public void testActionCustomization() {
        final String errorForm    = "tekgenesis.showcase.ErrorShowcase";
        final String navigateForm = "tekgenesis.showcase.NiceToNavigateTo";

        final FormDom normalShowcase = driver.formDom(errorForm);
        final FormDom navigateNormal = normalShowcase.button("navigateNormal").clickAndExpect(navigateForm);

        driver.delay();

        assertThat(driver.getBaseUrl().endsWith(navigateForm));
        assertThat(navigateNormal.hasSuccessAction()).isTrue();
        navigateNormal.feedback().getFirst().map(feedback -> assertThat(feedback).contains(ErrorShowcase.NORMAL_NAVIGATE));

        final FormDom errorShowcase = navigateNormal.button("cancel").clickAndExpect(errorForm);
        final FormDom navigateError = errorShowcase.button("navigateError").clickAndExpect(navigateForm);

        driver.delay();

        assertThat(driver.getBaseUrl().endsWith(navigateForm));
        assertThat(navigateError.hasErrorAction()).isTrue();
        navigateError.feedback().getFirst().map(feedback -> assertThat(feedback).contains(ErrorShowcase.NAVIGATE_ERROR));

        final FormDom warningShowcase = navigateError.button("cancel").clickAndExpect(errorForm);
        final FormDom navigateWarning = warningShowcase.button("navigateWarning").clickAndExpect(navigateForm);

        driver.delay();

        assertThat(driver.getBaseUrl().endsWith(navigateForm));
        assertThat(navigateWarning.hasWarningAction()).isTrue();
        navigateWarning.feedback().getFirst().map(feedback -> assertThat(feedback).contains(ErrorShowcase.NAVIGATE_WARNING));
    }

    @Test public void testButtonDisability() {
        final FormDom formDom = driver.formDom("tekgenesis.showcase.DisplayShowcase");
        assertThat(formDom.button("button").isEnabled()).isEqualTo(true);
        formDom.checkBox("disableCheck").setSelected(true);

        assertThat(formDom.button("button").isEnabled()).isEqualTo(false);

        formDom.button("simpleSync").click();
        driver.delay();

        assertThat(formDom.button("button").isEnabled()).isEqualTo(false);
    }

    @Test public void testConfirmDoesntCloseDialog() {
        final FormDom formDom = driver.formDom("tekgenesis.showcase.DialogsForm");
        driver.delay();
        // open dialog some
        formDom.label("some").click();
        driver.delay();
        final SeleniumDriverRule.ModalDom modalDom = driver.modalDom().getOrFail("No modal exists");
        // assert dialog was opened
        assertThat(modalDom.isOpened()).isTrue();
        assertThat(modalDom.textField("text").getText().isEmpty()).isTrue();
        // click confirm button
        modalDom.button("confirm").click();
        // assert  confirm alert opened
        final SeleniumDriverRule.ConfirmDom confirmDom = driver.confirmDom().getOrFail("No alert is opened");
        confirmDom.ok();
        // assert confirm is closed
        assertThat(confirmDom.isOpened()).isFalse();
        // assert modal is opened
        assertThat(modalDom.isOpened()).isTrue();
        assertThat(modalDom.textField("text").getText()).isEqualTo(DialogsForm.BOCA);
        // close modal
        modalDom.close();
        assertThat(modalDom.isOpened()).isFalse();
        // asssert modal is closed
    }

    @Test public void testFocusOnFields() {
        final FormDom formDom = driver.formDom("tekgenesis.showcase.DialogsForm");
        driver.delay();
        // open dialog some
        formDom.button("foc").click();
        formDom.textField("text3").isFocused();

        // open dialog to test focus working inside
        formDom.button("open").click();
        final SeleniumDriverRule.ModalDom modalDom = driver.modalDom().getOrFail("No modal exists");

        // assert dialog was opened and text2 focused
        assertThat(modalDom.isOpened()).isTrue();
        assertThat(modalDom.textField("text2").getText().isEmpty()).isTrue();
        assertThat(modalDom.textFieldParent("text2").isFocused()).isTrue();

        // close modal
        modalDom.close();
        assertThat(modalDom.isOpened()).isFalse();
        // asssert modal is closed
        driver.delay();
    }

    @Test public void testMailBox() {
        final SeleniumDriverRule.FormDom mailForm = driver.formDom("tekgenesis.showcase.MailFieldShowcaseForm");

        final SeleniumDriverRule.FieldFinder.MailBox mailbox = mailForm.mailbox("mail");
        mailbox.setValue("joaquin@");
        assertThat(mailbox.getText()).isEqualTo("joaquin@hotmail.com");

        final SeleniumDriverRule.FieldFinder.MailBox mailSuggest = mailForm.mailbox("mailSuggest");
        mailSuggest.setValue("joaquin@");
        assertThat(mailSuggest.getText()).isEqualTo("joaquin@compumundo.com.ar");

        final SeleniumDriverRule.FieldFinder.MailBox mailSuggestSync = mailForm.mailbox("mailSuggestSync");
        mailSuggestSync.setValue("joaquin@");
        assertThat(mailSuggestSync.getText()).isEqualTo("joaquin@garbarino.com.ar");

        final SeleniumDriverRule.RowDom mailTable = mailForm.table("mailTable").row(0);

        final SeleniumDriverRule.FieldFinder.MailBox tableSuggest = mailTable.mailbox("tableSuggest");
        tableSuggest.setValue("joaquin@");
        assertThat(tableSuggest.getText()).isEqualTo("joaquin@facebook.com");

        final SeleniumDriverRule.FieldFinder.MailBox tableSuggestSync = mailTable.mailbox("tableSuggestSync");
        tableSuggestSync.setValue("joaquin@");
        assertThat(tableSuggestSync.getText()).isEqualTo("joaquin@garbarino.com.ar");
    }

    @Test public void testMustacheHtmlGenerationTest()
        throws InterruptedException
    {
        final FormDom form = driver.formDom("tekgenesis.showcase.HtmlGeneratorForm");

        form.textArea("source").setValue(SAMPLE_JSP);
        form.button("generateMustache").click();

        Thread.sleep(BIG_DELAY);

        final String result = form.textArea("result").getText();
        assertThat(result).contains("Lucas").contains("Pedro");
    }

    @Test public void testNoFirstFocusInSearchbox() {
        final FormDom formDom = driver.formDom("tekgenesis.showcase.MakeForm");
        driver.delay();
        driver.delay();
        assertThat(formDom.textFieldParent("name").isFocused()).isTrue();
        driver.delay();
    }

    @Test public void testPopulateBoundEntity() {
        final FormDom dniDom = driver.formDom("tekgenesis.showcase.DNIForm");
        dniDom.textField("number").setValue("37417103");
        dniDom.textField("descr").setValue("Esto es boca viejo!");
        dniDom.saveAndStay();

        final FormDom formDom = driver.formDom("tekgenesis.showcase.PersonWithDniForm");
        formDom.suggestBox("dni").setValue("37417103");
        formDom.textField("name").setValue("Joaquin");
        formDom.textField("lastname").setValue("Bucca");
        formDom.saveAndStay();

        driver.delay();
        formDom.search("37417103");
        driver.delay();
        assertThat(formDom.suggestBox("dni").getText()).isEqualTo("37417103");
        assertThat(formDom.textField("name").getText()).isEqualTo("Joaquin");
        assertThat(formDom.textField("lastname").getText()).isEqualTo("Bucca");
    }

    @Test public void validateGroupTest() {
        final SeleniumDriverRule.FormDom horizontal = driver.formDom("tekgenesis.showcase.ValidateGroupForm");

        horizontal.button("addButton").click();
        horizontal.button("horButt").click();
        assertThat(horizontal.textField("horInt").hasError()).isTrue();
        assertThat(horizontal.textField("horVertInt").hasError()).isTrue();
        assertThat(horizontal.textField("mail").hasError()).isFalse();
        assertThat(horizontal.table("horTable").row(0).textField("horTableInt").hasErrorInsideTable(0)).isTrue();
        assertThat(horizontal.textField("verInt").hasError()).isFalse();
        assertThat(horizontal.textField("footInt").hasError()).isFalse();

        final SeleniumDriverRule.FormDom vertical = driver.formDom("tekgenesis.showcase.ValidateGroupForm");

        vertical.button("addButton").click();
        vertical.button("verButt").click();
        assertThat(vertical.textField("horInt").hasError()).isFalse();
        assertThat(vertical.textField("horVertInt").hasError()).isFalse();
        assertThat(vertical.textField("mail").hasError()).isFalse();
        assertThat(vertical.table("horTable").row(0).textField("horTableInt").hasErrorInsideTable(0)).isFalse();
        assertThat(vertical.textField("verInt").hasError()).isTrue();
        assertThat(vertical.textField("footInt").hasError()).isFalse();

        final SeleniumDriverRule.FormDom footer = driver.formDom("tekgenesis.showcase.ValidateGroupForm");

        footer.button("addButton").click();
        footer.button("footButt").click();
        assertThat(footer.textField("horInt").hasError()).isFalse();
        assertThat(footer.textField("horVertInt").hasError()).isFalse();
        assertThat(footer.textField("mail").hasError()).isFalse();
        assertThat(footer.table("horTable").row(0).textField("horTableInt").hasErrorInsideTable(0)).isFalse();
        assertThat(footer.textField("verInt").hasError()).isFalse();
        assertThat(footer.textField("footInt").hasError()).isTrue();

        final SeleniumDriverRule.FormDom dialog = driver.formDom("tekgenesis.showcase.ValidateGroupForm");

        dialog.button("addButton").click();
        dialog.button("dialogButt").click();
        assertThat(dialog.textField("horInt").hasError()).isFalse();
        assertThat(dialog.textField("horVertInt").hasError()).isFalse();
        assertThat(dialog.textField("mail").hasError()).isFalse();
        assertThat(dialog.table("horTable").row(0).textField("horTableInt").hasErrorInsideTable(0)).isFalse();
        assertThat(dialog.textField("verInt").hasError()).isFalse();
        assertThat(dialog.textField("footInt").hasError()).isFalse();

        final SeleniumDriverRule.FormDom all = driver.formDom("tekgenesis.showcase.ValidateGroupForm");

        all.button("addButton").click();
        all.button("allButt").click();
        assertThat(all.textField("horInt").hasError()).isTrue();
        assertThat(all.textField("horVertInt").hasError()).isTrue();
        assertThat(all.textField("mail").hasError()).isFalse();
        assertThat(all.table("horTable").row(0).textField("horTableInt").hasErrorInsideTable(0)).isTrue();
        assertThat(all.textField("verInt").hasError()).isTrue();
        assertThat(all.textField("footInt").hasError()).isTrue();
    }  // end method validateGroupTest

    private void testDateBox(SeleniumDriverRule.FieldFinder.DateBox dateBox) {
        dateBox.setValue("12232014");
        assertThat(dateBox.getText()).isEqualTo("12/23/2014");

        dateBox.setValue("12/23/2014");
        assertThat(dateBox.getText()).isEqualTo("12/23/2014");
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { SeleniumBrowser.CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    private static final int BIG_DELAY = 5000;

    private static final String SAMPLE_JSP = "<!DOCTYPE html>\n" +
                                             "<html>\n" +
                                             "    <head>\n" +
                                             "        <title>Sample Mustache</title>\n" +
                                             "    </head>\n" +
                                             "    <body>\n" +
                                             "        {{#persons}}\n" +
                                             "            <p>Person with name {{this.name}} and age {{this.age}}</p>\n" +
                                             "        {{/persons}}\n" +
                                             "    </body>\n" +
                                             "</html>\n";

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class ShowcaseTest
