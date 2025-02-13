
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

import tekgenesis.metadata.exception.DuplicateFieldException;

/**
 * Field clash exception.
 */
public class FieldClashesWithUnrelatedTypeException extends DuplicateFieldException {

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    public FieldClashesWithUnrelatedTypeException(@NotNull String fieldA, @NotNull String typeA, @NotNull String fieldB, @NotNull String typeB,
                                                  @NotNull String model) {
        super(String.format("'%s' in '%s' clashes with '%s' in '%s'; fields have unrelated types", fieldA, typeA, fieldB, typeB), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929201041984L;
}
