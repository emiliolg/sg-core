
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

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.exists;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class EventScopesTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    /**
     * create evt scope --+ | v.
     *
     * <p>+------------------------------+ | embedded sub process | +-----+ | +-----------+
     * +---------+ | +----+ +---+ |start|-->| |startInside|-->|endInside| |-->|wait|-->|end| +-----+
     * | +-----------+ +---------+ | +----+ +---+ +------------------------------+</p>
     *
     * <p>/\ | destroy evt scope --+</p>
     */
    @Test public void activityEndDestroysEventScopes() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Activity End destroys Event Scopes").createActivity("start")
                                                    .initial()
                                                    .behavior(new Automatic())
                                                    .transition("embeddedsubprocess")
                                                    .endActivity()
                                                    .createActivity("embeddedsubprocess")
                                                    .scope()
                                                    .behavior(new EventScopeCreatingSubProcess())
                                                    .createActivity("startInside")
                                                    .behavior(new Automatic())
                                                    .transition("endInside")
                                                    .endActivity()
                                                    .createActivity("endInside")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .transition("wait")
                                                    .endActivity()
                                                    .createActivity("wait")
                                                    .behavior(new WaitState())
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(exists(processInstance.getExecutions(), e -> e != null && e.isEventScope())).isTrue();
        processInstance.signal();
        assertThat(processInstance.isEnded()).isTrue();
    }

    /**
     * +----------------------------------------------------------------------+ | embedded sub
     * process | | | | create evt scope --+ | | | | | v | | | | +--------------------------------+ |
     * | | nested embedded sub process | | +-----+ | +-----------+ | +-----------------+ | +----+
     * +---+ | +---+ |start|-->| |startInside|--> | |startNestedInside| |-->|wait|-->|end| |-->|end|
     * +-----+ | +-----------+ | +-----------------+ | +----+ +---+ | +---+ |
     * +--------------------------------+ | | |
     * +----------------------------------------------------------------------+.
     *
     * <p>/\ | destroy evt scope --+</p>
     */
    @Test public void transitionDestroysEventScope() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Transition destroys Event Scope").createActivity("start")
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
                                                    .behavior(new EventScopeCreatingSubProcess())
                                                    .createActivity("startNestedInside")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .transition("wait")
                                                    .endActivity()
                                                    .createActivity("wait")
                                                    .behavior(new WaitState())
                                                    .transition("endInside")
                                                    .endActivity()
                                                    .createActivity("endInside")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .transition("end")
                                                    .endActivity()
                                                    .createActivity("end")
                                                    .behavior(new Automatic())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.start();

        assertThat(processInstance.findActiveActivityIds().get(0)).isEqualTo("wait");

        processInstance.findExecution("wait").signal();
        assertThat(processInstance.isEnded()).isTrue();
    }  // end method transitionDestroysEventScope
}  // end class EventScopesTest
