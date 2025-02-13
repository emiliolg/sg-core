
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.FormTable;

/**
 * User class for Form: CustomerInvoicesForm
 */
public class CustomerInvoicesForm extends CustomerInvoicesFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Customer populate() {
        final Customer               customer = super.populate();
        final FormTable<InvoicesRow> table    = getInvoices();
        Invoice.list().forEach(i -> table.add().populate(i));
        return customer;
    }

    //~ Inner Classes ................................................................................................................................

    public class InvoicesRow extends InvoicesRowBase {
        @Override public void populate(@NotNull Invoice invoice) {
            super.populate(invoice);
            setItems(invoice.getItems().size());
        }

        @Override public void populateProtectedFields(@NotNull Invoice invoice) {
            setIdKey(invoice.getIdKey());
        }
    }
}
