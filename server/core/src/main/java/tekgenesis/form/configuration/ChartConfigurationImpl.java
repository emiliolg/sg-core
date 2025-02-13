
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.configuration.AxisConfig.Axis;
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.configuration.SeriesMode;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.metadata.form.configuration.AxisConfig.Axis.X;
import static tekgenesis.metadata.form.configuration.AxisConfig.Axis.Y;

class ChartConfigurationImpl extends AbstractWidgetConfiguration<ChartConfig> implements ChartConfiguration {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<AxisConfiguration>  xAxis;
    @NotNull private final List<AxisConfiguration>  yAxis;

    //~ Constructors .................................................................................................................................

    ChartConfigurationImpl(@NotNull final Model model, @NotNull final Widget widget) {
        super(model, widget);
        xAxis = new ArrayList<>();
        yAxis = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public AxisConfiguration axis(Axis a, int i) {
        if (i < 1) throw new IllegalStateException("Axis are 1-based indexed!");

        // Axis are 1-based to the outside of the API, and 0-based on the inside.
        final int fixIndex = i - 1;

        final List<AxisConfiguration> resultAxis = a == X ? xAxis : yAxis;

        final AxisConfiguration result;

        if (fixIndex >= resultAxis.size()) {
            result = new AxisConfigurationImpl(model, widget, this);
            final AxisConfigurationImpl impl = (AxisConfigurationImpl) result;
            impl.setAxis(a);
            impl.setIndex(fixIndex);
            resultAxis.add(result);
        }
        else result = resultAxis.get(fixIndex);

        return result;
    }

    @NotNull @Override public ChartConfiguration barWidth(double v) {
        getConfig().barWidth(v);
        return this;
    }

    @NotNull @Override public ChartConfiguration colors(String... colors) {
        getConfig().colors(colors);
        return this;
    }

    @NotNull @Override public ChartConfiguration dimension(int w, int h) {
        getConfig().dimension(w, h);
        return this;
    }

    @NotNull @Override public ChartConfiguration height(int h) {
        getConfig().height(h);
        return this;
    }

    @NotNull @Override public ChartConfiguration hoverable(boolean h) {
        getConfig().hoverable(h);
        return this;
    }

    @NotNull @Override public ChartConfiguration legend(boolean l) {
        getConfig().legend(l);
        return this;
    }

    @NotNull @Override public ChartConfiguration lineSteps(boolean v) {
        getConfig().lineSteps(v);
        return this;
    }

    @NotNull @Override public AxisConfiguration mainXAxis() {
        return axis(X, 1);
    }

    @NotNull @Override public AxisConfiguration mainYAxis() {
        return axis(Y, 1);
    }

    @NotNull @Override public ChartConfiguration positiveQuadrant() {
        getConfig().positiveQuadrant();
        return this;
    }

    @NotNull @Override public ChartConfiguration seriesMode(SeriesMode m) {
        getConfig().seriesMode(m);
        return this;
    }

    @NotNull @Override public ChartConfiguration showLabelsOnHover(boolean s) {
        getConfig().showLabelsOnHover(s);
        return this;
    }

    @NotNull @Override public ChartConfiguration width(int w) {
        getConfig().width(w);
        return this;
    }

    @NotNull @Override public ChartConfiguration withVerticalLabels(boolean v) {
        getConfig().verticalLabels(v);
        return this;
    }

    @NotNull @Override ChartConfig createConfig() {
        return new ChartConfig();
    }

    @NotNull ChartConfig getConfig() {
        return config();
    }
}  // end class ChartConfigurationImpl
