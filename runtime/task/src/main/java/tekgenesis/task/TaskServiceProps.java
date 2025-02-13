
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
import tekgenesis.service.ServiceProps;

/**
 * Properties for the task service.
 */
@Named("taskservice")
public class TaskServiceProps extends ServiceProps {

    //~ Instance Fields ..............................................................................................................................

    public int     concurrentTasks = 5;
    public boolean enableSchedule  = true;

    //~ Instance initializers ........................................................................................................................

    {
        shutdownTimeout = Times.MILLIS_MINUTE / 2;
    }
}
