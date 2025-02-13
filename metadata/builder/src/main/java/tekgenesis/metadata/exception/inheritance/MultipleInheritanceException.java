
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
 * Multiple Inheritance exception.
 */
public class MultipleInheritanceException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** MultipleInheritanceException constructor. */
    public MultipleInheritanceException(@NotNull String typeA, @NotNull String typeB, @NotNull String model) {
        super(
            String.format(
                "Type cannot extend multiple types: attempting to extend '%s' already extending '%s' type. Only interface types are allowed.",
                typeA,
                typeB),
            model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929212071985L;
}
