
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
import tekgenesis.sales.basic.Product;

/**
 * Add to Cart Form.
 */
@SuppressWarnings("WeakerAccess")
public class AddToCart extends AddToCartBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button($S2) is clicked. */
    @NotNull @Override public Action addClicked() {
        setApi(false);
        addToCart();
        return actions.getDefault();
    }

    /** Invoked when suggest_box(product) value changes. */
    @NotNull @Override public Action productChanged() {
        if (isApi()) addToCart();
        return actions.getDefault();
    }

    private void addToCart() {
        final Cart cart = Cart.current(context.getUser());

        final Product product = getProduct();

        int qty    = 0;
        int amount = 0;

        for (final CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                qty = item.getQuantity() + 1;
                item.setQuantity(qty);
            }
            amount += item.getQuantity();
        }

        if (qty == 0) {
            final CartItem item = cart.getItems().add();
            item.setProduct(product);
            item.setQuantity(1);
            amount++;
        }

        cart.persist();
        setAmount(amount);
        setCurrent(Cart.current(context.getUser()).getId());
    }
}  // end class AddToCart
