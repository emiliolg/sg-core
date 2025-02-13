
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
 * Cancels a form an clicking a cancel button.
 */
public class CancelEvent extends TerminationEvent<CancelEvent.CancelEventHandler> {

    //~ Constructors .................................................................................................................................

    /** Creates cancel event with a valid model. */
    public CancelEvent(@NotNull final FormModel model, @NotNull final SourceWidget source) {
        super(model, source);
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<CancelEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override protected void dispatch(final CancelEventHandler handler) {
        handler.onCancel(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<CancelEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link CancelEvent}.
     */
    interface CancelEventHandler extends EventHandler {
        /** Handles sync. */
        void onCancel(CancelEvent deleteEvent);
    }
}
