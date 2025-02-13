
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.SymbolNotFoundException;
import tekgenesis.type.Type;

/**
 * Solves the Type for a field reference.
 */
public interface RefTypeSolver {

    //~ Instance Fields ..............................................................................................................................

    RefTypeSolver EMPTY = (name, isColumn) -> { throw new SymbolNotFoundException(name); };

    //~ Methods ......................................................................................................................................

    /** Resolves the reference and returns its type. */
    @NotNull Type doResolve(@NotNull String referenceName, boolean isColumn);
}
