
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.localizer.test;
// ...............................................................................................................................
//
// (C) Copyright  2011/2014 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.metadata.common.Localizer;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.tools.test.Tests.GoldenTest;
import static tekgenesis.common.tools.test.Tests.wrapForParameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class LocalizerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public File project = null;

    //~ Methods ......................................................................................................................................

    @Test public void extractStrings() {
        final ModelRepositoryLoader loader     = new ModelRepositoryLoader(new File(project, "src/main/mm"));
        final ModelRepository       repository = loader.build();
        for (final MetaModel model : repository.getModels()) {
            final Map<String, String> strings = Localizer.stringsToLocalize(model);
            if (!strings.isEmpty()) {
                final String testName = model.getFullName() + PROPERTIES_EXT;

                final GoldenTest g = Tests.goldenCreate(testName, OUTPUT_DIR, GOLDEN_DIR);
                try(final PrintWriter pw = new PrintWriter(g.getOutputFile())) {
                    for (final Map.Entry<String, String> e : strings.entrySet())
                        pw.println(e.getKey() + " : " + e.getValue());
                }
                catch (final FileNotFoundException e) {
                    fail("Can't open output File: " + g.getOutputFile());
                }
                g.check();
            }
        }
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "{0}")
    public static Seq<Object[]> listProjects() {
        return wrapForParameters(new File("samples").listFiles(DIRS_WITH_SOURCES));
    }

    //~ Static Fields ................................................................................................................................

    private static final FileFilter DIRS_WITH_SOURCES = path -> path.isDirectory() && new File(path, "src").exists();

    private static final String OUTPUT_DIR = "target/codegen/test-output/build-project/";
    private static final String GOLDEN_DIR = "codegen/metamodel/src/test/data/i18n";
}  // end class LocalizerTest
