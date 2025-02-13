
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.expr.RefTypeSolver;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * Resolves the ElemType of a reference in a Form.
 */
public class TypeBuilderRefSolver implements RefTypeSolver {

    //~ Instance Fields ..............................................................................................................................

    private final CompositeBuilder<?, ?, ?, ?> context;

    //~ Constructors .................................................................................................................................

    /** Creates the Type resolver for the form. */
    public TypeBuilderRefSolver(final CompositeBuilder<?, ?, ?, ?> context) {
        this.context = context;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Type doResolve(@NotNull String referenceName, boolean isColumn) {
        final CompositeFieldBuilder<?> a = context.getAttribute(referenceName);
        return a == null ? Types.nullType() : a.getType();
    }
}
