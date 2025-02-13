
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.util.GwtReplaceable.isClient;
import static tekgenesis.metadata.form.model.MultipleChanges.NONE;
import static tekgenesis.metadata.form.widget.Widget.ElemType.COLUMN;

/**
 * Holds the row models of a table.
 */
public class MultipleModel implements Iterable<RowModel> {

    //~ Instance Fields ..............................................................................................................................

    private int                           clientSideCardinality = 0;
    @NotNull private final UiModelBase<?> container;
    private int                           currentPage;

    private final String name;

    private List<RowModel> rows = new ArrayList<>();

    @Nullable private transient MultipleWidget widget;

    //~ Constructors .................................................................................................................................

    /** Constructs a multiple model. */
    MultipleModel(@NotNull final UiModelBase<?> container, @NotNull final MultipleWidget widget) {
        this.container = container;
        this.widget    = widget;
        name           = widget.getName();
    }

    /** Constructs a multiple model. */
    private MultipleModel(@NotNull final UiModelBase<?> container, @NotNull final String name) {
        this.container = container;
        this.name      = name;
        widget         = null;
    }

    //~ Methods ......................................................................................................................................

    /** Creates a new empty row model and adds it to the end of the table . */
    public RowModel addRow() {
        return addRow(new RowModel(this));
    }

    /** Creates a new empty row model and inserts it at the specified index . */
    public RowModel addRow(int index) {
        return addRow(index, new RowModel(this));
    }

    /** Removes all the rows form the table. */
    public void clear() {
        rows.clear();
        currentPage = 0;
    }

    public Iterator<RowModel> iterator() {
        return rows.iterator();
    }

    /** Removes a row model from the table. */
    @SuppressWarnings("WeakerAccess")  // Used from generated code
    public void removeRow(int row) {
        markDirtyRows(row);
        rows.remove(row);
    }

    /** Sets a row model at the given index. Marks all widgets in that row dirty. */
    public void set(int i, RowModel rowModel) {
        rows.set(i, rowModel);
        rows.get(i).markDirtyElements();
    }

    /** Return TableModel size. */
    public int size() {
        return rows.size();
    }

    /** Sorts the elements with the given Comparator. */
    public void sort(Comparator<RowModel> c) {
        Collections.sort(rows, c);
    }

    /** Swaps the elements at the specified positions. */
    public void swap(final int i, final int j) {
        if (i != j) {
            Collections.swap(rows, i, j);
            rows.get(i).markDirtyElements();
            rows.get(j).markDirtyElements();
        }
    }

    /** Returns an Iterable of a specific scalar column for all the table rows. */
    public Iterable<Object> getColumn(final Widget field) {
        Model.checkType(field, COLUMN);
        return () -> new ColumnIterator(rows.iterator(), field);
    }

    /** Gets the current page. */
    public int getCurrentPage() {
        return currentPage;
    }

    /** Sets the current page. */
    public void setCurrentPage(int page) {
        currentPage = page;
    }

    /** Get widget for model. */
    public MultipleWidget getMultipleWidget() {
        if (widget == null) widget = (MultipleWidget) container().widgetByName(name);
        return widget;
    }

    /** Get a row model by index. */
    public RowModel getRow(final int index) {
        return rows.get(index);
    }

    /** Return true if the TableModel is empty. */
    public boolean isEmpty() {
        return rows.isEmpty();
    }

    /** Copy actual MultipleModel rows and data to given MultipleModel copy. */
    protected void copyTo(MultipleModel copy) {
        copy.currentPage           = currentPage;
        copy.clientSideCardinality = clientSideCardinality;

        for (final RowModel row : rows) {
            final RowModel other = new RowModel(copy);
            other.setSyncIndex(row.getSyncIndex());
            row.copyTo(other);
            copy.addRow(other);
        }
    }

    /** Return container model (containing table). */
    UiModelBase<?> container() {
        return container;
    }

    @SuppressWarnings("ConstantConditions")
    MultipleModel deserialize(final StreamReader r, boolean full) {
        final int rowSize = r.readInt();
        if (rowSize > 0 || !full) {
            final int tableDimension     = r.readInt();
            final int optionsDimension   = r.readInt();
            final int subformDimension   = r.readInt();
            final int widgetDefDimension = r.readInt();
            final int configureDimension = r.readInt();
            currentPage           = r.readInt();
            clientSideCardinality = r.readInt();

            for (int j = 0; j < rowSize; j++) {
                final RowModel rowModel = new RowModel(this,
                        tableDimension,
                        optionsDimension,
                        subformDimension,
                        widgetDefDimension,
                        configureDimension);

                // read the server last index
                if (isClient() || full) rowModel.setSyncIndex(r.readInt());
                // persist the last index of the row in the client (before the user modifications)
                else rowModel.setSyncIndex(j);

                addRow(rowModel);  // Add before deserialization to allow section() method calls
                rowModel.deserialize(r, full);
            }
        }
        return this;
    }

    /** Returns true if multiple has any row dirty. */
    boolean hasAnyRowDirty() {
        return exists(rows, rowModel -> rowModel != null && rowModel.isRowDirtyByUser());
    }

    @SuppressWarnings("ConstantConditions")
    void serialize(final StreamWriter w, boolean full) {
        w.writeString(name);
        w.writeInt(rows.size());
        if (!rows.isEmpty() || !full) {
            if (full) {
                final RowModel sample = rows.get(0);
                w.writeInt(sample.values.length);
                w.writeInt(sample.options.length);
                w.writeInt(sample.subforms.length);
                w.writeInt(sample.widgets.length);
                w.writeInt(sample.configurations.length);
            }
            else {
                w.writeInt(getMultipleWidget().getFieldDimension());
                w.writeInt(getMultipleWidget().getOptionsDimension());
                w.writeInt(getMultipleWidget().getSubformDimension());
                w.writeInt(getMultipleWidget().getWidgetDefDimension());
                w.writeInt(getMultipleWidget().getConfigureDimension());
            }
            w.writeInt(currentPage);
            w.writeInt(isClient() ? rows.size() : clientSideCardinality);

            for (final RowModel rowModel : rows) {
                // persist server last index
                if (!isClient() || full) w.writeInt(rowModel.getSyncIndex());
                // else don't need it, will be overridden by deserialize

                rowModel.serialize(w, full);
            }
        }
    }

    /** Sync row values. Return true if any value has changed. */
    MultipleChanges sync(final MultipleModel otherModel) {
        // already mark as changed if row sizes differ
        boolean changed = size() != otherModel.size();

        if (currentPage != otherModel.currentPage) {
            currentPage = otherModel.currentPage;
            changed     = true;
        }

        if (otherModel.isServerSidePristine()) {
            if (changed) return new MultipleChanges(true, emptyList());
            else return NONE;
        }

        // will hold new synced rows
        final List<RowModel> clientRows = new ArrayList<>(otherModel.size());

        final List<Integer> changedIndexes = new ArrayList<>();

        // used to detect removed rows
        int processedIndex = 0;

        for (final RowModel serverRow : otherModel.rows) {
            final RowModel clientRow;
            boolean        rowChanged = false;

            // index of the row before the changes made on the server
            final int clientIndex = serverRow.getSyncIndex();

            if (clientIndex == -1) {
                // detect row added from server and:
                clientRow  = serverRow.reparent(this);  // use it, but with the new parent model
                rowChanged = true;                      // force notify change
            }
            else {
                // detect row removed: force notify change
                if (processedIndex != clientIndex) rowChanged = true;

                // get old data
                clientRow = rows.get(clientIndex);
                // update/sync it with upcoming server data
                if (clientRow.sync(serverRow)) rowChanged = true;
            }

            // add to new synced rows
            clientRows.add(clientRow);

            // mark the place where we are now on the rows
            // processedIndex = clientIndex;
            if (rowChanged) changedIndexes.add(processedIndex);
            processedIndex++;
        }

        if (!changedIndexes.isEmpty()) changed = true;

        // if there are changed rows or server model replace rows with synced ones
        if (changed) rows = clientRows;

        return new MultipleChanges(changed, seq(changedIndexes));
    }  // end method sync

    /** Are rows server-side pristine? */
    boolean isServerSidePristine() {
        boolean result = rows.size() == clientSideCardinality && !rows.isEmpty();
        for (int i = 0; result && i < rows.size(); i++) {
            final RowModel row = rows.get(i);
            result = row.isServerSidePristine();
        }
        return result;
    }

    /** If there is a row in that index return it, otherwise create it. */
    RowModel getOrAddRow(final int index) {
        return index == rows.size() ? addRow() : getRow(index);
    }

    Integer getRowIndex(RowModel rowModel) {
        return rows.indexOf(rowModel);
    }

    String getRowPath(int row) {
        return container().getPath() + "/" + name + "#" + row;
    }

    /** Adds a row model to the table. */
    private RowModel addRow(RowModel model) {
        rows.add(model);
        return model;
    }

    /** Creates a new empty row model and inserts it at the specified index . */
    private RowModel addRow(int index, final RowModel row) {
        rows.add(index, row);
        markDirtyRows(index);
        return row;
    }

    private void markDirtyRows(final int fromIndex) {
        for (int i = fromIndex; i < rows.size(); i++)
            rows.get(i).markDirtyElements();
    }

    //~ Methods ......................................................................................................................................

    static MultipleModel instantiate(final StreamReader r, final UiModelBase<?> parent) {
        final String tableWidgetName = r.readString();
        return new MultipleModel(parent, tableWidgetName);
    }

    //~ Inner Classes ................................................................................................................................

    private static class ColumnIterator extends ImmutableIterator<Object> {
        private final Widget             column;
        private final Iterator<RowModel> rows;

        private ColumnIterator(Iterator<RowModel> rows, Widget column) {
            this.rows   = rows;
            this.column = column;
        }

        @Override public boolean hasNext() {
            return rows.hasNext();
        }

        @Override public Object next() {
            return rows.next().get(column);
        }
    }
}  // end class MultipleModel
