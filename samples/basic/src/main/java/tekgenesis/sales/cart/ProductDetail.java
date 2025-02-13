
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

import tekgenesis.sales.basic.Product;

/**
 * Product Details Form class.
 */
@SuppressWarnings("WeakerAccess")
public class ProductDetail extends ProductDetailBase {

    //~ Methods ......................................................................................................................................

    /** Overriding populate to load images. */
    @NotNull @Override public Product populate() {
        final Product product = super.populate();
        setImages(product.getImageResources());
        return product;
    }
}
