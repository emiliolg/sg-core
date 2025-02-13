
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.common.util.Reflection;
import tekgenesis.persistence.EntityRef;
import tekgenesis.test.basic.A;
import tekgenesis.test.basic.B;
import tekgenesis.test.basic.C;
import tekgenesis.test.basic.E;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test EntiotyRef soft references.
 */
@RunWith(Parameterized.class)
public class EntityRefTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;
    private final DbRule                   db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    /** Test soft references. */
    @Test public void testReference() {
        final E e = E.create(1);
        final A a = A.create(1);
        final B b = B.create(1);
        a.insert();
        b.insert();
        e.setA(a);
        e.setBb(b);
        e.insert();

        final E e1 = E.find(1);
        assertThat(e1).isNotNull();

        final EntityRef<A, Integer>                 a1 = Reflection.getPrivateField(e1, "a");
        final EntityRef<B, Integer>                 b1 = Reflection.getPrivateField(e1, "bb");
        final EntityRef<C, Tuple<Integer, Integer>> c1 = Reflection.getPrivateField(e1, "c");
        assert a1 != null;
        assertThat(a1.isUndefined()).isTrue();
        assert b1 != null;
        assertThat(b1.isUndefined()).isTrue();
        assert c1 != null;
        assertThat(c1.isUndefined()).isTrue();

        final A a2 = e1.getA();
        final B b2 = e1.getBb();
        c1.solve(null);
        assertThat(a2).isNotNull();
        assertThat(b2).isNotNull();
        assertThat(a1.isUndefined()).isFalse();
        assertThat(b1.isUndefined()).isFalse();
        assertThat(c1.isUndefined()).isFalse();

        forceOOM();

        assertThat(a1.isUndefined()).isTrue();
        assertThat(b1.isUndefined()).isTrue();
        assertThat(e1.getC()).isNull();
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void forceOOM() {
        /* Force releasing SoftReferences */
        try {
            final List<long[]> memhog = new LinkedList<>();
            // noinspection InfiniteLoopStatement
            while (true)
                memhog.add(new long[1000000000]);
        }
        catch (final OutOfMemoryError ex) {
            /* At this point all SoftReferences have been released - GUARANTEED. */
        }
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class EntityRefTest
