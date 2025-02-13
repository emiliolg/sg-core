
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Reflection;
import tekgenesis.metadata.form.LocalWidget;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.widget.MultipleWidget;

import static tekgenesis.codegen.CodeGeneratorConstants.DEFINE_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.F_VAR;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.util.Reflection.invokeDeclared;
import static tekgenesis.common.util.Reflection.setPrivateField;
import static tekgenesis.form.ReflectedFormInstance.checkPublicNotStatic;
import static tekgenesis.form.ServerUiModelContext.createServerContext;
import static tekgenesis.metadata.form.exprs.ModelExpressionsEvaluator.itemCreatedCompute;
import static tekgenesis.metadata.form.exprs.ModelExpressionsEvaluator.itemDeletedCompute;

class FormTableImpl<T> implements FormTable<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final UiModelInstance enclosing;
    @NotNull private final UiModelBase<?>  parent;
    private final ReflectedFormInstance    root;
    @NotNull private final Class<T>        rowClass;
    @NotNull private final MultipleModel   table;

    //~ Constructors .................................................................................................................................

    FormTableImpl(@NotNull final UiModelAccessorImpl accessor, @NotNull final MultipleModel table, @NotNull final Class<T> rowClass,
                  @NotNull final UiModelInstance enclosing) {
        checkPublicNotStatic(rowClass);
        parent         = accessor.container();
        root           = accessor.root();
        this.table     = table;
        this.rowClass  = rowClass;
        this.enclosing = enclosing;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public T add() {
        return add(empty());
    }

    @NotNull @Override public T add(int index) {
        return add(some(index));
    }

    @Override public void clear() {
        delete(empty());
    }

    @NotNull @Override public Option<T> current() {
        return getCurrentIndex().map(this::get);
    }

    @NotNull @Override public T get(final int index) {
        return rowToClass(table.getRow(index));
    }

    @NotNull @Override public Integer indexOf(T row) {
        final UiModelAccessorImpl formImplementor = ensureNotNull(Reflection.getPrivateField(row, "f"));
        final RowModel            rowModel        = cast(formImplementor.getContextModel());
        return rowModel.getRowIndex();
    }

    @NotNull @Override public ImmutableIterator<T> iterator() {
        return Colls.map(table, this::rowToClass).toList().iterator();
    }

    @NotNull @Override public Option<T> previous() {
        final Option<Integer> previous = getPreviousIndex();
        return previous.isPresent() ? some(get(previous.get())) : empty();
    }

    @Override public void remove(final int index) {
        delete(some(index));
    }

    @Override public void remove(T row) {
        final Integer index = indexOf(row);
        if (index == -1) throw new IllegalStateException();
        else remove(index);
    }

    @Override public void removeCurrent() {
        getCurrentIndex().ifPresent(this::remove);
    }

    @Override public int size() {
        return table.size();
    }

    @Override public void sort(@NotNull Comparator<T> c) {
        final Map<T, RowModel> mapping = new HashMap<>();
        final T[]              rows    = cast(new Object[table.size()]);

        for (int i = 0; i < table.size(); i++) {
            final RowModel model = table.getRow(i);
            final T        row   = rowToClass(model);
            mapping.put(row, model);
            rows[i] = row;
        }

        Arrays.sort(rows, c);

        for (int i = 0; i < rows.length; i++)
            table.set(i, mapping.get(rows[i]));
    }

    @Override public void swap(final int i, final int j) {
        table.swap(i, j);
    }

    @Override public String toString() {
        return Colls.mkString(this);
    }

    @NotNull @Override public T getCurrent() {
        return current().getOrFail("No current row. You should use current().isDefined() to check for that situation.");
    }

    @Override public Option<Integer> getCurrentIndex() {
        return itemIndex(parent.getCurrentItem());
    }

    @Override public int getCurrentPage() {
        return table.getCurrentPage() + 1;
    }

    @Override public void setCurrentPage(int page) {
        if (page < 1) throw new IllegalArgumentException("Table current page must be greater than 1.");
        table.setCurrentPage(page - 1);
    }

    public MultipleModel getMultipleModel() {
        return table;
    }

    @NotNull @Override public T getPrevious() {
        return previous().getOrFail("No previous row. You should use previous().isDefined() to check for that situation.");
    }

    @NotNull @Override public Option<Integer> getPreviousIndex() {
        return itemIndex(parent.getLastItem());
    }

    @Override public boolean isEmpty() {
        return table.isEmpty();
    }

    private T add(@NotNull Option<Integer> section) {
        final UiModelInstanceHandler instance = UiModelInstanceHandler.wrap(root, enclosing);
        return rowToClass(itemCreatedCompute(parent, section, table.getMultipleWidget(), createServerContext(instance)));
    }

    private void delete(@NotNull Option<Integer> section) {
        final UiModelInstanceHandler instance = UiModelInstanceHandler.wrap(root, enclosing);
        itemDeletedCompute(parent, section, table.getMultipleWidget(), createServerContext(instance));
    }

    private Option<Integer> itemIndex(@Nullable LocalWidget w) {
        if (w == null) return empty();
        Option<Integer> result = empty();
        for (final MultipleWidget widget : w.getMultipleWidget(parent.metadata())) {
            if (table.getMultipleWidget().getName().equals(widget.getName())) result = w.getItem();
        }
        return result;
    }

    private T rowToClass(final RowModel rowModel) {
        try {
            final T                      instance = cast(rowClass.getDeclaredConstructors()[0].newInstance(enclosing));
            final RowDefineMethodInvoker invoker  = new RowDefineMethodInvoker(instance);
            setPrivateField(instance, F_VAR, new UiModelAccessorImpl(root, invoker, parent, rowModel));
            return instance;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private class RowDefineMethodInvoker implements WidgetDefineMethodInvoker {
        private final Object instance;

        private RowDefineMethodInvoker(Object instance) {
            this.instance = instance;
        }

        @Override public <W extends WidgetInstance<?>> W invokeDefine(String name) {
            return ensureNotNull(invokeDeclared(instance, DEFINE_METHOD + capitalizeFirst(name)));
        }
    }
}  // end class FormTableImpl
