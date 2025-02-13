
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.BitArray;
import tekgenesis.common.core.Option;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.configuration.WidgetConfig;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.metadata.form.model.FilterData.bits;
import static tekgenesis.metadata.form.model.FilterData.data;
import static tekgenesis.metadata.form.widget.Widget.ElemType.COLUMN;

/**
 * Model values for row instances.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class RowModel extends Model {

    //~ Instance Fields ..............................................................................................................................

    /** Used for filtering, both ways. */
    private FilterData filter;

    private MultipleModel model;
    private boolean       rowDirtyByUser;
    private int           syncIndex;

    //~ Constructors .................................................................................................................................

    /** Constructs an empty row model. */
    public RowModel(MultipleModel multiple) {
        this(multiple,
            multiple.getMultipleWidget().getFieldDimension(),
            multiple.getMultipleWidget().getOptionsDimension(),
            multiple.getMultipleWidget().getSubformDimension(),
            multiple.getMultipleWidget().getWidgetDefDimension(),
            multiple.getMultipleWidget().getConfigureDimension());
    }

    RowModel(MultipleModel multiple, int multipleDimension, int optionsDimension, int subformDimension, int widgetDefDimension,
             int configureDimension) {
        super(multipleDimension, optionsDimension, subformDimension, widgetDefDimension, configureDimension);
        model     = multiple;
        syncIndex = -1;
        filter    = FilterData.NONE;
    }

    //~ Methods ......................................................................................................................................

    @Override public Object get(@NotNull Widget field) {
        // return the scalar column for a column, otherwise go to the parent form
        return field.getElemType() == COLUMN ? values[field.getFieldSlot()] : container().get(field);
    }

    @NotNull @Override public Option<ParentModel> parent() {
        return container().parent();
    }

    @NotNull @Override public Option<Integer> section() {
        return of(getRowIndex());
    }

    @Override public Widget widgetByEnumOrdinal(final int fieldOrdinal) {
        return container().widgetByEnumOrdinal(fieldOrdinal);
    }

    @Override public Widget widgetByName(final String name) {
        return container().widgetByName(name);
    }

    @NotNull @Override public Iterable<Object> getArray(@NotNull Widget field) {
        final Iterable<Object> result;
        if (field.getElemType() == COLUMN) {
            final Iterable<Object> v = cast(values[field.getFieldSlot()]);
            result = notNull(v, emptyIterable());
        }
        else result = container().getArray(field);
        return result;
    }

    /** Returns the cell value in the RowModel, using the field slot. */
    public Object getByFieldSlot(final int field) {
        return get(widgetByFieldSlot(field));
    }

    /** Set the cell value in the RowModel, using the field slot. */
    public void setByFieldSlot(final int field, Object value) {
        set(widgetByFieldSlot(field), value);
    }

    /** Get the cell configuration in the RowModel, using the field slot. */
    @Nullable public WidgetConfig getConfigByFieldSlot(final int field) {
        return getFieldConfig(widgetByFieldSlot(field));
    }

    /** Set the cell configuration in the RowModel, using the field slot. */
    public void setConfigByFieldSlot(final int field, final WidgetConfig config) {
        setFieldConfig(widgetByFieldSlot(field), config);
    }

    @Override public boolean isDeprecated() {
        return container().isDeprecated();
    }

    @Override public boolean isUpdate() {
        return container().isUpdate();
    }

    /** Get filtering bit array. */
    public BitArray getFilterBits() {
        return filter.getBits();
    }

    /** Set filter data to store given amount of bits. */
    public void setFilterBits(int bits) {
        filter = bits(bits);
    }

    /** Get filtering data. */
    public FilterData getFilterData() {
        return filter;
    }

    /** Set filter data to store given amount of bits and offset. */
    public void setFilterData(int bits, int offset, boolean exclusive) {
        filter = data(bits, offset, exclusive);
    }

    /** Get filter offset. */
    public int getFilterOffset() {
        return filter.getOffset();
    }

    /** Sets the local options for a row field. */
    public void setLocalOptions(final int ordinal, final KeyMap opts) {
        setOptions(widgetByEnumOrdinal(ordinal), opts);
    }

    @Override public MultipleModel getMultiple(@NotNull MultipleWidget field) {
        return container().getMultiple(field);
    }

    /** Returns parent multiple model. */
    public MultipleModel getMultipleModel() {
        return model;
    }

    @Override public KeyMap getOptions(@NotNull Widget field) {
        // return the local options for a column, otherwise go to the parent model and return the global options
        final KeyMap result = options[field.getLocalOptionsSlot()];
        return result != null ? result : container().getOptions(field);
    }

    /** Sets the local options for a row field. */
    @Override public boolean setOptions(@NotNull Widget field, KeyMap opts) {
        return setOptions(opts, field.getLocalOptionsSlot());
    }

    /** Set the cell options in the RowModel, using the field slot. */
    public void setOptionsByFieldSlot(final int field, KeyMap opts) {
        setOptions(widgetByFieldSlot(field), opts);
    }

    @Override public String getPath() {
        return model.getRowPath(getRowIndex());
    }

    /** Has the row been modified by user?. */
    public boolean isRowDirtyByUser() {
        return rowDirtyByUser;
    }

    /** Returns the index represented by this row model. */
    public int getRowIndex() {
        return model.getRowIndex(this);
    }

    @Nullable @Override public FormModel getSubform(@NotNull Widget field) {
        checkType(field, COLUMN);
        return subforms[field.getSubformSlot()];
    }

    @NotNull @Override public UiModel getUiModel() {
        return container().metadata();
    }

    @Nullable @Override public WidgetDefModel getWidgetDef(@NotNull Widget field) {
        checkType(field, COLUMN);
        return widgets[field.getSubformSlot()];
    }

    @Override public boolean isReadOnly() {
        return container().isReadOnly();
    }

    /** Returns table {@link UiModelBase container} model. */
    @NotNull @Override UiModelBase<?> container() {
        return model.container();
    }

    @Override void deserialize(StreamReader r, boolean full) {
        super.deserialize(r, full);
        filter = FilterData.initialize(r);
    }

    @NotNull @Override MultipleWidget metadata() {
        return model.getMultipleWidget();
    }

    @Override void notifyDefinitionChange(@NotNull final IndexedWidget widget) {
        container().notifyDefinitionChangeListeners(widget);
    }

    @Override void notifyModelChange(@NotNull final IndexedWidget field) {
        container().notifyChangeListeners(field);
    }

    /** Change current parent multiple model with given one. */
    RowModel reparent(MultipleModel multiple) {
        model = multiple;

        for (final WidgetDefModel widget : widgets)
            widget.reparent(multiple.container());

        return this;
    }

    @NotNull @Override FormModel root() {
        return container().root();
    }

    @Override void serialize(StreamWriter w, boolean full) {
        super.serialize(w, full);
        filter.serialize(w);
    }

    @Override boolean sync(Model other) {
        final boolean sync = super.sync(other);
        filter = ((RowModel) other).filter;  // Always 'sync' filter
        return sync;
    }

    /** Returns the index of the row before any change is made on the server and -1 for new rows. */
    int getSyncIndex() {
        return syncIndex;
    }

    /** Sets the index of the row before any change is made on the server. */
    void setSyncIndex(int syncIndex) {
        this.syncIndex = syncIndex;
    }

    @Override void setValue(@NotNull Widget field, @Nullable Object value, boolean init) {
        // If user interaction has started the first change to the row will make it dirty.
        if (root().isUserInteractionStarted() && !rowDirtyByUser) rowDirtyByUser = true;

        super.setValue(field, value, init);
    }
}  // end class RowModel
