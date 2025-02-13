
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.tools.test.*;
import tekgenesis.test.basic.AuditableCustomer;
import tekgenesis.test.basic.DocType;
import tekgenesis.test.basic.Sex;

import static org.assertj.core.api.Assertions.*;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.TestSession.TEST_USER;
import static tekgenesis.common.tools.test.Tests.assertNotNull;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class AuditableTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    private final DbRule     db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule rule = TransactionalRule.into(db);

    @Rule public DbTimeProviderRule timeProvider = new DbTimeProviderRule();

    //~ Methods ......................................................................................................................................

    @Test public void testAuditable() {
        AuditableCustomer.create(DocType.PASS, BigDecimal.valueOf(53246), Sex.M)
            .setFirstName("Optimus")
            .setLastName("Prime")
            .setNickname("Mionca")
            .insert();

        final AuditableCustomer customerInserted = assertNotNull(AuditableCustomer.find(DocType.PASS, BigDecimal.valueOf(53246), Sex.M));
        final DateTime          creationDate     = assertNotNull(customerInserted.getCreationTime());
        assertThat(creationDate).isNotNull();
        assertThat(customerInserted.getCreationUser()).isEqualTo(TEST_USER);
        assertThat(customerInserted.getUpdateTime()).isEqualTo(creationDate);
        assertThat(customerInserted.getUpdateUser()).isEqualTo(TEST_USER);

        timeProvider.increment(1_000);
        customerInserted.setNickname("ilCapo").update();

        final AuditableCustomer customerUpdated = assertNotNull(AuditableCustomer.find(DocType.PASS, BigDecimal.valueOf(53246), Sex.M));

        assertThat(customerUpdated.getCreationTime()).isNotNull();
        assertThat(customerUpdated.getCreationUser()).isEqualTo(TEST_USER);
        final DateTime lastUpdateDate = assertNotNull(customerUpdated.getUpdateTime());
        assertThat(lastUpdateDate.isGreaterThan(creationDate)).isTrue();
        assertThat(customerUpdated.getUpdateUser()).isEqualTo(TEST_USER);

        timeProvider.increment(1_000);
        customerUpdated.setNickname("Poroto").merge();

        final AuditableCustomer customerMerged = assertNotNull(AuditableCustomer.find(DocType.PASS, BigDecimal.valueOf(53246), Sex.M));

        assertThat(customerMerged.getCreationTime()).isNotNull();
        assertThat(customerMerged.getCreationUser()).isEqualTo(TEST_USER);
        final DateTime lastUpdateDate2 = assertNotNull(customerMerged.getUpdateTime());
        assertThat(lastUpdateDate2.isGreaterThan(lastUpdateDate)).isTrue();
        assertThat(customerMerged.getUpdateUser()).isEqualTo(TEST_USER);
    }  // end method testAuditable

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class AuditableTest
