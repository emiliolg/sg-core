
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static tekgenesis.common.media.Mime.APPLICATION_PDF;

/**
 * User class for Form: ExportTable
 */
public class ExportTable extends ExportTableBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action inline() {
        final Action result = actions.getDefault();
        result.withDownload(OrdersPdfDump.class).withFileName("exported-orders.pdf").withContentType(APPLICATION_PDF).inline();
        return result;
    }

    @Override public void load() {
        final FormTable<OrdersRow> table = getOrders();

        for (int i = 0; i < 10; i++) {
            final OrdersRow row = table.add();
            row.setStrCol("str" + i);
            row.setIntCol(i);
            row.setDateCol(DateOnly.current().addDays(i));
            row.setDateTimeCol(DateTime.current().addHours(i));
        }
    }

    @NotNull @Override public Action print() {
        final Action result = actions.getDefault();
        result.withDownload(OrdersPdfDump.class).withContentType(APPLICATION_PDF).print();
        return result;
    }

    //~ Inner Classes ................................................................................................................................

    public class OrdersRow extends OrdersRowBase {
        @NotNull @Override public Action clicked() {
            System.out.println("getOrders().getCurrentIndex() = " + getOrders().getCurrentIndex());
            System.out.println("getOrders().getPreviousIndex() = " + getOrders().getPreviousIndex());
            return actions.getDefault();
        }
    }
}
