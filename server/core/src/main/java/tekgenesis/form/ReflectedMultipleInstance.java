
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Comparator;
import java.util.Iterator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.core.Lazy;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Reflection.Instance;
import tekgenesis.form.ReflectedMultipleInstance.RowInstance;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.codegen.CodeGeneratorConstants.DEFINE_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.F_VAR;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.common.util.Reflection.invokeDeclared;

/**
 * Reflected {@link FormTable multiple} wrapping class.
 */
public class ReflectedMultipleInstance extends Instance implements FormTable<RowInstance> {

    //~ Constructors .................................................................................................................................

    ReflectedMultipleInstance(@NotNull FormTable<?> instance) {
        super(instance);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public RowInstance add() {
        return createRowInstance(getInstance().add());
    }

    @NotNull @Override public RowInstance add(int index) {
        return createRowInstance(getInstance().add(index));
    }

    @Override public void clear() {
        getInstance().clear();
    }

    @NotNull @Override public Option<RowInstance> current() {
        final Option<Object> current = getInstance().current();
        return current.isPresent() ? some(createRowInstance(current.get())) : Option.empty();
    }

    @NotNull @Override public RowInstance get(int index) {
        return createRowInstance(getInstance().get(index));
    }

    @NotNull @Override public Integer indexOf(RowInstance row) {
        return getInstance().indexOf(row);
    }

    @NotNull @Override public ImmutableIterator<RowInstance> iterator() {
        return new ImmutableIterator<RowInstance>() {
            private final Iterator<Object> iterator = getInstance().iterator();

            @Override public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override public RowInstance next() {
                return createRowInstance(iterator.next());
            }
        };
    }

    @NotNull @Override public Option<RowInstance> previous() {
        final Option<Object> previous = getInstance().previous();
        return previous.isPresent() ? some(createRowInstance(previous.get())) : Option.empty();
    }

    @Override public void remove(int index) {
        getInstance().remove(index);
    }

    @Override public void remove(RowInstance row) {
        getInstance().remove(row);
    }

    @Override public void removeCurrent() {
        getInstance().removeCurrent();
    }

    @Override public int size() {
        return getInstance().size();
    }

    @Override public void sort(Comparator<RowInstance> c) {
        throw notImplemented("Reflected sort");
    }

    @Override public void swap(int i, int j) {
        getInstance().swap(i, j);
    }

    @NotNull @Override public RowInstance getCurrent() {
        return createRowInstance(getInstance().getCurrent());
    }

    @Override public Option<Integer> getCurrentIndex() {
        return getInstance().getCurrentIndex();
    }

    @Override public int getCurrentPage() {
        return getInstance().getCurrentPage();
    }

    @Override public void setCurrentPage(int page) {
        getInstance().setCurrentPage(page);
    }

    @Override public FormTable<Object> getInstance() {
        return cast(super.getInstance());
    }

    @NotNull @Override public RowInstance getPrevious() {
        return createRowInstance(getInstance().getPrevious());
    }

    @NotNull @Override public Option<Integer> getPreviousIndex() {
        return getInstance().getPreviousIndex();
    }

    @Override public boolean isEmpty() {
        return getInstance().isEmpty();
    }

    private RowInstance createRowInstance(@NotNull final Object row) {
        return RowInstance.create(row);
    }

    //~ Inner Classes ................................................................................................................................

    public static class RowInstance extends Instance implements FieldAccessor, WidgetDefineMethodInvoker {
        private final Lazy<UiModelAccessorImpl> accessor;

        private RowInstance(@NotNull Object instance) {
            super(instance);
            accessor = new Lazy<>(() -> getPrivateField(F_VAR));
        }

        @Override public <W extends WidgetInstance<?>> W invokeDefine(String name) {
            return ensureNotNull(invokeDeclared(super.getInstance(), DEFINE_METHOD + capitalizeFirst(name)));
        }

        @Override public void setField(@NotNull Widget field, @Nullable Object value) {
            invoke(setterName(field.getName()), value != null ? value : new Object[] { null });
        }

        /** Get Row model. */
        public RowModel getModel() {
            return (RowModel) getUiModelAccessor().getContextModel();
        }

        @Override public void setSlot(@NotNull Widget field, @Nullable Object value) {
            invoke(setterName(field.getName()), value != null ? value : new Object[] { null });
        }

        @NotNull private UiModelAccessorImpl getUiModelAccessor() {
            return accessor.get();
        }

        private static RowInstance create(@NotNull Object instance) {
            return new RowInstance(instance);
        }
    }  // end class RowInstance
}  // end class ReflectedMultipleInstance
