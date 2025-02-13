
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
import static org.assertj.core.api.Assertions.entry;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class PvmVariablesTest extends PvmTests {

    //~ Methods ......................................................................................................................................

    @Test public void variables() {
        final ProcessDefinition processDefinition = new ProcessDefinitionBuilder("Variables").createActivity("a")
                                                    .initial()
                                                    .behavior(new WaitState())
                                                    .endActivity()
                                                    .build();

        final ProcessInstance processInstance = processDefinition.createProcessInstance();
        processInstance.setVariable("amount", 500L);
        processInstance.setVariable("msg", "hello world");
        processInstance.start();

        assertThat(processInstance.getVariable("amount", Long.class)).isEqualTo(500L);
        assertThat(processInstance.getVariable("msg", String.class)).isEqualTo("hello world");

        final PvmExecution activityInstance = processInstance.findExecution("a");
        assertThat(activityInstance.getVariable("amount", Long.class)).isEqualTo(500L);
        assertThat(activityInstance.getVariable("msg", String.class)).isEqualTo("hello world");

        assertThat(processInstance.getVariables()).contains(entry("amount", 500L), entry("msg", "hello world"));
        assertThat(activityInstance.getVariables()).contains(entry("amount", 500L), entry("msg", "hello world"));
    }
}
