
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.math.BigDecimal;
import java.util.List;

import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PieDataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.PlotOptions;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.ChartType;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.widget.ChartType.BAR;
import static tekgenesis.metadata.form.widget.ChartType.COLUMN;
import static tekgenesis.metadata.form.widget.ChartType.LINE;
import static tekgenesis.metadata.form.widget.ChartType.PIE;

/**
 * Utility class to deal with charts.
 */
public class Charts {

    //~ Constructors .................................................................................................................................

    private Charts() {}

    //~ Methods ......................................................................................................................................

    static void update(MultipleWidget widget, final MultipleModel model, PlotModel plot, PlotOptions options, ChartModelMapper mapper,
                       ChartConfig config) {
        final List<Widget> columns = seq(widget).toList();
        final ChartType    type    = widget.getChartType();

        if (type.is(COLUMN) || type.is(BAR) || type.is(LINE)) {
            if (mapper.hasLabel() && type.is(LINE) && config.isLineSteps())
                mapper.addLabels(map(model.getColumn(columns.get(mapper.getLabel())), Object::toString).toList());

            // Default Axis
            axis(model, (AxisOptions) options.getXAxisOptions(), (AxisOptions) options.getYAxisOptions(), mapper, type);

            mapper.resetRealRowMapper(columns.size());

            // Data
            final boolean empty = plot.getSeries().length() == 0;

            for (int i = 0; i < columns.size(); i++) {
                if (mapper.isMapped(i)) {
                    final Widget columnWidget = columns.get(i);

                    checkAndConfigureNewAxis(model, options, mapper, type, columnWidget);

                    final List<Object> column = toList(model.getColumn(columnWidget));
                    mapper.setRealRowMapperSeriesSize(i, column.size());

                    final SeriesHandler handler;

                    // Create a series
                    if (empty) {
                        final Series newSeries = Series.of(columnWidget.getLabel());
                        if (columnWidget.hasAxis()) setAxisAccordingly(type, columnWidget, newSeries);
                        handler = plot.addSeries(newSeries);
                    }
                    else {
                        // Empty a series
                        handler = plot.getHandlers().get(mapper.getColumn(i));
                        handler.clear();
                    }

                    addDataToSeries(mapper, type, i, column, handler);
                }
            }
        }
        else if (type.is(PIE)) {
            // Data
            plot.removeAllSeries();

            for (int i = 0; i < model.size(); i++) {
                final RowModel row = model.getRow(i);

                final SeriesHandler handler = plot.addSeries(
                        mapper.hasLabel() ? Series.of(notEmpty((String) row.getByFieldSlot(mapper.getLabel()), "")) : Series.create());

                getCell(row.getByFieldSlot(mapper.getFirst())).ifPresent(c -> handler.add(PieDataPoint.of(c)));
            }
        }
    }  // end method update

    private static void addDataToSeries(ChartModelMapper mapper, ChartType type, int i, List<Object> column, SeriesHandler handler) {
        int    fakeRow = 0;
        double x       = 1;
        for (int j = 0; j < column.size(); j++) {
            final Option<Double> cell = getCell(column.get(j));

            if (cell.isPresent()) {
                final Double y = cell.get();
                handler.add(point(x, y, type.is(BAR)));
                x++;

                // i is the series, j is the real row
                mapper.mapToRealRow(i, fakeRow, j);
                fakeRow++;
            }
            else {
                x--;
                handler.add(point(x, null, type.is(BAR)));
            }
        }
    }

    private static void axis(MultipleModel model, AxisOptions xAxisOptions, AxisOptions yAxisOptions, ChartModelMapper mapper, ChartType type) {
        if ((isLineOrColumn(type))) {
            if (mapper.hasLabel()) setTickFormatter(model, mapper, xAxisOptions);
            if (mapper.hasIntColumn()) integerAxisOptions(yAxisOptions);
        }
        if (type.is(BAR)) {
            if (mapper.hasLabel()) setTickFormatter(model, mapper, yAxisOptions);
            if (mapper.hasIntColumn()) integerAxisOptions(xAxisOptions);
        }
    }

    private static void checkAndConfigureNewAxis(MultipleModel model, PlotOptions options, ChartModelMapper mapper, ChartType type,
                                                 Widget columnWidget) {
        if (columnWidget.hasAxis()) {
            final int axis = columnWidget.getAxis();
            if ((isLineOrColumn(type))) {
                final AxisOptions yAxisOptions = (AxisOptions) options.getYAxisOptions(axis);
                if (yAxisOptions == null) {
                    final AxisOptions axisOptions = AxisOptions.create();
                    options.addYAxisOptions(axisOptions);
                    axis(model, (AxisOptions) options.getXAxisOptions(), axisOptions, mapper, type);
                }
            }

            if (type.is(BAR)) {
                final AxisOptions xAxisOptions = (AxisOptions) options.getXAxisOptions(axis);
                if (xAxisOptions == null) {
                    final AxisOptions axisOptions = AxisOptions.create();
                    options.addXAxisOptions(axisOptions);
                    axis(model, axisOptions, (AxisOptions) options.getYAxisOptions(), mapper, type);
                }
            }
        }
    }

    private static void integerAxisOptions(AxisOptions axisOptions) {
        axisOptions.setMinTickSize(1).setTickDecimals(0);
    }

    /**
     * Creates a {@link DataPoint} with the specified coordinates. Similar to GFlot one but uses
     * Double to allow null values (for stepped line charts).
     *
     * @param   x  Coordinate for x axis
     * @param   y  Coordinate for y axis
     *
     * @return  a {@link DataPoint} with the specified coordinates.
     */
    private static native DataPoint of(@Nullable final Double x, @Nullable final Double y)
    /*-{
        return [ x, y ];
    }-*/;

    @SuppressWarnings("SuspiciousNameCombination")
    private static DataPoint point(@Nullable final Double x, @Nullable final Double y, boolean transposed) {
        return transposed ? of(y, x) : of(x, y);
    }

    private static void setAxisAccordingly(ChartType type, Widget columnWidget, Series newSeries) {
        if (isLineOrColumn(type)) newSeries.setYAxis(columnWidget.getAxis());
        if (type.is(BAR)) newSeries.setXAxis(columnWidget.getAxis());
    }

    /** Knows how to cast an Object that is a supported chart value type. */
    private static Option<Double> getCell(Object o) {
        if (o instanceof Double) return some((Double) o);
        if (o instanceof Integer) return some(((Integer) o).doubleValue());
        if (o instanceof BigDecimal) return some(((BigDecimal) o).doubleValue());
        return Option.empty();
    }

    private static boolean isLineOrColumn(ChartType type) {
        return type.is(LINE) || type.is(COLUMN);
    }

    private static void setTickFormatter(final MultipleModel model, final ChartModelMapper mapper, AxisOptions options) {
        options.setTickFormatter((tick, axis) -> {
                if (tick == Math.rint(tick) && tick >= 1 && tick <= model.size())
                    return mapper.hasUniqueLabels() ? mapper.getLabels().get((int) tick - 1)
                                                    : notEmpty((String) model.getRow((int) tick - 1).getByFieldSlot(mapper.getLabel()), "");
                else return "";
            }).setTickLength(0).setTickSize(1);
    }
}  // end class Charts
