
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.test.pvm;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.logging.Logger;
import tekgenesis.process.interceptor.RetryInterceptor;
import tekgenesis.process.pvm.*;
import tekgenesis.process.pvm.execution.*;
import tekgenesis.process.pvm.impl.ExecutionImpl;

import static java.lang.String.format;

import static tekgenesis.common.logging.Logger.Level;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * Base class for Pvm Tests.
 */
@SuppressWarnings("JavaDoc")
public abstract class PvmTests {

    //~ Methods ......................................................................................................................................

    @AfterClass public static void afterAll() {
        LogConfig.stop();
    }
    @BeforeClass public static void beforeAll() {
        Context.getContext().unbind(PvmManager.class);

        Context.bind(PvmManager.class, MemoryPvmManager.class);
        LogConfig.start();
        LogConfig.bridgeJul();
        getLogger(AtomicOperation.class).setLevel(Level.WARNING);
        getLogger(RetryInterceptor.class).setLevel(Level.WARNING);
        getLogger(ExecutionImpl.class).setLevel(Level.WARNING);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Bare automatic activity.
     */
    static class Automatic implements ActivityBehavior {
        @Override public void execute(ActivityExecution execution) {
            final List<Transition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
            if (outgoingTransitions.isEmpty()) execution.end();
            else execution.take(outgoingTransitions.get(0));
        }
    }

    static class EmbeddedSubProcess implements CompositeActivityBehavior {
        @Override public void execute(ActivityExecution execution) {
            for (final Activity activity : execution.getActivity().getActivities()) {
                if (activity.getIncomingTransitions().isEmpty()) execution.executeActivity(activity);
            }
        }

        // def timerFires(execution:ActivityExecution, signalName:String, signalData:AnyRef) {
        // execution.takeAll(execution.getActivity().getOutgoingTransitions)
        // }

        @Override public void lastExecutionEnded(ActivityExecution execution) {
            final List<Transition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
            if (outgoingTransitions.isEmpty()) execution.end();
            else execution.takeAll(outgoingTransitions);
        }
    }

    static class End implements ActivityBehavior {
        @Override public void execute(ActivityExecution execution) {
            execution.end();
        }
    }

    static class EventCollector implements ExecutionListener {
        final List<String> events = new ArrayList<>();

        @Override public void notify(DelegateExecution execution) {
            log.info("collecting event: " + execution.getEventType() + " on " + execution.getEventSource());
            events.add(execution.getEventType() + " on " + execution.getEventSource());
        }

        void clean() {
            events.clear();
        }

        private static final Logger log = getLogger(EventCollector.class);
    }

    static class EventScopeCreatingSubProcess implements CompositeActivityBehavior {
        @Override public void execute(ActivityExecution execution) {
            for (final Activity a : execution.getActivity().getActivities()) {
                if (a.getIncomingTransitions().isEmpty()) execution.executeActivity(a);
            }
        }

        @Override public void lastExecutionEnded(ActivityExecution execution) {
            final InterpretableExecution outgoingExecution = (InterpretableExecution) execution.getParent().createExecution();
            outgoingExecution.setConcurrent(false);
            outgoingExecution.setActivity(execution.getActivity());

            execution.setConcurrent(false);
            execution.setActive(false);
            ((InterpretableExecution) execution).setEventScope(true);

            final List<Transition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
            if (outgoingTransitions.isEmpty()) outgoingExecution.end();
            else outgoingExecution.takeAll(outgoingTransitions);
        }
    }

    static class ParallelGateway implements ActivityBehavior {
        @Override public void execute(ActivityExecution execution) {
            final Activity activity = execution.getActivity();

            final List<Transition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();

            execution.inactivate();

            final List<ActivityExecution> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);

            final int nbrOfExecutionsToJoin = execution.getActivity().getIncomingTransitions().size();
            final int nbrOfExecutionsJoined = joinedExecutions.size();

            final boolean activates = nbrOfExecutionsJoined == nbrOfExecutionsToJoin;
            log.info(
                format("Parallel gateway '%s' %s %d of %d joined",
                    activity.getId(),
                    activates ? "activates" : "does not activate",
                    nbrOfExecutionsJoined,
                    nbrOfExecutionsToJoin));
            if (activates) execution.takeAll(outgoingTransitions, joinedExecutions);
        }

        private static final Logger log = getLogger(ParallelGateway.class);
    }

    static class ReusableSubProcess implements SubProcessActivityBehavior {
        private final ProcessDefinition processDefinition;

        ReusableSubProcess(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }

        @Override public void completed(ActivityExecution execution) {
            execution.takeAll(execution.getActivity().getOutgoingTransitions());
        }

        @Override public void completing(DelegateExecution execution, DelegateExecution subProcessInstance) {
            // TODO extract information from the sub-process and inject it into the super-process
            System.out.println();
        }

        @Override public void execute(ActivityExecution execution) {
            final ProcessInstance subProcessInstance = execution.createSubProcessInstance(processDefinition);
            // TODO set variables
            subProcessInstance.start();
        }
    }

    static class WaitState extends Automatic implements SignallableActivityBehavior {
        @Override public void execute(ActivityExecution execution) {}

        @Override public void signal(ActivityExecution execution, String signalEvent, Object signalData) {
            super.execute(execution);
        }
    }

    static class While implements ActivityBehavior {
        private final String done;
        private final int    from;
        private final String more;
        private final int    to;

        private final String variableName;

        While(String variableName, int from, int to, String more, String done) {
            this.variableName = variableName;
            this.from         = from;
            this.to           = to;
            this.more         = more;
            this.done         = done;
        }

        @Override public void execute(ActivityExecution execution) {
            final String next;
            if (!execution.hasVariable(variableName)) {
                execution.setVariable(variableName, from);
                next = more;
            }
            else {
                final int value = execution.getVariable(variableName, Integer.class) + 1;
                if (value < to) {
                    execution.setVariable(variableName, value);
                    next = more;
                }
                else next = done;
            }
            execution.take(execution.getActivity().findOutgoingTransition(next));
        }
    }
}
