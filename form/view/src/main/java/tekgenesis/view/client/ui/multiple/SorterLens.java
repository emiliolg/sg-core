
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.FormStorage;
import tekgenesis.view.client.ui.TableUI;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.metadata.form.widget.WidgetType.INTERNAL;
import static tekgenesis.metadata.form.widget.WidgetTypes.isGroup;
import static tekgenesis.view.client.ui.multiple.MultipleModelLens.CompoundLens;
import static tekgenesis.view.client.ui.multiple.SorterLens.SortType.RESET;

/**
 * Creates a TableSortLens Impl for a particular table in a form.
 */
public class SorterLens extends CompoundLens {

    //~ Instance Fields ..............................................................................................................................

    private int                            column    = -1;
    private List<Tuple<Integer, SortType>> criteria;
    private final String                   formName;
    private List<Integer>                  rowMapper;
    private SortType                       type      = SortType.RESET;

    //~ Constructors .................................................................................................................................

    /** Creates a SorterLens for a form/table combination. */
    private SorterLens(String formName) {
        this.formName = formName;
        rowMapper     = new ArrayList<>();
        criteria      = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public void doReact(@NotNull LensEvent event) {
        if (event instanceof SortEvent) {
            final SortEvent e = (SortEvent) event;
            criteria = e.getCriteria();
            column   = criteria.get(0).first();
            type     = criteria.get(0).second();
        }
        else if (event instanceof ItemEvent) cardinalityChanged((ItemEvent) event);

        if (event.refresh()) doSort();
    }

    /** If its the same updates the internal mapper. */
    /*public void update(SorterLens s) {
     *  if (s.formName.equals(formName) && s.model.getMultipleWidget().getName().equals(model.getMultipleWidget().getName())) rowMapper = s.rowMapper;
     *}*/

    @Override protected Option<Integer> itemToSection(int item) {
        final int section = rowMapper.indexOf(item);
        return section >= 0 ? of(section) : empty();
    }

    @Override protected int sectionToItem(int section) {
        if (section < rowMapper.size()) return rowMapper.get(section);
        else {  // if it isn't in the rowMapper, add it and return it.
            rowMapper.add(section);
            return section;
        }
    }           // end method sectionToItem

    private void cardinalityChanged(ItemEvent refresh) {
        switch (refresh.getType()) {
        case ITEM_CREATED:
            final int item = refresh.getItem();
            rowMapper.add(item, item);
            break;
        case ITEM_DELETED:
            doSort();
            break;
        }
    }

    private void doSort() {
        if (type == RESET) reset();
        else sort();

        storeSort(formName, getMultipleWidget(), column, type);
    }

    private void reset() {
        final int size = size();
        rowMapper = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            rowMapper.add(i);
    }

    /** Effectively sort. Called on react. */
    private void sort() {
        if (rowMapper.size() != size()) reset();
        Collections.sort(rowMapper, new SorterLensComparator(criteria));
    }  // end method sort

    //~ Methods ......................................................................................................................................

    /** Restores Sorter Lens from the local storage. */
    @NotNull public static SorterLens create(final String formName) {
        return new SorterLens(formName);
    }

    public static void reactToStoredSort(final String formName, final TableUI table, MultipleWidget multiple) {
        for (final FormStorage storage : FormStorage.getInstance()) {
            final String sortedBy = storage.get(listOf("sort", formName, table.getId()).mkString("/"));
            if (isNotEmpty(sortedBy)) {
                final List<String> parts    = Strings.split(sortedBy, '~');
                final int          column   = Integer.parseInt(parts.get(0));
                final SortType     sortType = SortType.valueOf(parts.get(1));

                final String columnName = parts.size() > 2 ? parts.get(2) : "";

                final boolean validColumnIdx = column != -1 && column < multiple.getFieldDimension();
                if (validColumnIdx && multiple.getWidgetByFieldSlot(column).getName().equals(columnName)) {
                    try {
                        final SortEvent sortEvent = new SortEvent(column, sortType);
                        table.setSortingBy(column, sortType);
                        table.react(sortEvent);
                    }
                    catch (final ArrayIndexOutOfBoundsException e) {
                        LOGGER.info("Trying to sort a column that no longer exist: " + column);
                    }
                }
            }
        }
    }

    /** Stores a particular sort criteria in the user's local storage. */
    private static void storeSort(final String formName, MultipleWidget multipleWidget, final int column, final SortType type) {
        for (final FormStorage storage : FormStorage.getInstance()) {
            final String            tableId     = multipleWidget.getName();
            final ArrayList<String> sortingData = new ArrayList<>();
            sortingData.add("" + column);
            sortingData.add(type.name());

            if (column != -1) sortingData.add(multipleWidget.getWidgetByFieldSlot(column).getName());

            storage.set(listOf("sort", formName, tableId).mkString("/"), toList(sortingData).mkString("~"));
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(SorterLens.class);

    //~ Enums ........................................................................................................................................

    public enum SortType {
        RESET, ASCENDING,
        DESCENDING { public int getSortMultiplier() { return -1; } };

        /** Sort multiplier. */
        public int getSortMultiplier() {
            return 1;
        }
    }

    //~ Inner Classes ................................................................................................................................

    private class SorterLensComparator implements Comparator<Integer> {
        private final List<Tuple<Integer, SortType>> columns;

        public SorterLensComparator(List<Tuple<Integer, SortType>> columns) {
            this.columns = columns;
        }

        @Override public int compare(final Integer i1, final Integer i2) {
            for (final Tuple<Integer, SortType> sortCriteria : columns) {
                final Integer resolvedColumn = resolveColumn(sortCriteria.first());
                for (final Integer result : compareByCol(resolvedColumn, sortCriteria.second(), i1, i2)) {
                    if (result != 0) return result;
                }
            }
            return 0;
        }

        private Option<Integer> compareByCol(int col, SortType sortType, Integer i1, Integer i2) {
            final RowModel row1 = getItemModel(i1);
            final RowModel row2 = getItemModel(i2);

            final Widget widget1 = row1.widgetByFieldSlot(col);
            final Widget widget2 = row2.widgetByFieldSlot(col);

            Object val1 = row1.get(widget1);
            Object val2 = row2.get(widget2);
            if (val1 == null) return of(val2 == null ? 0 : sortType.getSortMultiplier());
            if (val2 == null) return of(-sortType.getSortMultiplier());

            if (widget1.getType().isDatabaseObject() || widget1.getType().isEnum()) {
                // use describe by
                val1 = coverText(row1.getOptions(widget1).get(val1));
                val2 = coverText(row2.getOptions(widget2).get(val2));
            }

            if (val1 instanceof String && val2 instanceof String) {
                final String a = cast(val1);
                final String b = cast(val2);
                // noinspection NonJREEmulationClassesInClientCode
                return of((a.compareToIgnoreCase(b)) * sortType.getSortMultiplier());
            }

            if (val1 instanceof Comparable || val2 instanceof Comparable) {
                final Comparable<Object> a = cast(val1);
                final Comparable<Object> b = cast(val2);
                return of((a == b ? 0 : a.compareTo(b)) * sortType.getSortMultiplier());
            }

            LOGGER.warning("Trying to sort column: " + col + ", val 1: " + val1 + " and val 2: " + val2);
            return Option.empty();
        }

        private int resolveColumn(int c) {
            int i      = 0;
            int result = c;

            for (final Widget widget : getMultipleWidget()) {
                if (isGroup(widget.getWidgetType())) {
                    // If we are trying to sort a group column, we sort by its first widget.
                    if (i == result) return result + 1;

                    final int childs = widget.getChildren().size();

                    i      += childs + 1;
                    result += childs;
                }
                else if (widget.getWidgetType() == INTERNAL) {  // Skip internal cols.
                    i++;
                    result++;
                }
                else {
                    if (i == result) return result;
                    i++;
                }
            }
            return -1;
        }
    }  // end class SorterLensComparator
}  // end class SorterLens
