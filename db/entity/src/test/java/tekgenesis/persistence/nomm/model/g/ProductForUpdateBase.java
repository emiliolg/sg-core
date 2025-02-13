
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.*;

/**
 * Javadoc.
 */
public class ProductForUpdateBase extends Product implements PersistableInstance<Product, String> {

    //~ Methods ......................................................................................................................................

    /** Javadoc. */
    public ProductForUpdate setColor(Color v) {
        ((ProductBase) this).color = v;
        return (ProductForUpdate) this;
    }

    /** Javadoc. */
    public ProductForUpdate setMainCategory(Category category) {
        ((ProductBase) this).mainCategory.set(category);
        ((ProductBase) this).mainCategoryCode = category == null ? null : category.getCode();
        return (ProductForUpdate) this;
    }

    /** Javadoc. */
    public ProductForUpdate setName(String v) {
        ((ProductBase) this).name = v;
        return (ProductForUpdate) this;
    }

    /** Javadoc. */
    public ProductForUpdate setOptCategory(Category category) {
        ((ProductBase) this).optCategory.set(category);
        ((ProductBase) this).optCategoryCode = category == null ? null : category.getCode();
        return (ProductForUpdate) this;
    }

    //~ Methods ......................................................................................................................................

    /** Create a product. */
    public static ProductForUpdate create(String key) {
        final ProductForUpdate result = new ProductForUpdate();
        ((ProductBase) result).code = key;
        return result;
    }
    public static ProductForUpdate productForUpdate(ProductBase product) {
        return product.copyTo(new ProductForUpdate());
    }
}  // end class ProductForUpdateBase
