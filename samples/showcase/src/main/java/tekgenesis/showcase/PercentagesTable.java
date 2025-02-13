
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

/**
 * User class for Form: PercentagesTable
 */
public class PercentagesTable extends PercentagesTableBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        for (int i = 1; i <= 10; i++) {
            final TableRow row = getTable().add();

            row.setLabel("row" + i);
            row.setTotal(new BigDecimal(10.23 * i));
            row.setDiscountTotal(new BigDecimal(10.5));
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {}
}
