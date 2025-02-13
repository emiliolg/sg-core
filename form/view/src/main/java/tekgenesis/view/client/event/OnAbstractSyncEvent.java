
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
 * Syncs the {@link FormModel} with the server after an abstract event.
 */
public class OnAbstractSyncEvent extends WidgetEvent<OnAbstractSyncEvent.OnAbstractSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final SourceWidget delegate;

    //~ Constructors .................................................................................................................................

    /** Creates a sync event, triggered by the specified widget. */
    public OnAbstractSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget delegate, @NotNull final SourceWidget source) {
        super(model, source);
        this.delegate = delegate;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnAbstractSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns the event delegated widget id. */
    @NotNull public SourceWidget getDelegateWidget() {
        return delegate;
    }

    @Override protected void dispatch(final OnAbstractSyncEventHandler handler) {
        handler.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnAbstractSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link OnAbstractSyncEvent}.
     */
    interface OnAbstractSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnAbstractSyncEvent event);
    }
}  // end class OnAbstractSyncEvent
