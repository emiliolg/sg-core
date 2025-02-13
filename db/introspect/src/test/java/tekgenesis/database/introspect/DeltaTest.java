
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

import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.database.DbIntrospector;
import tekgenesis.database.introspect.delta.DeltaGenerator;
import tekgenesis.database.introspect.delta.MdDelta;
import tekgenesis.database.introspect.delta.TableDeltas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.runners.Parameterized.Parameter;

import static tekgenesis.database.introspect.TableInfo.Element.*;

/**
 * Test Introspection.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class DeltaTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    //~ Methods ......................................................................................................................................

    @Test public void createDelta()
        throws IOException
    {
        final SchemaInfo     schema1 = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL);
        final SchemaInfo     schema2 = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL_V2);
        final DeltaGenerator dg      = new DeltaGenerator(schema1, schema2);
        final DeltaGenerator dg2     = new DeltaGenerator(schema2, schema1);

        final MdDelta diffSequences = dg.diffSequences();
        assertThat(diffSequences.getFromOnly()).containsOnly("DUMMY_SEQ", "JOSE_SEQ", "SEQUENCER2_SEQ", "LOCATION_SEQ");

        assertThat(diffSequences.isMinor()).isFalse();
        assertThat(dg2.diffSequences().isMinor()).isTrue();

        final TableDeltas tableDeltas = dg.diffTables();

        assertThat(tableDeltas.isEmpty()).isFalse();

        assertThat(tableDeltas.isMinor()).isFalse();

        assertThat(tableDeltas.getFromOnly()).containsExactly("A_WITH_INNER", "DUMMY", "DUMMY_INNER", "INNER_A", "PERSON_ADDRESS", "SEQUENCER2");
        assertThat(tableDeltas.getToOnly()).containsExactly("BASE_PRODUCT");
        assertThat(tableDeltas.getChanged()).containsExactly("ADDRESS_PHONE",
            "CATEGORY",
            "JOSE",
            "LOCATION",
            "PARTICIPANT",
            "PERSON",
            "PRODUCT",
            "SERIAL_NUMBER",
            "TYPES");
        assertThat(tableDeltas.getRenamed()).containsExactly(entry("TYPES", "TYPES_NEW"));

        assertThat(tableDeltas.diff("PARTICIPANT", COLUMN).getFromOnly()).containsExactly("ADDRESS_CODE");

        final MdDelta personIndices = tableDeltas.diff("PERSON", INDEX);

        assertThat(personIndices.getChanged()).containsExactly("PERSON_NAME");
        assertThat(personIndices.getRenamed()).containsExactly(entry("PERSON_UPD", "PERSON_UPD2"));

        final MdDelta apFks = tableDeltas.diff("ADDRESS_PHONE", FOREIGN_KEY);
        assertThat(apFks.getFromOnly()).containsExactly("FK_PERSON_ADDRESS");

        final MdDelta snFks = tableDeltas.diff("SERIAL_NUMBER", FOREIGN_KEY);
        assertThat(snFks.getChanged()).containsExactly("LOCATION_SERIAL_NUMBER_FK");

        assertThat(tableDeltas.diff("PRODUCT", CHECK).isEmpty()).isTrue();

        assertThat(tableDeltas.primaryKeyChange("TYPES")).isTrue();
        assertThat(tableDeltas.primaryKeyChange("ADDRESS_PHONE")).isTrue();

        final MdDelta diffViews = dg.diffViews();
        assertThat(diffViews.isEmpty()).isFalse();
        assertThat(diffViews.getChanged()).containsExactly("PRODUCT_SQL_VIEW");

        outputDir.mkdirs();
        final File out = new File(outputDir, "MetadataDatabase-Delta.sql");
        try(final FileWriter writer = new FileWriter(out)) {
            dg.generate(writer);
        }
        Tests.checkDiff(out, TEST_SQL_DELTA);
    }

    @Test public void createFromFile()
        throws IOException
    {
        final SchemaInfo schema = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL);
        assertThat(schema.getTables()).extracting("name").contains("ADDRESS", "ADDRESS_PHONE");
    }

    @Test public void testDelta() {
        final SchemaInfo     created     = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL_V2);
        final SchemaInfo     evolved     = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL, TEST_SQL_DELTA);
        final DeltaGenerator dg          = new DeltaGenerator(created, evolved);
        final TableDeltas    tableDeltas = dg.diffTables();
        assertThat(tableDeltas.getChanged()).hasSize(0);
        assertThat(tableDeltas.isEmpty()).isTrue();
    }

    @Test public void testMinor()
        throws IOException
    {
        final SchemaInfo     schema1 = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL);
        final SchemaInfo     schema2 = DbIntrospector.introspectSchema(schemaName, Colls.emptyIterable(), TEST_SQL_MINOR);
        final DeltaGenerator dg      = new DeltaGenerator(schema1, schema2);
        final DeltaGenerator dg2     = new DeltaGenerator(schema2, schema1);

        assertThat(dg.diffSequences().isMinor()).isTrue();
        assertThat(dg.diffTables().isMinor()).isTrue();

        assertThat(dg2.diffTables().isMinor()).isFalse();
    }

    //~ Static Fields ................................................................................................................................

    private static final File   TEST_SQL       = new File("db/introspect/src/test/resources/MetadataDatabase.sql");
    private static final File   TEST_SQL_V2    = new File("db/introspect/src/test/resources/MetadataDatabase_V2.sql");
    private static final File   TEST_SQL_MINOR = new File("db/introspect/src/test/resources/MetadataDatabase_minor.sql");
    private static final File   TEST_SQL_DELTA = new File("db/introspect/src/test/resources/MetadataDatabase_Delta.sql");
    private static final String schemaName     = "MODEL";
    private static final File   outputDir      = new File("target/db/introspect/test-output");
}  // end class DeltaTest
