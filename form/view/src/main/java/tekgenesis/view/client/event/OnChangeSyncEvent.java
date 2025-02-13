
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;

import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;

/**
 * Syncs the {@link FormModel} with the server after an onChange event.
 */
public class OnChangeSyncEvent extends FormModelEvent<OnChangeSyncEvent.OnChangeSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final List<SourceWidget> sources;

    //~ Constructors .................................................................................................................................

    /** Creates a sync event, triggered by the specified widget. */
    public OnChangeSyncEvent(final FormModel model, final List<SourceWidget> sources) {
        super(model);
        this.sources = sources;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnChangeSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns the widget id that triggered the event. */
    public List<SourceWidget> getSourceWidgets() {
        return sources;
    }

    @Override protected void dispatch(final OnChangeSyncEventHandler handler) {
        handler.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnChangeSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link OnChangeSyncEvent}.
     */
    interface OnChangeSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnChangeSyncEvent event);
    }
}  // end class OnChangeSyncEvent
