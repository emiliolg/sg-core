
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

/**
 * Chart's Axis Configuration (Valid for Columns, Bars and Lines).
 */
public interface AxisConfiguration extends ChartConfiguration {

    //~ Methods ......................................................................................................................................

    /** Sets axis label. */
    @NotNull AxisConfiguration axisLabel(String l);

    /** Sets axis maximum value. */
    @NotNull AxisConfiguration axisMaximum(double v);

    /** Sets axis minimum value. */
    @NotNull AxisConfiguration axisMinimum(double v);

    /** Renders axis on the secondary axis right for Columns and Lines and top for Bars. */
    @NotNull AxisConfiguration axisToSecondary();

    /** Sets chart axis visible or not. */
    @NotNull AxisConfiguration axisVisible(boolean v);

    /** Resets maximums to be auto calculated (Valid for Columns, Bars and Lines). */
    @NotNull AxisConfiguration resetMaximums();

    /** Resets minimums to be auto calculated (Valid for Columns, Bars and Lines). */
    @NotNull AxisConfiguration resetMinimums();
}
