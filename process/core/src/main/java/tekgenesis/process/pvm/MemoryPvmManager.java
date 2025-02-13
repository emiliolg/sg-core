
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.process.pvm.impl.ExecutionImpl;
import tekgenesis.process.pvm.impl.ProcessDefinitionImpl;

/**
 * MemoryPvmManager is used to use pvm in memory.
 */
public class MemoryPvmManager implements PvmManager {

    //~ Methods ......................................................................................................................................

    @Override public void delete(@NotNull String id) {
        executions.remove(id);
    }

    @Override public Iterable<ExecutionImpl> findChildExecutionsByParentExecutionId(@NotNull String id) {
        final List<ExecutionImpl> result = new ArrayList<>();
        for (final ExecutionImpl exec : executions.values()) {
            if (id.equals(exec.getParentId())) result.add(exec);
        }
        return result;
    }

    @Override public ProcessDefinitionImpl findDeployedProcessDefinitionById(@NotNull String processDefinitionId) {
        return definitions.get(processDefinitionId);
    }

    @Override public ExecutionImpl findExecutionById(@NotNull String id) {
        return executions.get(id);
    }

    @Override public ExecutionImpl findSubProcessInstanceBySuperExecutionId(@NotNull String id) {
        for (final ExecutionImpl exec : executions.values()) {
            if (id.equals(exec.getSuperExecutionId())) return exec;
        }
        return null;
    }

    @Override public void initialize() {}

    @Override public void insert(@NotNull ExecutionImpl executionImpl) {
        executions.put(executionImpl.getId(), executionImpl);
    }

    @Override public void insertDeployedProcessDefinition(@NotNull ProcessDefinitionImpl processDefinition) {
        definitions.put(processDefinition.getId(), processDefinition);
    }

    @Override public void update(@NotNull ExecutionImpl executionImpl) {}

    //~ Static Fields ................................................................................................................................

    private static final Map<String, ExecutionImpl>         executions  = new HashMap<>();
    private static final Map<String, ProcessDefinitionImpl> definitions = new HashMap<>();
}  // end class MemoryPvmManager
