
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;

/**
 * User class for Form: ProductGallery
 */
public class ProductImgShowcase extends ProductImgShowcaseBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        final Product product = selectFrom(PRODUCT).get();

        if (product != null) setImgShowcase(product.getImageResources());
    }
}
