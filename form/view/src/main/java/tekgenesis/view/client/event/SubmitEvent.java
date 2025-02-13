
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
 * Submit the form after clicking a submit button.
 */
public class SubmitEvent extends TerminationEvent<SubmitEvent.SubmitEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final String action;

    //~ Constructors .................................................................................................................................

    /** Creates submit event with a valid model and an action. */
    public SubmitEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, final String action) {
        super(model, source);
        this.action = action;
    }

    //~ Methods ......................................................................................................................................

    /** Return submit actions. */
    public String getAction() {
        return action;
    }

    @Override public Type<SubmitEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override protected void dispatch(final SubmitEventHandler handler) {
        handler.onSubmit(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<SubmitEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link tekgenesis.view.client.event.SubmitEvent}.
     */
    interface SubmitEventHandler extends EventHandler {
        /** Handles sync. */
        void onSubmit(SubmitEvent submitEvent);
    }
}  // end class SubmitEvent
