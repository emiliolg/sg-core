
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.memetix.mst.language.Language;

import org.junit.BeforeClass;
import org.junit.Test;

import tekgenesis.lang.mm.document.DocumentationItem;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.translate.LanguageManager;
import tekgenesis.lang.mm.util.Retryable;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * Documentation Test Case.
 */
// NOTE: Intellij Test are based on JUNIT 3. We can modify them in order to use Junit4 :(
public class DocumentationTestCase extends MMProjectTest implements GoldenFiles {

    //~ Methods ......................................................................................................................................

    /** DocumentMM test. */
    @SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
    @Test public void testDocumentMM()
        throws FileNotFoundException
    {
        outputDir.mkdirs();
        for (final MetaModel f : getForms()) {
            // Combine languages and models into DocumentationItems
            final Language origin = LanguageManager.getInstance().getLanguage();
            testDocumentGeneration(f, origin);
            for (final Language language : LanguageManager.getInstance().consolidate().getTranslations())
                testDocumentGeneration(f, language);
        }
    }

    @Override protected String getProjectPath() {
        return TestMMGraph.PLUGIN_MM_TEST_PROJECT;
    }

    private void testDocumentGeneration(MetaModel f, Language lang)
        throws FileNotFoundException
    {
        final DocumentationItem generator = new DocumentationItem(f, lang, outputDir.getAbsolutePath());

        generator.process(new DummyRetry());

        final File outFile    = generator.getFile();
        final File goldenFile = new File(goldenDir, "help/" + f.getDomain().replaceAll("\\.", "/") + File.separator + outFile.getName());

        assertThat(TestUtil.checkDiff(outFile, goldenFile)).isTrue();
    }

    private List<MetaModel> getForms() {
        final List<MMFile> project = ProjectUtils.getAllMMFilesInProject(myProject);

        final List<MetaModel> ret = new ArrayList<>();

        for (final MMFile mmFile : project) {
            final PsiForm[] modelsByType = mmFile.getModelsByType(PsiForm.class);
            if (isNotEmpty(modelsByType)) {
                for (final PsiMetaModel<? extends MetaModel> ast : mmFile.getMetaModels()) {
                    if (ast != null && ast.getMetaModelKind() == MetaModelKind.FORM) {
                        final MetaModel mm = ast.getModelOrNull();
                        if (mm != null) ret.add(mm);
                    }
                }
            }
        }
        return ret;
    }

    //~ Methods ......................................................................................................................................

    /** Init Test.* */
    @BeforeClass public static void initTest() {
        outputDir.mkdirs();
    }

    //~ Static Fields ................................................................................................................................

    private static final File outputDir = new File("target/plugin/mm/test-output/markdown");

    //~ Inner Classes ................................................................................................................................

    class DummyRetry implements Retryable {
        @Override public boolean shouldRetry() {
            return true;
        }

        @Override public void stopRetrying() {}
    }
}  // end class DocumentationTestCase
