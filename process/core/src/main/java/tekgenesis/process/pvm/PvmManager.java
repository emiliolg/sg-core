
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import org.jetbrains.annotations.NotNull;

import tekgenesis.process.pvm.impl.ExecutionImpl;
import tekgenesis.process.pvm.impl.ProcessDefinitionImpl;

/**
 * Used to manage Pvm Executions and ProcessDefinitions.
 */
@SuppressWarnings("EmptyMethod")
public interface PvmManager {

    //~ Methods ......................................................................................................................................

    /**
     * Deletes an Execution from cache and.
     *
     * @param  id:  ExecutionId
     */
    void delete(@NotNull final String id);

    /**
     * finds ExecutionImpl by parent execution Id.
     *
     * @param   id  parentId
     *
     * @return  children executions
     */
    Iterable<ExecutionImpl> findChildExecutionsByParentExecutionId(@NotNull String id);

    /**
     * finds ProcessDefinition by Id.
     *
     * @param   processDefinitionId:
     *
     * @return  ProcessDefinition implementation if exists
     */
    ProcessDefinitionImpl findDeployedProcessDefinitionById(@NotNull String processDefinitionId);

    /**
     * finds ExecutionImpl by Id.
     *
     * @param   id:
     *
     * @return  Execution if exists
     */
    ExecutionImpl findExecutionById(@NotNull String id);

    /**
     * finds SubProcessInstance By SuperExecutionId.
     *
     * @param   id:
     *
     * @return  subProcess execution if exists
     */
    ExecutionImpl findSubProcessInstanceBySuperExecutionId(@NotNull String id);

    /** initialize PvmManager. */
    void initialize();

    /**
     * inserts an ExecutionImpl.
     *
     * @param  executionImpl:
     */
    void insert(@NotNull final ExecutionImpl executionImpl);

    /**
     * inserts ProcessDefinition.
     *
     * @param  processDefinition:
     */
    void insertDeployedProcessDefinition(@NotNull ProcessDefinitionImpl processDefinition);

    /**
     * update an ExecutionImpl in memory... this method must *NOT* persist the execution impl
     *
     * @param  executionImpl:
     */
    void update(@NotNull final ExecutionImpl executionImpl);
}  // end interface PvmManager
