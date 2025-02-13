
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.test;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.common.util.Files;
import tekgenesis.database.Database;
import tekgenesis.database.MetaDataEntry;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.exception.EvolutionException;
import tekgenesis.database.hikari.HikariDatabaseConfig;
import tekgenesis.database.hikari.HikariDatabaseFactory;
import tekgenesis.transaction.JDBCTransactionManager;
import tekgenesis.transaction.TransactionManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.database.MetaDataEntry.dbVersion;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test Database evolution;
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class DbEvolveTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public MuteRule                mute             = new MuteRule(SqlStatement.class, JDBCTransactionManager.class);
    File                                 classesTargetDir = null;
    HikariDatabaseConfig                 config           = null;
    Database                             db               = null;
    final MemoryEnvironment              env              = new MemoryEnvironment();
    private final JDBCTransactionManager tm               = new JDBCTransactionManager();
    final HikariDatabaseFactory          dbFactory        = new HikariDatabaseFactory(env, tm);
    File                                 dbTargetDir      = null;

    @Rule public ExternalResource r = new ExternalResource() {
            @Override protected void before()
                throws Throwable
            {
                Context.getContext().setSingleton(TransactionManager.class, tm);
                db               = DbTests.createDatabase(dbFactory, dbName, "TESTER");
                config           = DbTests.configurationFor(dbName);
                classesTargetDir = new File("target/db/base/classes/test");
                dbTargetDir      = new File(classesTargetDir, "db");
                Files.remove(dbTargetDir);
            }

            @Override protected void after() {
                db.close();
                env.dispose();
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void createAndEvolve()
        throws IOException
    {
        step1();
        Files.remove(dbTargetDir);
        step2();
        Files.remove(dbTargetDir);
        step3();
    }  // end method createAndEvolve

    @Test public void createAndEvolveFailingLast()
        throws IOException
    {
        step1();
        step4();
    }

    @Test public void createAndEvolveSkippingVersion()
        throws IOException
    {
        step1();
        step3();
    }

    @Test public void createAndRetest()
        throws IOException
    {
        step1();
        step1_2();
        step1_2();
    }

    private void checkMetadata(final String expectedVersion, final String expectedSha, boolean checkSql) {
        final MetaDataEntry metadata = assertNotNull(dbVersion(db, "tester", true));
        assertThat(metadata.getVersion().toString()).isEqualTo(expectedVersion);
        assertThat(metadata.getSha()).isEqualTo(expectedSha);

        if (checkSql) Tests.checkDiff(new File(classesTargetDir, "db/current/tester.sql"), assertNotNull(metadata.getSchemaSql()).getReader());
    }

    private void copyDbDefinition(final String step)
        throws IOException
    {
        Files.copyDirectory(new File("db/base/src/test/resources/", step), classesTargetDir);
    }

    private void initializeAndCheck(final String step, final String expectedVersion, final String expectedSha)
        throws IOException
    {
        copyDbDefinition(step);
        dbFactory.initialize(dbName, config, false, "tester");
        checkMetadata(expectedVersion, expectedSha, true);
    }

    private void initializeFailAndCheck(final String step, final String expectedVersion, String expectedSha)
        throws IOException
    {
        copyDbDefinition(step);
        try {
            dbFactory.initialize(dbName, config, false, "tester");
            failBecauseExceptionWasNotThrown(EvolutionException.class);
        }
        catch (final EvolutionException e) {
            assertThat(e.getMessage()).isEqualTo("Exception while evolving 'TESTER' to version '4.0'");
        }
        checkMetadata(expectedVersion, expectedSha, false);
    }

    private void step1()
        throws IOException
    {
        initializeAndCheck("step1", "1.0", "b367e0e86b2799f458d29ebde608f7d4f7f1f8540e68b3f5554dc882a41a2fc5");
    }

    private void step1_2()
        throws IOException
    {
        initializeAndCheck("step1_2", "1.0", "41f4182faa956e46a71ed27247a0dc0d329a6433961381157614f04fc018697a");
    }

    private void step2()
        throws IOException
    {
        initializeAndCheck("step2", "2.0", "b36c771fa6a0b982815c9207fbdc583f9fb372713f6941720af964babefbaf2f");
        runInTransaction(() -> db.sqlStatement("insert into QName(TESTER, ADD_TABLE) values ('1', '1', '1')").executeDml());
    }

    private void step3()
        throws IOException
    {
        initializeAndCheck("step3", "3.0", "3f40d0e67bde2debcd45abad10a45b20e8a0d6ebee27af33eade95d767df8a29");
        runInTransaction(() -> db.sqlStatement("insert into QName(TESTER, ADD_TABLE) values ('2', '1', '1', 1)").executeDml());
    }

    private void step4()
        throws IOException
    {
        initializeFailAndCheck("step4", "3.0", "3f40d0e67bde2debcd45abad10a45b20e8a0d6ebee27af33eade95d767df8a29");
        runInTransaction(() -> db.sqlStatement("insert into QName(TESTER, ADD_TABLE) values ('2', '1', '1', 1)").executeDml());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class DbEvolveTest
