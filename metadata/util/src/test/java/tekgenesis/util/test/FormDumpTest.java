
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
import java.util.function.Predicate;

import org.junit.Test;

import tekgenesis.common.collections.Seq;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.util.MMDumper;

import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.tools.test.Tests.diff;
import static tekgenesis.type.MetaModelKind.FORM;
import static tekgenesis.type.MetaModelKind.WIDGET;

@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class FormDumpTest {

    //~ Methods ......................................................................................................................................

    @Test public void testUiModelDump() {
        final File directory = new File("samples", "showcase");

        final ModelRepository repository = new ModelRepositoryLoader(new File(directory, "src/main/mm")).build();

        dumpRepository(repository,
            OUTPUT_DIR.getPath() + "/" + directory.getName() + "/full",
            true,
            model -> model != null && (model.getMetaModelKind() == FORM || model.getMetaModelKind() == WIDGET));

        final Seq<String> diffs = repository.getDomains().flatMap(domain -> {
                    final String outName    = directory.getName() + "/full/" + domain.replace(".", "/") + "/MetaModel.mm";
                    final File   outFile    = new File(OUTPUT_DIR, outName);
                    final File   goldenFile = new File(new File(MM_DATA_DIR, "mm"), directory.getName() + "-" + domain + ".mm.full");
                    return diff(outFile, goldenFile);
                }).toList();

        if (!diffs.isEmpty()) fail(diffs.mkString("\n"));
    }

    /** Generate the files. */
    private void dumpRepository(ModelRepository repository, String outDir, Boolean full, Predicate<MetaModel> filter) {
        final File output = new File(outDir);

        for (final String domain : repository.getDomains()) {
            final File dir = new File(output, domain.replace('.', File.separatorChar));
            dir.mkdirs();

            final MMDumper dumper = MMDumper.createDumper(repository).models(repository.getModels(domain).filter(filter)).withPackage();
            if (full) dumper.full();
            dumper.toFile(new File(dir, "MetaModel.mm"));
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final File MM_DATA_DIR = new File("metadata/util/src/test/data");
    private static final File OUTPUT_DIR  = new File("target/metadata/util/test-output/mm");
}  // end class FormDumpTest
