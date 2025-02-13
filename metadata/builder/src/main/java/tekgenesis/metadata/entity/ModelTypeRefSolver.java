
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
import tekgenesis.common.core.Option;
import tekgenesis.expr.RefTypeSolver;
import tekgenesis.field.TypeField;
import tekgenesis.type.ModelType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * Resolves the ElemType of a reference in a Form.
 */
public class ModelTypeRefSolver implements RefTypeSolver {

    //~ Instance Fields ..............................................................................................................................

    private final ModelType context;

    //~ Constructors .................................................................................................................................

    /** Creates the Type resolver for the form. */
    public ModelTypeRefSolver(final ModelType context) {
        this.context = context;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Type doResolve(@NotNull String referenceName, boolean isColumn) {
        final Option<? extends TypeField> a = context.getField(referenceName);
        return a.isEmpty() ? Types.nullType() : a.get().getFinalType();
    }
}
