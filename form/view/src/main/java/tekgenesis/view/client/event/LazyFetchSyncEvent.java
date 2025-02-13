
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
 * Syncs the {@link FormModel} with the server after a table lazy request was performed.
 */
public class LazyFetchSyncEvent extends WidgetEvent<LazyFetchSyncEvent.LazyFetchSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final int limit;

    private final int offset;

    //~ Constructors .................................................................................................................................

    public LazyFetchSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, int offset, int limit) {
        super(model, source);
        this.offset = offset;
        this.limit  = limit;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<LazyFetchSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    @Override protected void dispatch(final LazyFetchSyncEventHandler handler) {
        handler.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<LazyFetchSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link LazyFetchSyncEvent}.
     */
    interface LazyFetchSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(LazyFetchSyncEvent event);
    }
}  // end class LazyFetchSyncEvent
