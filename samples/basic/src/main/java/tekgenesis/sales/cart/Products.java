
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

import tekgenesis.common.core.Resource;
import tekgenesis.form.Action;
import tekgenesis.sales.basic.Image;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.State;

/**
 * Products Form class.
 */
@SuppressWarnings("WeakerAccess")
public class Products extends ProductsBase {

    //~ Methods ......................................................................................................................................

    public void load() {
        Product.forEach(product -> {
            final ProductsRow row = getProducts().add();
            row.populate(product);
            row.setActive(product.getState() == State.ACTIVE);
            for (final Image image : product.getImages().getFirst()) {
                final Resource imageId = image.getImageId();
                row.setImage(imageId);
            }
        });
    }

    //~ Inner Classes ................................................................................................................................

    public class ProductsRow extends ProductsRowBase {
        @NotNull @Override public Action goToProduct() {
            final ProductDetail form = forms.initialize(ProductDetail.class);
            form.setProductId(getProductId());
            return actions.navigate(form);
        }
    }
}
