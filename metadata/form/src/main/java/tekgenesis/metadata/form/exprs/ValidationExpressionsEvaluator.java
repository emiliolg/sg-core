
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.Check;
import tekgenesis.check.CheckMsg;
import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.MetadataFormMessages;
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleChanges;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.model.KeyMap;
import tekgenesis.type.DecimalType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.DateOnly.fromDate;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Times.millisToDate;
import static tekgenesis.metadata.form.MetadataFormMessages.MSGS;
import static tekgenesis.metadata.form.exprs.Expressions.evaluate;
import static tekgenesis.metadata.form.exprs.ItemContext.item;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.metadata.form.widget.WidgetTypes.hasOptions;
import static tekgenesis.metadata.form.widget.WidgetTypes.isDateRangeWidget;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;

/**
 * Evaluates all expressions regarding validations. To be used on client side, server side and user
 * code.
 */
public class ValidationExpressionsEvaluator implements WidgetExpressionEvaluator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Evaluator                evaluator;
    @NotNull private final List<ValidationListener> listeners;

    //~ Constructors .................................................................................................................................

    /** Constructor with a ValidationListener to notify upon validations. */
    public ValidationExpressionsEvaluator(@NotNull final ValidationListener listener) {
        evaluator = new Evaluator();
        listeners = new ArrayList<>();
        addValidationListener(listener);
    }

    //~ Methods ......................................................................................................................................

    /** Adds a validation listener. */
    public void addValidationListener(@NotNull final ValidationListener listener) {
        listeners.add(listener);
    }

    @Override public void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model model, @NotNull MultipleChanges indexes) {
        evaluateExpressions(widget, model, Option.<SubformContext>empty());
    }

    public void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Seq<Integer> indexes, @NotNull Model model) {
        final MultipleModel table = model.getMultiple(widget);
        for (final Widget column : widget.getTableElements()) {
            for (final Integer i : indexes)
                evaluateExpressions(column, table.getRow(i), item(widget, i));
        }
    }

    @Override public void evaluateExpressions(@NotNull final Widget w, @NotNull final Model m, @NotNull final Option<ItemContext> section) {
        evaluateExpressions(w, m, section, empty());
    }

    private boolean checkFilled(final Widget widget, Model model) {
        return widget.getWidgetType() == SUBFORM
               ? model.getSubform(widget) != null
               : widget.getWidgetType() == WIDGET ? model.getWidgetDef(widget) != null : !widget.hasValue() || model.hasValue(widget);
    }

    private boolean checkOptional(final Widget widget, Model model) {
        return !(widget.hasValue() || widget.getWidgetType() == SUBFORM) || !widget.getIsExpression().isNull() ||
               evaluateBoolean(widget.getOptionalExpression(), model);
    }

    private void checkRangeConstraints(final Widget widget, Model model, List<CheckMsg> messages) {
        if (getEffectiveWidgetType(widget, model) == RANGE) {
            final List<Object> values = Colls.toList(model.getArray(widget));
            if (!values.isEmpty()) {
                final Number from = (Number) values.get(0);
                final Number to   = (Number) values.get(1);
                if (to.doubleValue() - from.doubleValue() < 0) messages.add(new CheckMsg(MSGS.invalidRangeValues()));
            }
        }
        else if (getEffectiveWidgetType(widget, model) == RANGE_VALUE && model.hasValue(widget)) validateRangeRange(widget, model, messages);
        else if (isDateRangeWidget(widget.getWidgetType()) && model.hasValue(widget)) validateDateRange(widget, model, messages);
    }  // end method checkRangeConstraints

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    private void checkSizeConstraints(final Widget widget, Model model, List<CheckMsg> messages) {
        if (isMultiple(widget.getWidgetType())) return;

        final Type type = widget.getType();
        if (type.isArray()) return;

        final Object value = model.get(widget);
        if (value == null) return;

        if (type.isString() && type.getLength().isPresent()) {
            final Integer length = type.getLength().get();
            final int     l      = ((String) value).length();
            if (l > length) messages.add(new CheckMsg(MSGS.valueExceedsLimit(l, length)));
        }

        if (value instanceof BigDecimal) {
            final BigDecimal  d  = (BigDecimal) value;
            final DecimalType dt = (DecimalType) type;
            if (d.precision() > dt.getPrecision() || d.scale() > dt.getDecimals())
                messages.add(new CheckMsg(MSGS.valueExceedsLimitDecimal(d.precision(), d.scale(), dt.getPrecision(), dt.getDecimals())));
        }

        if (type.getKind() == Kind.INT) {
            final int    length    = type.getLength().get();
            final String val       = value.toString();
            final int    valLength = widget.isSigned() && val.startsWith("-") ? val.length() - 1 : val.length();
            if (valLength > length) messages.add(new CheckMsg(MSGS.valueExceedsLimit(val.length(), length)));
        }

        if (widget.getWidgetType() != DISPLAY && value instanceof Long && (type.getKind() == Kind.DATE || type.getKind() == Kind.DATE_TIME)) {
            final Long       l      = (Long) value;
            final Date       date   = millisToDate(l, type.getKind() != Kind.DATE_TIME);
            final DateConfig config = (DateConfig) model.getFieldConfig(widget);
            if (config != null) validateDate(date, config, messages);
        }
    }

    private void checkUniqueColumn(final Widget widget, Model model, Option<Integer> s, List<CheckMsg> messages) {
        if (widget.isUnique()) {
            // check that this value is unique in the column
            final Object val = model.get(widget);

            final int section = s.get();

            int current = 0;
            for (final Object col : model.getColumn(widget)) {
                if (section != current++ && equal(col, val)) messages.add(new CheckMsg(MSGS.columnValueTaken()));
            }
        }
    }

    private void checkValidOptionInCombo(Widget widget, Model model, List<CheckMsg> messages) {
        final Object      v       = widget.isMultiple() ? null : model.get(widget);
        final Iterable<?> vs      = widget.isMultiple() ? model.getArray(widget) : v != null ? listOf(v) : emptyList();
        final KeyMap      options = model.getOptions(widget);
        for (final Object o : vs)
            if (isNotInOptions(options, o) && isNotInOptions(options, o != null ? o.toString() : ""))
                messages.add(new CheckMsg(MetadataFormMessages.MSGS.invalidValueForOptionWidget(o != null ? o.toString() : "", widget.getName())));
    }

    /** Evaluates a Boolean Expression. */
    private boolean evaluateBoolean(final Expression e, Model model) {
        return Expressions.evaluateBoolean(evaluator, model, e);
    }

    private boolean evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model model, @NotNull final Option<SubformContext> parentSubform) {
        final MultipleModel      table   = model.getMultiple(widget);
        final Collection<Widget> columns = widget.getTableElements();
        for (int i = 0; i < table.size(); i++) {
            final RowModel row         = table.getRow(i);
            boolean        errorsOnRow = false;
            for (final Widget column : columns)
                errorsOnRow |= evaluateExpressions(column, row, item(widget, i), parentSubform);
            if (errorsOnRow) return true;
        }
        return false;
    }

    private boolean evaluateExpressions(@NotNull final Widget widget, @NotNull final Model model, @NotNull final Option<ItemContext> section,
                                        @NotNull final Option<SubformContext> parentSubform) {
        if (!shouldValidate(widget, model)) return false;

        final List<CheckMsg> messages = new ArrayList<>();

        // check required values
        final boolean required = !checkOptional(widget, model);
        final boolean filled   = checkFilled(widget, model);

        final Option<Integer> item = section.map(ItemContext::getItem);

        if (!filled && required) messages.add(new CheckMsg(MSGS.requiredValue()));
        else {
            if (widget.getWidgetType() == SUBFORM) evaluateSubform(widget, model, item);
            else if (widget.getWidgetType() == WIDGET) evaluateWidgetDefinition(widget, model);
            else if (filled) {      // || required) {
                // check unique
                checkUniqueColumn(widget, model, item, messages);
                // check size
                checkSizeConstraints(widget, model, messages);
                // check ranges
                checkRangeConstraints(widget, model, messages);
                // check if valid option
                if (hasOptions(widget.getWidgetType()) && widget.getWidgetType() != WidgetType.DYNAMIC)
                    checkValidOptionInCombo(widget, model, messages);
            }

            final CheckMsg checkMsg = model.getFieldMsg(widget);
            if (checkMsg != null && isNotEmpty(checkMsg.getText())) messages.add(checkMsg);
            else if (filled) {
                // execute validations
                for (final Check checks : widget.getChecks()) {
                    if (!evaluateBoolean(checks.getExpr(), model)) {
                        messages.add(checks.getMsg());
                        break;
                    }
                }
            }
        }

        final Seq<CheckMsg> msgs = seq(messages);

        if (parentSubform.isPresent()) notifyParentSubform(widget, model, item, parentSubform, msgs);
        else {
            for (final ValidationListener listener : listeners)
                listener.onValidation(widget, model, item, empty(), msgs);
        }

        return !messages.isEmpty();
    }

    private void evaluateSubform(@NotNull Widget widget, @NotNull Model model, @NotNull Option<Integer> row) {
        final FormModel subform = model.getSubform(widget);
        if (subform != null) {
            final Option<SubformContext> context = of(new SubformContext(widget, model, row));
            for (final Widget child : subform.metadata().getDescendants()) {
                if (child instanceof MultipleWidget) evaluateExpressions((MultipleWidget) child, subform, context);
                else if (child.getMultiple().isEmpty()) evaluateExpressions(child, subform, empty(), context);
            }
        }
    }

    private void evaluateWidgetDefinition(@NotNull Widget widget, @NotNull Model model) {
        final boolean required = !checkOptional(widget, model);
        final boolean empty    = !checkFilled(widget, model);
        if (required && empty) throw unreachable();  // todo pcolunga validation
    }

    private void notifyParentSubform(@NotNull Widget widget, @NotNull Model model, @NotNull Option<Integer> row,
                                     @NotNull Option<SubformContext> parentSubform, Seq<CheckMsg> msgs) {
        final SubformContext context = parentSubform.get();
        for (final ValidationListener listener : listeners) {
            final Widget         subform     = context.getSubform();
            final Option<String> subformPath;

            if (subform.isInline()) {
                subformPath = of(context.getModel().getPath() + "/" + subform.getName());
                listener.onValidation(widget, model, row, subformPath, msgs);
            }
            else {
                subformPath = of(context.getModel().getPath());
                listener.onValidation(subform, context.getModel(), context.getRow(), subformPath, msgs);
            }
        }
    }

    private boolean shouldValidate(Widget widget, Model model) {
        final WidgetType widgetType = widget.getWidgetType();
        return widgetType != INTERNAL && (!(widget.hasValue() || widgetType == SUBFORM) || model.isDefined(widget));
    }

    @SuppressWarnings("deprecation")
    private void validateDate(Date d, DateConfig c, List<CheckMsg> messages) {
        final DateOnly date = fromDate(d);
        if (!c.getEnabled().contains(date) && (c.isDayOfWeekDisabled(ChartConfig.toGetDay(d.getDay())) || c.getDisabled()
                                                                                                           .contains(date)))
            messages.add(new CheckMsg(MSGS.disabledDate()));
    }

    private void validateDateRange(Widget widget, Model model, List<CheckMsg> messages)
    {
        final long value = ((Number) getSingletonValue(widget, model)).longValue();

        final Expression fromExpression = widget.getFromExpression();
        if (!fromExpression.isNull()) {
            final Object f = evaluate(evaluator, model, fromExpression);
            if (value - (Long) f < 0) messages.add(new CheckMsg(MSGS.invalidRangeValue()));
        }

        final Expression toExpression = widget.getToExpression();
        if (!toExpression.isNull()) {
            final Object t = evaluate(evaluator, model, toExpression);
            if ((Long) t - value < 0) messages.add(new CheckMsg(MSGS.invalidRangeValue()));
        }
    }

    private void validateRangeRange(Widget widget, Model model, List<CheckMsg> messages) {
        final List<Object> boundaries = Colls.toList(model.getOptions(widget).keySet());
        final double       value      = ((Number) getSingletonValue(widget, model)).doubleValue();
        final int          size       = boundaries.size();
        if (size > 0) {
            final double from = ((Number) boundaries.get(0)).doubleValue();
            if (size > 1) {
                final double to = ((Number) boundaries.get(1)).doubleValue();
                if (to - from >= 0 && (value - from < 0 || to - value < 0)) {  // Expect consistent constraints :)
                    messages.add(new CheckMsg(MSGS.invalidRangeValue()));
                }
            }
            else {
                if (value < from) messages.add(new CheckMsg(MSGS.invalidRangeValue()));
            }
        }
    }

    /** Effective widget type. Base widget type, maybe shadowed by dynamic implementation. */
    private WidgetType getEffectiveWidgetType(@NotNull final Widget widget, @NotNull final Model model) {
        final WidgetType base = widget.getWidgetType();
        if (base == DYNAMIC) {
            final DynamicConfig config = (DynamicConfig) model.getFieldConfig(widget);
            if (config != null && config.getWidget() != NONE) return config.getWidget();
        }
        return base;
    }

    private boolean isNotInOptions(KeyMap options, Object o) {
        return !options.containsKey(o) && !options.containsValue(o.toString());
    }

    private Object getSingletonValue(Widget widget, Model model) {
        return widget.isMultiple() ? model.getArray(widget).iterator().next() : model.get(widget);
    }

    //~ Methods ......................................................................................................................................

    /** Validate entire form model, including table cells. */
    public static void validate(@NotNull final FormModel formModel, @NotNull final ValidationListener listener) {
        final ValidationExpressionsEvaluator evaluator = new ValidationExpressionsEvaluator(listener);

        for (final Widget child : formModel.metadata().getDescendants()) {
            if (child instanceof MultipleWidget) evaluator.evaluateExpressions((MultipleWidget) child, formModel, MultipleChanges.NONE);
            else if (child.getMultiple().isEmpty()) evaluator.evaluateExpressions(child, formModel, empty());
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class SubformContext {
        @NotNull private final Model           model;
        @NotNull private final Option<Integer> row;
        @NotNull private final Widget          subform;

        private SubformContext(@NotNull Widget subform, @NotNull Model model, @NotNull Option<Integer> row) {
            this.subform = subform;
            this.model   = model;
            this.row     = row;
        }

        /** Return subform parent model. */
        @NotNull public Model getModel() {
            return model;
        }

        /** Return subform row (if defined). */
        @NotNull public Option<Integer> getRow() {
            return row;
        }

        /** Return subform widget. */
        @NotNull public Widget getSubform() {
            return subform;
        }
    }
}
