
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

/**
 */
@SuppressWarnings("WeakerAccess")
public interface TaskMBean extends Serializable {

    //~ Methods ......................................................................................................................................

    /** Stops the current execution of the task. Nothing, If the task is not running */
    void stop();

    /** Returns the completion status a number from 0 to 100.* */
    int getCompletion();

    /** @return  The phase name */
    String getPhase();
}
