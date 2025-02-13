
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.execution;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.process.pvm.*;
import tekgenesis.process.pvm.impl.TransitionImpl;

/**
 * An Execution of the process.
 */
public interface InterpretableExecution extends ActivityExecution, ProcessInstance {

    //~ Methods ......................................................................................................................................

    /** delete this execution and all the descendants. */
    void deleteCascade(String deleteReason);

    /** destroy de execution. */
    void destroy();

    /** Discard the StartingExecution2. */
    void disposeStartingExecution();

    /** initialize the execution. */
    @SuppressWarnings("EmptyMethod")
    void initialize();

    /** Perform an operation. */
    void performOperation(AtomicOperation operation);

    /** remove de execution. */
    void remove();
    void take(Transition transition);

    /** Sets the activity. */
    void setActivity(@Nullable Activity activity);

    /** returns true if this is an event scope. */
    boolean isEventScope();

    /** returns whether this execution is a process instance or not. */
    boolean isProcessInstance();

    /** Returns true if the executions is an scope. */
    boolean isScope();

    /** sets event scope to true. */
    void setEventScope(boolean isEventScope);

    /** Sets the event source. */
    void setEventSource(@Nullable ProcessElement element);

    /** Sets the event type. */
    void setEventType(@NotNull PvmEvent eventType);

    /** Returns the execution listener index. */
    Integer getExecutionListenerIndex();

    /** Sets the execution listener at the specified position. */
    void setExecutionListenerIndex(Integer executionListenerIndex);

    /** Sets the parent for this execution. */
    void setParent(InterpretableExecution parent);

    /** Returns the process definition. */
    ProcessDefinition getProcessDefinition();

    /** Sets the process definition for this execution. */
    void setProcessDefinition(ProcessDefinition processDefinitionImpl);

    /** Sets the process instance for this execution. */
    void setProcessInstance(InterpretableExecution processInstance);

    /** Sets the process instance Id for this execution. Used in loading Executions */
    void setProcessInstanceId(String processInstanceId);

    /** Get the execution that replaces this one. */
    InterpretableExecution getReplacedBy();

    /** Sets the execution to replace this one. */
    void setReplacedBy(InterpretableExecution replacedBy);

    /** Get the StartingExecution for this one. */
    StartingExecution getStartingExecution();

    /** Get the execution for the sub-process. */
    InterpretableExecution getSubProcessInstance();
    /** Sets the execution for the sub-process. */
    void setSubProcessInstance(@Nullable InterpretableExecution subProcessInstance);

    /** Get the super-process execution. */
    InterpretableExecution getSuperExecution();

    /** returns true if this is the delete root. */
    boolean isDeleteRoot();

    /** Get the next transition. */
    TransitionImpl getTransition();

    /** Sets the next transition. */
    void setTransition(@Nullable TransitionImpl object);
}  // end interface InterpretableExecution
