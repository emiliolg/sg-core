
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import org.jetbrains.annotations.NotNull;

/**
 * A Symbol for the Evaluator.
 */
public abstract class Symbol {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String name;

    //~ Constructors .................................................................................................................................

    Symbol(@NotNull String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    /** The name of the Symbol. */
    @NotNull public String getName() {
        return name;
    }
}
