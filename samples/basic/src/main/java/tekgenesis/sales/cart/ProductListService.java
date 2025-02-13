
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.cart;

import java.math.BigDecimal;

import tekgenesis.form.FormTable;

/**
 * User class for Form: ProductListService
 */
public class ProductListService extends ProductListServiceBase {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection" })
    public void onLoad() {
        setTitle("Televisores");

        final FormTable<PathRow> path = getPath();
        path.add().populate("tv-dvd", "TV/Video");
        path.add().populate("tv-dvd/televisores", "Televisores");

        final FormTable<ProductsRow> products = getProducts();
        products.add().populate("1", "SMART TV LED Samsung 40", new BigDecimal(6499.00));
        products.add().populate("2", "Product 2", new BigDecimal(200));
        products.add().populate("3", "Product 3", new BigDecimal(300));
    }

    //~ Inner Classes ................................................................................................................................

    public class PathRow extends PathRowBase {
        private void populate(String id, String description) {
            setPathId(id);
            setPathDescription(description);
        }
    }

    public class ProductsRow extends ProductsRowBase {
        private void populate(String id, String description, BigDecimal price) {
            setId(id);
            setDescription(description);
            setPrice(price);
        }
    }
}
