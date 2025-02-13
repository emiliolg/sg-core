
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.impl;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.process.exception.PvmBuildException;
import tekgenesis.process.pvm.Activity;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.PvmEvent;
import tekgenesis.process.pvm.Scope;
import tekgenesis.process.pvm.execution.ExecutionListener;

/**
 * The Implementation of a Scope.
 */
public abstract class ScopeImpl extends ProcessElementImpl implements Scope {

    //~ Instance Fields ..............................................................................................................................

    private final List<ActivityImpl>                         activities;
    private final EnumMap<PvmEvent, List<ExecutionListener>> executionListeners;
    private final Map<String, ActivityImpl>                  namedActivities;

    //~ Constructors .................................................................................................................................

    ScopeImpl(@NotNull String id, @Nullable ProcessDefinition processDefinition) {
        super(id, processDefinition);
        activities         = new ArrayList<>();
        executionListeners = new EnumMap<>(PvmEvent.class);
        namedActivities    = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Add an execution listener for the specified type of event. */
    public void addExecutionListener(PvmEvent eventType, ExecutionListener executionListener) {
        addExecutionListener(eventType, executionListener, -1);
    }

    public boolean contains(Activity activity) {
        if (namedActivities.containsKey(activity.getId())) return true;
        for (final ActivityImpl nestedActivity : activities) {
            if (nestedActivity.contains(activity)) return true;
        }
        return false;
    }

    public ActivityImpl createActivity(@NotNull String id) {
        final ActivityImpl activity = new ActivityImpl(id, getProcessDefinition());
        if (!id.isEmpty()) {
            if (getProcessDefinition().findActivity(id) != null) throw new PvmBuildException("duplicate activity id '" + id + "'");
            namedActivities.put(id, activity);
        }
        activity.setParent(this);
        activities.add(activity);
        return activity;
    }

    public Activity findActivity(String activityId) {
        final ActivityImpl localActivity = namedActivities.get(activityId);
        if (localActivity != null) return localActivity;
        for (final ActivityImpl activity : activities) {
            final Activity nestedActivity = activity.findActivity(activityId);
            if (nestedActivity != null) return nestedActivity;
        }
        return null;
    }

    public Activity findNextScope(Activity destination) {
        Activity nextScope = destination;
        while (nextScope.getParent() instanceof Activity && nextScope.getParent() != this)
            nextScope = (Activity) nextScope.getParent();
        return nextScope;
    }

    // getters and setters //////////////////////////////////////////////////////

    public List<ActivityImpl> getActivities() {
        return activities;
    }

    /** Return the listeners. */
    public EnumMap<PvmEvent, List<ExecutionListener>> getExecutionListeners() {
        return executionListeners;
    }

    // event listeners //////////////////////////////////////////////////////////

    @Override public List<ExecutionListener> getExecutionListeners(PvmEvent event) {
        final List<ExecutionListener> executionListenerList = executionListeners.get(event);
        if (executionListenerList != null) return executionListenerList;
        return Collections.emptyList();
    }

    private void addExecutionListener(PvmEvent eventType, ExecutionListener executionListener, int index) {
        final List<ExecutionListener> listeners = listenersFor(eventType);
        if (index < 0) listeners.add(executionListener);
        else listeners.add(index, executionListener);
    }

    private List<ExecutionListener> listenersFor(PvmEvent eventType) {
        return executionListeners.computeIfAbsent(eventType, k -> new ArrayList<>());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}  // end class ScopeImpl
