
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;

/**
 * Syncs the {@link FormModel} with the server after an OnLoadFormSyncEvent event.
 */
public class OnLoadFormSyncEvent extends WidgetEvent<OnLoadFormSyncEvent.OnLoadFormSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final String pk;

    //~ Constructors .................................................................................................................................

    /** Creates a sync event, triggered by the specified widget. */
    public OnLoadFormSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, @Nullable final String pk) {
        super(model, source);
        this.pk = pk;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnLoadFormSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns the primary key of the entity instance. */
    @Nullable public String getPk() {
        return pk;
    }

    @Override protected void dispatch(final OnLoadFormSyncEventHandler handlerOnChange) {
        handlerOnChange.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnLoadFormSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link tekgenesis.view.client.event.OnLoadFormSyncEvent}.
     */
    interface OnLoadFormSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnLoadFormSyncEvent eventOnChange);
    }
}  // end class OnLoadFormSyncEvent
