
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import org.jetbrains.annotations.NotNull;

import tekgenesis.service.Factory;
import tekgenesis.service.Result;

/**
 * User class for Handler: HB
 */
public class HB extends HBBase {

    //~ Constructors .................................................................................................................................

    HB(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/". */
    @NotNull @Override public Result<String> home() {
        return notImplemented();
    }
}
