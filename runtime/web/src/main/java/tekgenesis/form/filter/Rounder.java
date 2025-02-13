
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
import java.math.RoundingMode;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.cast;

/**
 * Defines a value rounder.
 */
public abstract class Rounder<U> {

    //~ Methods ......................................................................................................................................

    /**
     * Returns the largest value that is less than or equal to the argument and satisfies rounding
     * criteria.
     */
    public abstract U down(@NotNull final U value);

    /**
     * Returns the smallest value that is greater than or equal to the argument and satisfies
     * rounding criteria.
     */
    public abstract U up(@NotNull final U value);

    //~ Methods ......................................................................................................................................

    /** Return default identity rounder. */
    public static <F> Rounder<F> identity() {
        return cast(IDENTITY);
    }

    /** Default module rounder implementation for BigDecimals. */
    public static Rounder<BigDecimal> module(final BigDecimal module) {
        return new Rounder<BigDecimal>() {
            @Override public BigDecimal up(@NotNull BigDecimal value) {
                return module.multiply(new BigDecimal(value.divide(module, RoundingMode.CEILING).doubleValue()));
            }

            @Override public BigDecimal down(@NotNull BigDecimal value) {
                return module.multiply(new BigDecimal(value.divide(module, RoundingMode.FLOOR).doubleValue()));
            }
        };
    }

    /** Default module rounder implementation for Integers. */
    public static Rounder<Integer> module(final int module) {
        return new Rounder<Integer>() {
            @Override public Integer up(@NotNull Integer value) {
                return module * (int) Math.ceil(value / (double) module);
            }

            @Override public Integer down(@NotNull Integer value) {
                return module * (int) Math.floor(value / (double) module);
            }
        };
    }

    //~ Static Fields ................................................................................................................................

    private static final Rounder<Object> IDENTITY = new Rounder<Object>() {
            @Override public Object down(@NotNull Object value) {
                return value;
            }

            @Override public Object up(@NotNull Object value) {
                return value;
            }
        };
}  // end class Rounder
