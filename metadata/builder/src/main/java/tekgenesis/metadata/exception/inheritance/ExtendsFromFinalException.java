
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception.inheritance;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.exception.BuilderException;

/**
 * Interface extends From final type exception.
 */
public class ExtendsFromFinalException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public ExtendsFromFinalException(@NotNull String type, @NotNull String model) {
        super(String.format("unable to extend from a final type '%s'.", type), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1762384001637342537L;
}
