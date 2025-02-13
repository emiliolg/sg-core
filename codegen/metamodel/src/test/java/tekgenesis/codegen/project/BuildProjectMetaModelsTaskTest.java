
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
import java.util.Locale;
import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.util.FileFilters;
import tekgenesis.common.util.Files;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;

/**
 * Test Metamodels task generation.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class BuildProjectMetaModelsTaskTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public final TemporaryFolder tmp   = new TemporaryFolder();
    private final File                 mmDir = new File("codegen/metamodel/src/test/mm");

    //~ Methods ......................................................................................................................................

    @Test public void testMetamodelsRemoteGeneration()
        throws BuilderException, IOException
    {
        final File outputDir           = tmp.newFolder("classes");
        final File generatedSourcesDir = tmp.newFolder("src_managed");

        final BuilderErrorListener errors = new BuilderErrorListener.Default();

        final ModelRepository origin = buildModelRepository(errors, mmDir);
        assertThat(errors.hasErrors()).isFalse();
        assertThat(origin.getModels()).extracting("name")
            .containsOnly("EA",
                "EB",
                "EC",
                "ED",
                "EnumA",
                "EnumB",
                "FormA",
                "FormB",
                "FormRemote",
                "HA",
                "HB",
                "TA",
                "TB",
                "Sex",
                "PersonType",
                "WeekDay");

        // Assert label and localization
        final Form originForm = origin.getModel("tekgenesis.test.FormRemote", Form.class).get();
        assertThat(originForm.getLabel()).isEqualTo("Form Remote Label");
        final Form originLocalized = localizeForm(originForm);
        assertThat(originLocalized.getLabel()).isEqualTo("Formulario Remoto");

        final ProjectBuilder projectBuilder = new ProjectBuilder(origin, mmDir, outputDir, generatedSourcesDir, false, true, emptyIterable());
        projectBuilder.withProjectName("darpa");
        final List<File> files = projectBuilder.buildProject();

        // Assert java classes generated
        assertThat(files).extracting("name")
            .containsOnly("EnumA.java", "EnumB.java", "FormRemote.java", "WeekDay.java", "Sex.java", "PersonType.java");

        // Assert enum resources are copied
        final Seq<String> resources = Files.list(outputDir, FileFilters.withExtension(".properties")).map(FILE_NAME_ONLY);
        assertThat(resources).containsOnly("EnumA_es.properties", "EnumB_es.properties", "FormRemote_es.properties");

        // Build repository with generated sources and assert
        final ModelRepository generated = buildModelRepository(errors, outputDir);
        assertThat(errors.hasErrors()).isFalse();
        assertThat(generated.getModels()).extracting("name")
            .containsOnly("EA", "EB", "EC", "ED", "EnumA", "EnumB", "FormRemote", "TA", "TB", "WeekDay", "Sex", "PersonType");

        // Assert label and localization via copied resources
        final Form generatedForm = origin.getModel("tekgenesis.test.FormRemote", Form.class).get();
        assertThat(generatedForm.getLabel()).isEqualTo("Form Remote Label");
        final Form generatedLocalized = localizeForm(generatedForm);
        assertThat(generatedLocalized.getLabel()).isEqualTo("Formulario Remoto");
    }  // end method testMetamodelsRemoteGeneration

    private ModelRepository buildModelRepository(BuilderErrorListener errors, File metamodels) {
        final ModelRepositoryLoader loader = new ModelRepositoryLoader(metamodels);
        loader.withErrorListener(errors);
        return loader.build();
    }

    private Form localizeForm(Form form) {
        return localizer(form, new Locale("es")).localize();
    }

    //~ Static Fields ................................................................................................................................

    private static final Function<String, String> FILE_NAME_ONLY = f -> f.substring(f.lastIndexOf('/') + 1);
}  // end class BuildProjectMetaModelsTaskTest
