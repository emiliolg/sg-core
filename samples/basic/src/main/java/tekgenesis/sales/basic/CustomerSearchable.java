
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

import tekgenesis.sales.basic.g.CustomerSearchableBase;

/**
 * User class for Model: CustomerSearchable
 */
public class CustomerSearchable extends CustomerSearchableBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getSomeExtraField() {
        return "some extra value!";
    }
}
