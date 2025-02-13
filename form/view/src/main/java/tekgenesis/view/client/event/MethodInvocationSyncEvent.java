
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.event;

import com.google.gwt.event.shared.EventHandler;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormModel;

/**
 * Syncs the {@link FormModel} with the server after a server side method invocation event.
 */
public class MethodInvocationSyncEvent extends FormModelEvent<MethodInvocationSyncEvent.MethodInvocationSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final String method;

    //~ Constructors .................................................................................................................................

    /** Creates a method invocation sync event. */
    public MethodInvocationSyncEvent(@NotNull final FormModel model, final String method) {
        super(model);
        this.method = method;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<MethodInvocationSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Get server side method to invoke. */
    public String getMethod() {
        return method;
    }

    @Override protected void dispatch(final MethodInvocationSyncEventHandler h) {
        h.onMethodInvocation(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<MethodInvocationSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link MethodInvocationSyncEvent}.
     */
    interface MethodInvocationSyncEventHandler extends EventHandler {
        /** Handles method invocation sync. */
        void onMethodInvocation(MethodInvocationSyncEvent eventOnSchedule);
    }
}  // end class MethodInvocationSyncEvent
