
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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;

import tekgenesis.common.util.Files;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MMFixtureTestCase.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "rawtypes" })
public abstract class MMFixtureTestCase extends CodeInsightFixtureTestCase {

    //~ Instance Fields ..............................................................................................................................

    protected Module      mainModule = null;
    protected VirtualFile srcJava    = null;
    protected VirtualFile srcMM      = null;

    //~ Methods ......................................................................................................................................

    @Override public void tearDown()
        throws Exception
    {
        final Project project = getProject();
        super.tearDown();
        Disposer.dispose(getTestRootDisposable());
        if (project != null && !project.isDisposed()) Disposer.dispose(project);
        ClassLoader.getSystemClassLoader().setPackageAssertionStatus("com.intellij.codeInsight.daemon.impl", true);
    }

    @Override public void setUp()
        throws Exception
    {
        System.getProperties().remove("idea.platform.prefix");
        System.getProperties().put("idea.home.path", "/tmp");
        ClassLoader.getSystemClassLoader().setPackageAssertionStatus("com.intellij.codeInsight.daemon.impl", false);
        super.setUp();
    }

    void initProject()
        throws IOException
    {
        mainModule = myFixture.getModule();

        String         dir   = "";
        final String[] split = mainModule.getModuleFilePath().split("/");
        for (final String s : split)
            dir += s + "/";

        final File mmSrcFile        = new File(dir, "src/main/mm");
        final File javaSrcFile      = new File(dir, "src/main/java");
        final File generatedSources = new File(dir, "target/generated-sources/mm");

        Files.remove(javaSrcFile);
        javaSrcFile.mkdirs();
        Files.remove(mmSrcFile);
        mmSrcFile.mkdirs();
        Files.remove(generatedSources);
        generatedSources.mkdirs();

        srcMM = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(mmSrcFile);
        assert srcMM != null;
        assertThat(srcMM).isNotNull();
        LocalFileSystem.getInstance().createChildDirectory(this, srcMM, "test");
        LocalFileSystem.getInstance().createChildDirectory(this, srcMM, "catalog");
        LocalFileSystem.getInstance().createChildDirectory(this, srcMM, "sales");

        srcJava = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(javaSrcFile);
        final VirtualFile generatedSrcJava = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(generatedSources);

        myFixture.setTestDataPath("plugin/mm/src/test");

        PsiTestUtil.addSourceRoot(mainModule, srcMM);
        PsiTestUtil.addSourceRoot(mainModule, srcJava);
        PsiTestUtil.addSourceRoot(mainModule, generatedSrcJava);
        mainModule.getComponent(MMModuleComponent.class).initializeModelRepository();
    }
}  // end class MMFixtureTestCase
