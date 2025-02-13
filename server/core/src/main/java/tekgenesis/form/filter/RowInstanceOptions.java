
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.filter;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.DiscreteDomain;
import tekgenesis.common.collections.Range;
import tekgenesis.common.collections.Seq;

import static java.lang.Math.ceil;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.form.ReflectedMultipleInstance.RowInstance;

/**
 * Utility class to deal with FilterOptions serving RowInstance implementations.
 */
class RowInstanceOptions {

    //~ Constructors .................................................................................................................................

    private RowInstanceOptions() {}

    //~ Methods ......................................................................................................................................

    /** Wrap accepts from user typed to column model implementation. */
    static <T, F> Accepts<RowInstance, Object> wrapAcceptsToColumn(@NotNull final FormFieldFilter<?> origin, @NotNull final Accepts<T, F> user) {
        final FormFieldFilter<T> o = cast(origin);
        return new ColumnAcceptsWrapper<>(o, user);
    }

    /** Wrap accepts from user typed to row model implementation. */
    static <T, F> Accepts<RowInstance, Object> wrapAcceptsToRow(@NotNull final Accepts<T, F> user) {
        return new RowAcceptsWrapper<>(user);
    }

    /** Wrap options from user typed to column model implementation. */
    static <T, F> Options<RowInstance, Object> wrapOptionsToColumn(@NotNull final FormFieldFilter<T> origin, @NotNull final Options<T, F> user) {
        return new ColumnOptionsWrapper<>(origin, user);
    }

    /** Wrap options from user typed to row model implementation. */
    static <T, F> Options<RowInstance, Object> wrapOptionsToRow(@NotNull final Options<T, F> user) {
        return new RowOptionsWrapper<>(user);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Abstract range options implementation.
     */
    private abstract static class AbstractRangeColumnOptions<U extends Comparable<? super U>> implements Options<RowInstance, Range<U>> {
        @NotNull final DiscreteDomain<U> domain;
        @NotNull final Rounder<U>        rounder;
        @NotNull private final Enum<?>   field;

        AbstractRangeColumnOptions(@NotNull final Enum<?> field, @NotNull final DiscreteDomain<U> domain, @NotNull final Rounder<U> rounder) {
            this.field   = field;
            this.domain  = domain;
            this.rounder = rounder;
        }

        @NotNull U nextRounded(@NotNull U last, @NotNull U current, int distance) {
            U result = current;
            for (int i = 0; i < distance; i++) {
                U next = domain.next(result);
                next = rounder.up(next);
                if (next.compareTo(last) > 0) return last;  // Cut last range on the last option (return un-rounded!)
                result = next;
                if (domain.distance(current, result) >= distance) break;
            }

            return rounder.up(result);
        }

        SortedSet<U> getElements(Seq<RowInstance> elements) {
            final SortedSet<U> set = new TreeSet<>();
            for (final RowInstance row : elements)
                set.add(getFieldValue(row));
            return set;
        }

        private U getFieldValue(@NotNull final RowInstance row) {
            return cast(row.getModel().get(field.ordinal()));
        }
    }

    private static class ColumnAcceptsWrapper<T, U> implements Accepts<RowInstance, Object> {
        @NotNull private final FormFieldFilter<T> origin;
        @NotNull private final Accepts<T, U>      user;

        private ColumnAcceptsWrapper(@NotNull final FormFieldFilter<T> origin, @NotNull final Accepts<T, U> user) {
            this.origin = origin;
            this.user   = user;
        }

        @Override public boolean apply(@NotNull RowInstance element, @NotNull Object option) {
            final U opt = cast(option);
            return user.apply(origin.map(getModelValue(element)), opt);
        }

        /** Get model raw value. */
        private Object getModelValue(RowInstance row) {
            return row.getModel().get(origin.field.ordinal());
        }
    }

    private static class ColumnOptionsWrapper<T, U> implements Options<RowInstance, Object> {
        @NotNull private final FormFieldFilter<T> origin;
        @NotNull private final Options<T, U>      user;

        private ColumnOptionsWrapper(@NotNull final FormFieldFilter<T> origin, @NotNull final Options<T, U> user) {
            this.origin = origin;
            this.user   = user;
        }

        @Override public Seq<Object> values(@NotNull Seq<RowInstance> elements) {
            // Raw options
            final Set<Object> options = new LinkedHashSet<>();

            for (final RowInstance row : elements)
                options.add(getModelValue(row));

            // Mapped <T> options
            final Seq<T> mapped = Colls.seq(options).map(origin::map);

            return cast(user.values(mapped));
        }

        /** Get model raw value. */
        private Object getModelValue(RowInstance row) {
            return row.getModel().get(origin.field.ordinal());
        }
    }

    /**
     * Given a desired distance between ranges, calculate the number of ranges and generate them.
     * Resultant distance may differ from expected, depending on how values are rounded. Resultant
     * number of ranges will be equal or lesser than {@link #MAX_STEPS}. An extra range enclosing
     * all left values may be added if {@link #MAX_STEPS} is reached.
     */
    static class DistanceColumnOptions<U extends Comparable<? super U>> extends AbstractRangeColumnOptions<U> {
        private final int distance;

        DistanceColumnOptions(@NotNull final Enum<?> field, @NotNull final DiscreteDomain<U> domain, @NotNull Rounder<U> rounder, int distance) {
            super(field, domain, rounder);
            this.distance = distance;
        }

        @Override public Seq<Range<U>> values(@NotNull Seq<RowInstance> elements) {
            final SortedSet<U> set = getElements(elements);

            final U first = set.first();
            final U last  = set.last();

            U lower = rounder.down(first);

            final List<Range<U>> result = new ArrayList<>();

            U upper = lower;

            for (int step = 0; step < MAX_STEPS && upper.compareTo(last) < 0; step++) {
                upper = nextRounded(last, upper, distance);

                result.add(Range.closed(lower, upper));

                lower = upper;
            }

            if (result.size() == MAX_STEPS && upper.compareTo(last) < 0)
                result.add(Range.closed(upper, last));  // Add an extra enclosing-all-left-values range.

            return Colls.immutable(result);
        }

        private static final int MAX_STEPS = 20;
    }

    /**
     * Range accepts implementation for column fields.
     */
    static class RangeColumnAccepts<U extends Comparable<? super U>> implements Accepts<RowInstance, Range<U>> {
        @NotNull private final Enum<?> field;

        RangeColumnAccepts(@NotNull final Enum<?> field) {
            this.field = field;
        }

        @Override public boolean apply(@NotNull RowInstance element, @NotNull Range<U> option) {
            return option.contains(getFieldValue(element));
        }

        private U getFieldValue(@NotNull final RowInstance row) {
            return cast(row.getModel().get(field.ordinal()));
        }
    }

    /**
     * Given a desired number of ranges, calculate the distance between ranges and generate them.
     * Resultant number of ranges may be equal or lesser than expected, depending on how values are
     * rounded.
     */
    static class RangeColumnOptions<U extends Comparable<? super U>> extends AbstractRangeColumnOptions<U> {
        private final int ranges;

        RangeColumnOptions(@NotNull final Enum<?> field, @NotNull final DiscreteDomain<U> domain, @NotNull Rounder<U> rounder, int ranges) {
            super(field, domain, rounder);
            this.ranges = ranges;
        }

        @Override public Seq<Range<U>> values(@NotNull Seq<RowInstance> elements) {
            final SortedSet<U> set = getElements(elements);

            final U first = set.first();
            final U last  = set.last();

            final int distance = distance(first, last);  // Calculate step distance!

            U lower = rounder.down(first);

            final List<Range<U>> result = new ArrayList<>(ranges);

            U upper = lower;

            for (int range = 0; range < ranges && upper.compareTo(last) < 0; range++) {
                upper = nextRounded(last, lower, distance);

                result.add(Range.closed(lower, upper));

                lower = upper;
            }

            return Colls.immutable(result);
        }

        /**
         * Calculate step distance as the distance between all values divided into the number of
         * ranges.
         */
        private int distance(@NotNull U first, @NotNull U last) {
            return (int) ceil(domain.distance(first, last) / (double) ranges);
        }
    }  // end class RangeColumnOptions

    private static class RowAcceptsWrapper<T, U> implements Accepts<RowInstance, Object> {
        private final Accepts<T, U> user;

        private RowAcceptsWrapper(@NotNull final Accepts<T, U> user) {
            this.user = user;
        }

        @Override public boolean apply(@NotNull RowInstance element, @NotNull Object option) {
            final T instance = cast(element.getInstance());
            final U opt      = cast(option);
            return user.apply(instance, opt);
        }
    }

    private static class RowOptionsWrapper<T, F> implements Options<RowInstance, Object> {
        private final Options<T, F> user;

        private RowOptionsWrapper(@NotNull final Options<T, F> user) {
            this.user = user;
        }

        @Override public Seq<Object> values(@NotNull Seq<RowInstance> elements) {
            final Seq<T> instances = cast(elements.map(RowInstance::getInstance));
            return cast(user.values(instances));
        }
    }
}  // end class RowInstanceOptions
