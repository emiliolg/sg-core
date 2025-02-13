
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

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.constants.NumberConstants;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.widget.PredefinedMask;

import static com.google.gwt.i18n.client.NumberFormat.getCurrencyFormat;
import static com.google.gwt.i18n.client.NumberFormat.getFormat;
import static com.google.gwt.i18n.client.NumberFormat.getPercentFormat;
import static com.google.gwt.i18n.client.NumberFormat.getScientificFormat;

import static tekgenesis.common.core.Strings.nChars;

/**
 * Formats input to selected Format Type.
 */
class PredefinedFormatters {

    //~ Constructors .................................................................................................................................

    private PredefinedFormatters() {}

    //~ Methods ......................................................................................................................................

    /**
     * Provides the format suitable to display unit quantities for the default locale.
     *
     * @return  a <code>NumberFormat</code> capable of producing and consuming unit quantities
     *          format for the default locale
     */
    private static NumberFormat getUnitFormat() {
        return NumberFormatHolder.cachedStockFormat;
    }

    //~ Static Fields ................................................................................................................................

    private static final NumberConstants localizedNumberConstants = LocaleInfo.getCurrentLocale().getNumberConstants();

    static final Formatter<Object> CURRENCY = new NumericFormatter(PredefinedMask.CURRENCY) {
            @Override String format(@NotNull Number number) {
                return getCurrencyFormat("ARS").format(number);
            }
        };

    static final Formatter<Object> PERCENT = new NumericFormatter(PredefinedMask.PERCENT) {
            @Override String format(@NotNull Number number) {
                return getPercentFormat().format(number);
            }
        };

    static final Formatter<Object> SCIENTIFIC = new NumericFormatter(PredefinedMask.SCIENTIFIC) {
            @Override String format(@NotNull Number number) {
                return getScientificFormat().format(number);
            }
        };

    static final Formatter<Object> RAW = new NumericFormatter(PredefinedMask.RAW) {
            @Override String format(@NotNull Number number) {
                return number.toString();
            }
        };

    static final Formatter<Object> DECIMAL = new NumericFormatter(PredefinedMask.DECIMAL) {
            @Override String format(@NotNull Number number) {
                String pattern = "#,##0";
                if (number instanceof BigDecimal) {
                    final int scale = ((BigDecimal) number).scale();
                    if (scale > 0) pattern += "." + nChars('0', scale);
                }
                return getFormat(pattern).format(number);
            }
        };

    static final Formatter<Object> UNIT = new NumericFormatter(PredefinedMask.UNIT) {
            @Override String format(@NotNull Number number) {
                return getUnitFormat().format(number);
            }
        };

    private static final Logger LOGGER = Logger.getLogger(PredefinedFormatters.class);

    //~ Inner Classes ................................................................................................................................

    private static class NumberFormatHolder {
        private static final NumberFormat cachedStockFormat = getFormat("#,##0;(#,##0)");
    }

    private abstract static class NumericFormatter implements Formatter<Object> {
        private final PredefinedMask mask;

        private NumericFormatter(@NotNull PredefinedMask mask) {
            this.mask = mask;
        }

        @Override public String format(Object value) {
            if (value == null) return null;

            final String result;

            if (value instanceof Number) result = format((Number) value);
            else {
                logIllegalStateWarning(value);
                result = value.toString();
            }

            return result;
        }

        abstract String format(@NotNull Number number);

        private void logIllegalStateWarning(Object value) {
            LOGGER.warning("Attempting " + mask + " mask formatting on value '" + value + "' of type '" + value.getClass() + "'");
        }
    }  // end class NumericFormatter
}  // end class PredefinedFormatters
