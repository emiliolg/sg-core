
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.jmx;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.DateTime;
import tekgenesis.metadata.task.TaskConstants;
import tekgenesis.sg.TaskEntry;
import tekgenesis.task.TaskStatus;

import static tekgenesis.common.core.Constants.HASH_SALT;
import static tekgenesis.common.core.Strings.truncate;

/**
 * Task Info.
 */
@SuppressWarnings({ "WeakerAccess", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber" })
public class TaskEntryInfo implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String cronExpression = TaskConstants.NEVER_CRON;

    @Nullable private DateTime currentCronExecution = null;
    @Nullable private String   data                 = null;
    @Nullable private DateTime dataTime             = null;
    private DateTime           dueTime              = null;

    @Nullable private Integer     logId      = null;
    @NotNull private String       member     = "";
    @NotNull private final String name;
    @Nullable private DateTime    startTime  = null;
    @NotNull private TaskStatus   status     = TaskStatus.NOT_SCHEDULED;
    @NotNull private DateTime     updateTime = DateTime.EPOCH;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public TaskEntryInfo(@NotNull String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    public boolean equals(Object that) {
        return this == that || that instanceof TaskEntryInfo && eq((TaskEntryInfo) that);
    }

    public int hashCode() {
        return name.hashCode() * HASH_SALT;
    }

    /** Returns true is the task is marked as running. */
    public boolean markedAsRunning() {
        return status == TaskStatus.RUNNING || status == TaskStatus.RETRYING;
    }

    @NotNull public String toString() {
        return getName();
    }

    /** return Cron expression.* */
    @NotNull public String getCronExpression() {
        return cronExpression;
    }

    /**  */
    public void setCronExpression(@NotNull String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /** Returns the Cron Execution. */
    @Nullable public DateTime getCurrentCronExecution() {
        return currentCronExecution;
    }

    /** Sets the value of the Cron Execution. */
    public void setCurrentCronExecution(@Nullable DateTime currentCronExecution) {
        this.currentCronExecution = currentCronExecution;
    }

    /**  */
    @Nullable public String getData() {
        return data;
    }

    /** raw data. */
    public void setData(@Nullable String d) {
        data = d;
    }
    /**  */
    @Nullable public DateTime getDataTime() {
        return dataTime;
    }

    /** datetime data. */
    public void setDataTime(@Nullable DateTime d) {
        dataTime = d;
    }

    /**  */
    public DateTime getDueTime() {
        return dueTime;
    }

    /**  */
    public void setDueTime(DateTime d) {
        dueTime = d;
    }

    /** Full Task Name (id +name).* */
    public String getFullName() {
        return getName();
    }

    /** return task log id if exists. */
    @Nullable public Integer getLogId() {
        return logId;
    }

    /** Set task log id. */
    public void setLogId(@Nullable Integer logId) {
        this.logId = logId;
    }

    /** Returns the Running At Member. */
    @NotNull public String getMember() {
        return Predefined.notEmpty(member, "N/A");
    }

    /** Sets the value of the Running At Member. */
    public void setMember(@Nullable String runningAtMember) {
        final String s = truncate(runningAtMember, 256);
        if (s != null) member = s;
    }

    /** Returns the Task Id. */
    @NotNull public String getName() {
        return name;
    }

    /**  */
    @Nullable public DateTime getStartTime() {
        return startTime;
    }

    /**  */
    public void setStartTime(@Nullable DateTime time) {
        startTime = time;
    }

    /** Returns the Status. */
    @NotNull public TaskStatus getStatus() {
        return status;
    }

    /** Sets the value of the Status. */
    public void setStatus(@NotNull TaskStatus status) {
        this.status = status;
    }

    /**  */
    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }
    /** .* */
    public void setUpdateTime(@NotNull DateTime time) {
        updateTime = time;
    }

    private boolean eq(@NotNull TaskEntryInfo that) {
        return Predefined.equal(name, that.name);
    }

    //~ Methods ......................................................................................................................................

    /** Builder. */
    @Nullable public static TaskEntryInfo create(@Nullable TaskEntry f) {
        if (f == null) return null;
        final TaskEntryInfo info = new TaskEntryInfo(f.getName());

        info.setCronExpression(f.getCronExpression());
        info.setLogId(f.getCurrentLogId());
        info.setMember(f.getMember());
        info.setStatus(f.isSuspended() ? TaskStatus.SUSPENDED : f.getStatus());
        info.setStartTime(f.getStartTime());
        info.setUpdateTime(f.getUpdateTime());
        info.setData(f.getData());
        info.setDataTime(f.getDataTime());
        info.setDueTime(f.getDueTime());

        return info;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1439508934757467517L;
}  // end class TaskEntryInfo
