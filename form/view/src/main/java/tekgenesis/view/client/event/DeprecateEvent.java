
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
 * Deprecates a form instance clicking a delete button.
 */
public class DeprecateEvent extends FormModelEvent<DeprecateEvent.DeprecateEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean deprecate;

    //~ Constructors .................................................................................................................................

    /** Creates save event with a valid model. */
    public DeprecateEvent(final FormModel model, boolean deprecate) {
        super(model);
        this.deprecate = deprecate;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<DeprecateEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns true if this event is for deprecation or false if this event is undeprecation. */
    public boolean isDeprecate() {
        return deprecate;
    }

    @Override protected void dispatch(final DeprecateEventHandler handler) {
        handler.onDeprecate(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<DeprecateEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link tekgenesis.view.client.event.DeprecateEvent}.
     */
    interface DeprecateEventHandler extends EventHandler {
        /** Handles sync on deprecate/undeprecate. */
        void onDeprecate(DeprecateEvent deprecateEvent);
    }
}  // end class DeprecateEvent
