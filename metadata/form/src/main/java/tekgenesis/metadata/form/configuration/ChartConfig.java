
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.configuration.AxisConfig.Axis;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.common.Predefined.equalElements;
import static tekgenesis.metadata.form.configuration.AxisConfig.Axis.X;
import static tekgenesis.metadata.form.configuration.SeriesMode.valueOf;

/**
 * Chart widget serializable configuration.
 */
@SuppressWarnings("UnusedReturnValue")
public class ChartConfig extends SizeConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private Double barWidth;

    @NotNull private List<String> colors;
    @NotNull private Boolean      hoverable;
    @NotNull private Boolean      labelsOnHover;
    @NotNull private Boolean      legend;
    private Boolean               lineSteps;
    private boolean               minsSet;
    @NotNull private Boolean      positiveQuadrant;

    @NotNull private SeriesMode seriesMode;
    @NotNull private Boolean    verticalLabels;

    @NotNull private List<AxisConfig>  xAxes;
    @NotNull private List<AxisConfig>  yAxes;

    //~ Constructors .................................................................................................................................

    /** Create default chart configuration. */
    public ChartConfig() {
        seriesMode       = SeriesMode.STACKED;
        hoverable        = true;
        legend           = true;
        labelsOnHover    = false;
        verticalLabels   = false;
        positiveQuadrant = false;
        lineSteps        = false;
        barWidth         = DEFAULT_BAR_WIDTH;

        minsSet = false;
        colors  = new ArrayList<>();

        xAxes = new ArrayList<>();
        yAxes = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Proper axis configuration. */
    public AxisConfig axis(@NotNull Axis a, int i) {
        final List<AxisConfig> resultAxis = a == X ? xAxes : yAxes;

        final AxisConfig result;
        if (i >= resultAxis.size()) {
            result = new AxisConfig();
            resultAxis.add(result);
        }
        else result = resultAxis.get(i);

        return result;
    }

    /** Sets bar chart bar width (between 0 and 1). */
    @NotNull public ChartConfig barWidth(double v) {
        barWidth = v;
        return this;
    }

    /** Sets this config's colors to be used by graph's series. */
    public ChartConfig colors(@NotNull final String[] cs) {
        colors = Arrays.asList(cs);
        return this;
    }

    @Override public void deserializeFields(StreamReader r) {
        super.deserializeFields(r);

        seriesMode       = valueOf(r.readString());
        hoverable        = r.readBoolean();
        legend           = r.readBoolean();
        labelsOnHover    = r.readBoolean();
        verticalLabels   = r.readBoolean();
        positiveQuadrant = r.readBoolean();
        minsSet          = r.readBoolean();
        lineSteps        = r.readBoolean();
        barWidth         = r.readDouble();
        colors           = r.readStrings();

        xAxes = deserializeAxis(r);
        yAxes = deserializeAxis(r);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartConfig)) return false;
        if (!super.equals(o)) return false;

        final ChartConfig that = (ChartConfig) o;

        return equalElements(that.colors, colors) && that.seriesMode == seriesMode && that.hoverable == hoverable &&
               that.labelsOnHover == labelsOnHover && that.legend == legend && that.verticalLabels == verticalLabels &&
               that.positiveQuadrant == positiveQuadrant && that.lineSteps == lineSteps && Objects.equals(that.barWidth, barWidth) &&
               equalElements(that.xAxes, xAxes) && equalElements(that.yAxes, yAxes);
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + colors.hashCode();
        result = 31 * result + seriesMode.hashCode();
        result = 31 * result + hoverable.hashCode();
        result = 31 * result + labelsOnHover.hashCode();
        result = 31 * result + legend.hashCode();
        result = 31 * result + verticalLabels.hashCode();
        result = 31 * result + positiveQuadrant.hashCode();
        result = 31 * result + lineSteps.hashCode();
        result = 31 * result + barWidth.hashCode();
        result = 31 * result + xAxes.hashCode();
        result = 31 * result + yAxes.hashCode();
        return result;
    }

    /** Returns true if this config has labels on hover. */
    @NotNull public Boolean hasLabelsOnHover() {
        return labelsOnHover;
    }

    /** Returns true if this config has legend. */
    @NotNull public Boolean hasLegend() {
        return legend;
    }

    /** Returns true if this config has vertical labels. */
    @NotNull public Boolean hasVerticalLabels() {
        return verticalLabels;
    }

    /** Sets this config as hoverable or not. */
    public ChartConfig hoverable(@NotNull Boolean h) {
        hoverable = h;
        return this;
    }

    /** Sets this config to have legend or not. */
    public ChartConfig legend(@NotNull Boolean l) {
        legend = l;
        return this;
    }

    public ChartConfig lineSteps(boolean v) {
        lineSteps = v;
        return this;
    }

    /** Sets this chart config to only display the positive quadrant. */
    public ChartConfig positiveQuadrant() {
        positiveQuadrant = true;
        return this;
    }

    @Override public void serializeFields(StreamWriter w) {
        super.serializeFields(w);

        w.writeString(seriesMode.name());
        w.writeBoolean(hoverable);
        w.writeBoolean(legend);
        w.writeBoolean(labelsOnHover);
        w.writeBoolean(verticalLabels);
        w.writeBoolean(positiveQuadrant);
        w.writeBoolean(minsSet);
        w.writeBoolean(lineSteps);
        w.writeDouble(barWidth);
        w.writeStrings(colors);

        serializeAxis(w, xAxes);
        serializeAxis(w, yAxes);
    }

    /** Sets this config with a seriesMode. */
    public ChartConfig seriesMode(@NotNull SeriesMode b) {
        seriesMode = b;
        return this;
    }

    /** Sets this config to show labels (plus value) on hover popup. */
    public ChartConfig showLabelsOnHover(@NotNull Boolean s) {
        labelsOnHover = s;
        return this;
    }

    /** Sets this config to vertical labels on X axis (Valid for Columns, Bars and Lines). */
    public ChartConfig verticalLabels(@NotNull Boolean v) {
        verticalLabels = v;
        return this;
    }

    /** Returns bar chart bar width. */
    @NotNull public Double getBarWidth() {
        return barWidth;
    }

    /** Returns this config's colors to be used by graph's series. */
    @NotNull public List<String> getColors() {
        return colors;
    }

    /** Returns true if this config is hoverable. */
    @NotNull public Boolean isHoverable() {
        return hoverable;
    }

    /** Returns true if line charts are with steps or not. */
    public Boolean isLineSteps() {
        return lineSteps;
    }

    /** Returns the seriesMode. */
    @NotNull public SeriesMode getSeriesMode() {
        return seriesMode;
    }

    /** Returns true if this chart is configured to only display the positive quadrant. */
    @NotNull public Boolean isPositiveQuadrant() {
        return positiveQuadrant;
    }

    /** Returns x axis config. */
    @NotNull public List<AxisConfig> getXAxis() {
        return xAxes;
    }

    /** Returns y axis config. */
    @NotNull public List<AxisConfig> getYAxis() {
        return yAxes;
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.CHART;
    }

    @NotNull private List<AxisConfig> deserializeAxis(StreamReader r) {
        final int              size   = r.readInt();
        final List<AxisConfig> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final AxisConfig axisConfig = new AxisConfig();
            axisConfig.deserializeFields(r);
            result.add(axisConfig);
        }
        return result;
    }

    private void serializeAxis(StreamWriter w, List<AxisConfig> axis) {
        w.writeInt(axis.size());
        for (final AxisConfig config : axis)
            config.serializeFields(w);
    }

    //~ Methods ......................................................................................................................................

    /** From SUN->SAT to MON->SUN. */
    public static int toGetDay(int day) {
        return day != 0 ? day - 1 : 6;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8085847541701428849L;

    public static final ChartConfig DEFAULT           = new ChartConfig();
    private static final double     DEFAULT_BAR_WIDTH = .8;
}  // end class ChartConfig
