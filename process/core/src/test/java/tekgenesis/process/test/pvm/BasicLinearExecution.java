
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
import tekgenesis.process.pvm.PvmExecution;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic Linear Execution of a process.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class BasicLinearExecution extends PvmTests {

    //~ Methods ......................................................................................................................................

    /** +-----+ +------+ +-------+ | one |-->| wait |-->| three | +-----+ +------+ +-------+. */
    @Test public void singleWait() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("one-wait-three").createActivity("one")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("two")
                                                    .endActivity()
                                                    .createActivity("two")
                                                    .behavior(new WaitState())
                                                    .transition("three")
                                                    .endActivity()
                                                    .createActivity("three")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        final PvmExecution activityInstance = processInstance.findExecution("two");
        assertThat(activityInstance).isNotEqualTo(null);

        activityInstance.signal();

        assertThat(processInstance.findActiveActivityIds()).isEmpty();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /** +-------+ +-----+ | start |-->| end | +-------+ +-----+. */
    @Test public void startEnd() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("start-end").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();
        assertThat(processDefinition.getId()).isEqualTo("start-end");

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds()).isEmpty();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /**
     * +----------------------------+ v | +-------+ +------+ +-----+ +-----+ +-------+ | start |-->|
     * loop |-->| one |-->| two |--> | three | +-------+ +------+ +-----+ +-----+ +-------+ | |
     * +-----+ +-->| end | +-----+.
     */

    @Test public void testWhile() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("while").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("loop")
                                                    .endActivity()
                                                    .createActivity("loop")
                                                    .behavior(new While("count", 0, 5, "more", "done"))
                                                    .transition("one", "more")
                                                    .transition("end", "done")
                                                    .endActivity()
                                                    .createActivity("one")
                                                    .behavior(new Automatic())
                                                    .transition("two")
                                                    .endActivity()
                                                    .createActivity("two")
                                                    .behavior(new Automatic())
                                                    .transition("three")
                                                    .endActivity()
                                                    .createActivity("three")
                                                    .behavior(new Automatic())
                                                    .transition("loop")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds()).isEmpty();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /**
     * +-----+ +-----+ +-------+ +------+ +------+ | one |-->| two |-->| three |-->| four |--> |
     * five | +-----+ +-----+ +-------+ +------+ +------+.
     */
    @Test public void waitsAndAutomatics() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("waits and automatics").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("one")
                                                    .endActivity()
                                                    .createActivity("one")
                                                    .behavior(new WaitState())
                                                    .transition("two")
                                                    .endActivity()
                                                    .createActivity("two")
                                                    .behavior(new WaitState())
                                                    .transition("three")
                                                    .endActivity()
                                                    .createActivity("three")
                                                    .behavior(new Automatic())
                                                    .transition("four")
                                                    .endActivity()
                                                    .createActivity("four")
                                                    .behavior(new Automatic())
                                                    .transition("five")
                                                    .endActivity()
                                                    .createActivity("five")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        final PvmExecution activityInstance = processInstance.findExecution("one");
        assertThat(activityInstance).isNotEqualTo(null);
        activityInstance.signal();

        final PvmExecution activityInstance1 = processInstance.findExecution("two");
        assertThat(activityInstance1).isNotEqualTo(null);
        activityInstance1.signal();

        assertThat(processInstance.findActiveActivityIds()).isEmpty();
        assertThat(processInstance.isEnded()).isTrue();
    }  // end method waitsAndAutomatics
}  // end class BasicLinearExecution
