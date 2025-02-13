
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.configuration.ChartConfiguration;

/**
 * User class for Form: ToggleCharts
 */
public class ToggleCharts extends ToggleChartsBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        this.<ChartConfiguration>configuration(Field.SALES_BY_STORE_CHART).dimension(0, 480);
        this.<ChartConfiguration>configuration(Field.SALES_BY_CATEGORY_CHART).dimension(0, 480);

        search();
    }

    @NotNull @Override public Action search() {
        getSalesByCategoryChart().clear();
        final Random c = new Random();
        for (int i = 0; i < 10; i++)
            getSalesByCategoryChart().add().populate(c);

        getSalesByStoreChart().clear();
        final Random s = new Random();
        for (int i = 0; i < 10; i++)
            getSalesByStoreChart().add().populate(s);

        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final String[] categories = { "CategoryA", "CategoryB", "CategoryC" };
    private static final String[] stores     = { "StoreA", "StoreB", "StoreC" };

    //~ Inner Classes ................................................................................................................................

    public class SalesByCategoryChartRow extends SalesByCategoryChartRowBase {
        public void populate(Random r) {
            final String category = categories[r.nextInt(categories.length)];
            setCategoryKey(category);
            setCategoryLabel(category);
            setSalesByCategoryAmount(new BigDecimal(r.nextDouble()));
        }
    }

    public class SalesByStoreChartRow extends SalesByStoreChartRowBase {
        public void populate(Random r) {
            final String store = stores[r.nextInt(stores.length)];
            setStoreKey(store);
            setStoreLabel(store);
            setSalesByStoreAmount(new BigDecimal(r.nextDouble()));
        }
    }
}  // end class ToggleCharts
