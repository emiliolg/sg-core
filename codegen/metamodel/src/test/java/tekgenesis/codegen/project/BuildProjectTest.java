
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
import java.io.UncheckedIOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Files;

import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.tools.test.Tests.diff;
import static tekgenesis.common.tools.test.Tests.wrapForParameters;
import static tekgenesis.common.util.Files.normalize;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class BuildProjectTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public File file       = null;
    private final File     goldenBase = new File("codegen/metamodel/src/test/data");

    private final File outputDir = normalize(new File("target/parser/metamodel/test-output/projectBuild").getAbsoluteFile());

    //~ Methods ......................................................................................................................................

    @Test public void build()
        throws IOException
    {
        final boolean force     = Boolean.getBoolean("golden.force");
        final File    rootDir   = new File(file, "src/main/mm");
        final File    out       = new File(outputDir, file.getName());
        final File    sourceDir = new File(out, "src");
        final File    goldenDir = new File(goldenBase, file.getPath());

        // Clean source directory to force generation
        Files.remove(sourceDir);

        final ProjectBuilder builder = new ProjectBuilder(rootDir, out, sourceDir);
        builder.withSourcesDir(sourceDir);

        final Seq<File> files      = seq(builder.buildProject());
        final int       beginIndex = sourceDir.getPath().length() + 1;

        final Seq<String> diffs = files.flatMap(f -> {
                    final File golden = Files.changeExtension(new File(goldenDir, f.getPath().substring(beginIndex)), ".j");
                    return force ? copy(f, golden) : diff(f, golden);
                }).toList();

        if (!diffs.isEmpty()) fail(diffs.mkString("\n"));
    }

    public Option<String> copy(File f, File golden) {
        try {
            Files.copy(f, golden, true);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return empty();
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "build {0}")
    public static Seq<Object[]> listProjects() {
        return wrapForParameters(new File("samples").listFiles(path -> path.isDirectory() && new File(path, "src").exists()));
    }
}  // end class BuildProjectTest
