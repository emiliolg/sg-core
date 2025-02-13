
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.test.basic.DeprecableEntity;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.tools.test.TestSession.TEST_USER;
import static tekgenesis.test.basic.g.DeprecableEntitySearcherBase.DEPRECABLE_ENTITY_SEARCHER;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * DeprecationTest.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "MagicNumber" })
public class DeprecationTest extends IndexSuite {

    //~ Methods ......................................................................................................................................

    @Test public void testDeprecationBasics()
        throws InterruptedException
    {
        runInTransaction(() -> {
            DeprecableEntity.create(54563189).setFirstName("Daniel").setLastName("Craig").insert();
            DeprecableEntity.create(2131241).setFirstName("Roger").setLastName("Moore").insert();
            DeprecableEntity.create(242345).setFirstName("Sean").setLastName("Connery").insert();
            DeprecableEntity.create(123123).setFirstName("Sean").setLastName("Bean").insert();
        });

        waitForProcessing(4);

        assertThat(DEPRECABLE_ENTITY_SEARCHER.search("Sean").size()).isEqualTo(2);

        assertThat(
                DEPRECABLE_ENTITY_SEARCHER.search(
                    DEPRECABLE_ENTITY_SEARCHER.FIRST_NAME.eq("Sean").and(DEPRECABLE_ENTITY_SEARCHER.deprecated().eq(false)))
                                          .size()).isEqualTo(2);

        runInTransaction(() -> DeprecableEntity.findOrFail(242345).deprecate(true).update());

        waitForProcessing(1);

        runInTransaction(() -> {
            final DeprecableEntity de = DeprecableEntity.findOrFail(242345);
            assertThat(de.isDeprecated()).isTrue();
            assertThat(de.getDeprecationUser()).isEqualTo(TEST_USER);

            assertThat(
                DEPRECABLE_ENTITY_SEARCHER.search(
                    DEPRECABLE_ENTITY_SEARCHER.FIRST_NAME.eq("Sean").and(DEPRECABLE_ENTITY_SEARCHER.deprecated().eq(false)))
                                          .size()).isEqualTo(1);
            assertThat(
                DEPRECABLE_ENTITY_SEARCHER.search(
                    DEPRECABLE_ENTITY_SEARCHER.FIRST_NAME.eq("Sean").and(DEPRECABLE_ENTITY_SEARCHER.deprecated().eq(true)))
                                          .size()).isEqualTo(1);

            de.deprecate(false).update();
        });

        waitForProcessing(1);

        runInTransaction(() -> {
            assertThat(
                DEPRECABLE_ENTITY_SEARCHER.search(
                    DEPRECABLE_ENTITY_SEARCHER.FIRST_NAME.eq("Sean").and(DEPRECABLE_ENTITY_SEARCHER.deprecated().eq(false)))
                                          .size()).isEqualTo(2);

            final DeprecableEntity de = DeprecableEntity.findOrFail(242345);
            assertThat(de.isDeprecated()).isFalse();
            assertThat(de.getDeprecationUser()).isNull();
        });
    }  // end method testDeprecationBasics

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ZDeprecationTest
