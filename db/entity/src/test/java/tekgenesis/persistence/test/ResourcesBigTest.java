
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.LrgTests;
import tekgenesis.persistence.resource.DbResourceHandler;
import tekgenesis.test.basic.ResA;
import tekgenesis.test.basic.ResB;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.media.Mime.TEXT_PLAIN;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ResourcesBigTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private DbResourceHandler rh = null;

    @Rule public DbRule db = new DbRule(DbRule.AUTHORIZATION, DbRule.SG, DbRule.BASIC_TEST, DbRule.BASIC, DbRule.SHOWCASE, DbRule.MAIL) {
            @Override protected void before() {
                createDatabase(dbName);
                rh = new DbResourceHandler(env, database);
            }
        };

    //~ Methods ......................................................................................................................................

    @Category(LrgTests.class)
    @Test public void resourceGCBigAmount() {
        final List<Resource> resourcesSeq    = new ArrayList<>();
        final String         resourceContent = "Resource Content";

        for (int i = 1; i <= 5500; i++) {
            resourcesSeq.add(rh.create().upload("name " + i, TEXT_PLAIN.getMime(), new StringReader(resourceContent)));
            if (i % 1000 == 0) System.out.println("Created " + i + " resources");
        }

        final List<Resource> toPurge = new ArrayList<>();

        final String mustExist = invokeInTransaction(t -> {
                t.beginBatch();
                String s = null;
                int    i = 0;
                for (final Resource resource : resourcesSeq) {
                    final ResA create = ResA.create();
                    create.setName("a" + i);
                    if (i % 50 != 0) {
                        create.setRes(resource);
                        s = resource.getUuid();
                    }
                    else toPurge.add(resource);
                    create.persist();

                    final ResB resB = ResB.create();
                    resB.setName("b" + i);
                    if (i % 50 != 0) resB.setRes2(resource);
                    resB.persist();
                    i += 1;
                    if (i % 100 == 0) System.out.println("Created " + i + " entities");
                }
                t.commit();
                return s;
            });

        System.out.println("Before big GC");
        final long millis = System.currentTimeMillis();
        rh.collect();

        final long elapsed = System.currentTimeMillis() - millis;
        System.out.println("Gc run in " + elapsed + " ms");
        assertThat(elapsed < 30000).isTrue();

        for (final Resource r : toPurge)
            assertThat(rh.findResource(r.getUuid())).isEqualTo(Option.empty());

        assertThat(rh.findResource(mustExist)).isNotEqualTo(Option.empty());
        // assertThat(purged).isNotEmpty().hasSize(20);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ResourcesBigTest
