
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.configuration.ChartConfiguration;

import static tekgenesis.showcase.DrillDownChartBase.Field.COLUMN;

/**
 * User class for Form: DrillDownChart
 */
public class DrillDownChart extends DrillDownChartBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action back() {
        return actions.getDefault();
    }

    /** Invoked when chart(column) is clicked. */
    @NotNull @Override public Action drilldown(@NotNull Field field) {
        return actions.navigate(DrillDownChart.class);
    }

    @Override public void load() {
        this.<ChartConfiguration>configuration(COLUMN).dimension(854, 480).showLabelsOnHover(true);
        loadData();
    }

    private void loadData() {
        final Random r = new Random();
        for (int i = 0; i < 10; i++) {
            final DrillDownChart.ColumnRow row = getColumn().add();
            row.setLabel("Label" + i);
            row.setSeries1(Math.abs(r.nextInt(5)));
            row.setSeries2(Math.abs(r.nextInt(5)));
            row.setKey("" + i);
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class ColumnRow extends ColumnRowBase {}
}  // end class DrillDownChart
