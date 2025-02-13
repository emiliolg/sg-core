
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.parser.ASTNode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

/**
 * Migrated from Scala.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class SchemaCompilerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public File file = null;

    //~ Methods ......................................................................................................................................

    @Test public void treeGeneration()
        throws FileNotFoundException
    {
        final Tests.GoldenTest g = Tests.goldenCreate(file, OUTPUT_DIR);

        final SchemaCompiler builder = SchemaCompiler.createSchemaCompiler(file);
        final SchemaAST      parse   = builder.parse();

        assertThat(builder.getMessages()).isEmpty();
        ASTNode.Utils.printTree(parse, new PrintWriter(g.getOutputFile()));

        g.check();
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "{0}")
    public static Seq<Object[]> listFiles() {
        return Tests.listFiles(DATA_DIR, ".*sc");
    }

    //~ Static Fields ................................................................................................................................

    private static final String DATA_DIR   = "ix/parser/src/test/data";
    private static final String OUTPUT_DIR = "target/ix/parser/test-output/ast";
}  // end class SchemaCompilerTest
