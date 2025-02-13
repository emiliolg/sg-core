
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.filter;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.DiscreteDomain;
import tekgenesis.common.collections.Range;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityTable;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.form.ReflectedMultipleInstance.RowInstance;
import static tekgenesis.form.filter.Filter.*;
import static tekgenesis.form.filter.RowInstanceOptions.*;

/**
 * Filter factory implementation.
 */
public class FilterFactoryImpl implements FilterFactory {

    //~ Constructors .................................................................................................................................

    /** Default constructor for injection. */
    public FilterFactoryImpl() {}

    //~ Methods ......................................................................................................................................

    @Override public BooleanFilter boolFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new BooleanFilterImpl(field, title);
    }

    @Override public <T, F> CustomOptions<T, F> customFilter(@NotNull String title, @NotNull Options<?, F> options) {
        return new CustomRowOptionsImpl<>(title, options);
    }

    @Override public DateFilter dateFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new DateFilterImpl(field, title);
    }

    @Override public DateTimeFilter dateTimeFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new DateTimeFilterImpl(field, title);
    }

    @Override public DecimalFilter decimalFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new DecimalFilterImpl(field, title);
    }

    @Override public <T> EntityFilter<T> entityFilter(@NotNull Enum<?> field, @NotNull Class<T> type, @NotNull String title) {
        return new EntityFilterImpl<>(field, type, title);
    }

    @Override public <T extends Enum<T> & Enumeration<T, I>, I> EnumFilter<T, I> enumFilter(@NotNull Enum<?> field, @NotNull Class<T> type,
                                                                                            @NotNull String title) {
        return new EnumFilterImpl<>(field, type, title);
    }

    @Override public IntegerFilter intFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new IntegerFilterImpl(field, title);
    }

    @Override public RealFilter realFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new RealFilterImpl(field, title);
    }

    @Override public StringFilter strFilter(@NotNull Enum<?> field, @NotNull String title) {
        return new StringFilterImpl(field, title);
    }

    //~ Inner Classes ................................................................................................................................

    private static class BooleanFilterImpl extends FormFieldFilter<Boolean> implements BooleanFilter {
        private BooleanFilterImpl(@NotNull Enum<?> field, @NotNull String title) {
            super(field, title);
            exclusive();
        }
    }

    static class CustomColumnOptionsImpl<T, F> implements CustomOptions<T, F> {
        @NotNull private final Options<T, F> options;

        @NotNull private final FormFieldFilter<T> origin;

        CustomColumnOptionsImpl(@NotNull FormFieldFilter<T> origin, @NotNull Options<T, F> options) {
            this.origin  = origin;
            this.options = options;
        }

        @Override public TypedFilter<F> accepts(@NotNull Accepts<? super T, F> accepts) {
            return new TypedFilterImpl<>(origin.getTitle(), wrapOptionsToColumn(origin, options), wrapAcceptsToColumn(origin, accepts));
        }
    }

    static class CustomRowOptionsImpl<T, F> implements CustomOptions<T, F> {
        @NotNull private final Options<?, F> options;

        @NotNull private final String title;

        CustomRowOptionsImpl(@NotNull String title, @NotNull Options<?, F> options) {
            this.title   = title;
            this.options = options;
        }

        @Override public TypedFilter<F> accepts(@NotNull Accepts<? super T, F> accepts) {
            return new TypedFilterImpl<>(title, wrapOptionsToRow(options), wrapAcceptsToRow(accepts));
        }
    }

    private static class DateFilterImpl extends TemporalFilterImpl<DateOnly> implements DateFilter {
        private DateFilterImpl(Enum<?> field, @NotNull String title) {
            super(field, title);
        }

        @Override DateOnly map(@NotNull Object option) {
            return DateOnly.fromMilliseconds((Long) option);
        }
    }

    private static class DateTimeFilterImpl extends TemporalFilterImpl<DateTime> implements DateTimeFilter {
        private DateTimeFilterImpl(Enum<?> field, @NotNull String title) {
            super(field, title);
        }

        @Override DateTime map(@NotNull Object option) {
            return DateTime.fromMilliseconds((Long) option);
        }
    }

    private static class DecimalFilterImpl extends NumericFilterImpl<BigDecimal> implements DecimalFilter {
        private DecimalFilterImpl(@NotNull Enum<?> field, @NotNull String title) {
            super(field, title);
        }

        @Override protected Options<RowInstance, Range<BigDecimal>> range(int r, @NotNull Rounder<BigDecimal> rounder) {
            return new RangeColumnOptions<>(field, DiscreteDomain.decimals(), rounder, r);
        }

        @Override protected Options<RowInstance, Range<BigDecimal>> step(int d, @NotNull Rounder<BigDecimal> rounder) {
            return new DistanceColumnOptions<>(field, DiscreteDomain.decimals(), rounder, d);
        }
    }

    private static class EntityFilterImpl<T> extends FormFieldFilter<T> implements EntityFilter<T> {
        @NotNull private final EntityTable<? extends EntityInstance<?, ?>, ?> table;

        private EntityFilterImpl(@NotNull Enum<?> field, @NotNull Class<T> type, @NotNull String title) {
            super(field, title);
            table = cast(EntityTable.forName(type.getName()));
            final Labels<T> provider = cast(Labels.DEFAULT);
            labels(provider);  // labels invocation to map values
        }

        @Nullable @Override T map(@NotNull Object key) {
            return cast(table.findByString((String) key));
        }
    }

    private static class EnumFilterImpl<T extends Enum<T> & Enumeration<T, I>, I> extends FormFieldFilter<T> implements EnumFilter<T, I> {
        @NotNull private final Class<T> enumType;

        @SuppressWarnings("Convert2MethodRef")
        private EnumFilterImpl(@NotNull Enum<?> field, @NotNull Class<T> enumType, @NotNull String title) {
            super(field, title);
            this.enumType = enumType;
            labels(o -> o.label());  // labels invocation to map values
        }

        /** Overridden to return high level Enumeration<T> instead of model raw String. */
        @Override T map(@NotNull Object name) {
            return cast(Enumerations.valueOf(enumType, (String) name));
        }
    }

    private static class IntegerFilterImpl extends NumericFilterImpl<Integer> implements IntegerFilter {
        private IntegerFilterImpl(@NotNull Enum<?> field, @NotNull String title) {
            super(field, title);
        }

        @Override protected Options<RowInstance, Range<Integer>> range(int r, @NotNull Rounder<Integer> rounder) {
            return new RangeColumnOptions<>(field, DiscreteDomain.integers(), rounder, r);
        }

        @Override protected Options<RowInstance, Range<Integer>> step(int d, @NotNull Rounder<Integer> rounder) {
            return new DistanceColumnOptions<>(field, DiscreteDomain.integers(), rounder, d);
        }
    }

    private abstract static class NumericFilterImpl<U extends Comparable<? super U>> extends FormFieldFilter<U> implements NumericFilter<U> {
        private NumericFilterImpl(@NotNull final Enum<?> field, @NotNull String title) {
            super(field, title);
        }

        @Override public TypedFilter<U> ranges(int r) {
            return ranges(r, rounder());
        }

        @Override public TypedFilter<U> ranges(int ranges, @NotNull Rounder<U> rounder) {
            setOptions(range(ranges, rounder));
            setAccepts(new RangeColumnAccepts<U>(field));
            exclusive();
            return cast(this);
        }

        @Override public TypedFilter<U> steps(int d) {
            return steps(d, rounder());
        }

        @Override public TypedFilter<U> steps(int distance, @NotNull Rounder<U> rounder) {
            setOptions(step(distance, rounder));
            setAccepts(new RangeColumnAccepts<U>(field));
            exclusive();
            return cast(this);
        }

        /** Options for a given amount of ranges. */
        protected abstract Options<RowInstance, Range<U>> range(int ranges, @NotNull Rounder<U> rounder);

        /** Options for a given distance between ranges. */
        protected abstract Options<RowInstance, Range<U>> step(int distance, @NotNull Rounder<U> rounder);

        /** Element Rounder. */
        private Rounder<U> rounder() {
            return Rounder.identity();
        }
    }

    private static class RealFilterImpl extends NumericFilterImpl<Double> implements RealFilter {
        private RealFilterImpl(@NotNull Enum<?> field, @NotNull String title) {
            super(field, title);
        }

        @Override protected Options<RowInstance, Range<Double>> range(int r, @NotNull Rounder<Double> rounder) {
            return new RangeColumnOptions<>(field, DiscreteDomain.doubles(), rounder, r);
        }

        @Override protected Options<RowInstance, Range<Double>> step(int d, @NotNull Rounder<Double> rounder) {
            return new DistanceColumnOptions<>(field, DiscreteDomain.doubles(), rounder, d);
        }
    }

    private static class StringFilterImpl extends FormFieldFilter<String> implements StringFilter {
        private StringFilterImpl(@NotNull final Enum<?> field, @NotNull String title) {
            super(field, title);
        }
    }

    private abstract static class TemporalFilterImpl<U> extends FormFieldFilter<U> implements TemporalFilter<U> {
        private TemporalFilterImpl(@NotNull final Enum<?> field, @NotNull String title) {
            super(field, title);
        }
    }

    private static class TypedFilterImpl<F> extends FormFilter implements TypedFilter<F> {
        private TypedFilterImpl(@NotNull String title, @NotNull Options<RowInstance, Object> options, @NotNull Accepts<RowInstance, Object> accepts) {
            super(title, options, accepts);
        }

        @Override public Filter labels(@NotNull Labels<F> provider) {
            labels = cast(provider);
            return this;
        }
    }
}  // end class FilterFactoryImpl
