
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.database.SqlExecutionLogger;
import tekgenesis.database.SqlStatement;
import tekgenesis.transaction.LeakException;
import tekgenesis.transaction.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;

import static tekgenesis.common.logging.Logger.Level.INFO;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test Statement execution;
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class LogStatementTest {

    //~ Instance Fields ..............................................................................................................................

    @NotNull @Parameter public String dbName     = "";
    private final MyHandler           logHandler = new MyHandler();

    private final DatabaseRule db = new DatabaseRule() {
            final Logger       logger   = SqlExecutionLogger.logger;
            final Logger.Level previous = logger.getLevel();

            @Override protected void before() {
                runInTransaction(() -> {
                    debugJdbc = true;
                    logger.setLevel(INFO);
                    logger.getTargetLogger().addHandler(logHandler);
                    createDatabase(dbName);
                    sqlStatement(
                        "create table TEST (" +
                        "  CODE int primary key, NAME varchar(50)," +
                        "  X_CODE varchar(20), X_NAME varchar(50), " +
                        "  BIG    clob" +
                        ")").execute();
                });
            }

            @Override protected void after() {
                logger.setLevel(previous);
                logger.getTargetLogger().removeHandler(logHandler);
                runInTransaction(() -> sqlStatement("drop table TEST").execute());
            }
        };

    @Rule public TestRule chain = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void basic() {
        assertThat(db.sqlStatement("select count(*) from TEST").getInt()).isZero();
        assertLastLog("select count(*) from \"TEST\"");

        assertThat(db.sqlStatement("select 1 from TEST").getInt()).isEqualTo(null);
        assertLastLog("select 1 from \"TEST\"");

        insert(1, "Hello");
        assertLastLog("insert into \"TEST\" values (`1`,`Hello`,`1`,`Hello`,`Big one`)");

        final Integer c = db.sqlStatement("select max(CODE) from TEST").getInt();
        assertLastLog("select max(\"CODE\") from \"TEST\"");
        assertThat(c).isEqualTo(1);
        for (final LogRecord log : logHandler.logs)
            System.out.println(log.getMessage());
    }

    @Test public void leakTest() {
        insert(1, "Hello");
        insert(2, "Good Bye");

        try(final SqlStatement.Prepared prepared = db.sqlStatement("select CODE from TEST").prepare()) {
            assertThat(prepared.list(Integer.class)).containsExactly(1, 2);
        }

        final SqlStatement.Prepared prepared = db.sqlStatement("select CODE from TEST").prepare();
        assertThat(prepared.list(Integer.class)).containsExactly(1, 2);

        try {
            Transaction.getCurrent().ifPresent(Transaction::commit);
            failBecauseExceptionWasNotThrown(LeakException.class);
        }
        catch (final LeakException e) {
            assertThat(e.getMessage()).startsWith("Resource class tekgenesis.database.SqlStatement$Prepared");
            prepared.close();
        }
    }

    private void assertLastLog(String message) {
        final List<LogRecord> logs = logHandler.logs;
        assertThat(logs.size()).isGreaterThan(1);
        final String logMessage = logs.get(logs.size() - 2).getMessage();
        assertThat(logMessage).isEqualTo(SqlExecutionLogger.EXECUTING + message);
    }

    private int insert(int code, String name) {
        return db.sqlStatement("insert into TEST values (?,?,?,?,?)").onArgs(code, name, code + "", name, "Big one").executeDml();
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Inner Classes ................................................................................................................................

    static class MyHandler extends Handler {
        List<LogRecord> logs = new ArrayList<>();

        @Override public void close() {}

        @Override public void flush() {}

        @Override public void publish(LogRecord record) {
            logs.add(record);
        }
    }
}  // end class LogStatementTest
