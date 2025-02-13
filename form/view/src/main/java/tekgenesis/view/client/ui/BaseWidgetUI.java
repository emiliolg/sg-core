
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.annotation.Pure;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.WidgetUI.Context;

/**
 * Common interface for all ui widgets.
 */
public interface BaseWidgetUI {

    //~ Methods ......................................................................................................................................

    /** Returns {@link ModelUI container}. */
    ModelUI container();

    /** Return widget ui as {@link IndexedWidget indexed}. */
    @NotNull @Pure default IndexedWidget toIndexed() {
        return container().indexed(this);
    }

    /** Return widget ui as {@link SourceWidget source widget} with fqn as path. */
    @NotNull @Pure default SourceWidget toSourceWidget() {
        return toIndexed().toSourceWidget();
    }

    /** Returns the context: where the widget is located. */
    Context getContext();

    /** Returns the widget associated meta model. */
    Widget getModel();
}
