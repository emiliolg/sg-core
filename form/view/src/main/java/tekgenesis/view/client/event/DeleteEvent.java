
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
 * Deletes a form after clicking an Delete button.
 */
public class DeleteEvent extends TerminationEvent<DeleteEvent.DeleteEventHandler> {

    //~ Constructors .................................................................................................................................

    /** Creates delete event with a valid model. */
    public DeleteEvent(@NotNull final FormModel model, @NotNull final SourceWidget source) {
        super(model, source);
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<DeleteEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override protected void dispatch(final DeleteEventHandler handler) {
        handler.onDelete(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<DeleteEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link DeleteEvent}.
     */
    interface DeleteEventHandler extends EventHandler {
        /** Handles sync. */
        void onDelete(DeleteEvent deleteEvent);
    }
}
