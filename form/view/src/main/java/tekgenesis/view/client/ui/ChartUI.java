
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
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.Tick;
import com.googlecode.gflot.client.event.PlotClickListener;
import com.googlecode.gflot.client.event.PlotHoverListener;
import com.googlecode.gflot.client.event.PlotItem;
import com.googlecode.gflot.client.event.PlotPosition;
import com.googlecode.gflot.client.jsni.Plot;
import com.googlecode.gflot.client.options.*;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.configuration.AxisConfig;
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.configuration.SeriesMode;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.widget.ChartType;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static java.lang.Math.round;

import static com.googlecode.gflot.client.options.AbstractAxisOptions.AxisPosition.BOTTOM;
import static com.googlecode.gflot.client.options.AbstractAxisOptions.AxisPosition.LEFT;
import static com.googlecode.gflot.client.options.AbstractAxisOptions.AxisPosition.RIGHT;
import static com.googlecode.gflot.client.options.AbstractAxisOptions.AxisPosition.TOP;
import static com.googlecode.gflot.client.options.BarSeriesOptions.create;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.configuration.ChartConfig.DEFAULT;
import static tekgenesis.metadata.form.configuration.SeriesMode.OVERLAPPED;
import static tekgenesis.metadata.form.configuration.SeriesMode.SIDE_BY_SIDE;
import static tekgenesis.metadata.form.configuration.SeriesMode.STACKED;
import static tekgenesis.metadata.form.widget.ChartType.BAR;
import static tekgenesis.metadata.form.widget.ChartType.COLUMN;
import static tekgenesis.metadata.form.widget.ChartType.LINE;
import static tekgenesis.metadata.form.widget.ChartType.PIE;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Chart UI widget.
 */
public class ChartUI extends BaseMultipleUI implements MultipleUI {

    //~ Instance Fields ..............................................................................................................................

    private ChartConfig config               = DEFAULT;
    private boolean     couldApplySeriesMode = false;

    private ChartEventHandler eventHandler;

    private final List<ChartHandler> handlers;
    private HTML                     labelContent;
    private final ChartModelMapper   mapper;

    private PlotModel        model;
    private PlotOptions      options;
    private final SimplePlot plot;

    private PopupPanel         popup;
    private Option<InlineHTML> title = Option.empty();

    //~ Constructors .................................................................................................................................

    /** Create chart UI widget. */
    public ChartUI(@NotNull final ModelUI container, @NotNull final MultipleWidget widget) {
        super(container, widget);
        handlers = new ArrayList<>();
        plot     = createPlot();
        createHoverablePopup();

        final String widgetLabel = widget.getLabel();
        if (isNotEmpty(widgetLabel)) {
            final InlineHTML t = HtmlWidgetFactory.span(widgetLabel);
            t.addStyleName("chartTitle");
            div.add(t);
            title = some(t);
        }

        plot.addStyleName(CHART_STYLE);

        initWidget(plot);

        mapper = new ChartModelMapper(widget);
        applyConfig(DEFAULT);
    }

    //~ Methods ......................................................................................................................................

    /** Add chart handlers. */
    public void addChartHandler(final ChartHandler handler) {
        handlers.add(handler);
    }

    /** Apply a new configuration. */
    public void applyConfig(@NotNull final ChartConfig c) {
        config = c;

        // Global Options
        setGlobalOptions(options, getMultipleModel().getChartType(), config);

        // Width
        configDimensions();

        // Hoverable
        setHoverable(config.isHoverable());

        // Legend
        plot.getOptions().getLegendOptions().setShow(config.hasLegend());

        // Vertical labels
        if (config.hasVerticalLabels()) plot.addStyleName(CHART_VERTICAL_LABEL);
        else plot.removeStyleName(CHART_VERTICAL_LABEL);

        // Axes
        configAxis(plot.getOptions().getXAxesOptions(), config.getXAxis(), true, config.isPositiveQuadrant());
        configAxis(plot.getOptions().getYAxesOptions(), config.getYAxis(), false, config.isPositiveQuadrant());

        // Series
        configSeries();

        plot.redraw();
    }  // end method applyConfig

    @Override public void toggleFilteredSection(int rowIndex, boolean accepted) {
        throw new UnsupportedOperationException("toggleFilteredRow() on ChartUI!");
    }

    /** Update chart model. */
    @Override public void updateModel(@NotNull final MultipleModel multiple) {
        couldApplySeriesMode = !multiple.isEmpty();
        Charts.update(getMultipleModel(), multiple, model, options, mapper, config);

        // Only redraw if visible (flot uses containers width, and if it's not visible uses 0 as width, aka error!)
        if (isVisible()) plot.redraw();
    }

    /** Toggles series visibility. */
    public void setColumnVisibility(final int column, @NotNull final Boolean visible) {
        final Integer mappedColumn = mapper.getColumn(column);
        if (mappedColumn != null) {
            final SeriesHandler handler = model.getHandlers().get(mappedColumn);
            handler.setVisible(visible);
            plot.redraw();
        }
    }

    @Override public Iterable<WidgetUI> getSection(int section) {
        return emptyIterable();
    }

    /** Set series label. */
    public void setSeriesLabel(final int column, @NotNull final String l) {
        final Integer mappedColumn = mapper.getColumn(column);
        if (mappedColumn != null) {
            final Series series = model.getSeries().get(mappedColumn);
            series.setLabel(l);
            plot.redraw();
        }
    }

    @Override protected void fitToExactSections() {}

    @NotNull @Override Option<Element> createLabel() {
        return title.map(UIObject::getElement);
    }

    private void configAxis(AxesOptions axesOptions, List<AxisConfig> axesConfig, boolean traverse, boolean positiveQuadrant) {
        for (int i = 0; i < axesOptions.length(); i++) {
            final AbstractAxisOptions<?> axisOptions = axesOptions.get(i);

            if (i < axesConfig.size()) {
                final AxisConfig axisConfig = axesConfig.get(i);
                axisOptions.setLabel(axisConfig.getLabel());
                axisOptions.setShow(axisConfig.isVisible());

                if (axisConfig.isAxisOnSecondary()) axisOptions.setPosition(traverse ? TOP : RIGHT);
                else axisOptions.setPosition(traverse ? BOTTOM : LEFT);

                if (positiveQuadrant) axisOptions.setMinimum(0D);

                if (axisConfig.isMinimumSet()) axisOptions.setMinimum(axisConfig.getMinimum());
                else axisOptions.clearMinimum();

                if (axisConfig.isMaximumSet()) axisOptions.setMaximum(axisConfig.getMaximum());
                else axisOptions.clearMaximum();
            }
        }
    }

    private void configDimensions() {
        final int configWidth = config.getWidth();
        if (configWidth != 0) {
            plot.setWidth(configWidth);
            plot.removeStyleName(AUTO_WIDTH);
            if (title.isPresent()) title.get().setWidth(config.getWidthPx());
        }
        else {
            plot.addStyleName(AUTO_WIDTH);
            if (title.isPresent()) title.get().setWidth("auto");
        }

        // Height
        final int configHeight = config.getHeight();
        plot.setHeight(configHeight != 0 ? configHeight : DEFAULT.getHeight());
    }

    @SuppressWarnings("AssignmentToForLoopParameter")  // Yes, but it's not affecting the looping.
    private void configSeries() {
        final JsArray<Series> series     = model.getSeries();
        final SeriesMode      seriesMode = config.getSeriesMode();
        for (int i = 0, colorIndex = 0; i < series.length(); i++) {
            final Series current = series.get(i);

            // Color.
            if (!config.getColors().isEmpty()) {
                if (colorIndex >= config.getColors().size()) colorIndex = 0;  // reset to make it cycle...
                if (isEmpty(config.getColors().get(colorIndex))) current.clear(COLOR_KEY);
                else current.setColor(config.getColors().get(colorIndex++));
            }
            else current.clear(COLOR_KEY);

            // Series mode.
            if (couldApplySeriesMode) {  // Theres an issue on GFlot when applying BarsSeriesOptions on empty data.
                current.setBarsSeriesOptions(create());
                if (seriesMode == STACKED) current.setStack(true);
                if (seriesMode == OVERLAPPED) current.setStack(false);
                if (seriesMode == SIDE_BY_SIDE)
                    current.setBarsSeriesOptions(create().setOrder(i).setBarWidth(MAGIC_BAR_WIDTH / series.length()).setLineWidth(1));
            }
        }
    }

    private void createHoverablePopup() {
        popup = HtmlWidgetFactory.popup();
        popup.addStyleName("tooltip top info-tooltip tooltip-inner padding5");
        labelContent = HtmlWidgetFactory.html();
        popup.add(labelContent);
    }

    private SimplePlot createPlot() {
        model        = new PlotModel();
        eventHandler = new ChartEventHandler();

        final GridOptions   gridOptions   = GridOptions.create().setBorderWidth(0);
        final LegendOptions legendOptions = LegendOptions.create().setShow(false);

        options = PlotOptions.create();
        options.setGridOptions(gridOptions);
        options.setLegendOptions(legendOptions);
        options.addXAxisOptions(AxisOptions.create());
        options.addYAxisOptions(AxisOptions.create());

        final SimplePlot simplePlot = new SimplePlot(model, options);

        if (getMultipleModel().hasOnClickMethod()) {
            simplePlot.addStyleName(FormConstants.CLICKABLE);
            gridOptions.setClickable(true);
            simplePlot.addClickListener(eventHandler, true);
        }

        simplePlot.addAttachHandler(event -> {
            if (!event.isAttached()) popup.removeFromParent();
        });

        return simplePlot;
    }

    /** Displays at posX, posY the given value. */
    private void displayValueAt(final int posX, final int posY, @NotNull final Double value, @NotNull final String l) {
        final StringBuilder text = new StringBuilder();
        if (!l.isEmpty() && config.hasLabelsOnHover()) text.append(l).append("</br>");
        text.append(MSGS.hoverableValueLabel()).append(value);
        labelContent.setHTML(text.toString());

        popup.setPopupPosition(posX + X_OFFSET, posY - Y_OFFSET);

        popup.show();
    }

    private boolean isLine() {
        return getModel().getChartType() == LINE;
    }

    private boolean isPie() {
        return getModel().getChartType() == PIE;
    }

    private void setHoverable(boolean hoverable) {
        plot.getOptions().getGridOptions().setHoverable(hoverable);

        if (hoverable) plot.addHoverListener(eventHandler, false);
    }

    private boolean isColumn() {
        return getModel().getChartType() == COLUMN;
    }

    private boolean isBar() {
        return getModel().getChartType() == BAR;
    }

    //~ Methods ......................................................................................................................................

    private static void setGlobalOptions(@NotNull final PlotOptions options, @NotNull final ChartType type, @NotNull final ChartConfig config) {
        final GlobalSeriesOptions global = GlobalSeriesOptions.create();

        if (type.is(BAR) || type.is(COLUMN)) {
            final Double           barWidth = config.getBarWidth();
            final BarSeriesOptions bars     = BarSeriesOptions.create()
                                              .setShow(true)
                                              .setBarWidth(barWidth)
                                              .setAlignment(BarSeriesOptions.BarAlignment.CENTER);
            if (type.is(BAR)) bars.setHorizontal(true);
            options.setGlobalSeriesOptions(global.setBarsSeriesOptions(bars));
        }
        else if (type.is(LINE)) {
            final LineSeriesOptions lines = LineSeriesOptions.create().setShow(true).setSteps(config.isLineSteps());
            options.setGlobalSeriesOptions(global.setLineSeriesOptions(lines));
        }
        else if (type.is(PIE)) {
            final PieSeriesOptions pies = PieSeriesOptions.create().setShow(true).setRadius(1).setInnerRadius(MAGIC_INNER_RADIUS);

            pies.setLabel(
                PieSeriesOptions.Label.create()
                                      .setShow(true)
                                      .setRadius(PIE_LABEL_RADIUS)
                                      .setBackground(PieSeriesOptions.Label.Background.create().setOpacity(PIE_LABEL_OPACITY))
                                      .setThreshold(PIE_LABEL_THRESHOLD)
                                      .setFormatter((label, series) -> {
                                          final double p = (double) round(series.getPercent() * 100) / 100;
                                          return "<div style=\"font-size:8pt;text-align:center;padding:2px;color:white;\">" + label + "<br/>" + p +
                                          "%</div>";
                                      }));
            options.setGlobalSeriesOptions(global.setPieSeriesOptions(pies));
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String AUTO_WIDTH = "auto-width-chart";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String CHART_STYLE = "chart";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String COLOR_KEY = "color";

    private static final String CHART_VERTICAL_LABEL = "chart-vertical-label";

    private static final int X_OFFSET = 25;
    private static final int Y_OFFSET = 25;

    private static final double PIE_LABEL_OPACITY   = 0.8;
    private static final double PIE_LABEL_RADIUS    = 3d / 4d;
    private static final double PIE_LABEL_THRESHOLD = 0.05;
    private static final double MAGIC_BAR_WIDTH     = 0.8;
    private static final double MAGIC_INNER_RADIUS  = 0.2;

    //~ Inner Interfaces .............................................................................................................................

    public interface ChartHandler {
        /** Clicked. */
        void onChartClicked(int row, int column);

        class Default implements ChartHandler {
            @Override public void onChartClicked(int row, int column) {
                Logger.getLogger(ChartUI.class).info("ChartHandler.onChartClicked on row: " + row + " column: " + column);
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    private class ChartEventHandler implements PlotClickListener, PlotHoverListener {
        @Override public void onPlotClick(final Plot p, final PlotPosition position, final PlotItem item) {
            final int column = isPie() ? mapper.getFirst() : mapper.getSeriesIndex(item.getSeriesIndex());
            final int row    = resolveRow(item, column);

            for (final ChartHandler handler : handlers) {
                popup.hide();
                handler.onChartClicked(row, column);
            }
        }

        @Override public void onPlotHover(final Plot p, final PlotPosition position, final PlotItem item) {
            if (item != null) {
                final String label;
                final Double value;

                if (isLine() || isColumn()) {
                    final int           xAxis = item.getSeries().getXAxis();
                    final JsArray<Tick> ticks = p.getAxes().getX(xAxis + 1).getJsObject(TICKS_KEY);
                    final int           x     = (int) item.getDataPoint().getX();
                    label = getLabel(ticks.get(isLine() ? x - 1 : x));  // Line charts handles differently the labels on the x axis.
                    value = item.getDataPoint().getY();
                }
                else if (isBar()) {
                    final int           yAxis = item.getSeries().getYAxis();
                    final JsArray<Tick> ticks = p.getAxes().getY(yAxis + 1).getJsObject(TICKS_KEY);
                    label = getLabel(ticks.get((int) item.getDataPoint().getY()));
                    value = item.getDataPoint().getX();
                }
                else {                                                  // if (isPie())
                    final Series series = item.getSeries();
                    label = series.getLabel();
                    value = series.getData().get(0).getY();
                }

                displayValueAt(position.getPageX(), position.getPageY(), value, label);
            }
            else popup.hide();
        }

        /**
         * There is a discrepancy on GFlot library with ticks arrrays on axes. Ideally, we could
         * just use the array objects using tick.getLabel() method. But we have to access it
         * natively as the previous one wasn't working.
         */
        protected native String getLabel(JavaScriptObject o)  /*-{ return o.label; }-*/;

        private int resolveRow(PlotItem item, int column) {
            if (isPie()) return item.getSeriesIndex();
            return mapper.getMappedRow(column, item.getDataIndex());
        }

        @NonNls private static final String TICKS_KEY = "ticks";
    }  // end class ChartEventHandler
}  // end class ChartUI
