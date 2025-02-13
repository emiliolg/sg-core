
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.impl;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.process.pvm.Activity;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.Transition;
import tekgenesis.process.pvm.execution.ExecutionListener;

/**
 * An Implementation of a transition.
 */
public class TransitionImpl extends ProcessElementImpl implements Transition {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Activity                destination;
    @NotNull private final List<ExecutionListener> executionListeners;
    @NotNull private final Activity                source;

    //~ Constructors .................................................................................................................................

    /** Create a Transition Implementation. */
    public TransitionImpl(@NotNull String id, @NotNull ProcessDefinition pd, @NotNull Activity source, @NotNull Activity destination,
                          @NotNull List<ExecutionListener> listeners) {
        super(id, pd);
        executionListeners = listeners;
        this.source        = source;
        this.destination   = destination;
        // Todo this must be done in a builder.....
        ((ActivityImpl) source).addOutgoing(this);
        ((ActivityImpl) destination).addIncoming(this);
    }

    //~ Methods ......................................................................................................................................

    /** Add an execution Listener. */
    public void addExecutionListener(ExecutionListener executionListener) {
        executionListeners.add(executionListener);
    }

    public String toString() {
        return String.format("(%s)--%s-->(%s)", source.getId(), getId(), destination.getId());
    }

    @NotNull public Activity getDestination() {
        return destination;
    }

    /** Return the list of Listeners. */
    @NotNull public List<ExecutionListener> getExecutionListeners() {
        return executionListeners;
    }

    @NotNull public Activity getSource() {
        return source;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7187708715129808298L;
}  // end class TransitionImpl
