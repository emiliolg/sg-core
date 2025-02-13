
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.filter;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.ReflectedMultipleInstance;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.form.filter.Filter.FieldFilter;
import static tekgenesis.form.filter.FilterFactoryImpl.CustomColumnOptionsImpl;

/**
 * Implementation class for {@link FieldFilter}.
 */
abstract class FormFieldFilter<T> extends FormFilter implements FieldFilter<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final Enum<?> field;

    //~ Constructors .................................................................................................................................

    FormFieldFilter(@NotNull final Enum<?> field, @NotNull final String title) {
        super(title, new DefaultColumnOptions(field), new DefaultColumnAccepts(field));
        this.field = field;
    }

    //~ Methods ......................................................................................................................................

    @Override public <F> CustomOptions<T, F> custom(@NotNull Options<T, F> options) {
        return new CustomColumnOptionsImpl<>(this, options);
    }

    /**
     * Map from raw-model-object FilterLabels to user typed implementation. Use {@link #map(Object)}
     */
    @Override public Filter labels(@NotNull final Labels<T> provider) {
        labels = option -> provider.value(map(option));
        return cast(this);
    }

    /** Map object model value to user typed class (expected on user implemented interfaces). */
    T map(@NotNull final Object option) {
        return cast(option);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Default model field accepts. Uses equal for accepts.
     */
    private static class DefaultColumnAccepts implements Accepts<ReflectedMultipleInstance.RowInstance, Object> {
        @NotNull private final Enum<?> field;

        private DefaultColumnAccepts(@NotNull final Enum<?> field) {
            this.field = field;
        }

        @Override public boolean apply(@NotNull final ReflectedMultipleInstance.RowInstance row, @NotNull final Object option) {
            return equal(getModelValue(row), option);
        }

        /** Get column raw value. */
        private Object getModelValue(ReflectedMultipleInstance.RowInstance row) {
            return row.getModel().get(field.ordinal());
        }
    }

    /**
     * Default model field options. Options are retrieved as a distinct on the column.
     */
    private static class DefaultColumnOptions implements Options<ReflectedMultipleInstance.RowInstance, Object> {
        @NotNull private final Enum<?> field;

        private DefaultColumnOptions(@NotNull final Enum<?> field) {
            this.field = field;
        }

        @Override public Seq<Object> values(@NotNull final Seq<ReflectedMultipleInstance.RowInstance> elements) {
            final Set<Object> options = new LinkedHashSet<>();
            for (final ReflectedMultipleInstance.RowInstance row : elements)
                options.add(getModelValue(row));
            return Colls.seq(options);
        }

        /** Get column raw value. */
        private Object getModelValue(ReflectedMultipleInstance.RowInstance row) {
            return row.getModel().get(field.ordinal());
        }
    }
}  // end class FormFieldFilter
