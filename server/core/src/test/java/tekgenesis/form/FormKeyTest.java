
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
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.samples.form.AutoincrementWithMultiple;
import tekgenesis.samples.form.BaseEntitiesWithKeysForm;
import tekgenesis.samples.form.BaseWithKey;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class FormKeyTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DbRule db = new DbRule(DbRule.AUTHORIZATION, DbRule.FORM) {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule chain = TransactionalRule.into(db).around(new FormRule());

    //~ Methods ......................................................................................................................................

    @Test public void formKeyAsStringTests() {
        SecurityUtils.getSession().authenticate("admin", "password");

        final BaseEntitiesWithKeysForm  form                       = FormsImpl.init(BaseEntitiesWithKeysForm.class);
        final AutoincrementWithMultiple autoincrementWithMultipleA = AutoincrementWithMultiple.create().setFieldD("D1");
        final AutoincrementWithMultiple autoincrementWithMultipleB = AutoincrementWithMultiple.create().setFieldD("D2");
        autoincrementWithMultipleA.insert();
        autoincrementWithMultipleB.insert();

        // Checking keyAsString without composed keys
        final BaseWithKey baseA1 = BaseWithKey.create("A1", "A2").setFieldA("FieldA").setWithKeyBackRef(autoincrementWithMultipleA);
        final BaseWithKey baseB1 = BaseWithKey.create("B1", "B2").setFieldA("FieldB").setWithKeyBackRef(autoincrementWithMultipleB);
        baseA1.insert();
        baseB1.insert();

        form.setEntA(baseA1);
        form.setEntB(baseB1);
        form.create();

        assertThat(form.keyAsString()).isEqualTo("A1:A2:B1:B2");

        // Checking keyAsString with all keys composed
        final BaseWithKey baseA = BaseWithKey.create("A1:A3", "A2:A4").setFieldA("FieldA").setWithKeyBackRef(autoincrementWithMultipleA);
        final BaseWithKey baseB = BaseWithKey.create("B1:B3", "B2:B4").setFieldA("FieldB").setWithKeyBackRef(autoincrementWithMultipleB);
        baseA.insert();
        baseB.insert();

        form.setEntA(baseA);
        form.setEntB(baseB);
        form.create();

        assertThat(form.keyAsString()).isEqualTo("A1\\:A3:A2\\:A4:B1\\:B3:B2\\:B4");

        // Checking keyAsString with mixed composed keys
        final BaseWithKey baseA2 = BaseWithKey.create("A1", "A2:A3:A4").setFieldA("FieldA").setWithKeyBackRef(autoincrementWithMultipleA);
        final BaseWithKey baseB2 = BaseWithKey.create("B1:B3", "B2").setFieldA("FieldB").setWithKeyBackRef(autoincrementWithMultipleB);
        baseA2.insert();
        baseB2.insert();

        form.setEntA(baseA2);
        form.setEntB(baseB2);
        form.create();

        assertThat(form.keyAsString()).isEqualTo("A1:A2\\:A3\\:A4:B1\\:B3:B2");
    }  // end method formKeyAsStringTests

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class FormKeyTest
