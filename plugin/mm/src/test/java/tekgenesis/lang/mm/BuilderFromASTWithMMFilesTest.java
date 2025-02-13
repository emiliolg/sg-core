
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

import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.PsiTestCase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.mmcompiler.builder.BuilderFromAST;
import tekgenesis.repository.ModelRepository;
import tekgenesis.repository.MultiModuleRepository;

/**
 * Test {@link BuilderFromAST} buildFromAST() with MMFiles (PsiTree built with parser adapter).
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class BuilderFromASTWithMMFilesTest extends PsiTestCase {

    //~ Methods ......................................................................................................................................

    /** Test Entities and Forms. */
    public void testEntitiesAndForms()
        throws IOException
    {
        buildRepositoriesAndCompare(new File(ENTITIES_AND_FORMS), new ModelRepository(), new ModelRepository());
    }

    /** Test Entities and Views. */
    public void testEntitiesAndViews()
        throws IOException
    {
        buildRepositoriesAndCompare(new File(ENTITIES_AND_VIEWS), new ModelRepository(), new ModelRepository());
    }

    /** Test Remote Views. */
    public void testRemoteViews()
        throws IOException
    {
        final ModelRepository libraries = new ModelRepository();
        createRepositoryFromFile(new File(ENTITIES_AND_VIEWS), libraries, new BuilderErrorListener.Default());

        final MultiModuleRepository seedForFile = new MultiModuleRepository();
        seedForFile.dependsOn(libraries);
        final MultiModuleRepository seedForMMFile = new MultiModuleRepository();
        seedForMMFile.dependsOn(libraries);
        buildRepositoriesAndCompare(new File(REMOTE_VIEWS), seedForFile, seedForMMFile);
    }

    @Override protected void setUp()
        throws Exception
    {
        System.getProperties().remove(TestAction.IDEA_PLATFORM_PREFIX);
        System.getProperties().put("idea.home.path", "/tmp");
        super.setUp();
    }

    private ModelRepository buildRepositoriesAndCompare(File file, ModelRepository seedForFile, ModelRepository seedForMMFile) {
        final BuilderErrorListener.Default listener = new BuilderErrorListener.Default();

        final ModelRepository mr1 = createRepositoryFromFile(file, seedForFile, listener);
        if (listener.hasErrors()) failWithErrors(file);

        final ModelRepository mr2 = createRepositoryFromMMFile(file, seedForMMFile, listener);
        if (listener.hasErrors()) failWithErrors(file);

        assertEquals(mr1.getDomains(), mr2.getDomains());
        assertEquals(mr1.getModels().size(), mr2.getModels().size());

        return seedForFile;
    }

    /** Create MMFile for given file. */
    private MMFile createMetaModelFile(@NotNull File file) {
        final String text = TestUtil.readFile(file);
        return (MMFile) PsiFileFactory.getInstance(getProject()).createFileFromText(file.getName(), MMFileType.INSTANCE, text);
    }

    private ModelRepository createRepositoryFromFile(File file, ModelRepository repository, BuilderErrorListener.Default listener) {
        return new ModelRepositoryLoader(file, repository).withErrorListener(listener).build();
    }

    private ModelRepository createRepositoryFromMMFile(File file, ModelRepository repository, BuilderErrorListener.Default listener) {
        final MMFile mm = createMetaModelFile(file);
        return new BuilderFromAST(repository, listener).build(mm.getPath(), mm.getFirstRoot());
    }

    private void failWithErrors(File file) {
        fail(String.format("File %s contains errors!", file.getAbsolutePath()));
    }

    //~ Static Fields ................................................................................................................................

    private static final String ENTITIES_AND_FORMS = "plugin/mm/src/test/resources/idea/test/EntitiesAndForms.mm";
    private static final String ENTITIES_AND_VIEWS = "plugin/mm/src/test/resources/idea/test/EntitiesAndViews.mm";
    private static final String REMOTE_VIEWS       = "plugin/mm/src/test/resources/idea/test/RemoteViews.mm";
}  // end class BuilderFromASTWithMMFilesTest
