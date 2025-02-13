
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.*;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.expr.Expression;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.configuration.WidgetConfigurations;
import tekgenesis.metadata.form.model.*;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.model.KeyMap;
import tekgenesis.model.TreeNode;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.HasChildren;
import tekgenesis.persistence.HasImage;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.core.Tuple.tuple2;
import static tekgenesis.form.FormUtils.mapSetValue;
import static tekgenesis.form.FormsImpl.bindWidgetInstance;
import static tekgenesis.form.FormsImpl.formInstance;
import static tekgenesis.form.ServerUiModelRetriever.getRetriever;
import static tekgenesis.metadata.form.exprs.Expressions.evaluate;
import static tekgenesis.metadata.form.widget.WidgetTypes.isShowingEntity;
import static tekgenesis.model.KeyMap.singleton;

/**
 * Implements {@link UiModelAccessor model accessor}.
 */
class UiModelAccessorImpl implements UiModelAccessor {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Map<FormFieldRef, Object> cache;

    @NotNull private final UiModelBase<?>             container;
    @NotNull private final WidgetDefineMethodInvoker  invoker;
    @NotNull private final Model                      local;
    @NotNull private final ReflectedFormInstance      root;

    //~ Constructors .................................................................................................................................

    UiModelAccessorImpl(@NotNull ReflectedFormInstance root, @NotNull WidgetDefineMethodInvoker invoker, @NotNull UiModelBase<?> container) {
        this(root, invoker, container, container);
    }

    UiModelAccessorImpl(@NotNull ReflectedFormInstance root, @NotNull WidgetDefineMethodInvoker invoker, @NotNull UiModelBase<?> container,
                        @NotNull Model local) {
        this.root      = root;
        this.invoker   = invoker;
        this.container = container;
        this.local     = local;
        cache          = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public <T extends WidgetConfiguration> T config(@NotNull FormFieldRef field) {
        return WidgetConfigurations.create(local, ordinal(field));
    }

    @Override public boolean defined(@NotNull final FormFieldRef... fs) {
        for (final FormFieldRef f : fs) {
            if (notDefined(getWidget(f), widgetBelongsToForm(f) ? container : local)) return false;
        }
        return true;
    }

    @Override public void focus(@NotNull FormFieldRef f) {
        final Widget w = getWidget(f);
        if (widgetBelongsToForm(f)) container.setFocus(w);
        else local.setFocus(w);
    }

    @Override public <T> T get(@NotNull FormFieldRef field, Class<T> c) {
        return get(getWidget(field), c);
    }

    @NotNull @Override public <T extends FormInstance<?>> T init(@NotNull final FormFieldRef field, @NotNull final Class<T> clazz) {
        return init(field, clazz, null);
    }

    @NotNull @Override public <T extends FormInstance<?>> T init(@NotNull final FormFieldRef field, @NotNull final Class<T> clazz,
                                                                 @Nullable String pk) {
        final Tuple<T, FormModel> tuple = FormsImpl.init(clazz, pk);
        setSubform(field, tuple.second());
        return tuple.first();
    }

    @Override public String label(@NotNull FormFieldRef field) {
        final Widget     widget          = getWidget(field);
        final Expression labelExpression = widget.getLabelExpression();
        if (!labelExpression.isEmpty()) return cast(evaluate(new Evaluator(), local, labelExpression));
        return widget.getLabel();
    }

    @NotNull @Override public Message msg(@NotNull FormFieldRef field, @NotNull String msg) {
        return new MessageImpl(widgetBelongsToForm(field) ? container : local, ordinal(field), msg);
    }

    @NotNull @Override public <W extends WidgetInstance<?>> OptionalWidget<W> optionalWidget(@NotNull FormFieldRef field, @NotNull Class<W> clazz) {
        return cast(
            cache.computeIfAbsent(field,
                f -> {
                    final Widget w = getWidget(field);
                    return local.isDefined(w) ? new OptionalWidgetImpl<>(requiredWidget(clazz, f))
                                              : new OptionalWidgetImpl<>(widgetSupplier(field, clazz, w));
                }));
    }

    /** Set the field associated options. */
    @Override public void opts(@NotNull final FormFieldRef field, @NotNull final Iterable<?> items) {
        opts(field, asMap(items));
    }

    /** Set the field associated options. */
    public void opts(@NotNull final FormFieldRef field, @NotNull final KeyMap items) {
        local.setOptions(ordinal(field), mapKeyMap(items));
    }

    @Override public <T extends HasChildren<T>> void optsTree(@NotNull final FormFieldRef field, @NotNull final Iterable<? extends T> items) {
        optsTree(field, items, Comparator.comparing(Object::toString));
    }

    @Override public <T extends HasChildren<T>> void optsTree(@NotNull FormFieldRef field, @NotNull Iterable<? extends T> items,
                                                              @NotNull Comparator<? super T> comparator) {
        local.setOptions(ordinal(field), asTreeNodesMap(items, comparator));
    }

    @Override public void reset(@NotNull FormFieldRef... fs) {
        for (final FormFieldRef f : fs) {
            final Widget w = getWidget(f);
            if (widgetBelongsToForm(f)) container.reset(w);
            else local.reset(w);
        }
    }

    @NotNull @Override public ReflectedFormInstance root() {
        return root;
    }

    @Override public void set(@NotNull FormFieldRef field, @Nullable Object v) {
        set(getWidget(field), v);
    }

    @Nullable @Override public <T extends FormInstance<?>> T subform(@NotNull final FormFieldRef field, @NotNull final Class<T> clazz) {
        final FormModel subFormModel = getSubform(field);
        return subFormModel != null ? formInstance(clazz, subFormModel) : null;
    }

    @NotNull @Override public <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> row, @NotNull UiModelInstance enclosing) {
        return cast(cache.computeIfAbsent(field, f -> new FormTableImpl<>(this, container.getMultiple(ordinal(field)), row, enclosing)));
    }

    @Override public String title() {
        return container.metadata().getLabel();
    }

    @NotNull @Override public <W extends WidgetInstance<?>> W widget(@NotNull FormFieldRef field, @NotNull Class<W> clazz) {
        return cast(cache.computeIfAbsent(field, f -> requiredWidget(clazz, f)));
    }

    @NotNull @Override public <T> Seq<T> getArray(@NotNull final FormFieldRef field, final Class<T> c) {
        if (Enumeration.class.isAssignableFrom(c)) {
            final Iterable<String>  names        = values(field);
            final Class<ActionType> anyEnumClass = cast(c);  // Any Enum class just to fool generics
            return cast(seq(Enumerations.enumSet(anyEnumClass, names)));
        }
        if (EntityInstance.class.isAssignableFrom(c)) {
            final Iterable<String> pks = values(field);
            return map(pks, pk -> cast(EntityTable.forName(c.getName()).findByString(pk)));
        }
        final Iterable<T> vs = values(field);
        return seq(vs);
    }

    @Override public void setArray(@NotNull FormFieldRef field, @Nullable Iterable<?> v) {
        final Widget                widget = getWidget(field);
        final ImmutableList<Object> val    = map(v, FormUtils::mapSetValue).toList();
        checkOptional(widget, val, true);
        local.setArray(widget, val, true);

        if (v != null && isShowingEntity(widget.getWidgetType())) {
            final KeyMap options = KeyMap.create();
            final KeyMap images  = KeyMap.create();
            for (final Object i : v) {
                if (i instanceof EntityInstance) {
                    final String key = ((EntityInstance<?, ?>) i).keyAsString();
                    options.put(key, i.toString());
                    if (i instanceof HasImage && widget.hasImage()) images.put(key, ((HasImage) i).imagePath());
                }
            }

            if (!options.isEmpty()) local.setOptions(widget, options);
            if (!images.isEmpty()) local.setImages(widget, images);
        }
    }

    /** Get the wrapped container model. */
    @NotNull UiModelBase<?> container() {
        return container;
    }

    void set(@NotNull Widget widget, @Nullable Object v) {
        checkOptional(widget, v, true);
        final Object val = mapSetValue(v);

        if (isShowingEntity(widget.getWidgetType())) {
            if (v instanceof EntityInstance) {
                local.setOptions(widget, singleton(tuple(val, v.toString())));
                if (v instanceof HasImage && widget.hasImage()) local.setImages(widget, singleton(tuple(val, ((HasImage) v).imagePath())));
            }
            else if (v instanceof Suggestion) local.setOptions(widget, singleton(tuple(val, ((Suggestion) v).getReplacementString())));
            else if (widget.getType().isString() && v != null) local.setOptions(widget, singleton(tuple(val, v.toString())));
        }

        local.set(widget, val);
    }

    /** Get the wrapped model. */
    Model getContextModel() {
        return local;
    }

    @NotNull private KeyMap asMap(@NotNull final Iterable<?> items) {
        final KeyMap map = KeyMap.create();
        for (final Object value : items)
            map.put(getKey(value), getLabel(value));
        return map;
    }

    @NotNull private <T extends HasChildren<T>> KeyMap asTreeNodesMap(@NotNull final Iterable<? extends T> items,
                                                                      @NotNull final Comparator<? super T> comparator) {
        final Map<T, KeyMap> sorted = new TreeMap<>(comparator);
        for (final T item : items)
            sorted.put(item, asTreeNodesMap(item.children(), comparator));

        final KeyMap map;

        if (!sorted.isEmpty()) {
            map = KeyMap.create();
            for (final Map.Entry<T, KeyMap> entry : sorted.entrySet()) {
                final T item = entry.getKey();
                // Extracting to variable to not let the compiler infer type.
                final Object key = getKey(item);
                map.put(new TreeNode(String.valueOf(key), getLabel(item), mapToTreeNodes(entry.getValue().keySet())), "");
            }
        }
        else map = KeyMap.EMPTY;

        return map;
    }

    private void checkOptional(@NotNull final Widget widget, @Nullable final Object v, boolean isSetter) {
        if (v == null && widget.isRequired()) {
            final String msg = isSetter
                               ? "Attempting to set null '" + widget.getName() + "' required field."
                               : "Trying to access required Field '" + widget.getName() +
                                 "' before it is defined. You should use isDefined() to check for that situation";
            throw new IllegalArgumentException(msg);
        }
    }

    private <T> T get(final Widget widget, final Class<T> c) {
        final Object value = local.get(widget);
        checkOptional(widget, value, false);
        return c.cast(value);
    }

    /** Map KeyMap keys and values. */
    @NotNull private KeyMap mapKeyMap(@NotNull final KeyMap items) {
        final KeyMap map = KeyMap.create();
        for (final Map.Entry<Object, String> item : items)
            map.put(getKey(item.getKey()), isEmpty(item.getValue()) ? getLabel(item.getKey()) : item.getValue());
        return map;
    }

    private List<TreeNode> mapToTreeNodes(@NotNull Collection<Object> nodes) {
        final List<TreeNode> result = new ArrayList<>();
        for (final Object node : nodes)
            result.add(cast(node));
        return result;
    }

    private boolean modelEqualsMultipleModel(@NotNull final RowModel model, @NotNull final MultipleWidget multiple) {
        return multiple.getName().equals(model.getMultipleModel().getMultipleWidget().getName());
    }

    private boolean notDefined(final Widget w, final Model model) {
        return w.getWidgetType() == WidgetType.SUBFORM ? model.getSubform(w) == null : !model.hasValue(w);
    }

    @NotNull private <W extends WidgetInstance<?>> W optionalWidget(@NotNull FormFieldRef field, @NotNull Class<W> clazz, @NotNull Widget w) {
        final WidgetDef      def    = getRetriever().getWidget(createQName(w.getWidgetDefinitionFqn()));
        final W              widget = invoker.invokeDefine(field.id());
        final WidgetDefModel model  = new WidgetDefModel(def, container, w, local.section());
        local.setWidgetDef(w, model);
        bindWidgetInstance(root, clazz.getName(), widget, model, true);
        return widget;
    }

    private int ordinal(final FormFieldRef field) {
        return field.mapOrdinal(container.metadata());
    }

    @NotNull private <W extends WidgetInstance<?>> W requiredWidget(@NotNull Class<W> clazz, FormFieldRef f) {
        final W              widget = invoker.invokeDefine(f.id());
        final WidgetDefModel model  = ensureNotNull(local.getWidgetDef(ordinal(f)));
        bindWidgetInstance(root, clazz.getName(), widget, model, false);
        return widget;
    }

    private <T> Iterable<T> values(FormFieldRef field) {
        return cast(local.getArray(ordinal(field)));
    }

    private boolean widgetBelongsToForm(final FormFieldRef field) {
        final Option<MultipleWidget> multiple = getWidget(field).getMultiple();
        if (multiple.isPresent()) {
            if (local instanceof RowModel && modelEqualsMultipleModel((RowModel) local, multiple.get())) return false;
            else {
                final String msg = "Trying to access multiple field %s from " + (local == container ? "form context" : "a different row context");
                throw new IllegalArgumentException(format(msg, field));
            }
        }
        else return true;
    }

    @NotNull private <W extends WidgetInstance<?>> Supplier<W> widgetSupplier(@NotNull FormFieldRef field, @NotNull Class<W> clazz,
                                                                              @NotNull Widget w) {
        return () -> optionalWidget(field, clazz, w);
    }

    /** Should check item to be a known serializable type. */
    private Object getKey(final Object item) {
        return mapSetValue(item);
    }

    @Nullable private String getLabel(final Object item) {
        if (item instanceof Enumeration) return ((Enumeration<?, ?>) item).label();
        if (item instanceof EntityInstance<?, ?>) return item.toString();
        if (item instanceof DateOnly || item instanceof DateTime) return item.toString();
        return null;  // Leave formatting for widgets!
    }

    @Nullable private FormModel getSubform(@NotNull final FormFieldRef field) {
        return local.getSubform(ordinal(field));
    }

    private void setSubform(@NotNull FormFieldRef field, @NotNull FormModel subFormModel) {
        local.setSubform(ordinal(field), subFormModel);
    }

    private Widget getWidget(FormFieldRef field) {
        return container.widgetByEnumOrdinal(ordinal(field));
    }
}  // end class UiModelAccessorImpl
