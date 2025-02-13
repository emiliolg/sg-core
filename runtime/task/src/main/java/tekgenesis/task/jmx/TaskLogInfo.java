
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

import tekgenesis.common.core.DateTime;
import tekgenesis.sg.TaskExecutionLog;
import tekgenesis.task.TaskStatus;

/**
 * TaskLogInfo.
 */
public class TaskLogInfo implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private String      data         = null;
    @Nullable private DateTime    dataTime     = null;
    private DateTime              endTime      = null;
    private String                errorMessage = null;
    private int                   failedItems  = 0;
    private int                   ignoredItems = 0;
    private String                mds          = null;
    private String                member       = null;
    @NotNull private final String name;
    private DateTime              startTime    = null;
    private TaskStatus            status       = null;
    private int                   successItems = 0;
    private int                   totalItems   = 0;
    private DateTime              updateTime   = null;

    //~ Constructors .................................................................................................................................

    /**  */
    public TaskLogInfo(@NotNull String n) {
        name = n;
    }

    //~ Methods ......................................................................................................................................

    /**  */
    @Nullable public String getData() {
        return data;
    }

    /**  */
    public void setData(@Nullable String d) {
        data = d;
    }

    /**  */
    @Nullable public DateTime getDataTime() {
        return dataTime;
    }

    /**  */
    public void setDataTime(@Nullable DateTime d) {
        dataTime = d;
    }

    /**  */
    public DateTime getEndTime() {
        return endTime;
    }

    /**  */
    public void setEndTime(@Nullable DateTime d) {
        endTime = d;
    }

    /**  */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**  */
    public void setErrorMessage(@Nullable String e) {
        errorMessage = e;
    }

    /**  */
    public int getFailedItems() {
        return failedItems;
    }

    /**  */
    public void setFailedItems(int failedItems) {
        this.failedItems = failedItems;
    }

    /** @return  nro items ignored */
    public int getIgnoredItems() {
        return ignoredItems;
    }

    /**  */
    public void setIgnoredItems(int ignoredItems) {
        this.ignoredItems = ignoredItems;
    }

    /**  */
    public String getMds() {
        return mds;
    }

    /**  */
    public void setMds(String m) {
        mds = m;
    }

    /**  */
    public String getMember() {
        return member;
    }

    /**  */
    public void setMember(String m) {
        member = m;
    }

    /**  */
    @NotNull public String getName() {
        return name;
    }

    /**  */
    public DateTime getStartTime() {
        return startTime;
    }

    /**  */
    public void setStartTime(DateTime s) {
        startTime = s;
    }

    /**  */
    public TaskStatus getStatus() {
        return status;
    }

    /**  */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    /**  */
    public int getSuccessItems() {
        return successItems;
    }
    /**  */
    public void setSuccessItems(int successItems) {
        this.successItems = successItems;
    }
    /**  */
    public int getTotalItems() {
        return totalItems;
    }
    /**  */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    /**  */
    public DateTime getUpdateTime() {
        return updateTime;
    }

    /**  */
    public void setUpdateTime(DateTime u) {
        updateTime = u;
    }

    //~ Methods ......................................................................................................................................

    /**  */
    @Nullable public static TaskLogInfo create(@Nullable TaskExecutionLog t) {
        if (t == null) return null;
        final TaskLogInfo info = new TaskLogInfo(t.getName());
        info.setStartTime(t.getStartTime());
        info.setStatus(t.getStatus());
        info.setMember(t.getMember());
        info.setData(t.getData());
        info.setDataTime(t.getDataTime());
        info.setEndTime(t.getEndTime());
        info.setErrorMessage(t.getErrorMessage());
        info.setMds(t.getMdc());
        info.setUpdateTime(t.getUpdateTime());
        info.setSuccessItems(t.getSuccessItemsCount());
        info.setFailedItems(t.getErrorItemsCount());
        info.setIgnoredItems(t.getIgnoreItemsCount());
        info.setTotalItems(t.getTotalItems());
        return info;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7235451972564766827L;
}  // end class TaskLogInfo
