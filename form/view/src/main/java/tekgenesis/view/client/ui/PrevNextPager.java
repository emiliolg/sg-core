
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Anchor;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.multiple.Rangeable;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;

/**
 * Simple Previous ~ Next pager, with indexes text in the middle.
 */
public class PrevNextPager extends AbstractPager {

    //~ Instance Fields ..............................................................................................................................

    private final HtmlList.Item data;
    private final HtmlList.Item first;
    private final HtmlList.Item last;
    private final HtmlList.Item next;
    private final HtmlList.Item prev;

    //~ Constructors .................................................................................................................................

    public PrevNextPager(@NotNull final Rangeable rangeable) {
        super(rangeable);
        final HtmlList.Unordered ul = HtmlWidgetFactory.ul();

        first = HtmlWidgetFactory.li();
        first.add(createFirstAnchor());

        prev = HtmlWidgetFactory.li();
        prev.add(createPreviousAnchor());

        data = HtmlWidgetFactory.li();
        data.setStyleName("tableIndex");

        next = HtmlWidgetFactory.li();
        next.add(createNextAnchor());

        last = HtmlWidgetFactory.li();
        last.add(createLastAnchor());

        ul.add(first);
        ul.add(prev);
        ul.add(data);
        ul.add(next);
        ul.add(last);

        ul.addStyleName(FormConstants.PAGINATION);
        initWidget(ul);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void onRangeOrLensChanged() {
        // hide if there is no need to page
        setVisible(hasPreviousPage() || hasNextPage());

        data.setText(createText());

        toggleDisable(prev, hasPreviousPage());
        toggleDisable(next, hasNextPage());
        toggleDisable(first, hasPreviousPage());
        toggleDisable(last, hasNextPage());
    }

    private Anchor createFirstAnchor() {
        final Anchor result = anchor(MSGS.first());
        result.addClickHandler(event -> firstPage());
        return result;
    }

    private Anchor createLastAnchor() {
        final Anchor result = anchor(MSGS.last());
        result.addClickHandler(event -> lastPage());
        return result;
    }

    private Anchor createNextAnchor() {
        final Anchor result = anchor(MSGS.next());
        result.addClickHandler(event -> nextPage());
        return result;
    }

    private Anchor createPreviousAnchor() {
        final Anchor result = anchor(MSGS.previous());
        result.addClickHandler(event -> previousPage());
        return result;
    }

    private String createText() {
        final NumberFormat formatter = NumberFormat.getFormat("#.###");
        final Rangeable    display   = getRangeable();
        final ItemsRange   range     = display.getVisibleRange();
        final int          pageStart = range.getStart() + 1;
        final int          pageSize  = range.getLength();
        final int          dataSize  = display.getItemsCount();
        final int          endIndex  = max(pageStart, min(dataSize, pageStart + pageSize - 1));
        return formatter.format(pageStart) + "-" + formatter.format(endIndex) + " " + MSGS.of() + " " + formatter.format(dataSize);
    }
}  // end class PrevNextPager
