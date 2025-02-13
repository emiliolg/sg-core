
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

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleChanges;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;

/**
 * Widget expressions evaluator.
 */
public interface WidgetExpressionEvaluator {

    //~ Methods ......................................................................................................................................

    /** Evaluate table widget expressions. */
    void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model model, @NotNull MultipleChanges indexes);

    /** Evaluate widget expressions with an optional {@link ItemContext section context}. */
    void evaluateExpressions(@NotNull Widget w, @NotNull Model m, @NotNull Option<ItemContext> section);
}
