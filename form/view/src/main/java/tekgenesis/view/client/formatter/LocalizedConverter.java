
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.formatter;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.valueOf;
import static java.math.BigDecimal.ROUND_UNNECESSARY;

import static tekgenesis.common.Predefined.mapNullable;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.formatter.InputFilterUtil.DECIMAL_SEPARATOR;
import static tekgenesis.view.client.formatter.InputFilterUtil.MINUS_SIGN;

/**
 * Converts a localized string to a T instance.
 */
public interface LocalizedConverter<T> {

    //~ Instance Fields ..............................................................................................................................

    LocalizedConverter<Boolean> BOOLEAN_CONVERTER = new BooleanConverter();

    LocalizedConverter<Integer> INT_CONVERTER = new Int();

    LocalizedConverter<Long> LONG_CONVERTER = new LongConverter();

    LocalizedConverter<Object> OBJECT_CONVERTER = new Identity();

    LocalizedConverter<Double> REAL_CONVERTER = new Real();

    LocalizedConverter<String> STRING_CONVERTER = new Str();

    //~ Methods ......................................................................................................................................

    /** Converts the localized string to an instance. */
    @Nullable T fromString(@Nullable String s);

    /** Converts the instance to localized string. */
    @Nullable String toString(@Nullable Object t);

    //~ Inner Classes ................................................................................................................................

    class BooleanConverter extends Default<Boolean> {
        @Override public Boolean fromNotEmptyString(@NotNull String s) {
            if (s.equals(MSGS.trueLabel())) return true;
            if (s.equals(MSGS.falseLabel())) return false;
            return Boolean.valueOf(s);
        }

        @Override String toLocalizedString(@NotNull String s) {
            return Boolean.valueOf(s) ? MSGS.trueLabel() : MSGS.falseLabel();
        }
    }

    class Decimal extends Num<BigDecimal> {
        private final int scale;

        Decimal(int scale) {
            this.scale = scale;
        }

        @Override public BigDecimal fromNotEmptyString(@NotNull String s) {
            BigDecimal bigDecimal = new BigDecimal(s);
            bigDecimal = bigDecimal.setScale(scale, ROUND_UNNECESSARY);
            return bigDecimal;
        }
        /** Returns converter with scale. */
        public static Decimal converter(int scale) {
            return new Decimal(scale);
        }
    }

    abstract class Default<T> implements LocalizedConverter<T> {
        public T fromString(@Nullable String s) {
            return s == null || s.isEmpty() ? null : fromNotEmptyString(s);
        }
        public String toString(@Nullable Object t) {
            return mapNullable(t, ts -> toLocalizedString(ts.toString()));
        }

        protected abstract T fromNotEmptyString(@NotNull String s);
        String toLocalizedString(@NotNull String s) {
            return s;
        }
    }

    class Identity extends Default<Object> {
        @Override public Object fromNotEmptyString(@NotNull String s) {
            throw new IllegalStateException("Cannot infer object from string.");
        }
    }

    class Int extends Num<Integer> {
        @Override public Integer fromNotEmptyString(@NotNull String s) {
            return Integer.valueOf(s);
        }
    }

    class LongConverter extends Default<Long> {
        @Override public Long fromNotEmptyString(@NotNull String s) {
            return Long.valueOf(s);
        }
    }

    abstract class Num<T extends Number> extends Default<T> {
        public T fromString(@Nullable String s) {
            return s == null || s.isEmpty() || valueOf(DECIMAL_SEPARATOR).equals(s) || valueOf(MINUS_SIGN).equals(s)
                   ? null
                   : fromNotEmptyString(s.replace(DECIMAL_SEPARATOR, '.'));
        }

        @Override protected String toLocalizedString(@NotNull final String s) {
            return s.replace('.', DECIMAL_SEPARATOR);
        }
    }

    class Real extends Num<Double> {
        @Override public Double fromNotEmptyString(@NotNull String s) {
            return Double.valueOf(s);
        }
    }

    class Str extends Default<String> {
        @Override public String fromNotEmptyString(@NotNull String s) {
            return s;
        }
        @Override public String toString(@Nullable Object s) {
            return (String) s;
        }
    }
}  // end interface LocalizedConverter
