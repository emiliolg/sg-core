
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.service.RemoteTaskService;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.task.TaskService;
import tekgenesis.task.jmx.TaskLogInfo;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.DateTime.EPOCH;
import static tekgenesis.console.TaskTable.timeAsLabel;

/**
 * User class for Form: TaskHistoryForm
 */
public class TaskHistoryForm extends TaskHistoryFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void onLoad() {
        final String taskFqn = getTaskFqn();

        // final String taskId = Strings.deCapitalizeFirst(QName.createQName(taskFqn).getName());

        final Option<RemoteTaskService> service = Context.getSingleton(Clusters.class).getService(RemoteTaskService.class, TaskService.SERVICE_NAME);

        if (service.isEmpty()) return;

        final List<TaskLogInfo> log = service.get().getTaskLog(taskFqn);

        getHistoryTable().clear();
        for (final TaskLogInfo info : log) {
            final HistoryTableRow row = getHistoryTable().add();
            row.setStatus(info.getStatus().label());
            DateTime startTime = info.getStartTime();
            if (info.getStartTime() != null) {
                startTime = info.getStartTime();
                row.setStartTime(notNull(info.getStartTime(), EPOCH));
            }
            row.setUpdateTime(notNull(info.getUpdateTime(), EPOCH));

            final DateTime endTime     = info.getEndTime() != null ? info.getEndTime() : info.getUpdateTime();
            final long     elapsedTime = startTime != null ? endTime.toMilliseconds() - startTime.toMilliseconds() : 0;

            row.setElapsedTime(timeAsLabel(elapsedTime));
            row.setMemberName(info.getMember());
            row.setTotalItems(info.getTotalItems());
            row.setFailedItems(info.getFailedItems());
            row.setSuccessItems(info.getSuccessItems());
            row.setIgnoredItems(info.getIgnoredItems());
        }
    }

    /** Invoked when populating a form instance. */
    @NotNull @Override public Object populate() {
        return getTaskFqn();
    }

    //~ Inner Classes ................................................................................................................................

    public class HistoryTableRow extends HistoryTableRowBase {}
}  // end class TaskHistoryForm
