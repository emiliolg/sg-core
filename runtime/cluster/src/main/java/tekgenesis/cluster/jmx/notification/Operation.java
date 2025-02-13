
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.notification;

/**
 * Enum for App operations.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public enum Operation {

    //~ Enum constants ...............................................................................................................................

    COMPONENT_INFO("getComponents"), MEMORY_USAGE("getMemoryUsage"), UPDATE_VERSION("upgrade"), TASK_SERVICE_RUNNING("isTaskServiceRunning"),
    EMAIL_SERVICE_RUNNING("isMailProcessorRunning"), MAIL_STATS("getMailProcessorStats"), CLEAN_MAIL_QUEUE("cleanMailQueue"),
    RETRY_MAILS("retryMailSendFor"), CPU_USAGE("getCpuUsage"), UPTIME("getUpTime"), SERVICES("getServices"), START_SERVICE("startService"),
    STOP_SERVICE("stopService");

    //~ Instance Fields ..............................................................................................................................

    private final String methodName;

    //~ Constructors .................................................................................................................................

    Operation(String methodName) {
        this.methodName = methodName;
    }

    //~ Methods ......................................................................................................................................

    /** Return method name to invoke. */
    public String getMethodName() {
        return methodName;
    }
}
