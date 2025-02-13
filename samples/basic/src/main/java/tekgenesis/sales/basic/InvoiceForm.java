
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.MappingCallback;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.size;
import static tekgenesis.sales.basic.InvoiceFormBase.Field.*;
import static tekgenesis.sales.basic.TwitterApi.isFollowing;
import static tekgenesis.sales.basic.g.PaymentTypeBase.listByType;

/**
 * Invoice Form class.
 */
@SuppressWarnings("WeakerAccess")
public class InvoiceForm extends InvoiceFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action createCustomer(String text) {
        final CustomerForm categoryForm = forms.initialize(CustomerForm.class);

        final Option<BigDecimal> number = parseNumber(text);
        if (number.isPresent()) {
            categoryForm.setDocumentType(DocType.DNI);
            categoryForm.setDocumentId(number.get());
        }
        else categoryForm.setFirstName(text);

        return actions.navigate(categoryForm).callback(CUSTOMER).withMessage("Create customer for Invoice");
    }

    @NotNull @Override public Invoice populate() {
        final Invoice invoice = find();

        setInvoiceDate(invoice.getInvoiceDate());
        setCustomer(invoice.getCustomer());

        final FormTable<ItemsRow> itemsRows = getItems();
        for (final Item item : invoice.getItems()) {
            final ItemsRow row = itemsRows.add();
            updateRowUnitPrice(row);
            row.populate(item);
        }

        final FormTable<PaymentsRow> paymentsRows = getPayments();
        for (final Payment payment : invoice.getPayments()) {
            final PaymentsRow row = paymentsRows.add();
            updateRowPayments(row, payment);
        }
        return invoice;
    }

    @NotNull @Override
    @SuppressWarnings("MagicNumber")
    public Action updateCustomer() {
        if (isDefined(CUSTOMER)) {
            final Customer    customer    = getCustomer();
            final Preferences preferences = Preferences.findByCustomer(customer.getDocumentType(), customer.getDocumentId(), customer.getSex());

            if (preferences != null && isNotEmpty(preferences.getTwitter())) {
                if (isFollowing(preferences.getTwitter(), "@garbarino")) {
                    setDisc(20);
                    message(CUSTOMER, customer.getFirstName() + " is following @garbarino!").info();
                }
                else {
                    setDisc(10);
                    message(CUSTOMER, customer.getFirstName() + " is not following @garbarino").warning();
                }
            }
            else setDisc(10);
        }

        return actions.getDefault();
    }

    private Option<BigDecimal> parseNumber(String text) {
        Option<BigDecimal> result;
        try {
            result = Option.some(new BigDecimal(NumberFormat.getInstance().parse(text).intValue()));
        }
        catch (final ParseException e) {
            result = Option.empty();
        }
        return result;
    }

    private void updateRowPayments(final PaymentsRow row, Payment payment) {
        final PaymentType paymentType = payment.getPayment();
        row.setType(paymentType.getType());
        setPaymentTypeOptions(row, paymentType.getType());
        row.populate(payment);
    }
    private void updateRowUnitPrice(final ItemsRow row) {
        if (row.isDefined(PRODUCT)) row.setUnit(row.getProduct().getPrice());
        else row.reset(UNIT);
    }

    private void setPaymentTypeOptions(PaymentsRow row, PaymentOption option) {
        row.setPaymentOptions(listByType(option).toList());
    }

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {
        @NotNull @Override public Action createProduct(@Nullable String text) {
            final ProductForm form = forms.initialize(ProductForm.class);
            form.setProductId(notNull(text, ""));
            return actions.navigate(form).callback(OutFromProduct.class);
        }
        @NotNull @Override public Action updateUnitPrice() {
            updateRowUnitPrice(this);
            return actions.getDefault();
        }
    }

    public static class OutFromProduct implements MappingCallback<ProductForm, InvoiceForm> {
        @Override public void onSave(@NotNull ProductForm productForm, @NotNull InvoiceForm invoiceForm) {
            final FormTable<ItemsRow> items = invoiceForm.getItems();
            for (final ItemsRow current : items.current())
                current.setProduct(productForm.find());
        }
    }

    public class PaymentsRow extends PaymentsRowBase {
        @NotNull @Override public Action updatePayments() {
            if (isDefined(TYPE)) {
                final PaymentOption option = getType();
                setPaymentTypeOptions(this, option);

                if (size(getPayments()) == 1) {
                    final BigDecimal subtotal = getSubtotal();
                    if (subtotal != null) setAmount(subtotal);
                }
            }

            return actions.getDefault();
        }
    }
}  // end class InvoiceForm
