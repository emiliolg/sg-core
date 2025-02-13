
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.persistence.EntityListener;
import tekgenesis.test.basic.Author;
import tekgenesis.test.basic.Category;
import tekgenesis.test.basic.Store;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionContext;
import tekgenesis.transaction.TransactionListener;
import tekgenesis.transaction.TransactionListener.Operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.EntityListenerType.*;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.test.BasicTest.createCategory;
import static tekgenesis.test.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.transaction.Transaction.runInTransaction;
import static tekgenesis.transaction.TransactionListener.Operation.BEGIN;
import static tekgenesis.transaction.TransactionListener.Operation.COMMIT;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ListenerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DbRule db = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    private int checkUpdate;

    private int deleteCount;

    private final EntityListener<Category> DELETE_LISTENER = new EntityListener<Category>() {
            @Override public boolean invoke(final Category instance) {
                deleteCount++;
                return true;
            }
        };

    private int                          insertCount;
    private final EntityListener<Author> AUTHOR_INSERT_LISTENER = new EntityListener<Author>() {
            @Override public boolean invoke(final Author instance) {
                insertCount++;
                return true;
            }
        };

    private final EntityListener<Category> INSERT_LISTENER = new EntityListener<Category>() {
            @Override public boolean invoke(final Category instance) {
                insertCount++;
                return true;
            }
        };

    private int                          persistCount;
    private final EntityListener<Author> AUTHOR_PERSIST_LISTENER = new EntityListener<Author>() {
            @Override public boolean invoke(final Author instance) {
                persistCount++;
                return true;
            }
        };

    private final EntityListener<Category> PERSIST_LISTENER = new EntityListener<Category>() {
            @Override public boolean invoke(final Category instance) {
                persistCount++;
                return true;
            }
        };

    private int                          updateCount;
    private final EntityListener<Author> AUTHOR_UPDATE_LISTENER = new EntityListener<Author>() {
            @Override public boolean invoke(final Author instance) {
                updateCount++;
                return true;
            }
        };

    private final EntityListener<Author> AUTHOR_UPDATE_WITH_DIFF = new EntityListener.Update<Author>() {
            @Override public boolean invoke(final Author oldAuthor, final Author newAuthor) {
                checkUpdate++;
                return !oldAuthor.getName().equals(newAuthor.getName());
            }
        };

    private final EntityListener<Category> UPDATE_LISTENER = new EntityListener<Category>() {
            @Override public boolean invoke(final Category instance) {
                updateCount++;
                return true;
            }
        };

    private final EntityListener<Category> UPDATE_WITH_DIFF = new EntityListener.Update<Category>() {
            @Override public boolean invoke(final Category oldCat, final Category newCat) {
                checkUpdate++;
                return !oldCat.getDescr().equals(newCat.getDescr());
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void exerciseListeners() {
        Category.addListener(AFTER_PERSIST, PERSIST_LISTENER);
        Category.addListener(AFTER_INSERT, INSERT_LISTENER);
        Category.addListener(AFTER_UPDATE, UPDATE_LISTENER);

        // Create 2 categories;
        final Category c = createCategory(1, "Category-1", "Category-1 Description");
        createCategory(2, "Category-2", "Category-2 Description");
        c.setDescr("Cat-1").persist();

        Category.addListener(BEFORE_DELETE, DELETE_LISTENER);
        Category.addListener(AFTER_DELETE, DELETE_LISTENER);

        assertThat(persistCount).isEqualTo(3);
        assertThat(insertCount).isEqualTo(2);
        assertThat(updateCount).isEqualTo(1);
        persistCount = insertCount = updateCount = 0;

        Category.addListener(BEFORE_UPDATE, UPDATE_WITH_DIFF);
        final Category c1 = assertNotNull(Category.find(1));
        final Category c2 = assertNotNull(Category.find(2));
        c1.setDescr("Category-1").persist();
        c2.setDescr("Category-2 Description").persist();

        assertThat(persistCount).isEqualTo(1);
        assertThat(insertCount).isEqualTo(0);
        assertThat(updateCount).isEqualTo(1);
        assertThat(checkUpdate).isEqualTo(2);

        assertThat(selectFrom(CATEGORY).count()).isEqualTo(2);

        Category.forEach(Category::delete);

        assertThat(deleteCount).isEqualTo(4);

        createCategory(1, "Category-1", "Category-1 Description");
        createCategory(2, "Category-2", "Category-2 Description");

        Category.removeListener(BEFORE_DELETE, DELETE_LISTENER);
        Category.removeListener(AFTER_DELETE, DELETE_LISTENER);

        // Check that they are there, delete the first;
        assertThat(selectFrom(CATEGORY).count()).isEqualTo(2);

        Category.forEach(Category::delete);

        assertThat(deleteCount).isEqualTo(4);
    }  // end method exerciseListeners

    @Test public void exerciseListenersOnAutoKey() {
        AuthorInitializer.reset();

        Author.addListener(AFTER_PERSIST, AUTHOR_PERSIST_LISTENER);
        Author.addListener(AFTER_INSERT, AUTHOR_INSERT_LISTENER);
        Author.addListener(AFTER_UPDATE, AUTHOR_UPDATE_LISTENER);

        // Create 2 Authors
        final Author a = Author.create().setName("Author1").persist();
        Author.create().setName("Author2").persist();
        a.setName("A-1").persist();

        assertThat(persistCount).isEqualTo(3);
        assertThat(insertCount).isEqualTo(2);
        assertThat(updateCount).isEqualTo(1);
        persistCount = insertCount = updateCount = 0;

        Author.addListener(BEFORE_UPDATE, AUTHOR_UPDATE_WITH_DIFF);
        final Author a1 = assertNotNull(Author.find(a.getId()));
        final Author a2 = assertNotNull(Author.find(a.getId() + 1));
        a2.setName("Author2").persist();
        a1.setName("Au-1").persist();

        assertThat(persistCount).isEqualTo(1);
        assertThat(insertCount).isEqualTo(0);
        assertThat(updateCount).isEqualTo(1);
        assertThat(checkUpdate).isEqualTo(2);
        assertThat(AuthorInitializer.authorPersist).isEqualTo(4);
    }

    @Test public void reEntrantListener() {
        Store.addListener(BEFORE_PERSIST, instance -> {
                persistCount++;
                instance.insert();
                return true;
            });
        Store.create().insert();
        Store.create().insert();

        assertThat(persistCount).isEqualTo(2);
    }

    @Test public void reEntrantListenerByValue() {
        Category.addListener(BEFORE_PERSIST,
            instance -> {
                persistCount++;
                createCategory(instance.getIdKey(), instance.getName(), instance.getDescr());
                return true;
            });
        // Create 2 categories;
        createCategory(1, "Category-1", "Category-1 Description");

        createCategory(2, "Category-2", "Category-2 Description");

        assertThat(persistCount).isEqualTo(2);
    }

    @Test public void transactionListener() {
        Transaction.getCurrent().ifPresent(Transaction::commit);
        final TransactionListener<MyContext> listener = ListenerTest::transactionListener;
        Transaction.addListener(listener);

        //J-
        Category.addListener(BEFORE_PERSIST,
            category -> {
                final MyContext ctx = Transaction.getCurrent().map(t -> t.getContext(listener)).get();
                if (ctx.doInsert) return true;
                ctx.buffer.add(category);
                return false;
            });
        //J+

        // Create 3 categories;
        runInTransaction(() -> {
            createCategory(1, "Category-1", "Category-1 Description");
            createCategory(2, "Category-2", "Category-2 Description");
            createCategory(4, "Category-4", "Category-4 Description");

            assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).count()).isZero();
        });

        runInTransaction(() -> assertThat(select(CATEGORY.NAME).from(CATEGORY).list()).containsExactly("C1", "C2", "C4"));
        Transaction.removeListener(listener);
    }

    @Test public void vetoListener() {
        // Create 2 categories;
        Category.addListener(BEFORE_PERSIST, category -> category.getIdKey() % 2 == 0);

        createCategory(1, "Category-1", "Category-1 Description");
        createCategory(2, "Category-2", "Category-2 Description");
        createCategory(4, "Category-4", "Category-4 Description");

        assertThat(select(CATEGORY.ID_KEY).from(CATEGORY).list()).containsExactly(2, 4);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static MyContext transactionListener(final Operation op, final MyContext ctx) {
        if (op == BEGIN) return new MyContext();
        if (op == COMMIT) {
            ctx.doInsert = true;
            for (final Category c : ctx.buffer)
                c.setName("C" + c.getIdKey()).insert();
        }
        return ctx;
    }

    //~ Inner Classes ................................................................................................................................

    private static class MyContext implements TransactionContext {
        final List<Category> buffer   = new ArrayList<Category>();
        boolean              doInsert = false;
    }
}  // end class DeleteListenerTest
