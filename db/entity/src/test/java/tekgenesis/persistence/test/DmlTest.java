
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.DbTimeProviderRule;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.test.basic.Category;
import tekgenesis.transaction.Transaction;

import static java.lang.String.format;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.persistence.Sql.*;
import static tekgenesis.persistence.expr.Expr.CURRENT_TIME;
import static tekgenesis.test.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class DmlTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String              dbName       = null;
    @Rule public final DbTimeProviderRule timeProvider = new DbTimeProviderRule();
    private final DbRule                  db           = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void insertAndUpdate() {
        // Create Some categories;
        for (int i = 1; i <= 10; i++)
            createCategory(i, format("Category %s", i), i < 5 ? "Description 1" : "Description 2'");

        assertThat(selectFrom(CATEGORY).count()).isEqualTo(10);
        assertThat(selectFrom(CATEGORY).where(CATEGORY.NAME.like("%y 1%")).count()).isEqualTo(2);

        update(CATEGORY).set(CATEGORY.NAME, "Cat 1").where(CATEGORY.ID_KEY.eq(1)).execute();

        assertThat(selectFrom(CATEGORY).where(CATEGORY.NAME.like("%y 1%")).count()).isEqualTo(1);
        // if (db.getDatabaseType() == DatabaseType.POSTGRES) return;
        final int count = insertInto(CATEGORY).set(CATEGORY.ID_KEY, 1)
                          .set(CATEGORY.NAME, "Cat 1")
                          .set(CATEGORY.DESCR, "Cat 1")
                          .onConflict(CATEGORY.ID_KEY)
                          .doNothing()
                          .execute();

        assertThat(count).isEqualTo(0);

        assertThat(Category.findOrFail(1).getDescr()).isEqualTo("Description 1");

        final int count2 = insertInto(CATEGORY).set(CATEGORY.ID_KEY, 1)
                           .set(CATEGORY.NAME, "Cat 1")
                           .set(CATEGORY.DESCR, "Category #1")
                           .onConflict(CATEGORY.ID_KEY)
                           .update()
                           .set(CATEGORY.NAME, "Category 1")
                           .set(CATEGORY.DESCR, CATEGORY.DESCR.lower())
                           .where(CATEGORY.NAME.eq("Cat 1"))
                           .execute();

        assertThat(Category.findOrFail(1).getDescr()).isEqualTo("description 1");

        assertThat(count2).isEqualTo(1);

        final int count3 = insertInto(CATEGORY).set(CATEGORY.ID_KEY, 1)
                           .set(CATEGORY.NAME, "Cat 1")
                           .set(CATEGORY.DESCR, "Cat 1")
                           .onConflict(CATEGORY.ID_KEY)
                           .update()
                           .set(CATEGORY.NAME, "Any Name")
                           .where(CATEGORY.NAME.eq("Pepe"))
                           .execute();

        assertThat(count3).isEqualTo(0);

        assertThat(selectFrom(CATEGORY).where(CATEGORY.NAME.like("%y 1%")).count()).isEqualTo(2);
    }  // end method insertAndUpdate

    @Test public void selectForUpdate() {
        createCategory(1, "-", "++");
        Transaction.getCurrent().ifPresent(Transaction::commit);

        final ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++)
            executor.submit(this::updateName);
        shutdown(executor);

        assertThat(invokeInTransaction(() -> Category.findOrFail(1).getName())).isEqualTo(Strings.nChars('-', 11));
    }

    private void updateName() {
        runInTransaction(() -> selectFrom(CATEGORY).forUpdate().where(CATEGORY.ID_KEY.eq(1)).forEach(c -> c.setName(c.getName() + "-").update()));
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static void createCategory(int code, String nm, String desc) {
        insertInto(CATEGORY).set(CATEGORY.ID_KEY, code)
            .set(CATEGORY.NAME, nm)
            .set(CATEGORY.DESCR, desc)
            .set(CATEGORY.UPDATE_TIME, CURRENT_TIME)
            .execute();
    }

    private static void shutdown(ExecutorService executor) {
        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (final InterruptedException ignore) {}
    }
}  // end class DmlTest
