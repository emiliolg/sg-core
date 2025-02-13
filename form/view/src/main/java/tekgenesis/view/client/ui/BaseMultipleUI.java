
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.multiple.LensEvent;
import tekgenesis.view.client.ui.multiple.LensRefreshEvent;
import tekgenesis.view.client.ui.multiple.MultipleModelLens;
import tekgenesis.view.client.ui.multiple.MultipleModelLens.UnreachableLens;
import tekgenesis.view.client.ui.multiple.RangeChangeEvent;

/**
 * Base class for multiple ui widgets.
 */
public abstract class BaseMultipleUI extends WidgetUI implements MultipleUI {

    //~ Instance Fields ..............................................................................................................................

    private MultipleModelLens lens;
    private int               sections;

    //~ Constructors .................................................................................................................................

    BaseMultipleUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        lens     = new UnreachableLens("Base Multiple UI");
        sections = 0;
    }

    //~ Methods ......................................................................................................................................

    @Override public final HandlerRegistration addLensRefreshHandler(LensRefreshEvent.Handler handler) {
        return addHandler(handler, LensRefreshEvent.getType());
    }

    public final HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
        return addHandler(handler, RangeChangeEvent.getType());
    }

    /** Called when sections toggling finished. */
    public void doneFiltering() {}

    @Override public final Option<Integer> mapItemToSection(int item) {
        return lens.mapItemToSection(item);
    }

    @Override public final int mapSectionToItem(int section) {
        return lens.mapSectionToItem(section);
    }

    /** Make Lenses react to an event. */
    public void react(@NotNull LensEvent e) {
        lens.react(e);

        final int size = lens.size();
        if (sections != size) {
            sections = size;
            fitToExactSections();
        }

        if (e.refresh()) LensRefreshEvent.fire(this);
    }

    /** Set true if filter accepted specified row. */
    public abstract void toggleFilteredSection(int rowIndex, boolean accepted);

    /** Do nothing on update. */
    @Override public void updateModel(@NotNull MultipleModel multiple) {}

    /** Add a lens. */
    public void withLens(@NotNull MultipleModelLens l) {
        lens = l.decorate(lens);
    }

    /** Return multiple widget. */
    @NotNull public final MultipleWidget getMultipleModel() {
        return (MultipleWidget) getModel();
    }

    @Override public final int getSectionsCount() {
        return sections;
    }

    protected abstract void fitToExactSections();

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}  // end class BaseMultipleUI
