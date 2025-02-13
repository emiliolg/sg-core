
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.test;

import java.io.File;
import java.io.FileFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.util.MMDumper;

import static java.util.Arrays.asList;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.seq;

@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class ModelRepositoryDumpTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public File file = null;

    //~ Methods ......................................................................................................................................

    @Test public void testDump() {
        final ModelRepository repository = new ModelRepositoryLoader(new File(file, "src/main/mm")).build();

        dumpRepository(repository, OUTPUT_DIR.getPath() + "/" + file.getName(), false);

        for (final String domain : repository.getDomains()) {
            final String outName    = file.getName() + "/" + domain.replace(".", "/") + "/MetaModel.mm";
            final File   outFile    = new File(OUTPUT_DIR, outName);
            final File   goldenFile = new File(new File(MM_DATA_DIR, "mm"), file.getName() + "-" + domain + ".mm");

            Tests.checkDiff(outFile, goldenFile);
        }
    }

    @Test public void testDumpFull() {
        final ModelRepository repository = new ModelRepositoryLoader(new File(file, "src/main/mm")).build();

        dumpRepository(repository, OUTPUT_DIR.getPath() + "/" + file.getName() + "/full", true);

        for (final String domain : repository.getDomains()) {
            final String outName    = file.getName() + "/full/" + domain.replace(".", "/") + "/MetaModel.mm";
            final File   outFile    = new File(OUTPUT_DIR, outName);
            final File   goldenFile = new File(new File(MM_DATA_DIR, "mm"), file.getName() + "-" + domain + ".mm.full");

            Tests.checkDiff(outFile, goldenFile);
        }
    }

    /** Generate the files. */
    private void dumpRepository(ModelRepository repository, String outDir, Boolean full) {
        final File output = new File(outDir);

        for (final String domain : repository.getDomains()) {
            final File dir = new File(output, domain.replace('.', File.separatorChar));
            dir.mkdirs();

            final MMDumper dumper = MMDumper.createDumper(repository).models(repository.getModels(domain)).withPackage();
            if (full) dumper.full();
            dumper.toFile(new File(dir, "MetaModel.mm"));
        }
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "{0}")
    public static Seq<Object[]> listFiles() {
        final File   fs = new File(new File("samples"), ".");
        final File[] a  = fs.listFiles(filter());
        return a != null ? seq(asList(a)).map(f -> new Object[] { f }) : emptyIterable();
    }

    private static FileFilter filter() {
        return path ->
               path.isDirectory() && ("enums".equals(path.getName()) || "views".equals(path.getName()) || "models".equals(path.getName())) &&
               !"another".equals(path.getName()) && !"sequence".equals(path.getName()) && !"showcase".equals(path.getName());
    }

    //~ Static Fields ................................................................................................................................

    private static final File MM_DATA_DIR = new File("metadata/util/src/test/data");
    private static final File OUTPUT_DIR  = new File("target/metadata/util/test-output/mm");
}  // end class ModelRepositoryDumpTest
