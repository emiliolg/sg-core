
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
import tekgenesis.form.FormInstance;
import tekgenesis.form.FormTable;

/**
 * Invoice Review Form class.
 */
@SuppressWarnings("WeakerAccess")
public class InvoiceReviewForm extends InvoiceReviewFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<InvoicesRow> invoicesTable = getInvoices();
        Invoice.forEach(invoice -> {
            final InvoicesRow invoicesRow = invoicesTable.add();
            invoicesRow.setId(invoice.getIdKey());
            invoicesRow.setDate(invoice.getInvoiceDate());
            loadCustomer(invoicesRow.createCustomer(), invoice.getCustomer());
            loadItems(invoicesRow.createItems(), invoice.getItems());
            loadPayments(invoicesRow.createPayments(), invoice.getPayments());
        });
    }

    private void loadCustomer(CustomerReviewForm form, Customer customer) {
        form.setDocumentType(customer.getDocumentType().toString());
        form.setDocumentId(customer.getDocumentId());
        form.setSex(customer.getSex().toString());
        form.populate();
    }

    private void loadItems(ItemsForm form, Iterable<Item> items) {
        final FormTable<ItemsForm.ItemsRow> itemTable = form.getItems();
        for (final Item item : items) {
            final ItemsForm.ItemsRow row = itemTable.add();
            row.setProduct(item.getProduct().getModel());
            row.setUnit(item.getProduct().getPrice());
            row.setQuantity(item.getQuantity());
            form.setCount(form.getCount() + 1);
        }
    }

    private void loadPayments(PaymentsForm form, Iterable<Payment> payments) {
        final FormTable<PaymentsForm.PaymentsRow> paymentsTable = form.getPayments();
        for (final Payment payment : payments) {
            final PaymentsForm.PaymentsRow row = paymentsTable.add();
            row.setType(payment.getPayment().getType().toString());
            row.setPayment(payment.getPayment().getSubtype());
            row.setAmount(payment.getAmount());
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class InvoicesRow extends InvoicesRowBase {
        @NotNull @Override public Action navigate() {
            final Class<? extends FormInstance<?>> formClass = getId() == 1 ? InvoiceForm.class : CustomerForm.class;
            return actions.navigate(formClass, getId() + "").withMessage("Navigating to Invoice...");
        }
    }
}  // end class InvoiceReviewForm
