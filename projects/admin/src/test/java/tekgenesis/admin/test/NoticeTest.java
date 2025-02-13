
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.admin.notice.Advice;
import tekgenesis.admin.notice.AdviceType;
import tekgenesis.admin.notice.Level;
import tekgenesis.admin.notice.State;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Times;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.DbTimeProviderRule;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.database.SqlStatement;
import tekgenesis.persistence.TableFactory;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.SuiGenerisAdvisor;
import tekgenesis.task.TaskServiceForTest;
import tekgenesis.task.TaskStatus;
import tekgenesis.test.basic.Category;
import tekgenesis.views.A;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Advice Test.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class NoticeTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    @Rule public MuteRule     mute        = new MuteRule(SqlStatement.class);
    public TaskServiceForTest taskService = null;

    @Rule public DbTimeProviderRule timer = new DbTimeProviderRule(DateTime.current());

    @Rule public RuleChain chain = RuleChain.outerRule(timer)
                                   .around(
            new DbRule(DbRule.SG,
                DbRule.BASIC_TEST,
                DbRule.ADVICE,
                DbRule.AUTHORIZATION,
                DbRule.TEST,
                DbRule.BASIC,
                DbRule.CART,
                DbRule.SHOWCASE,
                DbRule.MAIL,
                DbRule.FORM,
                DbRule.VIEWS) {
                @Override protected void after() {
                    if (taskService != null) taskService.getServiceManager().shutdown();
                }

                @Override protected void before() {
                    createDatabase(dbName);
                }

                @Override protected void initTableFactory() {
                    taskService = new TaskServiceForTest(env, timer);
                    TableFactory.setFactory(new TableFactory(createStoreHandlerFactory()));
                }
            });

    //~ Methods ......................................................................................................................................

    @Test public void indexesTest() {
        runInTransaction(() -> {
            for (int i = 1; i < 15000; i++) {
                Category.create(i).insert();
                A.create().insert();
            }
        });

        runTask();
        final ImmutableList<Advice> advices = Advice.list().list();
        assertThat(advices).hasSize(2);
        assertThat(advices.getFirst().get().getType()).isEqualTo(AdviceType.INDEX_MISSING);
        assertThat(advices.get(1).getType()).isEqualTo(AdviceType.INDEX_MISSING);
    }

    @Test public void purge() {
        runInTransaction(() -> {
            Advice.create("Test", Level.INFO, AdviceType.INDEX_MISSING).insert();
            Advice.create("Test3", Level.INFO, AdviceType.INDEX_MISSING).insert();
            Advice.create("Test2", Level.INFO, AdviceType.LONG_RUNNING_TASK).setState(State.DISMISSED).insert();
            timer.increment(Times.MILLIS_DAY * 31);
        });
        runTask();

        runInTransaction(() -> {
            final ImmutableList<Advice> advices = Advice.list().list();
            assertThat(advices).hasSize(2);
            assertThat(advices.getFirst().get().getType()).isEqualTo(AdviceType.INDEX_MISSING);
            Advice.create("Test4", Level.INFO, AdviceType.INDEX_MISSING).insert();

            timer.increment(Times.MILLIS_DAY * 61);
        });
        runTask();

        runInTransaction(() -> {
            final ImmutableList<Advice> advices2 = Advice.list().list();
            assertThat(advices2).hasSize(1);
            assertThat(advices2.getFirst().get().getType()).isEqualTo(AdviceType.INDEX_MISSING);
        });
    }

    @Test public void tasksTest() {
        runInTransaction(() -> {
            final TaskExecutionLog task1 = TaskExecutionLog.create();
            task1.setName("MyTask1");
            task1.setStartTime(DateTime.current().addHours(-3));
            task1.insert();

            final TaskEntry myTask1 = TaskEntry.create("MyTask1");
            myTask1.setStatus(TaskStatus.RUNNING);
            myTask1.setCurrentLogId(task1.getId());
            myTask1.setExpirationTime(DateTime.current().addHours(-2));
            myTask1.insert();

            final TaskExecutionLog task2 = TaskExecutionLog.create();
            task2.setName("MyTask2");
            task2.setStartTime(DateTime.current().addHours(-10));
            task2.setExpirationTime(DateTime.current().addHours(-12));
            task2.setEndTime(DateTime.current().addHours(-1));
            task2.insert();

            final TaskEntry myTask2 = TaskEntry.create("MyTask2");
            myTask2.setStatus(TaskStatus.FINISHED);
            myTask2.setCurrentLogId(task2.getId());
            myTask2.insert();

            final TaskExecutionLog task3 = TaskExecutionLog.create();
            task3.setName("MyTask3");
            task3.setStartTime(DateTime.current().addHours(-10));
            task3.setEndTime(DateTime.current().addHours(-10));
            task3.setStatus(TaskStatus.FAILED);
            task3.insert();

            final TaskEntry myTask3 = TaskEntry.create("MyTask3");
            myTask3.setStatus(TaskStatus.FAILED);
            myTask3.setCurrentLogId(task3.getId());
            myTask3.insert();
        });

        runTask();
        final ImmutableList<Advice> advices = Advice.list().list();
        assertThat(advices).hasSize(3);
        assertThat(advices.getFirst().get().getType()).isEqualTo(AdviceType.LONG_RUNNING_TASK);
        assertThat(advices.get(1).getType()).isEqualTo(AdviceType.LONG_RUNNING_TASK);
        assertThat(advices.get(2).getType()).isEqualTo(AdviceType.FAILED_TASK);
    }  // end method tasksTest

    @Test public void userTest() {
        runInTransaction(() -> Advice.notice("User", Level.INFO));

        final ImmutableList<Advice> advices = Advice.list().toList();
        assertThat(advices).hasSize(1);
        assertThat(advices.getFirst().get().getType()).isEqualTo(AdviceType.USER_NOTICE);
    }

    private void runTask() {
        final ScheduledTask task = ScheduledTask.createFromFqn(SuiGenerisAdvisor.class.getName()).get();
        taskService.submitForTest(task);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class NoticeTest
