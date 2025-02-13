
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.execution;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.process.pvm.Activity;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.Transition;

/**
 * Sets of operations to execute for a given activity.
 */
public interface ActivityExecution extends DelegateExecution {

    //~ Methods ......................................................................................................................................

    /* Execution management */

    /**
     * creates a new execution. This execution will be the parent of the newly created execution.
     * properties processDefinition, processInstance and activity will be initialized.
     */
    ActivityExecution createExecution();

    /**
     * creates a new sub process instance. The current execution will be the super execution of the
     * created execution.
     */
    ProcessInstance createSubProcessInstance(ProcessDefinition processDefinition);

    /** ends this execution. */
    void end();

    /** Executes the {@link ActivityBehavior} associated with the given activity. */
    void executeActivity(Activity activity);

    /** Retrieves all executions which are concurrent and inactive at the given activity. */
    List<ActivityExecution> findInactiveConcurrentExecutions(Activity activity);

    /**
     * Inactivates this execution. This is useful for example in a join: the execution still exists,
     * but it is not longer active.
     */
    void inactivate();

    /** leaves the current activity by taking the given transition. */
    void take(Transition transition);
    /** Takes the given outgoing transitions. */
    void takeAll(@NotNull List<Transition> outgoingTransitions);

    /**
     * Takes the given outgoing transitions, reusing the given list of executions that were
     * previously joined.
     */
    void takeAll(@NotNull List<Transition> outgoingTransitions, @NotNull List<ActivityExecution> joinedExecutions);

    /** makes this execution active or inactive. */
    void setActive(boolean isActive);
    /** returns the current {@link Activity} of the execution. */
    Activity getActivity();

    /** changes the concurrent indicator on this execution. */
    void setConcurrent(boolean isConcurrent);

    /** returns whether this execution is currently active. */
    boolean isActive();

    /** Executing inside an scope. */
    boolean isScope();

    /** returns the list of execution of which this execution the parent of. */
    List<? extends ActivityExecution> getExecutions();

    /** returns the parent of this execution, or null if there no parent. */
    InterpretableExecution getParent();

    /** returns whether this execution is concurrent or not. */
    boolean isConcurrent();
}  // end interface ActivityExecution
