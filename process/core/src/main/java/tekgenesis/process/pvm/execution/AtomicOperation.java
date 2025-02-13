
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

import tekgenesis.common.logging.Logger;
import tekgenesis.process.pvm.*;
import tekgenesis.process.pvm.impl.ActivityImpl;
import tekgenesis.process.pvm.impl.ProcessDefinitionImpl;
import tekgenesis.process.pvm.impl.TransitionImpl;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.process.pvm.PvmEvent.*;

/**
 * @author  Tom Baeyens
 * @author  Daniel Meyer
 */
public enum AtomicOperation {

    //~ Enum constants ...............................................................................................................................

    PROCESS_START(START) {
        Scope getScope(InterpretableExecution execution) { return execution.getProcessDefinition(); }

        @Override protected void doExecute(InterpretableExecution e) {
            final ProcessDefinitionImpl processDefinition = (ProcessDefinitionImpl) e.getProcessDefinition();
            e.setActivity(processDefinition.getInitialActivityStack(e.getStartingExecution().getInitial()).get(0));
            e.performOperation(PROCESS_START_INITIAL);
        }},

    PROCESS_START_INITIAL(START) {
        @Override protected void doExecute(InterpretableExecution e) {
            final ProcessDefinitionImpl processDefinition = (ProcessDefinitionImpl) e.getProcessDefinition();
            final Activity              initial           = e.getStartingExecution().getInitial();
            if (e.getActivity() == initial) {
                e.disposeStartingExecution();
                e.performOperation(ACTIVITY_EXECUTE);
            }
            else {
                final List<Activity> initialActivityStack = processDefinition.getInitialActivityStack(initial);
                final int            index                = initialActivityStack.indexOf(e.getActivity());
                final Activity       activity             = initialActivityStack.get(index + 1);

                final InterpretableExecution executionToUse = activity.isScope() ? e.getExecutions().get(0) : e;
                executionToUse.setActivity(activity);
                executionToUse.performOperation(PROCESS_START_INITIAL);
            }
        }},

    PROCESS_END(END) {
        Scope getScope(InterpretableExecution execution) { return execution.getProcessDefinition(); }

        @Override protected void doExecute(InterpretableExecution e) {
            final InterpretableExecution superExecution             = e.getSuperExecution();
            SubProcessActivityBehavior   subProcessActivityBehavior = null;

            // copy variables before destroying the ended sub process instance
            if (superExecution != null) {
                final Activity activity = superExecution.getActivity();
                subProcessActivityBehavior = (SubProcessActivityBehavior) activity.getActivityBehavior();
                subProcessActivityBehavior.completing(superExecution, e);
            }

            e.destroy();
            e.remove();

            // and trigger execution afterwards
            if (superExecution != null) {
                superExecution.setSubProcessInstance(null);
                subProcessActivityBehavior.completed(superExecution);
            }
        }},
    ACTIVITY_START(START) {
        @Override protected void doExecute(InterpretableExecution execution) {
            execution.performOperation(ACTIVITY_EXECUTE);  //
        }},

    ACTIVITY_EXECUTE(NONE) {
        @Override protected void doExecute(InterpretableExecution e) {
            final Activity         activity         = e.getActivity();
            final ActivityBehavior activityBehavior = activity.getActivityBehavior();
            log.info(e + " executes " + activity + ": " + activityBehavior.getClass().getName());
            activityBehavior.execute(e);
        }},

    ACTIVITY_END(END) {
        @Override protected void doExecute(InterpretableExecution e) {
            final Activity activity       = e.getActivity();
            final Activity parentActivity = activity.getParentActivity();

            // if the execution is a single path of execution inside the process definition scope
            if (parentActivity != null && !parentActivity.isScope()) {
                e.setActivity(parentActivity);
                e.performOperation(ACTIVITY_END);
            }
            else if (e.isProcessInstance()) e.performOperation(PROCESS_END);
            else if (e.isScope()) {
                final ActivityBehavior parentActivityBehavior = parentActivity != null ? parentActivity.getActivityBehavior() : null;
                if (parentActivityBehavior instanceof CompositeActivityBehavior) {
                    final CompositeActivityBehavior compositeActivityBehavior = (CompositeActivityBehavior) parentActivity.getActivityBehavior();

                    if (activity.isScope() && activity.getOutgoingTransitions().isEmpty()) {
                        // there is no transition destroying the scope
                        final InterpretableExecution parentScopeExecution = e.getParent();
                        e.destroy();
                        e.remove();
                        parentScopeExecution.setActivity(parentActivity);
                        compositeActivityBehavior.lastExecutionEnded(parentScopeExecution);
                    }
                    else {
                        e.setActivity(parentActivity);
                        compositeActivityBehavior.lastExecutionEnded(e);
                    }
                }
                else {
                    // default destroy scope behavior
                    final InterpretableExecution parentScopeExecution = e.getParent();
                    e.destroy();
                    e.remove();
                    // if we are a scope under the process instance
                    // and have no outgoing transitions: end the process instance here
                    if (activity.getParent() == activity.getProcessDefinition() && activity.getOutgoingTransitions().isEmpty()) {
                        parentScopeExecution.setActivity(activity);
                        // we call end() because it sets isEnded on the execution
                        parentScopeExecution.end();
                    }
                    else {
                        parentScopeExecution.setActivity(parentActivity);
                        parentScopeExecution.performOperation(ACTIVITY_END);
                    }
                }
            }
            else {  // execution.isConcurrent() && !execution.isScope()

                e.remove();

                // prune if necessary
                final InterpretableExecution concurrentRoot = e.getParent();
                if (concurrentRoot.getExecutions().size() == 1) {
                    final InterpretableExecution lastConcurrent = concurrentRoot.getExecutions().get(0);
                    if (!lastConcurrent.isScope()) {
                        concurrentRoot.setActivity(lastConcurrent.getActivity());
                        lastConcurrent.setReplacedBy(concurrentRoot);

                        // Move children of lastConcurrent one level up
                        if (!lastConcurrent.getExecutions().isEmpty()) {
                            concurrentRoot.getExecutions().clear();
                            for (final ActivityExecution childExecution : lastConcurrent.getExecutions()) {
                                concurrentRoot.addExecution(childExecution);
                                ((InterpretableExecution) childExecution).setParent(concurrentRoot);
                            }
                            lastConcurrent.getExecutions().clear();
                        }

                        // Copy execution-local variables of lastConcurrent
                        // concurrentRoot.setVariablesLocal(lastConcurrent.getVariablesLocal());

                        // Make sure parent execution is re-activated when the last concurrent child execution is active
                        if (!concurrentRoot.isActive() && lastConcurrent.isActive()) concurrentRoot.setActive(true);

                        lastConcurrent.remove();
                    }
                    else lastConcurrent.setConcurrent(false);
                }
            }
        }  // end method doExecute
    },

    TRANSITION_NOTIFY_LISTENER_END(END) {
        @Override protected void doExecute(InterpretableExecution e) {
            e.performOperation(TRANSITION_DESTROY_SCOPE);  //
        }},

    TRANSITION_DESTROY_SCOPE(NONE) {
        private boolean transitionLeavesNextOuterScope(Scope nextScopeElement, Activity destination) {
            return !nextScopeElement.contains(destination);
        }

        @Override protected void doExecute(InterpretableExecution e) {
            final InterpretableExecution propagatingExecution;

            final Activity activity = e.getActivity();
            // if this transition is crossing a scope boundary
            if (activity.isScope()) {
                // if this is a concurrent execution crossing a scope boundary
                if (e.isConcurrent() && !e.isScope()) {
                    // first remove the execution from the current root
                    final InterpretableExecution concurrentRoot      = e.getParent();
                    final InterpretableExecution parentScopeInstance = e.getParent().getParent();

                    log.info("moving concurrent " + e + " one scope up under " + parentScopeInstance);

                    final List<InterpretableExecution> parentScopeInstanceExecutions = cast(parentScopeInstance.getExecutions());
                    final List<InterpretableExecution> concurrentRootExecutions      = cast(concurrentRoot.getExecutions());
                    // if the parent scope had only one single scope child
                    if (parentScopeInstanceExecutions.size() == 1)
                    // it now becomes a concurrent execution
                    parentScopeInstanceExecutions.get(0).setConcurrent(true);

                    concurrentRootExecutions.remove(e);
                    parentScopeInstanceExecutions.add(e);
                    e.setParent(parentScopeInstance);
                    e.setActivity(activity);
                    propagatingExecution = e;

                    // if there is only a single concurrent execution left
                    // in the concurrent root, auto-prune it.  meaning, the
                    // last concurrent child execution data should be cloned into
                    // the concurrent root.
                    if (concurrentRootExecutions.size() == 1) {
                        final InterpretableExecution lastConcurrent = concurrentRootExecutions.get(0);
                        if (lastConcurrent.isScope()) lastConcurrent.setConcurrent(false);
                        else {
                            log.info("merging last concurrent " + lastConcurrent + " into concurrent root " + concurrentRoot);

                            // We can't just merge the data of the lastConcurrent into the concurrentRoot.
                            // This is because the concurrent root might be in a takeAll-loop.  So the
                            // concurrent execution is the one that will be receiving the take
                            concurrentRoot.setActivity(lastConcurrent.getActivity());
                            concurrentRoot.setActive(lastConcurrent.isActive());
                            lastConcurrent.setReplacedBy(concurrentRoot);
                            lastConcurrent.remove();
                        }
                    }
                }
                else if (e.isConcurrent() && e.isScope()) {
                    log.info("scoped concurrent " + e + " becomes concurrent and remains under " + e.getParent());

                    // TODO!
                    e.destroy();
                    propagatingExecution = e;
                }
                else {
                    propagatingExecution = e.getParent();
                    propagatingExecution.setActivity(e.getActivity());
                    propagatingExecution.setTransition(e.getTransition());
                    propagatingExecution.setActive(true);
                    log.info("destroy scope: scoped " + e + " continues as parent scope " + propagatingExecution);
                    e.destroy();
                    e.remove();
                }
            }
            else propagatingExecution = e;

            // if there is another scope element that is ended
            final Scope          nextOuterScopeElement = activity.getParent();
            final TransitionImpl transition            = propagatingExecution.getTransition();
            final Activity       destination           = transition.getDestination();
            if (transitionLeavesNextOuterScope(nextOuterScopeElement, destination)) {
                propagatingExecution.setActivity((ActivityImpl) nextOuterScopeElement);
                propagatingExecution.performOperation(TRANSITION_NOTIFY_LISTENER_END);
            }
            else propagatingExecution.performOperation(TRANSITION_NOTIFY_LISTENER_TAKE);
        }},

    TRANSITION_NOTIFY_LISTENER_TAKE(NONE) {
        @Override protected void doExecute(InterpretableExecution e) {
            final TransitionImpl          transition = e.getTransition();
            final List<ExecutionListener> listeners  = transition.getExecutionListeners();

            if (notify(e, listeners, PvmEvent.TAKE, transition)) e.performOperation(this);
            else {
                // log.info(e + " takes transition " + transition);
                e.setExecutionListenerIndex(0);
                e.setEventType(NONE);
                e.setEventSource(null);

                final Activity activity  = e.getActivity();
                final Activity nextScope = activity.getParent().findNextScope(transition.getDestination());
                e.setActivity(nextScope);
                e.performOperation(TRANSITION_CREATE_SCOPE);
            }
        }},

    TRANSITION_CREATE_SCOPE(NONE) {
        @Override boolean isAsynchronous(InterpretableExecution execution) { return execution.getActivity().isAsynchronous(); }

        @Override protected void doExecute(InterpretableExecution e) {
            final Activity activity = e.getActivity();
            if (activity.isScope()) {
                final InterpretableExecution propagatingExecution = (InterpretableExecution) e.createExecution();
                propagatingExecution.setActivity(activity);
                propagatingExecution.setTransition(e.getTransition());
                e.setTransition(null);
                e.setActivity(null);
                e.setActive(false);
                log.info("Create scope: parent " + e + " continues as execution " + propagatingExecution);
                propagatingExecution.initialize();
                propagatingExecution.performOperation(TRANSITION_NOTIFY_LISTENER_START);
            }
            else e.performOperation(TRANSITION_NOTIFY_LISTENER_START);
        }},

    TRANSITION_NOTIFY_LISTENER_START(START) {
        @Override protected void doExecute(InterpretableExecution e) {
            final Transition transition  = e.getTransition();
            final Activity   destination = transition == null ? e.getActivity() : transition.getDestination();

            final Activity activity = e.getActivity();
            if (activity != destination) {
                e.setActivity(activity.findNextScope(destination));
                e.performOperation(TRANSITION_CREATE_SCOPE);
            }
            else {
                e.setTransition(null);
                e.setActivity(destination);
                e.performOperation(ACTIVITY_EXECUTE);
            }
        }},
    DELETE_CASCADE(NONE) {
        @Override protected void doExecute(InterpretableExecution e) {
            final InterpretableExecution firstLeaf  = findFirstLeaf(e);
            final InterpretableExecution subProcess = firstLeaf.getSubProcessInstance();

            if (subProcess != null) subProcess.deleteCascade(e.getDeleteReason());
            firstLeaf.performOperation(DELETE_CASCADE_FIRE_ACTIVITY_END);
        }

        private InterpretableExecution findFirstLeaf(InterpretableExecution e) {
            return e.getExecutions().isEmpty() ? e : findFirstLeaf(e.getExecutions().get(0));
        }},

    DELETE_CASCADE_FIRE_ACTIVITY_END(END) {
        @Override protected void doExecute(InterpretableExecution execution) {
            final Activity activity = execution.getActivity();
            if (execution.isScope() && activity != null && !activity.isScope()) {
                execution.setActivity(activity.getParentActivity());
                execution.performOperation(DELETE_CASCADE_FIRE_ACTIVITY_END);
            }
            else {
                if (execution.isScope()) execution.destroy();
                execution.remove();
                if (!execution.isDeleteRoot()) {
                    final InterpretableExecution parent = execution.getParent();
                    if (parent != null) parent.performOperation(DELETE_CASCADE);
                }
            }
        }

        Scope getScope(InterpretableExecution execution) {
            final Activity activity = execution.getActivity();

            if (activity != null) return activity;

            final InterpretableExecution parent = execution.getParent();
            return parent != null ? getScope(parent) : execution.getProcessDefinition();
        }};

    //~ Instance Fields ..............................................................................................................................

    private final PvmEvent event;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    AtomicOperation(PvmEvent event) {
        this.event = event;
    }

    //~ Methods ......................................................................................................................................

    /** Execute the operation in the specified execution. */
    public void execute(InterpretableExecution e) {
        if (event == NONE) doExecute(e);
        else {
            final Scope                   scope     = getScope(e);
            final List<ExecutionListener> listeners = scope.getExecutionListeners(event);

            if (notify(e, listeners, event, scope)) e.performOperation(this);
            else {
                e.setExecutionListenerIndex(0);
                e.setEventType(PvmEvent.NONE);
                e.setEventSource(null);
                doExecute(e);
            }
        }
    }  // end method execute

    protected abstract void doExecute(InterpretableExecution execution);

    PvmEvent getEvent() {
        return event;
    }

    /** Returns true if the operations is Asynchronous. */
    boolean isAsynchronous(InterpretableExecution execution) {
        return false;
    }

    @SuppressWarnings("WeakerAccess")
    Scope getScope(InterpretableExecution execution) {
        return execution.getActivity();
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    static boolean notify(InterpretableExecution e, List<ExecutionListener> listeners, PvmEvent eventType, ProcessElement eventSource) {
        final int index = e.getExecutionListenerIndex();

        if (listeners.size() <= index) return false;

        e.setEventType(eventType);
        e.setEventSource(eventSource);

        final ExecutionListener listener = listeners.get(index);
        listener.notify(e);
        e.setExecutionListenerIndex(index + 1);
        return true;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger log = Logger.getLogger(AtomicOperation.class);
}
