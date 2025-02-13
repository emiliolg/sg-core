
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.notice.Advice;
import tekgenesis.admin.notice.NoticeProps;
import tekgenesis.admin.notice.State;
import tekgenesis.admin.sg.AdminViews;
import tekgenesis.cluster.jmx.util.Format;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.*;
import tekgenesis.common.env.context.Context;
import tekgenesis.database.Databases;
import tekgenesis.database.DbIntrospector;
import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.introspect.TableInfo;
import tekgenesis.mail.Mail;
import tekgenesis.mail.MailException;
import tekgenesis.mail.MailSender;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableMetadata;
import tekgenesis.sg.TaskEntry;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.sg.g.TaskEntryTable;
import tekgenesis.sg.g.TaskExecutionLogTable;

import static java.lang.String.format;

import static tekgenesis.admin.notice.AdviceType.*;
import static tekgenesis.admin.notice.Level.SEVERE;
import static tekgenesis.admin.notice.Level.WARNING;
import static tekgenesis.admin.notice.g.AdviceTable.ADVICE;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Times.HOURS_DAY;
import static tekgenesis.persistence.Sql.deleteFrom;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.task.TaskStatus.FAILED;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * User class for Task: SuiGenerisAdvisor
 */
public class SuiGenerisAdvisor extends SuiGenerisAdvisorBase {

    //~ Instance Fields ..............................................................................................................................

    private final ArrayList<Advice> notices = new ArrayList<>();

    //~ Constructors .................................................................................................................................

    /** Constructor with task. */
    SuiGenerisAdvisor(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        runInTransaction(() -> {
            checkIndexes();
            checkRunningTasks();
            checkFailedTasks();
            purgeDismissed();
            purgeOld();
            sendNotification();
        });
        return Status.done();
    }

    private void checkFailedTasks() {
        final TaskExecutionLogTable    TL           = TaskExecutionLogTable.TASK_EXECUTION_LOG;
        final HashMap<String, Integer> tasksSummary = new HashMap<>();
        selectFrom(TL).where(TL.START_TIME.ge(DateTime.current().addHours(-HOURS_DAY)).and(TL.STATUS.eq(FAILED))).forEach(execLog -> {
            Advice.create(format("Task %s execution has failed at %s", execLog.getName(), execLog.getStartTime().format()), SEVERE, FAILED_TASK)
                  .insert();
            Integer fails = notNull(tasksSummary.get(execLog.getName()), 0);
            tasksSummary.put(execLog.getName(), ++fails);
        });
        for (final Map.Entry<String, Integer> entry : tasksSummary.entrySet())
            notices.add(Advice.create(format("Task %s execution has failed %s times", entry.getKey(), entry.getValue()), SEVERE, FAILED_TASK));
    }

    private void checkIndex(DbIntrospector introspector, String s, DbTable<?, ?> table, long count) {
        if (count <= MIN_ROWS) return;
        final Option<TableInfo> tableInfo = introspector.getTable(
                QName.createQName(introspector.getSchemaPrefix() + table.metadata().getTableQName()));
        if (tableInfo.isEmpty()) return;

        for (final TableInfo.Index index : tableInfo.get().getIndices()) {
            if (index.getColumns().size() == 1 && "update_time".equalsIgnoreCase(index.getColumns().getFirst().get().getName())) return;
        }

        notices.add(
            Advice.create(format("Entity %s has more than %d rows and no index for updateTime", s, MIN_ROWS), WARNING, INDEX_MISSING).insert());
    }

    private void checkIndexes() {
        runInTransaction(() -> {
            try(final DbIntrospector introspector = DbIntrospector.forDatabase(Databases.openDefault())) {
                for (final String s : TableMetadata.localEntities(Context.getEnvironment())) {
                    final DbTable<?, ?> table = DbTable.forName(s);
                    if (table.metadata().isRemotable()) checkIndex(introspector, s, table, getCount(table));
                }
            }
        });
    }

    private void checkRunningTasks() {
        final TaskEntryTable  TE      = TaskEntryTable.TASK_ENTRY;
        final List<TaskEntry> dbTasks = selectFrom(TE).where(TE.STATUS.eq(TaskStatus.RUNNING)).list();

        final DateTime currentTime = Databases.openDefault().currentTime();

        final TaskExecutionLogTable TL = TaskExecutionLogTable.TASK_EXECUTION_LOG;
        for (final TaskEntry taskEntry : dbTasks) {
            if (taskEntry.getCurrentLogId() != null) {
                final TaskExecutionLog taskExecutionLog = selectFrom(TL).where(TL.ID.eq(taskEntry.getCurrentLogId())).get();

                if (taskExecutionLog != null) {
                    final DateTime startTime = taskExecutionLog.getStartTime();
                    final long     time      = currentTime.toMilliseconds() - startTime.toMilliseconds();
                    if (taskEntry.getExpirationTime() != null && currentTime.compareTo(taskEntry.getExpirationTime()) > 0) {
                        final Advice advice = Advice.create(
                                format("Task %s has been running further than expiration time (%s)", taskEntry.getName(), Format.timeAsString(time)),
                                WARNING,
                                LONG_RUNNING_TASK);
                        advice.insert();
                        notices.add(advice);
                    }
                }
            }
        }
        selectFrom(TL).where(TL.START_TIME.ge(DateTime.current().addHours(-HOURS_DAY))).forEach(execLog -> {
            if (execLog.getEndTime() != null && execLog.getExpirationTime() != null &&
                execLog.getEndTime().compareTo(execLog.getExpirationTime()) > 0)
            {
                final long   time   = execLog.getEndTime().toMilliseconds() - execLog.getStartTime().toMilliseconds();
                final Advice advice = Advice.create(
                        format("Task %s execution time has been more than 30 minutes (%s) at %s",
                            execLog.getName(),
                            Format.timeAsString(time),
                            execLog.getStartTime()),
                        WARNING,
                        LONG_RUNNING_TASK);
                advice.insert();
            }
        });
    }  // end method checkRunningTasks

    private void purgeDismissed() {
        deleteFrom(ADVICE).where(ADVICE.CREATION_TIME.le(DateTime.current().addHours(-HOURS_DAY * DAYS_MONTH)).and(ADVICE.STATE.eq(State.DISMISSED)))
            .execute();
    }

    private void purgeOld() {
        deleteFrom(ADVICE).where(ADVICE.CREATION_TIME.le(DateTime.current().addHours(-HOURS_DAY * 3 * DAYS_MONTH))).execute();
    }

    private void schedule(NoticeProps props, Mail templateMail) {
        final String[] split    = props.mailSchedule.split(":");
        DateTime       dateTime = DateTime.current().withMilliseconds(0);
        if (split.length >= 3) dateTime = dateTime.withSeconds(Integer.parseInt(split[2]));
        if (split.length >= 2) dateTime = dateTime.withSeconds(Integer.parseInt(split[1]));
        if (split.length >= 1) dateTime = dateTime.withSeconds(Integer.parseInt(split[0]));
        templateMail.schedule(dateTime);
    }

    private void sendNotification() {
        final NoticeProps props            = Context.getProperties(NoticeProps.class);
        final String      notificationMail = props.notificationMail;
        if (notificationMail != null) {
            try {
                final Mail mail = new Mail();
                mail.to(Colls.listOf(notificationMail));
                mail.from(props.mailFrom);
                mail.withSubject(props.mailSubject);
                mail.withBody(AdminViews.notices(Colls.immutable(notices)));
                schedule(props, mail);
                MailSender.queue(mail);
            }
            catch (final MailException e) {
                logError(e);
            }
        }
    }

    private long getCount(DbTable<?, ?> table) {
        try {
            return selectFrom(table).count();
        }
        catch (final DatabaseException e) {
            // ignore
        }
        return 0;
    }

    //~ Static Fields ................................................................................................................................

    private static final int MIN_ROWS   = 10000;
    private static final int DAYS_MONTH = 30;
}  // end class SuiGenerisAdvisor
