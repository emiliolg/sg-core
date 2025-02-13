
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.Kind;

import static tekgenesis.common.collections.Colls.seq;

/**
 * Class to analyze and map the columns of a given MultipleWidget to a proper chart model.
 */
class ChartModelMapper {

    //~ Instance Fields ..............................................................................................................................

    private final ImmutableList<Widget> columns;
    private final List<String>          labels;
    private final Map<String, Integer>  mapper;
    private int[][]                     realRowMapper;

    //~ Constructors .................................................................................................................................

    /** Creates a ChartModelMapper for the given multiple widget. */
    ChartModelMapper(MultipleWidget widget) {
        columns = seq(widget).toList();
        mapper  = new HashMap<>();
        map();
        realRowMapper = null;
        labels        = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Returns the first column that has data (is a number column) or -1 if there isn't any. */
    public int getFirst() {
        return mapper.getOrDefault(FIRST_KEY, -1);
    }

    /** Returns the label column if any, if not returns -1. */
    public int getLabel() {
        return mapper.getOrDefault(LABEL_KEY, -1);
    }

    public List<String> getLabels() {
        return labels;
    }

    /** Add unique label. */
    void addLabels(@NotNull final Collection<String> ls) {
        for (final String l : ls) {
            if (!labels.contains(l)) labels.add(l);
        }
    }

    /** Returns true if model has at least one displayable integer column. */
    boolean hasIntColumn() {
        return mapper.get(INTS_KEY) > 0;
    }

    /** Returns true if there is a label. */
    boolean hasLabel() {
        return mapper.containsKey(LABEL_KEY);
    }

    /** Has unique labels or should use them directly from the model. */
    boolean hasUniqueLabels() {
        return !labels.isEmpty();
    }

    void mapToRealRow(int column, int fakeRow, int realRow) {
        realRowMapper[column][fakeRow] = realRow;
    }

    void resetRealRowMapper(int columnSize) {
        realRowMapper = new int[columnSize][];
    }

    /** Returns the mapped index for a column or 'null' if the key is not mapped. */
    Integer getColumn(int column) {
        return mapper.get("" + column);
    }

    /** Returns true if the given column is mapped. */
    boolean isMapped(int column) {
        return mapper.containsKey("" + column);
    }

    int getMappedRow(int column, int fakeRow) {
        return realRowMapper[column][fakeRow];
    }

    void setRealRowMapperSeriesSize(int column, int rowSize) {
        realRowMapper[column] = new int[rowSize];
    }

    /** Given a series index, returns its real index. */
    int getSeriesIndex(int index) {
        return mapper.getOrDefault(SERIES_KEY + index, -1);
    }

    private void map() {
        mapper.put(INTS_KEY, 0);
        int index = 0;

        for (int i = 0; i < columns.size(); i++) {
            final Widget widget = columns.get(i);

            if (widget.getWidgetType() != WidgetType.INTERNAL) {
                if (widget.getType().isNumber()) {
                    if (getFirst() == -1) mapper.put(FIRST_KEY, i);
                    mapper.put(INTS_KEY, widget.getType().getKind() == Kind.INT ? mapper.get(INTS_KEY) + 1 : mapper.get(INTS_KEY) - 1);
                    mapper.put("" + i, index);
                    mapper.put(SERIES_KEY + index, i);
                    index++;
                }
                else if (widget.getType().isString()) mapper.put(LABEL_KEY, i);
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String FIRST_KEY  = "F";
    private static final String SERIES_KEY = "S";
    private static final String LABEL_KEY  = "L";

    private static final String INTS_KEY = "I";
}
