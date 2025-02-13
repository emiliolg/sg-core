
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.File;

import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.test.basic.Author;
import tekgenesis.test.basic.BookStore;
import tekgenesis.test.basic.StoreLocation;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.etl.EntityEtl.csv;
import static tekgenesis.persistence.etl.EntityEtl.xml;
import static tekgenesis.test.basic.g.AuthorTable.AUTHOR;
import static tekgenesis.test.basic.g.BookStoreTable.BOOK_STORE;
import static tekgenesis.test.basic.g.StoreLocationTable.STORE_LOCATION;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class EtlAdvancedTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    final File           goldenDir = new File("db/etl/src/test/data");
    private final DbRule db        = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule r = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void testDelete() {
        final String fileName = "BookStore.csv";
        EntityImporter.load(BOOK_STORE).using(csv()).from(new File(goldenDir, fileName));

        final BookStore store = assertNotNull(BookStore.find(100));
        assertThat(store.getCodigo()).isEqualTo(100);
        assertThat(store.getName()).isEqualTo("Ateneo");

        final BookStore store2 = assertNotNull(BookStore.find(200));
        assertThat(store2.getCodigo()).isEqualTo(200);
        assertThat(store2.getName()).isEqualTo("Jenny");

        EntityImporter.load(BOOK_STORE).delete().using(csv()).from(new File(goldenDir, fileName));

        final BookStore store3 = BookStore.find(100);
        assertThat(store3).isNull();

        final BookStore store4 = BookStore.find(200);
        assertThat(store4).isNull();

        EntityImporter.load(BOOK_STORE).delete().using(csv()).from(new File(goldenDir, fileName));
    }

    @Test public void testDeleteDefault() {
        final String fileName = "Author2.csv";
        EntityImporter.load(AUTHOR).using(csv()).from(new File(goldenDir, fileName));

        final Author author = assertNotNull(Author.find(20));
        assertThat(author.getId()).isEqualTo(20);
        assertThat(author.getName()).isEqualTo("Julio");
        assertThat(author.getLastName()).isEqualTo("Verne");

        EntityImporter.load(AUTHOR).delete().using(csv()).from(new File(goldenDir, fileName));

        final Author author2 = Author.find(20);

        assertThat(author2).isNull();

        EntityImporter.load(AUTHOR).delete().using(csv()).from(new File(goldenDir, fileName));
    }

    @Test public void testDeleteDefaultXml() {
        final String fileName = "Author2.xml";
        EntityImporter.load(AUTHOR).using(xml()).from(new File(goldenDir, fileName));

        final Author author = assertNotNull(Author.find(20));
        assertThat(author.getId()).isEqualTo(20);
        assertThat(author.getName()).isEqualTo("Julio");
        assertThat(author.getLastName()).isEqualTo("Verne");

        EntityImporter.load(AUTHOR).delete().using(xml()).from(new File(goldenDir, fileName));

        final Author author2 = Author.find(20);

        assertThat(author2).isNull();

        EntityImporter.load(AUTHOR).delete().using(xml()).from(new File(goldenDir, fileName));
    }

    @Test public void testDeleteXML() {
        final String fileName = "BookStore.xml";
        EntityImporter.load(BOOK_STORE).using(xml()).from(new File(goldenDir, fileName));

        final BookStore store = assertNotNull(BookStore.find(100));
        assertThat(store.getCodigo()).isEqualTo(100);
        assertThat(store.getName()).isEqualTo("Ateneo");

        final BookStore store2 = assertNotNull(BookStore.find(200));
        assertThat(store2.getCodigo()).isEqualTo(200);
        assertThat(store2.getName()).isEqualTo("Jenny");

        EntityImporter.load(BOOK_STORE).delete().using(xml()).from(new File(goldenDir, fileName));

        final BookStore store3 = BookStore.find(100);
        assertThat(store3).isNull();
        final BookStore store4 = BookStore.find(200);
        assertThat(store4).isNull();

        EntityImporter.load(BOOK_STORE).delete().using(xml()).from(new File(goldenDir, fileName));
    }

    @Test public void testErrorXml() {
        try {
            final String fileName = "BookStoreError.csv";
            EntityImporter.load(BOOK_STORE).insert().using(xml()).from(new File(goldenDir, fileName));
            failBecauseExceptionWasNotThrown(XMLException.class);
        }
        catch (final XMLException ignore) {}
    }

    @Test public void testImportInners() {
        EntityImporter.load(BOOK_STORE).using(csv()).from(new File(goldenDir, "BookStore.csv"));
        EntityImporter.load(STORE_LOCATION).using(csv()).from(new File(goldenDir, "StoreLocation.csv"));

        final BookStore store = assertNotNull(BookStore.find(100));
        assertThat(store.getCodigo()).isEqualTo(100);
        assertThat(store.getName()).isEqualTo("Ateneo");
        assertThat(store.getAddress()).hasSize(2);
        final StoreLocation location = store.getAddress().getFirst().get();
        assertThat(location.getDir()).isEqualTo("Alem 123");
        final StoreLocation location2 = store.getAddress().get(1);
        assertThat(location2.getDir()).isEqualTo("Mitre 555");
    }

    @Test public void testImportInnersXml() {
        final String fileName = "BookStoreInner.xml";
        EntityImporter.load(BOOK_STORE).using(xml()).from(new File(goldenDir, fileName));

        final BookStore store = assertNotNull(BookStore.find(100));
        assertThat(store.getCodigo()).isEqualTo(100);
        assertThat(store.getName()).isEqualTo("Ateneo");
        assertThat(store.getAddress().size()).isEqualTo(2);
        final StoreLocation location = store.getAddress().getFirst().get();
        assertThat(location.getDir()).isEqualTo("Alem 123");
        final StoreLocation location2 = store.getAddress().get(1);
        assertThat(location2.getDir()).isEqualTo("Mitre 555");

        final BookStore bookStore = assertNotNull(BookStore.find(300));

        assertThat(bookStore.getCodigo()).isEqualTo(300);
        assertThat(bookStore.getName()).isEqualTo("Librito");
        assertThat(bookStore.getAddress()).hasSize(1);
        final StoreLocation location3 = bookStore.getAddress().getFirst().get();
        assertThat(location3.getDir()).isEqualTo("Alem 123");
    }

    @Test public void testImportWithDefaultNotSet() {
        final String fileName = "Author.csv";
        EntityImporter.load(AUTHOR).using(csv()).from(new File(goldenDir, fileName));

        final Author author = assertNotNull(getFirstAuthor());
        assertThat(author.getName()).isEqualTo("Julio");
        assertThat(author.getLastName()).isEqualTo("Verne");
    }
    @Test public void testImportWithDefaultNotSet2() {
        final String fileName = "Author3.csv";
        EntityImporter.load(AUTHOR).using(csv()).from(new File(goldenDir, fileName));

        final Author author = assertNotNull(getFirstAuthor());
        assertThat(author.getName()).isEqualTo("Julio");
        assertThat(author.getLastName()).isEqualTo("Verne");
    }

    @Test public void testImportWithDefaultNotSetXml() {
        final String fileName = "Author.xml";
        EntityImporter.load(AUTHOR).using(xml()).from(new File(goldenDir, fileName));

        final Author author = assertNotNull(getFirstAuthor());
        assertThat(author.getName()).isEqualTo("Julio");
        assertThat(author.getLastName()).isEqualTo("Verne");
    }

    @Test public void testImportWithDefaultSetButKept() {
        final String fileName = "Author2.csv";

        EntityImporter.load(AUTHOR).using(csv()).from(new File(goldenDir, fileName));

        assertThat(Author.find(1)).isNull();

        final Author auhor = assertNotNull(Author.find(20));
        assertThat(auhor.getName()).isEqualTo("Julio");
        assertThat(auhor.getLastName()).isEqualTo("Verne");
    }

    @Test public void testImportWithDefaultSetButKeptXml() {
        final String fileName = "Author2.xml";

        EntityImporter.load(AUTHOR).insert().using(xml()).from(new File(goldenDir, fileName));

        final Author author = Author.find(1);
        assertThat(author).isNull();

        final Author author2 = assertNotNull(Author.find(20));
        assertThat(author2.getName()).isEqualTo("Julio");
        assertThat(author2.getLastName()).isEqualTo("Verne");
    }

    @Test public void testUpdate() {
        EntityImporter.load(BOOK_STORE).using(csv()).from(new File(goldenDir, "BookStore.csv"));

        final BookStore store = assertNotNull(BookStore.find(100));
        assertThat(store.getCodigo()).isEqualTo(100);
        assertThat(store.getName()).isEqualTo("Ateneo");

        final BookStore store2 = assertNotNull(BookStore.find(200));
        assertThat(store2.getCodigo()).isEqualTo(200);
        assertThat(store2.getName()).isEqualTo("Jenny");

        EntityImporter.load(BOOK_STORE).update().using(csv()).from(new File(goldenDir, "BookStoreUpdate.csv"));

        final BookStore store3 = assertNotNull(BookStore.find(100));
        assertThat(store3.getCodigo()).isEqualTo(100);
        assertThat(store3.getName()).isEqualTo("El Ateneo");

        final BookStore store4 = assertNotNull(BookStore.find(200));
        assertThat(store4.getCodigo()).isEqualTo(200);
        assertThat(store4.getName()).isEqualTo("Jenny");

        EntityImporter.load(BOOK_STORE).updateOrInsert().using(csv()).from(new File(goldenDir, "BookStoreUpdate.csv"));

        final BookStore store5 = assertNotNull(BookStore.find(100));
        assertThat(store5.getCodigo()).isEqualTo(100);
        assertThat(store5.getName()).isEqualTo("El Ateneo");

        final BookStore store6 = assertNotNull(BookStore.find(200));
        assertThat(store6.getCodigo()).isEqualTo(200);
        assertThat(store6.getName()).isEqualTo("Jenny");
    }

    @Test public void testUpdateXML() {
        EntityImporter.load(BOOK_STORE).using(xml()).from(new File(goldenDir, "BookStore.xml"));

        final BookStore store = assertNotNull(BookStore.find(100));
        assertThat(store.getCodigo()).isEqualTo(100);
        assertThat(store.getName()).isEqualTo("Ateneo");

        final BookStore store2 = assertNotNull(BookStore.find(200));
        assertThat(store2.getCodigo()).isEqualTo(200);
        assertThat(store2.getName()).isEqualTo("Jenny");

        EntityImporter.load(BOOK_STORE).update().using(xml()).from(new File(goldenDir, "BookStoreUpdate.xml"));

        final BookStore store3 = assertNotNull(BookStore.find(100));
        assertThat(store3.getCodigo()).isEqualTo(100);
        assertThat(store3.getName()).isEqualTo("El Ateneo");

        final BookStore store4 = assertNotNull(BookStore.find(200));
        assertThat(store4.getCodigo()).isEqualTo(200);
        assertThat(store4.getName()).isEqualTo("Jenny");

        EntityImporter.load(BOOK_STORE).updateOrInsert().using(xml()).from(new File(goldenDir, "BookStore.xml"));

        final BookStore store5 = assertNotNull(BookStore.find(100));
        assertThat(store5.getCodigo()).isEqualTo(100);
        assertThat(store5.getName()).isEqualTo("Ateneo");

        final BookStore store6 = assertNotNull(BookStore.find(200));
        assertThat(store6.getCodigo()).isEqualTo(200);
        assertThat(store6.getName()).isEqualTo("Jenny");
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    @Nullable static Author getFirstAuthor() {
        return selectFrom(AUTHOR).orderBy(AUTHOR.ID).get();
    }
}  // end class EtlAdvancedTest
