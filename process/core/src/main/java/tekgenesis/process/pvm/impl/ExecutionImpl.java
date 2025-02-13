
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.impl;

import java.io.Serializable;
import java.util.*;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.process.exception.PvmException;
import tekgenesis.process.pvm.*;
import tekgenesis.process.pvm.execution.*;

/**
 * An Implementation of an execution.
 */
@SuppressWarnings({ "ClassWithTooManyFields", "OverlyComplexClass", "ClassWithTooManyMethods", "WeakerAccess", "JavaDoc" })
public class ExecutionImpl implements Serializable, ActivityExecution, DelegateExecution, PvmExecution, ProcessInstance, InterpretableExecution {

    //~ Instance Fields ..............................................................................................................................

    /** current activity. */
    private Activity       activity     = null;
    private String         activityId   = null;
    private String         activityName = null;
    private boolean        alive        = true;
    private String         businessKey  = null;
    private String         deleteReason = null;
    private boolean        deleteRoot   = false;
    private ProcessElement eventSource  = null;

    private PvmEvent eventType              = null;
    private int      executionListenerIndex = 0;

    /** nested executions representing scopes or concurrent paths. */
    private List<ExecutionImpl> executions = null;

    private final String id;

    /**
     * indicates if this execution represents an active path of execution. Executions are made
     * inactive in the following situations:
     *
     * <ul>
     *   <li>an execution enters a nested scope</li>
     *   <li>an execution is split up into multiple concurrent executions, then the parent is made
     *     inactive.</li>
     *   <li>an execution has arrived in a parallel gateway or join and that join has not yet
     *     activated/fired.</li>
     *   <li>an execution is ended.</li>
     * </ul>
     */
    private boolean isActive     = true;
    private boolean isConcurrent = false;
    private boolean isEnded      = false;
    private boolean isEventScope = false;
    private boolean isScope      = true;

    /**
     * when execution structure is pruned during a takeAll, then the original execution has to be
     * resolved to the replaced execution. protected ExecutionEntity replacedBy; // atomic
     * operations //////////////////////////////////////////////////////// /** next operation.
     * process execution is in fact runtime interpretation of the process model. each operation is a
     * logical unit of interpretation of the process. so sequentially processing the operations
     * drives the interpretation or execution of a process.
     *
     * @see  AtomicOperation
     * @see  #performOperation(AtomicOperation)
     */
    private AtomicOperation nextOperation = null;

    private boolean notOperating = true;

    /** the parent execution. */
    private ExecutionImpl parent = null;

    /**
     * persisted reference to the parent of this execution.
     *
     * @see  #getParent()
     */
    private String parentId = null;

    private ProcessDefinition processDefinition = null;

    /**
     * persisted reference to the processDefinition.
     *
     * @see  #processDefinition
     * @see  #getProcessDefinition()
     */
    private String processDefinitionId = null;

    /**
     * the process instance. this is the root of the execution tree. the processInstance of a
     * process instance is a self reference.
     */
    private ExecutionImpl processInstance = null;

    /**
     * persisted reference to the process instance.
     *
     * @see  #getProcessInstance()
     */
    private String processInstanceId = null;

    private ExecutionImpl replacedBy = null;

    private StartingExecution startingExecution = null;

    // state/type of execution //////////////////////////////////////////////////

    /**
     * reference to a subprocessinstance, not-null if currently subprocess is started from this
     * execution.
     */
    private ExecutionImpl subProcessInstance = null;

    /** super execution, not-null if this execution is part of a subprocess. */
    private ExecutionImpl superExecution = null;

    /** persisted reference to the super execution of this execution. */
    private String superExecutionId = null;

    /** current transition. is null when there is no transition being taken. */
    private TransitionImpl transition = null;

    private final Map<String, Object> variables;

    //~ Constructors .................................................................................................................................

    public ExecutionImpl() {
        this(UUID.randomUUID().toString());
    }

    public ExecutionImpl(String existentId) {
        this(existentId, new LinkedHashMap<>());
    }

    public ExecutionImpl(Activity activityImpl) {
        this(UUID.randomUUID().toString());
        startingExecution = new StartingExecution(activityImpl);
    }

    public ExecutionImpl(String existentId, Map<String, Object> variables) {
        id             = existentId;
        this.variables = variables;
    }

    //~ Methods ......................................................................................................................................

    public void addExecution(ActivityExecution execution) {
        executions.add((ExecutionImpl) execution);
    }

    /**
     * creates a new execution. properties processDefinition, processInstance and activity will be
     * initialized.
     */
    public ExecutionImpl createExecution() {
        // create the new child execution
        final ExecutionImpl createdExecution = newExecution();

        // manage the bidirectional parent-child relation
        ensureExecutionsInitialized();
        executions.add(createdExecution);
        createdExecution.setParent(this);

        // initialize the new execution
        createdExecution.setProcessDefinition(getProcessDefinition());
        createdExecution.setProcessInstance(getProcessInstance());
        createdExecution.setActivity(getActivity());

        log.debug(() -> "Child execution " + createdExecution + " created with parent " + this);
        getPvmManager().insert(createdExecution);
        return createdExecution;
    }

    public ProcessInstance createSubProcessInstance(ProcessDefinition pd) {
        final ExecutionImpl subProcess = newExecution();

        // manage bidirectional super-subprocess relation
        subProcess.setSuperExecution(this);
        setSubProcessInstance(subProcess);

        // Initialize the new execution
        subProcess.setProcessDefinition(pd);
        subProcess.setProcessInstance(subProcess);

        getPvmManager().insert(subProcess);

        return subProcess;
    }

    public void deleteCascade(String reason) {
        deleteReason = reason;
        deleteRoot   = true;
        performOperation(AtomicOperation.DELETE_CASCADE);
    }

    public void destroy() {
        ensureParentInitialized();
        setScope(false);
    }

    public void disposeStartingExecution() {
        startingExecution = null;
    }

    /**
     * removes an execution. if there are nested executions, those will be ended recursively. if
     * there is a parent, this method removes the bidirectional relation between parent and this
     * execution.
     */
    public void end() {
        isActive = false;
        isEnded  = true;
        getPvmManager().update(this);
        performOperation(AtomicOperation.ACTIVITY_END);
    }

    public void executeActivity(Activity act) {
        setActivity(act);
        performOperation(AtomicOperation.ACTIVITY_START);
    }

    public List<String> findActiveActivityIds() {
        final List<String> activeActivityIds = new ArrayList<>();
        collectActiveActivityIds(activeActivityIds);
        return activeActivityIds;
    }

    /** searches for an execution positioned in the given activity. */
    public ExecutionImpl findExecution(String actId) {
        if ((getActivity() != null) && (getActivity().getId().equals(actId))) return this;
        for (final ExecutionImpl nestedExecution : getExecutions()) {
            final ExecutionImpl result = nestedExecution.findExecution(actId);
            if (result != null) return result;
        }
        return null;
    }

    public List<ActivityExecution> findInactiveConcurrentExecutions(Activity act) {
        final List<ActivityExecution> inactiveConcurrentExecutionsInActivity = new ArrayList<>();
        final List<ActivityExecution> otherConcurrentExecutions              = new ArrayList<>();
        if (isConcurrent()) {
            final List<? extends ActivityExecution> concurrentExecutions = getParent().getAllChildExecutions();
            for (final ActivityExecution concurrentExecution : concurrentExecutions) {
                if (act.equals(concurrentExecution.getActivity())) {
                    if (!concurrentExecution.isActive()) inactiveConcurrentExecutionsInActivity.add(concurrentExecution);
                }
                else otherConcurrentExecutions.add(concurrentExecution);
            }
        }
        else {
            if (!isActive()) inactiveConcurrentExecutionsInActivity.add(this);
            else otherConcurrentExecutions.add(this);
        }

        log.info("inactive concurrent executions in '" + act + "': " + inactiveConcurrentExecutionsInActivity);
        log.info("other concurrent executions: " + otherConcurrentExecutions);

        return inactiveConcurrentExecutionsInActivity;
    }

    @Override public boolean hasVariable(String variableName) {
        return variables.containsKey(variableName) || parent != null && parent.hasVariable(variableName);
    }

    public void inactivate() {
        isActive = false;
        getPvmManager().update(this);
    }

    public void initialize() {}

    public void insert() {
        getPvmManager().insert(this);
    }

    /**
     * next operation. process execution is in fact runtime interpretation of the process model.
     * each operation is a logical unit of interpretation of the process. so sequentially processing
     * the operations drives the interpretation or execution of a process.
     */
    public void performOperation(AtomicOperation executionOperation) {
        setNextOperation(executionOperation);
        if (notOperating) {
            notOperating = false;
            while (hasNextOperation()) {
                final AtomicOperation currentOperation = nextOperation;
                setNextOperation(null);
                log.info("AtomicOperation: " + currentOperation + " on " + this);
                currentOperation.execute(this);
                getPvmManager().update(this);
            }
            notOperating = true;
            if (alive) getPvmManager().insert(this);
        }
    }

    // customized persistence behaviour /////////////////////////////////////////

    public void remove() {
        ensureParentInitialized();
        if (parent != null) {
            parent.ensureExecutionsInitialized();
            parent.executions.remove(this);
            // getPvmManager().update(parent);
        }

        removeEventScopes();

        alive = false;
        getPvmManager().delete(getId());
    }

    public void signal() {
        signal("", null);
    }

    public void signal(@NotNull String signalName, @Nullable Object signalData) {
        ensureActivityInitialized();
        activity.getActivityBehavior(SignallableActivityBehavior.class).signal(this, signalName, signalData);
        // SignallableActivityBehavior activityBehavior = (SignallableActivityBehavior) activity.getActivityBehavior();
        // try {
        // activityBehavior.signal(this, signalName, signalData);
        // }
        // catch (RuntimeException e) {
        // throw e;
        // }
        // catch (Exception e) {
        // throw new PvmException("couldn't process signal '" + signalName + "' on activity '" + activity.getId() + "': " +
        // e.getMessage(), e);
        // }
    }

    // process instance start implementation ////////////////////////////////////

    public void start() {
        if (startingExecution == null && isProcessInstance()) startingExecution = new StartingExecution(processDefinition.getInitial());
        performOperation(AtomicOperation.PROCESS_START);
    }

    // methods that translate to operations /////////////////////////////////////

    public void take(Transition trans) {
        if (transition != null) throw new PvmException("already taking a transition");
        if (trans == null) throw new PvmException("transition is null");
        setActivity(trans.getSource());
        setTransition((TransitionImpl) trans);
        performOperation(AtomicOperation.TRANSITION_NOTIFY_LISTENER_END);
    }

    public void takeAll(@NotNull List<Transition> ts) {
        takeAll(ts, Collections.emptyList());
    }

    @SuppressWarnings("OverlyLongMethod")
    public void takeAll(@NotNull List<Transition> ts, @NotNull List<ActivityExecution> rs) {
        final List<ActivityExecution> recyclableExecutions = new ArrayList<>(rs);

        if (recyclableExecutions.size() > 1) {
            for (final ActivityExecution recyclableExecution : recyclableExecutions) {
                if ((recyclableExecution).isScope()) throw new PvmException("joining scope executions is not allowed");
            }
        }

        final ExecutionImpl       concurrentRoot               = ((isConcurrent && !isScope) ? getParent() : this);
        final List<ExecutionImpl> concurrentActiveExecutions   = new ArrayList<>();
        final List<ExecutionImpl> concurrentInActiveExecutions = new ArrayList<>();
        for (final ExecutionImpl execution : concurrentRoot.getExecutions()) {
            if (execution.isActive()) concurrentActiveExecutions.add(execution);
            else concurrentInActiveExecutions.add(execution);
        }

        final List<Transition> transitions = new ArrayList<>(ts);
        log.info("transitions to take concurrent: " + transitions);
        log.info("active concurrent executions: " + concurrentActiveExecutions);

        if ((transitions.size() == 1) && (concurrentActiveExecutions.isEmpty()) && allExecutionsInSameActivity(concurrentInActiveExecutions)) {
            for (final ActivityExecution e : recyclableExecutions) {
                final ExecutionImpl prunedExecution = (ExecutionImpl) e;
                // End the pruned executions if necessary.
                // Some recyclable executions are inactivated (joined executions)
                // Others are already ended (end activities)
                if (!prunedExecution.isEnded()) {
                    log.info(PRUNING_EXECUTION + prunedExecution);
                    prunedExecution.remove();
                }
            }

            log.info("activating the concurrent root " + concurrentRoot + " as the single path of execution going forward");
            concurrentRoot.setActive(true);
            concurrentRoot.setActivity(activity);
            concurrentRoot.setConcurrent(false);
            concurrentRoot.take(transitions.get(0));
        }
        else {
            recyclableExecutions.remove(concurrentRoot);

            log.info("recyclable executions for reused: " + recyclableExecutions);

            // first create the concurrent executions
            final List<OutgoingExecution> outgoingExecutions = new ArrayList<>();
            while (!transitions.isEmpty()) {
                final Transition outgoingTransition = transitions.remove(0);

                final ExecutionImpl outgoingExecution;
                if (recyclableExecutions.isEmpty()) {
                    outgoingExecution = concurrentRoot.createExecution();
                    log.info(
                        "new " + outgoingExecution + " with parent " + outgoingExecution.getParent() + " created to take transition " +
                        outgoingTransition);
                }
                else {
                    outgoingExecution = (ExecutionImpl) recyclableExecutions.remove(0);
                    log.info("recycled " + outgoingExecution + " to take transition " + outgoingTransition);
                }

                outgoingExecution.setActive(true);
                outgoingExecution.setScope(false);
                outgoingExecution.setConcurrent(true);
                outgoingExecutions.add(new OutgoingExecution(outgoingExecution, outgoingTransition));
            }

            // prune the executions that are not recycled
            for (final ActivityExecution prunedExecution : recyclableExecutions) {
                log.info(PRUNING_EXECUTION + prunedExecution);
                prunedExecution.end();
            }

            // then launch all the concurrent executions
            for (final OutgoingExecution outgoingExecution : outgoingExecutions)
                outgoingExecution.take();
        }
    }  // end method takeAll

    // toString /////////////////////////////////////////////////////////////////

    public String toString() {
        if (isProcessInstance()) return "ProcessInstance[" + getToStringIdentity() + "]";
        else return (isConcurrent ? "Concurrent" : "") + (isScope ? "Scope" : "") + "Execution[" + getToStringIdentity() + "]" + getActivity();
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        getPvmManager().update(this);
    }

    /** ensures initialization and returns the activity. */
    public Activity getActivity() {
        ensureActivityInitialized();
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        if (activity != null) {
            activityId   = activity.getId();
            activityName = (String) activity.getProperty("name");
        }
        else {
            activityId   = null;
            activityName = null;
        }
        getPvmManager().update(this);
    }

    // parent ///////////////////////////////////////////////////////////////////
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    // bussiness key ////////////////////////////////////////////////////////////

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public void setConcurrent(boolean isConcurrent) {
        this.isConcurrent = isConcurrent;
        getPvmManager().update(this);
    }

    public String getCurrentActivityId() {
        return activityId;
    }

    public String getCurrentActivityName() {
        return activityName;
    }
    public boolean isEnded() {
        return isEnded;
    }

    public String getDeleteReason() {
        return deleteReason;
    }
    public boolean isActive() {
        return isActive;
    }

    public boolean isEventScope() {
        return isEventScope;
    }

    public boolean isProcessInstance() {
        return parentId == null;
    }

    public boolean isScope() {
        return isScope;
    }

    public void setEventScope(boolean isEventScope) {
        this.isEventScope = isEventScope;
        getPvmManager().update(this);
    }

    public ProcessElement getEventSource() {
        return eventSource;
    }

    public void setEventSource(ProcessElement eventSource) {
        this.eventSource = eventSource;
        getPvmManager().update(this);
    }

    @Override public PvmEvent getEventType() {
        return eventType;
    }

    @Override public void setEventType(@NotNull PvmEvent eventType) {
        this.eventType = eventType;
    }
    public Integer getExecutionListenerIndex() {
        return executionListenerIndex;
    }
    public void setExecutionListenerIndex(Integer executionListenerIndex) {
        this.executionListenerIndex = executionListenerIndex;
    }

    /** ensures initialization and returns the non-null executions list. */
    public List<ExecutionImpl> getExecutions() {
        ensureExecutionsInitialized();
        return executions;
    }

    public void setExecutions(List<ExecutionImpl> executions) {
        this.executions = executions;
    }
    public String getId() {
        return id;
    }

    /** ensures initialization and returns the parent. */
    public ExecutionImpl getParent() {
        ensureParentInitialized();
        return parent;
    }

    public void setParent(InterpretableExecution parent) {
        this.parent = (ExecutionImpl) parent;
        if (parent != null) parentId = (parent).getId();
        else parentId = null;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    // process definition ///////////////////////////////////////////////////////

    /** ensures initialization and returns the process definition. */
    public ProcessDefinition getProcessDefinition() {
        ensureProcessDefinitionInitialized();
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
        processDefinitionId    = processDefinition.getId();
    }

    // process instance /////////////////////////////////////////////////////////

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    /** ensures initialization and returns the process instance. */
    public ExecutionImpl getProcessInstance() {
        ensureProcessInstanceInitialized();
        return processInstance;
    }

    public void setProcessInstance(InterpretableExecution processInstance) {
        this.processInstance = (ExecutionImpl) processInstance;
        if (processInstance != null) setProcessInstanceId(this.processInstance.getId());
    }

    public String getProcessInstanceId() {
        if (processInstanceId == null) return getProcessInstance().getId();
        else return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public ExecutionImpl getReplacedBy() {
        return replacedBy;
    }
    public void setReplacedBy(InterpretableExecution replacedBy) {
        this.replacedBy = (ExecutionImpl) replacedBy;
    }

    public void setScope(boolean isScope) {
        this.isScope = isScope;
    }

    public StartingExecution getStartingExecution() {
        return startingExecution;
    }

    public ExecutionImpl getSubProcessInstance() {
        ensureSubProcessInstanceInitialized();
        return subProcessInstance;
    }

    public void setSubProcessInstance(@Nullable InterpretableExecution subProcessInstance) {
        this.subProcessInstance = (ExecutionImpl) subProcessInstance;
    }

    public ExecutionImpl getSuperExecution() {
        ensureSuperExecutionInitialized();
        return superExecution;
    }

    // super- and subprocess executions /////////////////////////////////////////

    public String getSuperExecutionId() {
        return superExecutionId;
    }

    public void setSuperExecutionId(String superExecutionId) {
        this.superExecutionId = superExecutionId;
    }

    public boolean isConcurrent() {
        return isConcurrent;
    }
    public boolean isDeleteRoot() {
        return deleteRoot;
    }

    public TransitionImpl getTransition() {
        return transition;
    }
    public void setTransition(TransitionImpl transition) {
        this.transition = transition;
    }

    // variables ////////////////////////////////////////////////////////////////
    public Object getVariable(String variableName) {
        // If value is found in this scope, return it
        if (variables.containsKey(variableName)) return variables.get(variableName);

        // If value not found in this scope, check the parent scope
        ensureParentInitialized();
        if (parent != null) return parent.getVariable(variableName);

        // Variable is nowhere to be found
        return null;
    }

    @Override public <T> T getVariable(String variableName, Class<T> clazz) {
        return clazz.cast(getVariable(variableName));
    }

    public void setVariable(String variableName, Object value) {
        if (variables.containsKey(variableName)) setVariableLocally(variableName, value);
        else {
            if (parent != null) parent.setVariable(variableName, value);
            else setVariableLocally(variableName, value);
        }
        getPvmManager().update(this);
    }  // end method setVariable

    public Object getVariableLocal(String variableName) {
        return variables.get(variableName);
    }

    public Object setVariableLocal(String variableName, Object value) {
        final Object val = variables.put(variableName, value);
        getPvmManager().update(this);
        return val;
    }

    public Map<String, Object> getVariables() {
        final Map<String, Object> collectedVariables = new HashMap<>();
        collectVariables(collectedVariables);
        return collectedVariables;
    }

    public Map<String, Object> getVariablesLocal() {
        return variables;
    }

    public void setVariablesLocal(Map<String, ?> variables) {}

    private boolean allExecutionsInSameActivity(List<ExecutionImpl> executionList) {
        if (executionList.size() > 1) {
            final String actId = executionList.get(0).getActivityId();
            for (final ExecutionImpl execution : executionList) {
                final String otherActivityId = execution.getActivityId();
                if (!execution.isEnded) {
                    if ((actId == null && otherActivityId != null) || (actId != null && otherActivityId == null) ||
                        (actId != null && !otherActivityId.equals(actId))) return false;
                }
            }
        }
        return true;
    }

    private void collectActiveActivityIds(List<String> activeActivityIds)
    {
        ensureActivityInitialized();
        if (isActive && activity != null) activeActivityIds.add(activity.getId());
        ensureExecutionsInitialized();
        for (final ExecutionImpl execution : executions)
            execution.collectActiveActivityIds(activeActivityIds);
    }

    private void collectVariables(Map<String, Object> collectedVariables) {
        if (parent != null) parent.collectVariables(collectedVariables);

        for (final String variableName : variables.keySet())
            collectedVariables.put(variableName, variables.get(variableName));
    }

    /** must be called before the activity member field or getActivity() is called. */
    private void ensureActivityInitialized() {
        if ((activity == null) && (activityId != null)) activity = getProcessDefinition().findActivity(activityId);
    }
    private void ensureExecutionsInitialized() {
        executions = new ArrayList<>();
        for (final ExecutionImpl execution : getPvmManager().findChildExecutionsByParentExecutionId(id))
            executions.add(execution);
    }

    private void ensureParentInitialized() {
        if (parentId != null) parent = getPvmManager().findExecutionById(parentId);
    }

    /** for setting the process definition, this setter must be used as subclasses can override. */
    private void ensureProcessDefinitionInitialized() {
        if ((processDefinition == null) && (processDefinitionId != null)) {
            final ProcessDefinitionImpl deployedProcessDefinition = getPvmManager().findDeployedProcessDefinitionById(processDefinitionId);
            setProcessDefinition(deployedProcessDefinition);
        }
    }

    private void ensureProcessInstanceInitialized() {
        if (processInstanceId == null)
        // first
        setProcessInstanceId(getId());
        processInstance = getPvmManager().findExecutionById(processInstanceId);
    }

    private void ensureSubProcessInstanceInitialized() {
        subProcessInstance = getPvmManager().findSubProcessInstanceBySuperExecutionId(id);
    }

    private void ensureSuperExecutionInitialized() {
        if (superExecutionId != null) superExecution = getPvmManager().findExecutionById(superExecutionId);
    }

    // event subscription support //////////////////////////////////////////////

    private boolean hasNextOperation() {
        return nextOperation != null;
    }

    private ExecutionImpl newExecution() {
        final ExecutionImpl newExecution = new ExecutionImpl();
        newExecution.executions = new ArrayList<>();

        return newExecution;
    }

    private void removeEventScopes() {
        final List<InterpretableExecution> childExecutions = new ArrayList<>(getExecutions());
        for (final InterpretableExecution childExecution : childExecutions) {
            if (childExecution.isEventScope()) {
                log.info("removing eventScope " + childExecution);
                childExecution.destroy();
                childExecution.remove();
            }
        }
    }

    private List<ExecutionImpl> getAllChildExecutions() {
        final List<ExecutionImpl> childExecutions = new ArrayList<>();
        for (final ExecutionImpl childExecution : getExecutions()) {
            childExecutions.add(childExecution);
            childExecutions.addAll(childExecution.getAllChildExecutions());
        }
        return childExecutions;
    }

    private void setNextOperation(@Nullable AtomicOperation executionOperation) {
        nextOperation = executionOperation;
    }

    @NotNull private PvmManager getPvmManager() {
        return Context.getSingleton(PvmManager.class);
    }

    private void setSuperExecution(@Nullable ExecutionImpl superExecution) {
        this.superExecution = superExecution;
        if (superExecution != null) superExecution.setSubProcessInstance(null);

        if (superExecution != null) superExecutionId = superExecution.getId();
        else superExecutionId = null;
    }

    // scopes ///////////////////////////////////////////////////////////////////

    private String getToStringIdentity() {
        return id;
    }

    private void setVariableLocally(String variableName, Object value) {
        log.info("setting variable '" + variableName + "' to value '" + value + "' on " + this);
        variables.put(variableName, value);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String PRUNING_EXECUTION = "pruning execution ";

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(ExecutionImpl.class);
}  // end class ExecutionImpl
