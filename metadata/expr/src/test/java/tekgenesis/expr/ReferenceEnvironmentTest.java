
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import org.junit.Test;

import tekgenesis.expr.SimpleReferenceSolver.ValueType;
import tekgenesis.type.Type;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test ReferenceEnvironment.
 */
public class ReferenceEnvironmentTest {

    //~ Methods ......................................................................................................................................

    @Test public void testReferencesBinding() {
        final Object context = new Object();

        final ValueType iva = env.bindRef("iva", false);
        assertThat(iva.apply(context)).isEqualTo(21.00);

        final ValueType total = env.bindRef("sale.total", false);
        assertThat(total.apply(context)).isEqualTo(199.99);

        final ValueType name = env.bindRef("sale.customer.name", false);
        assertThat(name.apply(context)).isEqualTo("Walter White");

        final ValueType age = env.bindRef("sale.customer.age", false);
        assertThat(age.apply(context)).isEqualTo(51);

        final ValueType credit = env.bindRef("credit.total", false);
        assertThat(credit.apply(context)).isEqualTo(10000.00);

        final ValueType kind = env.bindRef("credit.kind", false);
        assertThat(kind.apply(context)).isEqualTo("gold");
    }

    @Test public void testReferencesType() {
        final Type iva = env.doResolve("iva", false);
        assertThat(iva.isNumber()).isTrue();

        final Type total = env.doResolve("sale.total", false);
        assertThat(total.isNumber()).isTrue();

        final Type name = env.doResolve("sale.customer.name", false);
        assertThat(name.isString()).isTrue();

        final Type age = env.doResolve("sale.customer.age", false);
        assertThat(age.isNumber()).isTrue();

        final Type credit = env.doResolve("credit.total", false);
        assertThat(credit.isNumber()).isTrue();

        final Type kind = env.doResolve("credit.kind", false);
        assertThat(kind.isString()).isTrue();
    }

    //~ Static Fields ................................................................................................................................

    private static final ReferenceEnvironment env = new ReferenceEnvironment();

    static {
        //J-
        env.put("iva", 21.00).put("usd", 14.10);

        env.putEnvironment("sale")
                .put("total", 199.99)
                .putEnvironment("customer")
                    .put("name", "Walter White")
                    .put("age", 51);

        env.putEnvironment("credit")
                .put("total", 10000.00);

        // Reuse previous environment if exists
        env.putEnvironment("credit")
                .put("kind", "gold");
        //J+
    }
}  // end class ReferenceEnvironmentTest
