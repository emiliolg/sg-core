
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
public class OnNewLocationSyncEvent extends WidgetEvent<OnNewLocationSyncEvent.OnNewLocationSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final double lat;
    private final double lng;

    //~ Constructors .................................................................................................................................

    /** Creates a sync event, triggered by the specified widget. */
    public OnNewLocationSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, final double lat, final double lng) {
        super(model, source);
        this.lat = lat;
        this.lng = lng;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnNewLocationSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override protected void dispatch(final OnNewLocationSyncEventHandler handler) {
        handler.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnNewLocationSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link OnNewLocationSyncEvent}.
     */
    interface OnNewLocationSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnNewLocationSyncEvent event);
    }
}  // end class OnNewLocationSyncEvent
