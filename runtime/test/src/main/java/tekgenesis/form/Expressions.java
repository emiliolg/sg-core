
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.Evaluator;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.assignment.AssignmentType;

import static tekgenesis.metadata.form.exprs.Expressions.evaluateAssignmentList;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateBoolean;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateString;

/**
 * Utility class to deal with Expressions on test environments.
 */
public class Expressions {

    //~ Instance Fields ..............................................................................................................................

    private final Model     context;
    private final Evaluator evaluator = new Evaluator();
    private final Widget    widget;

    //~ Constructors .................................................................................................................................

    /** Returns Expressions utility for given field. */
    public Expressions(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        final ReflectedFormInstance wrap = ReflectedFormInstance.wrap(form);
        widget  = wrap.getWidget(field);
        context = row == -1 ? wrap.getModel() : wrap.getRowModel(row, widget);
    }

    //~ Methods ......................................................................................................................................

    /** Returns if field is disabled. */
    public boolean isDisabled() {
        final Expression expr = widget.getDisableExpression();
        return evaluateBoolean(evaluator, context, expr);
    }

    /** Returns field filter. */
    public Iterable<AssignmentType> getFilterExpression() {
        final Expression expr = widget.getFilterExpression();
        return evaluateAssignmentList(evaluator, context, expr);
    }

    /** Returns field hint. */
    public String getHint() {
        final Expression expr = widget.getHint();
        return evaluateString(evaluator, context, expr);
    }

    /** Returns if field is optional. */
    public boolean isOptional() {
        final Expression expr = widget.getOptionalExpression();
        return evaluateBoolean(evaluator, context, expr);
    }

    /** Returns field custom mask. */
    public String getMask() {
        final Expression expr = widget.getCustomMaskExpression();
        return evaluateString(evaluator, context, expr);
    }

    /** Returns if field is hidden. */
    public boolean isHidden() {
        final Expression expr = widget.getHideExpression();
        return evaluateBoolean(evaluator, context, expr);
    }

    /** Returns if field is hidden. */
    public boolean isHiddenColumn() {
        final Expression expr = widget.getHideColumnExpression();
        return evaluateBoolean(evaluator, context, expr);
    }

    /** Returns field placeholder. */
    public String getPlaceholder() {
        final Expression expr = widget.getPlaceholderExpression();
        return evaluateString(evaluator, context, expr);
    }
}  // end class Expressions
