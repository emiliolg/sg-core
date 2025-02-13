
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
 * An Activity behavior that supports signalling.
 */
public interface SignallableActivityBehavior extends ActivityBehavior {

    //~ Methods ......................................................................................................................................

    /** Sends a signal. */
    void signal(ActivityExecution execution, String signalEvent, Object signalData);
}
