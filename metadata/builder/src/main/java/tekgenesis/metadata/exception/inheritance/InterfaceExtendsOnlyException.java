
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
 * Interface extends only exception.
 */
public class InterfaceExtendsOnlyException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public InterfaceExtendsOnlyException(@NotNull String extension, @NotNull String model) {
        super(String.format("Interface types are allow to extend from interface types only. Attempting to extend from type '%s'.", extension), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929230091984L;
}
