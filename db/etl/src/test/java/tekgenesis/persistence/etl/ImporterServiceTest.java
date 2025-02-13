
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.*;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.common.util.Files;
import tekgenesis.database.DatabaseType;
import tekgenesis.persistence.resource.DbResourceHandler;
import tekgenesis.test.basic.Author;
import tekgenesis.test.basic.Book;

import static java.lang.Math.max;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.test.basic.g.AuthorTable.AUTHOR;
import static tekgenesis.test.basic.g.BookTable.BOOK;
import static tekgenesis.test.basic.g.LibraryTable.LIBRARY;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class ImporterServiceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String  dbName      = null;
    private MemoryEnvironment environment = null;
    private DbResourceHandler rh          = null;

    @Rule public DbRule db = new DbRule(DbRule.SG, DbRule.BASIC, DbRule.BASIC_TEST, DbRule.SHOWCASE, DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(dbName);
                rh          = new DbResourceHandler(env, database);
                environment = env;
            }
        };

    @Rule public MuteRule mute = new MuteRule();

    //~ Methods ......................................................................................................................................

    /** Seed database from files in the specified directory. */
    public void seedDatabase(@NotNull String seedDir) {
        runInTransaction(() -> ImporterService.seedDatabase(environment, db.getTransactionManager(), rh, new File(seedDir), false));
    }

    @Test public void testImportService()
        throws IOException
    {
        runInTransaction(() -> {
            final File dataDir = createDataDir("import");
            addEmptyFiles(dataDir, "pepe", "jose", Author.class.getName() + ".pepe");
            createCsv(dataDir, Book.class, "title,authorId");

            runImport(dataDir);

            assertThat(selectFrom(AUTHOR).count()).isEqualTo(2);

            final Author author1 = assertNotNull(selectFrom(AUTHOR).orderBy(AUTHOR.ID).get());
            assertThat(author1.getName()).isEqualTo("Importer");
            assertThat(author1.getLastName()).isEqualTo("Test 2");

            final Author author2 = assertNotNull(Author.find(author1.getId() + 1));
            assertThat(author2.getName()).isEqualTo("Importer Model");
            assertThat(author2.getLastName()).isEqualTo("Test");
        });
    }

    @Test public void testImportServiceBadRecord()
        throws FileNotFoundException
    {
        runInTransaction(t -> {
            final File dataDir = createDataDir("importDefault");
            createCsv(dataDir, Author.class, "name,lastName", "Julio,Verne", "NULL,NULL", "Julio,Cortazar");

            runImport(dataDir);

            if (db.getDatabaseType() == DatabaseType.POSTGRES)
                assertThat(selectFrom(AUTHOR).count()).isEqualTo(0);  // postgress aborts transaction when constraint is violated
            else {
                assertThat(selectFrom(AUTHOR).count()).isEqualTo(2);
                final Author author = assertNotNull(selectFrom(AUTHOR).orderBy(AUTHOR.ID).get());
                assertThat(author.getName()).isEqualTo("Julio");
                assertThat(author.getLastName()).isEqualTo("Verne");
            }
            t.abort();
        });
    }                                                                 // end method testImportServiceBadRecord

    @Test public void testImportServiceDefault()
        throws FileNotFoundException
    {
        runInTransaction(() -> {
            final File dataDir = createDataDir("importDefault");
            createCsv(dataDir, Author.class, "name,lastName", "Julio,Verne");

            runImport(dataDir);

            assertThat(selectFrom(AUTHOR).count()).isEqualTo(1);

            final Author author = assertNotNull(selectFrom(AUTHOR).orderBy(AUTHOR.ID).get());
            assertThat(author.getName()).isEqualTo("Julio");
            assertThat(author.getLastName()).isEqualTo("Verne");
        });
    }

    @Test public void testImportServiceError()
        throws FileNotFoundException
    {
        runInTransaction(() -> {
            mute.mute(ImporterService.class);
            final File dataDir = createDataDir("importError");
            createCsv(dataDir, Author.class, "name,lastNames", "Julio,Verne");
            createCsv(dataDir, Book.class, "title,authorId");

            runImport(dataDir);

            assertThat(selectFrom(AUTHOR).count()).isZero();

            final File errors = new File(dataDir.getParent(), "error");
            assertThat(errors.exists()).isTrue();
            final File[] listFiles = assertNotNull(errors.listFiles());
            assertThat(listFiles).isNotEmpty();

            assertThat(listFiles[0].listFiles()).hasSize(2);
        });
    }

    @Test public void testSeed() {
        seedDatabase("db/etl/src/test/data");

        runInTransaction(t -> {
            assertThat(selectFrom(AUTHOR).count()).isEqualTo(2);

            final Author author10 = assertNotNull(Author.find(10));
            assertThat(author10.getName()).isEqualTo("Julio");
            assertThat(author10.getLastName()).isEqualTo("Verne");

            final Author author20 = assertNotNull(Author.find(20));
            assertThat(author20.getName()).isEqualTo("Julio");
            assertThat(author20.getLastName()).isEqualTo("Cortazar");

            Author.create().setName("Jorge Luis").setLastName("Borges").insert();

            final Author author21 = assertNotNull(Author.find(21));
            assertThat(author21.getName()).isEqualTo("Jorge Luis");
            assertThat(author21.getLastName()).isEqualTo("Borges");

            assertThat(selectFrom(LIBRARY).count()).isEqualTo(1);
            t.abort();
        });
    }

    @Test public void testSeed1Record() {
        seedDatabase("db/etl/src/test/data/import");

        runInTransaction(t -> {
            assertThat(selectFrom(AUTHOR).count()).isEqualTo(1);

            final Author author1 = assertNotNull(Author.find(1));
            assertThat(author1.getName()).isEqualTo("Julio");
            assertThat(author1.getLastName()).isEqualTo("Verne");

            Author.create().setName("Jorge Luis").setLastName("Borges").insert();

            final Author author2 = assertNotNull(Author.find(2));
            assertThat(author2.getName()).isEqualTo("Jorge Luis");
            assertThat(author2.getLastName()).isEqualTo("Borges");
            t.abort();
        });
    }

    @Test public void testSeedDefault() {
        seedDatabase("db/etl/src/test/data/importDefault");

        runInTransaction(t -> {
            assertThat(selectFrom(AUTHOR).count()).isEqualTo(2);

            final Author author1 = assertNotNull(selectFrom(AUTHOR).orderBy(AUTHOR.ID).get());
            assertThat(author1.getName()).isEqualTo("Julio");
            assertThat(author1.getLastName()).isEqualTo("Verne");

            final Author author2 = assertNotNull(Author.find(author1.getId() + 1));
            assertThat(author2.getName()).isEqualTo("Julio");
            assertThat(author2.getLastName()).isEqualTo("Cortazar");

            Author.create().setName("Jorge Luis").setLastName("Borges").insert();

            final Author author3 = assertNotNull(Author.find(author1.getId() + 2));
            assertThat(author3.getName()).isEqualTo("Jorge Luis");
            assertThat(author3.getLastName()).isEqualTo("Borges");
            t.abort();
        });
    }

    @Test public void testSeedMix() {
        seedDatabase("db/etl/src/test/data/importMix");
        runInTransaction(t -> {
            assertThat(selectFrom(AUTHOR).count()).isEqualTo(2);

            final Author author20 = assertNotNull(Author.find(20));
            assertThat(author20.getName()).isEqualTo("Julio");
            assertThat(author20.getLastName()).isEqualTo("Verne");

            final Author author = assertNotNull(selectFrom(AUTHOR).where(AUTHOR.ID.ne(20)).get());
            assertThat(author.getName()).isEqualTo("Julio");
            assertThat(author.getLastName()).isEqualTo("Cortazar");

            Author.create().setName("Jorge Luis").setLastName("Borges").insert();

            final Author authorLast = assertNotNull(Author.find(max(20, author.getId()) + 1));
            assertThat(authorLast.getName()).isEqualTo("Jorge Luis");
            assertThat(authorLast.getLastName()).isEqualTo("Borges");
            t.abort();
        });
    }

    @Test public void testSeedResources() {
        seedDatabase("db/etl/src/test/data/importResources");
        runInTransaction(() -> {
            assertThat(selectFrom(BOOK).count()).isEqualTo(1);
            final Book author = assertNotNull(selectFrom(BOOK).orderBy(BOOK.ID).get());

            final Resource resource = assertNotNull(author.getCover());
            assertThat(resource.getEntries()).hasSize(3);
            final Entry entry = assertNotNull(resource.getEntry("ext"));
            assertThat(entry.isExternal()).isTrue();
        });
    }

    private void runImport(File dataDir) {
        final ImporterService service = new ImporterService(environment, rh, dataDir.getParentFile());
        service.importFiles(dataDir.getName());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static void addEmptyFiles(File dataDir, String... files) {
        try {
            for (final String f : files)
                new File(dataDir, f).createNewFile();
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void createCsv(File dataDir, Class<?> entity, String header, String... lines) {
        try {
            final PrintWriter w = new PrintWriter(new File(dataDir, entity.getName() + ".csv"));
            w.println(header);
            for (final String line : lines)
                w.println(line);
            w.close();
        }
        catch (final FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static File createDataDir(String d) {
        final File dataDir = new File("target/db/etl/test-run", d);
        Files.remove(dataDir);
        dataDir.mkdirs();
        return dataDir;
    }
}  // end class ImporterServiceTest
