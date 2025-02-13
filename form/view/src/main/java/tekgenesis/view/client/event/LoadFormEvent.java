
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
import org.jetbrains.annotations.Nullable;

/**
 * Creates a navigation event to another form.
 */
public class LoadFormEvent extends GwtEvent<LoadFormEvent.NavigateFormEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String  fqn;
    @Nullable private final String pk;

    //~ Constructors .................................................................................................................................

    private LoadFormEvent(@NotNull final String fqn, @Nullable final String pk) {
        this.fqn = fqn;
        this.pk  = pk;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<NavigateFormEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns the destination form id. */
    @NotNull public String getFqn() {
        return fqn;
    }

    /** Returns the primary key of the entity instance. */
    @Nullable public String getPk() {
        return pk;
    }

    @Override protected void dispatch(NavigateFormEventHandler handler) {
        handler.onNavigateForm(this);
    }

    //~ Methods ......................................................................................................................................

    /** Creates a navigation event to a form with a given pk. */
    public static LoadFormEvent loadForm(@NotNull String fqn, @Nullable String pk) {
        return new LoadFormEvent(fqn, pk);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<NavigateFormEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * EventHandler for Form navigation.
     */
    interface NavigateFormEventHandler extends EventHandler {
        /** Handles navigation. */
        void onNavigateForm(LoadFormEvent event);
    }
}  // end class LoadFormEvent
