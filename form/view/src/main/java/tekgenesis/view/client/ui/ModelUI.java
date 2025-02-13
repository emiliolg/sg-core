
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

import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.Parentable;
import tekgenesis.metadata.form.widget.UiModel;

/**
 * Represent UI for {@link UiModel ui models} such as forms and widget definitions.
 */
public interface ModelUI extends Parentable<ModelUI>, HasWidgetsUI, Iterable<WidgetUI> {

    //~ Methods ......................................................................................................................................

    /** Return associated {@link WidgetUIFinder finder}. */
    @NotNull WidgetUIFinder finder();

    /** Return {@link IndexedWidget indexed} based on {@link BaseWidgetUI widget ui}. */
    @NotNull default IndexedWidget indexed(@NotNull final BaseWidgetUI widget) {
        return indexed(widget.getModel(), widget.getContext().getItem());
    }

    /** Return container id. */
    String getId();

    /** Return associated {@link UiModel metamodel}. */
    @NotNull UiModel getUiModel();
}
