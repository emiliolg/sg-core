
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package code;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import tekgenesis.code.Fun;
import tekgenesis.code.PredefinedFunctions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class PredefinedFunctionsTest {

    //~ Methods ......................................................................................................................................

    @Test public void decimalFunctions() {
        assertThat(dec("scale", Decimals.fromInt(1), 2)).isEqualTo(Decimals.fromString("1.00"));
        assertThat(dec("scale", Decimals.fromDouble(1.21), 0)).isEqualTo(Decimals.fromInt(1));
    }

    @Test public void mathFunctions() {
        assertThat(real("sqrt", 4.0)).isEqualTo(2.0);
        assertThat(real("pow", 2.0, 2.0)).isEqualTo(4.0);
    }

    @SuppressWarnings("NullArgumentToVariableArgMethod")
    @Test public void stringFunctions() {
        assertThat(integer("indexOf", "hola pepe", "pepe")).isEqualTo(5);
        assertThat(integer("indexOf", "hola pepe", "pipa")).isEqualTo(-1);
        assertThat(integer("indexOf", "hola pepe", null)).isZero();
        assertThat(integer("indexOf", null, "pipa")).isEqualTo(-1);

        assertThat(integer("length", "a1")).isEqualTo(2);
        assertThat(integer("length", "")).isZero();
        assertThat(integer("length", (String) null)).isZero();

        assertThat(bool("matches", "hola pepe", ".*pepe.*")).isTrue();
        assertThat(bool("matches", "hola pepe", "hola.*")).isTrue();
        assertThat(bool("matches", "hola pepe", "hola")).isFalse();

        assertThat(str("repeat", "xx ", 2)).isEqualTo("xx xx ");
        assertThat(str("repeat", "xx ", 0)).isEqualTo("");
        assertThat(str("repeat", null, 5)).isNull();
        assertThat(str("repeat", "xx", null)).isNull();

        assertThat(str("replace", "pepe y pepa", "pe", "be")).isEqualTo("bebe y bepa");
        assertThat(str("replace", "pepe y pepa", "p([ae])", "b$1")).isEqualTo("bebe y beba");
        assertThat(str("replace", "pepe y pepa", "p([ae])", null)).isEqualTo(" y ");
        assertThat(str("replace", null, "x", null)).isNull();
        assertThat(str("replace", "x", null, null)).isNull();

        assertThat(str("substring", "Hola", 1, 3)).isEqualTo("ol");
        assertThat(str("substring", "Hola", 1, 100)).isEqualTo("ola");
        assertThat(str("substring", "Hola", 100, 200)).isEqualTo("");
        assertThat(str("substring", "Hola", 100, 2)).isEqualTo("");

        assertThat(str("substring", "Hola", -2, -1)).isEqualTo("l");
        assertThat(str("substring", "Hola", -2, null)).isEqualTo("la");
        assertThat(str("substring", "Hola", -20, -1)).isEqualTo("Hol");

        assertThat(str("substr", "Hola", 1, 2)).isEqualTo("ol");
        assertThat(str("substr", "Hola", 1, 100)).isEqualTo("ola");
        assertThat(str("substr", "Hola", 100, 200)).isEqualTo("");
        assertThat(str("substr", "Hola", 100, 2)).isEqualTo("");

        assertThat(str("substr", "Hola", -2, 1)).isEqualTo("l");
        assertThat(str("substr", "Hola", -2, 2)).isEqualTo("la");
        assertThat(str("substr", "Hola", -20, 3)).isEqualTo("Hol");

        assertThat(str("toLowerCase", "Hola")).isEqualTo("hola");
        assertThat(str("toLowerCase", (String) null)).isNull();

        assertThat(str("toUpperCase", "Hola")).isEqualTo("HOLA");
        assertThat(str("toUpperCase", (String) null)).isNull();

        assertThat(str("trim", " aaaa ")).isEqualTo("aaaa");
        assertThat(str("trim", "aaaa")).isEqualTo("aaaa");
        assertThat(str("trim", (String) null)).isNull();
        assertThat(str("trim", "")).isEqualTo("");

        assertThat(str("slug", "Url,, with!! strange?? things**")).isEqualTo("url-with-strange-things");
        assertThat(str("slug", "Remove UPPERCASE and áccént")).isEqualTo("remove-uppercase-and-accent");
        assertThat(str("slug", (String) null)).isNull();
        assertThat(str("slug", "")).isEqualTo("");
    }  // end method stringFunctions

    @Test public void timeFunctions() {
        final Long today = lng("today");
        final Long now   = lng("now");

        final long currentTime = DateTime.current().toMilliseconds();
        assertThat(now).isBetween(currentTime - 50, currentTime + 50);

        final long currentDate = DateOnly.current().toMilliseconds();
        assertThat(today).isBetween(currentDate - 1, currentDate + 1);

        final Long     firstDayOfMonth = f(Long.class, "firstDayOfMonth", today);
        final DateOnly firstDay        = DateOnly.fromMilliseconds(firstDayOfMonth);
        final Long     lastDayOfMonth  = f(Long.class, "lastDayOfMonth", today);
        final DateOnly lastDay         = DateOnly.fromMilliseconds(lastDayOfMonth);
        assertThat(firstDay.getDay()).isEqualTo(1);
        assertThat(lastDay.getDay()).isGreaterThanOrEqualTo(28);
        assertThat(lastDay.addDays(1).getDay()).isEqualTo(1);
    }

    private boolean bool(String fn, Object... args) {
        return f(Boolean.class, fn, args);
    }

    private BigDecimal dec(String fn, Object... args) {
        return f(BigDecimal.class, fn, args);
    }

    private <T> T f(Class<T> clazz, String fn, Object... args) {
        final Fun<?>   fun       = FUNCTIONS.get(fn);
        final Object[] arguments = args != null ? args : new Object[] { null };
        return clazz.cast(fun.invoke(arguments));
    }

    private int integer(String fn, Object... args) {
        return f(Integer.class, fn, args);
    }

    private Long lng(String fn, Object... args) {
        return f(Long.class, fn, args);
    }

    private Double real(String fn, Object... args) {
        return f(Double.class, fn, args);
    }

    private String str(String fn, Object... args) {
        return f(String.class, fn, args);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, Fun<?>> FUNCTIONS = new HashMap<>();

    static {
        for (final Fun<?> fun : new PredefinedFunctions())
            FUNCTIONS.put(fun.getName(), fun);
    }
}  // end class PredefinedFunctionsTest
