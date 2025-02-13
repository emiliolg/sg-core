
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

import tekgenesis.sales.basic.g.ProductViewBase;

/**
 * User class for Model: ProductView
 */
public class ProductView extends ProductViewBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getMyDesc() {
        return "Hello Desc";
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}
