
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
import tekgenesis.process.pvm.PvmExecution;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class PvmParallelTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    @Test public void joinForkCombinedInOneParallelGateway() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Join Fork Combined in one Parallel Gateway").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .transition("c3")
                                                    .endActivity()
                                                    .createActivity("c1")
                                                    .behavior(new Automatic())
                                                    .transition("join1")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new Automatic())
                                                    .transition("join1")
                                                    .endActivity()
                                                    .createActivity("c3")
                                                    .behavior(new Automatic())
                                                    .transition("join2")
                                                    .endActivity()
                                                    .createActivity("join1")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c4")
                                                    .transition("c5")
                                                    .transition("c6")
                                                    .endActivity()
                                                    .createActivity("c4")
                                                    .behavior(new Automatic())
                                                    .transition("join2")
                                                    .endActivity()
                                                    .createActivity("c5")
                                                    .behavior(new Automatic())
                                                    .transition("join2")
                                                    .endActivity()
                                                    .createActivity("c6")
                                                    .behavior(new Automatic())
                                                    .transition("join2")
                                                    .endActivity()
                                                    .createActivity("join2")
                                                    .behavior(new ParallelGateway())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.findExecution("end")).isNotNull();
    }  // end method joinForkCombinedInOneParallelGateway

    @Test public void simpleAutomaticConcurrency() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Simple Automatic Concurrency").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .endActivity()
                                                    .createActivity("c1")
                                                    .behavior(new Automatic())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new Automatic())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new End())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.isEnded()).isTrue();
    }

    @Test public void simpleWaitStateConcurrency() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Simple Wait State Concurrency").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .endActivity()
                                                    .createActivity("c1")
                                                    .behavior(new WaitState())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new WaitState())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        final PvmExecution c1 = processInstance.findExecution("c1");
        assertThat(c1).isNotNull();

        final PvmExecution c2 = processInstance.findExecution("c2");
        assertThat(c2).isNotNull();

        c1.signal();
        c2.signal();

        assertThat(processInstance.findActiveActivityIds()).containsExactly("end");
    }  // end method simpleWaitStateConcurrency

    @Test public void unstructuredConcurrencyTwoForks() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Unstructured Concurrency Two Forks").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork1")
                                                    .endActivity()
                                                    .createActivity("fork1")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .transition("fork2")
                                                    .endActivity()
                                                    .createActivity("c1")
                                                    .behavior(new Automatic())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new Automatic())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("fork2")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c3")
                                                    .transition("c4")
                                                    .endActivity()
                                                    .createActivity("c3")
                                                    .behavior(new Automatic())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("c4")
                                                    .behavior(new Automatic())
                                                    .transition("join")
                                                    .endActivity()
                                                    .createActivity("join")
                                                    .behavior(new ParallelGateway())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.findExecution("end")).isNotNull();
    }  // end method unstructuredConcurrencyTwoForks

    @Test public void unstructuredConcurrencyTwoJoins() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Unstructured Concurrency Two Joins").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("fork")
                                                    .endActivity()
                                                    .createActivity("fork")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c1")
                                                    .transition("c2")
                                                    .transition("c3")
                                                    .endActivity()
                                                    .createActivity("c1")
                                                    .behavior(new Automatic())
                                                    .transition("join1")
                                                    .endActivity()
                                                    .createActivity("c2")
                                                    .behavior(new Automatic())
                                                    .transition("join1")
                                                    .endActivity()
                                                    .createActivity("c3")
                                                    .behavior(new Automatic())
                                                    .transition("join2")
                                                    .endActivity()
                                                    .createActivity("join1")
                                                    .behavior(new ParallelGateway())
                                                    .transition("c4")
                                                    .endActivity()
                                                    .createActivity("c4")
                                                    .behavior(new Automatic())
                                                    .transition("join2")
                                                    .endActivity()
                                                    .createActivity("join2")
                                                    .behavior(new ParallelGateway())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();
        assertThat(processInstance.findExecution("end")).isNotNull();
    }  // end method unstructuredConcurrencyTwoJoins
}  // end class PvmParallelTest
