
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.DbTimeProviderRule;
import tekgenesis.database.DatabaseType;
import tekgenesis.metadata.task.TaskConstants;
import tekgenesis.persistence.TableFactory;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.test.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.test.IsolatedTask.IsolatedStatus.*;
import static tekgenesis.test.TestTask.createTestTask;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
@SuppressWarnings("ClassWithTooManyMethods")
public class TaskServiceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DbTimeProviderRule timer       = new DbTimeProviderRule();
    private TaskServiceForTest      taskService = null;

    private final DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION, DbRule.TASK_TEST, DbRule.VIEWS) {
            @Override protected void after() {
                if (taskService != null) {
                    taskService.getServiceManager().shutdown();
                    taskService.doShutdown();
                }
                super.after();
            }

            @Override protected void before() {
                createDatabase(dbName);
            }

            @Override protected void initTableFactory() {
                taskService = new TaskServiceForTest(env, timer);
                TableFactory.setFactory(new TableFactory(createStoreHandlerFactory()));
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

    @Rule public RuleChain chain = RuleChain.outerRule(timer).around(db);

    private int executions = 0;

    //~ Methods ......................................................................................................................................

    @Test public void bigErrorMsg() {
        final ScheduledTask testTask = new ScheduledTask(ErrorTask.class);
        final ErrorTask     instance = testTask.getInstance();
        instance.setErrorMsg("Normal msg error");
        taskService.submitForTest(testTask);
        assertTaskEntry(ErrorTask.class, TaskStatus.FAILED);

        final ScheduledTask erroTask     = new ScheduledTask(ErrorTask.class);
        final ErrorTask     bigErrorTask = erroTask.getInstance();

        final char[] chars = new char[2000];
        Arrays.fill(chars, '*');

        bigErrorTask.setErrorMsg(new String(chars));
        taskService.submitForTest(erroTask);
        assertTaskEntry(ErrorTask.class, TaskStatus.FAILED);
    }

    @Test public void dataStoreInTask() {
        final DateTime      d    = DateTime.current();
        final ScheduledTask task = createTestTask(t -> {
                assertThat(t.getData()).isEqualTo("");
                assertThat(t.getDataTime()).isNull();
                t.setData("Hello world");
                t.setDataTime(d);
                executions++;
                return Status.done();
            });

        final Status status = taskService.submitForTest(task);

        assertThat(executions).isEqualTo(1);
        assertThat(status.isSuccess()).isTrue();

        final ScheduledTask task2 = createTestTask(t -> {
                assertThat(t.getData()).isEqualTo("Hello world");
                assertThat(t.getDataTime()).isEqualTo(d);
                return Status.done();
            });
        taskService.submitForTest(task2);
    }

    @Test public void extendedProcessorTaskAfterAbort() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, null, Status.abort("Abort Task"), 0, TaskStatus.FAILED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, null, Status.abort("Abort Task"), 3, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.ok(), Status.abort("Abort Task"), 23, TaskStatus.FINISHED);
    }

    @Test public void extendedProcessorTaskAfterDone() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, null, Status.done(), 10, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, null, Status.done(), 10, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.ok(), Status.done(), 10, TaskStatus.FINISHED);
    }

    @Test public void extendedProcessorTaskAfterError() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, null, Status.error("Abort task with Error status"), 0, TaskStatus.FAILED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, null, Status.error("Abort task with Error status"), 3, TaskStatus.FAILED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class,
            Status.ok(),
            Status.error("Abort task with Error status"),
            23,
            TaskStatus.FAILED);
    }

    @Test public void extendedProcessorTaskAfterOk() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, null, Status.ok(), 10, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, null, Status.ok(), 10, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.ok(), Status.ok(), 10, TaskStatus.FINISHED);
    }

    @Test public void extendedProcessorTaskBeforeAbort() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, Status.abort("Abort Task"), null, 0, TaskStatus.FAILED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.abort("Abort Task"), null, 0, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.abort("Abort Task"), Status.ok(), 0, TaskStatus.FINISHED);
    }

    @Test public void extendedProcessorTaskBeforeDone() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, Status.done(), null, 20, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.done(), null, 20, TaskStatus.FINISHED);
    }

    @Test public void extendedProcessorTaskBeforeError() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, Status.error("Abort task with Error status"), null, 0, TaskStatus.FAILED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.error("Abort task with Error status"), null, 3, TaskStatus.FINISHED);
    }

    @Test public void extendedProcessorTaskBeforeOk() {
        assertExtendedTaskProcessor(ExtendedProcessorAllTask.class, Status.ok(), null, 23, TaskStatus.FINISHED);
        assertExtendedTaskProcessor(ExtendedProcessorIsolatedTask.class, Status.ok(), null, 23, TaskStatus.FINISHED);
    }

    @Test public void isolated() {
        final ScheduledTask testTask = new ScheduledTask(IsolatedTask.class);
        final IsolatedTask  instance = testTask.getInstance();
        final int           id       = getIsolated();
        instance.init(listOf(OK, OK, EXCEPTION, FAIL, OK, OK), id);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(4);
        assertThat(isolatedCount(id)).isEqualTo(4);
    }

    @Test public void isolatedAbort() {
        final ScheduledTask testTask = new ScheduledTask(IsolatedTask.class);
        final IsolatedTask  instance = testTask.getInstance();
        final int           id       = getIsolated();
        instance.init(listOf(OK, OK, FAIL, ABORT, FAIL, OK, OK), id);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(2);
        assertThat(isolatedCount(id)).isEqualTo(2);
        assertTaskEntry(IsolatedTask.class, TaskStatus.FINISHED);
    }

    @Test public void isolatedDone() {
        final ScheduledTask testTask = new ScheduledTask(IsolatedTask.class);
        final IsolatedTask  instance = testTask.getInstance();
        final int           id       = getIsolated();
        instance.init(listOf(OK, OK, FAIL, EXCEPTION, DONE, FAIL, OK, OK), id);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(3);
        assertThat(isolatedCount(id)).isEqualTo(3);

        assertTaskEntry(IsolatedTask.class, TaskStatus.FINISHED);
    }

    @Test public void isolatedForceCommit() {
        final ScheduledTask testTask = new ScheduledTask(IsolatedTask.class);
        final IsolatedTask  instance = testTask.getInstance();
        final int           id       = getIsolated();
        instance.init(listOf(OK, TX_FORCE_COMMIT), id);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(2);
        assertThat(isolatedCount(id)).isEqualTo(2);
    }

    @Test public void isolatedForceRollback() {
        final ScheduledTask testTask = new ScheduledTask(IsolatedTask.class);
        final IsolatedTask  instance = testTask.getInstance();
        final int           id       = getIsolated();
        instance.init(listOf(OK, TX_FORCE_COMMIT), id);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(2);
        assertThat(isolatedCount(id)).isEqualTo(2);
    }

    @Test public void processor() {
        final ScheduledTask testTask = new ScheduledTask(MyProcessorTask.class);
        taskService.submitForTest(testTask);
        final MyProcessorTask instance = testTask.getInstance();
        assertThat(instance.processed).isEqualTo(100);
        assertThat(instanceData(instance)).isEqualTo("100");

        assertTaskEntry(MyProcessorTask.class, TaskStatus.FINISHED, 100, 100, 0, 0);
    }

    @Test public void processorAbort() {
        final ScheduledTask   testTask = new ScheduledTask(MyProcessorTask.class);
        final MyProcessorTask instance = testTask.getInstance();
        instance.setMustAbort(true);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(55);
        assertThat(instanceData(instance)).isEqualTo("50");

        assertTaskEntry(MyProcessorTask.class, TaskStatus.FAILED, 100, 55, 1, 0);
    }

    @Test public void processorAbortAll() {
        final ScheduledTask      testTask = new ScheduledTask(MyProcessorAllTask.class);
        final MyProcessorAllTask instance = testTask.getInstance();
        instance.setMustAbort(true);
        taskService.submitForTest(testTask);

        assertThat(instance.processed).isEqualTo(55);
        assertThat(instanceData(instance)).isEqualTo("");

        assertTaskEntry(MyProcessorAllTask.class, TaskStatus.FAILED, 100, 55, 1, 0);
    }

    @Test public void processorAll() {
        final ScheduledTask      testTask = new ScheduledTask(MyProcessorAllTask.class);
        final MyProcessorAllTask instance = testTask.getInstance();
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(100);
        assertThat(instanceData(instance)).isEqualTo("100");

        assertTaskEntry(MyProcessorAllTask.class, TaskStatus.FINISHED, 100, 100, 0, 0);
    }

    @Test public void processorAllDone() {
        final ScheduledTask      testTask = new ScheduledTask(MyProcessorAllTask.class);
        final MyProcessorAllTask instance = testTask.getInstance();
        instance.setDone(true);

        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(55);
        assertThat(instanceData(instance)).isEqualTo("55");

        assertTaskEntry(MyProcessorAllTask.class, TaskStatus.FINISHED, 100, 55, 0, 0);
    }
    @Test public void processorAllError() {
        final ScheduledTask      testTask = new ScheduledTask(MyProcessorAllTask.class);
        final MyProcessorAllTask instance = testTask.getInstance();
        instance.setError(true);

        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(100);
        assertThat(instanceData(instance)).isEqualTo("100");

        assertTaskEntry(MyProcessorAllTask.class, TaskStatus.FINISHED, 100, 99, 1, 0);
    }

    @Test public void processorIgnore() {
        final ScheduledTask   testTask = new ScheduledTask(MyProcessorTask.class);
        final MyProcessorTask instance = testTask.getInstance();
        instance.setMustIgnore(true);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(60);
        assertThat(instanceData(instance)).isEqualTo("60");

        assertTaskEntry(MyProcessorTask.class, TaskStatus.FINISHED, 100, 60, 0, 40);
    }

    @Test public void processorUndo() {
        final ScheduledTask   testTask = new ScheduledTask(MyProcessorTask.class);
        final MyProcessorTask instance = testTask.getInstance();
        instance.setMustUndo(true);
        taskService.submitForTest(testTask);
        assertThat(instance.processed).isEqualTo(70);
        assertThat(instanceData(instance)).isEqualTo("70");

        assertTaskEntry(MyProcessorTask.class, TaskStatus.FINISHED, 100, 70, 0, 1);
    }

    @Test public void processorWithInstanceValue() {
        final ScheduledTask testTask = new ScheduledTask(MyProcessorTask.class);
        taskService.submitForTest(testTask, 1);
        final MyProcessorTask instance = testTask.getInstance();
        assertThat(instance.processed).isEqualTo(1);
        assertThat(instanceData(instance)).isEqualTo("1");

        assertTaskEntry(MyProcessorTask.class, TaskStatus.FINISHED);
    }

    @Test public void scheduledComplexCronExpression() {
        final ScheduledTask task = createTestTask("0 2-9,12-19,22-29,32-39,42-49,52-59",
                t -> {
                    t.setDataTime(DateTime.current());
                    executions++;
                    return Status.done();
                });

        taskService.scheduleForTest(task);
    }

    @Test public void scheduledSimple() {
        final ScheduledTask task = createTestTask("0/1", t -> {
                    t.setDataTime(DateTime.current());
                    executions++;
                    return Status.done();
                });

        taskService.scheduleForTest(task);
        assertThat(executions).isEqualTo(0);
        taskService.increment(2, 1);
        assertThat(executions).isEqualTo(2);
        taskService.stop();
        assertThat(executions).isEqualTo(2);
    }

    @Test public void scheduleMultipleAtATime() {
        final int[]         exs   = new int[2];
        final ScheduledTask task1 = createTestTask("0/1", t -> {
                    exs[0]++;
                    return Status.done();
                });
        final ScheduledTask task2 = createTestTask(TestTask2.class, "0/2", t -> {
                    exs[1]++;
                    return Status.done();
                });
        taskService.scheduleForTest(task1, task2);
        assertThat(exs[0]).isEqualTo(0);
        assertThat(exs[1]).isEqualTo(0);
        taskService.increment(2, 1);
        assertThat(exs[0]).isEqualTo(2);
        assertThat(exs[1]).isEqualTo(1);
        taskService.stop();
    }

    @Test public void scheduleSimpleAbortExecution() {
        final ScheduledTask task = createTestTask("0/1", t -> {
                    // Mark execution as abort
                    executions++;
                    return Status.abort("");
                });

        taskService.scheduleForTest(task);
        assertThat(executions).isEqualTo(0);
        taskService.increment(2, 1);
        assertThat(executions).isEqualTo(2);
        taskService.stop();
        assertThat(executions).isEqualTo(2);

        assertTaskEntry(TestTask.class, TaskStatus.SCHEDULED);
    }

    @Test public void scheduleSlowAndFast() {
        final int[]         exs   = new int[2];
        final ScheduledTask task1 = createTestTask("1/1", t -> {
                    exs[0]++;
                    timer.increment(2_000);
                    return Status.done();
                });
        final ScheduledTask task2 = createTestTask(TestTask2.class, "1/3", t -> {
                    exs[1]++;
                    return Status.done();
                });
        taskService.scheduleForTest(task1, task2);
        assertThat(exs[0]).isEqualTo(0);
        assertThat(exs[1]).isEqualTo(0);
        taskService.increment(2, 1);
        assertThat(exs[0]).isEqualTo(2);
        assertThat(exs[1]).isEqualTo(2);
        taskService.stop();
    }

    @Test(expected = IllegalArgumentException.class)
    public void scheduleTaskWithInstanceValue() {
        final ScheduledTask testTask = new ScheduledTask(TestTask.class);
        taskService.submitForTest(testTask, 1);
    }
    @Test public void stopTaskProcessor() {
        final ScheduledTask testTask = new ScheduledTask(InfiniteTask.class);

        taskService.submitForTestAsync(testTask);

        try {
            Thread.sleep(500);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        testTask.cancel(true);
        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        assertThat(taskService.runningTasks()).isEmpty();
    }

    @Test public void stopTaskSql() {
        if (db.getDatabaseType().getType() == DatabaseType.HSQLDB) return;
        final ScheduledTask testTask = new ScheduledTask(SlowSqlTask.class);

        taskService.submitForTestAsync(testTask);
        try {
            Thread.sleep(500);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        testTask.cancel(true);
        try {
            Thread.sleep(500);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        assertThat(taskService.runningTasks()).isEmpty();
    }
    @Test public void stopWaitingTask() {
        final ScheduledTask testTask = new ScheduledTask(WaitingTask.class);

        taskService.submitForTestAsync(testTask);

        try {
            Thread.sleep(500);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        testTask.cancel(true);
        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        assertThat(taskService.runningTasks()).isEmpty();
    }

    @Test public void suspendOneRunOther() {
        final int[]         exs   = new int[2];
        final ScheduledTask task1 = createTestTask("0/1", t -> {
                    exs[0]++;
                    return Status.done();
                });
        final ScheduledTask task2 = createTestTask(TestTask2.class, "0/2", t -> {
                    exs[1]++;
                    return Status.done();
                });
        taskService.scheduleForTest(task1, task2);
        assertThat(exs[0]).isEqualTo(0);
        assertThat(exs[1]).isEqualTo(0);
        assertThat(task1.suspend()).isTrue();
        taskService.increment(2, 1);
        assertThat(exs[0]).isEqualTo(0);
        assertThat(exs[1]).isEqualTo(1);
        assertThat(task1.resume()).isTrue();
        taskService.increment(3, 1);
        assertThat(exs[0]).isEqualTo(2);
        assertThat(exs[1]).isEqualTo(2);
        taskService.stop();
    }

    @Test public void testChangeCron() {
        final ScheduledTask task = createTestTask("0/2", t -> {
                    t.setDataTime(DateTime.current());
                    executions++;
                    return Status.done();
                });
        taskService.scheduleForTest(task);

        taskService.changeCron(task.getFqn(), "never");

        final TaskProps taskProps = Context.getEnvironment().get(Task.idFromName(task.getFqn()), TaskProps.class);

        assertThat(taskProps.cron).isEqualTo(TaskConstants.NEVER_CRON);
    }

    private void assertExtendedTaskProcessor(Class<? extends ProcessorTaskInstance<Object>> c, @Nullable Status before, @Nullable Status after,
                                             int expectedCount, TaskStatus expectedStatus) {
        final ScheduledTask                                  testTask = new ScheduledTask(c);
        final ExtendedProcessorBaseTask.ExtendedProcesorTask instance = testTask.getInstance();
        final ExtendedProcessorBaseTask                      base     = instance.getBase();
        final int                                            id       = getIsolated();
        base.init(id);
        base.returnAtBefore(before);
        base.returnAtAfter(after);
        taskService.submitForTest(testTask);

        assertThat(isolatedCount(id)).isEqualTo(expectedCount);

        assertTaskEntry(c, expectedStatus);
    }

    private TaskEntry assertTaskEntry(Class<?> clazz, TaskStatus status) {
        return invokeInTransaction(() -> {
            final TaskEntry taskEntry = TaskEntry.find(clazz.getCanonicalName());
            assertThat(taskEntry).isNotNull();
            assertThat(taskEntry.getStatus()).isEqualTo(status);
            return taskEntry;
        });
    }

    private void assertTaskEntry(Class<?> clazz, TaskStatus status, int totalItems, int successItems, int errorItems, int ignoreItems) {
        runInTransaction(() -> {
            final TaskEntry taskEntry = assertTaskEntry(clazz, status);

            final TaskExecutionLog l = taskEntry.getCurrentLog();

            assertThat(l).isNotNull();
            assertThat(l.getTotalItems()).isEqualTo(totalItems);
            assertThat(l.getSuccessItemsCount()).isEqualTo(successItems);
            assertThat(l.getErrorItemsCount()).isEqualTo(errorItems);
            assertThat(l.getIgnoreItemsCount()).isEqualTo(ignoreItems);
        });
    }

    private int isolatedCount(int id) {
        return invokeInTransaction(() -> Isolated.findOrFail(id).getCount());
    }

    private int getIsolated() {
        return invokeInTransaction(() -> Isolated.create().insert().getId());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    @NotNull private static String instanceData(TaskInstance<?> instance) {
        return invokeInTransaction(() -> TaskEntry.findOrFail(instance.getClass().getName())).getData();
    }
}  // end class TaskServiceTest
