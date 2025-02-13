
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LegendElement;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.InlineHTML;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.aggregate.Aggregate;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.controller.ViewCreator;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.multiple.FilterEvent;
import tekgenesis.view.client.ui.multiple.SortEvent;
import tekgenesis.view.client.ui.multiple.SorterLens.SortType;
import tekgenesis.view.client.ui.tablefilters.Comparison;
import tekgenesis.view.client.ui.tablefilters.FilterUI;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_UP;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.STYLE_ATTR;
import static tekgenesis.metadata.form.model.FormConstants.TABLE_ACTIVE_ROW_STYLE;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;
import static tekgenesis.view.client.ui.tablefilters.FilterUI.FilterWidget;

/**
 * A static Table UI widget.
 */
@SuppressWarnings({ "OverlyComplexClass", "ClassWithTooManyMethods" })
public class TableUI extends BaseMultipleUI implements HasWidgetsUI, HasPlaceholderUI, MultipleUI {

    //~ Instance Fields ..............................................................................................................................

    TableRowHandler                               handler;
    private final Map<Aggregate, AggregateFooter> aggregators;

    private final List<Widget>                      columns;
    private final InlineHTML                        emptyMessage;
    private final Map<Widget, Option<FilterWidget>> filters;
    private LazyPopulator                           lazy = null;

    private LazyFetchPopulator lazyFetch   = null;
    private PrevNextPager      pager       = null;
    private int                selectedCol = 0;
    private int                selectedRow = -1;

    private final FlexTableUI   table;
    private final LegendElement tableLabel;

    //~ Constructors .................................................................................................................................

    /** Creates a Table UI widget. */
    public TableUI(@NotNull final ModelUI container, @NotNull final MultipleWidget model) {
        super(container, model);

        handler     = null;
        columns     = new ArrayList<>();
        aggregators = new HashMap<>();
        filters     = new HashMap<>();

        table = new FlexTableUI();
        if (model.hasOnClickMethod()) table.setTableAsClickable();

        final FlowPanel wrapper = new FlowPanel();

        wrapper.add(table);

        table.addCustomDragHandler((fromRow, toRow) -> {
            clearSelectedRow();
            moveRow(fromRow, toRow);
        });

        table.addClickHandler(event -> {
            final Element target = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
            // prevent row clicked event when the click should be handled by a child component
            // ex: row click for a checkbox inside a table
            if (!Constants.INPUT.equals(target.getTagName()) && !"BUTTON".equals(target.getTagName()) && !"A".equals(target.getTagName())) {
                final HTMLTable.Cell cell = table.getCellForEvent(event);
                if (cell != null) rowClicked(cell.getRowIndex());
            }
        });

        table.addSortHandler(new SortHandler() {
                @Override void notifySort(@NotNull List<Tuple<Integer, SortType>> criteria) {
                    react(new SortEvent(criteria));
                }
            });

        table.addFilterHandler(this::filter);

        // inits and adds table to DOM.
        initWidget(wrapper);

        // Initially the footer is hidden, if there is one row it will appear.
        table.setFooterVisibility(false);

        emptyMessage = span(MSGS.emptyTable());
        emptyMessage.setStyleName(TABLE_EMPTY_MESSAGE);
        div.add(emptyMessage);

        if (model.isDraggable() && model.getDisableExpression() != Expression.TRUE) table.addStyleName("draggable dragging");
        if (model.isScrollable()) {
            wrapper.addStyleName("scroll-table");
            final Icon icon = new Icon(IconType.CHEVRON_RIGHT);
            icon.addStyleName("scroll-icon animooted");
            wrapper.add(icon);
            wrapper.addDomHandler(event -> icon.removeStyleName("animooted"), ScrollEvent.getType());
            wrapper.sinkEvents(Event.ONSCROLL);
        }

        tableLabel = Document.get().createLegendElement();
        getElement().insertFirst(tableLabel);
    }  // end ctor TableUI

    //~ Methods ......................................................................................................................................

    /** Add column header. */
    @Override public void addChild(WidgetUI column) {
        final int col = table.getColumnCount();
        column.withinMultiple(this, 0, col);

        Option<FilterWidget> filter = empty();
        final Widget         model  = column.getModel();
        if (getModel().isFilterable()) {
            filter = new FilterUI(model).getPanel();
            addFilter(filter, model);
        }

        table.setHeader(column, getModel().isSortable(), filter);
        columns.add(model);
        addAggregatesForColumn(model, col);
    }

    /** Adds a new row. */
    public void addRow() {
        final int item = handler.onItemCreated();

        if (hasPager()) pager.lastPage();
        if (getModel().isFilterable()) table.resetFilters();

        mapItemToSection(item).ifPresent(row -> {
            selectedCol = 0;
            setSelectedRow(row);
        });
    }

    /** Clear the selection of the row table. */
    public void clearSelectedRow() {
        if (selectedRow > -1) {
            // deselect old
            deselectActiveRow(true);
            selectedCol = 0;
        }
    }

    /** Disables all inner widgets, or enables the ones that has to. */
    public void disableRow(int row, boolean disable) {
        immutable(table.getRow(row)).filter(w -> w.getModel().getDisableExpression() == Expression.FALSE).forEach(w -> w.setDisabled(disable));
        if (getModel().isDraggable()) table.getWidget(row, 0).getElement().setAttribute("drag", String.valueOf(!disable));
    }

    /**
     * Called when CTRL+KEY_DOWN. Extracted because its also called from RootInputHandler on ENTER.
     *
     * <p>The intended behavior is: - if no pager then go to next row, and if we reach the bottom,
     * go to first row. - if pager then go to next row, but if we reach the end of the page, go to
     * the first row on the next page. - also if we reach the end of the table go to the first row
     * on the first page.</p>
     */
    public void down(KeyDownEvent event) {
        final int nextRow = selectedRow + 1;
        if (nextRow >= getSectionsCount()) {
            if (hasPager() && !pager.isLastPage()) nextPage();
            else if (hasPager() && pager.isLastPage()) firstPage();
            setSelectedRow(0);
        }
        else setSelectedRow(nextRow);
        event.preventDefault();
    }

    /** Handle key events on this table. */
    public boolean handleRootKeyDown(final KeyDownEvent event) {
        if (selectedRow > -1) {
            switch (event.getNativeKeyCode()) {
            case KEY_LEFT:
                if (hasPager()) previousPage();
                return true;
            case KEY_RIGHT:
                if (hasPager()) nextPage();
                return true;
            case KEY_DOWN:
                down(event);
                return true;
            case KEY_UP:
                up(event);
                return true;
            }
        }
        return false;
    }

    /** Handles clicks on this table or focus changes. */
    public void handleSelectionEvent(final Event event) {
        final IntIntTuple rowForEvent = table.getRowForEvent(event);
        if (rowForEvent != null) {
            selectedCol = rowForEvent.second();
            final Integer first = rowForEvent.first();
            if (selectedRow != first) setSelectedRow(first);
        }
        else deselectActiveRow(true);
    }

    /** Removes the selected row and maintains the selections. */
    public void removeSelectedRow() {
        if (selectedRow > -1) {
            final int oldSectionsCount = getSectionsCount();
            removeRow(selectedRow);

            // re-select item at its position or clear selection
            // (check for a size change because the removeRow may only fire an event to the server)
            if (getSectionsCount() > 0 && getSectionsCount() != oldSectionsCount) {
                final int oldSelected = selectedRow;
                selectedRow = -1;
                setSelectedRow(oldSelected);
            }
            else clearSelectedRow();
        }
    }

    /** Set true if filter accepted specified row. */
    public void toggleFilteredSection(int row, boolean show) {
        final Element element = table.getRowElement(row);
        if (element != null) {
            if (show) element.removeClassName("hide");
            else element.addClassName("hide");
        }
    }

    /** Update aggregate value. */
    public void updateAggregate(@NotNull final Aggregate aggregate, @Nullable final String value) {
        final AggregateFooter footer = aggregators.get(aggregate);
        if (footer != null) footer.update(value);
    }

    /** Sets the visibility of a column. */
    public void visibleAndStyle(final Widget column, boolean visible, Option<String> columnStyle) {
        final int col = columns.indexOf(column);
        if (col != -1) {
            for (int section = 0; section < table.getRowCount(); section++) {
                table.setCellVisible(section, col, visible);
                for (final String style : columnStyle)
                    table.setCellStyle(section, col, style);
            }

            table.setHeaderFooterVisible(col, visible);
            for (final String style : columnStyle)
                table.setHeaderStyle(col, style);
        }
    }

    public void withLazy(final LazyPopulator p) {
        createBottomPanel(lazy = p);
    }

    public void withLazyFetch(final LazyFetchPopulator p) {
        createBottomPanel(lazyFetch = p);
    }

    public void withPager(final PrevNextPager p) {
        createBottomPanel(pager = p);
    }

    /** Sets the label of a column. */
    public void setColumnLabel(final Widget column, final String text) {
        final int col = columns.indexOf(column);
        if (col != -1) table.setLabel(text, col);
    }

    /** Sets the width of a column. */
    public void setColumnWidth(final Widget column, final int width) {
        final int col = columns.indexOf(column);
        if (col != -1) table.setWidth(width, col);
    }

    /** Returns filter widget as Option if any. */
    public Option<FilterWidget> getFilterForCol(Widget col) {
        return filters.get(col);
    }

    /** Returns the actual page. */
    public int getPage() {
        if (pager != null) return pager.getPage();
        else return 0;
    }

    /** Sets the actual page. */
    public void setPage(int page) {
        if (pager != null && pager.getPage() != page) pager.setPage(page);
    }

    @NotNull public Option<AbstractPager> getPager() {
        return ofNullable(pager);
    }

    @Override public void setPlaceholder(final String placeholder) {
        emptyMessage.setText(placeholder);
    }

    /** Sets a styleClass to a row. */
    public void setRowElementStyle(int row, String styleClass) {
        table.getRowElement(row).setClassName(styleClass);
        if (selectedRow == row) table.getRowElement(selectedRow).addClassName(TABLE_ACTIVE_ROW_STYLE);
    }

    /** Add handler for phantom row creation and discard. */
    public void setRowHandler(final TableRowHandler h) {
        handler = h;
    }

    /** Sets an inlineStyle to a row. */
    public void setRowInlineStyle(int row, String inlineStyle) {
        table.getRowElement(row).setAttribute(STYLE_ATTR, notNull(inlineStyle));
    }

    /** Returns all the cells of a certain row. */
    @Override public Iterable<WidgetUI> getSection(int row) {
        return table.getRow(row);
    }

    /** Returns current selected row. */
    public int getSelectedRow() {
        return selectedRow;
    }

    /** Set the selected row. */
    public void setSelectedRow(final int row) {
        if (selectedRow != row) {
            // deselect old
            deselectActiveRow(false);

            final int rowCount = getSectionsCount();

            if (rowCount > 0) {
                // select new
                selectedRow = max(0, min(row, rowCount - 1));

                selectActiveRow();
            }
            rowSelected();
        }
        // update focus
        focusActiveRow();
    }

    /** Sets a 'sorting by' type to the specified column. */
    public void setSortingBy(int column, SortType sortType) {
        table.setSortingBy(column, sortType);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(TABLE_WIDGET_CLASSNAME);
        // noinspection DuplicateStringLiteralInspection
        table.addStyleName("table");
    }

    /** Fit table to avoid empty rows and create new ones. */
    @Override protected void fitToExactSections() {
        // Ensure exact sections
        final int expected = getSectionsCount();
        final int actual   = table.getRowCount();

        if (expected > actual)
        // Insert blank sections
        createRows(actual, expected - actual);
        else if (expected < actual) {
            // Remove extra sections
            final int decrease = actual - expected;
            removeRows(actual - decrease, decrease);
        }

        // Show/hide empty table msg
        final boolean emptyTable = getSectionsCount() == 0;
        emptyMessage.setVisible(emptyTable);
        table.setFooterVisibility(!emptyTable);
    }  // end method fitToExactSections

    @Override protected void onDetach() {
        super.onDetach();

        if (lazyFetch != null) lazyFetch.unregister();

        if (lazy != null) lazy.unregister();
    }

    @Override void clear() {
        super.clear();

        for (int row = 0; row < getSectionsCount(); row++) {
            for (int col = 0; col < table.getUserCellCount(row); col++)
                table.getWidgetUI(row, col).clear();
        }
    }

    @NotNull @Override Option<Element> createLabel() {
        return some(tableLabel);
    }

    /** Performs a first page. */
    void firstPage() {
        pager.firstPage();
    }

    /** Performs a next page. */
    void nextPage() {
        if (pager != null) pager.nextPage();
    }

    /** Performs a previous page. */
    void previousPage() {
        if (pager != null) pager.previousPage();
    }

    /** Row is clicked. */
    void rowClicked(int row) {
        handler.onRowClicked(row);
    }

    WidgetUI getCell(int row, int col) {
        return table.getWidgetUI(row, col);
    }

    /** Returns all the table cells. */
    Iterable<WidgetUI> getCells() {
        return table.getCells();
    }

    /** Returns the class attribute of the row. */
    String getRowStyle(int row) {
        return table.getRowElement(row).getClassName();
    }

    /** Get visible columns. */
    int getVisibleColumns() {
        return table.getVisibleColumns();
    }

    private void addAggregatesForColumn(@NotNull final Widget column, final int col) {
        for (final Aggregate aggregate : getModel().getAggregates()) {
            if (equal(aggregate.getRef(), column.getName())) {
                final InlineHTML span = span();
                table.setFooter(aggregate.getFn(), col + 1, span);
                final AggregateFooter footer = new AggregateFooter(span);
                aggregators.put(aggregate, footer);
            }
        }
        table.prepareFooter(col + 1);
    }

    private void addFilter(Option<FilterWidget> filter, Widget col) {
        filters.put(col, filter);
    }

    private void createBottomPanel(final com.google.gwt.user.client.ui.Widget inner) {
        final FlowPanel bottom = new FlowPanel();
        bottom.addStyleName("tableBottomPanel");
        bottom.add(inner);
        div.add(bottom);
    }

    private void createRows(int rowFrom, int amount) {
        for (int i = 0; i < amount; i++)
            createTableRow(rowFrom + i);
    }

    private void createTableRow(int row) {
        ViewCreator.createView(container(), getModel(), new FlexTableRowWrapper(row));
        if (handler != null) handler.onRowCreated(row);
    }

    private void deselectActiveRow(boolean notifyRowUnselected) {
        if (selectedRow > -1) {
            final Element rowElement = table.getRowElement(selectedRow);
            if (rowElement != null && getSectionsCount() != 0 && isNotEmpty(rowElement.getClassName())) {
                rowElement.removeClassName(TABLE_ACTIVE_ROW_STYLE);
                leaveRowFocus();
            }
            selectedRow = -1;

            if (notifyRowUnselected) rowSelected();
        }
    }

    /** Actual filter the rows in the table's model. */
    private void filter(String column, Comparison c, String value) {
        // Notify UI.
        table.filterBy(column, value);
        react(new FilterEvent(column, c, value));

        if (getPage() != 0) setPage(0);
    }

    private void focusActiveRow() {
        if (selectedRow > -1) {
            final WidgetUI newFocus = table.getWidgetUI(selectedRow, selectedCol);
            if (newFocus.acceptsFocus()) newFocus.setFocus(true);  // try to focus the same col on the new row
            else focusRow(selectedRow);   // try to focus some elem in the row
        }
    }

    /** Focus given row. */
    private void focusRow(int row) {
        for (final WidgetUI cell : table.getRow(row)) {
            if (cell.acceptsFocus()) {
                cell.setFocus(true);
                break;
            }
        }
    }

    private boolean hasPager() {
        return pager != null;
    }

    private void leaveRowFocus() {
        if (selectedRow > -1) handler.onItemFocusLeave(mapSectionToItem(selectedRow));
    }

    /** Actual move of the rows in the model. */
    private void moveRow(int fromRow, int toRow) {
        handler.onRowMoved(fromRow, toRow);
    }

    /** Remove row by index. */
    private void removeRow(int row) {
        handler.onItemDeleted(mapSectionToItem(row));
    }

    /** Actual remove of the rows. */
    private void removeRows(int rowFrom, int amount) {
        for (int row = rowFrom + amount - 1; row >= rowFrom; row--) {
            handler.onRowDeleted(row);
            table.removeRow(row);
        }
    }

    /** Row is selected. */
    private void rowSelected() {
        final Option<Integer> row = selectedRow == -1 ? empty() : some(selectedRow);
        handler.onRowSelected(row);
    }

    private void selectActiveRow() {
        table.getRowElement(selectedRow).addClassName(TABLE_ACTIVE_ROW_STYLE);
        handler.onItemFocusEnter(mapSectionToItem(selectedRow));
    }

    /**
     * Called when CTRL+KEY_UP.
     *
     * <p>The intended behavior is: - if no pager then go to previous row, and if we reach the top,
     * go to last row. - if pager then go to previous row, but if we reach the top of the page, go
     * to the last row on the previous page. - also if we reach the top of the table go to the last
     * row on the last page.</p>
     */
    private void up(KeyDownEvent event) {
        final int previousRow = selectedRow - 1;
        if (previousRow <= -1) {
            if (hasPager() && pager.isFirstPage()) pager.lastPage();
            else if (hasPager()) previousPage();
            final int lastRow = getSectionsCount();
            setSelectedRow(lastRow - 1);
        }
        else setSelectedRow(previousRow);
        event.preventDefault();
    }

    private boolean isLazy() {
        return getModel().isLazy();
    }

    //~ Static Fields ................................................................................................................................

    public static final String TABLE_WIDGET_CLASSNAME = "tableWidget";

    static final String TABLE_EMPTY_MESSAGE = "tableEmptyMessage";

    private static final Logger logger = Logger.getLogger(TableUI.class);

    //~ Inner Interfaces .............................................................................................................................

    public interface TableRowHandler {
        /** Item to be created on item index. */
        int onItemCreated();

        /** Item on item index to be deleted. */
        default void onItemDeleted(int item) {
            logger.info("TableRowHandler.onItemDeleted item :: " + item);
        }

        /** Focus enter on item index. */
        default void onItemFocusEnter(int item) {
            logger.info("TableRowHandler.onItemFocusEnter item :: " + item);
        }

        /** Focus leave on item index. */
        default void onItemFocusLeave(int item) {
            logger.info("TableRowHandler.onItemFocusLeave item :: " + item);
        }

        /** Row clicked on table row index. */
        default void onRowClicked(int row) {
            logger.info("TableRowHandler.onRowClicked row :: " + row);
        }

        /** Row created on table row index. */
        default void onRowCreated(int row) {
            logger.info("TableRowHandler.onRowCreated row :: " + row);
        }

        /** Row deleted on table row index. */
        default void onRowDeleted(int row) {
            logger.info("TableRowHandler.onRowDeleted row :: " + row);
        }

        /** Row moved. */
        default void onRowMoved(int fromRow, int toRow) {
            logger.info("TableRowHandler.onRowMoved fromRow :: " + fromRow + " toRow :: " + toRow);
        }

        /** Row selected. */
        default void onRowSelected(Option<Integer> row) {
            logger.info("TableRowHandler.onRowSelected row :: " + row.orElse(-1));
        }
    }  // end interface TableRowHandler

    //~ Inner Classes ................................................................................................................................

    /**
     * Aggregate Footer Cell.
     */
    private class AggregateFooter {
        private final InlineHTML span;

        private AggregateFooter(@NotNull final InlineHTML span) {
            this.span = span;
        }

        private void update(@Nullable final String value) {
            span.setText(value);
        }
    }

    private class FlexTableRowWrapper implements HasWidgetsUI {
        private int       col;
        private final int row;

        private FlexTableRowWrapper(int row) {
            this.row = row;
            table.prepareCell(row, table.getColumnCount());
        }

        @Override public void addChild(WidgetUI widgetUI) {
            table.setCell(widgetUI.withinMultiple(TableUI.this, row, col++));
        }
    }
}  // end class TableUI
