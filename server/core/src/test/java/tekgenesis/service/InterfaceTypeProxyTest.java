
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.math.BigDecimal;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import tekgenesis.type.InterfaceType;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection", "JavaDoc" })
public class InterfaceTypeProxyTest {

    //~ Methods ......................................................................................................................................

    @Test public void proxyGetPutTest() {
        final InterfaceTypeProxy invocationHandler = new InterfaceTypeProxy();

        final TypeSample sample = invocationHandler.proxy(TypeSample.class);

        sample.setA("String a");
        sample.setB(10);
        sample.setC(new BigDecimal(99.9));
        sample.setD(new TypeStruct());

        assertThat(sample.getA()).isEqualTo("String a");
        assertThat(sample.getB()).isEqualTo(10);
        assertThat(sample.getC()).isEqualTo(new BigDecimal(99.9));
        final TypeStruct d = sample.getD();
        assertThat(d).isNotNull();
        assertThat(d.getA()).isEmpty();
    }

    //~ Inner Interfaces .............................................................................................................................

    private interface TypeSample extends InterfaceType {
        /** Sets the value of the A. */
        @Nullable String getA();

        /** Returns the A. */
        void setA(@Nullable String a);

        /** Sets the value of the B. */
        int getB();

        /** Returns the B. */
        void setB(int b);

        /** Sets the value of the C. */
        @Nullable BigDecimal getC();

        /** Returns the C. */
        void setC(@Nullable BigDecimal c);

        /** Sets the value of the D. */
        @Nullable TypeStruct getD();

        /** Returns the D. */
        void setD(@Nullable TypeStruct d);
    }

    //~ Inner Classes ................................................................................................................................

    private class TypeStruct {
        private String a = "";
        private int    b = 0;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }
}  // end class InterfaceTypeProxyTest
