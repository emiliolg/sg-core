
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.exprs.ModelExpressionsEvaluator;
import tekgenesis.metadata.form.exprs.ValidationExpressionsEvaluator;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.*;

import static tekgenesis.common.Predefined.min;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.view.client.ClientNoComputeUiModelContext.getNoComputeUiModelContext;

/**
 * Used to bind all {@link ModelUI model uis} on view for sync.
 */
class UiSyncBindingVisitor implements ModelUiVisitor {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<ModelExpressionsEvaluator> evaluators;

    @NotNull private final Stack<UiModelBase<?>> stack;

    //~ Constructors .................................................................................................................................

    UiSyncBindingVisitor(@NotNull final FormModel root) {
        stack      = new Stack<>();
        evaluators = new ArrayList<>();
        stack.add(root);
    }

    //~ Methods ......................................................................................................................................

    @Override public void visit(FormUI form) {
        bind(form);
    }

    @Override public void visit(WidgetDefUI widget) {
        widget.parent().ifPresent(p -> {
            final Widget         w     = p.anchor();
            final WidgetDefModel model = resolveModel(last(), w, p.item()).getWidgetDef(w);
            if (model != null) {
                stack.add(model);
                bind(widget);
                stack.pop();
            }
        });
    }

    @Override public void visit(MultipleUI ui) {
        final MultipleModel multiple = last().getMultiple(ui.getMultipleModel());

        for (int section = 0; section < min(multiple.size(), ui.getSectionsCount()); section++)
            traverse(ui.getSection(section));
    }

    void forEachEvaluator(@NotNull final Consumer<ModelExpressionsEvaluator> consumer) {
        evaluators.forEach(consumer);
    }

    private void bind(@NotNull final ModelUI m) {
        final WidgetUIFinder f = m.finder();

        final ModelExpressionsEvaluator evaluator = new ModelExpressionsEvaluator(last(), getNoComputeUiModelContext())  //
                                                    .with(new UiExpressionsEvaluator(f))
                                                    .with(new ValidationExpressionsEvaluator(new ModelUIValidationListener(f)));
        evaluators.add(evaluator);

        // Traverse after binding
        traverse(m);
    }

    private UiModelBase<?> last() {
        return stack.peek();
    }
}  // end class UiSyncBindingVisitor
