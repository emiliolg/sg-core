
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.CronExpression;
import tekgenesis.metadata.task.TaskConstants;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.util.Reflection.findClass;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Scheduled Task.
 */
@SuppressWarnings("WeakerAccess")
public final class ScheduledTask extends Task<ScheduledTaskInstance> {

    //~ Constructors .................................................................................................................................

    /** Create an Scheduled Task. */
    public ScheduledTask(@NotNull final Class<? extends ScheduledTaskInstance> taskClass) {
        super(taskClass);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Return the Cron Expression for this task (eventually overridden in the environment if the
     * Cron expression is invalid or 'never' it will returns null.
     */
    @Nullable public CronExpression getCronExpression(Environment env) {
        final String cron = notEmpty(getTaskProperties(env).cron, getInstance().getCronExpression());
        return cron.equals(TaskConstants.NEVER_CRON) ? null : CronExpression.parse(cron).getOrNull();
    }

    /** @return  the qn of the task trigger */
    @NotNull public String getScheduleAfter() {
        return getInstance().getScheduleAfter();
    }

    @Override void endExecution(Status status) {
        getInstance().getTaskEntry().endExecution(status, invokeInTransaction(() -> getInstance().getNextDueTime()));
    }

    //~ Methods ......................................................................................................................................

    /** Create an Scheduled Task. */
    public static Option<ScheduledTask> createFromFqn(@NotNull final String taskFqn) {
        try {
            final String qName = taskFqn.contains(":") ? taskFqn.split(":")[0] : taskFqn;
            return findClass(qName, ScheduledTaskInstance.class).map(ScheduledTask::new);
        }
        catch (final Exception e) {
            logger.error("Cannot instantiate task " + taskFqn, e);
            return Option.empty();
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(ScheduledTask.class);
}  // end class ScheduledTask
