
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.nomm.model.Sequencer;
import tekgenesis.transaction.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class GeneratedPrimaryKeyTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DatabaseRule db = new NoMmDbRule() {
            @Override protected void before() {
                createDatabase(dbName);
                Transaction.getCurrent().ifPresent(Transaction::commit);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void generatedKeyTest() {
        final Sequencer sequencer = new Sequencer();
        runInTransaction(() -> sequencer.setFirstName("First").setLastName("Last").insert());

        final int first = sequencer.getId();
        runInTransaction(t -> {
            assertThat(first).isGreaterThanOrEqualTo(1);
            assertThat(sequencer.getFirstName()).isEqualTo("First");
            assertThat(sequencer.getLastName()).isEqualTo("Last");

            final Sequencer storedSequencer = assertNotNull(Sequencer.find(first));
            assertThat(storedSequencer.getId()).isEqualTo(first);
            assertThat(storedSequencer.getFirstName()).isEqualTo("First");
            assertThat(storedSequencer.getLastName()).isEqualTo("Last");

            final Sequencer rollbackSequencer = new Sequencer();
            rollbackSequencer.setFirstName("First");
            rollbackSequencer.setLastName("Last");
            rollbackSequencer.insert();

            assertThat(rollbackSequencer.getId()).isEqualTo(first + 1);
            assertThat(rollbackSequencer.getFirstName()).isEqualTo("First");
            assertThat(rollbackSequencer.getLastName()).isEqualTo("Last");
            t.abort();
        });

        runInTransaction(() -> {
            final Sequencer nullSequencer = Sequencer.find(first + 1);
            assertThat(nullSequencer).isNull();

            final Sequencer continueSequencer = new Sequencer();
            continueSequencer.setFirstName("First");
            continueSequencer.setLastName("Last");
            continueSequencer.insert();

            assertThat(continueSequencer.getId()).isEqualTo(first + 2);
            assertThat(continueSequencer.getFirstName()).isEqualTo("First");
            assertThat(continueSequencer.getLastName()).isEqualTo("Last");
        });
    }  // end method generatedKeyTest

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class GeneratedPrimaryKeyTest
