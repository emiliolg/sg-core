
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.ui.multiple.FilterEvent;
import tekgenesis.view.client.ui.multiple.SortEvent;
import tekgenesis.view.client.ui.multiple.SorterLens;
import tekgenesis.view.client.ui.tablefilters.Comparison;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;

import static tekgenesis.view.client.FormTester.fireKeyEvent;

/**
 * Wraps a TableUI to test it.
 */
public class TableTester {

    //~ Instance Fields ..............................................................................................................................

    private final TableUI table;

    //~ Constructors .................................................................................................................................

    /** Creates a TableTester. */
    public TableTester(final TableUI table) {
        this.table = table;
    }

    //~ Methods ......................................................................................................................................

    /** Adds a row to a table. Returns it index. */
    public int addRow() {
        table.addRow();
        return visibleRows() - 1;
    }

    /** Filter a table. */
    public final void filter() {
        table.react(new FilterEvent("amount", Comparison.EQUALS, "26"));
    }

    /** Performs a first page. */
    public void firstPage() {
        table.firstPage();
    }

    /** Emulates a keyDown fired from a given row. */
    public void keyDown(int fromRow) {
        selectRow(fromRow);
        RootInputHandler.getInstance().setCurrent(table);

        fireKeyEvent(KEY_ENTER);
    }

    /** Performs a next page and returns the visible range start row. */
    public void nextPage() {
        table.nextPage();
    }

    /** Return optional pager. */
    public Option<AbstractPager> pager() {
        return table.getPager();
    }

    /** Performs a previous page. */
    public void previousPage() {
        table.previousPage();
    }

    /** Removes all the rows. */
    public void removeAll() {
        while (table.getSectionsCount() > 0)
            removeRow(0);
    }

    /** Removes the last row. */
    public void removeLast() {
        removeRow(visibleRows() - 1);
    }

    /** Removes a row to a table. */
    public void removeRow(int row) {
        table.setSelectedRow(row);
        table.removeSelectedRow();
    }

    /** Performs a click on the selected row. */
    public void rowClicked(int row) {
        table.rowClicked(row);
    }

    /** Selects a row in the table. */
    public void selectRow(int row) {
        table.setSelectedRow(row);
    }

    /** Returns the sections count. */
    public int size() {
        final Option<AbstractPager> pager = table.getPager();
        return pager.isPresent() ? pager.get().getRangeable().getItemsCount() : table.getSectionsCount();
    }

    /** Sorts a table. */
    public final void sort(List<Tuple<Integer, SorterLens.SortType>> cs) {
        table.react(new SortEvent(cs));
    }

    /** Returns the rows count (visible rows). */
    public int visibleRows() {
        return table.getSectionsCount();
    }

    /** Gets value of cell represented by r and c. */
    @Nullable public Object getCellValue(int r, int c) {
        return ((HasScalarValueUI) table.getCell(r, c)).getValue();
    }

    /** Returns the actual page. */
    public int getPage() {
        return table.getPage();
    }

    /** Returns the row's class attribute. */
    public String getRowStyle(int row) {
        return table.getRowStyle(row);
    }

    /** Returns the visible columns. */
    public int getVisibleColumns() {
        return table.getVisibleColumns();
    }
}  // end class TableTester
