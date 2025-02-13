
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.persistence.Initialize;
import tekgenesis.sales.basic.g.ProductBase;

import static tekgenesis.persistence.EntityListenerType.AFTER_PERSIST;
import static tekgenesis.persistence.EntityListenerType.BEFORE_PERSIST;

/**
 * Product Entity class.
 */
@SuppressWarnings("WeakerAccess")
public class Product extends ProductBase {

    //~ Instance Fields ..............................................................................................................................

    private boolean created = false;

    //~ Methods ......................................................................................................................................

    /** Set categoryAtt. */
    public void setCategoryAtt() {
        final Option<ProductByCat> first = getSecondary().getFirst();
        if (first.isPresent()) setCategoryAttPersisted(first.get().getSecondaryCategory().getName());
    }

    /** Returns images as resources. */
    public Seq<Resource> getImageResources() {
        return getImages().map(Image::getImageId);
    }

    //~ Methods ......................................................................................................................................

    @Initialize private static void initialize() {
        addListener(BEFORE_PERSIST, product -> {
                product.created = find(product.getProductId()) == null;
                return true;
            });

        addListener(AFTER_PERSIST,
            product -> {
                if (product.created) {
                    final ProductData productData = ProductData.create(product);
                    productData.insert();
                }
                product.created = false;
                return true;
            });
    }
}
