
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

public abstract class WidgetEvent<H extends EventHandler> extends FormModelEvent<H> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean abstractInvocation;

    @NotNull private final SourceWidget source;

    //~ Constructors .................................................................................................................................

    WidgetEvent(@NotNull final FormModel model, @NotNull final SourceWidget source) {
        this(model, source, false);
    }

    WidgetEvent(@NotNull final FormModel model, @NotNull final SourceWidget source, boolean abstractInvocation) {
        super(model);
        this.source             = source;
        this.abstractInvocation = abstractInvocation;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if source widget is marked as abstract invocation. */
    public boolean isAbstractInvocation() {
        return abstractInvocation;
    }

    /** Returns the widget id that triggered the event. */
    @NotNull public SourceWidget getSourceWidget() {
        return source;
    }
}
