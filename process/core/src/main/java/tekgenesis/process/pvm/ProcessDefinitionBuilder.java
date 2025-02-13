
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Stack;
import tekgenesis.common.core.Builder;
import tekgenesis.process.exception.PvmBuildException;
import tekgenesis.process.exception.PvmException;
import tekgenesis.process.pvm.execution.ActivityBehavior;
import tekgenesis.process.pvm.execution.ExecutionListener;
import tekgenesis.process.pvm.impl.ActivityImpl;
import tekgenesis.process.pvm.impl.ProcessDefinitionImpl;
import tekgenesis.process.pvm.impl.ScopeImpl;
import tekgenesis.process.pvm.impl.TransitionImpl;

import static tekgenesis.common.Predefined.notNull;

/**
 * A builder for memory process definitions.
 */
public class ProcessDefinitionBuilder implements Builder<ProcessDefinition> {

    //~ Instance Fields ..............................................................................................................................

    private final ProcessDefinitionImpl processDefinition;
    private final Stack<Scope>          scopeStack;
    /** The current transition. */
    private TransitionBuilder             transition;
    private final List<TransitionBuilder> unresolvedTransitions;

    //~ Constructors .................................................................................................................................

    // public ProcessDefinitionBuilder() { this(null); }

    /** Create a process builder to build a process with the given id. */
    public ProcessDefinitionBuilder(@NotNull final String processDefinitionId) {
        processDefinition = createProcessDefinition(processDefinitionId);
        scopeStack        = Stack.createStack();
        scopeStack.push(processDefinition);
        unresolvedTransitions = new ArrayList<>();
        transition            = null;
    }
    //
    // //~ Methods
    // ..................................................................................................................
    //

    //~ Methods ......................................................................................................................................

    /** Plug a behavior for the current activity. */
    public ProcessDefinitionBuilder behavior(ActivityBehavior activityBehaviour) {
        getActivity().setActivityBehavior(activityBehaviour);
        return this;
    }

    /** Build a ProcessDefinition. */
    public ProcessDefinition build() {
        // Build Transitions.
        for (final TransitionBuilder t : unresolvedTransitions)
            t.build();
        return processDefinition;
    }

    /** Create an activity with the specified id. */
    public ProcessDefinitionBuilder createActivity(@NotNull String id) {
        final Activity activity = scopeStack.peek().createActivity(id);
        scopeStack.push(activity);
        transition = null;
        return this;
    }
    /** End the building of an activity. */
    public ProcessDefinitionBuilder endActivity() {
        scopeStack.pop();
        transition = null;
        return this;
    }
    /** Ends the building of a transition. */
    @SuppressWarnings("WeakerAccess")
    public ProcessDefinitionBuilder endTransition() {
        transition = null;
        return this;
    }

    /** Add an execution listener to the current transition. */
    public ProcessDefinitionBuilder executionListener(ExecutionListener executionListener) {
        if (transition == null) throw new PvmException("not in a transition scope");
        transition.addExecutionListener(executionListener);
        return this;
    }

    /** Add an execution listener to the current process or activity. */
    public ProcessDefinitionBuilder executionListener(PvmEvent eventType, ExecutionListener executionListener) {
        if (transition != null) throw new PvmException("not in an activity- or process definition scope. (but in a transition scope)");
        ((ScopeImpl) scopeStack.peek()).addExecutionListener(eventType, executionListener);
        return this;
    }

    /** Mark the current activity as the initial one. */
    public ProcessDefinitionBuilder initial() {
        processDefinition.setInitial(getActivity());
        return this;
    }
    //
    // public ProcessDefinitionBuilder property(String name, Object value) {
    // processElement.setProperty(name, value);
    // return this;
    // }
    //
    /** Build an scope. */
    public ProcessDefinitionBuilder scope() {
        ((ActivityImpl) scopeStack.peek()).setScope(true);
        return this;
    }

    /** Add a transition to the current activity. without an explicit id */
    public ProcessDefinitionBuilder startTransition(@NotNull String destinationActivityId) {
        return startTransition(destinationActivityId, "");
    }

    /** Add a transition to the current activity. with an explicit id */
    @SuppressWarnings("WeakerAccess")
    public ProcessDefinitionBuilder startTransition(@NotNull String destinationActivityId, @NotNull String transitionId) {
        final TransitionBuilder t = new TransitionBuilder(transitionId, processDefinition, getActivity(), destinationActivityId);
        transition = t;
        unresolvedTransitions.add(t);
        return this;
    }

    /** Add a transition to the current activity. with not id */
    public ProcessDefinitionBuilder transition(@NotNull String destinationActivityId) {
        return transition(destinationActivityId, "");
    }

    /** Add a transition to the current activity. with not id */
    @SuppressWarnings("WeakerAccess")
    public ProcessDefinitionBuilder transition(@NotNull String destinationActivityId, @NotNull String transitionId) {
        return startTransition(destinationActivityId, transitionId).endTransition();
    }

    private ProcessDefinitionImpl createProcessDefinition(@NotNull String processDefinitionId) {
        return new ProcessDefinitionImpl(processDefinitionId);
    }

    /** Returns the current activity being processed. */

    private Activity getActivity() {
        return (Activity) scopeStack.peek();
    }

    //~ Inner Classes ................................................................................................................................

    private static class TransitionBuilder implements Builder<Transition> {
        private final String                  id;
        private final List<ExecutionListener> listeners;
        private final ProcessDefinition       pd;
        private final Activity                source;
        private final String                  target;

        private TransitionBuilder(String id, ProcessDefinition pd, Activity source, String target) {
            this.id     = notNull(id);
            this.pd     = pd;
            this.source = source;
            this.target = target;
            listeners   = new ArrayList<>(1);
        }

        public void addExecutionListener(ExecutionListener listener) {
            listeners.add(listener);
        }

        public Transition build() {
            final Activity destination = pd.findActivity(target);
            if (destination == null)
                throw new PvmBuildException("destination '" + target + "' not found.  (referenced from transition in '" + source.getId() + "')");
            return new TransitionImpl(id, pd, source, destination, listeners);
        }
    }
}  // end class ProcessDefinitionBuilder
