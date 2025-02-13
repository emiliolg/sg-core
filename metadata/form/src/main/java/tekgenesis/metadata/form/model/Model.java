
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.core.BitArray;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.Parentable;
import tekgenesis.metadata.form.configuration.WidgetConfig;
import tekgenesis.metadata.form.widget.ModelContainer;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.Widget.ElemType;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.model.KeyMap;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.util.GwtReplaceable.isClient;
import static tekgenesis.metadata.form.IndexedWidget.createIndexed;
import static tekgenesis.metadata.form.widget.ToggleButtonType.DEPRECATE;
import static tekgenesis.metadata.form.widget.Widget.ElemType.*;
import static tekgenesis.metadata.form.widget.WidgetType.TOGGLE_BUTTON;

/**
 * Abstract model representation.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass" })
public abstract class Model implements Parentable<Model> {

    //~ Instance Fields ..............................................................................................................................

    /** Configurations. */
    final WidgetConfig[] configurations;

    /** Options. */
    final KeyMap[] options;

    /** Subforms. */
    final FormModel[] subforms;

    /** Values (Has custom field serializable and assumes serializable values). */
    final Object[] values;

    /** Widget definitions. */
    final WidgetDefModel[] widgets;

    private BitArray       configurationsDirtyState;
    private BitArray       dirtyState;
    private final KeyMap[] images;

    private BitArray initState;

    /** Messages. */
    private final CheckMsg[]  messages;
    private BitArray          messagesDirtyState;
    private BitArray          optionsDirtyState;
    private String            path;
    private BitArray          resetState;
    private final FormModel[] subFormCopies;
    private long              timestamp;

    //~ Constructors .................................................................................................................................

    Model(int fieldDimension, int optionsDimension, int subformDimension, int widgetDefDimension, int configureDimension) {
        subforms                 = new FormModel[subformDimension];
        subFormCopies            = new FormModel[subformDimension];
        widgets                  = new WidgetDefModel[widgetDefDimension];
        options                  = new KeyMap[optionsDimension];
        images                   = new KeyMap[optionsDimension];
        values                   = new Object[fieldDimension];
        messages                 = new CheckMsg[fieldDimension];
        configurations           = new WidgetConfig[configureDimension];
        initState                = new BitArray(fieldDimension);
        dirtyState               = new BitArray(fieldDimension);
        optionsDirtyState        = new BitArray(optionsDimension);
        messagesDirtyState       = new BitArray(fieldDimension);
        configurationsDirtyState = new BitArray(configureDimension);
        resetState               = new BitArray(fieldDimension);
        path                     = "";
    }

    //~ Methods ......................................................................................................................................

    /** Backup a SubForm when opened. */
    public void backup(@NotNull final Widget field) {
        final int       subFormSlot = field.getSubformSlot();
        final FormModel origin      = subforms[subFormSlot];
        if (origin != null) subFormCopies[subFormSlot] = origin.copy();
    }

    /** Get the object for the given field name. */
    public Object get(@NotNull final String fieldName) {
        return get(widgetByName(fieldName));
    }

    /** Get the object for the given ordinal. */
    public Object get(final int ordinal) {
        return get(widgetByEnumOrdinal(ordinal));
    }

    /** Get the object for the given field. */
    public Object get(@NotNull final Widget field) {
        checkType(field, SCALAR);
        return values[field.getFieldSlot()];
    }

    /** Queries the given permission name over the permissions. */
    public boolean hasPermission(final String name) {
        return root().hasPermission(name);
    }

    /**
     * Return true if widget has value: scalar value should not be null or array value should not be
     * empty.
     */
    public boolean hasValue(@NotNull final Widget field) {
        return field.isMultiple() ? !isEmpty(getArray(field)) : get(field) != null;
    }

    /** Mark the elements as init. this should be called before posting the model */
    public void markInitElements() {
        initState.setAll(true);

        for (final FormModel model : subforms) {
            if (model != null) model.markInitElements();
        }

        for (final WidgetDefModel model : widgets) {
            if (model != null) model.markInitElements();
        }
    }

    /** Returns optional {@link ParentModel parent} model. */
    @NotNull public abstract Option<ParentModel> parent();

    /**
     * Resets init state to false, also marks widget as reset and after setting 'null' on purposely
     * reset its dirty state to false. Model expression should then be evaluated on the fired change
     * event to set the default value.
     */
    public void reset(@NotNull final Widget field) {
        final int slot = field.getFieldSlot();
        resetState.set(slot, true);
        initState.set(slot, false);

        if (field.isMultiple()) setArray(field, emptyIterable(), false);
        else set(field, null, false);

        dirtyState.set(slot, false);
    }

    /** Triggers a reset and immediately turns off the reset state finishing the reset cycle. */
    public void resetInternal(@NotNull final Widget field) {
        reset(field);
        resetState.set(field.getFieldSlot(), false);
    }

    /** Restore a subform when cancelled. */
    @SuppressWarnings("AssignmentToNull")
    public void restore(@NotNull Widget field) {
        final int subFormSlot = field.getSubformSlot();
        subforms[subFormSlot]      = subFormCopies[subFormSlot];
        subFormCopies[subFormSlot] = null;
    }

    /** Return optional model section. */
    @NotNull public abstract Option<Integer> section();

    /** Sets the value for that field and returns true if the value has changed. */
    public boolean set(@NotNull final Widget field, @Nullable final Object value) {
        return set(field, value, true);
    }

    /**
     * Sets the value for that field.
     *
     * @param   init  activates the init state of the widget
     *
     * @return  true if the value has changed
     */
    public boolean set(@NotNull final Widget field, @Nullable final Object value, boolean init) {
        final boolean changed = !equal(get(field), value);
        if (changed) setValue(field, value, init);
        return changed;
    }

    /** Find metamodel widget given enum ordinal. */
    public abstract Widget widgetByEnumOrdinal(int fieldOrdinal);

    /** Find metamodel widget given field slot. */
    public Widget widgetByFieldSlot(int slot) {
        return metadata().getWidgetByFieldSlot(slot);
    }

    /** Find metamodel widget given field name. */
    public abstract Widget widgetByName(String name);

    /** Get the array values for the given field ordinal. */
    @NotNull public Iterable<?> getArray(final int ordinal) {
        return getArray(widgetByEnumOrdinal(ordinal));
    }

    /** Get the values of the specified array. */
    @NotNull public Iterable<Object> getArray(@NotNull final Widget field) {
        checkType(field, ARRAY);
        final Iterable<Object> array = cast(values[field.getFieldSlot()]);
        return notNull(array, emptyIterable());
    }

    /**
     * Set the value for given field array.
     *
     * @return  true if the array has changed
     */
    public boolean setArray(@NotNull final Widget field, @NotNull final Iterable<?> array) {
        return setArray(field, array, true);
    }

    /**
     * Set the value for given field array.
     *
     * @param   init  activates the init state of the widget
     *
     * @return  true if the array has changed
     */
    public boolean setArray(@NotNull final Widget field, @NotNull final Iterable<?> array, boolean init) {
        final boolean changed = !equalElements(getArray(field), array);
        if (changed) setValue(field, toList(array), init);
        return changed;
    }

    /** Get the values of the specified column. */
    @NotNull public Iterable<Object> getColumn(@NotNull final Widget field) {
        checkType(field, COLUMN);
        return getMultiple(field.getMultiple().get()).getColumn(field);
    }

    /** True if elem is defined on the model. */
    public boolean isDefined(@NotNull final Widget elem) {
        return !elem.getIsExpression().isNull() || elem.getWidgetType() == WidgetType.SUBFORM || initState.get(elem.getFieldSlot());
    }

    /** Returns true if this model represents a deprecated instance. */
    public abstract boolean isDeprecated();

    /** set dirty state. */
    public void setDirtyState(@NotNull final Widget field, boolean state) {
        dirtyState.set(field.getFieldSlot(), state);
    }

    /** Return true if values, options and messages are not modified on server. */
    public boolean isServerSidePristine() {
        return dirtyState.isEmpty() && optionsDirtyState.isEmpty() && messagesDirtyState.isEmpty() && resetState.isEmpty() &&
               configurationsDirtyState.isEmpty() && areSubformsServerSidePristine() && areWidgetDefsServerSidePristine();
    }

    /** Returns true for an update, false for a create. */
    public abstract boolean isUpdate();

    /** Gets the field configuration for that widget (if any, if not returns null). */
    @Nullable public WidgetConfig getFieldConfig(final Widget field) {
        final int slot = field.getConfigurationSlot();
        configurationsDirtyState.set(slot, true);
        return configurations[slot];
    }

    /** Sets field configuration. Gets the field with the given configuration slot. */
    public void setFieldConfig(final Widget widget, WidgetConfig config) {
        final int slot = widget.getConfigurationSlot();
        configurations[slot] = config;
        configurationsDirtyState.set(slot, true);
    }

    /** Gets the field message for that widget (if any, if not returns null). */
    @Nullable public CheckMsg getFieldMsg(final Widget field) {
        return messages[field.getFieldSlot()];
    }

    /** Sets a msg to the field. Gets the field with the given ordinal. */
    public void setFieldMsg(final int ordinal, CheckMsg msg) {
        final Widget widget = widgetByEnumOrdinal(ordinal);
        final int    slot   = widget.getFieldSlot();
        messages[slot] = msg;
        messagesDirtyState.set(slot, true);
    }

    /** Set intended new focus (from user code). */
    public final void setFocus(@NotNull final Widget field) {
        final IndexedWidget indexed = indexed(field);
        root().setIntendedFocus(indexed.toSourceWidget());
    }

    /** Return images. */
    public KeyMap getImages(@NotNull final Widget field) {
        checkType(field, SCALAR, ARRAY, COLUMN);
        final KeyMap opts = images[field.getGlobalOptionsSlot()];
        return opts == null ? KeyMap.EMPTY : opts;
    }

    /** Sets the images for a given field. */
    @SuppressWarnings("UnusedReturnValue")
    public boolean setImages(@NotNull final Widget field, final KeyMap is) {
        return setImages(is, field.getGlobalOptionsSlot());
    }

    /** Gets the multiple model for that field. Finds the field with the given name. */
    public MultipleModel getMultiple(@NotNull final String multipleName) {
        return getMultiple((MultipleWidget) widgetByName(multipleName));
    }

    /** Gets the multiple model for that field. Gets the field with the given ordinal. */
    public MultipleModel getMultiple(final int multipleOrdinal) {
        return getMultiple((MultipleWidget) widgetByEnumOrdinal(multipleOrdinal));
    }

    /** Gets the multiple model for given field. */
    public abstract MultipleModel getMultiple(@NotNull final MultipleWidget field);

    /** Gets the options for the given field. */
    public KeyMap getOptions(@NotNull final Widget field) {
        checkType(field, SCALAR, ARRAY, COLUMN);
        final KeyMap opts = options[field.getGlobalOptionsSlot()];
        return opts == null ? KeyMap.EMPTY : opts;
    }

    /** Sets the options for a given field. */
    @SuppressWarnings("UnusedReturnValue")
    public boolean setOptions(@NotNull final Widget field, final KeyMap opts) {
        return setOptions(opts, field.getGlobalOptionsSlot());
    }

    /** Sets the options for a field. */
    public void setOptions(final int ordinal, final KeyMap opts) {
        setOptions(widgetByEnumOrdinal(ordinal), opts);
    }

    /** Returns this model path. */
    public String getPath() {
        return path;
    }

    /** Sets this model path. */
    public void setPath(String path) {
        this.path = path;
    }

    /** Get subform model for given field. */
    @Nullable public FormModel getSubform(@NotNull final Widget field) {
        checkType(field, SUBFORM);
        return subforms[field.getSubformSlot()];
    }

    /** Get subform model for given field ordinal. Retrieve field given its ordinal. */
    @Nullable public FormModel getSubform(final int subFormOrdinal) {
        return getSubform(widgetByEnumOrdinal(subFormOrdinal));
    }

    /** Sets the model of a SubForm for that field. Gets the field with the given ordinal. */
    public void setSubform(final int subFormOrdinal, final FormModel model) {
        setSubform(widgetByEnumOrdinal(subFormOrdinal), model);
    }

    /** Set the model of a subform. */
    public void setSubform(@NotNull final Widget field, final FormModel model) {
        subforms[field.getSubformSlot()] = model;
        initState.set(field.getFieldSlot(), true);

        final IndexedWidget indexed = indexed(field);
        // noinspection ConstantConditions
        if (!isClient()) model.addValueChangeListener(new ChangeListener() {
                @Override public void onModelChange(@NotNull IndexedWidget w) {
                    notifyModelChange(indexed);}
                @Override public void onMultipleModelChange(@NotNull MultipleWidget w, @NotNull MultipleChanges idx) {
                    notifyModelChange(indexed);}
                @Override public void onWidgetDefinitionChange(@NotNull IndexedWidget w) {}
            });

        notifyModelChange(indexed);
    }

    /** Returns true if the element has been init. */
    public boolean isInit() {
        return !initState.isEmpty();
    }

    /** Returns true if the element has been reset. */
    public boolean isReset(int field) {
        return resetState.get(field);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /** Sets a value for a field. */
    public void setValue(final int ordinal, final Object value) {
        set(widgetByEnumOrdinal(ordinal), value);
    }

    /** Get widget definition model for given field. */
    @Nullable public WidgetDefModel getWidgetDef(@NotNull final Widget field) {
        checkType(field, WIDGET);
        return widgets[field.getWidgetDefSlot()];
    }

    /** Get widget definition model for given field ordinal. Retrieve field given its ordinal. */
    @Nullable public WidgetDefModel getWidgetDef(final int ordinal) {
        return getWidgetDef(widgetByEnumOrdinal(ordinal));
    }

    /** Sets the model of a widget definition. */
    public void setWidgetDef(@NotNull final Widget field, final WidgetDefModel model) {
        widgets[field.getWidgetDefSlot()] = model;
        initState.set(field.getFieldSlot(), true);
        notifyModelChange(indexed(field));
    }

    /** Returns true if the model is dirty. */
    public boolean isDirty() {
        return !dirtyState.isEmpty();
    }

    /** Returns true is model is read-only. */
    public abstract boolean isReadOnly();

    /** Returns {@link UiModelBase container} model. */
    @NotNull abstract UiModelBase<?> container();

    /** Copy actual Model values to given Model copy. */
    void copyTo(Model copy) {
        copy.path = path;

        copy.initState = initState.copy();

        // Copy reset state
        copy.resetState = resetState.copy();

        // Copy dirty state
        copy.dirtyState = dirtyState.copy();

        // Copy options dirty state
        copy.optionsDirtyState = optionsDirtyState.copy();

        // Copy options dirty state
        copy.messagesDirtyState = messagesDirtyState.copy();

        // Copy configurations dirty state
        copy.configurationsDirtyState = configurationsDirtyState.copy();

        // Options copy
        arrayCopy(copy.options, options);

        // Options copy
        arrayCopy(copy.images, images);

        // Message copy
        arrayCopy(copy.messages, messages);

        // Configurations copy
        arrayCopy(copy.configurations, configurations);

        // Values copy
        arrayCopy(copy.values, values);

        // Subforms copy
        for (int i = 0; i < subforms.length; i++) {
            final FormModel model = subforms[i];
            if (model != null) copy.subforms[i] = model.copy();
        }

        // Widget definitions copy
        for (int i = 0; i < widgets.length; i++) {
            final WidgetDefModel model = widgets[i];
            if (model != null) copy.widgets[i] = model.copy();
        }
    }  // end method copyTo

    @SuppressWarnings("ConstantConditions")  // isClient is a gwt replaceable method
    void deserialize(final StreamReader r, boolean full) {
        path      = r.readString();
        timestamp = r.readLong();

        initState = BitArray.initialize(r);

        final boolean client = isClient();
        if (client || full) {
            // Deserialize reset state
            resetState = BitArray.initialize(r);

            // Deserialize dirty state
            dirtyState = BitArray.initialize(r);

            // Deserialize options dirty state
            messagesDirtyState = BitArray.initialize(r);

            // Deserialize configurations dirty state
            configurationsDirtyState = BitArray.initialize(r);

            // Deserialize options dirty state
            optionsDirtyState = BitArray.initialize(r);

            // Options deserialization
            deserialize(r, images, KeyMap::instantiate);

            // Message deserialization
            deserialize(r, messages, CheckMsg::instantiate);
        }

        // Options deserialization
        deserialize(r, options, KeyMap::instantiate);

        // Configurations deserialization
        deserialize(r, configurations, WidgetConfig::instantiate);

        // Values deserialization
        for (int i = 0; i < values.length; i++)
            if (full || !client || dirtyState.get(i)) values[i] = r.readObjectConst();

        // Subforms deserialization
        for (int i = 0; i < subforms.length; i++) {
            if (!r.readBoolean()) {
                final FormModel model = FormModel.instantiate(r);
                model.deserialize(r, full);
                subforms[i] = model;
            }
        }

        // Widget definitions deserialization
        for (int i = 0; i < widgets.length; i++) {
            if (!r.readBoolean()) {
                final WidgetDefModel model = WidgetDefModel.instantiate(r, container(), section());
                model.deserialize(r, full);
                widgets[i] = model;
            }
        }
    }  // end method deserialize

    /**
     * Mark the elements as dirty. this should be called if you want to force a sync of the model
     */
    void markDirtyElements() {
        dirtyState.setAll(true);
    }

    /** Returns {@link ModelContainer metadata}. */
    @NotNull abstract ModelContainer metadata();

    /** Notify {@link Widget widget} definition changed. */
    abstract void notifyDefinitionChange(@NotNull final IndexedWidget field);

    /** Notify {@link Widget widget} model changed. */
    abstract void notifyModelChange(@NotNull final IndexedWidget field);

    /** Returns {@link FormModel root} model. */
    @NotNull abstract FormModel root();

    @SuppressWarnings("ConstantConditions")
    void serialize(StreamWriter w, boolean full) {
        w.writeString(path);
        w.writeLong(timestamp);

        initState.serialize(w);

        final boolean client = isClient();
        if (!client || full) {  // Running on Server
            // Serialize reset state
            resetState.serialize(w);

            // Serialize dirty state
            dirtyState.serialize(w);

            // Serialize messages dirty state
            messagesDirtyState.serialize(w);

            // Serialize configuration dirty state
            configurationsDirtyState.serialize(w);

            // Serialize options dirty state
            optionsDirtyState.serialize(w);

            // Images serialization
            serialize(w, images, KeyMap::serialize);

            // Messages serialization
            serialize(w, messages, CheckMsg::serialize);
        }

        // Options serialization
        serialize(w, options, KeyMap::serialize);

        // Configurations serialization
        serialize(w, configurations, WidgetConfig::serialize);

        // Field Values serialization
        for (int i = 0; i < values.length; i++)
            // todo we should use the form metadata instead of getting the type from the object!!!!
            if (full || client || dirtyState.get(i)) w.writeObjectConst(values[i]);

        // Subforms serialization
        for (final FormModel model : subforms) {
            final boolean isNull = model == null;
            w.writeBoolean(isNull);
            if (!isNull) model.serialize(w, full);
        }

        // Widget definitions serialization
        for (final WidgetDefModel model : widgets) {
            final boolean isNull = model == null;
            w.writeBoolean(isNull);
            if (!isNull) model.serialize(w, full);
        }
    }  // end method serialize

    /** Sync the dirty values of the passed model. Return true if any value has changed. */
    @SuppressWarnings({ "ConstantConditions", "OverlyLongMethod", "OverlyComplexMethod" })
    boolean sync(final Model model) {
        assert values.length == model.values.length : "Should be the same model, just a different instance";

        path      = model.path;
        timestamp = model.timestamp;

        boolean changed = false;

        // Sync widget definitions
        for (int i = 0; i < widgets.length; i++) {
            final WidgetDefModel actual = widgets[i];
            final WidgetDefModel other  = model.widgets[i];
            if (actual == null) {
                if (other != null) {
                    other.reparent(container());
                    setWidgetDef(widgetDefBySlot(i), other);
                    changed = true;
                }
            }
            else {
                if (other != null && !other.isServerSidePristine()) {
                    changed |= actual.sync(other);
                    notifyModelChange(indexed(widgetDefBySlot(i)));
                }  // todo pcolunga handle removal of widget definition ;)
            }
        }

        // Sync scalar values (may have dependencies on widget definitions)
        for (int i = 0; i < values.length; i++) {
            if (model.isReset(i)) resetInternal(widgetByFieldSlot(i));

            final Widget field = widgetByFieldSlot(i);
            if (model.isDirty(i)) {
                if (field.isMultiple()) changed = setArray(field, model.getArray(field)) || changed;
                else changed = set(field, model.get(field)) || changed;
            }
            else if (field.getWidgetType() == TOGGLE_BUTTON && field.getToggleButtonType() == DEPRECATE)
                changed = model.set(field, model.isDeprecated()) || changed;
        }

        final Set<IndexedWidget> notifications = new LinkedHashSet<>();

        if (!model.optionsDirtyState.isEmpty()) {
            for (int i = 0; i < options.length; i++) {
                if (model.optionsDirtyState.get(i)) {
                    options[i] = model.options[i];
                    notifications.add(indexed(widgetByOptionSlot(i)));
                }
            }
        }

        for (int i = 0; i < images.length; i++) {
            if (model.images[i] != null) {
                images[i] = model.images[i];
                notifications.add(indexed(widgetByOptionSlot(i)));
            }
        }

        // arraycopy(model.messages, 0, messages, 0, model.messages.length); changed to for loop to notify.
        if (!model.messagesDirtyState.isEmpty()) {
            for (int i = 0; i < model.messages.length; i++) {
                if (model.messagesDirtyState.get(i)) {
                    messages[i] = model.messages[i];
                    notifications.add(indexed(widgetByFieldSlot(i)));
                }
            }
        }

        if (!model.configurationsDirtyState.isEmpty()) {
            for (int i = 0; i < model.configurations.length; i++) {
                // if (model.configurations[i] != null && !equal(model.configurations[i], configurations[i])) {
                if (model.configurationsDirtyState.get(i)) {
                    configurations[i] = model.configurations[i];
                    notifications.add(indexed(widgetByConfigurationSlot(i)));
                }
            }
        }

        // Sync subforms
        for (int i = 0; i < subforms.length; i++) {
            final FormModel actual = subforms[i];
            final FormModel other  = model.subforms[i];
            if (actual == null) {
                if (other != null) {
                    setSubform(subformBySlot(i), other);
                    changed = true;
                }
            }
            else {
                if (other != null && !other.isServerSidePristine()) {
                    changed |= actual.sync(other);
                    notifyModelChange(indexed(subformBySlot(i)));
                }
            }
        }

        for (final IndexedWidget widget : notifications)
            notifyDefinitionChange(widget);

        return changed;
    }  // end method sync

    boolean setOptions(final KeyMap opts, final int slot) {
        options[slot] = opts;
        optionsDirtyState.set(slot, true);
        return true;
    }

    /**
     * Sets the value for that field.
     *
     * @param  init  activates the init state of the widget
     */
    void setValue(@NotNull final Widget field, @Nullable final Object value, boolean init) {
        final int slot = field.getFieldSlot();

        LOGGER.debug("SET(" + field.getName() + ")[" + slot + "] = " + value);

        values[slot] = value;

        // noinspection AssignmentToNull
        messages[slot] = null;

        dirtyState.set(slot, true);

        if (init) initState.set(slot, true);

        notifyModelChange(indexed(field));
    }

    private boolean areSubformsServerSidePristine() {
        boolean result = true;
        for (int i = 0; result && i < subforms.length; i++) {
            final FormModel model = subforms[i];
            if (model != null) result = model.isServerSidePristine();
        }
        return result;
    }

    private boolean areWidgetDefsServerSidePristine() {
        boolean result = true;
        for (int i = 0; result && i < widgets.length; i++) {
            final WidgetDefModel model = widgets[i];
            if (model != null) result = model.isServerSidePristine();
        }
        return result;
    }

    private void arrayCopy(Object[] copy, Object[] origin) {
        assert copy.length == origin.length;
        System.arraycopy(origin, 0, copy, 0, origin.length);
    }

    /** Common array deserialization function. */
    private <T> void deserialize(@NotNull final StreamReader r, @NotNull final T[] array, @NotNull final Deserializer<T> d) {
        final boolean empty = r.readBoolean();
        if (empty) Arrays.fill(array, null);
        else {
            for (int i = 0; i < array.length; i++)
                // noinspection AssignmentToNull
                array[i] = r.readBoolean() ? null : d.deserialize(r);
        }
    }  // end method deserialize

    /** Return widget as indexed. */
    private IndexedWidget indexed(@NotNull final Widget widget) {
        return createIndexed(widget, section());
    }

    /** Common array serialization function. */
    private <T> void serialize(@NotNull final StreamWriter w, @NotNull final T[] array, @NotNull final Serializer<T> s) {
        final boolean empty = isArrayEmpty(array);
        w.writeBoolean(empty);
        if (!empty) {
            for (final T value : array) {
                final boolean isNull = value == null;
                w.writeBoolean(isNull);
                if (!isNull) s.serialize(value, w);
            }
        }
    }

    /** Finds the subform in the form metamodel with the given slot. */
    private Widget subformBySlot(int slot) {
        return metadata().getSubformBySlot(slot);
    }

    /** Finds the meta model widget for the given configuration slot. */
    private Widget widgetByConfigurationSlot(int slot) {
        return metadata().getWidgetByConfigurationSlot(slot);
    }

    /** Finds the meta model widget for the given option slot. */
    private Widget widgetByOptionSlot(int slot) {
        return metadata().getWidgetByOptionSlot(slot);
    }

    /** Finds the widget definition in the form metamodel with the given slot. */
    private Widget widgetDefBySlot(int slot) {
        return metadata().getWidgetDefBySlot(slot);
    }

    private boolean setImages(final KeyMap is, final int slot) {
        images[slot] = is;
        return true;
    }

    private <T> boolean isArrayEmpty(@NotNull final T[] array) {
        for (final T t : array) {
            if (t != null) return false;
        }
        return true;
    }

    private boolean isDirty(int field) {
        return dirtyState.get(field) && widgetByFieldSlot(field).hasValue();
    }

    //~ Methods ......................................................................................................................................

    /** Resolve {@link Model model} given {@link Widget widget} and optional row. */
    public static Model resolveModel(@NotNull final Model model, @NotNull final Widget widget, @NotNull final Option<Integer> item) {
        return widget.getMultiple().map(model::getMultiple).flatMap(mm -> item.map(r -> (Model) mm.getRow(r))).orElse(model);
    }

    static void checkType(Widget field, ElemType... expected) {
        for (final ElemType type : expected)
            if (field.getElemType() == type) return;

        throw new IllegalArgumentException(
            "Expected " + Arrays.toString(expected) + " but found " + field.getElemType() + " on field '" + field.getName() +
            "' (this usually happens when referencing columns on scalar expressions or viceversa)");
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(Model.class);

    //~ Inner Interfaces .............................................................................................................................

    private interface Deserializer<T> {
        T deserialize(@NotNull final StreamReader r);
    }

    private interface Serializer<T> {
        void serialize(@NotNull final T object, @NotNull final StreamWriter w);
    }
}  // end class MetaModel
