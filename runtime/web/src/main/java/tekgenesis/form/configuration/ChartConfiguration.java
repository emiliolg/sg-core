
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.configuration.AxisConfig;
import tekgenesis.metadata.form.configuration.SeriesMode;

/**
 * Configuration for Chart widgets.
 */
@SuppressWarnings("UnusedReturnValue")
public interface ChartConfiguration extends SizeConfiguration<ChartConfiguration> {

    //~ Instance Fields ..............................................................................................................................

    String[] BLUE_COLORS = { "#0B5467", "#067D9D", "#09A5BB", "#51C1CC", "#CAE2E2" };
    /** Default colors. */
    String[] DEFAULT_COLORS = { "#edc240", "#afd8f8", "#cb4b4b", "#4da74d", "#9440ed" };
    String[] GREEN_COLORS   = { "#283A10", "#4D642D", "#8EA86C", "#C3D6AA", "#EAF7D9" };
    String[] PALETTE1       = { "#FA6900", "#F38630", "#E0E4CC", "#A7DBD8", "#69D2E7" };
    String[] PALETTE2       = { "#53777A", "#542437", "#C02942", "#D95B43", "#ECD078" };
    String[] PALETTE3       = { "#556270", "#4ECDC4", "#C7F464", "#FF6B6B", "#C44D58" };
    String[] RED_COLORS     = { "#A31A20", "#CE1641", "#D62831", "#F20C2E", "#EA5869" };

    //~ Methods ......................................................................................................................................

    /** Configure the given axis. */
    @NotNull AxisConfiguration axis(AxisConfig.Axis a, int i);

    /** Sets bar chart bar width (between 0 and 1). */
    @NotNull ChartConfiguration barWidth(double v);

    /**
     * Sets the colors to be used by graph's series. Colors can be specified in the same way as CSS
     * colors.
     *
     * <p>For example, to set something to red all of this options are valid:</p>
     *
     * <p>colors("red", "blue"); colors("#FF0000"); colors("rgb(255,0,0)");</p>
     *
     * <p>Colors will be used sequentially if they are less than the series number.</p>
     */
    @NotNull ChartConfiguration colors(String... colors);

    /** Set chart to hoverable. */
    @NotNull ChartConfiguration hoverable(boolean h);

    /** Set chart with legend. */
    @NotNull ChartConfiguration legend(boolean l);

    /** Set line charts with steps or not (default false). */
    @NotNull ChartConfiguration lineSteps(boolean s);

    /** Configure the main X axis (shorthand for .axis(X, 0)). */
    @NotNull AxisConfiguration mainXAxis();

    /** Configure the main X axis (shorthand for .axis(Y, 0)). */
    @NotNull AxisConfiguration mainYAxis();

    /**
     * Sets chart to display only the positive quadrant (I) (Valid for Columns, Bars and Lines) on
     * ALL axis. This locks all axis to be positive.
     */
    @NotNull ChartConfiguration positiveQuadrant();

    /** Set chart series mode (applicable on bar or column). */
    @NotNull ChartConfiguration seriesMode(SeriesMode m);

    /** Set chart with labels (and value) on hover popup. */
    @NotNull ChartConfiguration showLabelsOnHover(boolean s);

    /** Sets chart to have vertical labels on X axis (Valid for Columns, Bars and Lines). */
    @NotNull ChartConfiguration withVerticalLabels(boolean v);
}  // end interface ChartConfiguration
