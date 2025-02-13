
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
import java.util.List;

import org.junit.Test;

import tekgenesis.codegen.html.HtmlFactoryCodeGenerator;
import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.codegen.project.StructTypeGenerationTest.createSimpleProductType;
import static tekgenesis.common.tools.test.Tests.checkDiff;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class HtmlFactoryCodeGenerationTest {

    //~ Methods ......................................................................................................................................

    @Test public void factoryClassGeneration()
        throws BuilderException
    {
        final File outputDir = new File("target/parser/metamodel/test-output/html").getAbsoluteFile();

        final ModelRepository repository = new ModelRepository();
        repository.add(createSimpleProductType());

        final File       generatedSourcesDir = new File(outputDir, "src_generated");
        final List<File> generated           = HtmlFactoryCodeGenerator.generate(repository,
                generatedSourcesDir,
                new File("codegen/metamodel/src/test/data/html"));
        assertThat(generated).hasSize(1);

        final File golden = new File("codegen/metamodel/src/test/data/tekgenesis/test/ViewsSimpleFactory.j");
        checkDiff(generated.get(0), golden);
    }

    @Test public void projectBuilderWithFactoryGeneration()
        throws IOException
    {
        final File outputDir = new File("target/parser/metamodel/test-output/html").getAbsoluteFile();
        final File rootDir   = new File("parser/metamodel/src/test/data/templates/mm");

        final File generatedSourcesDir = new File(outputDir, "src_generated");

        final ModelRepository repository = buildModelRepository(new BuilderErrorListener.Default(), rootDir);

        final ProjectBuilder builder   = new ProjectBuilder(repository, rootDir, outputDir, generatedSourcesDir, Colls.emptyIterable());
        final List<File>     generated = builder.buildProject();
        assertThat(generated).hasSize(1);

        final File golden = new File("codegen/metamodel/src/test/data/tekgenesis/test/ViewsFactory.j");
        checkDiff(generated.get(0), golden);
    }

    private ModelRepository buildModelRepository(BuilderErrorListener errors, File metamodels) {
        final ModelRepositoryLoader loader = new ModelRepositoryLoader(metamodels);
        loader.withErrorListener(errors);
        return loader.build();
    }
}  // end class HtmlFactoryCodeGenerationTest
