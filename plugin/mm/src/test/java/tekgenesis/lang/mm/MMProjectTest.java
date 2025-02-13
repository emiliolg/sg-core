
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

import com.intellij.openapi.project.ex.ProjectManagerEx;
import com.intellij.testFramework.PsiTestCase;

import static tekgenesis.common.util.Files.copyDirectory;

/**
 * base class for Test that Use the Test Project in data.
 */
public abstract class MMProjectTest extends PsiTestCase {

    //~ Methods ......................................................................................................................................

    @Override protected void tearDown()
        throws Exception
    {
        if (myProjectManager != null && myProjectManager.canClose(myProject)) myProjectManager.closeProject(myProject);
        super.tearDown();
    }

    protected abstract String getProjectPath();

    @Override protected void setUpProject()
        throws Exception
    {
        myProjectManager = ProjectManagerEx.getInstanceEx();
        assertNotNull("Cannot instantiate ProjectManager component", myProjectManager);

        copyProjectToTestOutput(getProjectPath(), PROJECT_TEST_OUTPUT);

        myProject = myProjectManager.loadAndOpenProject(PROJECT_TEST_OUTPUT);
        if (myProject != null) myProject.getComponent(MMProjectComponent.class).buildModuleRepositories();
    }

    private void copyProjectToTestOutput(String projectPath, String outputPath)
        throws IOException
    {
        final File targetDir = new File(outputPath);
        if ((targetDir.exists() && targetDir.isDirectory()) || targetDir.mkdirs()) copyDirectory(new File(projectPath), targetDir);
    }

    //~ Static Fields ................................................................................................................................

    public static final String PROJECT_TEST_OUTPUT = "target/plugin/mm/test-output/mmProjectTest";
}
