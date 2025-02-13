
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.web.bindery.event.shared.EventBus;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableList.Builder;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.ModelBindings;
import tekgenesis.metadata.form.UiModelContext;
import tekgenesis.metadata.form.exprs.*;
import tekgenesis.metadata.form.model.*;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.ui.*;
import tekgenesis.view.client.ui.WidgetUI.Context;

import static java.lang.Math.min;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.collections.ImmutableList.builder;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.metadata.form.exprs.ItemContext.createItemContext;
import static tekgenesis.metadata.form.exprs.ItemContext.item;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.model.MultipleChanges.NONE;
import static tekgenesis.view.client.ClientNoComputeUiModelContext.getNoComputeUiModelContext;
import static tekgenesis.view.client.ClientUiModelContext.createClientContext;
import static tekgenesis.view.client.exprs.UiExpressionsEvaluator.isReadOnly;

/**
 * Entry points to trigger the evaluation of the expressions in the client.
 */
public class ExpressionEvaluatorFactory {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final EventBus       bus;
    @NotNull private final FormController controller;
    @NotNull private final WidgetUIFinder finder;
    @NotNull private final FormModel      root;
    @NotNull private final FormUI         view;

    //~ Constructors .................................................................................................................................

    /** Constructs the factory. */
    public ExpressionEvaluatorFactory(@NotNull final FormController controller) {
        this.controller = controller;
        view            = controller.getView();
        root            = controller.getModel();
        bus             = controller.getBus();
        finder          = view.finder();
    }

    //~ Methods ......................................................................................................................................

    /**
     * Bind {@link ModelExpressionsEvaluator expressions evaluator} and
     * {@link OnChangeHandler change handler} and compute widget expressions.
     */
    public void bindAndCompute(@NotNull final WidgetDefUI ui, @NotNull final WidgetDefModel model, @NotNull final OnChangeNotifications pendings) {
        final ClientOnChangeHandler handler = new ClientOnChangeHandler(ui, pendings);

        final ModelExpressionsEvaluator evaluator = new ModelExpressionsEvaluator(model, createContext(pendings, ui));
        evaluator.with(new UiExpressionsEvaluator(ui.finder())).with(handler);

        evaluator.startListening();

        evaluator.compute();

        evaluator.stopListening();
    }

    /** Reevaluates save buttons expressions as they can change with deprecation status. */
    public void deprecated(final boolean deprecated) {
        new ModelUiVisitor() {
                @Override public void visit(WidgetUI ui) {
                    ui.setReadOnly(isReadOnly(root, ui.getModel()) || deprecated);
                }

                @Override public void visit(MultipleUI ui) {
                    final MultipleModel multiple = root.getMultiple(ui.getMultipleModel());

                    for (int section = 0; section < min(multiple.size(), ui.getSectionsCount()); section++)
                        traverse(ui.getSection(section));
                }

                @Override public void visit(ToggleButtonUI toggle) {
                    if (toggle.isDeprecate()) toggle.setValue(deprecated);
                }
            }.visit(view);

        view.setDeprecated(deprecated);
    }

    /**
     * Reevaluates filter bits and bytes :) Should be performed after sync because we cannot assume
     * any precedence between filters and results.
     */
    public void filters() {
        final UiExpressionsEvaluator evaluator = new UiExpressionsEvaluator(finder);
        new ModelUiVisitor() {
                @Override public void visit(SectionUI section) {
                    final String filtering = section.getMultipleModel().getFiltering();
                    if (!isEmpty(filtering)) evaluator.dispatchFilteringChange(root, section, filtering);
                }
            }.visit(view);
    }

    /** Compute model and ui expressions of the new row and it's dependencies. */
    public void itemCreated(@NotNull final TableUI ui, final int item) {
        final ImmutableList<ModelUI> path = collectPath(ui.container());

        final OnChangeNotifications pendings = new OnChangeNotifications();
        final ModelBindings         binding  = bind(path, pendings);

        binding.startListening();

        binding.deepest().itemCreatedCompute(item, ui.getMultipleModel());

        binding.stopListening();

        pendings.fire(bus, root);
    }

    /** Evaluate the UI expressions of a map. */
    public void itemCreated(final MapUI mapUI, final int row) {
        new UiExpressionsEvaluator(finder).evaluateExpressions(mapUI.getMultipleModel(), root, mapUI.getSection(row), row);
    }

    /** Compute model and ui expressions of the deleted row dependencies. */
    public void itemDeleted(@NotNull final TableUI ui) {
        final ImmutableList<ModelUI> path = collectPath(ui.container());

        final OnChangeNotifications pendings = new OnChangeNotifications();
        final ModelBindings         binding  = bind(path, pendings);

        binding.startListening();

        binding.deepest().itemDeletedCompute(ui.getMultipleModel());

        binding.stopListening();

        pendings.fire(bus, root);
    }

    /** Evaluate the UI expressions of a table. */
    public void rangeChanged(final TableUI ui) {
        ui.toIndexed().mapping(root).ifPresent(local -> {
            final WidgetUIFinder f = ui.container().finder();
            new UiExpressionsEvaluator(f).evaluateExpressions(ui.getMultipleModel(), local, NONE);
        });
    }

    public void readOnly() {
        final ModelExpressionsEvaluator modelEvaluator = new ModelExpressionsEvaluator(root, getNoComputeUiModelContext());
        modelEvaluator.with(new UiExpressionsEvaluator(finder));
        modelEvaluator.compute();
    }  // end method readOnly

    /** Evaluate ALL the expressions that are triggered by the change of this ui field. */
    public void uiChanged(final HasValueUI ui) {
        final ImmutableList<ModelUI> path = collectPath(ui.container());

        final OnChangeNotifications pendings = new OnChangeNotifications();
        final ModelBindings         binding  = bind(path, pendings);

        binding.startListening();

        final Widget widget = ui.getModel();
        final Model  model  = resolveModel(binding.getLast(), widget, ui.getContext().getItem());

        final boolean changed = widget.isMultiple() ? model.setArray(widget, ((HasArrayValueUI) ui).getValues())
                                                    : model.set(widget, ((HasScalarValueUI) ui).getValue());

        binding.stopListening();

        if (changed) pendings.fire(bus, root);
    }

    /** Evaluate the UI expressions of a form. The model should already be computed on the server */
    public void uiLoad() {
        new UiLoadVisitor(finder, root).visit(view);
    }

    /** Evaluate UI after external subform model changed. */
    public void uiSubformUpdate(final SubformUI ui) {
        final ModelExpressionsEvaluator evaluator = new ModelExpressionsEvaluator(root, getNoComputeUiModelContext()).with(
                new UiExpressionsEvaluator(finder));
        evaluator.startListening();
        evaluator.onModelChange(ui.toIndexed());
        evaluator.stopListening();
    }

    /**
     * Creates an evaluator for the ui expressions and returns it. Used as a listener for the model
     * after a sync form the server.
     */
    public ModelExpressionsEvaluator uiSync() {
        final ModelExpressionsEvaluator evaluator = new ModelExpressionsEvaluator(root, getNoComputeUiModelContext());
        evaluator.with(new UiExpressionsEvaluator(finder)).with(new ValidationExpressionsEvaluator(createListener()));
        return evaluator;
    }

    public void uiSync(@NotNull final Runnable sync) {
        final UiSyncBindingVisitor visitor = new UiSyncBindingVisitor(root);
        visitor.visit(view);

        visitor.forEachEvaluator(ModelExpressionsEvaluator::startListening);
        sync.run();
        visitor.forEachEvaluator(ModelExpressionsEvaluator::stopListening);
    }

    /**
     * Evaluate ALL the expressions of the group passed, if none is passed it will evaluate all the
     * form and returns true if all the validations passed.
     */
    public ValidationResult validate(@NotNull Option<Widget> groupWidget) {
        final ModelUIValidationListener listener = createListener();

        final ValidationExpressionsEvaluator evaluator     = new ValidationExpressionsEvaluator(listener);
        final UiValidations                  uiValidations = new UiValidations(listener, finder);

        final Iterable<Widget> formElements = groupWidget.map(this::getAllChilren)  //
                                              .orElseGet(() -> root.metadata().getDescendants());

        for (final Widget child : formElements) {
            if (child instanceof MultipleWidget) {
                final MultipleWidget multipleWidget = (MultipleWidget) child;
                evaluator.evaluateExpressions(multipleWidget, root, NONE);
                uiValidations.evaluateExpressions(multipleWidget, root);
            }
            else if (child.getMultiple().isEmpty()) {  //
                finder.byWidget(child).ifPresent(w -> {
                    evaluator.evaluateExpressions(child, root, empty());
                    uiValidations.evaluateExpressions(w, root);
                });
            }
        }

        return listener.getResult();
    }  // end method validate

    /** Evaluate all the expressions of a row and returns true if all the validations passed. */
    public ValidationResult validateRow(MultipleWidget widget, RowModel row, int i) {
        final ModelUIValidationListener      listener  = createListener();
        final ValidationExpressionsEvaluator evaluator = new ValidationExpressionsEvaluator(listener);

        for (final Widget column : widget)
            evaluator.evaluateExpressions(column, row, item(widget, i));

        return listener.getResult();
    }

    /** Create any missing optional widget definitions view. */
    public void widgets() {
        new UiWidgetDefVisitor(controller, finder, root).visit(view);
    }

    private StrBuilder appendPathPart(@NotNull StrBuilder builder, WidgetUI ui) {
        return builder.appendElement(ui.getModel().getName() + ui.getContext().getItem().map(i -> "#" + i).orElse(""));
    }

    /** Bind {@link ModelUI ui models}. */
    private ModelBindings bind(@NotNull final ImmutableList<ModelUI> path, @NotNull final OnChangeNotifications pendings) {
        final ModelBindings result = new ModelBindings(root);

        path.forEach(m -> {
            final WidgetUIFinder        f       = m.finder();
            final ClientOnChangeHandler handler = new ClientOnChangeHandler(m, pendings);

            final ModelExpressionsEvaluator evaluator = new ModelExpressionsEvaluator(result.next(m), createContext(pendings, m));
            evaluator.with(new UiExpressionsEvaluator(f)).with(handler);

            final ValidationExpressionsEvaluator validation = new ValidationExpressionsEvaluator(new ModelUIValidationListener(f));
            validation.addValidationListener(handler);
            evaluator.with(validation);

            result.addEvaluator(evaluator);
        });

        return result;
    }

    /** Collect top-down {@link ModelUI models} to specified container. */
    private ImmutableList<ModelUI> collectPath(@NotNull final ModelUI container) {
        final Builder<ModelUI> path = builder();
        collectPath(container, path);
        return path.build();
    }

    private void collectPath(@NotNull final ModelUI container, @NotNull final Builder<ModelUI> collector) {
        container.parent().ifPresent(p -> collectPath(p.value(), collector));
        collector.add(container);
    }

    @NotNull private UiModelContext createContext(@NotNull OnChangeNotifications pendings, @NotNull ModelUI m) {
        return createClientContext(this, m, controller, pendings);
    }

    private ModelUIValidationListener createListener() {
        return new ModelUIValidationListener(finder);
    }

    private Option<ItemContext> mapContext(@NotNull Context context) {
        return context.getItem().map(item -> createItemContext(context.getMultiple().get().getMultipleModel(), item));
    }

    private Collection<Widget> getAllChilren(Widget widget) {
        final List<Widget> all = new ArrayList<>();
        for (final Widget inner : widget) {
            if (inner.getWidgetType().isGroup()) all.addAll(getAllChilren(inner));
            else all.add(inner);
        }
        return all;
    }

    //~ Methods ......................................................................................................................................

    static boolean isErrorType(final CheckMsg checkMsg) {
        return checkMsg != null && checkMsg.getType() == CheckType.ERROR;
    }

    //~ Inner Classes ................................................................................................................................

    private static class ClientOnChangeHandler implements OnChangeHandler, ValidationListener {
        private final ModelUI               container;
        private final OnChangeNotifications pendings;

        private ClientOnChangeHandler(ModelUI container, OnChangeNotifications pendings) {
            this.container = container;
            this.pendings  = pendings;
        }

        @Override public void handleOnChange(@NotNull Widget widget, @NotNull Option<ItemContext> context) {
            if (widget.hasOnChangeMethod() || isNotEmpty(widget.getOnUiChangeMethodName())) {
                final IndexedWidget indexed = container.indexed(widget, context.map(ItemContext::getItem));
                pendings.add(indexed.toSourceWidget());
            }
        }

        @Override public void onValidation(Widget widget, Model model, Option<Integer> item, Option<String> subformPath, Seq<CheckMsg> messages) {
            if (messages.exists(CheckMsg::isError)) {
                final IndexedWidget indexed = container.indexed(widget, item);
                pendings.remove(indexed.toSourceWidget());
            }
        }
    }

    public static class ValidationResult implements Iterable<ValidationResult.Error> {
        private final List<Error> errors = new ArrayList<>();

        @Override public Iterator<Error> iterator() {
            return errors.iterator();
        }

        /** Returns true if the validation is successful. */
        public boolean isValid() {
            return errors.isEmpty();
        }

        void add(final Iterable<CheckMsg> messages, final Widget widget, final Option<Integer> row, final Option<String> subformPath) {
            errors.add(new Error(messages, widget, row, subformPath));
        }

        public static class Error {
            private final Seq<CheckMsg>   msg;
            private final Option<Integer> row;
            private Option<String>        subformPath;
            private final Widget          widget;

            private Error(final Iterable<CheckMsg> msg, final Widget widget, final Option<Integer> row, final Option<String> subformPath) {
                this.msg         = seq(msg);
                this.widget      = widget;
                this.row         = row;
                this.subformPath = subformPath;
            }

            /** Plain the error to a common one. */
            public Error plain() {
                subformPath = empty();
                return this;
            }

            /** Returns all the messages for this widget. */
            public Seq<CheckMsg> getMsg() {
                return msg;
            }
            /** Returns the row where the error is. */
            public Option<Integer> getRow() {
                return row;
            }
            /** Returns the subform path. */
            public Option<String> getSubformPath() {
                return subformPath;
            }
            /** Returns the widget that has a validation error. */
            public Widget getWidget() {
                return widget;
            }
        }
    }  // end class ValidationResult
}  // end class ExpressionEvaluatorFactory
