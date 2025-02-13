
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at;
 *
 *      http://www.apache.org/licenses/LICENSE-2.0;
 *
 * Unless required by applicable law or agreed to in writing, software;
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tekgenesis.process.test.pvm;

import org.junit.Test;

import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessDefinitionBuilder;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.PvmExecution;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class SubProcessTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    /**
     * +-------------------------------+ | embedded sub process | +-----+ | +-----------+
     * +---------+ | +---+ |start|-->| |startInside|-->|endInside| |-->|end| +-----+ | +-----------+
     * +---------+ | +---+ +-------------------------------+.
     */
    @Test public void embeddedSubProcess() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Embedded SubProcess").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("endInside")
                                                    .endActivity()
                                                    .createActivity("endInside")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds().get(0)).isEqualTo("end");
    }

    /**
     * +------------------------------+ | embedded sub process | +-----+ | +-----------+ +---------+
     * | |start|-->| |startInside|-->|endInside| | +-----+ | +-----------+ +---------+ |
     * +------------------------------+.
     */
    @Test public void embeddedSubProcessNoEnd() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Embedded SubProcess no End").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("endInside")
                                                    .endActivity()
                                                    .createActivity("endInside")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /**
     * +------------------------------+ | embedded sub process | +-----+ | +-----------+ |
     * |start|-->| |startInside| | +-----+ | +-----------+ | +------------------------------+.
     */
    @Test public void embeddedSubProcessWithoutEnd() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Embedded SubProcess without end").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /**
     * +----------------------------------------+ | embedded sub process +----------+ | |
     * +---->|endInside1| | | | +----------+ | | | | +-----+ | +-----------+ +----+ +----------+ |
     * +---+ |start|-->| |startInside|-->|fork|-->|endInside2| |-->|end| +-----+ | +-----------+
     * +----+ +----------+ | +---+ | | | | | +----------+ | | +---->|endInside3| | | +----------+ |
     * +----------------------------------------+.
     */
    @Test public void multipleConcurrentEndsInsideEmbeddedSubProcess() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Multiple Concurrent Ends Inside Embedded SubProcess")
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("endInside1")
                                                    .transition("endInside2")
                                                    .transition("endInside3")
                                                    .endActivity()
                                                    .createActivity("endInside1")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .createActivity("endInside2")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .createActivity("endInside3")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }  // end method multipleConcurrentEndsInsideEmbeddedSubProcess

    /**
     * +---------------------------------------------------+ | embedded sub process +----------+ | |
     * +---->|endInside1| | | | +----------+ | | | | +-----+ | +-----------+ +----+ +----+
     * +----------+ | +---+ |start|-->| |startInside|-->|fork|-->|wait|-->|endInside2| |-->|end|
     * +-----+ | +-----------+ +----+ +----+ +----------+ | +---+ | | | | | +----------+ | |
     * +---->|endInside3| | | +----------+ | +---------------------------------------------------+.
     */
    @Test public void multipleConcurrentEndsInsideEmbeddedSubProcessWithWaitState() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Multiple Ends with Wait").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("endInside1")
                                                    .transition("wait")
                                                    .transition("endInside3")
                                                    .endActivity()
                                                    .createActivity("endInside1")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .createActivity("wait")
                                                    .behavior(new WaitState())
                                                    .transition("endInside2")
                                                    .endActivity()
                                                    .createActivity("endInside2")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .createActivity("endInside3")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isFalse();

        final PvmExecution execution = processInstance.findExecution("wait");
        execution.signal();
        assertThat(processInstance.isEnded()).isTrue();
    }  // end method multipleConcurrentEndsInsideEmbeddedSubProcessWithWaitState

    /**
     * +-------------------------------------------------------+ | embedded sub process | |
     * +--------------------------------+ | | | nested embedded sub process | | +-----+ |
     * +-----------+ | +-----------+ | | |start|--->| |startInside|--> | |startInside| | | +-----+ |
     * +-----------+ | +-----------+ | | | +--------------------------------+ | | |
     * +-------------------------------------------------------+.
     */
    @Test public void nestedSubProcessBothNoEnd() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Nested SubProcess Both no End").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("nestedSubProcess")
                                                    .endActivity()
                                                    .createActivity("nestedSubProcess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startNestedInside")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .endActivity()
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /**
     * +-------------------------------------------------------+ | embedded sub process | |
     * +--------------------------------+ | | | nested embedded sub process | | +-----+ |
     * +-----------+ | +-----------+ +---------+ | | +---+ |start|-->| |startInside|--> |
     * |startInside|-->|endInside| | |-->|end| +-----+ | +-----------+ | +-----------+ +---------+ |
     * | +---+ | +--------------------------------+ | | |
     * +-------------------------------------------------------+.
     */
    @Test public void nestedSubProcessWithNoEnd() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Nested SubProcess with no End").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("nestedSubProcess")
                                                    .endActivity()
                                                    .createActivity("nestedSubProcess")
                                                    .scope()
                                                    .behavior(new EmbeddedSubProcess())
                                                    .createActivity("startNestedInside")
                                                    .behavior(new Automatic())
                                                    .transition("endInside")
                                                    .endActivity()
                                                    .createActivity("endInside")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .endActivity()
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds().get(0)).isEqualTo("end");
    }
    @Test public void reusableSubProcess() {
        final ProcessDefinition subProcessDefinition = new ProcessDefinitionBuilder("Reusable SubProcess").createActivity("start")
                                                       .initial()
                                                       .behavior(new Automatic())
                                                       .transition("subEnd")
                                                       .endActivity()
                                                       .createActivity("subEnd")
                                                       .behavior(new End())
                                                       .endActivity()
                                                       .build();

        final ProcessDefinition superProcessDefinition = new ProcessDefinitionBuilder("super").createActivity("start")
                                                         .initial()
                                                         .behavior(new Automatic())
                                                         .transition("subprocess")
                                                         .endActivity()
                                                         .createActivity("subprocess")
                                                         .behavior(new ReusableSubProcess(subProcessDefinition))
                                                         .transition("superEnd")
                                                         .endActivity()
                                                         .createActivity("superEnd")
                                                         .behavior(new End())
                                                         .endActivity()
                                                         .build();

        final ProcessInstance processInstance = superProcessDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }
}  // end class SubProcessTest
