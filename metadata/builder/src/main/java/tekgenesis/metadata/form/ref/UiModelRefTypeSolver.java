
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.ref;

import org.jetbrains.annotations.NotNull;

import tekgenesis.expr.RefPermissionSolver;
import tekgenesis.expr.RefTypeSolver;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.metadata.form.QualifiedWidget.createFromReference;
import static tekgenesis.type.Types.nullType;

/**
 * Resolve references type in {@link UiModel graphical models}.
 */
public class UiModelRefTypeSolver implements RefTypeSolver, RefPermissionSolver {

    //~ Instance Fields ..............................................................................................................................

    private final UiModel         context;
    private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    /** Creates the Type resolver for the form. */
    public UiModelRefTypeSolver(final UiModel context, ModelRepository repository) {
        this.context    = context;
        this.repository = repository;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Type doResolve(@NotNull String reference, boolean column) {
        return createFromReference(retriever(), context, reference).map(qualified -> {
                final Widget widget = qualified.widget();
                Type         type   = nullType();
                // Referencing a valued widget
                if (widget.hasValue()) {
                    type = widget.getType();
                    if (column && widget.getMultiple().isPresent()) type = Types.arrayType(type);
                }
                return type;
            }).orElse(nullType());
    }

    @Override public boolean hasPermission(@NotNull String permissionName) {
        return context.hasPermission(permissionName);
    }

    @NotNull private UiModelRetriever retriever() {
        return key -> repository.getModel(key, UiModel.class);
    }
}  // end class UiModelRefTypeSolver
