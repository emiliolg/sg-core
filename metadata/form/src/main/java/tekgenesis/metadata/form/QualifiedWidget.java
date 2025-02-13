
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.util.ArrayDeque;
import java.util.Queue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.Pure;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.core.Option.*;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.split;

/**
 * Qualified widget.
 */
public class QualifiedWidget extends BaseQualifiedWidget<QualifiedWidget> {

    //~ Constructors .................................................................................................................................

    private QualifiedWidget(@NotNull final Widget widget) {
        super(widget);
    }

    private QualifiedWidget(@NotNull final QualifiedWidget qualification, @NotNull final Widget widget) {
        super(qualification, widget);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<Model> mapping(@NotNull Model m) {
        return mapping(qualification, m);
    }

    /** Map given model to deepest model. */
    @NotNull private Option<Model> mapping(Option<QualifiedWidget> qualified, Model m) {
        if (qualified.isEmpty()) return some(m);
        return qualified.flatMap(q -> {
            if (q.widget.isWidgetDef()) return mapping(q, m.getWidgetDef(q.widget));
            if (q.widget.isSubform()) return mapping(q, m.getSubform(q.widget));
            throw unreachable("Invalid scoping for " + q.widget);
        });
    }

    @NotNull private Option<Model> mapping(@NotNull QualifiedWidget w, @Nullable UiModelBase<?> m) {
        return ofNullable(m).flatMap(model -> mapping(w.qualification, model));
    }

    /**
     * Creates a new {@link QualifiedWidget qualified widget} qualifying given {@link Widget widget}
     * (e.g.: employee.nest(name) -> employee.name)
     */
    @NotNull @Pure private QualifiedWidget qualifying(@NotNull Widget w) {
        return new QualifiedWidget(this, w);
    }

    //~ Methods ......................................................................................................................................

    /** Create a {@link QualifiedWidget qualified widget} from a given reference. */
    @NotNull public static Option<QualifiedWidget> createFromReference(@NotNull final UiModelRetriever retriever, @NotNull final UiModel model,
                                                                       @NotNull final String reference) {
        final Queue<String> queue  = new ArrayDeque<>(split(reference, '.'));
        QualifiedWidget     result = null;
        UiModel             scope  = model;
        while (!queue.isEmpty() && scope != null) {
            final String name = queue.remove();
            if (!scope.containsElement(name)) return empty();
            final Widget widget = scope.getElement(name);
            result = result != null ? result.qualifying(widget) : new QualifiedWidget(widget);
            scope  = nextScope(retriever, scope, widget);
        }
        return ofNullable(result);
    }

    @Nullable static UiModel nextScope(@NotNull final UiModelRetriever retriever, @NotNull final UiModel scope, @NotNull final Widget widget) {
        if (widget.isWidgetDef()) return retriever.getWidget(createQName(widget.getWidgetDefinitionFqn()));
        if (widget.isSubform()) return retriever.getForm(createQName(widget.getSubformFqn()));
        return null;
    }
}  // end class QualifiedWidget
