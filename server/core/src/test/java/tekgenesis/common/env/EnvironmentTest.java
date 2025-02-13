
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.env;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import tekgenesis.common.core.Option;
import tekgenesis.common.env.impl.BaseEnvironment;
import tekgenesis.common.env.impl.MemoryEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

/**
 * Memory Environment Test Suite;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "AssignmentToStaticFieldFromInstanceMethod" })
public class EnvironmentTest {

    //~ Instance Fields ..............................................................................................................................

    Environment env = null;

    @Rule public ExternalResource resource = new ExternalResource() {
            @Override protected void before()
                throws Throwable
            {
                env = new MemoryEnvironment();
            }

            @Override protected void after() {
                env.dispose();
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void basicEnvironmentOperations() {
        env.put("a.b", "x", "100");
        env.put("", "x", "1");
        env.put("", "y", "2");

        assertThat(getString("a.b", "x", null)).isEqualTo("100");
        assertThat(getString("a.b", "y", null)).isEqualTo("2");

        assertThat(getString("", "x", null)).isEqualTo("1");
        assertThat(getString("", "y", null)).isEqualTo("2");

        env.put("a.b", "y", "200");
        assertThat(getString("a.b", "y", null)).isEqualTo("200");

        env.delete("a.b", "y");
        assertThat(getString("a.b", "y", null)).isEqualTo("2");

        env.delete("", "y");
        assertThat(getString("a.b", "y", null)).isEqualTo("");
    }

    @Test public void basicPut() {
        putStr("", "x", "100");
        assertThat(env.get("a.b", "x", Str.class, null).str).isEqualTo("100");
        assertThat(env.get("a.b", "x", Str.class, null).str).isEqualTo("100");
    }

    @Test public void listeners() {
        env.put("a.b", "x1", "100");

        putStr("a.b", "x", "100");
        putStr("", "x", "1");
        putStr("", "y", "2");

        try {
            getString("a.b", "x1", value -> {});
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertThat(e).hasMessage(BaseEnvironment.LISTENER_FOR_IMMUTABLE_VALUE_MSG);
        }

        final StrListener testListener = new StrListener();

        assertThat(env.get("a.b", "x", Str.class, testListener).str).isEqualTo("100");
        assertThat(env.get("a.b", "y", Str.class, testListener).str).isEqualTo("2");

        putStr("a.b", "x", "101");
        assertThat(testListener.lastValue).isEqualTo("101");

        putStr("", "y", "3");
        assertThat(testListener.lastValue).isEqualTo("3");

        putStr("a.b", "y", "200");
        assertThat(testListener.lastValue).isEqualTo("200");

        putStr("", "y", "4");
        assertThat(testListener.lastValue).isEqualTo("200");

        env.delete("a.b", "y");
        assertThat(testListener.lastValue).isEqualTo("4");

        env.delete("", "y");
        assertThat(testListener.lastValue).isEqualTo("");

        putStr("a.b", "y", "201");
        assertThat(testListener.lastValue).isEqualTo("201");

        putStr("", "y", "5");
        assertThat(testListener.lastValue).isEqualTo("201");

        env.delete("a.b", "y");
        assertThat(testListener.lastValue).isEqualTo("5");
    }  // end method listeners

    private void putStr(String scope, String name, String value) {
        env.put(scope, name, new Str(value));
    }

    private String getString(String scope, String name, Environment.Listener<String> l) {
        return env.get(scope, name, String.class, l);
    }

    //~ Inner Classes ................................................................................................................................

    @Mutable static class Age {
        @JsonProperty int value;

        Age() {
            this(0);
        }

        Age(int v) {
            value = v;
        }
    }

    @Mutable static class Str {
        @JsonProperty String str = "";

        Str() {
            str = "";
        }

        Str(String s) {
            str = s;
        }
    }

    static class StrListener implements Environment.Listener<Str> {
        String lastValue = "";

        @Override public void onChange(@NotNull Option<Str> value) {
            lastValue = value.orElse(new Str("")).str;
        }
    }
}  // end class EnvironmentTest
