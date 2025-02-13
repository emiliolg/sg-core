
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

/**
 * Product images entity class.
 */
@SuppressWarnings("WeakerAccess")
public class ProductImages extends ProductImagesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Product populate() {
        final Product product = getProduct();
        setModel(product.getModel());
        setGallery(product.getImageResources());
        return product;
    }

    @NotNull @Override public Action update() {
        final Product product = getProduct();
        copyTo(product);
        product.persist();
        return actions.getDefault();
    }

    private void copyTo(@NotNull final Product product) {
        product.getImages().merge(getGallery(), Image::setImageId);
    }
}
