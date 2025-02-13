
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.test.pvm;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.process.interceptor.RetryInterceptor;
import tekgenesis.process.pvm.PvmManager;
import tekgenesis.process.pvm.execution.AtomicOperation;
import tekgenesis.process.pvm.impl.ExecutionImpl;
import tekgenesis.process.pvm.impl.ProcessDefinitionImpl;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.logging.Logger.Level;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * Base class for Pvm Tests with persistence.
 */
@SuppressWarnings("JavaDoc")
public class PvmPersistedTests {

    //~ Methods ......................................................................................................................................

    @AfterClass public static void afterAll() {
        LogConfig.stop();
    }

    @BeforeClass public static void beforeAll() {
        Context.getContext().unbind(PvmManager.class);
        Context.bind(PvmManager.class, TestingPvmManager.class);
        LogConfig.start();
        LogConfig.bridgeJul();
        getLogger(AtomicOperation.class).setLevel(Level.WARNING);
        getLogger(RetryInterceptor.class).setLevel(Level.WARNING);
        getLogger(ExecutionImpl.class).setLevel(Level.WARNING);
        testingPvmManager = (TestingPvmManager) Context.getSingleton(PvmManager.class);
    }

    //~ Static Fields ................................................................................................................................

    protected static TestingPvmManager testingPvmManager = null;

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    static class ExecutionBean {
        String              activityId;
        String              businessKey;
        String              id;
        Boolean             isActive;
        Boolean             isConcurrent;
        Boolean             isEventScope;
        Boolean             isScope;
        Map<String, Object> localVariables;
        String              parentId;
        String              processDefinitionId;
        String              processInstanceId;
        String              superExecutionId;

        ExecutionBean(String activityId, String businessKey, String id, Boolean isActive, Boolean isConcurrent, Boolean isEventScope, Boolean isScope,
                      String parentId, String processDefinitionId, String superExecutionId, Map<String, Object> localVariables,
                      String processInstanceId) {
            this.activityId          = activityId;
            this.businessKey         = businessKey;
            this.id                  = id;
            this.isActive            = isActive;
            this.isConcurrent        = isConcurrent;
            this.isEventScope        = isEventScope;
            this.isScope             = isScope;
            this.parentId            = parentId;
            this.processDefinitionId = processDefinitionId;
            this.superExecutionId    = superExecutionId;
            this.localVariables      = localVariables;
            this.processInstanceId   = processInstanceId;
        }

        ExecutionImpl buildExecutionPersistence() {
            final ExecutionImpl executionEntity = new ExecutionImpl(id, localVariables);
            executionEntity.setProcessDefinitionId(processDefinitionId);
            executionEntity.setActivityId(activityId);
            executionEntity.setBusinessKey(businessKey);
            executionEntity.setParentId(parentId);
            executionEntity.setSuperExecutionId(superExecutionId);
            executionEntity.setActive(isActive);
            executionEntity.setScope(isScope);
            executionEntity.setConcurrent(isConcurrent);
            executionEntity.setEventScope(isEventScope);
            executionEntity.setProcessInstanceId(processInstanceId);
            return executionEntity;
        }
    }  // end class ExecutionBean

    static class TestingPvmManager implements PvmManager {
        Map<String, ExecutionImpl>         cached      = new HashMap<>();
        Map<String, ProcessDefinitionImpl> definitions = new HashMap<>();

        Map<String, ExecutionBean> persisted = new HashMap<>();

        @Override public void delete(@NotNull String id) {
            cached.remove(id);
            persisted.remove(id);
        }

        @Override public Iterable<ExecutionImpl> findChildExecutionsByParentExecutionId(@NotNull final String id) {
            return filter(cached.values(), e -> e != null && id.equals(e.getParentId()));
        }

        @Override public ProcessDefinitionImpl findDeployedProcessDefinitionById(@NotNull String processDefinitionId) {
            return definitions.get(processDefinitionId);
        }

        @Override public ExecutionImpl findExecutionById(@NotNull String id) {
            return cached.get(id);
        }

        @Nullable @Override public ExecutionImpl findSubProcessInstanceBySuperExecutionId(@NotNull final String id) {
            return filter(cached.values(), e -> e != null && id.equals(e.getParentId())).getFirst().getOrNull();
        }

        @Override public void initialize() {}

        @Override public void insert(@NotNull ExecutionImpl executionEntity) {
            if (executionEntity.getParentId() == null) insertAll(executionEntity);
        }

        @Override public void insertDeployedProcessDefinition(@NotNull ProcessDefinitionImpl processDefinition) {
            definitions.put(processDefinition.getId(), processDefinition);
        }

        @Override public void update(@NotNull ExecutionImpl executionImpl) {
            cached.put(executionImpl.getId(), executionImpl);
        }

        void reload(Iterable<ExecutionBean> executions) {
            cached.clear();
            persisted.clear();
            for (final ExecutionBean e : executions) {
                cached.put(e.id, e.buildExecutionPersistence());
                persisted.put(e.id, e);
            }
        }

        private void insertAll(ExecutionImpl executionEntity) {
            executionEntity.getExecutions().forEach(this::insertAll);
            insertExecution(executionEntity);
        }

        private void insertExecution(ExecutionImpl e) {
            cached.put(e.getId(), e);
            persisted.put(e.getId(),
                new ExecutionBean(e.getActivityId(),
                    e.getBusinessKey(),
                    e.getId(),
                    e.isActive(),
                    e.isConcurrent(),
                    e.isEventScope(),
                    e.isScope(),
                    e.getParentId(),
                    e.getProcessDefinitionId(),
                    e.getSuperExecutionId(),
                    e.getVariablesLocal(),
                    e.getProcessInstanceId()));
        }
    }  // end class TestingPvmManager
}  // end class PvmPersistedTests
