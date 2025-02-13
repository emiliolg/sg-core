
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
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.ChartConfiguration;

import static tekgenesis.showcase.ClickChartShowcaseBase.Field.COLUMN;

/**
 * User class for Form: ClickChartShowcase
 */
@Generated(value = "tekgenesis/showcase/ChartShowcase.mm", date = "1363889333961")
public class ClickChartShowcase extends ClickChartShowcaseBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void data() {
        loadData();

        this.<ChartConfiguration>configuration(COLUMN).dimension(854, 480);
    }

    @NotNull @Override public Action doClick(@NotNull Field field) {
        final ColumnRow current = getColumn().getCurrent();
        String          sex     = "";
        if (field == Field.MEN) sex = "Men";
        else sex = "Women";

        setCurrentMonth("El mes corriente es: " + current.getMonth() + " " + sex + " con key: " + current.getKey());
        return actions.getDefault();
    }

    @NotNull @Override public Action lineClick(@NotNull Field field) {
        setCurrentMonth("Current Index: " + getLine().getCurrentIndex());
        return actions.getDefault();
    }

    @NotNull @Override public Action reload() {
        getColumn().clear();
        load();

        return actions.getDefault();
    }

    private void loadData() {
        final FormTable<ColumnRow> column = getColumn();
        final Random               r      = new Random();
        final FormTable<LineRow>   line   = getLine();

        for (int i = 0; i < 12; i++) {
            final ColumnRow row = column.add();
            row.setMonth(months[i]);
            row.setMen(Math.abs(r.nextInt(5)));
            row.setWomen(Math.abs(r.nextInt(5)));
            row.setKey("key" + i);
        }

        for (int i = 0; i < 12; i++) {
            final LineRow row = line.add();
            row.setChartLabel("label" + i);
            if (Math.random() < 0.5) row.setSerie1(BigDecimal.valueOf(r.nextInt(100)));
            if (Math.random() < 0.7) row.setSerie2(BigDecimal.valueOf(r.nextInt(100)));
            row.setSerie3(BigDecimal.valueOf(r.nextInt(100)));
        }
    }

    //~ Static Fields ................................................................................................................................

    private static String[] months = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    //~ Inner Classes ................................................................................................................................

    public class ColumnRow extends ColumnRowBase {}

    public class LineRow extends LineRowBase {}
}  // end class ClickChartShowcase
