
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.process.pvm.execution.ActivityBehavior;

/**
 * An activity inside the process.
 */
public interface Activity extends Scope {

    //~ Methods ......................................................................................................................................

    /** Find an outgoing transition given its id. */
    Transition findOutgoingTransition(String transitionId);

    /** get the activity behavior. */
    @NotNull ActivityBehavior getActivityBehavior();

    /** get the activity behavior ensuring that implements a particular behavior. */
    @NotNull <T extends ActivityBehavior> T getActivityBehavior(Class<T> clazz);

    /** Plug a behavior for a given activity. */
    void setActivityBehavior(ActivityBehavior behavior);

    /** Returns true if the Activity is an Scope. */
    boolean isScope();

    /** Return the list of incoming transitions for the activity. */
    List<Transition> getIncomingTransitions();

    /** Return the list of outgoing transitions of the activity. */
    List<Transition> getOutgoingTransitions();

    /** Returns the parent {@link Scope} this activity belongs to. */
    Scope getParent();

    /** Get the parent activity for this activity. */
    Activity getParentActivity();

    /** Returns true if the activity is asynchronous. */
    boolean isAsynchronous();
    //
    // boolean isExclusive();
}
