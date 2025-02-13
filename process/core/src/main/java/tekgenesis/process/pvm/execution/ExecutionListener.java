
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
 * Callback interface to be notified of execution events like starting a process instance, ending an
 * activity instance or taking a transition.
 */
public interface ExecutionListener {

    //~ Methods ......................................................................................................................................

    /** Notify of the the execution. */
    void notify(DelegateExecution execution);
}
