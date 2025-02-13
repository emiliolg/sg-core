
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
 * Syncs the {@link FormModel} with the server after an SuggestNewSync event.
 */
public class OnSuggestNewSyncEvent extends WidgetEvent<OnSuggestNewSyncEvent.OnSuggestNewSyncEventHandler> {

    //~ Instance Fields ..............................................................................................................................

    private final String text;

    //~ Constructors .................................................................................................................................

    /** Creates a sync event, triggered by the specified widget. */
    public OnSuggestNewSyncEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, final String text) {
        super(model, source);
        this.text = text;
    }

    //~ Methods ......................................................................................................................................

    @Override public Type<OnSuggestNewSyncEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** Returns the text entered by the user. */
    public String getText() {
        return text;
    }

    @Override protected void dispatch(final OnSuggestNewSyncEventHandler handlerOnChange) {
        handlerOnChange.onSyncTriggered(this);
    }

    //~ Static Fields ................................................................................................................................

    static final Type<OnSuggestNewSyncEventHandler> TYPE = new Type<>();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler for a {@link OnChangeSyncEvent}.
     */
    interface OnSuggestNewSyncEventHandler extends EventHandler {
        /** Handles sync. */
        void onSyncTriggered(OnSuggestNewSyncEvent eventOnChange);
    }
}  // end class OnSuggestNewSyncEvent
