
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

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.database.Databases;
import tekgenesis.persistence.Sql;
import tekgenesis.service.ServiceManager;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.g.TaskEntryTable;

import static java.lang.String.format;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.persistence.expr.Expr.CURRENT_TIME;
import static tekgenesis.task.Status.abort;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * User class for Task: SuiGeneris TaskMonitor
 */
public class SuiGenerisTaskMonitor extends SuiGenerisTaskMonitorBase {

    //~ Instance Fields ..............................................................................................................................

    private String currentMemberName = null;

    //~ Constructors .................................................................................................................................

    private SuiGenerisTaskMonitor(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        currentMemberName = Context.getSingleton(ClusterManager.class).getMemberName();

        final TaskEntryTable           TE      = TaskEntryTable.TASK_ENTRY;
        final ImmutableList<TaskEntry> entries = invokeInTransaction(() ->
                    Sql.selectFrom(TE)
                       .where(
                           TE.STATUS.eq(TaskStatus.RUNNING)
                                    .and(CURRENT_TIME.gt(TE.EXPIRATION_TIME).or(TE.EXPIRATION_TIME.isNull()))
                                    .and(TE.MEMBER.eq(currentMemberName)))
                       .list()
                       .toList());

        entries.forEach(this::checkTaskExecution);

        return Status.ok();
    }

    private void checkTaskExecution(@NotNull TaskEntry te) {
        // Double check
        final TaskEntry taskEntry = invokeInTransaction(() -> TaskEntry.find(te.getName()));
        if (taskEntry == null) return;

        final boolean  isRunning   = taskEntry.getStatus() == TaskStatus.RUNNING;
        final DateTime currentTime = invokeInTransaction(() -> Databases.openDefault().currentTime());
        final boolean  isExpired   = taskEntry.getExpirationTime() == null || currentTime.compareTo(taskEntry.getExpirationTime()) > 0;

        if (isRunning && isExpired) {
            if (isThreadAlive(taskEntry)) {
                // First Try to stop the task
                final ServiceManager serviceManager = Context.getSingleton(ServiceManager.class);

                final TaskService service = (TaskService) serviceManager.getService(TaskService.SERVICE_NAME);

                if (service != null) {
                    try {
                        logWarning("Task Monitor: Stop Task '%s' by Time Expired in node '%s' ", te.getName(), te.getMember());
                        service.stopTask(te.getName());
                    }
                    catch (final Exception e) {
                        taskEntry.endExecution(abort("Time expired."), null);
                    }
                }
                else logWarning("Unable to get TaskService.");
            }
            else taskEntry.endExecution(abort(format("Thread not present or aborted in node '%s'. Task Time expired.", currentMemberName)), null);
        }
    }

    private boolean isThreadAlive(@NotNull TaskEntry taskEntry) {
        final String currentThreadGroup = Thread.currentThread().getThreadGroup().getName();

        return filter(Thread.getAllStackTraces().keySet(),  //
                t -> {
                    final boolean hasThreadGroup = t != null && t.getThreadGroup() != null && t.getThreadGroup().getName() != null;
                    return hasThreadGroup && t.getThreadGroup().getName().equals(currentThreadGroup) && t.getName().endsWith(taskEntry.getName());
                })             //
               .getFirst()     //
               .filter(Thread::isAlive)  //
               .isPresent();
    }
}
