
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.LocalWidget;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetTypes;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.metadata.form.widget.Widget.ElemType.MULTIPLE;

/**
 * Model values for a {@link UiModel ui models}.
 */
public abstract class UiModelBase<T extends UiModel> extends Model {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final MultipleModel[] multiples;

    @Nullable private LocalWidget item     = null;
    @Nullable private LocalWidget lastItem = null;

    @NotNull private final transient List<ChangeListener> listeners;

    @Nullable private transient T                        metamodel;
    @NotNull private final transient Option<ParentModel> parent;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    UiModelBase(@Nullable T metamodel, @Nullable ParentModel parent, int fieldDimension, int multipleDimension, int optionsDimension,
                int subformDimension, int widgetDefDimension, int configureDimension) {
        super(fieldDimension, optionsDimension, subformDimension, widgetDefDimension, configureDimension);
        this.metamodel = metamodel;
        this.parent    = ofNullable(parent);
        multiples      = new MultipleModel[multipleDimension];
        listeners      = new ArrayList<>();
        if (metamodel != null) populateTables();
    }

    //~ Methods ......................................................................................................................................

    /** Adds a ChangeListener to the list of listeners. */
    public void addValueChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    /** Returns true if the given ChangeListener is contained in the list of listeners. */
    public boolean hasValueChangeListener(ChangeListener listener) {
        return listeners.contains(listener);
    }

    /** Associates {@link UiModel model} metamodel. */
    public UiModelBase<T> init(@NotNull final T m) {
        metamodel = m;
        return this;
    }

    /** Mark the elements as init. this should be called before posting the model */
    @Override public void markInitElements() {
        super.markInitElements();

        for (final MultipleModel multiple : multiples) {
            for (final RowModel r : multiple)
                r.markInitElements();
        }
    }

    /** Returns {@link UiModel metamodel}. */
    @NotNull @Override public T metadata() {
        return ensureNotNull(metamodel, "Should call init(metamodel) method first!");
    }

    /** Returns optional {@link ParentModel parent} model. */
    @NotNull @Override public final Option<ParentModel> parent() {
        return parent;
    }

    /** Remove the ChangeListener from the list of listeners. */
    public void removeValueChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    /** Returns {@link FormModel root} model. */
    @NotNull public abstract FormModel root();

    @NotNull @Override public Option<Integer> section() {
        return empty();
    }

    /** Update current (local) item. */
    public final void updateRow(@NotNull LocalWidget next) {
        if (next.equals(item)) return;

        final LocalWidget tmp = item;

        item     = null;
        lastItem = null;

        next.getMultipleWidget(metadata()).ifPresent(incoming -> {
            ofNullable(tmp).flatMap(l -> l.getMultipleWidget(metadata())).ifPresent(actual -> {
                if (actual.getName().equals(incoming.getName())) lastItem = tmp;
            });
            item = next;
        });
    }

    /** Get current item on model. */
    @Nullable public final LocalWidget getCurrentItem() {
        return item;
    }

    @Override public boolean isServerSidePristine() {
        boolean result = super.isServerSidePristine();
        for (int i = 0; result && i < multiples.length; i++) {
            final MultipleModel multiple = multiples[i];
            result = multiple.isServerSidePristine();
        }
        return result;
    }

    /** Get previous item on model. */
    @Nullable public final LocalWidget getLastItem() {
        return lastItem;
    }

    @Override public MultipleModel getMultiple(@NotNull MultipleWidget field) {
        checkType(field, MULTIPLE);
        return multiples[field.getFieldSlot()];
    }

    /** Returns true if metamodel has already been initialized. */
    public boolean isMetaModelInit() {
        return metamodel != null;
    }

    @NotNull @Override public UiModel getUiModel() {
        return metadata();
    }

    @NotNull @Override UiModelBase<?> container() {
        return this;
    }

    @Override void copyTo(Model model) {
        super.copyTo(model);

        final UiModelBase<?> copy = (UiModelBase<?>) model;

        copy.item     = item;
        copy.lastItem = lastItem;

        for (int i = 0; i < multiples.length; i++)
            multiples[i].copyTo(copy.multiples[i]);
    }

    @Override void deserialize(StreamReader r, boolean full) {
        item     = LocalWidget.deserialize(r);
        lastItem = LocalWidget.deserialize(r);

        super.deserialize(r, full);

        for (int i = 0; i < multiples.length; i++)
            multiples[i] = MultipleModel.instantiate(r, this).deserialize(r, full);
    }

    void notifyChangeListeners(@NotNull final IndexedWidget widget) {
        for (final ChangeListener listener : listeners)
            listener.onModelChange(widget);

        parent().ifPresent(parent -> parent.notifyModelChangeListeners(widget));
    }

    @Override void notifyDefinitionChange(@NotNull final IndexedWidget widget) {
        notifyDefinitionChangeListeners(widget);
    }

    void notifyDefinitionChangeListeners(@NotNull final IndexedWidget widget) {
        for (final ChangeListener listener : listeners)
            listener.onWidgetDefinitionChange(widget);

        parent().ifPresent(parent -> parent.notifyDefinitionChangeListeners(widget));
    }

    @Override void notifyModelChange(@NotNull final IndexedWidget widget) {
        if (isUserInteractionStarted() && !WidgetTypes.isGroup(widget.widget().getWidgetType())) root().setDirtyByUser();
        notifyChangeListeners(widget);
    }

    @Override void serialize(StreamWriter w, boolean full) {
        LocalWidget.serialize(w, item);
        LocalWidget.serialize(w, lastItem);

        super.serialize(w, full);

        for (final MultipleModel multiple : multiples)
            multiple.serialize(w, full);
    }

    @Override boolean sync(Model model) {
        boolean changed = super.sync(model);

        final UiModelBase<?> other = (UiModelBase<?>) model;
        item     = other.item;
        lastItem = other.lastItem;

        final Map<MultipleWidget, MultipleChanges> notifications = new LinkedHashMap<>();

        for (final MultipleModel multiple : multiples) {
            final MultipleWidget  multipleWidget = multiple.getMultipleWidget();
            final MultipleChanges changes        = multiple.sync(other.getMultiple(multipleWidget));
            if (changes.hasChanges()) {
                notifications.put(multipleWidget, changes);
                changed = true;
            }
        }

        // Notify changes after syncing all multiples
        for (final Map.Entry<MultipleWidget, MultipleChanges> multiple : notifications.entrySet())
            notifyChangeListeners(multiple.getKey(), multiple.getValue());

        return changed;
    }

    /** Returns true if user interaction has started. */
    boolean isUserInteractionStarted() {
        return root().isUserInteractionStarted();
    }

    private void notifyChangeListeners(MultipleWidget multipleWidget, MultipleChanges changes) {
        for (final ChangeListener listener : listeners)
            listener.onMultipleModelChange(multipleWidget, changes);
    }

    private void populateTables() {
        for (final Widget widget : metadata().getDescendants()) {
            if (widget instanceof MultipleWidget) {
                final MultipleWidget multiple = (MultipleWidget) widget;
                setMultiple(multiple, new MultipleModel(this, multiple));
            }
        }
    }

    /** Sets the multiple model for that field. */
    private void setMultiple(@NotNull final Widget field, final MultipleModel value) {
        multiples[field.getFieldSlot()] = value;
    }
}  // end class UiModelBase
