
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

import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessDefinitionBuilder;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.PvmEvent;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class EventTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    /**
     * +------------------------------+ +-----+ | +-----------+ +----------+ | +---+ |start|-->|
     * |startInside|-->|endInside | |-->|end| +-----+ | +-----------+ +----------+ | +---+
     * +------------------------------+.
     */
    @Test public void embeddedSubProcessEvents() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("events").executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("endInside")
                                                    .endActivity()
                                                    .createActivity("endInside")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
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

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(events)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(embeddedsubprocess)",
            "START on Activity(startInside)",
            "END on Activity(startInside)",
            "START on Activity(endInside)",
            "END on Activity(endInside)",
            "END on Activity(embeddedsubprocess)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on ProcessDefinition(events)");
    }  // end method embeddedSubProcessEvents

    /**
     * +---------------+ |outer scope | | +-----------+ | | |inner scope| | +-----+ | | +----+ | |
     * +---+ |start|---->|wait|-------->|end| +-----+ | | +----+ | | +---+ | +-----------+ |
     * +---------------+.
     */
    @Test public void nestedActivitiesEventsOnTransitionEvents() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("events").executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .startTransition("wait", "")
                                                    .executionListener(eventCollector)
                                                    .endTransition()
                                                    .endActivity()
                                                    .createActivity("outerscope")
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("innerscope")
                                                    .scope()
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("wait")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("end")
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(events)",
            "START on Activity(start)",
            "END on Activity(start)",
            "TAKE on (start)---->(wait)",
            "START on Activity(outerscope)",
            "START on Activity(innerscope)",
            "START on Activity(wait)");

        eventCollector.clean();
        processInstance.findExecution("wait").signal();

        assertThat(eventCollector.events).containsExactly("END on Activity(wait)", "END on Activity(innerscope)", "END on Activity(outerscope)");
    }  // end method nestedActivitiesEventsOnTransitionEvents

    /**
     * +--+ +--->|c1|---+ | +--+ | | v +-----+ +----+ +----+ +---+ |start|-->|fork| |join|-->|end|
     * +-----+ +----+ +----+ +---+ | | | +--+ | +--->|c2|---+ +--+.
     */
    @Test public void simpleAutomaticConcurrencyEvents() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("events").executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
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
                                                    .createActivity("c1")
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new Automatic())
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
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        processDefinition.createProcessInstance().start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(events)",
            "START on Activity(start)",
            "END on Activity(start)",
            "START on Activity(fork)",
            "END on Activity(fork)",
            "START on Activity(c1)",
            "END on Activity(c1)",
            "START on Activity(join)",
            "END on Activity(fork)",
            "START on Activity(c2)",
            "END on Activity(c2)",
            "START on Activity(join)",
            "END on Activity(join)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on ProcessDefinition(events)");
    }  // end method simpleAutomaticConcurrencyEvents
    /** +-------+ +-----+ | start |-->| end | +-------+ +-----+. */
    @Test public void testStartEndEvents() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("events").executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .startTransition("end")
                                                    .executionListener(eventCollector)
                                                    .endTransition()
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(events)",
            "START on Activity(start)",
            "END on Activity(start)",
            "TAKE on (start)---->(end)",
            "START on Activity(end)",
            "END on Activity(end)",
            "END on ProcessDefinition(events)");
    }
}  // end class EventTest
