
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.math.BigDecimal;

import tekgenesis.form.configuration.ChartConfiguration;

import static tekgenesis.form.configuration.ChartConfiguration.PALETTE3;
import static tekgenesis.metadata.form.configuration.AxisConfig.Axis.Y;
import static tekgenesis.metadata.form.configuration.SeriesMode.SIDE_BY_SIDE;
import static tekgenesis.showcase.ChartDualAxisBase.Field.CHART;

/**
 * User class for Form: ChartDualAxis
 */
public class ChartDualAxis extends ChartDualAxisBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override
    @SuppressWarnings("MagicNumber")
    public void data() {
        this.<ChartConfiguration>configuration(CHART)
            .colors(PALETTE3)
            .showLabelsOnHover(true)
            .seriesMode(SIDE_BY_SIDE)
            .dimension(854, 480)
            .mainYAxis()
            .axisLabel("Cantidad")
            .axis(Y, 2)
            .axisLabel("%")
            .axisToSecondary();

        getChart().add().fill("Boca", 429, new BigDecimal(39));
        getChart().add().fill("River", 286, new BigDecimal(26));
        getChart().add().fill("Independiente", 110, new BigDecimal(10));
        getChart().add().fill("Racing", 55, new BigDecimal(5));
        getChart().add().fill("San Lorenzo", 33, new BigDecimal(3));
        getChart().add().fill("Otro", 99, new BigDecimal(9));
        getChart().add().fill("Ninguno", 88, new BigDecimal(8));
    }

    //~ Inner Classes ................................................................................................................................

    public class ChartRow extends ChartRowBase {
        void fill(String team, Integer value, BigDecimal percentage) {
            setLabel(team);
            setValue(value);
            setPercentage(percentage);
        }
    }
}
