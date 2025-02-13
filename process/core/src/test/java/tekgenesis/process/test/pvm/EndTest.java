
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
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
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
import tekgenesis.process.pvm.PvmEvent;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class EndTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    /**
     * +----+ +--->|end1| | +----+ | +-----+ +----+ |start|-->|fork| +-----+ +----+ | | +----+
     * +--->|end2| +----+.
     */
    @Test public void parallelEnd() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("parallel end").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("end1")
                                                    .transition("end2")
                                                    .endActivity()
                                                    .createActivity("end1")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .createActivity("end2")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }

    @Test public void simpleProcessInstanceEnd() {
        final EventCollector    eventCollector    = new EventCollector();
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Simple ProcessInstance End").executionListener(PvmEvent.START,
                    eventCollector).executionListener(PvmEvent.END, eventCollector)
                                                    .createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("wait")
                                                    .endActivity()
                                                    .createActivity("wait")
                                                    .behavior(new WaitState())
                                                    .executionListener(PvmEvent.START, eventCollector)
                                                    .executionListener(PvmEvent.END, eventCollector)
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(eventCollector.events).containsExactly("START on ProcessDefinition(Simple ProcessInstance End)", "START on Activity(wait)");
        eventCollector.clean();

        processInstance.deleteCascade("test");

        assertThat(eventCollector.events).containsExactly("END on Activity(wait)", "END on ProcessDefinition(Simple ProcessInstance End)");
    }
}  // end class EndTest
