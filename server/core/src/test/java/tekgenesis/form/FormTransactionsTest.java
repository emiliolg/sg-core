
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.List;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.database.DatabaseConstants;
import tekgenesis.form.exprs.ServerExpressions;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;
import tekgenesis.samples.form.TransactionalEntity;
import tekgenesis.samples.form.TransactionalForm;
import tekgenesis.samples.form.TransactionalFormBase.Field;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.form.exprs.ServerExpressions.*;
import static tekgenesis.samples.form.TransactionalFormBase.Field.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test for {@link ServerExpressions} transactions.
 */
public class FormTransactionsTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCreateCommitOnSuccess() {
        entities().doesNotContain("create-success");
        final FormModel model  = createFormModel("create-success", FALSE, FALSE);
        final Action    action = invokeInTransaction(() -> execCreateOrUpdate(model, widget(model, SAVE)));
        assertThat(action.isError()).isFalse();
        entities().contains("create-success");
    }

    @Test public void testCreateRollbackOnError() {
        entities().doesNotContain("create-error");
        final FormModel model  = createFormModel("create-error", FALSE, TRUE);
        final Action    action = invokeInTransaction(() -> execCreateOrUpdate(model, widget(model, SAVE)));
        assertThat(action.isError()).isTrue();
        entities().doesNotContain("create-error");
    }

    @Test public void testCreateRollbackOnException() {
        entities().doesNotContain("create-exception");
        final FormModel model = createFormModel("create-exception", TRUE, FALSE);
        try {
            runInTransaction(() -> execCreateOrUpdate(model, widget(model, SAVE)));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        }
        catch (final RuntimeException e) {
            assertThat(deepest(e)).hasMessageContaining("Fail");
        }
        entities().doesNotContain("create-exception");
    }

    @Test public void testDeleteCommitOnSuccess() {
        runInTransaction(() -> TransactionalEntity.create("delete-success").insert());

        entities().contains("delete-success");
        final FormModel model  = createFormModel("delete-success", FALSE, FALSE);
        final Action    action = invokeInTransaction(() -> execDelete(model, widget(model, DELETE)));
        assertThat(action.isError()).isFalse();
        entities().doesNotContain("delete-success");
    }

    @Test public void testDeleteRollbackOnError() {
        runInTransaction(() -> TransactionalEntity.create("delete-error").insert());

        entities().contains("delete-error");
        final FormModel model  = createFormModel("delete-error", FALSE, TRUE);
        final Action    action = invokeInTransaction(() -> execDelete(model, widget(model, DELETE)));
        assertThat(action.isError()).isTrue();
        entities().contains("delete-error");
    }

    @Test public void testDeleteRollbackOnException() {
        runInTransaction(() -> TransactionalEntity.create("delete-exception").insert());

        entities().contains("delete-exception");
        final FormModel model = createFormModel("delete-exception", TRUE, FALSE);
        try {
            runInTransaction(() -> execDelete(model, widget(model, DELETE)));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        }
        catch (final RuntimeException e) {
            assertThat(deepest(e)).hasMessageContaining("Fail");
        }
        entities().contains("delete-exception");
    }

    @Test public void testSyncCommitOnSuccess() {
        entities().doesNotContain("sync-success");
        final FormModel model  = createFormModel("sync-success", FALSE, FALSE);
        final Action    action = invokeInTransaction(() -> execOnClick(model, widget(model, BUTTON), empty()));
        assertThat(action.isError()).isFalse();
        entities().contains("sync-success");
    }

    @Test public void testSyncRollbackOnError() {
        entities().doesNotContain("sync-error");
        final FormModel model  = createFormModel("sync-error", FALSE, TRUE);
        final Action    action = invokeInTransaction(() -> execOnClick(model, widget(model, BUTTON), empty()));
        assertThat(action.isError()).isTrue();
        entities().doesNotContain("sync-error");
    }

    @Test public void testSyncRollbackOnException() {
        entities().doesNotContain("sync-exception");
        final FormModel model = createFormModel("sync-exception", TRUE, FALSE);
        try {
            runInTransaction(() -> execOnClick(model, widget(model, BUTTON), empty()));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        }
        catch (final RuntimeException e) {
            assertThat(deepest(e)).hasMessageContaining("Fail");
        }
        entities().doesNotContain("sync-exception");
    }

    @NotNull private FormModel createFormModel(@NotNull String value, boolean fail, boolean error) {
        final Form      form  =  //
                                Context.getSingleton(ModelRepository.class).getModel(createQName(TransactionalForm.class), Form.class).get();
        final FormModel model = new FormModel(form);
        model.set(form.getWidget(KEY.id()).get(), value);
        model.set(form.getWidget(FAIL.id()).get(), fail);
        model.set(form.getWidget(ERROR.id()).get(), error);
        return model;
    }

    /** Finds deepest exception. */
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private Throwable deepest(Throwable cause) {
        return cause.getCause() != null ? deepest(cause.getCause()) : cause;
    }

    @NotNull
    @SuppressWarnings("TypeParameterExtendsFinalClass")
    private AbstractListAssert<?, List<? extends String>, String, ObjectAssert<String>> entities() {
        return assertThat(invokeInTransaction(() -> TransactionalEntity.list().toList())).extracting("key", String.class);
    }

    @NotNull private SourceWidget widget(FormModel model, Field field) {
        final Widget widget = model.metadata().getWidget(field.id()).get();
        return model.indexed(widget, empty()).toSourceWidget();
    }

    //~ Static Fields ................................................................................................................................

    public static final DbRule db = new DbRule(DbRule.AUTHORIZATION, DbRule.FORM) {
            @Override protected void before() {
                createDatabase(DatabaseConstants.MEM);
            }
        };

    @ClassRule public static final TestRule chain = db.around(new FormRule());
}  // end class FormTransactionsTest
