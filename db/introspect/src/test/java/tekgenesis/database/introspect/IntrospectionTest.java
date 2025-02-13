
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.codegen.sql.MetaModelFromSql;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Functions;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.DbIntrospector;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.InvalidFieldNameException;
import tekgenesis.properties.SchemaProps;
import tekgenesis.repository.ModelRepository;
import tekgenesis.util.MMDumper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.tools.test.DbTests.loadSql;
import static tekgenesis.database.DatabaseType.HSQLDB_NOSEQ;
import static tekgenesis.database.DatabaseType.POSTGRES;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test Introspection.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class IntrospectionTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DatabaseRule db = new DatabaseRule() {
            @Override protected void before() {
                createDatabase(dbName);
                final DatabaseType dbType = database.getDatabaseType();
                final Database     dbs    = database.asSystem();

                runInTransaction(() -> dbType.dropSchema(dbs, schemaName, true));
                runInTransaction(() -> dbType.createSchema(dbs, schemaName, env.get(schemaName, SchemaProps.class).tableTablespace));
                runInTransaction(() -> database.sqlStatement(ensureNotNull(loadSql(TEST_SQL))).executeScript());
            }

            @Override protected void after() {
                database.close();
                openDatabase(dbName);
                final Database     dbs    = database.asSystem();
                final DatabaseType dbType = database.getDatabaseType();
                runInTransaction(() -> dbType.dropSchema(dbs, schemaName, true));
            }
        };

    // private Database db = null;

    @Rule public MuteRule mute = new MuteRule();

    //~ Methods ......................................................................................................................................

    @Test public void dumpAndCompare()
        throws IOException
    {
        outputDir.mkdirs();
        final File out = new File(outputDir, "MetadataDatabase.sql");
        try(final FileWriter writer = new FileWriter(out);
            final DbIntrospector dbIntrospector = DbIntrospector.forDatabase(db.getDatabase()))
        {
            final SchemaInfo schema = dbIntrospector.getSchema(schemaName());
            schema.dumpSql(writer, false);
        }
        Tests.checkDiff(out, new File(TEST_SQL_FILE));
    }

    @Test public void generateMm()
        throws IOException, DuplicateFieldException, InvalidFieldNameException
    {
        outputDir.mkdirs();
        final File out = new File(outputDir, "MetadataDatabase.mm");

        try(final FileWriter writer = new FileWriter(out);
            final DbIntrospector dbIntrospector = DbIntrospector.forDatabase(db.getDatabase()))
        {
            final ModelRepository repository = new MetaModelFromSql("tekgenesis.test").createRepository(dbIntrospector.getSchema(schemaName()));
            final MMDumper        dumper     = MMDumper.createDumper(repository).models(repository.getModels()).withPackage();
            dumper.toWriter(writer);
        }
        Tests.checkDiff(out, new File(TEST_MM_FILE));
    }

    @Test public void introspect() {
        // List Schemas
        try(final DbIntrospector i = DbIntrospector.forDatabase(db.getDatabase())) {
            assertThat(i.supportsCatalogs()).isEqualTo(db.getDatabaseType().getType() == DatabaseType.HSQLDB);
            assertThat(i.supportsSchemas()).isTrue();
            assertThat(i.getIdentifierQuoteString()).isEqualTo("\"");

            assertThat(i.listSchemaNames()).contains(schemaName());

            final SchemaInfo schema = i.getSchema(schemaName());
            assertThat(i.getSchemas()).contains(schema);

            assertThat(i.getSchemas()).extracting("plainName").contains(schemaName);

            // List Table Types
            assertThat(i.getTableTypes()).contains(TableType.TABLE, TableType.VIEW);
            assertThat(schema.getViews().size()).isEqualTo(1);

            // List Table names
            // assertThat(schema.getTables()).hasSize(10);
            assertThat(schema.getTables()).extracting("name").contains("ADDRESS", "ADDRESS_PHONE");
            assertThat(schema.getTables()).extracting("plainName").contains("MODEL.ADDRESS", "MODEL.ADDRESS_PHONE");

            // Table columns

            introspectColumns(schema);

            // Primary Key
            assertThat(schema.getTable("TYPES").get().getPrimaryKey().isUndefined()).isFalse();

            // Indices
            final TableInfo person = schema.getTable("PERSON").get();
            assertThat(person.getIndices()).hasSize(2);
            final TableInfo.Index byName = person.getIndex("PERSON_NAME");
            assertThat(byName.isUnique()).isFalse();
            assertThat(byName.getColumns().toStrings()).containsExactly("LAST_NAME", "FIRST_NAME");

            assertThat(person.getIndex("PERSON_UPD").isUnique()).isTrue();

            // Foreign keys

            final TableInfo address = i.getTable(createQName(schemaName(), "PERSON_ADDRESS")).get();
            assertThat(address.getForeignKeys()).hasSize(1);
            final TableInfo.ForeignKey fk = address.getForeignKey("FK_PERSON");
            assertThat(fk.getReferencedTable()).isEqualTo(createQName(schemaName(), "PERSON"));
            assertThat(fk.getColumns()).hasSize(2);
            assertThat(fk.getColumns().toStrings()).containsExactly("DOC_TYPE=DOC_TYPE", "DOC_CODE=DOC_CODE");

            final TableInfo participant = schema.getTable("PARTICIPANT").get();
            assertThat(participant.getForeignKeys().getFirst().get().getColumns().toStrings()).containsExactly("CODE=ADDRESS_CODE");

            // Sequences
            final DatabaseType dbType = db.getDatabaseType();
            if (dbType != HSQLDB_NOSEQ && dbType != POSTGRES) {
                assertThat(schema.getSequences()).hasSize(5);
                final SequenceInfo seq = schema.getSequence("SEQUENCER1_SEQ");
                assertThat(seq.getInc()).isEqualTo(1);
                assertThat(seq.isCycle()).isFalse();
            }

            // Uniques and constraints
            final TableInfo category = i.getTable(createQName(schemaName(), "CATEGORY")).get();

            assertThat(category.getUniques()).hasSize(1);
            final ImmutableList<TableInfo.IndexColumn> columns = category.getUnique("NAME_UNQT").getColumns();
            assertThat(columns).hasSize(1);
            assertThat(columns.get(0).getName()).isEqualTo("NAME");
            assertThat(category.getColumns()).hasSize(4);
        }
    }  // end method introspect

    private void introspectColumns(final SchemaInfo schema) {
        final TableInfo t = schema.getTable("ADDRESS").get();
        assertThat(t.getColumns()).hasSize(4);
        assertThat(t.getColumns()).extracting("name").containsExactly("CODE", "ROOM", "STREET", "UPDATE_TIME");

        final TableInfo person = schema.getTable("PERSON").get();
        assertThat(person.getColumn("SALARY").getType().format()).isEqualTo("decimal(10,2)");
        assertThat(person.getColumn("BIRTHDAY").getType().format()).isEqualTo("date");
        assertThat(person.getColumn("DOC_TYPE").getType().format()).isEqualTo("int");

        final TableInfo.PrimaryKey pk = person.getPrimaryKey();
        assertThat(pk.getName()).isEqualTo("PK_PERSON");
        assertThat(pk.getColumns()).extracting("name").containsExactly("DOC_TYPE", "DOC_CODE");

        final TableInfo.Column seqId = schema.getTable("SEQUENCER2").get().getColumn("ID");
        assertThat(seqId.isSerial()).isTrue();
        assertThat(seqId.getType().getSqlKind()).isEqualTo(SqlKind.BIGINT);

        final TableInfo types = schema.getTable("TYPES").get();
        assertThat(types.getColumns().map(Functions.mkString()))  //
        .containsExactly("INT1                              int              default 1 not null",
            "NUM3                              decimal(3)",
            "NUM92                             decimal(9,2)     default 2.10",
            "REAL1                             double           default 1.1",
            "DATE1                             date             default CurrentDate",
            "DT0                               datetime(0)      default CurrentTime",
            "DT1                               datetime(1)",
            "DT3                               datetime(3)",
            "DT6                               datetime(6)",
            "BOOL                              boolean          default False CheckBoolConstraint(TYPES_BOOL_B, BOOL)",
            "STR                               nvarchar(10)     default EmptyString");
    }

    private String schemaName() {
        return db.getDatabase().getConfiguration().schemaPrefix + schemaName;
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    private static final String TEST_SQL      = "MetadataDatabase.sql";
    private static final String TEST_SQL_FILE = "db/introspect/src/test/resources/" + TEST_SQL;
    private static final String TEST_MM_FILE  = "db/introspect/src/test/resources/MetadataDatabases.mm";
    private static final String schemaName    = "MODEL";
    private static final File   outputDir     = new File("target/db/introspect/test-output");
}  // end class IntrospectionTest
