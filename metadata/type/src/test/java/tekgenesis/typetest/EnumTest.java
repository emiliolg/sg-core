
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.typetest;

import org.junit.Test;

import test.MyEnum;
import test.MyEnum2;

import static org.assertj.core.api.Assertions.assertThat;

import static test.MyEnum.A;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class EnumTest {

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("EqualsWithItself")
    @Test public void enumTest() {
        assertThat(A.name()).isEqualTo("A");

        final MyEnum c = MyEnum.valueOf("C");
        assertThat(c.name()).isEqualTo("C");

        assertThat(c).isNotEqualTo(A);

        assertThat(MyEnum2.A).isNotEqualTo(A);

        assertThat(c).isNotEqualTo("xxx");

        // noinspection EqualsCalledOnEnumConstant
        assertThat(c.equals(c)).isTrue();

        assertThat(c.compareTo(A)).isGreaterThan(0);

        assertThat(c.hashCode()).isNotEqualTo(A.hashCode());

        assertThat(c.ordinal()).isEqualTo(2);

        assertThat(c.label()).isEqualTo("cc");

        assertThat(MyEnum.valueOf("D").toString()).isEqualTo("D");
    }
}
