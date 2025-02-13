
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.form;

import tekgenesis.form.FormTable;

import static tekgenesis.samples.form.DependeciesForm.products;

/**
 * User class for Form: SubformsExpressionsShowcase
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SubformsExpressions extends SubformsExpressionsBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<ProductsRow> table = getProducts();
        for (final String product : products)
            table.add().populate(product);
    }

    //~ Inner Classes ................................................................................................................................

    public class ProductsRow extends ProductsRowBase {
        private void populate(String product) {
            createDependencies(product);
        }
    }
}
