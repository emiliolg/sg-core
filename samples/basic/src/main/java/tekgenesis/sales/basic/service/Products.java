
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import java.math.BigDecimal;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Utility class to create products.
 */
@SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection" })
class Products {

    //~ Constructors .................................................................................................................................

    private Products() {}

    //~ Methods ......................................................................................................................................

    static Product createProduct(String id, String model, BigDecimal price, @Nullable String desc) {
        final Product product = new Product();
        product.setProductId(id);
        product.setModel(model);
        product.setPrice(price);
        product.setDescription(desc);
        product.setCreated(DateTime.current());
        return product;
    }

    static ProductList createProductList(String id) {
        final ProductList list = new ProductList();
        list.setList(Integer.valueOf(id));
        list.setProducts(createProductSequence());
        return list;
    }

    static ImmutableList<Product> createProductSequence() {
        return listOf(tv, phone, tablet);
    }

    static Product phone(String id) {
        return copy(phone, id);
    }
    static Product tv(String id) {
        return copy(tv, id);
    }

    private static Product copy(Product p, String id) {
        return createProduct(id, p.getModel(), p.getPrice(), p.getDescription());
    }

    //~ Static Fields ................................................................................................................................

    static final Product tv;
    static final Product phone;
    static final Product tablet;

    static {
        tv     = createProduct("1", "Sony 42", new BigDecimal(499.99), "Tv");
        phone  = createProduct("2", "Galaxy S3", new BigDecimal(359.99), "Phone");
        tablet = createProduct("3", "Ipad", new BigDecimal(669.99), "Tablet");
    }
}  // end class Products
