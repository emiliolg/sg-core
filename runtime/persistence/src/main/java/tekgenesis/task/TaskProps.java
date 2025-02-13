
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import javax.inject.Named;

import tekgenesis.common.core.Times;
import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;
import tekgenesis.metadata.task.TaskConstants;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * Cache properties.
 */
@Mutable
@Named("task")
@SuppressWarnings({ "WeakerAccess", "MagicNumber" })
public class TaskProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public String cron = "";

    public int  maxRetries     = -1;                        // -1 : No retry
    public int  maxRunningTime = DEFAULT_MAX_RUNNING_TIME;  // seconds
    public long retryDelay     = 300;                       // millis

    //~ Methods ......................................................................................................................................

    /** Returns true id the task is disabled. */
    public boolean isEnabled() {
        return isNotEmpty(cron) && !TaskConstants.NEVER_CRON.equals(cron);
    }

    //~ Static Fields ................................................................................................................................

    public static int DEFAULT_MAX_RUNNING_TIME = Times.SECONDS_HOUR;
}
