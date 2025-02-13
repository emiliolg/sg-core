
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationSession;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.PsiTestUtil;

import org.junit.Test;

import tekgenesis.common.core.Option;
import tekgenesis.common.util.Files;
import tekgenesis.lang.mm.errors.MMBuilderErrorListener;
import tekgenesis.lang.mm.errors.MMErrorFixer;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.mmcompiler.builder.BuilderFromAST;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Fixer Test.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JUnit4AnnotatedMethodInJUnit3TestCase", "JavaDoc" })
public class FixerTest extends MMPsiTestCase implements GoldenFiles {

    //~ Instance Fields ..............................................................................................................................

    private final File errorsOutputDir = new File(TestUtil.TARGET_PLUGIN_MM_TEST_OUTPUT_ERRORS);

    private Module      mainModule = null;
    private VirtualFile srcMM      = null;

    //~ Methods ......................................................................................................................................

    @Test public void testBuilderErrorListener()
        throws IOException
    {
        final String bindingNotResolved    = TestUtil.readFile("plugin/mm/src/test/resources/builderErrors/BindingNotResolvedError.mm");
        final MMFile bindingNotResolvedPsi = (MMFile) createFile(mainModule,
                TestUtil.findChild(srcMM, "catalog"),
                "BindingNotResolvedError.mm",
                bindingNotResolved);
        final String primaryOptional       = TestUtil.readFile("plugin/mm/src/test/resources/builderErrors/primaryKeyOptionalError.mm");
        final MMFile primaryOptionalPsi    = (MMFile) createFile(mainModule,
                TestUtil.findChild(srcMM, "catalog"),
                "primaryKeyOptionalError.mm",
                primaryOptional);

        final AnnotationHolderImpl holder  = new AnnotationHolderImpl(new AnnotationSession(bindingNotResolvedPsi));
        final AnnotationHolderImpl holder2 = new AnnotationHolderImpl(new AnnotationSession(primaryOptionalPsi));

        final MMBuilderErrorListener listener  = new MMBuilderErrorListener(holder);
        final MMBuilderErrorListener listener2 = new MMBuilderErrorListener(holder2);

        final BuilderFromAST b = new BuilderFromAST(mainModule.getComponent(MMModuleComponent.class).getRepository(), listener);
        b.build(bindingNotResolvedPsi.getPath(), bindingNotResolvedPsi.getFirstRoot());
        final BuilderFromAST b2 = new BuilderFromAST(mainModule.getComponent(MMModuleComponent.class).getRepository(), listener2);
        b2.build(primaryOptionalPsi.getPath(), primaryOptionalPsi.getFirstRoot());

        assertThat(holder.size()).isEqualTo(2);
        assertThat(holder2.size()).isEqualTo(1);
    }

    @Test public void testLowerCaseModel()
        throws IOException
    {
        final String lowerCase    = TestUtil.readFile("plugin/mm/src/test/resources/builderErrors/lowercaseModel.mm");
        final MMFile lowercasePsi = (MMFile) createFile(mainModule, TestUtil.findChild(srcMM, "catalog"), "lowercaseModel.mm", lowerCase);

        final String outName = lowercasePsi.getName() + ".fixed";

        final MMErrorFixer.FixIntention<BuilderError> errorFix = getFixOrFail(lowercasePsi);

        errorFix.withNode((MetaModelAST) ensureNotNull(lowercasePsi.getEntity(errorFix.getError().getModelName())).getIdentifier());

        final File outputFile = new File(errorsOutputDir, outName);
        invokeFix(errorFix, lowercasePsi, outputFile);

        final File goldenFile = new File(goldenDir, outName);
        TestUtil.checkDiff(outputFile, goldenFile);
    }

    @Test public void testNoPackageFix()
        throws IOException
    {
        final String noPackage    = TestUtil.readFile("plugin/mm/src/test/resources/builderErrors/noPackageError.mm");
        final MMFile noPackagePsi = (MMFile) createFile(mainModule, TestUtil.findChild(srcMM, "catalog"), "noPackagePsi.mm", noPackage);

        final AnnotationHolderImpl holder = new AnnotationHolderImpl(new AnnotationSession(noPackagePsi));

        final String outName = noPackagePsi.getName() + ".fixed";

        ((PsiDomain) noPackagePsi.getFirstRoot().getChild(0)).annotate(holder);
        final List<Annotation.QuickFixInfo> quickFixes = holder.get(0).getQuickFixes();

        assert quickFixes != null;

        final File outputFile = new File(errorsOutputDir, outName);
        invokeFix(quickFixes.get(0).quickFix, noPackagePsi, outputFile);

        final File goldenFile = new File(goldenDir, outName);
        TestUtil.checkDiff(outputFile, goldenFile);
    }

    @Test public void testOptionalPrimaryKey()
        throws IOException
    {
        final String primaryOptional    = TestUtil.readFile("plugin/mm/src/test/resources/builderErrors/primaryKeyOptionalError.mm");
        final MMFile primaryOptionalPsi = (MMFile) createFile(mainModule,
                TestUtil.findChild(srcMM, "catalog"),
                "primaryKeyOptionalError.mm",
                primaryOptional);

        final String outName = primaryOptionalPsi.getName() + ".fixed";

        final MMErrorFixer.FixIntention<BuilderError> errorFix = getFixOrFail(primaryOptionalPsi);

        errorFix.withNode(ensureNotNull(primaryOptionalPsi.getAnyNode(errorFix.getError().getModelName())));

        final File outputFile = new File(errorsOutputDir, outName);
        invokeFix(errorFix, primaryOptionalPsi, outputFile);

        final File goldenFile = new File(goldenDir, outName);
        TestUtil.checkDiff(outputFile, goldenFile);
    }

    @Override public void setUp()
        throws Exception
    {
        super.setUp();
        createPsiFiles();
    }

    private void createPsiFiles()
        throws IOException
    {
        mainModule = createMainModule();
        final VirtualFile moduleVFile = mainModule.getModuleFile();
        assert moduleVFile != null;
        final VirtualFile parent = moduleVFile.getParent();

        assertThat(parent).isNotNull();
        final File moduleDir = new File(parent.getPath());
        // if (moduleVFile == null) fail();

        final File srcFile = new File(moduleDir, "../src/main/mm");
        Files.remove(srcFile);
        srcFile.mkdirs();
        final File salesDir = new File(srcFile, "sales");
        salesDir.mkdir();
        final File catalogDir = new File(srcFile, "catalog");
        catalogDir.mkdir();
        final File monuDir = new File(srcFile, "monumental");
        monuDir.mkdir();
        final File generatedSources = new File(moduleDir, "target/src_managed/main/mm");
        Files.remove(generatedSources);

        final File javaSources = new File(moduleDir, "../src/main/java");
        Files.remove(javaSources);
        generatedSources.mkdirs();
        javaSources.mkdirs();

        final VirtualFile srcJava          = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(javaSources);
        final VirtualFile generatedSrcJava = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(generatedSources);
        srcMM = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(srcFile);

        PsiTestUtil.addSourceRoot(mainModule, srcMM);
        PsiTestUtil.addSourceRoot(mainModule, srcJava);
        PsiTestUtil.addSourceRoot(mainModule, generatedSrcJava);

        mainModule.getComponent(MMModuleComponent.class).initializeModelRepository();

        // if (moduleVFile == null) fail();
        VirtualFileManager.getInstance().syncRefresh();
    }  // end method createPsiFiles

    private void invokeFix(IntentionAction errorFix, PsiFile psiFile, File outputFile)
        throws FileNotFoundException
    {
        final Document doc    = EditorFactory.getInstance().createDocument(psiFile.getText());
        final Editor   editor = EditorFactory.getInstance().createEditor(doc);

        errorFix.invoke(getProject(), editor, psiFile);
        writeOutput(outputFile, editor);
    }

    private void writeOutput(File outputFile, Editor editor)
        throws FileNotFoundException
    {
        outputFile.getParentFile().mkdirs();
        final PrintWriter writer = new PrintWriter(outputFile);
        writer.write(editor.getDocument().getCharsSequence().toString());
        writer.flush();

        EditorFactory.getInstance().releaseEditor(editor);
    }

    private MMErrorFixer.FixIntention<BuilderError> getFixOrFail(MMFile mmFile) {
        final FirstError     listener = new FirstError();
        final BuilderFromAST b        = new BuilderFromAST(mainModule.getComponent(MMModuleComponent.class).getRepository(), listener);
        b.build(mmFile.getPath(), mmFile.getFirstRoot());

        final Option<MMErrorFixer.FixIntention<BuilderError>> fixOp = MMErrorFixer.provideFix(listener.first.get());
        return fixOp.getOrFail("no Fix Found");
    }

    //~ Inner Classes ................................................................................................................................

    private class FirstError extends BuilderErrorListener.Silent {
        Option<BuilderError> first = Option.empty();

        public void error(BuilderError e) {
            if (first.isEmpty()) first = Option.some(e);
        }

        public void error(MetaModelAST node, BuilderError e) {
            error(e);
        }
    }
}  // end class FixerTest
