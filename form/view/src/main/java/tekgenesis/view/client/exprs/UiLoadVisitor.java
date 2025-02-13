
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.*;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.model.MultipleChanges.NONE;

/**
 * Ui load visitor :S.
 */
class UiLoadVisitor implements ModelUiVisitor {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final UiExpressionsEvaluator evaluator;
    @NotNull private final Model                  local;

    //~ Constructors .................................................................................................................................

    UiLoadVisitor(@NotNull final WidgetUIFinder finder, @NotNull final Model local) {
        this.local = local;
        evaluator  = new UiExpressionsEvaluator(finder);
    }

    //~ Methods ......................................................................................................................................

    @Override public void visit(MultipleUI ui) {
        // multiple widget expressions evaluation.
        evaluator.evaluateExpressions(ui.getMultipleModel(), local, NONE);
    }

    @Override public void visit(WidgetUI ui) {
        // any widget expressions evaluation.
        evaluator.evaluateExpressions(ui.getModel(), local, empty());
    }

    @Override public void visit(WidgetDefUI widget) {
        final Widget         w = widget.getModel();
        final WidgetDefModel m = resolveModel(local, w, widget.getContext().getItem()).getWidgetDef(w);
        if (w.isRequired())
        // Optional widgets will be visited by UiLoadVisitor on UiWidgetDefVisitor
        new UiLoadVisitor(widget.finder(), ensureNotNull(m)).traverse(widget);
    }
}
