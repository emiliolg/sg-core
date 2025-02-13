
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

import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;

/**
 * Syncs the {@link FormModel} with the server after an onClick event.
 */
public class OnClickSyncEvent extends WidgetEvent<OnClickSyncEvent.OnClickSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean feedback;

    //~ Constructors .................................................................................................................................

    public OnClickSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, boolean feedback) {
        super(model, source);
        this.feedback = feedback;
    }

    /** Creates a sync event, triggered by the specified widget. */
    public OnClickSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, boolean feedback, boolean abstractInvocation) {
        super(model, source, abstractInvocation);
        this.feedback = feedback;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnClickSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Return if click widget event requires feedback. */
    public boolean isFeedback() {
        return feedback;
    }

    @Override protected void dispatch(final OnClickSyncEventHandler handlerOnChange) {
        handlerOnChange.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnClickSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link OnClickSyncEvent}.
     */
    interface OnClickSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnClickSyncEvent eventOnChange);
    }
}  // end class OnClickSyncEvent
