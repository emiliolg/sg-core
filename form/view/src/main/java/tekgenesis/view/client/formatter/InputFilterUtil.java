
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.formatter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.constants.NumberConstants;

class InputFilterUtil {

    //~ Constructors .................................................................................................................................

    private InputFilterUtil() {}

    //~ Methods ......................................................................................................................................

    static String createString(final List<Character> filteredResult) {
        final char[] chars = new char[filteredResult.size()];
        for (int i = 0; i < chars.length; i++)
            chars[i] = filteredResult.get(i);
        return new String(chars);
    }

    static String filterNonDecimalChars(final String s, boolean signed) {
        final List<Character> r = new ArrayList<>(s.length());

        boolean firstDecimalSep = true;
        for (final char c : s.toCharArray()) {
            if (c == DECIMAL_SEPARATOR || c == EASY_PAD_DECIMAL_SEPARATOR) {
                if (firstDecimalSep) r.add(DECIMAL_SEPARATOR);
                firstDecimalSep = false;
            }
            else filterNonIntChar(c, r, signed);
        }

        return createString(r);
    }

    static String filterNonDecimalChars(final String s, final int maxDecimals, final int _maxNonDecimals, boolean signed) {
        final String r = filterNonDecimalChars(s, signed);

        // correct precision
        final boolean withMinus      = !r.isEmpty() && isMinus(r.charAt(0));
        final int     maxNonDecimals = withMinus ? _maxNonDecimals + 1 : _maxNonDecimals;

        int dot = r.indexOf(DECIMAL_SEPARATOR);
        dot = dot == -1 ? r.indexOf('.') : dot;

        String nonDecimals = dot > -1 ? r.substring(0, dot) : r;
        String decimals    = dot > -1 ? r.substring(dot + 1) : "";

        if (nonDecimals.length() > maxNonDecimals) nonDecimals = nonDecimals.substring(0, maxNonDecimals);

        if (dot <= -1) return nonDecimals;
        if (decimals.length() > maxDecimals) decimals = decimals.substring(0, maxDecimals);
        return nonDecimals + DECIMAL_SEPARATOR + decimals;
    }

    static String filterNonIntChars(final String s, final int maxLength, final boolean signed) {
        final List<Character> filtered = new ArrayList<>(s.length());

        for (final char c : s.toCharArray())
            filterNonIntChar(c, filtered, signed);

        String r = createString(filtered);

        // correct length
        final boolean withMinus = !r.isEmpty() && isMinus(r.charAt(0));
        final int     max       = maxLength + (withMinus ? 1 : 0);
        if (filtered.size() > max) r = r.substring(0, max);

        return r;
    }

    private static void filterNonIntChar(final char c, final List<Character> r, boolean signed) {
        if (isMinus(c)) {
            if (signed) {
                // invert sign when a minus is entered
                if (!r.isEmpty() && isMinus(r.get(0))) r.remove(0);
                else r.add(0, c);
            }
        }
        else if (Character.isDigit(c)) r.add(c);
    }

    private static boolean isMinus(char c) {
        return c == MINUS_SIGN;
    }

    //~ Static Fields ................................................................................................................................

    private static final NumberConstants LOCALIZED_NUMBER_CONSTANTS = LocaleInfo.getCurrentLocale().getNumberConstants();
    static final char                    DECIMAL_SEPARATOR          = LOCALIZED_NUMBER_CONSTANTS.decimalSeparator().charAt(0);
    static final char                    MINUS_SIGN                 = LOCALIZED_NUMBER_CONSTANTS.minusSign().charAt(0);
    static final char                    EASY_PAD_DECIMAL_SEPARATOR = '.';
}  // end class InputFilterUtil
