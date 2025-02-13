
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

import org.jetbrains.annotations.Nullable;

import tekgenesis.type.DecimalType;
import tekgenesis.type.IntType;

import static java.lang.Character.isLetter;
import static java.lang.Character.isLetterOrDigit;

import static tekgenesis.view.client.formatter.InputFilterUtil.filterNonDecimalChars;
import static tekgenesis.view.client.formatter.InputFilterUtil.filterNonIntChars;

/**
 * Filter invalid characters from the user input.
 */
public interface InputFilter {

    //~ Instance Fields ..............................................................................................................................

    InputFilter IDENTIFIER_FILTER = value -> {
                                        final List<Character> result = new ArrayList<>(value.length());

                                        for (int i = 0; i < value.length(); i++) {
                                            final char next    = value.charAt(i);
                                            Character  consume = null;
                                            if (i == 0 && isLetter(next) || i != 0 && isLetterOrDigit(next)) consume = next;
                                            if (consume != null) result.add(consume);
                                        }
                                        return InputFilterUtil.createString(result);
                                    };

    InputFilter LOWERCASE_FILTER = value -> value != null ? value.toLowerCase() : null;

    InputFilter NONE_FILTER = value -> value;

    InputFilter UPPERCASE_FILTER = value -> value != null ? value.toUpperCase() : null;

    //~ Methods ......................................................................................................................................

    /** Filters invalid chars and return a valid result. */
    @Nullable String filter(String value);

    //~ Inner Classes ................................................................................................................................

    class DecimalInputFilter implements InputFilter {
        private final int     maxDecimals;
        private final int     maxNonDecimals;
        private final boolean signed;

        private DecimalInputFilter(final int maxDecimals, final int maxNonDecimals, boolean signed) {
            this.maxDecimals    = maxDecimals;
            this.maxNonDecimals = maxNonDecimals;
            this.signed         = signed;
        }

        @Override public String filter(String value) {
            return filterNonDecimalChars(value, maxDecimals, maxNonDecimals, signed);
        }

        /** Creates a Decimal Input Filter given a DecimalType. */
        public static DecimalInputFilter create(final DecimalType decimalType, boolean signed) {
            final int maxDecimals    = decimalType.getDecimals();
            final int maxNonDecimals = decimalType.getPrecision() - decimalType.getDecimals();
            return new DecimalInputFilter(maxDecimals, maxNonDecimals, signed);
        }
    }  // end class DecimalInputFilter

    class IntInputFilter implements InputFilter {
        private final int     length;
        private final boolean signed;

        private IntInputFilter(int length, boolean signed) {
            this.length = length;
            this.signed = signed;
        }

        @Override public String filter(String value) {
            return filterNonIntChars(value, length, signed);
        }

        /** Creates a Int Input Filter given a IntType. */
        public static IntInputFilter create(final IntType intType, boolean signed) {
            return new IntInputFilter(intType.getLength().get(), signed);
        }
    }

    class RealInputFilter implements InputFilter {
        private final boolean signed;

        private RealInputFilter(boolean signed) {
            this.signed = signed;
        }

        @Override public String filter(String value) {
            return filterNonDecimalChars(value, signed);
        }

        /** Creates a Real Input Filter given a RealType. */
        public static RealInputFilter create(boolean signed) {
            return new RealInputFilter(signed);
        }
    }
}  // end interface InputFilter
