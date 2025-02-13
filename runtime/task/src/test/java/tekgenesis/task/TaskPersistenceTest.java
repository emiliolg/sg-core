
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.tools.test.*;
import tekgenesis.common.tools.test.DbTimeProviderRule;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.exception.DatabaseExecutionCanceledException;
import tekgenesis.persistence.Criteria;
import tekgenesis.sg.TaskEntry;
import tekgenesis.task.exception.TaskAlreadyRunningException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.sg.TaskEntry.nextTaskToExecute;
import static tekgenesis.sg.TaskEntry.startExecution;
import static tekgenesis.sg.g.TaskEntryTable.TASK_ENTRY;
import static tekgenesis.task.TaskStatus.NOT_SCHEDULED;
import static tekgenesis.task.TaskStatus.SCHEDULED;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test different task persistence methods.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "MagicNumber" })
public class TaskPersistenceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String     dbName     = null;
    private final ConcurrentRule concurrent = new ConcurrentRule();

    private final Criteria criteria = TASK_ENTRY.STATUS.eq(SCHEDULED).and(TASK_ENTRY.NAME.notLike("%:%"));
    private final DbRule   db       = new DbRule(DbRule.SG) {
            @Override protected void before() {
                createDatabase(dbName);
            }

            @Override protected void doCreateDatabase() {
                if (db.getDatabaseType() != DatabaseType.POSTGRES) super.doCreateDatabase();
                else {
                    final DatabaseType t = getDatabaseType();
                    try {
                        t.createDatabase(getDatabase());
                    }
                    catch (final Exception ignore) {}
                }
            }
        };

    private final DbTimeProviderRule time  = new DbTimeProviderRule();
    @Rule public TestRule            chain = db.around(time).around(concurrent);

    private SqlStatement.Terminator currentStatement = null;

    //~ Methods ......................................................................................................................................

    @Test public void concurrentNextTaskTest() {
        createSomeTasks();
        final ConcurrentRule.Exchanger<String> x        = concurrent.createExchanger();
        final ConcurrentRule.Barrier           barrier1 = concurrent.createBarrier();
        final ConcurrentRule.Barrier           barrier2 = concurrent.createBarrier();

        concurrent.add(n -> {
            final String first = nextTaskToExecute("that", criteria).getRight();
            assertThat(first).isIn("TEST1", "TEST2");
            assertThat(first).isNotEqualTo(x.exchange(first));

            assertThat(nextTaskToExecute("this", criteria).getLeft()).isEqualTo(5_000);

            time.increment(9_000);
            barrier1.await();

            barrier2.await();
            assertThat(nextTaskToExecute("this", criteria).getRight()).isEqualTo("TEST4");
        });
        concurrent.add(n -> {
            final String first = nextTaskToExecute("this", criteria).getRight();
            assertThat(first).isIn("TEST1", "TEST2");
            assertThat(first).isNotEqualTo(x.exchange(first));

            barrier1.await();

            assertThat(nextTaskToExecute("this", criteria).getRight()).isEqualTo("TEST3");
            time.increment(1_000);
            barrier2.await();
        });
    }
    @Category(LrgTests.class)
    @Test public void lockTest() {
        if (db.getDatabaseType().getType() == DatabaseType.HSQLDB) return;
        createSomeTasks();
        final ConcurrentRule.Barrier barrier = concurrent.createBarrier();

        concurrent.add(n ->
                runInTransaction(() -> {
                    final TaskEntry te = assertNotNull(TaskEntry.find("TEST1"));
                    te.update();
                    barrier.await();
                    concurrent.doWait(1_000);
                    if (currentStatement != null) currentStatement.cancel();
                    barrier.await();
                }));
        concurrent.add(n ->
                runInTransaction(() -> {
                    SqlStatement.addCurrentStatementListener(statement -> currentStatement = statement);
                    final TaskEntry te = assertNotNull(TaskEntry.find("TEST1"));
                    barrier.await();
                    try {
                        te.update();
                        failBecauseExceptionWasNotThrown(DatabaseExecutionCanceledException.class);
                    }
                    catch (final DatabaseExecutionCanceledException ignored) {}
                    barrier.await();
                }));
    }

    @Test public void nextTaskTest() {
        createSomeTasks();

        assertThat(nextTaskToExecute("this", criteria).getRight()).isEqualTo("TEST1");
        assertThat(nextTaskToExecute("this", criteria).getRight()).isEqualTo("TEST2");
        assertThat(nextTaskToExecute("this", criteria).getLeft()).isEqualTo(5_000);
        time.increment(9_000);
        assertThat(nextTaskToExecute("this", criteria).getRight()).isEqualTo("TEST3");
        assertThat(nextTaskToExecute("this", criteria).getLeft()).isEqualTo(1_000);
    }

    @Test public void startExecutionTest() {
        createSomeTasks();
        assertThat(startExecution("TEST1", "this", TaskProps.DEFAULT_MAX_RUNNING_TIME)).isNotNull();
        try {
            startExecution("TEST1", "this", TaskProps.DEFAULT_MAX_RUNNING_TIME);
            failBecauseExceptionWasNotThrown(TaskAlreadyRunningException.class);
        }
        catch (final TaskAlreadyRunningException ignore) {}
        assertThat(startExecution("TEST5", "this", TaskProps.DEFAULT_MAX_RUNNING_TIME)).isNotNull();
    }

    private void createSomeTasks() {
        final DateTime ts = DateTime.current();
        runInTransaction(() -> {
            createTask("TEST", NOT_SCHEDULED, ts.addSeconds(-10));
            createTask("TEST1", SCHEDULED, ts);
            createTask("TEST2", SCHEDULED, ts);
            createTask("TEST3", SCHEDULED, ts.addSeconds(5));
            createTask("TEST4", SCHEDULED, ts.addSeconds(10));
        });
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static void createTask(String name, TaskStatus status, DateTime dueTime) {
        TaskEntry.create(name).setStatus(status).setDueTime(dueTime).insert();
    }
}  // end class TaskPersistenceTest
