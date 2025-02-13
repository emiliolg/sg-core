
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.metadata.exception.InvalidKeyToBeLocalized;
import tekgenesis.metadata.exception.InvalidKeyToBeLocalized.InvalidKeyLocalizeType;
import tekgenesis.model.KeyMap;
import tekgenesis.sales.basic.Category;
import tekgenesis.sales.basic.CategoryForm;
import tekgenesis.showcase.*;
import tekgenesis.showcase.DisplayShowcase.ItemsRow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.form.FormTests.*;
import static tekgenesis.metadata.exception.InvalidKeyToBeLocalized.InvalidKeyLocalizeType.*;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class FormTestsTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    public DbRule db = new DbRule(DbRule.AUTHORIZATION, DbRule.BASIC) {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    //~ Methods ......................................................................................................................................

    @Test public void categoryFormTest() {
        runInTransaction(() -> {
            final CategoryForm form = initialize(CategoryForm.class);

            final int categoryId = 32;
            form.setId(categoryId);
            final String createName = "test";
            form.setName(createName);
            final String createDescr = "test description";
            form.setDescr(createDescr);
            form.create();

            // just check there is no need to use Databases.commitTransaction() to see db changes
            final Category category = Category.findPersisted(categoryId);
            assertThat(category).isNotNull();
            assertThat(category.getName()).isNotNull();
            assertThat(category.getName()).isEqualTo(createName);
            assertThat(category.getDescr()).isNotNull();
            assertThat(category.getDescr()).isEqualTo(createDescr);

            final CategoryForm updateForm = initialize(CategoryForm.class, category.keyAsString());
            final String       updateName = "test";
            updateForm.setName(updateName);
            final String updateDescr = "updated description";
            updateForm.setDescr(updateDescr);
            updateForm.update();

            // just check there is no need to use Databases.commitTransaction() to see db changes
            final Category upCat = Category.findPersisted(categoryId);
            assertThat(upCat).isNotNull();
            assertThat(upCat.getName()).isNotNull();
            assertThat(upCat.getName()).isEqualTo(updateName);
            assertThat(upCat.getDescr()).isNotNull();
            assertThat(upCat.getDescr()).isEqualTo(updateDescr);
        });
    }  // end method categoryFormTest

    @Test public void formTests() {
        final DisplayShowcase form = initialize(DisplayShowcase.class);

        // test initial expressions;
        final Expressions textExpr = getFieldExpressions(form, DisplayShowcaseBase.Field.TEXT2);

        assertThat(textExpr.isDisabled()).isTrue();
        assertThat(isDisabled(form, DisplayShowcaseBase.Field.TEXT2)).isTrue();
        assertThat(textExpr.isHidden()).isFalse();
        assertThat(isHidden(form, DisplayShowcaseBase.Field.TEXT2)).isFalse();
        assertThat(textExpr.getHint()).isEqualTo("Change Input #1");
        assertThat(textExpr.getPlaceholder()).isEqualTo("look up!");

        // test that combo_box options are the ones expected
        final KeyMap options = getFieldOptions(form, DisplayShowcaseBase.Field.DISPLAY_COMBO);
        assertThat(options.size()).isEqualTo(3);

        // see that message of adding a row's action is fine
        assertThat(getActionMessage(form.addRow())).isEqualTo("Row added!");

        // check changing disable and hide expressions;
        final Expressions comboExpr = getFieldExpressions(form, DisplayShowcaseBase.Field.COMBO);

        assertThat(comboExpr.isDisabled()).isFalse();
        assertThat(isDisabled(form, DisplayShowcaseBase.Field.COMBO)).isFalse();
        assertThat(comboExpr.isHidden()).isFalse();
        assertThat(isHidden(form, DisplayShowcaseBase.Field.COMBO)).isFalse();

        form.setDisableCheck(true);

        assertThat(comboExpr.isDisabled()).isTrue();
        assertThat(isDisabled(form, DisplayShowcaseBase.Field.COMBO)).isTrue();
        assertThat(comboExpr.isHidden()).isFalse();
        assertThat(isHidden(form, DisplayShowcaseBase.Field.COMBO)).isFalse();

        form.setHideCheck(true);

        assertThat(comboExpr.isDisabled()).isTrue();
        assertThat(isDisabled(form, DisplayShowcaseBase.Field.COMBO)).isTrue();
        assertThat(comboExpr.isHidden()).isTrue();
        assertThat(isHidden(form, DisplayShowcaseBase.Field.COMBO)).isTrue();

        // check table expressions;
        final ItemsRow    row  = form.getItems().get(0);
        final Expressions expr = getFieldExpressions(form, DisplayShowcaseBase.Field.NUMBER_ITEM, 0);

        assertThat(expr.getHint()).isEqualTo("##");
        assertThat(expr.getPlaceholder()).isEmpty();
        assertThat(expr.isDisabled()).isFalse();
        assertThat(expr.isHidden()).isFalse();

        row.setCheckItem(true);

        assertThat(expr.getHint()).isEmpty();
        assertThat(expr.getPlaceholder()).isEmpty();
        assertThat(expr.isDisabled()).isTrue();
        assertThat(expr.isHidden()).isFalse();
    }  // end method formTests

    @Test public void i18nExtensionFormTest() {
        final DynamicTableForm form = initialize(DynamicTableForm.class);

        final Locale es = new Locale("es");
        assertThat(getLocalizedMessage(form, es, "DynamicTableForm")).isEqualTo("Dynamic Table Extended");
        assertThat(getLocalizedMessage(form, es, "ixMapping")).isEqualTo("Ix Mapping");
        assertThat(getLocalizedMessage(form, es, "clientAddresses")).isEqualTo("Addresses Extended");
        assertThat(getLocalizedMessage(form, es, "country")).isEqualTo("Country");
        assertThat(getLocalizedMessage(form, es, "state")).isEqualTo("State");
        assertThat(getLocalizedMessage(form, es, "city")).isEqualTo("City");
        assertThat(getLocalizedMessage(form, es, "street")).isEqualTo("Street");
        assertThat(getLocalizedMessage(form, es, "zip")).isEqualTo("Postal Code");
        assertThat(getLocalizedMessage(form, es, "addRowBottom")).isEqualTo("Add");
        assertThat(getLocalizedMessage(form, es, "removeRowBottom")).isEqualTo("Remove");
    }

    @Test public void i18nFormTest() {
        final I18nForm form = initialize(I18nForm.class);

        final Locale es = new Locale("es");
        assertThat(getLocalizedMessage(form, es, "name")).isEqualTo("Nombre");
        assertThat(getLocalizedMessage(form, es, "name.hint")).isEqualTo("Escribí tu nombre");
        assertThat(getLocalizedMessage(form, es, "statement.is")).isEqualTo("Esto es Boca Juniors");
        assertThat(getLocalizedMessage(form, es, "hasName.check")).isEqualTo("No hay nombre");
        assertThat(getLocalizedMessage(form, es, "hasName.check.1")).isEqualTo("El nombre es muy largo");
        assertI18nException(form, es, "noFieldHasThisName.check", INVALID_KEY);

        final Locale en = new Locale("en");
        assertThat(getLocalizedMessage(form, en, "name")).isEqualTo("Name");
        assertThat(getLocalizedMessage(form, en, "name.hint")).isEqualTo("Write your name");
        assertThat(getLocalizedMessage(form, en, "statement.is")).isEqualTo("This is Boca Juniors");
        assertThat(getLocalizedMessage(form, en, "hasName.check")).isEqualTo("No name");
        assertThat(getLocalizedMessage(form, en, "hasName.check.1")).isEqualTo("Name is too long");

        assertI18nException(form, en, "hasName.notExistentOption", FIELD_OPTION_DOESNT_EXIST);
        assertI18nException(form, en, "hasName.check.0.invalid", TOO_MANY_ARGUMENTS);
        assertI18nException(form, en, "hasName.disable", FIELD_OPTION_CANNOT_LOCALIZE);
        assertI18nException(form, en, "hasName.check.notnumber", NOT_A_NUMBER);
        assertI18nException(form, en, "hasName.check.2", NOT_THAT_MANY_CHECKS);
    }  // end method i18nFormTest

    @Test public void uiChangeFormTest() {
        final MailFieldShowcaseForm form = initialize(MailFieldShowcaseForm.class);

        final String mail = "asdas@gmail.com";
        setFieldCallingUiChange(form, MailFieldShowcaseFormBase.Field.MAIL_CHANGE, mail);
        assertThat(form.getMail()).isEqualTo(MailFieldShowcaseForm.CHANGE_MAIL);
        assertThat(form.getMailChange()).isEqualTo(mail);

        final String name = "name";
        setFieldCallingUiChange(form, MailFieldShowcaseFormBase.Field.UI_TABLE, 0, MailFieldShowcaseFormBase.Field.NAME, name);

        final MailFieldShowcaseForm.UiTableRow row = form.getUiTable().get(0);
        assertThat(row.getNick()).isEqualTo(MailFieldShowcaseForm.SUPER_NICK);
        assertThat(row.getName()).isEqualTo(name);
    }  // end method uiChangeFormTest

    @Test public void widgetDefInFormTest() {
        final CoordinatesForm form = initialize(CoordinatesForm.class);

        form.getFrom().setLat(1);
        form.getFrom().setLng(3);
        form.getTo().setLat(2);
        form.getTo().setLng(8);

        assertThat(form.getFromCount()).isEqualTo(1);
        assertThat(form.getToCount()).isEqualTo(1);
        assertThat(form.getLatDiff()).isEqualTo(1);
        assertThat(form.getLngDiff()).isEqualTo(5);
        form.getFrom().setLng(1);
        assertThat(form.getFromCount()).isEqualTo(2);
        assertThat(form.getLngDiff()).isEqualTo(7);
        form.getTo().setLng(7);
        assertThat(form.getToCount()).isEqualTo(2);
        assertThat(form.getLngDiff()).isEqualTo(6);
        form.getTo().setLng(8);
        assertThat(form.getToCount()).isEqualTo(3);
        assertThat(form.getLngDiff()).isEqualTo(7);
    }

    @Test public void setCurrentTest() {
        final RelatedTablesForm form = initialize(RelatedTablesForm.class);

        final RelatedTablesForm.FirstRow first = form.getFirst().add();
        setCurrent(form, RelatedTablesForm.Field.FIRST, 0);
        final RelatedTablesForm.FirstRow firstCurrent = form.getFirst().getCurrent();
        assertThat(first.getA()).isEqualTo(firstCurrent.getA());

        final RelatedTablesForm.SecondRow second = form.getSecond().add();
        setCurrent(form, RelatedTablesForm.Field.SECOND, 0);
        final RelatedTablesForm.SecondRow secondCurrent = form.getSecond().getCurrent();
        assertThat(second.getX()).isEqualTo(secondCurrent.getX());
    }

    private void assertI18nException(@NotNull final FormInstance<?> form, @NotNull Locale locale, @NotNull String key, InvalidKeyLocalizeType type) {
        try {
            getLocalizedMessage(form, locale, key);
            fail("The key shoueld've thrown an InvalidKeyToBeLozalized exception.");
        }
        catch (final InvalidKeyToBeLocalized ex) {
            assertThat(ex.getType()).isEqualTo(type);
        }
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class FormTestsTest
