
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

abstract class TerminationEvent<H extends EventHandler> extends WidgetEvent<H> {

    //~ Constructors .................................................................................................................................

    TerminationEvent(@NotNull final FormModel model, @NotNull final SourceWidget source) {
        super(model, source);
    }
}
