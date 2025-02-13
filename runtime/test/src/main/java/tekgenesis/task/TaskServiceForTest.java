
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.tools.test.TimeProviderRule;
import tekgenesis.service.ServiceManager;

import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.core.Option.option;

/**
 * A task service for test that intercepts certain calls to make the service easier to test.
 */
public class TaskServiceForTest extends TaskService {

    //~ Instance Fields ..............................................................................................................................

    private boolean                          running;
    private boolean                          sleeping;
    private final Map<String, ScheduledTask> testsTasks = new HashMap<>();
    private final TimeProviderRule           timer;

    //~ Constructors .................................................................................................................................

    /** Constructor with Environment and TimeProviderRule. */
    public TaskServiceForTest(Environment env, TimeProviderRule timer) {
        super(new ServiceManager(env));
        this.timer = timer;
        running    = false;
        sleeping   = false;
    }

    //~ Methods ......................................................................................................................................

    /** Submit right now and wait for execution. */
    public Status submitForTest(final ScheduledTask task) {
        createExecutor();
        try {
            return submit(task).get();
        }
        catch (InterruptedException | ExecutionException e) {
            logger.warning(e);
            return Status.abort(e);
        }
    }

    @NotNull @Override protected Option<ScheduledTask> createScheduledTask(String taskName) {
        final ScheduledTask t = testsTasks.get(taskName);
        return t != null ? option(t) : super.createScheduledTask(taskName);
    }

    /** Simulate a sleep over the timer. */
    @Override protected synchronized void doSleep(long milliseconds) {
        final long endTime = timer.getCurrentTime() + milliseconds;
        while (timer.getCurrentTime() < endTime) {
            if (running) {
                running = false;
                notify();
            }
            final long prevTime = timer.getCurrentTime();
            try {
                sleeping = true;
                wait(TIMEOUT);
            }
            catch (final InterruptedException ignore) {}
            if (!sleeping) return;
            if (prevTime == timer.getCurrentTime()) fail("Timeout waiting for simulated clock to advance");
            sleeping = false;
        }
    }

    @Override protected synchronized void notifyScheduler() {
        if (sleeping) sleeping = false;
        running = true;
        notifyAll();
    }

    @Override protected void runMonitorTask() {}

    void increment(int secs, int tick) {
        for (int n = tick; n <= secs; n += tick) {
            synchronized (this) {
                timer.increment(tick * 1_000);
                running = true;
                notify();
            }
            waitForIdle();
        }
    }

    void scheduleForTest(final ScheduledTask... task) {
        running = true;
        for (final ScheduledTask t : task) {
            schedule(t);
            testsTasks.put(t.getFqn(), t);
        }
        start();
        waitForIdle();
    }

    /** Submit right now and wait for execution. */
    <T> Status submitForTest(final ScheduledTask task, @Nullable T value) {
        createExecutor();
        try {
            return submit(task, value).get();
        }
        catch (InterruptedException | ExecutionException e) {
            logger.warning(e);
            return Status.abort(e);
        }
    }
    /** Submit right now and return Future. */
    Future<Status> submitForTestAsync(final ScheduledTask task) {
        createExecutor();
        return submit(task);
    }

    private synchronized void waitForIdle() {
        while (running || !runningTasks().isEmpty()) {
            try {
                wait();
            }
            catch (final InterruptedException ignore) {}
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int TIMEOUT = 5_000;
}  // end class TaskServiceForTest
