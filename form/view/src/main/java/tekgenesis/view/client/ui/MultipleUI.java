
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.view.client.ui.multiple.LensRefreshEvent;

/**
 * Basic common interface for multiple ui widgets.
 */
public interface MultipleUI extends BaseWidgetUI {

    //~ Methods ......................................................................................................................................

    /** Register handler. */
    <H extends EventHandler> HandlerRegistration addHandler(H handler, GwtEvent.Type<H> type);

    /** Add a {@link LensRefreshEvent.Handler}. */
    HandlerRegistration addLensRefreshHandler(LensRefreshEvent.Handler handler);

    /** Fire event. */
    void fireEvent(@NotNull final GwtEvent<?> event);

    /** Map item slot to ui child section index. */
    Option<Integer> mapItemToSection(int item);

    /** Map ui child section index to model item slot. */
    int mapSectionToItem(final int section);

    /** Update ui with given model. */
    void updateModel(@NotNull final MultipleModel multiple);

    /** Return multiple widget. */
    @NotNull MultipleWidget getMultipleModel();

    /** Get section at given index. */
    Iterable<WidgetUI> getSection(final int section);

    /** Get the multiple visible sections count. How many sections are there in the ui. */
    int getSectionsCount();
}  // end interface MultipleUI
