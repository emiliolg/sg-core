
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

import org.junit.Test;

import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessDefinitionBuilder;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.PvmExecution;
import tekgenesis.process.pvm.impl.ExecutionImpl;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.process.test.pvm.PvmTests.Automatic;
import static tekgenesis.process.test.pvm.PvmTests.ParallelGateway;
import static tekgenesis.process.test.pvm.PvmTests.WaitState;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class PvmPersistedParallelTest extends PvmPersistedTests {

    //~ Methods ......................................................................................................................................

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
        processInstance.setVariable("var1", "1");
        processInstance.start();

        final String id = processInstance.getId();

        final PvmExecution c1 = processInstance.findExecution("c1");
        c1.setVariable("var2", "2");
        assertThat(c1).isNotNull();

        final PvmExecution c2 = processInstance.findExecution("c2");
        c1.setVariable("var3", "3");
        assertThat(c2).isNotNull();

        final List<ExecutionBean> beans = new ArrayList<>(testingPvmManager.persisted.values());

        testingPvmManager.reload(beans);

        final ExecutionImpl instance = testingPvmManager.findExecutionById(id);

        assertThat(instance.getVariable("var1")).isEqualTo("1");

        final ExecutionImpl c1b = instance.findExecution("c1");
        assertThat(c1b).isNotNull();
        assertThat(c1b.getVariable("var2")).isEqualTo("2");
        final ExecutionImpl c2b = instance.findExecution("c2");
        assertThat(c2b).isNotNull();
        assertThat(c2b.getVariable("var3")).isEqualTo("3");
        assertThat(c2b.getVariable("var1")).isEqualTo("1");
        c2b.setVariable("var1", "4");

        c1b.signal();
        c2b.signal();
        assertThat(instance.findActiveActivityIds()).containsExactly("end");
        assertThat(instance.getVariable("var1")).isEqualTo("4");
    }  // end method simpleWaitStateConcurrency
}  // end class PvmPersistedParallelTest
