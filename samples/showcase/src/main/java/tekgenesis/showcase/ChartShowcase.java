
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

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.ChartConfiguration;

import static tekgenesis.metadata.form.configuration.SeriesMode.OVERLAPPED;
import static tekgenesis.metadata.form.configuration.SeriesMode.STACKED;
import static tekgenesis.showcase.ChartShowcaseBase.Field.BAR;
import static tekgenesis.showcase.ChartShowcaseBase.Field.COLUMN;
import static tekgenesis.showcase.ChartShowcaseBase.Field.LINE;
import static tekgenesis.showcase.ChartShowcaseBase.Field.PIE;

/**
 * User class for Form: ChartShowcase
 */
@Generated(value = "tekgenesis/showcase/ChartShowcase.mm", date = "2013-01-28T19:53:20Z")
public class ChartShowcase extends ChartShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void data() {
        generate(5);
        this.<ChartConfiguration>configuration(LINE).dimension(0, 480);
        this.<ChartConfiguration>configuration(COLUMN).dimension(0, 480).hoverable(false).legend(false);
        this.<ChartConfiguration>configuration(BAR).dimension(0, 480);
        this.<ChartConfiguration>configuration(PIE).dimension(0, 480);
    }

    @NotNull @Override public Action more() {
        generate(5);
        return actions.getDefault();
    }

    @NotNull @Override public Action nextRnds() {
        final Random r = new Random();

        for (PieRow row : getPie())
            row.setSuperman(r.nextInt(8));

        return actions.getDefault();
    }

    @NotNull @Override public Action size1280() {
        setMapsDimensions(1280, 720);
        return actions.getDefault();
    }

    @NotNull @Override public Action size640() {
        setMapsDimensions(640, 480);
        return actions.getDefault();
    }

    @NotNull @Override public Action size854() {
        setMapsDimensions(854, 480);
        return actions.getDefault();
    }

    @NotNull @Override public Action sizeAuto() {
        setMapsDimensions(0, 480);
        return actions.getDefault();
    }

    @NotNull @Override public Action stack() {
        this.<ChartConfiguration>configuration(LINE).seriesMode(isStack() ? STACKED : OVERLAPPED);
        this.<ChartConfiguration>configuration(COLUMN).seriesMode(isStack() ? STACKED : OVERLAPPED);
        return actions.getDefault();
    }

    private void generate(int values) {
        final Random               r      = new Random();
        final FormTable<LineRow>   line   = getLine();
        final FormTable<BarRow>    bar    = getBar();
        final FormTable<ColumnRow> column = getColumn();
        final FormTable<PieRow>    pie    = getPie();

        for (int i = 0; i < values; i++) {
            line.add().random(r, line.size());
            bar.add().random(r);
            column.add().random(r, column.size());
            pie.add().random(r, pie.size());
        }
    }

    private void setMapsDimensions(int w, int h) {
        final ChartConfiguration lineConfig = configuration(LINE);
        lineConfig.dimension(w, h);

        final ChartConfiguration columnConfig = configuration(COLUMN);
        columnConfig.dimension(w, h);

        final ChartConfiguration barConfig = configuration(BAR);
        barConfig.dimension(w, h);

        final ChartConfiguration pieConfig = configuration(PIE);
        pieConfig.dimension(w, h);
    }

    //~ Inner Classes ................................................................................................................................

    public class BarRow extends BarRowBase {
        public void random(Random r) {
            setTom(r.nextDouble());
            setJerry(r.nextDouble());
        }
    }

    public class ColumnRow extends ColumnRowBase {
        public void random(Random r, int size) {
            setB("Label " + size);
            setAbel(r.nextDouble());
        }
    }

    public class LineRow extends LineRowBase {
        public void random(Random r, int size) {
            setA("Label " + size);
            setBeavis(r.nextDouble());
        }
    }

    public class PieRow extends PieRowBase {
        public void random(Random r, int size) {
            setSuperman(r.nextInt(8));
        }
    }
}  // end class ChartShowcase
