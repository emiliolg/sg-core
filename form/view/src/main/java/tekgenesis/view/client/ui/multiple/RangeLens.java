
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

import com.google.gwt.event.shared.HandlerRegistration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.view.client.ui.ItemsRange;
import tekgenesis.view.client.ui.MultipleUI;

import static java.lang.Math.min;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.view.client.ui.multiple.ItemEvent.ItemEventType.ITEM_DELETED;
import static tekgenesis.view.client.ui.multiple.MultipleModelLens.CompoundLens;

/**
 * Visible range lens.
 */
public class RangeLens extends CompoundLens implements Rangeable {

    //~ Instance Fields ..............................................................................................................................

    private final MultipleUI display;
    private int              offset;
    private int              sections;

    //~ Constructors .................................................................................................................................

    /** Pager Lens constructor. */
    public RangeLens(MultipleUI display) {
        this.display = display;
        sections     = display.getMultipleModel().getVisibleRows();
        offset       = 0;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public HandlerRegistration addLensRefreshHandler(LensRefreshEvent.Handler handler) {
        return display.addHandler(handler, LensRefreshEvent.getType());
    }

    @NotNull @Override public final HandlerRegistration addRangeChangeHandler(@NotNull final RangeChangeEvent.Handler handler) {
        return display.addHandler(handler, RangeChangeEvent.getType());
    }

    @Override public int size() {
        return min(origin.size(), sections);
    }

    @Override public int getItemsCount() {
        return origin.size();
    }

    @NotNull @Override public ItemsRange getVisibleRange() {
        return new ItemsRange(offset, size());
    }

    @Override public final void setVisibleRange(@NotNull ItemsRange r) {
        if (offset != r.getStart() || sections != r.getLength()) {
            offset   = r.getStart();
            sections = r.getLength();

            if (r.isFire())
            // Update the pager and notify event handler if the visible range changed
            RangeChangeEvent.fire(display, getVisibleRange());
        }
    }

    @Override protected void doReact(LensEvent event) {
        if (event instanceof ItemEvent) {
            final ItemEvent itemEvent = (ItemEvent) event;
            if (itemEvent.getType() == ITEM_DELETED) {
                final ItemsRange range     = getVisibleRange();
                final int        pageSize  = range.getLength();
                final int        pageStart = Math.max(0, Math.min(getVisibleRange().getStart(), getItemsCount() - pageSize));

                if (pageStart != range.getStart()) setVisibleRange(new ItemsRange(pageStart, pageSize));
            }
        }
    }

    @Override protected Option<Integer> itemToSection(int item) {
        final ItemsRange range = getVisibleRange();
        return range.contains(item) ? some(item - range.getStart()) : empty();
    }

    @Override protected int sectionToItem(int section) {
        return offset + section;
    }
}  // end class RangeLens
