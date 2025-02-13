
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.execution;

/**
 * The way an activity is executed.
 */
public interface ActivityBehavior {

    //~ Methods ......................................................................................................................................

    /** Executes this activity in the specified context. */
    void execute(ActivityExecution execution);
}
