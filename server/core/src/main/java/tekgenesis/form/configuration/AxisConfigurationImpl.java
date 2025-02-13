
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
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

/**
 * Axis configuration implementation.
 */
public class AxisConfigurationImpl extends ChartConfigurationImpl implements AxisConfiguration {

    //~ Instance Fields ..............................................................................................................................

    private AxisConfig.Axis axis  = null;
    private int             index;

    private final ChartConfigurationImpl parent;

    //~ Constructors .................................................................................................................................

    AxisConfigurationImpl(@NotNull Model model, @NotNull Widget widget, ChartConfigurationImpl parent) {
        super(model, widget);
        this.parent = parent;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public AxisConfiguration axisLabel(String l) {
        getAxis().axisLabel(l);
        return this;
    }

    @NotNull @Override public AxisConfiguration axisMaximum(double v) {
        getAxis().axisMaximum(v);
        return this;
    }

    @NotNull @Override public AxisConfiguration axisMinimum(double v) {
        getAxis().axisMinimum(v);
        return this;
    }

    @NotNull @Override public AxisConfiguration axisToSecondary() {
        getAxis().axisToSecondary();
        return this;
    }

    @NotNull @Override public AxisConfiguration axisVisible(boolean v) {
        getAxis().axisVisible(v);
        return this;
    }

    @NotNull @Override public AxisConfiguration resetMaximums() {
        getAxis().resetMaximums();
        return this;
    }

    @NotNull @Override public AxisConfiguration resetMinimums() {
        getAxis().resetMinimums();
        return this;
    }

    void setAxis(AxisConfig.Axis axis) {
        this.axis = axis;
    }

    @NotNull @Override ChartConfig getConfig() {
        return parent.getConfig();
    }

    void setIndex(int index) {
        this.index = index;
    }

    private AxisConfig getAxis() {
        return getConfig().axis(axis, index);
    }
}  // end class AxisConfigurationImpl
