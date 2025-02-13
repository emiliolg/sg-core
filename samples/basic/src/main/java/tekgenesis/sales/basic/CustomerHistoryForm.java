
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

import tekgenesis.form.Action;
import tekgenesis.form.MappingCallback;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * User class for Form: CustomerHistoryForm
 */
public class CustomerHistoryForm extends CustomerHistoryFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(edit) on subform(invoices) is clicked. */
    @NotNull @Override public Action editInvoice() {
        final CustomerInvoicesForm.InvoicesRow invoice = ensureNotNull(getInvoices()).getInvoices().getCurrent();
        return actions.navigate(InvoiceForm.class, invoice.keyAsString()).callback(InvoiceFromHistoryMappingCallback.class).withMessage("Voila!");
    }

    @NotNull @Override public Customer populate() {
        final Customer customer = super.populate();
        createInvoices(customer.keyAsString());
        return customer;
    }

    //~ Inner Classes ................................................................................................................................

    public static class InvoiceFromHistoryMappingCallback implements MappingCallback<InvoiceForm, CustomerHistoryForm> {
        @Override public void onSave(@NotNull InvoiceForm base, @NotNull CustomerHistoryForm out) {
            ensureNotNull(out.getInvoices()).getInvoices().getCurrent().populate(base.find());
        }
    }
}
