
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

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;

/**
 * Form Filter.
 */
public interface Filter {

    //~ Methods ......................................................................................................................................

    /** Restrict to exclusive filter matching options. */
    Filter exclusive();

    /** Allow inclusive filter matching options. */
    Filter inclusive();

    /** Allow multiple filter options selection. */
    Filter multiple();

    /** Restrict to single filter options selection. */
    Filter single();

    /** Set filter title. */
    Filter title(@NotNull final String title);

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Exclusive filter for Boolean fields.
     */
    interface BooleanFilter extends FieldFilter<Boolean> {}

    /**
     * Custom arbitrary typed options.
     */
    interface CustomOptions<T, F> {
        /** Set accept predicate to options and return resulting filter. */
        TypedFilter<F> accepts(@NotNull final Accepts<? super T, F> provider);
    }

    /**
     * Filter for Date fields.
     */
    interface DateFilter extends TemporalFilter<DateOnly> {}

    /**
     * Filter for DateTime fields.
     */
    interface DateTimeFilter extends TemporalFilter<DateTime> {}

    /**
     * Filter for Decimal fields.
     */
    interface DecimalFilter extends NumericFilter<BigDecimal> {}

    /**
     * Filter for Enum fields.
     */
    interface EntityFilter<T> extends FieldFilter<T> {}

    /**
     * Filter for Enum fields.
     */
    interface EnumFilter<T extends Enum<T> & Enumeration<T, I>, I> extends FieldFilter<T> {}

    /**
     * Protected Field filter. Typed filter for specific model column fields. Allows custom options
     * implementation.
     */
    interface FieldFilter<T> extends TypedFilter<T> {
        /** Implement custom specific typed options. */
        <F> CustomOptions<T, F> custom(@NotNull final Options<T, F> options);
    }

    /**
     * Filter for Integer fields.
     */
    interface IntegerFilter extends NumericFilter<Integer> {}

    /**
     * Protected filter for numeric fields (Integer, Decimal, Real).
     */
    interface NumericFilter<U> extends FieldFilter<U> {
        /**
         * Map values to given number of ranges. Calculate distance from min and max values. Display
         * exclusive.
         */
        TypedFilter<U> ranges(int ranges);

        /**
         * Map values to given number of ranges. Calculate distance from min and max values. Uses
         * Rounder to round. Display exclusive.
         */
        TypedFilter<U> ranges(int ranges, @NotNull final Rounder<U> rounder);

        /**
         * Map values to an arbitrary number of ranges, using the given distance. Display exclusive.
         */
        TypedFilter<U> steps(int distance);

        /**
         * Map values to an arbitrary number of ranges, using the given distance. Uses Rounder to
         * round. Display exclusive.
         */
        TypedFilter<U> steps(int distance, @NotNull final Rounder<U> rounder);
    }

    /**
     * Filter for Real fields.
     */
    interface RealFilter extends NumericFilter<Double> {}

    /**
     * Filter for String fields.
     */
    interface StringFilter extends FieldFilter<String> {}

    /**
     * Protected filter for temporal fields (DateOnly and DateTime).
     */
    interface TemporalFilter<U> extends FieldFilter<U> {
        // FieldFilter<U> ranges(int ranges); todo implement ranges for temporal and handy methods for dates.
    }

    /**
     * Arbitrary typed filter.
     */
    interface TypedFilter<F> extends Filter {
        /** Set specific label provider. */
        Filter labels(@NotNull final Labels<F> provider);
    }
}  // end interface Filter
