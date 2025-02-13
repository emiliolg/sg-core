
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.UiModelContext;
import tekgenesis.metadata.form.dependency.PrecedenceData;
import tekgenesis.metadata.form.model.*;
import tekgenesis.metadata.form.widget.*;

import static tekgenesis.check.CheckType.TITLE;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Constants.DEPENDANTS;
import static tekgenesis.common.core.Constants.ROW;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Predicates.in;
import static tekgenesis.common.core.Predicates.not;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.isNotQualified;
import static tekgenesis.metadata.form.exprs.Expressions.evaluate;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateBoolean;
import static tekgenesis.metadata.form.exprs.ItemContext.item;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.model.MultipleChanges.NONE;
import static tekgenesis.metadata.form.widget.WidgetType.TABLE;

/**
 * Evaluates the expressions that change the model (defaultValue and is).
 */
public class ModelExpressionsEvaluator implements ChangeListener, WidgetExpressionEvaluator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final UiModelContext context;

    @NotNull private final Evaluator evaluator;

    @NotNull private OnChangeHandler                       handler;
    @NotNull private final UiModelBase<?>                  model;
    @NotNull private final Registration                    registration;
    @NotNull private final List<WidgetExpressionEvaluator> replicators;

    //~ Constructors .................................................................................................................................

    public ModelExpressionsEvaluator(@NotNull final UiModelBase<?> model, @NotNull final UiModelContext context) {
        this.model   = model;
        this.context = context;
        evaluator    = new Evaluator();
        handler      = new OnChangeHandler.Default();
        replicators  = new ArrayList<>();
        registration = new Registration(model, this);
    }

    //~ Methods ......................................................................................................................................

    /** Compute {@link UiModel model} expressions. */
    public void compute() {
        final UiModel        metamodel  = metamodel();
        final PrecedenceData precedence = metamodel.getPrecedenceData();

        final Iterable<String> recomputes = precedence.getRecomputeList(metamodel.getName());

        // Compute the interdependent, non referenced expressions (mostly constants)
        computeWidgets(names(metamodel.getDescendants()).filter(e -> !contains(recomputes, e)), empty());

        // Compute expressions with references in the correct order.
        computeWidgets(recomputes, empty());
    }

    @Override public void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model m, @NotNull MultipleChanges indexes) {
        for (final WidgetExpressionEvaluator replicator : replicators)
            replicator.evaluateExpressions(widget, m, indexes);
    }

    /** Evaluate widget expressions with an optional row. */
    @Override public void evaluateExpressions(@NotNull final Widget w, @NotNull final Model m, @NotNull final Option<ItemContext> section) {
        // Initialize required widget definitions
        if (w.isWidgetDef() && w.isRequired() && !m.isDefined(w)) {
            final WidgetDef      def = context.getWidget(createQName(w.getWidgetDefinitionFqn()));
            final WidgetDefModel wdm = new WidgetDefModel(def, model, w, section.map(ItemContext::getItem));
            m.setWidgetDef(w, wdm);
            // Recursively compute expressions on new widget defition models
            context.compute(wdm);
        }

        if (w.hasValue()) {
            // reset (for hide and disable)
            if (w.isResettable()) evaluateReset(w, m);

            boolean changed = false;

            // default
            if (!m.isDefined(w)) changed = evaluateAndSet(w.getDefaultValueExpression(), w, m);

            // is
            changed = changed || evaluateAndSet(w.getIsExpression(), w, m);

            // try to set the default init value if model is undefined
            if (!changed) setDefaultInitValue(w, m);

            // handle/notify change
            if ((changed) && !registration.isRegistered()) handleOnChange(w, section);
        }

        for (final WidgetExpressionEvaluator replicator : replicators)
            replicator.evaluateExpressions(w, m, section);
    }

    /** Computes expressions for dependant section widgets. */
    public void itemCreatedCompute(int section, MultipleWidget multiple) {
        final Iterable<String>    recomputes = metamodel().getPrecedenceData().getRecomputeList(multiple.getName() + ROW);
        final Option<ItemContext> ctx        = item(multiple, section);

        // Compute expressions of row interdependent columns (mostly constants).
        computeWidgets(names(desc(multiple)).filter(e -> !contains(recomputes, e)), ctx);

        // Compute expressions of row dependant widgets.
        computeWidgets(recomputes, ctx);
    }

    /** Computes expressions deleted row dependant widgets. */
    public void itemDeletedCompute(MultipleWidget table) {
        final Iterable<String> recomputes = seq(metamodel().getPrecedenceData().getRecomputeList(table.getName() + ROW)).filter(e ->
                    !contains(names(desc(table)), e));
        computeWidgets(recomputes, empty());
    }

    @Override public final void onModelChange(@NotNull final IndexedWidget w) {
        final IndexedWidget       root   = w.root();
        final Widget              widget = root.widget();
        final Option<ItemContext> ctx    = root.itemContext();

        // Fire on_change
        handleOnChange(widget, ctx);

        final Model  m    = resolveModel(model, widget, ctx.map(ItemContext::getItem));
        final String path = w.path();

        if (m.isDefined(widget) || m.isReset(widget.getFieldSlot())) {
            // Compute
            final Iterable<String> recomputes = metamodel().getPrecedenceData().getRecomputeList(path);
            if (!isEmpty(recomputes)) computeWidgets(recomputes, ctx);
            else if (isNotQualified(path)) computeWidget(widget, ctx);

            // Reset
            final Iterable<String> dependants = metamodel().getPrecedenceData().getRecomputeList(path + DEPENDANTS);
            flatMap(dependants, this::asLocalElement).forEach(m::resetInternal);
        }
    }

    @Override public void onMultipleModelChange(@NotNull final MultipleWidget multiple, @NotNull final MultipleChanges changes) {
        final Iterable<String> recomputeList = seq(metamodel().getPrecedenceData().getRecomputeList(multiple.getName() + ROW)).filter(
                not(in(names(desc(multiple)))));

        computeWidgets(recomputeList, empty());

        // Replicate!
        evaluateExpressions(multiple, model, changes);
    }

    /** When a widget's definition changes, we should only compute that widget. */
    @Override public void onWidgetDefinitionChange(@NotNull IndexedWidget widget) {
        final IndexedWidget root = widget.root();
        computeWidget(root.widget(), root.itemContext());
    }

    /** Registers this as ValueChangeListener. */
    public void startListening() {
        registration.add();
    }

    /** Un register this as ValueChangeListener. */
    public void stopListening() {
        registration.remove();
    }

    /** Adds a WidgetExpressionEvaluator as replicator. */
    public ModelExpressionsEvaluator with(@NotNull final WidgetExpressionEvaluator replicator) {
        replicators.add(replicator);
        return this;
    }

    /** Adds an OnChangeHandler delegate where on change calls will be delegated. */
    public ModelExpressionsEvaluator with(@NotNull final OnChangeHandler delegate) {
        handler = delegate;
        return this;
    }

    @Nullable private Option<Widget> asLocalElement(@NotNull String reference) {
        return of(reference).filter(QName::isNotQualified).map(this::getElement);
    }

    /** Compute agiven widget with an optional {@link ItemContext section context}. */
    private void computeWidget(final Widget widget, final Option<ItemContext> section) {
        final Option<MultipleWidget> m = widget.getMultiple();

        if (m.isEmpty()) {  // Any affected widget
            evaluateExpressions(widget, model, empty());
        }
        else {
            final MultipleWidget multiple = m.get();

            if (section.isPresent() && section.get().belongsTo(multiple)) {
                // Specific row cell in widget's same multiple
                final Model s = section.get().mapping(model);
                evaluateExpressions(widget, s, section);
            }
            else {
                // All cells in multiple (or other multiple)
                int item = 0;
                for (final RowModel s : model.getMultiple(multiple))
                    evaluateExpressions(widget, s, item(multiple, item++));

                if (multiple.getWidgetType() != TABLE) evaluateExpressions(multiple, model, NONE);
            }
        }
    }  // end method computeWidget

    private void computeWidgets(@NotNull final Iterable<String> recomputes, @NotNull final Option<ItemContext> section) {
        flatMap(recomputes, this::asLocalElement).forEach(widget -> computeWidget(widget, section));
    }

    private Seq<Widget> desc(final Iterable<Widget> widgets) {
        return deepSeq(widgets);
    }

    private boolean doEvaluateAndSet(final Expression e, final Widget widget, final Model m) {
        final Object result = evaluate(evaluator, m, e);
        final Object value  = result == null ? getDefaultInitValue(widget) : result;
        if (widget.isMultiple() && value != null) return m.setArray(widget, listOf(value), false);
        else return m.set(widget, value, false);
    }

    private boolean evaluateAndSet(final Expression e, final Widget widget, final Model m) {
        return e != Expression.NULL && doEvaluateAndSet(e, widget, m);
    }

    private void evaluateReset(final Widget w, final Model m) {
        if (evaluateBoolean(evaluator, m, w.getHideExpression()) || evaluateBoolean(evaluator, m, w.getDisableExpression())) resetWidget(w, m);
    }

    private void handleOnChange(@NotNull final Widget widget, @NotNull final Option<ItemContext> section) {
        handler.handleOnChange(widget, section);
    }

    private UiModel metamodel() {
        return model.metadata();
    }

    private Seq<String> names(@NotNull Iterable<Widget> widgets) {
        return map(widgets, Widget::getName);
    }

    private void resetWidget(final Widget w, final Model m) {
        final WidgetType widgetType = w.getWidgetType();
        if (widgetType.isGroup()) {
            for (final Widget child : w)
                resetWidget(child, m);
        }
        else if (!widgetType.isMultiple()) m.resetInternal(w);
    }

    @Nullable private Object getDefaultInitValue(Widget w) {
        return w.getMsgType() == TITLE ? metamodel().getLabel() : WidgetTypes.getDefaultInitValue(w);
    }

    /**
     * Set the default init value to widgets that can't be null (ej: false to boolean) or that have
     * a default value (ej: images and internals of type String)
     */
    private void setDefaultInitValue(Widget w, Model model) {
        if (!w.isMultiple() && model.get(w) == null) {
            final Object initValue = getDefaultInitValue(w);
            if (initValue != null) model.set(w, initValue, false);
        }
        model.setDirtyState(w, true);
    }

    @NotNull private Widget getElement(@NotNull final String reference) {
        return metamodel().getElement(reference);
    }

    //~ Methods ......................................................................................................................................

    /** Computes expressions for dependant row widgets. */
    public static RowModel itemCreatedCompute(UiModelBase<?> model, Option<Integer> row, MultipleWidget table, UiModelContext retriever) {
        final MultipleModel multipleModel = model.getMultiple(table);
        final RowModel      result        = row.isPresent() ? multipleModel.addRow(row.get()) : multipleModel.addRow();
        new ModelExpressionsEvaluator(model, retriever).itemCreatedCompute(result.getRowIndex(), table);
        return result;
    }

    /** Computes expressions for dependant row widgets. */
    public static void itemDeletedCompute(UiModelBase<?> model, Option<Integer> row, MultipleWidget table, UiModelContext context) {
        final MultipleModel multipleModel = model.getMultiple(table);
        if (row.isPresent()) multipleModel.removeRow(row.get());
        else multipleModel.clear();
        new ModelExpressionsEvaluator(model, context).itemDeletedCompute(table);
    }

    //~ Inner Classes ................................................................................................................................

    private static class Registration {
        private final ChangeListener listener;

        private final UiModelBase<?> model;
        private boolean              registered = false;

        private Registration(UiModelBase<?> model, ChangeListener listener) {
            this.model    = model;
            this.listener = listener;
        }

        private void add() {
            model.addValueChangeListener(listener);
            registered = true;
        }

        private void remove() {
            model.removeValueChangeListener(listener);
            registered = false;
        }

        private boolean isRegistered() {
            return registered;
        }
    }
}  // end class ModelExpressionsEvaluator
