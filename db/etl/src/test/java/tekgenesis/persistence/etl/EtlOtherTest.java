
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
import java.io.FileNotFoundException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.LoggerCollectRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.persistence.etl.EntityEtl.csv;
import static tekgenesis.persistence.etl.EntityExporter.export;
import static tekgenesis.persistence.etl.EntityImporter.load;
import static tekgenesis.persistence.etl.EtlTests.*;
import static tekgenesis.test.basic.g.IdeafixProductTable.IDEAFIX_PRODUCT;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class EtlOtherTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public LoggerCollectRule collector = new LoggerCollectRule(EntityCsvInput.class, EntityXmlReader.class);
    @Parameter public String       dbName    = null;
    @Rule public DbRule            db        = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void csvImportExport()
        throws FileNotFoundException
    {
        runInTransaction(() -> load(IDEAFIX_PRODUCT).using(csv().withFieldSeparator("\t")).updateOrInsert().from(new File(dataDir, "catalogo.tab")));

        final String[] expected = {
            "Insufficient number of fields in line: [GRUPO_GARBARINO, 00111, 0550001, N.BOOK\" EXTENSA 550 CD 575, TEXAS INSTRUMENTS]",
            "Error while processing field 'codflia' value '0260002+'\n" + "java.lang.NumberFormatException: For input string:" +
            " \"0260002+\""
        };

        assertThat(collector.getLoggerErrors()).hasSize(2).containsExactly(expected[0], expected[1]);

        final File outFile = new File(outDir, "catalogo.csv");
        runInTransaction(() ->
                export(IDEAFIX_PRODUCT).fields(IDEAFIX_PRODUCT.CODADM, IDEAFIX_PRODUCT.CODFLIA, IDEAFIX_PRODUCT.COD_NAME, IDEAFIX_PRODUCT.BRAND)
                                       .using(csv())
                                       .into(outFile));
        checkFile(outFile);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class EtlOtherTest
