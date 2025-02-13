
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.cart;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.sales.basic.InvoiceForm;

/**
 * Cart Form.
 */
@SuppressWarnings("WeakerAccess")
public class CartForm extends CartFormBase {

    //~ Methods ......................................................................................................................................

    /** Checkout. */
    @NotNull @Override public Action checkout() {
        final InvoiceForm invoice = forms.initialize(InvoiceForm.class);

        final FormTable<InvoiceForm.ItemsRow> items = invoice.getItems();

        for (final ItemsRow item : getItems()) {
            final InvoiceForm.ItemsRow row = items.add();
            row.setProduct(item.getProduct());
            row.setQuantity(item.getQuantity());
        }

        return actions.navigate(invoice);
    }

    @Override public void load() {
        final Cart cart = Cart.current(context.getUser());
        setPrimaryKey(cart.keyAsString());
        populate();
    }

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {
        /** Update unit price. */
        @NotNull @Override public Action updateUnitPrice() {
            updateRowUnitPrice();
            return actions.getDefault();
        }

        private void updateRowUnitPrice() {
            setUnit(getProduct().getPrice());
        }
    }
}
