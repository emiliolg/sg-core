
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.form.FormTable;

/**
 * User class for Form: SubformsExpressions
 */
public class SubformsExpressions extends SubformsExpressionsBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<ProductsRow> table = getProducts();
        for (final String product : DependeciesForm.products) {
            if (!"server".equals(product)) table.add().populate(product);
            else table.add();
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class ProductsRow extends ProductsRowBase {
        private void populate(String product) {
            createDependencies(product);
        }
    }
}
