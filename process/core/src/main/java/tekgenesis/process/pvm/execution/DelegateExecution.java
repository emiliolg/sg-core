
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.execution;

import tekgenesis.process.pvm.ProcessElement;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.PvmEvent;

/**
 * Execution used in {@link JavaDelegate}s and {@link ExecutionListener}s.
 */
public interface DelegateExecution extends VariableScope {

    //~ Methods ......................................................................................................................................

    /** Add an execution. */
    void addExecution(ActivityExecution execution);

    /** Gets the id of the current activity. */
    String getCurrentActivityId();

    /** Gets the name of the current activity. */
    String getCurrentActivityName();

    /** Returns the Delete reason. */
    String getDeleteReason();

    /** Returns the Event Source. */
    ProcessElement getEventSource();

    /**
     * The {@link PvmEvent } in case this execution is passed in for an {@link ExecutionListener}.
     */
    PvmEvent getEventType();

    /**
     * Unique id of this path of execution that can be used as a handle to provide external signals
     * back into the engine after wait states.
     */
    String getId();

    /**
     * Gets the id of the parent of this execution. If null, the execution represents a
     * process-instance.
     */
    String getParentId();

    /** The process definition key for the process instance this execution is associated with. */
    String getProcessDefinitionId();

    /** Returns the processInstance associated with this execution. */
    ProcessInstance getProcessInstance();

    /** Reference to the overall process instance. */
    String getProcessInstanceId();
}  // end interface DelegateExecution
