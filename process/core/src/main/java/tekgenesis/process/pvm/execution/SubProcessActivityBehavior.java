
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
 * Behavior for activities that delegate to a complete separate execution of a process definition.
 * This can be used to implement a reusable subprocess.
 */
@SuppressWarnings("WeakerAccess")
public interface SubProcessActivityBehavior extends ActivityBehavior {

    //~ Methods ......................................................................................................................................

    /**
     * called after the process instance is destroyed for this activity to perform its outgoing
     * control flow logic.
     */
    void completed(ActivityExecution execution);

    /**
     * called before the process instance is destroyed to allow this activity to extract data from
     * the sub process instance. No control flow should be done on the execution yet.
     */
    void completing(DelegateExecution execution, DelegateExecution subProcessInstance);
}
