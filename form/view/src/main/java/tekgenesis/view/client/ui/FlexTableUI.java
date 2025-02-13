
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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.aggregate.AggregateFn;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.PopoverType;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.Type;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.base.Popover;
import tekgenesis.view.client.ui.multiple.SorterLens.SortType;
import tekgenesis.view.client.ui.tablefilters.Comparison;

import static com.google.gwt.dom.client.Element.DRAGGABLE_TRUE;
import static com.google.gwt.dom.client.Style.Display.NONE;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.model.FormConstants.CLICKABLE_TABLE;
import static tekgenesis.metadata.form.model.FormConstants.DRAG_STARTED;
import static tekgenesis.metadata.form.model.FormConstants.DROP_TARGET;
import static tekgenesis.metadata.form.model.FormConstants.NUMBER_COLUMN_CLASS;
import static tekgenesis.metadata.form.widget.WidgetType.COMBO_BOX;
import static tekgenesis.metadata.form.widget.WidgetType.PROGRESS;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;
import static tekgenesis.view.client.ui.tablefilters.FilterUI.FilterWidget;

/**
 * Extension of GWT's FlexTable that adds header and footer management.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "NonJREEmulationClassesInClientCode" })
public class FlexTableUI extends FlexTable {

    //~ Instance Fields ..............................................................................................................................

    private DragHandler                      dragHandler;
    private FilterHandler                    filterHandler;
    private final List<Option<FilterWidget>> filterWidgets = new ArrayList<>();

    private final Element                   footer;
    private final Map<AggregateFn, Element> footerRows = new LinkedHashMap<>();

    private final Element headerTr;
    private SortHandler   sortHandler;

    private final List<Option<ExtButton>> sortWidgets = new ArrayList<>();
    private final List<Element>           spans       = new ArrayList<>();

    //~ Constructors .................................................................................................................................

    FlexTableUI() {
        final Element head = DOM.createTHead();
        headerTr = DOM.createTR();
        footer   = DOM.createTFoot();
        DOM.insertChild(getElement(), head, 0);
        DOM.insertChild(head, headerTr, 0);
        DOM.appendChild(getElement(), footer);
        dragHandler   = null;
        sortHandler   = null;
        filterHandler = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public int insertRow(int beforeRow) {
        final int r = super.insertRow(beforeRow);

        if (dragHandler != null) {
            final InlineHTML drag = span();
            // use fontawesome icons?
            drag.addStyleName("table-drag fa");
            drag.getElement().setDraggable(DRAGGABLE_TRUE);

            drag.addDragStartHandler(event -> {
                if (cannotDragRow(drag)) return;
                final int     row        = findRowIndex(event);
                final Element rowElement = getRowElement(row);
                rowElement.getStyle().setOpacity(DRAG_HANDLER_OPACITY);
                event.getDataTransfer().setDragImage(rowElement, 0, 5);
                event.setData("row", String.valueOf(row));
                rowElement.removeClassName("active alert");
                addStyleName(DRAG_STARTED);
            });

            drag.addDragEndHandler(event -> removeStyleName(DRAG_STARTED));

            drag.addDragEnterHandler(event -> {
                if (cannotDragRow(drag)) return;
                final Element eventTarget = event.getRelativeElement();
                eventTarget.addClassName(DROP_TARGET);
            });

            drag.addDragOverHandler(DragOverEvent::preventDefault);

            drag.addDragLeaveHandler(event -> {
                if (cannotDragRow(drag)) return;
                final Element eventTarget = event.getRelativeElement();
                eventTarget.removeClassName(DROP_TARGET);
            });

            drag.addDropHandler(event -> {
                event.preventDefault();
                if (cannotDragRow(drag)) return;
                final int     row         = findRowIndex(event);
                final Element eventTarget = event.getRelativeElement();
                eventTarget.removeClassName(DROP_TARGET);
                eventTarget.getStyle().clearOpacity();
                final String rowData = event.getData("row");
                if (!isEmpty(rowData)) {
                    final Integer eventRow = Integer.valueOf(rowData);
                    if (eventRow != row) exchangeRows(eventRow, row);
                }
            });
            setWidget(r, 0, drag);
        }

        return r;
    }  // end method insertRow

    @Override protected void onDetach() {
        super.onDetach();

        // onDetach of table, we must clean eventlisteners to prevent memory leaks as per GWT doc.
        for (final Option<ExtButton> w : sortWidgets)
            for (final ExtButton b : w)
                DOM.setEventListener(b.getElement(), null);

        // onDetach of table, we must clean eventlisteners to prevent memory leaks as per GWT doc.
        for (final Option<FilterWidget> w : filterWidgets)
            for (final FilterWidget f : w)
                f.detach();
    }

    /** Exposed to enhance row's cell creation. */
    @Override
    @SuppressWarnings("EmptyMethod")  // Only calls super, BUT makes it accesible to TableUI :)
    protected void prepareCell(int row, int column) {
        super.prepareCell(row, column);
    }

    /** Adds a custom drag handler, note that this is a 'custom' drag handler not the GWT one. */
    void addCustomDragHandler(DragHandler handler) {
        dragHandler = handler;
    }

    /** Registers a FilterHandler to be notified on filter. */
    void addFilterHandler(FilterHandler handler) {
        filterHandler = handler;
    }

    /** Registers a SortHandler to be notified on sort. */
    void addSortHandler(SortHandler handler) {
        sortHandler = handler;
    }

    /** Tells the table that is filtered by that column. */
    void filterBy(String column, String value) {
        filters().forEach(f -> f.select(isNotEmpty(value) && equal(f.getColumn(), column)));
    }

    void prepareFooter(int column) {
        for (final AggregateFn fn : footerRows.keySet())
            prepareFooter(fn, column);
    }

    void resetFilters() {
        filters().forEach(FilterWidget::reset);
    }

    void setCell(WidgetUI cell) {
        final Integer row    = cell.getContext().getRow().get();
        final Integer column = cell.getContext().getCol().get();
        setWidget(row, column + 1, cell);
    }

    /** Return all cells in table. */
    Iterable<WidgetUI> getCells() {
        return filter(this, IS_WIDGET_UI).map(TO_WIDGET_UI);
    }

    /** Sets style to a particular cell. */
    void setCellStyle(int row, int col, String style) {
        final int realCol = col + 1;
        getCellFormatter().setStyleName(row, realCol, style);
    }

    void setCellVisible(int row, int col, boolean visible) {
        final int realCol = col + 1;
        getCellFormatter().setVisible(row, realCol, visible);
    }

    Iterable<WidgetUI> getCol(int col) {
        final Collection<WidgetUI> result = new ArrayList<>(getRowCount());
        for (int row = 0; row < getRowCount(); row++)
            result.add(getWidgetUI(row, col));
        return result;
    }

    /** Returns the header UI widgets. Its minus 1 because of the first draggable column. */
    int getColumnCount() {
        return Math.max(DOM.getChildCount(headerTr) - 1, 0);
    }

    void setFooter(@NotNull AggregateFn fn, int column, @NotNull Widget cell) {
        final Element footerTr = prepareFooter(fn, column);
        cell.removeFromParent();
        final Element td = DOM.getChild(footerTr, column);
        td.setClassName(NUMBER_COLUMN_CLASS);
        DOM.appendChild(td, cell.getElement());
        adopt(cell);
    }

    void setFooterVisibility(boolean visible) {
        setVisible(footer, visible);
    }

    void setHeader(WidgetUI cell, boolean sortable, Option<FilterWidget> filter) {
        final Integer column = cell.getContext().getCol().get();
        setHeader(column + 1, cell.getModel(), sortable, filter);
    }

    void setHeaderFooterVisible(int col, boolean visible) {
        final int realCol = col + 1;
        // Header
        setVisible(DOM.getChild(headerTr, realCol), visible);

        // Footer
        for (final Element footerTr : footerRows.values())
            setVisible(DOM.getChild(footerTr, realCol), visible);
    }

    void setHeaderStyle(int col, String style) {
        final int realCol = col + 1;
        setStyleName(DOM.getChild(headerTr, realCol), style);
    }

    void setLabel(final String text, final int col) {
        spans.get(col).setInnerText(text);
    }

    Iterable<WidgetUI> getRow(int row) {
        final int                  userCells = getUserCellCount(row);
        final Collection<WidgetUI> result    = new ArrayList<>(userCells);
        for (int col = 0; col < userCells; col++)
            result.add(getWidgetUI(row, col));
        return result;
    }

    Element getRowElement(int row) {
        final Element element = getBodyElement();
        return DOM.getChild(element, row);
    }

    @Nullable IntIntTuple getRowForEvent(Event event) {
        final Element td = getEventTargetCell(event);
        if (td == null) return null;
        final int column = TableCellElement.as(td).getCellIndex();
        if (column == 0) return null;

        final int row = TableRowElement.as(td.getParentElement()).getSectionRowIndex();
        return tuple(row, column - 1);
    }

    /** Sets a 'sorting by' type to the specified column. */
    void setSortingBy(int column, SortType sortType) {
        for (final ExtButton headerWidget : sortWidgets.get(column))
            setSortIcon(headerWidget, sortType);
    }

    void setTableAsClickable() {
        addStyleName(CLICKABLE_TABLE);
    }

    /** Return user content cells, excludes dragging row cell. */
    int getUserCellCount(int row) {
        return getCellCount(row) - 1;
    }

    int getVisibleColumns() {
        int result = getColumnCount();
        for (int i = result; i > 0; i--) {
            final Element child = DOM.getChild(headerTr, i);
            if (child.getStyle().getDisplay().equals(NONE.getCssName())) result--;
        }
        return result;
    }

    WidgetUI getWidgetUI(int row, int col) {
        return TO_WIDGET_UI.apply(getWidget(row, col + 1));
    }

    void setWidth(final int width, final int col) {
        final Element th = DOM.getChild(headerTr, col + 1);
        th.getStyle().setWidth(width, Style.Unit.PCT);
    }  // end method setWidth

    private void addFilterHandler(String col, Element th, FilterWidget filter) {
        if (filterHandler != null) filter.setFilterHandler(col, filterHandler);

        final FlowPanel filterPanel = filter.asFlowPanel();
        filterPanel.addStyleName("filter-panel");

        final Popover popover = new Popover(PopoverType.TOP);
        filter.setParentPopover(popover);
        popover.addContent(filterPanel);
        popover.setLinkIcon(IconType.FILTER.getClassName());

        // Attach physically to DOM
        DOM.appendChild(th, popover.getElement());

        // Attach the filter UI widget.
        filter.attach();

        // Attach logically (prepare element to receive click events).
        DOM.setEventListener(popover.getLink().getElement(), popover.getLink());
        DOM.sinkEvents(popover.getLink().getElement(), Event.ONCLICK);
        adopt(popover);
    }

    private boolean cannotDragRow(InlineHTML drag) {
        return drag.getElement().hasAttribute("drag") && !Boolean.valueOf(drag.getElement().getAttribute("drag"));
    }

    //J-
    private native void addCellsToRow(Element row, int column, boolean isHeader)  /*-{
        for(var i = 0; i < column; i++) {
            var cell = $doc.createElement(isHeader ? "th" : "td");
            row.appendChild(cell);
        }
    }-*/;
    //J+

    private void exchangeRows(int fromRow, int toRow) {
        final Element tbody  = getBodyElement();
        final Node    child1 = tbody.getChild(fromRow);
        final Node    child2 = tbody.getChild(toRow);
        ((Element) child1).getStyle().clearOpacity();
        ((Element) child2).getStyle().clearOpacity();

        tbody.insertBefore(child1, child2);

        if (fromRow > toRow) {
            // moving row up
            for (int i = fromRow; i >= toRow; i--)
                for (final WidgetUI widgetUI : getRow(i))
                    widgetUI.getContext().setRow(i);
        }
        else {
            // moving row down
            for (int i = fromRow; i <= toRow; i++)
                for (final WidgetUI widgetUI : getRow(i))
                    widgetUI.getContext().setRow(i);
        }

        if (dragHandler != null) dragHandler.rowMoved(fromRow, toRow);
    }

    @NotNull private Seq<FilterWidget> filters() {
        return filter(filterWidgets, Option::isPresent).map(Option::get);
    }

    private int findRowIndex(DomEvent<?> event) {
        final Element span  = event.getRelativeElement();
        final Element td    = span.getParentElement();
        final Element tr    = td.getParentElement();
        final Element tbody = tr.getParentElement();
        for (int i = 0; i < tbody.getChildCount(); i++) {
            if (tbody.getChild(i).equals(tr)) return i;
        }
        throw new IllegalStateException();
    }

    @NotNull private Element prepareFooter(AggregateFn fn, int column) {
        if (column < 0) throw new IndexOutOfBoundsException("Cannot create a footer column with a negative index: " + column);
        final Element footerTr  = prepareFooterRow(fn);
        final int     cellCount = DOM.getChildCount(footerTr);
        final int     required  = column + 1 - cellCount;
        if (required > 0) addCellsToRow(footerTr, required, false);
        return footerTr;
    }

    private Element prepareFooterRow(AggregateFn fn) {
        Element footerTr = footerRows.get(fn);
        if (footerTr == null) {
            footerTr = DOM.createTR();
            footerTr.addClassName("aggregate-" + fn.name().toLowerCase());
            DOM.appendChild(footer, footerTr);
            addCellsToRow(footerTr, 1, false);
            final Element td = DOM.getChild(footerTr, 0);
            td.setInnerHTML(resolveAggregateLabel(fn));
            footerRows.put(fn, footerTr);
        }
        return footerTr;
    }

    private void prepareHeader(int column) {
        if (column < 0) throw new IndexOutOfBoundsException("Cannot create a header column with a negative index: " + column);
        final int cellCount = DOM.getChildCount(headerTr);
        final int required  = column + 1 - cellCount;
        if (required > 0) addCellsToRow(headerTr, required, true);
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    private void resetAllButMe(ExtButton icon) {
        for (final Option<ExtButton> widget : sortWidgets) {
            for (final ExtButton headerWidget : widget) {
                if (!headerWidget.equals(icon)) headerWidget.setIcon(IconType.SORT);
            }
        }
    }

    private String resolveAggregateLabel(AggregateFn fn) {
        final String result;
        switch (fn) {
        case SUM:
            result = MSGS.total();
            break;
        case AVG:
            result = MSGS.average();
            break;
        case MAX:
            result = MSGS.max();
            break;
        case MIN:
            result = MSGS.min();
            break;
        case COUNT:
            result = MSGS.count();
            break;
        case ROWS:
            result = MSGS.rows();
            break;
        case FIRST:
            result = MSGS.first();
            break;
        case LAST:
        default:
            result = MSGS.last();
            break;
        }
        return result;
    }

    private boolean supportsSort(Type m, WidgetType t) {
        return !m.isNull() && !m.isArray() && !m.isResource() && t != WidgetType.IMAGE;
    }

    private void setHeader(final int col, tekgenesis.metadata.form.widget.Widget column, boolean sortable, Option<FilterWidget> filter) {
        prepareHeader(col);

        final String text = column.getLabel();

        final Element th = DOM.getChild(headerTr, col);

        final Type       model  = column.getType();
        final WidgetType widget = column.getWidgetType();

        if (model.isNumber() && widget != PROGRESS && widget != COMBO_BOX) th.setClassName(NUMBER_COLUMN_CLASS);

        th.addClassName("column-label");

        // check that the columns is actually sortable before adding the handler
        if (sortable && supportsSort(model, widget)) setSortHandler(col, th);
        else sortWidgets.add(Option.empty());

        final InlineHTML span = span(text);
        // span.addStyleName("column-label");
        spans.add(span.getElement());
        DOM.appendChild(th, span.getElement());

        filterWidgets.add(filter);
        if (filter.isPresent()) addFilterHandler(column.getName(), th, filter.get());
    }  // end method setHeader

    private void setSortHandler(final int column, Element th) {
        final ExtButton icon = new ExtButton();
        icon.setIcon(IconType.SORT);
        icon.removeStyleName("btn");
        icon.addClickHandler(event -> {
            final NativeEvent nativeEvent = event.getNativeEvent();
            final boolean     addSort     = nativeEvent.getShiftKey();

            final IconType iconType = icon.getIcon().getIconType();

            final SortType sortType;
            if (iconType == IconType.SORT) sortType = SortType.ASCENDING;
            else if (iconType == IconType.SORT_DESC) sortType = SortType.DESCENDING;
            else sortType = SortType.RESET;

            setSortIcon(icon, sortType);
            if (sortHandler != null) sortHandler.sorted(addSort, column - 1, sortType);

            if (!addSort) resetAllButMe(icon);
        });

        // Attach physically to DOM
        DOM.appendChild(th, icon.getElement());

        // Attach logically (prepare element to receive click events).
        DOM.setEventListener(icon.getElement(), icon);
        DOM.sinkEvents(icon.getElement(), Event.ONCLICK);
        adopt(icon);

        sortWidgets.add(some(icon));
    }  // end method setSortHandler

    private void setSortIcon(ExtButton iconWidget, SortType sortType) {
        final IconType icon;
        switch (sortType) {
        case ASCENDING:
            icon = IconType.SORT_DESC;
            break;
        case DESCENDING:
            icon = IconType.SORT_ASC;
            break;
        default:  // RESET
            icon = IconType.SORT;
            break;
        }
        iconWidget.setIcon(icon);
    }

    //~ Static Fields ................................................................................................................................

    private static final double DRAG_HANDLER_OPACITY = 0.7;

    private static final Function<Widget, WidgetUI> TO_WIDGET_UI = value -> (WidgetUI) value;

    private static final Predicate<Widget> IS_WIDGET_UI = widget -> widget instanceof WidgetUI;

    //~ Inner Interfaces .............................................................................................................................

    interface DragHandler {
        /** Called when user moves a row. */
        void rowMoved(int formRow, int toRow);
    }

    public interface FilterHandler {
        /** Called when user chooses to filter by a column, with a criteria and a value. */
        void filtered(String column, Comparison c, String value);
    }
}  // end class FlexTableUI
