
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import java.math.BigDecimal;
import java.util.Iterator;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Urls;
import tekgenesis.type.Types;

import static java.util.Arrays.asList;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Strings.stripAccents;
import static tekgenesis.type.Types.*;

/**
 * The Bundle containing the predefined Functions.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class PredefinedFunctions implements FunctionBundle {

    //~ Methods ......................................................................................................................................

    @Override public Iterator<Fun<?>> iterator() {
        return asList(functions).iterator();
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String SCALE_FN = "scale";

    private static final Fun<String> SUBSTRING = new SubString("substring");
    private static final Fun<String> SUBSTR    = new SubStr();

    private static final Fun<String> TRIM = new Fun<String>("trim", stringType(), stringType()) {
            @Nullable @Override public String invoke(Object[] args) {
                final String str = (String) args[0];
                return str == null ? null : str.trim();
            }
        };

    private static final Fun<String> SLUG = new Fun<String>("slug", stringType(), stringType()) {
            @Nullable @Override public String invoke(Object[] args) {
                final String str = (String) args[0];
                return str == null ? null : Urls.slugUrl(str);
            }
        };

    private static final Fun<String> STRIP_ACCENTS = new Fun<String>("stripAccents", stringType(), stringType()) {
            @Nullable @Override public String invoke(Object[] args) {
                final String str = (String) args[0];
                return str == null ? null : stripAccents(str);
            }
        };

    private static final Fun<Integer> LENGTH = new Fun.Fun1<String, Integer>("length", intType(), stringType()) {
            @Override public Integer invoke(String str) {
                return str == null ? 0 : str.length();
            }
        };

    private static final Fun<Integer> INDEX_OF = new Fun.Fun2<String, String, Integer>("indexOf", intType(), stringType(), stringType()) {
            @Override public Integer invoke(String str1, String str2) {
                return str1 == null ? -1 : str2 == null ? 0 : str1.indexOf(str2);
            }
        };

    private static final Fun<Boolean> MATCHES = new Fun.Fun2<String, String, Boolean>("matches", booleanType(), stringType(), stringType()) {
            @Override public Boolean invoke(String str, String regex) {
                return str != null && regex != null && str.matches(regex);
            }
        };

    private static final Fun<String> REPEAT = new Fun.Fun2<String, Integer, String>("repeat", stringType(), intType(), stringType()) {
            @Nullable @Override public String invoke(String str, Integer count) {
                if (str == null || count == null) return null;
                final StringBuilder result = new StringBuilder();
                for (int i = 0; i < count; i++)
                    result.append(str);
                return result.toString();
            }
        };

    private static final Fun<String> REPLACE = new Fun.Fun3<String, String, String, String>("replace",
            stringType(),
            stringType(),
            stringType(),
            stringType()) {
            @Nullable @Override public String invoke(String str, String regex, String rep) {
                return str == null || regex == null ? null : str.replaceAll(regex, notNull(rep));
            }
        };

    private static final Fun<String> TO_UPPER_CASE = new Fun.Fun1<String, String>("toUpperCase", stringType(), stringType()) {
            @Nullable @Override public String invoke(String str) {
                return str == null ? null : str.toUpperCase();
            }
        };

    private static final Fun<String> TO_LOWER_CASE = new Fun.Fun1<String, String>("toLowerCase", stringType(), stringType()) {
            @Nullable @Override public String invoke(String str) {
                return str == null ? null : str.toLowerCase();
            }
        };

    private static final Fun<Long> TODAY = new Fun.Fun0<Long>("today", dateType()) {
            @Override public Long invoke() {
                return DateOnly.current().toMilliseconds();
            }

            @Override public boolean canBeConstant() {
                return false;
            }
        };

    private static final Fun<Long> NOW = new Fun.Fun0<Long>("now", dateTimeType()) {
            @Override public Long invoke() {
                return DateTime.current().toMilliseconds();
            }
            @Override public boolean canBeConstant() {
                return false;
            }
        };

    private static final Fun<Long> FIRST_DAY_OF_MONTH = new Fun.Fun1<Long, Long>("firstDayOfMonth", dateType(), dateType()) {
            @Override public Long invoke(Long date) {
                return DateOnly.fromMilliseconds(date).withDay(1).toMilliseconds();
            }

            @Override public boolean canBeConstant() {
                return false;
            }
        };

    private static final Fun<Long> LAST_DAY_OF_MONTH = new Fun.Fun1<Long, Long>("lastDayOfMonth", dateType(), dateType()) {
            @Override public Long invoke(Long date) {
                return DateOnly.fromMilliseconds(date).withDay(1).addMonths(1).addDays(-1).toMilliseconds();
            }

            @Override public boolean canBeConstant() {
                return false;
            }
        };

    private static final Fun<Double> SQRT = new Fun.Real("sqrt") {
            @Nullable @Override public Double invoke(Double f) {
                return f == null ? null : Math.sqrt(f);
            }
        };

    private static final Fun<Double> POW = new Fun.Fun2<Double, Double, Double>("pow", realType(), realType(), realType()) {
            @Nullable @Override public Double invoke(Double base, Double exp) {
                return base == null || exp == null ? null : Math.pow(base, exp);
            }
        };

    private static final Fun<BigDecimal> SCALE = new Fun.Fun2<BigDecimal, Integer, BigDecimal>(SCALE_FN, decimalType(), decimalType(), intType()) {
            @Nullable @Override public BigDecimal invoke(BigDecimal n, Integer scale) {
                return n == null ? null : n.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
            }
        };

    private static final Fun<?>[] functions = {
        INDEX_OF,
        LENGTH,
        MATCHES,
        REPEAT,
        REPLACE,
        SUBSTRING,
        SUBSTR,
        TO_LOWER_CASE,
        TO_UPPER_CASE,
        TRIM,
        SLUG,
        STRIP_ACCENTS,
        TODAY,
        NOW,
        FIRST_DAY_OF_MONTH,
        LAST_DAY_OF_MONTH,
        SQRT,
        POW,
        SCALE
    };

    //~ Inner Classes ................................................................................................................................

    private static class SubStr extends SubString {
        private SubStr() {
            super("substr");
        }

        @Override String getString(String str, int length, int beginIndex, int count) {
            final int begin = bound(beginIndex, length);
            return count <= 0 ? "" : str.substring(begin, Math.min(begin + count, length));
        }
    }

    /**
     * Substring function It follows {@link String#substring(int, int)} with the follow
     * improvements: - It never fails. (It will bound index to the actual string, returning ""
     * eventually). - It allow negative indices, meaning 'count from the end'
     */
    private static class SubString extends Fun.Fun3<String, Integer, Integer, String> {
        private SubString(String name) {
            super(name, Types.stringType(), Types.stringType(), Types.intType(), Types.intType());
        }

        @Nullable @Override protected String invoke(String str, Integer a2, Integer a3) {
            if (str == null) return null;
            final int length = str.length();
            return getString(str, length, notNull(a2, 0), notNull(a3, length));
        }

        int bound(int idx, int length) {
            return idx >= 0 ? Math.min(idx, length) : Math.max(length + idx, 0);
        }

        String getString(String str, int length, int begin, int end) {
            return begin >= end ? "" : str.substring(bound(begin, length), bound(end, length));
        }
    }
}  // end class PredefinedFunctions
