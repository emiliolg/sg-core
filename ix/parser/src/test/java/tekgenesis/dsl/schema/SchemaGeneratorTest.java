
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tekgenesis.common.util.Files;

import static tekgenesis.common.util.Files.normalize;

/**
 * SchemaCodeGenerator Test.
 */
@RunWith(JUnit4.class)
public class SchemaGeneratorTest {

    //~ Instance Fields ..............................................................................................................................

    // private final File dataDir = new File("ix/parser/src/test/mm");
    private final File outputDir = normalize(new File("target/ix/parser/test-output").getAbsoluteFile());

    //~ Methods ......................................................................................................................................

    /** Package Builder. */
    @Test public void test() {
        final File sourceDir = new File(outputDir, "src");
        Files.remove(sourceDir);

        // Clean source directory to force generation
        Files.remove(sourceDir);
        final SchemaCodeGenerator generator = new SchemaCodeGenerator(new File("ix/parser/src/test/mm/tekgenesis/ix/ventas/ventas.sc"),
                sourceDir,
                sourceDir);
        generator.generate();

        final SchemaCodeGenerator auxGenerator = new SchemaCodeGenerator(new File("ix/parser/src/test/mm/tekgenesis/ix/aux/aux.sc"),
                sourceDir,
                sourceDir);
        auxGenerator.generate();
    }
}
