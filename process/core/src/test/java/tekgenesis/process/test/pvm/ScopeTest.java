
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.test.pvm;

import org.junit.Test;

import tekgenesis.process.pvm.Activity;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessDefinitionBuilder;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.PvmEvent;
import tekgenesis.process.pvm.PvmExecution;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ScopeTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    /**
     * +---------+ |scope | +--+ | +---->|c1| | | | +--+ | | | +-----+ | +----+ | +--+
     * |start|--->|fork|--->|c2| +-----+ | +----+ | +--+ | | | | | | +--+ | +---->|c3| | | +--+
     * +---------+.
     */
    @Test public void concurrentPathsComingOutOfScope() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Concurrent Paths Coming out of Scope").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("scope")
                                                    .scope()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .transition("c3")
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("c1")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .createActivity("c3")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds()).containsOnly("c1", "c2", "c3");
    }

    /**
     * +------------+ |scope | +----------+ | | | v | +-----+ +--------+ | +------+ |
     * |start|-->|parallel|---->|inside| | +-----+ +--------+ | +------+ | | | | | +----------+ | |
     * | +------------+.
     */
    @Test public void concurrentPathsGoingIntoScope() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Concurrent Paths Going into Scope").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("parallel")
                                                    .endActivity()
                                                    .createActivity("parallel")
                                                    .behavior(new ParallelGateway())
                                                    .transition("inside")
                                                    .transition("inside")
                                                    .transition("inside")
                                                    .endActivity()
                                                    .createActivity("scope")
                                                    .scope()
                                                    .createActivity("inside")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds()).containsExactly("inside", "inside", "inside");
    }

    /**
     * +--------------------+ | +----+ | | +---->| c1 |------+ | | +----+ | v +-------+ | +------+ |
     * +------+ +-----+ | start |-->| fork |Scope | | join |-->| end | +-------+ | +------+ |
     * +------+ +-----+ | | +----+ | | | +---->| c2 |------+ | +----+ | +--------------------+.
     */
    @Test public void concurrentPathsGoingOutOfScope() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("scopes and concurrency").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("scope")
                                                    .scope()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .endActivity()
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("c1")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(scopes and concurrency)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(scope)",
            "START on Activity(fork)",
            "END on Activity(fork)",
            "START on Activity(c1)",
            "END on Activity(fork)",
            "START on Activity(c2)");

        eventCollector.clean();

        processInstance.findExecution("c1").signal();
        assertThat(eventCollector.events).containsExactly("END on Activity(c1)", "END on Activity(scope)", "START on Activity(join)");

        eventCollector.clean();

        processInstance.findExecution("c2").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(c2)",
            "END on Activity(scope)",
            "START on Activity(join)",
            "END on Activity(join)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on ProcessDefinition(scopes and concurrency)");
        assertThat(processInstance.isEnded()).isTrue();
    }  // end method concurrentPathsGoingOutOfScope

    /**
     * +--------------------+ | +----+ | +---->| c1 |------+ | | | +----+ v | +-------+ +------+ |
     * +------+ | +-----+ | start |-->| fork | | scope | join |-->| end | +-------+ +------+ |
     * +------+ | +-----+ | | +----+ | | +---->| c2 |------+ | | +----+ | +--------------------+.
     */
    @Test public void concurrentPathsJoiningInsideScope() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("scopes and concurrency").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .endActivity()
                                                    .createActivity("scope")
                                                    .scope()
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("c1")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(scopes and concurrency)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(fork)",
            "END on Activity(fork)",
            "START on Activity(scope)",
            "START on Activity(c1)",
            "END on Activity(fork)",
            "START on Activity(scope)",
            "START on Activity(c2)");

        eventCollector.clean();

        processInstance.findExecution("c1").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(c1)", "START on Activity(join)");
        eventCollector.clean();

        processInstance.findExecution("c2").signal();
        assertThat(eventCollector.events).containsExactly("END on Activity(c2)", "START on Activity(join)");
    }  // end method concurrentPathsJoiningInsideScope

    /**
     * +---------+ | +----+ | +---->| c1 |------+ | | +----+ | v +-------+ +------+ | | +------+
     * +-----+ | start |-->| fork | | no scope| | join |-->| end | +-------+ +------+ | | +------+
     * +-----+ | | +----+ | | +---->| c2 |------+ | +----+ | +---------+.
     */
    @Test public void concurrentPathsThroughNonScopeNestedActivity() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("scopes and concurrency").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .endActivity()
                                                    .createActivity("no-scope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("c1")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(scopes and concurrency)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(fork)",
            "END on Activity(fork)",
            "START on Activity(no-scope)",
            "START on Activity(c1)",
            "END on Activity(fork)",
            "START on Activity(no-scope)",
            "START on Activity(c2)");

        eventCollector.clean();

        processInstance.findExecution("c1").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(c1)", "END on Activity(no-scope)", "START on Activity(join)");

        eventCollector.clean();

        processInstance.findExecution("c2").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(c2)",
            "END on Activity(no-scope)",
            "START on Activity(join)",
            "END on Activity(join)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on ProcessDefinition(scopes and concurrency)");

        assertThat(processInstance.isEnded()).isTrue();
    }  // end method concurrentPathsThroughNonScopeNestedActivity

    /**
     * +---------+ | +----+ | +---->| c1 |------+ | | +----+ | v +-------+ +------+ | | +------+
     * +-----+ | start |-->| fork | | scope | | join |-->| end | +-------+ +------+ | | +------+
     * +-----+ | | +----+ | | +---->| c2 |------+ | +----+ | +---------+.
     */
    @Test public void concurrentPathsThroughScope() {
        final EventCollector eventCollector = new EventCollector();

        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("scopes and concurrency").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .endActivity()
                                                    .createActivity("scope")
                                                    .scope()
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("c1")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(scopes and concurrency)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(fork)",
            "END on Activity(fork)",
            "START on Activity(scope)",
            "START on Activity(c1)",
            "END on Activity(fork)",
            "START on Activity(scope)",
            "START on Activity(c2)");

        eventCollector.clean();

        processInstance.findExecution("c1").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(c1)", "END on Activity(scope)", "START on Activity(join)");
        eventCollector.clean();

        processInstance.findExecution("c2").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(c2)",
            "END on Activity(scope)",
            "START on Activity(join)",
            "END on Activity(join)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on ProcessDefinition(scopes and concurrency)");
        assertThat(processInstance.isEnded()).isTrue();
    }  // end method concurrentPathsThroughScope

    /**
     * +--------------+ | outerScope | +-----+ | +----------+ | +---+
     * |start|--->|scopedWait|--->|end| +-----+ | +----------+ | +---+ +--------------+.
     */
    @Test public void nestedScope() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Nested Scope").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("scopedWait")
                                                    .endActivity()
                                                    .createActivity("outerScope")
                                                    .scope()
                                                    .createActivity("scopedWait")
                                                    .scope()
                                                    .behavior(new WaitState())
                                                    .transition("end")
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        final PvmExecution activityInstance = processInstance.findExecution("scopedWait");
        assertThat(activityInstance).isNotNull();

        activityInstance.signal();

        assertThat(processInstance.findActiveActivityIds()).isEmpty();
        assertThat(processInstance.isEnded()).isTrue();
    }
    /**
     * +--------------------------------------------------------------------------------+ |
     * mostOuterNestedActivity | |
     * +----------------------------------------------------------------------------+ | | |
     * outerScope (scope) | | | | +----------------------------------+
     * +-----------------------------------+ | | | | | firstInnerScope (scope) | | secondInnerScope
     * (scope) | | | | | | +------------------------------+ | | +-------------------------------+ |
     * | | | | | | firstMostInnerNestedActivity | | | | secondMostInnerNestedActivity | | | | | | |
     * | +-------+ +-------------+ | | | | +--------------+ +-----+ | | | | | | | | | start |-->|
     * waitInFirst |--------->| waitInSecond |--> | end | | | | | | | | | +-------+ +-------------+
     * | | | | +--------------+ +-----+ | | | | | | | +------------------------------+ | |
     * +-------------------------------+ | | | | | +----------------------------------+
     * +-----------------------------------+ | | |
     * +----------------------------------------------------------------------------+ |
     * +--------------------------------------------------------------------------------+.
     */
    @Test public void startEndWithScopesAndNestedActivities() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("scopes and events").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("mostOuterNestedActivity")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("outerScope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("firstInnerScope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("firstMostInnerNestedActivity")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("waitInFirst")
                                                    .endActivity()
                                                    .createActivity("waitInFirst")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("waitInSecond")
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("secondInnerScope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("secondMostInnerNestedActivity")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("waitInSecond")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(scopes and events)",
            "START on Activity(mostOuterNestedActivity)",
            "START on Activity(outerScope)",
            "START on Activity(firstInnerScope)",
            "START on Activity(firstMostInnerNestedActivity)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(waitInFirst)");
        eventCollector.clean();

        processInstance.findExecution("waitInFirst").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(waitInFirst)",
            "END on Activity(firstMostInnerNestedActivity)",
            "END on Activity(firstInnerScope)",
            "START on Activity(secondInnerScope)",
            "START on Activity(secondMostInnerNestedActivity)",
            "START on Activity(waitInSecond)");

        eventCollector.clean();

        processInstance.findExecution("waitInSecond").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(waitInSecond)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on Activity(secondMostInnerNestedActivity)",
            "END on Activity(secondInnerScope)",
            "END on Activity(outerScope)",
            "END on Activity(mostOuterNestedActivity)",
            "END on ProcessDefinition(scopes and events)");
    }  // end method startEndWithScopesAndNestedActivities

    /**
     * +--------------------------------------------------------------------------------+ |
     * mostOuterNestedActivity | |
     * +----------------------------------------------------------------------------+ | | |
     * outerScope (scope) | | | | +----------------------------------+
     * +-----------------------------------+ | | | | | firstInnerScope (scope) | | secondInnerScope
     * (scope) | | | | | | +------------------------------+ | | +-------------------------------+ |
     * | | | | | | firstMostInnerNestedActivity | | | | secondMostInnerNestedActivity | | | | | | |
     * | +-------+ +-------------+ | | | | +--------------+ +-----+ | | | | | | | | | start |-->|
     * waitInFirst |--------->| waitInSecond |--> | end | | | | | | | | | +-------+ +-------------+
     * | | | | +--------------+ +-----+ | | | | | | | +------------------------------+ | |
     * +-------------------------------+ | | | | | +----------------------------------+
     * +-----------------------------------+ | | |
     * +----------------------------------------------------------------------------+ |
     * +--------------------------------------------------------------------------------+.
     */
    @Test public void startEndWithScopesAndNestedActivitiesNotAtInitial() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("scopes and events").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("mostOuterNestedActivity")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("outerScope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("firstInnerScope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("firstMostInnerNestedActivity")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("waitInFirst")
                                                    .endActivity()
                                                    .createActivity("waitInFirst")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("waitInSecond")
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("secondInnerScope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("secondMostInnerNestedActivity")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("waitInSecond")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .build();

        final Activity alternativeInitial = processDefinition.findActivity("waitInFirst");

        final ProcessInstance processInstance = processDefinition.createProcessInstanceForInitial(alternativeInitial);
        processInstance.start();
        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(scopes and events)",
            "START on Activity(mostOuterNestedActivity)",
            "START on Activity(outerScope)",
            "START on Activity(firstInnerScope)",
            "START on Activity(firstMostInnerNestedActivity)",
            "START on Activity(waitInFirst)");
        eventCollector.clean();

        processInstance.findExecution("waitInFirst").signal();
        assertThat(eventCollector.events).containsExactly("END on Activity(waitInFirst)",
            "END on Activity(firstMostInnerNestedActivity)",
            "END on Activity(firstInnerScope)",
            "START on Activity(secondInnerScope)",
            "START on Activity(secondMostInnerNestedActivity)",
            "START on Activity(waitInSecond)");
        eventCollector.clean();

        processInstance.findExecution("waitInSecond").signal();
        assertThat(eventCollector.events).containsExactly("END on Activity(waitInSecond)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on Activity(secondMostInnerNestedActivity)",
            "END on Activity(secondInnerScope)",
            "END on Activity(outerScope)",
            "END on Activity(mostOuterNestedActivity)",
            "END on ProcessDefinition(scopes and events)");
    }  // end method startEndWithScopesAndNestedActivitiesNotAtInitial

    /** +-----+ +----------+ +---+ |start|-->|scopedWait|-->|end| +-----+ +----------+ +---+. */
    @Test public void waitStateScope() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Wait State Scope").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("scopedWait")
                                                    .endActivity()
                                                    .createActivity("scopedWait")
                                                    .scope()
                                                    .behavior(new WaitState())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        final PvmExecution execution = processInstance.findExecution("scopedWait");
        assertThat(execution).isNotNull();

        execution.signal();
        assertThat(processInstance.findActiveActivityIds()).isEmpty();
        assertThat(processInstance.isEnded()).isTrue();
    }
}  // end class ScopeTest
