
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
import java.io.IOException;
import java.io.PrintWriter;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.PsiTestCase;

import tekgenesis.lang.mm.actions.TidyFileAction;
import tekgenesis.lang.mm.facet.MMFacetType;

/**
 * Test for Actions used in plugin.*
 */
public class TestAction extends PsiTestCase implements GoldenFiles {

    //~ Instance Fields ..............................................................................................................................

    private final File errorsOutputDir = new File(TestUtil.TARGET_PLUGIN_MM_TEST_OUTPUT_ERRORS);

    //~ Methods ......................................................................................................................................

    /** Test the tidy action. */
    public void testTidy()
        throws IOException
    {
        final String fileString = TestUtil.readFile("plugin/mm/src/test/resources/actionFiles/tidyTest.mm");

        final Module module = createMainModule();

        ApplicationManager.getApplication().runWriteAction(() -> { FacetManager.getInstance(module).addFacet(new MMFacetType(), "mm", null); });

        module.getComponent(MMModuleComponent.class).initializeModelRepository();
        final VirtualFile moduleFile = module.getModuleFile();
        if (moduleFile != null) {
            final File srcFile = new File(moduleFile.getParent().getPath(), "../src/main/mm");
            srcFile.mkdirs();
            final File salesDir = new File(srcFile, "test");
            salesDir.mkdir();
            final VirtualFile src = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(srcFile);
            assert src != null;
            final PsiFile psiFile = TestUtil.createFile(module, TestUtil.findChild(src, "test"), "tidy.mm", fileString);

            final String outName = psiFile.getName() + ".tidy";
            final File   outFile = new File(errorsOutputDir, outName);

            final TidyFileAction tidyFileAction = new TidyFileAction();

            tidyFileAction.writeTidy(psiFile, new PrintWriter(outFile));

            final File goldenFile = new File(goldenDir, outName);
            assert (TestUtil.checkDiff(outFile, goldenFile));
        }
    }

    @Override protected void setUp()
        throws Exception
    {
        System.getProperties().remove(IDEA_PLATFORM_PREFIX);
        System.getProperties().put("idea.home.path", "/tmp");
        goldenDir.mkdirs();
        errorsOutputDir.mkdirs();
        super.setUp();
        myProject.getComponent(MMProjectComponent.class).projectOpened();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    static final String IDEA_PLATFORM_PREFIX = "idea.platform.prefix";
}  // end class TestAction
