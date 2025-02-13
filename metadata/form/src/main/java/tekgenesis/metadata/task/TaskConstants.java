
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.task;

import org.jetbrains.annotations.NonNls;

/**
 * Task Constants.
 */
public interface TaskConstants {

    //~ Instance Fields ..............................................................................................................................

    /** Default Purge policy in days. */
    int DEFAULT_PURGE_POLICY = 15;

    String         DEFAULT_TASK_NAME   = " ";
    @NonNls String GET_CRON_EXPRESSION = "getCronExpression";
    @NonNls String GET_SCHEDULE_AFTER  = "getScheduleAfter";

    String MANUAL_EXECUTION = "Manual Execution";
    String MASTER           = "MASTER_SERVER";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    String NEVER_CRON = "never";
}
