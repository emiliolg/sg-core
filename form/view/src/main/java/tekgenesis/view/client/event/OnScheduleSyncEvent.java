
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

import tekgenesis.metadata.form.model.FormModel;

/**
 * Syncs the {@link FormModel} with the server after an onSchedule event.
 */
public class OnScheduleSyncEvent extends FormModelEvent<OnScheduleSyncEvent.OnScheduleSyncEventHandler> {

    //~ Constructors .................................................................................................................................

    /** Creates an schedule sync event. */
    public OnScheduleSyncEvent(final FormModel model) {
        super(model);
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnScheduleSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override protected void dispatch(final OnScheduleSyncEventHandler handlerOnChange) {
        handlerOnChange.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnScheduleSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link tekgenesis.view.client.event.OnScheduleSyncEvent}.
     */
    interface OnScheduleSyncEventHandler extends EventHandler {
        /** Handles schedule sync. */
        void onSyncTriggered(OnScheduleSyncEvent eventOnSchedule);
    }
}
