
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
 * Calls the user method when row changes.
 */
public class OnRowSelectedEvent extends WidgetEvent<OnRowSelectedEvent.OnRowSelectedEventHandler> {

    //~ Constructors .................................................................................................................................

    /** Creates a row change event triggered by the specified widget (table). */
    public OnRowSelectedEvent(@NotNull final FormModel model, @NotNull final SourceWidget source) {
        super(model, source);
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnRowSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override protected void dispatch(OnRowSelectedEventHandler handler) {
        handler.onRowSelected(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnRowSelectedEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link tekgenesis.view.client.event.OnRowSelectedEvent}.
     */
    interface OnRowSelectedEventHandler extends EventHandler {
        /** Handles row change event. */
        void onRowSelected(OnRowSelectedEvent event);
    }
}
