
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
 * Convenience class that should be used when a Java delegation is required (for example, to call
 * custom business logic).
 *
 * <p>This class can be used for both service tasks and event listeners.</p>
 *
 * <p>This class does not allow to influence the control flow. If you are in need of influencing the
 * flow in your process, use the class ActivityBehavior instead.</p>
 */
public interface JavaDelegate {

    //~ Methods ......................................................................................................................................

    /** Executes the delegation. */
    void execute(DelegateExecution execution);
}
