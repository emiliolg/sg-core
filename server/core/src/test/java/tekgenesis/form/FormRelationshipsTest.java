
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

import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.samples.form.AutoincrementWithInnerForm;
import tekgenesis.samples.form.g.AutoincrementWithInnerBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class FormRelationshipsTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DbRule db = new DbRule(DbRule.AUTHORIZATION, DbRule.FORM) {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule chain = TransactionalRule.into(db).around(new FormRule());

    //~ Methods ......................................................................................................................................

    @Test public void formRelationshipsTestsAutoincrementWithInner() {
        SecurityUtils.getSession().authenticate("admin", "password");

        final AutoincrementWithInnerForm autoincrementWithInner = FormsImpl.init(AutoincrementWithInnerForm.class);
        autoincrementWithInner.setFieldA("A");
        autoincrementWithInner.create();

        assertThat(autoincrementWithInner.getFieldA()).isEqualTo("A");
        assertThat(AutoincrementWithInnerBase.list().count()).isEqualTo(1);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}
