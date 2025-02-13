
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
import java.util.Random;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.configuration.ChartConfiguration;

import static tekgenesis.metadata.form.configuration.SeriesMode.valueOf;
import static tekgenesis.showcase.ChartDataShowcaseBase.Field.BIG_DECIMAL;
import static tekgenesis.showcase.ChartDataShowcaseBase.Field.COLUMN;
import static tekgenesis.showcase.ChartDataShowcaseBase.Field.INTEGER;
import static tekgenesis.showcase.ChartDataShowcaseBase.Field.REAL;

/**
 * User class for Form: ChartDataShowcase
 */
@Generated(value = "tekgenesis/showcase/ChartShowcase.mm", date = "1369770201025")
public class ChartDataShowcase extends ChartDataShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action blue() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.BLUE_COLORS);
        return actions.getDefault();
    }

    @NotNull @Override public Action changeWidth() {
        this.<ChartConfiguration>configuration(COLUMN).barWidth(getBarWidth());
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void data() {
        final Random r = new Random();

        for (int i = 0; i < 12; i++) {
            final ColumnRow row = getColumn().add();
            row.setLabel("Label" + i);

            row.setInteger(i);
            row.setReal(r.nextDouble());
            row.setBigDecimal(new BigDecimal(r.nextDouble()));
            row.setKey("key" + i);
        }

        this.<ChartConfiguration>configuration(COLUMN).mainXAxis().axisLabel("Another label").mainYAxis().axisLabel("Some label");
    }

    @NotNull @Override public Action defaultColors() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.DEFAULT_COLORS);
        return actions.getDefault();
    }

    /** Invoked when chart(column) is clicked. */
    @NotNull @Override public Action doClick(@NotNull Field field) {
        final ColumnRow current = getColumn().getCurrent();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("label: ").append(current.getLabel());
        strBuilder.append(" integer: ").append(current.getInteger());
        strBuilder.append(" real: ").append(current.getReal());
        strBuilder.append(" bigDecimal: ").append(current.getBigDecimal());
        strBuilder.append(" with key: ").append(current.getKey());
        if (field.equals(INTEGER)) strBuilder.append(" on column: ").append(INTEGER);
        if (field.equals(REAL)) strBuilder.append(" on column: ").append(REAL);
        if (field.equals(BIG_DECIMAL)) strBuilder.append(" on column: ").append(BIG_DECIMAL);

        setCurrent(strBuilder.toString());

        return actions.getDefault();
    }

    @NotNull @Override public Action green() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.GREEN_COLORS);
        return actions.getDefault();
    }

    @NotNull @Override public Action hoverable() {
        this.<ChartConfiguration>configuration(COLUMN).hoverable(isHoverable());
        return actions.getDefault();
    }

    @NotNull @Override public Action legend() {
        this.<ChartConfiguration>configuration(COLUMN).legend(isLegend());
        return actions.getDefault();
    }

    @NotNull @Override public Action palette1() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.PALETTE1);
        return actions.getDefault();
    }

    @NotNull @Override public Action palette2() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.PALETTE2);
        return actions.getDefault();
    }

    @NotNull @Override public Action palette3() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.PALETTE3);
        return actions.getDefault();
    }

    @NotNull @Override public Action red() {
        this.<ChartConfiguration>configuration(COLUMN).colors(ChartConfiguration.RED_COLORS);
        return actions.getDefault();
    }

    @NotNull @Override public Action reloadData() {
        getColumn().clear();
        data();
        return actions.getDefault();
    }

    @NotNull @Override public Action showLabels() {
        this.<ChartConfiguration>configuration(COLUMN).showLabelsOnHover(isShowLabels());
        return actions.getDefault();
    }

    @NotNull @Override public Action size1280() {
        this.<ChartConfiguration>configuration(COLUMN).dimension(1280, 720);
        return actions.getDefault();
    }

    @NotNull @Override public Action size320() {
        this.<ChartConfiguration>configuration(COLUMN).dimension(320, 240);
        return actions.getDefault();
    }

    @NotNull @Override public Action size640() {
        this.<ChartConfiguration>configuration(COLUMN).dimension(640, 480);
        return actions.getDefault();
    }

    @NotNull @Override public Action size854() {
        this.<ChartConfiguration>configuration(COLUMN).dimension(854, 480);
        return actions.getDefault();
    }

    @NotNull @Override public Action stack() {
        this.<ChartConfiguration>configuration(COLUMN).seriesMode(valueOf(getSeriesMode().name()));
        return actions.getDefault();
    }

    @NotNull @Override public Action toStepped() {
        this.<ChartConfiguration>configuration(COLUMN).lineSteps(isStepped());
        return actions().getDefault();
    }

    @NotNull @Override public Action toVerticalLabels() {
        this.<ChartConfiguration>configuration(COLUMN).withVerticalLabels(isVerticalLabels());
        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class ColumnRow extends ColumnRowBase {}
}  // end class ChartDataShowcase
