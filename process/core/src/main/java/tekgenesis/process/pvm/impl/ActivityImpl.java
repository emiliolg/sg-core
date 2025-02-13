
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.process.exception.PvmBuildException;
import tekgenesis.process.pvm.Activity;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.Transition;
import tekgenesis.process.pvm.execution.ActivityBehavior;

/**
 * An Implementation of an Activity.
 */
public class ActivityImpl extends ScopeImpl implements Activity {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private ActivityBehavior    activityBehavior;
    private boolean                       asynchronous;
    private boolean                       exclusive;
    private final List<Transition>        incomingTransitions;
    private final Map<String, Transition> namedOutgoingTransitions;
    private final List<Transition>        outgoingTransitions;
    private ScopeImpl                     parent;
    private boolean                       scope;

    //~ Constructors .................................................................................................................................

    /** Create an activity Implementation. */
    public ActivityImpl(@NotNull String id, @NotNull ProcessDefinition processDefinition) {
        super(id, processDefinition);
        activityBehavior         = null;
        incomingTransitions      = new ArrayList<>();
        asynchronous             = false;
        exclusive                = false;
        scope                    = false;
        namedOutgoingTransitions = new HashMap<>();
        outgoingTransitions      = new ArrayList<>();
        parent                   = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (o instanceof ActivityImpl) {
            final ActivityImpl other = (ActivityImpl) o;
            return other.getId().equals(getId()) && other.getProcessDefinition().equals(getProcessDefinition());
        }
        else return false;
    }

    public Transition findOutgoingTransition(String transitionId) {
        return namedOutgoingTransitions.get(transitionId);
    }

    @Override public int hashCode() {
        return Predefined.hashCodeAll(getId()) + getProcessDefinition().hashCode();
    }

    public String toString() {
        return "Activity(" + getId() + ")";
    }

    @NotNull public ActivityBehavior getActivityBehavior() {
        if (activityBehavior == null) throw new IllegalStateException();
        return activityBehavior;
    }

    @NotNull @Override public <T extends ActivityBehavior> T getActivityBehavior(Class<T> clazz) {
        if (clazz.isInstance(activityBehavior)) return clazz.cast(activityBehavior);
        throw new IllegalStateException(toString() + " does not implements the behaviour " + clazz.getName());
    }

    public void setActivityBehavior(@Nullable ActivityBehavior activityBehavior) {
        this.activityBehavior = activityBehavior;
    }

    /** Sets the asynchronous flag to true. */
    public void setAsynchronous(boolean b) {
        asynchronous = b;
    }

    /** Returns true if the activity is exclusive. */
    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isScope() {
        return scope;
    }

    /** Sets the exclusive flag to true. */
    public void setExclusive(boolean b) {
        exclusive = b;
    }

    public List<Transition> getIncomingTransitions() {
        return incomingTransitions;
    }

    // getters and setters //////////////////////////////////////////////////////

    public List<Transition> getOutgoingTransitions() {
        return outgoingTransitions;
    }

    public ScopeImpl getParent() {
        return parent;
    }

    public ActivityImpl getParentActivity() {
        if (parent instanceof ActivityImpl) return (ActivityImpl) parent;
        return null;
    }

    /** Returns true if the activity is asynchronous. */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /** Sets the scope flag to true. */
    public void setScope(boolean b) {
        scope = b;
    }

    // restricted setters ///////////////////////////////////////////////////////

    void addIncoming(Transition transition) {
        incomingTransitions.add(transition);
    }

    void addOutgoing(Transition transition) {
        outgoingTransitions.add(transition);
        final String tid = transition.getId();
        if (!tid.isEmpty()) {
            if (namedOutgoingTransitions.put(tid, transition) != null)
                throw new PvmBuildException(toString() + " has duplicate transition '" + tid + "'");
        }
    }

    void setParent(ScopeImpl parent) {
        this.parent = parent;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}  // end class ActivityImpl
