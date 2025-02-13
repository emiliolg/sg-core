
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.test;

import java.util.EnumSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.nomm.model.Address;
import tekgenesis.persistence.nomm.model.Participant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.persistence.nomm.model.Country.*;
import static tekgenesis.persistence.nomm.model.g.AddressForUpdateBase.addressForUpdate;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
public class TransactionTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public final DatabaseRule db = new NoMmDbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void testNestedTransaction() {
        runInTransaction(t -> {
            Participant.create(1).setFirstName("Juan").setLastName("Pérez").setNationalities(EnumSet.of(ARGENTINA, SPAIN)).insert();

            db.getTransactionManager().invokeInNestedTransaction(t1 -> {
                addressForUpdate("1234").setRoom("123").setStreet("123").insert();
                assertThat(Address.find("1234")).isNotNull();
                return 0;
            });
            assertThat(Participant.find(1)).isNotNull();
            t.abort();
        });
        runInTransaction(() -> {
            assertThat(Participant.find(1)).isNull();
            assertThat(Address.find("1234")).isNotNull();
        });
    }
    @Test public void testSpouriousTransaction() {
        runInTransaction(t -> {
            Participant.create(1).setFirstName("Juan").setLastName("Pérez").setNationalities(EnumSet.of(ARGENTINA, SPAIN)).insert();
            t.commit();
            Participant.create(2).setFirstName("Juan").setLastName("Pérez").setNationalities(EnumSet.of(ARGENTINA, SPAIN)).insert();
        });
        runInTransaction(() -> {
            assertThat(Participant.find(1)).isNotNull();
            assertThat(Participant.find(2)).isNotNull();
        });
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class TransactionTest
