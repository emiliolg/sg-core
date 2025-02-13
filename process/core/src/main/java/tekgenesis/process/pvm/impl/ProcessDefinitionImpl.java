
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.impl;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.env.context.Context;
import tekgenesis.process.exception.PvmException;
import tekgenesis.process.pvm.Activity;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessInstance;
import tekgenesis.process.pvm.PvmManager;
import tekgenesis.process.pvm.execution.InterpretableExecution;

/**
 * The Implementation of a ProcessDefinition.
 */

public class ProcessDefinitionImpl extends ScopeImpl implements ProcessDefinition {

    //~ Instance Fields ..............................................................................................................................

    private Activity                            initial;
    private final Map<Activity, List<Activity>> initialActivityStacks;
    private String                              name;

    //~ Constructors .................................................................................................................................

    /** Create a ProcessDefinition. */
    public ProcessDefinitionImpl(String id) {
        super(id, null);
        initial               = null;
        initialActivityStacks = new IdentityHashMap<>();
        name                  = id;
        final PvmManager pvmManager = Context.getSingleton(PvmManager.class);
        pvmManager.insertDeployedProcessDefinition(this);
    }

    //~ Methods ......................................................................................................................................

    public ProcessInstance createProcessInstance() {
        if (initial == null) throw new PvmException("Process '" + name + "' has no default activity (e. g. none start event).");
        return createProcessInstanceForInitial(initial);
    }

    /** creates a process instance using the provided activity as initial. */
    @Override public ProcessInstance createProcessInstanceForInitial(@NotNull Activity first) {
        final InterpretableExecution processInstance = newProcessInstance(first);
        processInstance.setProcessDefinition(this);
        processInstance.setProcessInstance(processInstance);
        processInstance.initialize();

        InterpretableExecution scopeInstance = processInstance;

        for (final Activity initialActivity : getInitialActivityStack(first)) {
            if (initialActivity.isScope()) {
                scopeInstance = (InterpretableExecution) scopeInstance.createExecution();
                scopeInstance.setActivity(initialActivity);
                if (initialActivity.isScope()) scopeInstance.initialize();
            }
        }

        scopeInstance.setActivity(first);

        return processInstance;
    }

    @Override public boolean equals(Object o) {
        return (o instanceof ProcessDefinitionImpl) && ((ProcessDefinition) o).getId().equals(getId());
    }

    @Override public int hashCode() {
        return Predefined.hashCodeAll(getId());
    }

    public String toString() {
        return "ProcessDefinition(" + getId() + ")";
    }

    public String getDeploymentId() {
        return null;
    }

    public String getDescription() {
        return (String) getProperty("documentation");
    }

    // getters and setters //////////////////////////////////////////////////////

    public Activity getInitial() {
        return initial;
    }

    /** Sets the initial activity. */
    public void setInitial(Activity initial) {
        this.initial = initial;
    }

    /** Get the initial activity stack. */
    public List<Activity> getInitialActivityStack() {
        return getInitialActivityStack(initial);
    }

    /** Get the initial activity stack for the specified activity. */
    public synchronized List<Activity> getInitialActivityStack(Activity startActivity) {
        List<Activity> initialActivityStack = initialActivityStacks.get(startActivity);
        if (initialActivityStack == null) {
            initialActivityStack = new ArrayList<>();
            Activity activity = startActivity;
            while (activity != null) {
                initialActivityStack.add(0, activity);
                activity = activity.getParentActivity();
            }
            initialActivityStacks.put(startActivity, initialActivityStack);
        }
        return initialActivityStack;
    }

    public String getName() {
        return name;
    }

    /** Sets the name of the process. */
    public void setName(String name) {
        this.name = name;
    }

    private ExecutionImpl createProcessInstance(@Nullable String businessKey, @Nullable ActivityImpl initialActivityImpl) {
        final ExecutionImpl processInstance;

        if (initialActivityImpl == null) processInstance = (ExecutionImpl) createProcessInstance();
        else processInstance = (ExecutionImpl) createProcessInstanceForInitial(initialActivityImpl);

        processInstance.setExecutions(new ArrayList<>());
        processInstance.setProcessDefinition(processDefinition);
        // Do not initialize variable map (let it happen lazily)

        if (businessKey != null) processInstance.setBusinessKey(businessKey);

        // reset the process instance in order to have the db-generated process instance id available
        processInstance.setProcessInstance(processInstance);

        return processInstance;
    }  // end method createProcessInstance

    // public ParticipantProcess getParticipantProcess() { return participantProcess; }
    //
    // public void setParticipantProcess(ParticipantProcess participantProcess) { this.participantProcess = participantProcess; }
    //
    private InterpretableExecution newProcessInstance(Activity activityImpl) {
        final ExecutionImpl processInstance = new ExecutionImpl(activityImpl);
        processInstance.insert();
        return processInstance;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7875992131890360036L;
}  // end class ProcessDefinitionImpl
