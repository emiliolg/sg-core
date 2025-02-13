
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
 * Syncs the {@link FormModel} with the server after an onBlur event.
 */
public class OnBlurSyncEvent extends WidgetEvent<OnBlurSyncEvent.OnBlurSyncEventHandler> {

    //~ Constructors .................................................................................................................................

    /** Creates a sync event, triggered by the specified widget. */
    public OnBlurSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, boolean abstractInvocation) {
        super(model, source, abstractInvocation);
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnBlurSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override protected void dispatch(final OnBlurSyncEventHandler handler) {
        handler.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnBlurSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link tekgenesis.view.client.event.OnBlurSyncEvent}.
     */
    interface OnBlurSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnBlurSyncEvent event);
    }
}
