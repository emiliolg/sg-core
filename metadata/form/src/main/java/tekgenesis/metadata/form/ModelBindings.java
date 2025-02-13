
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.exprs.ModelExpressionsEvaluator;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.widget.WidgetType.WIDGET;

/**
 * Model bindings.
 */
public class ModelBindings {

    //~ Instance Fields ..............................................................................................................................

    private final List<ModelExpressionsEvaluator> evaluators;
    private UiModelBase<?>                        last;

    //~ Constructors .................................................................................................................................

    public ModelBindings(@NotNull final FormModel root) {
        last       = root;
        evaluators = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    public void addEvaluator(ModelExpressionsEvaluator evaluator) {
        evaluators.add(evaluator);
    }

    /** Return {@link ModelExpressionsEvaluator deepest evaluator}. */
    public ModelExpressionsEvaluator deepest() {
        return evaluators.get(evaluators.size() - 1);
    }

    /** Advance to next parent and keep reference. */
    @NotNull public UiModelBase<?> next(Parentable<?> m) {
        m.parent().ifPresent(p -> {
            final Widget     anchor = p.anchor();
            final WidgetType wt     = anchor.getWidgetType();
            if (wt == WIDGET) last = resolveModel(last, anchor, p.item()).getWidgetDef(anchor);
        });

        if (last == null) throw new IllegalStateException("View " + m + " has no underlying model!");
        if (!m.getUiModel().equals(last.metadata())) throw new IllegalStateException("Expecting matching metadata!");

        return last;
    }

    public void startListening() {
        evaluators.forEach(ModelExpressionsEvaluator::startListening);
    }

    public void stopListening() {
        evaluators.forEach(ModelExpressionsEvaluator::stopListening);
    }

    /** Return last model after traverse. */
    public UiModelBase<?> getLast() {
        return last;
    }
}  // end class ModelBindings
