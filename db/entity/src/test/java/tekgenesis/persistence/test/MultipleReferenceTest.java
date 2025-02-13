
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.test.basic.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class MultipleReferenceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    private final DbRule     db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void multiReferenceSequence() {
        final A a = A.create(1);
        a.setX(1);

        final B b = B.create(1);
        b.setY(1);

        final C c = C.create(1, 1);
        c.setZ(1);

        final D d = D.create(1, 1, 1);
        d.setT(1);

        final E e = E.create(1);
        e.setA(a);
        e.setBb(b);

        final F f = F.create(1);
        f.setC(c);

        final G g = G.create(1, 1);
        g.setBb(b);

        final H h = H.create();
        h.insert();

        g.setH(h);

        a.insert();
        b.insert();
        c.insert();
        d.insert();
        e.insert();
        f.insert();
        g.insert();

        final G g1 = assertNotNull(G.find(1, 1));
        assertThat(g1.getA()).isEqualTo(a);
        assertThat(g1.getBb()).isEqualTo(b);

        final C storedC = assertNotNull(F.find(1)).getC();
        assertThat(storedC).isEqualTo(c);
        assertThat(storedC.getZ()).isEqualTo(1);

        final A storedA = storedC.getA();
        assertThat(storedA).isEqualTo(a);
        assertThat(storedA.getX()).isEqualTo(1);

        final B storedB = storedC.getBb();
        assertThat(storedB).isEqualTo(b);
        assertThat(storedB.getY()).isEqualTo(1);

        final E storedE = assertNotNull(E.find(1));
        assertThat(storedE.getA()).isEqualTo(a);
        assertThat(storedE.getBb()).isEqualTo(b);

        final D storedD = assertNotNull(D.find(1, 1, 1));
        assertThat(storedD).isEqualTo(d);
        assertThat(storedD.getD()).isEqualTo(1);
        assertThat(storedD.getT()).isEqualTo(1);
    }  // end method multiReferenceSequence

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class MultipleReferenceTest
