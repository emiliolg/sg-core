
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.user.client.ui.Composite;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.multiple.Rangeable;

/**
 * An abstract pager that exposes many methods useful for paging.
 */
public abstract class AbstractPager extends Composite {

    //~ Instance Fields ..............................................................................................................................

    int                              items     = -1;
    @NotNull private final Rangeable rangeable;

    //~ Constructors .................................................................................................................................

    AbstractPager(@NotNull final Rangeable rangeable) {
        this.rangeable = rangeable;
        rangeable.addRangeChangeHandler(event -> onRangeOrLensChanged());
        rangeable.addLensRefreshHandler(event -> onRangeOrLensChanged());
    }

    //~ Methods ......................................................................................................................................

    /** Go to the first page. */
    void firstPage() {
        setPage(0);
    }

    /**
     * Returns true if there is enough data such that a call to {@link #nextPage()} will succeed in
     * moving the starting point of the table forward.
     */
    boolean hasNextPage() {
        final ItemsRange range = rangeable.getVisibleRange();
        return range.getStart() + range.getLength() < rangeable.getItemsCount();
    }

    /**
     * Returns true if there is enough data such that a call to {@link #previousPage()} will succeed
     * in moving the starting point of the table backward.
     */
    boolean hasPreviousPage() {
        return getPageStart() > 0 && rangeable.getItemsCount() > 0;
    }

    /** Set the page start to the last index that will still show a full page. */
    void lastPage() {
        setPageStart(rangeable.getItemsCount() - getPageSize());
    }

    /** Advance the starting row by 'pageSize' rows. */
    void nextPage() {
        if (hasNextPage()) {
            final ItemsRange range = rangeable.getVisibleRange();
            setPageStart(range.getStart() + range.getLength());
        }
    }

    /** Called when the range or row count changes. Implementations should update the pager */
    abstract void onRangeOrLensChanged();

    /** Move the starting row back by 'pageSize' rows. */
    void previousPage() {
        if (hasPreviousPage()) {
            final ItemsRange range = rangeable.getVisibleRange();
            setPageStart(range.getStart() - range.getLength());
        }
    }

    void toggleDisable(HtmlList.Item li, boolean b) {
        if (!b) li.addStyleName(FormConstants.DISABLED_STYLE);
        else li.removeStyleName(FormConstants.DISABLED_STYLE);
    }

    /** Is first page? */
    boolean isFirstPage() {
        return getPage() == 0;
    }

    /** Is last page?. Pages are indexed starting from 0, while count it's absolute. */
    boolean isLastPage() {
        return getPage() + 1 == getPageCount();
    }

    /**
     * Get the current page index. The return value is the number of times {@link #previousPage()}
     * can be called
     */
    int getPage() {
        final ItemsRange range    = rangeable.getVisibleRange();
        final int        pageSize = range.getLength();
        return range.isEmpty() ? 0 : (range.getStart() + pageSize - 1) / pageSize;
    }

    /** Go to a specific page. */
    void setPage(int index) {
        if (hasPage(index)) {
            // We don't use setPageStart because it would constrain the index :)
            final int pageSize = getPageSize();
            final int start    = pageSize * index;
            if (start + pageSize < rangeable.getItemsCount()) rangeable.setVisibleRange(new ItemsRange(start, pageSize));
            else lastPage();
        }
    }

    /** Get the {@link Rangeable} being paged. */
    @NotNull Rangeable getRangeable() {
        return rangeable;
    }

    /** Returns true if there is enough data such that the specified page is within range. */
    private boolean hasPage(int index) {
        return getPageSize() * index < rangeable.getItemsCount();
    }

    /** Get the number of pages based on the data size. */
    private int getPageCount() {
        final int pageSize = getPageSize();
        return (rangeable.getItemsCount() + pageSize - 1) / pageSize;
    }

    /** Get the page size. */
    private int getPageSize() {
        return rangeable.getVisibleRange().getLength();
    }

    /** Get the page start index. */
    private int getPageStart() {
        return rangeable.getVisibleRange().getStart();
    }

    /** Set the page start index. */
    private void setPageStart(final int index) {
        final ItemsRange range     = rangeable.getVisibleRange();
        final int        pageSize  = range.getLength();
        final int        pageStart = Math.max(0, Math.min(index, rangeable.getItemsCount() - pageSize));

        if (pageStart != range.getStart()) rangeable.setVisibleRange(new ItemsRange(pageStart, pageSize));
    }
}  // end class AbstractPager
