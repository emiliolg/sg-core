
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
import org.jgroups.Address;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Reflection;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.Sql;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.sg.g.TaskEntryTable;
import tekgenesis.sg.g.TaskExecutionLogTable;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.DAYS;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.persistence.Sql.deleteFrom;
import static tekgenesis.persistence.expr.Expr.CURRENT_TIME;
import static tekgenesis.task.TaskStatus.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * User class for Task: SuiGenerisTaskPurge
 */
public class SuiGenerisTaskPurge extends SuiGenerisTaskPurgeBase {

    //~ Constructors .................................................................................................................................

    private SuiGenerisTaskPurge(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull public Status run() {
        final Seq<TaskEntry> zombieTasks = getZombieTasks();
        zombieTasks.forEach(this::process);

        purgeTaskExecutionLog();

        return Status.ok();
    }

    private void process(@Nullable TaskEntry te) {
        if (te == null) return;

        final Option<TaskEntry> entryOption = invokeInTransaction(() -> Option.ofNullable(TaskEntry.find(te.getName())));
        if (entryOption.isEmpty()) return;

        final TaskEntry taskEntry = entryOption.get();

        final String[] parts        = taskEntry.getName().split(":");
        final boolean  nodeBaseTask = parts.length > 1;
        final String   className    = nodeBaseTask ? parts[0] : taskEntry.getName();

        if (isTaskClassDefined(className)) runInTransaction(taskEntry::delete);
        else {
            // Double check...
            final Seq<String> members = getActiveMembers();

            if (te.getStatus() == RUNNING && !members.contains(taskEntry.getMember()))
                taskEntry.endExecution(Status.abort(format("Running at dead member %s.", taskEntry.getMember())), null);

            if (te.getStatus() == SCHEDULED && nodeBaseTask && !members.contains(parts[1])) {
                logWarning("Deleting task %s because it is scheduled in a dead node (%s)", te.getName(), parts[1]);
                runInTransaction(taskEntry::delete);
            }
        }
    }  // end method process

    private void purgeTaskExecutionLog() {
        runInTransaction(() -> {
            final TaskExecutionLogTable TEL = TaskExecutionLog.TL;
            deleteFrom(TEL).where(TEL.UPDATE_TIME.lt(CURRENT_TIME.sub(MAX_OLD_DAYS, DAYS)).and(TEL.STATUS.eq(FINISHED).or(TEL.STATUS.eq(FAILED))));
        });
    }

    private Seq<String> getActiveMembers() {
        final ClusterManager<Address> singleton = cast(getContext().getSingleton(ClusterManager.class));
        return singleton.getMembersAddressNames();
    }

    private boolean isTaskClassDefined(String className) {
        return invokeInTransaction(() -> Reflection.findClass(className, ScheduledTaskInstance.class).isEmpty());
    }

    private Seq<TaskEntry> getZombieTasks() {
        final TaskEntryTable TE = TaskEntryTable.TASK_ENTRY;

        final Seq<String> members = getActiveMembers();

        final Criteria notScheduledTask = TE.STATUS.eq(NOT_SCHEDULED);
        final Criteria oldSchedules     = TE.STATUS.eq(SCHEDULED);

        return invokeInTransaction(() ->
                Sql.selectFrom(TE)
                   .where(TE.STATUS.eq(RUNNING).and(TE.MEMBER.notIn(members)).or(oldSchedules).or(notScheduledTask))
                   .list()
                   .toList());
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_OLD_DAYS = 30;
}
