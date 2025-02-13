
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
import com.google.gwt.event.shared.GwtEvent;

import org.jetbrains.annotations.NotNull;

import tekgenesis.view.client.ui.AnchoredSubformUI;

/**
 * Creates a subform event. May be a show or a hide.
 */
public class SubformEvent extends GwtEvent<SubformEvent.LoadSubformEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final AnchoredSubformUI subForm;

    //~ Constructors .................................................................................................................................

    private SubformEvent(@NotNull final AnchoredSubformUI subForm) {
        this.subForm = subForm;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<LoadSubformEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns the sub form ui. */
    @NotNull public AnchoredSubformUI getSubform() {
        return subForm;
    }

    /** Returns the destination form id. */
    @NotNull public String getSubformFqn() {
        return subForm.getModel().getSubformFqn();
    }

    @Override protected void dispatch(LoadSubformEventHandler handler) {
        if (subForm.isHidden()) handler.showSubform(this);
        else handler.hideSubform(this);
    }

    //~ Methods ......................................................................................................................................

    /** Creates a navigation event to a form. */
    public static SubformEvent subformClicked(final AnchoredSubformUI subForm) {
        return new SubformEvent(subForm);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<LoadSubformEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * EventHandler for Form navigation.
     */
    interface LoadSubformEventHandler extends EventHandler {
        /** Hide subform. */
        void hideSubform(SubformEvent event);
        /** Show subform. Handles navigation. */
        void showSubform(SubformEvent event);
    }
}  // end class SubformEvent
