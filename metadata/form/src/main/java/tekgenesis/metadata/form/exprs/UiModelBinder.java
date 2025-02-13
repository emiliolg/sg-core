
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.Binder;
import tekgenesis.code.BoundRef;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.QualifiedWidget;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.metadata.form.QualifiedWidget.createFromReference;
import static tekgenesis.metadata.form.widget.Widget.ElemType.ARRAY;

/**
 * Resolves the value of a reference in a Form, binding a widget.
 */
class UiModelBinder extends Binder.Default {

    //~ Instance Fields ..............................................................................................................................

    private UiModel model = null;

    private final UiModelRetriever retriever;

    //~ Constructors .................................................................................................................................

    /** Creates a value resolver for the given {@link Form}. */
    UiModelBinder(final UiModelRetriever retriever) {
        this.retriever = retriever;
    }

    //~ Methods ......................................................................................................................................

    /** Bind given {@link UiModel ui model} descendant widgets. */
    public void bind(@NotNull final UiModel m) {
        model = m;
        bindWidgets();
    }

    @NotNull @Override public BoundRef<Boolean> bindPermissionRef(@NotNull final String name) {
        return context -> ((Model) context).hasPermission(name);
    }

    @NotNull @Override public BoundRef<Boolean> bindReadOnly() {
        return context -> ((Model) context).isReadOnly();
    }

    @NotNull @Override public BoundRef<Object> bindRef(@NotNull final String name, final boolean column) {
        final QualifiedWidget reference = createFromReference(retriever, model, name).getOrFail("Reference '" + name + NOT_FOUND);
        return createBoundRef(column, reference);
    }

    @NotNull @Override public BoundRef<Boolean> bindUpdate() {
        return context -> ((Model) context).isUpdate();
    }

    private void bindWidget(@NotNull Widget widget) {
        widget.getExpressionList().forEach(e -> e.bind(this));
    }

    private void bindWidgets() {
        for (final Widget child : model.getDescendants())
            // Bind widget expressions
            bindWidget(child);
    }

    @NotNull private BoundRef<Object> createBoundRef(boolean column, QualifiedWidget reference) {
        return context -> {
                   final Option<Object> result = reference.mapping((Model) context).map(m -> {
                           final Widget widget = reference.widget();
                           return column ? m.getColumn(widget) : widget.getElemType() == ARRAY ? m.getArray(widget) : m.get(widget);
                       });
                   return result.getOrNull();
               };
    }
}  // end class UiModelBinder
