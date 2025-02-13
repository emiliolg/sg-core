
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.test.basic.OptionalReferenceMapping;
import tekgenesis.test.basic.g.OptionalReferenceMappingBase;

import static org.assertj.core.api.Assertions.*;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class BugsTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    @Rule public DbRule      db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void testSui598() {
        runInTransaction(() -> {
            final OptionalReferenceMapping mapping = OptionalReferenceMappingBase.create();
            mapping.insert();
            assertThat(mapping.getGrupart()).isNull();
        });
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}
