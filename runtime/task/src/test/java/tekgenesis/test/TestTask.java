
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * User class for Task: NewTestTask
 */
public class TestTask extends TestTaskBase {

    //~ Instance Fields ..............................................................................................................................

    private String cronExpression;

    private Function<TestTask, Status> runner;

    //~ Constructors .................................................................................................................................

    TestTask(@NotNull ScheduledTask task) {
        super(task);
        runner         = t -> { throw new IllegalStateException("Not Initialized"); };
        cronExpression = "";
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        return ensureNotNull(runner.apply(this));
    }

    @NotNull @Override public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /** Set the runner for the task. */
    public void setRunner(Function<TestTask, Status> runner) {
        this.runner = runner;
    }

    //~ Methods ......................................................................................................................................

    /** Create an Instance of the task with the specified runner. */
    @NotNull public static ScheduledTask createTestTask(Function<TestTask, Status> runner) {
        return createTestTask("", runner);
    }

    /** Create an Instance of the task with the specified runner. */
    @NotNull public static ScheduledTask createTestTask(String cronExpression, Function<TestTask, Status> runner) {
        return createTestTask(TestTask.class, cronExpression, runner);
    }

    /** Create an Instance of the task with the specified runner. */
    @NotNull public static ScheduledTask createTestTask(Class<? extends TestTask> taskClass, String cronExpression,
                                                        Function<TestTask, Status> runner) {
        final ScheduledTask task     = new ScheduledTask(taskClass);
        final TestTask      instance = task.getInstance();
        instance.setRunner(runner);
        instance.setCronExpression(cronExpression);
        return task;
    }
}  // end class NewTestTask
