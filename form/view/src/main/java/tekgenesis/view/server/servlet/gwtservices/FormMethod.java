
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.ActionsImpl;
import tekgenesis.metadata.form.model.FormModel;

/**
 * Executes a Form Java class method.
 */
abstract class FormMethod implements Function<FormModel, Action> {

    //~ Instance Fields ..............................................................................................................................

    private final Scope scope;

    //~ Constructors .................................................................................................................................

    FormMethod() {
        this(Scope.OTHER);
    }

    FormMethod(@NotNull final Scope scope) {
        this.scope = scope;
    }

    //~ Methods ......................................................................................................................................

    /** Resolve action given model to be sync. */
    @Override public abstract Action apply(FormModel model);

    Scope getScope() {
        return scope;
    }

    //~ Static Fields ................................................................................................................................

    static final FormMethod DEFAULT = new FormMethod(Scope.OTHER) {
            @Override public Action apply(FormModel model) {
                return ActionsImpl.getInstance().getDefault();
            }
        };

    //~ Enums ........................................................................................................................................

    enum Scope { CREATE_OR_UPDATE, DELETE, CANCEL, OTHER }
}  // end class FormMethod
