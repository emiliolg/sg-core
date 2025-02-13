
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.exprs;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.form.*;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.LocalWidget;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.exprs.ItemContext;
import tekgenesis.metadata.form.exprs.ModelExpressionsEvaluator;
import tekgenesis.metadata.form.exprs.OnChangeHandler;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField;
import tekgenesis.transaction.Transaction;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.form.FormsImpl.bindWidgetInstance;
import static tekgenesis.form.FormsImpl.createUserFormInstance;
import static tekgenesis.form.ServerUiModelContext.createServerContext;
import static tekgenesis.form.ServerUiModelRetriever.getRetriever;
import static tekgenesis.metadata.form.IndexedWidget.createFromReference;
import static tekgenesis.metadata.form.MetadataFormMessages.MSGS;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.metadata.form.model.Model.resolveModel;

/**
 * Server Expressions Util.
 */
public class ServerExpressions {

    //~ Constructors .................................................................................................................................

    private ServerExpressions() {}

    //~ Methods ......................................................................................................................................

    /** Computes forms expressions. */
    public static ModelExpressionsEvaluator bindAndCompute(@NotNull final UiModelInstanceHandler instance) {
        final ModelExpressionsEvaluator evaluator = bindListener(instance);
        evaluator.compute();
        return evaluator;
    }

    /** Bind expressions listener. */
    @NotNull public static ModelExpressionsEvaluator bindListener(@NotNull final UiModelInstanceHandler handler) {
        final ModelExpressionsEvaluator evaluator = new ModelExpressionsEvaluator(handler.getModel(), createServerContext(handler)).with(
                new ServerOnChangeHandler(handler));
        evaluator.startListening();
        return evaluator;
    }

    /** Execute cancel termination method on instance. */
    public static Action execCancel(@NotNull final FormModel model, @NotNull final SourceWidget s) {
        return exec(model, listOf(s), (instance, source) -> instance.root().cancel());
    }

    /** Execute create or update termination method on instance. */
    public static Action execCreateOrUpdate(@NotNull final FormModel model, @NotNull final SourceWidget s) {
        return exec(model,
            listOf(s),
            (instance, source) -> {
                // Delegate create on parent
                final UiModelBase<?>        m    = instance.getModel();
                final ReflectedFormInstance root = instance.root();
                if (!m.isUpdate() || isEmpty(root.getForm().getBinding().getName())) return root.create();
                final Object found = root.find();
                return hasEntityChanged(model, found) ? entityChangedAction(found) : root.update();
            });
    }

    /** Execute delete termination method on instance. */
    public static Action execDelete(@NotNull final FormModel model, @NotNull final SourceWidget s) {
        return exec(model, listOf(s), (instance, source) -> instance.root().delete());
    }

    /** Execute deprecate method on instance. */
    public static Action execDeprecate(@NotNull final FormModel model, @NotNull final SourceWidget s, final boolean deprecate) {
        return exec(model,
            listOf(s),
            (instance, source) -> {
                final ReflectedFormInstance root    = instance.root();
                final Object                found   = root.find();
                final boolean               changed = hasEntityChanged(model, found);
                final Action                action  = changed ? entityChangedAction(found) : root.deprecate(deprecate);
                if (!changed) updateModelTimestamp(model, found);
                return action;
            });
    }

    /** Execute given function in context. */
    public static Action execFunction(final FormModel model, final SourceWidget s,
                                      final BiFunction<UiModelInstanceHandler, Option<IndexedWidget>, Action> function) {
        return exec(model, listOf(s), function::apply);
    }

    /** Execute lazy request. */
    public static Action execLazyFetch(final FormModel model, int offset, int limit, final SourceWidget s) {
        return exec(model,
            listOf(s),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                final Widget        w      = source.widget();
                return invokeUserMethod(instance, source.item(), w, w.getLazyFetchMethodName(), offset, limit);
            });
    }

    /** Execute abstract delegated methods of source widget. */
    public static Action execOnAbstract(final FormModel model, SourceWidget d, final SourceWidget source) {
        return exec(model,
            listOf(d),
            (instance, option) -> {
                final IndexedWidget   delegate    = option.get();
                final Widget          widget      = delegate.widget();
                final Option<Integer> item        = delegate.item();
                final Option<Model>   modelOption = widget.getMultiple().flatMap(m -> item.map(idx -> model.getMultiple(m).getRow(idx)));
                final Model           m           = modelOption.orElse(model);
                final FormModel       subform     = ensureNotNull(m.getSubform(widget));
                final String          method      = findAbstractMethodName(subform, widget.getSubformFqn(), source).getOrFail(
                        "Cannot find method name");
                return invokeUserMethod(instance, item, widget, method);
            });
    }

    /** Execute on blur method of the widget. */
    public static Action execOnBlur(final FormModel model, final SourceWidget s) {
        return exec(model,
            listOf(s),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                final Widget        widget = source.widget();
                return invokeUserMethod(instance, source.item(), widget, widget.getOnBlurMethodName());
            });
    }

    /** Execute on change methods of the widgets. */
    public static Action execOnChanges(final FormModel model, final List<SourceWidget> sources) {
        return exec(model,
            seq(sources),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                final Widget        widget = source.widget();

                final Function<FormInstance<?>, Action> onChangeMethodRef = cast(widget.getOnChangeMethodRef());
                if (onChangeMethodRef != null) return onChangeMethodRef.apply(instance.getExtendedInstance());

                final String method = notEmpty(widget.getOnChangeMethodName(), widget.getOnUiChangeMethodName());
                return invokeUserMethod(instance, source.item(), widget, method);
            });
    }

    /** Execute on click methods of the widget. */
    public static Action execOnClick(final FormModel model, final SourceWidget s, final Option<ExecutionFeedback> feedback) {
        return exec(model,
            listOf(s),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                final Widget        widget = source.widget();

                final Action result;

                final Function<FormInstance<?>, Action> onClickMethodRef = cast(widget.getOnClickMethodRef());
                if (onClickMethodRef != null) result = onClickMethodRef.apply(instance.getExtendedInstance());
                else if (isEmpty(widget.getOnClickMethodName())) {
                    // Exception for chart columns firing "delegate" on_click
                    final MultipleWidget chart      = widget.getMultiple().getOrFail("Expected chart column!");
                    final FormFieldRef   fieldsEnum = instance.getEnumFieldRef(widget);
                    result = instance.invokeUserMethod(chart.getOnClickMethodName(), fieldsEnum);
                }
                else if (feedback.isPresent())
                    result = invokeUserMethod(instance, source.item(), widget, widget.getOnClickMethodName(), feedback.get());
                else result = invokeUserMethod(instance, source.item(), widget, widget.getOnClickMethodName());

                return result;
            });
    }

    /** Execute on display form sync. */
    public static Action execOnDisplay(final FormModel model) {
        return exec(model, listOf(NONE), (instance, source) -> instance.invokeUserMethod(instance.root().getForm().getOnDisplayMethodName()));
    }

    /** Execute asynchronous method invocation. */
    public static Action execOnMethodInvocation(final String method, final FormModel model) {
        return exec(model, listOf(NONE), (instance, source) -> instance.invokeUserMethod(method));
    }

    /** Execute on new method of the widget. */
    public static Action execOnNew(@NotNull final FormModel model, @NotNull final SourceWidget s, final String text) {
        return exec(model,
            listOf(s),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                final Widget        widget = source.widget();
                return invokeUserMethod(instance, source.item(), widget, widget.getOnNewMethodName(), text);
            });
    }

    /** Execute on new location method of the widget. */
    public static Action execOnNewLocation(FormModel model, SourceWidget s, double lat, double lng) {
        return exec(model,
            listOf(s),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                final Widget        widget = source.widget();
                return invokeUserMethod(instance, source.item(), widget, widget.getOnNewLocationMethodName(), lat, lng);
            });
    }

    /** Execute on schedule form sync. */
    public static Action execOnSchedule(final FormModel model) {
        return exec(model, listOf(NONE), (instance, source) -> instance.invokeUserMethod(instance.root().getForm().getOnScheduleMethodName()));
    }

    /** Execute on select methods of a table. */
    public static Action execOnSelected(final FormModel model, final List<SourceWidget> sources) {
        return exec(model,
            seq(sources),
            (instance, option) -> {
                final IndexedWidget source = option.get();
                return instance.invokeUserMethod(source.widget().getOnSelectionMethodName());
            });
    }

    /** Invoke given method. */
    @Nullable public static <T> T invokeUserMethod(@NotNull UiModelInstanceHandler instance, @NotNull Option<Integer> item, @NotNull Widget widget,
                                                   @NotNull String method, Object... args) {
        final T multipleAction = invokeUserMethodInMultiple(instance, item, widget.getMultiple(), method, args);
        return multipleAction != null ? multipleAction : instance.invokeUserMethod(method, args);
    }

    /** Updates the FormModel's timestamp. */
    public static <T extends EntityInstance<T, K>, K> void updateModelTimestamp(@NotNull Model model, @Nullable Object found) {
        if (found instanceof EntityInstance) {
            final T instance = cast(found);

            if (instance.metadata().getUpdateTimeField() != null) {
                final DateTime updateTime = EntityTable.forTable(instance.table()).updateTime(instance.keyObject());
                if (updateTime != null) model.setTimestamp(updateTime.toMilliseconds());
            }
        }
    }

    /**
     * Return {@link UiModelInstanceHandler instance handler} for given
     * {@link IndexedWidget indexed widget}.
     */
    @NotNull public static UiModelInstanceHandler getUiModelInstanceHandler(FormModel model, ReflectedFormInstance root, IndexedWidget qualified) {
        return qualified.foldLeft((UiModelInstanceHandler) root,
            (previous, part) -> {
                final Widget widget = part.widget();

                // Update current widget
                previous.setCurrentWidget(widget);

                // Update current section
                final UiModelBase<?> p = previous.getModel();
                p.updateRow(part.toLocalWidget());

                if (widget.isWidgetDef()) {  // todo pcolunga MULTIPLE BINDINGS TO MODEL ON CHAINED ON_CHANGES
                    final WidgetDefModel            m       = ensureNotNull(resolveModel(p, widget, part.item()).getWidgetDef(widget));
                    final WidgetDefineMethodInvoker invoker = previous.resolveAnchorInstance(m.parent().get());
                    final WidgetInstance<?>         wi      = invoker.invokeDefine(widget.getName());
                    bindWidgetInstance(root, widget.getWidgetDefinitionFqn(), wi, m, false);
                    return new ReflectedWidgetDefInstance(root, wi);
                }

                return previous;
            });
    }

    @NotNull private static Option<IndexedWidget> asIndexedWidget(FormModel model, SourceWidget source) {
        return isEmpty(source.getPath()) ? empty() : createFromReference(getRetriever(), model.metadata(), source.getPath());
    }

    @NotNull private static Action entityChangedAction(Object found) {
        return ActionsImpl.getInstance().getError().withReloadMessage(MSGS.changedEntity(found.getClass().getSimpleName()));
    }

    private static Action exec(final FormModel model, final Seq<SourceWidget> sources, final MethodExecutor executor) {
        final ReflectedFormInstance root = (ReflectedFormInstance) createUserFormInstance(model);

        final ModelExpressionsEvaluator evaluator = bindListener(root);

        final Action initial = ActionsImpl.getInstance().getDefault();

        final Action result = sources.foldLeft(initial,
                (previous, source) -> {
                    if (previous.isError()) return previous;
                    final Option<IndexedWidget> indexed = asIndexedWidget(model, source);

                    final UiModelInstanceHandler instance =  //
                        indexed.map(qualified -> getUiModelInstanceHandler(model, root, qualified)).orElse(root);

                    return ensureNotNull(executor.execute(instance, indexed));
                });
        if (result.isError()) Transaction.getCurrent().ifPresent(Transaction::abort);

        evaluator.stopListening();

        return result;
    }  // end method exec

    private static Option<String> findAbstractMethodName(FormModel subform, String fqn, SourceWidget source) {
        return asIndexedWidget(subform, source).map(indexed -> {
            final Widget widget = indexed.widget();
            subform.updateRow(new LocalWidget(widget.getName(), indexed.item().getOrNull()));
            final Form form = FormUtils.findForm(fqn).orElseThrow(() -> new EntityInstanceNotFoundException(fqn));
            return notEmpty(widget.getOnChangeMethodName(),
                notEmpty(widget.getOnUiChangeMethodName(), notEmpty(widget.getOnBlurMethodName(), widget.getOnClickMethodName())));
        });
    }

    /** Returns true if entity has been changed. */
    private static <T extends EntityInstance<T, K>, K> boolean hasEntityChanged(@NotNull Model model, @Nullable Object found) {
        if (found instanceof EntityInstance && model.getTimestamp() > 0) {
            final T                instance    = cast(found);
            final TableField.DTime updateField = instance.metadata().getUpdateTimeField();

            return updateField != null &&
                   !EntityTable.forTable(instance.table()).checkAndLock(instance.keyObject(), DateTime.fromMilliseconds(model.getTimestamp()));
        }
        return false;
    }

    /** Invokes user method in multiple widget. */
    @Nullable private static <T> T invokeUserMethodInMultiple(UiModelInstanceHandler instance, Option<Integer> multipleIndex,
                                                              Option<MultipleWidget> multiple, String method, Object... args) {
        if (multiple.isPresent() && multipleIndex.isPresent()) {
            final ReflectedMultipleInstance tableInstance = instance.getMultipleInstance(multiple.get());
            return tableInstance.get(multipleIndex.get()).invoke(method, args);
        }
        return null;
    }

    private static Model model(@NotNull final Model model, @NotNull final Option<MultipleWidget> multiple, @NotNull final Option<Integer> item) {
        return multiple.<Model>map(m -> model.getMultiple(m).getRow(item.get())).orElse(model);
    }

    //~ Inner Interfaces .............................................................................................................................

    private interface MethodExecutor {
        @Nullable Action execute(@NotNull UiModelInstanceHandler instance, @NotNull Option<IndexedWidget> source);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Server {@link OnChangeHandler handler} with immediate method invocation.
     */
    private static class ServerOnChangeHandler implements OnChangeHandler {
        private final UiModelInstanceHandler handler;

        private ServerOnChangeHandler(@NotNull final UiModelInstanceHandler handler) {
            this.handler = handler;
        }

        @Override public void handleOnChange(@NotNull final Widget widget, @NotNull final Option<ItemContext> section) {
            if (isNotEmpty(widget.getOnChangeMethodName())) {
                final Option<Integer> s = section.map(ItemContext::getItem);
                handler.getModel().updateRow(new LocalWidget(widget.getName(), s.getOrNull()));
                invokeUserMethod(handler, s, widget, widget.getOnChangeMethodName());
            }
        }
    }
}  // end class ServerExpressions
