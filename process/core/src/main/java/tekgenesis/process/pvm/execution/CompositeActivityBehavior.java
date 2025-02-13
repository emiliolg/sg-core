
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
 * The way a composite activity is executed.
 */
@SuppressWarnings("WeakerAccess")
public interface CompositeActivityBehavior extends ActivityBehavior {

    //~ Methods ......................................................................................................................................

    /** To be executed when the last execution is ended. */
    void lastExecutionEnded(ActivityExecution execution);
}
