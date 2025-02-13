
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
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;

import org.jetbrains.annotations.Nullable;

import static java.math.RoundingMode.HALF_UP;

import static com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat.*;
import static com.google.gwt.i18n.client.DateTimeFormat.getFormat;

import static tekgenesis.common.Predefined.mapNullable;
import static tekgenesis.common.core.Times.*;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Formats a value object to a String (ex: with a mask)
 */
public interface Formatter<T> {

    //~ Instance Fields ..............................................................................................................................

    Formatter<Boolean> BOOLEAN_FORMATTER = value -> mapNullable(value, v -> v ? MSGS.trueLabel() : MSGS.falseLabel());

    Formatter<Long> DATE_FORMATTER      = new DateFormatter(DATE_MEDIUM);
    Formatter<Long> DATE_TIME_FORMATTER = new DateTimeFormatter(DATE_TIME_SHORT);

    Formatter<Double> FILE_SIZE_FORMATTER = new FileSizeFormatter();

    Formatter<Integer> INT_FORMATTER  = new NumericFormatter<>();
    Formatter<Long>    LONG_FORMATTER = new NumericFormatter<>();

    Formatter<String> LOWERCASE_FORMATTER = value -> mapNullable(value, String::toLowerCase);

    Formatter<String> NONE_FORMATTER   = value -> value;
    Formatter<Object> OBJECT_FORMATTER = String::valueOf;
    Formatter<Double> REAL_FORMATTER   = new NumericFormatter<>();
    Formatter<String> STRING_FORMATTER = value -> value;

    Formatter<Long> TIME_AGO_DATE_FORMATTER      = new TimeAgoFormatter(true);
    Formatter<Long> TIME_AGO_DATE_TIME_FORMATTER = new TimeAgoFormatter(false);

    Formatter<String> UPPERCASE_FORMATTER = value -> mapNullable(value, String::toUpperCase);

    //~ Methods ......................................................................................................................................

    /** Formats the value. */
    @Nullable String format(T value);

    //~ Inner Classes ................................................................................................................................

    class DateFormatter implements Formatter<Long> {
        private final DateTimeFormat format;

        private DateFormatter(PredefinedFormat format) {
            this.format = getFormat(format);
        }

        @Nullable @Override public String format(final Long value) {
            return mapNullable(value, v -> format.format(millisToDate(v, true)));
        }
    }

    class DateTimeFormatter implements Formatter<Long> {
        private final DateTimeFormat format;

        private DateTimeFormatter(PredefinedFormat format) {
            this.format = getFormat(format);
        }

        @Nullable @Override public String format(final Long value) {
            return mapNullable(value, v -> format.format(millisToDate(v, false)));
        }
    }

    class DecimalFormatter implements Formatter<BigDecimal> {
        private final NumberFormat format;

        private DecimalFormatter(String pattern) {
            format = NumberFormat.getFormat(pattern);
        }

        @Nullable @Override public String format(final BigDecimal value) {
            return mapNullable(value, format::format);
        }

        static DecimalFormatter create(int decimals) {
            final StringBuilder numberPattern = new StringBuilder((decimals <= 0) ? "" : "0.");
            for (int i = 0; i < decimals; i++)
                numberPattern.append('0');
            return new DecimalFormatter(numberPattern.toString());
        }
    }  // end class DecimalFormatter

    class FileSizeFormatter implements Formatter<Double> {
        private final String[] measures = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };

        @Nullable @Override public String format(final Double value) {
            if (value != null) return format(value, 0);
            return null;
        }

        private String format(final Double value, int counter) {
            if (value > KILO) return format(value / KILO, counter + 1);
            else return new BigDecimal(value).setScale(2, HALF_UP).doubleValue() + " " + measures[counter];
        }

        private static final int KILO = 1024;
    }

    class NumericFormatter<T extends Number> implements Formatter<T> {
        @Nullable @Override public String format(final T value) {
            return mapNullable(value, v -> NumberFormat.getDecimalFormat().format(v));
        }
    }

    class TimeAgoFormatter implements Formatter<Long> {
        private final boolean isDateOnly;

        /** Formatter Constructor. */
        private TimeAgoFormatter(boolean isDateOnly) {
            this.isDateOnly = isDateOnly;
        }

        /** Format date long as time ago string. */
        @Nullable public String format(final Long value) {
            if (value != null) {
                final Date date      = millisToDate(value, isDateOnly);
                final Date now       = new Date();
                final Date yesterday = millisToDate(addDays(now.getTime(), -1), false);

                if (isEqualDateOnly(date, now)) return isDateOnly ? MSGS.today() : getFormat(TIME_SHORT).format(date);
                else if (isEqualDateOnly(date, yesterday)) return MSGS.yesterday();
                else return getFormat(DATE_SHORT).format(date);
            }
            return null;
        }
    }
}  // end interface Formatter
