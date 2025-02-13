
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.metadata.form.exprs.ValidationExpressionsEvaluator;
import tekgenesis.samples.form.FormValidationsTestDefaults;
import tekgenesis.samples.form.FormValidationsTestDefaultsBase;
import tekgenesis.samples.form.FormValidationsTestInteractive;
import tekgenesis.samples.form.FormValidationsTestInteractiveBase;
import tekgenesis.samples.form.FormValidationsTestTable;
import tekgenesis.samples.form.InternalValidations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class FormValidationsTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    public DbRule db = new DbRule(DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    //~ Methods ......................................................................................................................................

    @Test public void formInternalValidations() {
        SecurityUtils.getSession().authenticate("admin", "password");
        final InternalValidations instance = FormsImpl.init(InternalValidations.class);
        ReflectedFormInstance.wrap(instance).getModel().markInitElements();

        final Validations validations = validate(instance);
        assertThat(validations.isEmpty()).isFalse();

        instance.setField("some");

        final Validations revalidate = validate(instance);
        assertThat(revalidate.isEmpty()).isTrue();
    }

    @Test public void formValidationsTestsDefaults() {
        SecurityUtils.getSession().authenticate("admin", "password");
        final FormValidationsTestDefaults instance = FormsImpl.init(FormValidationsTestDefaults.class);
        ReflectedFormInstance.wrap(instance).getModel().markInitElements();

        // Test simple default;
        assertThat(instance.getItem1()).isEqualTo(10);
        assertThat(instance.getItem2()).isEqualTo(10);

        final Validations validations = validate(instance);

        // Test validations on defaults;
        assertThat(validations.getMessages(FormValidationsTestDefaultsBase.Field.ITEM1).size()).isEqualTo(1);
        assertThat(validations.getMessages(FormValidationsTestDefaultsBase.Field.ITEM2)).isEqualTo(Colls.emptyList());
        assertThat(validations.getMessages(FormValidationsTestDefaultsBase.Field.TOTAL).size()).isEqualTo(1);
        assertThat(validations.getAllMessages().size()).isEqualTo(2);
    }

    @Test public void formValidationsTestsInteractive() {
        SecurityUtils.getSession().authenticate("admin", "password");

        final FormValidationsTestInteractive instance = FormsImpl.init(FormValidationsTestInteractive.class);
        ReflectedFormInstance.wrap(instance).getModel().markInitElements();

        final Validations vs1 = validate(instance);

        assertThat(vs1.getAllMessages().size()).isEqualTo(2);

        // Test required validations;
        assertThat(validations(vs1, FormValidationsTestInteractiveBase.Field.FROM)).isEqualTo("Required value.");
        assertThat(validations(vs1, FormValidationsTestInteractiveBase.Field.TO)).isEqualTo("Required value.");

        instance.setFrom(-5);

        final Validations vs2 = validate(instance);
        assertThat(vs2.getAllMessages().size()).isEqualTo(2);

        // Test positive and required validations;
        assertThat(validations(vs2, FormValidationsTestInteractiveBase.Field.FROM)).isEqualTo("Should be positive");
        assertThat(validations(vs2, FormValidationsTestInteractiveBase.Field.TO)).isEqualTo("Required value.");

        instance.setFrom(5);
        instance.setTo(3);

        final Validations vs3 = validate(instance);
        assertThat(vs3.getAllMessages().size()).isEqualTo(1);
        assertThat(validations(vs3, FormValidationsTestInteractiveBase.Field.TO)).isEqualTo("'To' should be greater than 'From'");

        instance.setTo(8);

        final Validations vs4 = validate(instance);
        assertThat(vs4.isEmpty());
    }

    @Test public void formValidationsTestsTable() {
        SecurityUtils.getSession().authenticate("admin", "password");
        final FormValidationsTestTable instance = FormsImpl.init(FormValidationsTestTable.class);
        instance.getTable().add();
        ReflectedFormInstance.wrap(instance).getModel().markInitElements();

        instance.setOptional(false);
        final Validations validationsFailed = validate(instance);
        assertThat(validationsFailed.getAllMessages().size()).isEqualTo(4);

        instance.setOptional(true);
        final Validations validationsPassed = validate(instance);
        assertThat(validationsPassed.getAllMessages().size()).isZero();
    }

    @Test public void formValidationsTestsUser() {
        SecurityUtils.getSession().authenticate("admin", "password");
        final FormValidationsTestDefaults instance = FormsImpl.init(FormValidationsTestDefaults.class);
        ReflectedFormInstance.wrap(instance).getModel().markInitElements();

        // Add user messages;
        instance.message(FormValidationsTestDefaultsBase.Field.ITEM1, "MessageItem1");
        instance.message(FormValidationsTestDefaultsBase.Field.ITEM2, "MessageItem2");
        instance.message(FormValidationsTestDefaultsBase.Field.TOTAL, "MessageTotal");

        final Validations validations = validate(instance);

        // Test user added messages overriding code validations;
        assertThat(validations.getMessages(FormValidationsTestDefaultsBase.Field.ITEM1).size()).isEqualTo(1);
        assertThat(validations.getMessages(FormValidationsTestDefaultsBase.Field.ITEM2).size()).isEqualTo(1);
        assertThat(validations.getMessages(FormValidationsTestDefaultsBase.Field.TOTAL).size()).isEqualTo(1);
        assertThat(validations.getAllMessages().size()).isEqualTo(3);
    }

    private Validations validate(FormInstance<?> instance) {
        final ValidationsImpl validations = new ValidationsImpl();
        ValidationExpressionsEvaluator.validate(ReflectedFormInstance.wrap(instance).getModel(), validations);
        return validations;
    }

    private String validations(Validations validations, Enum<?> field) {
        final Seq<Validations.Validation> messages = validations.getMessages(field);
        assertThat(messages.size()).isEqualTo(1);
        return messages.getFirst().get().getMessage();
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class FormValidationsTest
