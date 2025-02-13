
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.showcase.SomeNumberForm;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.form.FormTests.initialize;
import static tekgenesis.form.extension.FormListenerType.AFTER_CANCEL;
import static tekgenesis.form.extension.FormListenerType.AFTER_CREATE;
import static tekgenesis.form.extension.FormListenerType.AFTER_LOAD;
import static tekgenesis.form.extension.FormListenerType.AFTER_PERSIST;
import static tekgenesis.form.extension.FormListenerType.BEFORE_CANCEL;
import static tekgenesis.form.extension.FormListenerType.BEFORE_CREATE;
import static tekgenesis.form.extension.FormListenerType.BEFORE_LOAD;
import static tekgenesis.form.extension.FormListenerType.BEFORE_PERSIST;

@RunWith(Parameterized.class)
@SuppressWarnings("JavaDoc")
public class FormListenerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    public DbRule db = new DbRule(DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    private final Map<FormListenerType, Integer> callCounts = new EnumMap<>(FormListenerType.class);

    //~ Methods ......................................................................................................................................

    @Before public void beforeEach() {
        callCounts.clear();
    }

    @Test public void testCancelListeners() {
        assertCount(BEFORE_LOAD, 0);
        assertCount(AFTER_LOAD, 0);

        final FormListener<SomeNumberForm> beforeLoad   = addListener(BEFORE_LOAD);
        final FormListener<SomeNumberForm> afterLoad    = addListener(AFTER_LOAD);
        final FormListener<SomeNumberForm> beforeCancel = addListener(BEFORE_CANCEL);
        final FormListener<SomeNumberForm> afterCancel  = addListener(AFTER_CANCEL);

        assertThat(callCounts).isEmpty();

        final SomeNumberForm form = initialize(SomeNumberForm.class);
        form.setSomeNumber(1.0);

        assertCount(BEFORE_LOAD, 1);
        assertCount(AFTER_LOAD, 1);

        ReflectedFormInstance.wrap(form).cancel();

        assertCount(AFTER_CANCEL, 1);
        assertCount(BEFORE_CANCEL, 1);

        SomeNumberForm.removeListener(BEFORE_LOAD, beforeLoad);
        SomeNumberForm.removeListener(AFTER_LOAD, afterLoad);
        SomeNumberForm.removeListener(BEFORE_CANCEL, beforeCancel);
        SomeNumberForm.removeListener(AFTER_CANCEL, afterCancel);
    }

    @Test public void testCreateListeners() {
        assertCount(BEFORE_LOAD, 0);
        assertCount(AFTER_LOAD, 0);

        final FormListener<SomeNumberForm> beforeLoad    = addListener(BEFORE_LOAD);
        final FormListener<SomeNumberForm> afterLoad     = addListener(AFTER_LOAD);
        final FormListener<SomeNumberForm> beforePersist = addListener(BEFORE_PERSIST);
        final FormListener<SomeNumberForm> afterPersist  = addListener(AFTER_CREATE);
        final FormListener<SomeNumberForm> beforeCreate  = addListener(BEFORE_CREATE);
        final FormListener<SomeNumberForm> afterCreate   = addListener(AFTER_PERSIST);

        assertThat(callCounts).isEmpty();

        final SomeNumberForm form = initialize(SomeNumberForm.class);
        form.setSomeNumber(1.0);

        assertCount(BEFORE_LOAD, 1);
        assertCount(AFTER_LOAD, 1);

        ReflectedFormInstance.wrap(form).create();

        assertCount(BEFORE_PERSIST, 1);
        assertCount(AFTER_PERSIST, 1);
        assertCount(BEFORE_CREATE, 1);
        assertCount(AFTER_CREATE, 1);

        SomeNumberForm.removeListener(BEFORE_LOAD, beforeLoad);
        SomeNumberForm.removeListener(AFTER_LOAD, afterLoad);
        SomeNumberForm.removeListener(BEFORE_PERSIST, beforePersist);
        SomeNumberForm.removeListener(AFTER_PERSIST, afterPersist);
        SomeNumberForm.removeListener(BEFORE_CREATE, beforeCreate);
        SomeNumberForm.removeListener(AFTER_CREATE, afterCreate);
    }

    @NotNull private FormListener<SomeNumberForm> addListener(final FormListenerType type) {
        final FormListener<SomeNumberForm> fn = instance -> count(type);
        SomeNumberForm.addListener(type, fn);
        return fn;
    }

    private void assertCount(final FormListenerType type, int expected) {
        assertThat(notNull(callCounts.get(type), 0)).isEqualTo(expected);
    }

    private void count(final FormListenerType t) {
        callCounts.put(t, notNull(callCounts.get(t), 0) + 1);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class FormListenerTest
