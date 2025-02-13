
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class BigRepoTest {

    //~ Methods ......................................................................................................................................

    @Test public void loadBigRepository()
        throws IOException
    {
        final File outputDir = new File("target/parser/metamodel/test-output/big").getAbsoluteFile();

        final File src = new File(outputDir, "src");
        if (!src.exists()) src.mkdirs();

        final File           rootDir             = new File("parser/metamodel/src/test/data/generatedModels");
        final File           generatedSourcesDir = new File(outputDir, "generatedsrc");
        final ProjectBuilder builder             = new ProjectBuilder(rootDir, outputDir, generatedSourcesDir);
        builder.withSourcesDir(src).withForceBaseGeneration();
        assertThat(builder.buildProject().isEmpty()).isFalse();
    }
}
