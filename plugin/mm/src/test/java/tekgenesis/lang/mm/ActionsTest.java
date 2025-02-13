
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
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import com.intellij.psi.PsiFile;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.common.util.Diff;
import tekgenesis.lang.mm.actions.DefaultUiModelExpansionAction;
import tekgenesis.lang.mm.actions.TidyFileAction;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.WidgetDef;

import static tekgenesis.common.tools.test.Tests.checkDiff;

/**
 * ActionsTest.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "JUnit4AnnotatedMethodInJUnit3TestCase" })
public class ActionsTest extends MMFixtureTestCase implements GoldenFiles {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public File file = null;

    //~ Methods ......................................................................................................................................

    @Test public void expandFormTest()
        throws FileNotFoundException
    {
        final String fileString = TestUtil.readFile(file.getPath());

        final String[]                            split   = file.getName().split("/");
        final String                              last    = split[split.length - 1];
        final PsiFile                             psiFile = TestUtil.createFile(mainModule, TestUtil.findChild(srcMM, "test"), last, fileString);
        final DefaultUiModelExpansionAction<Form> action  = new DefaultUiModelExpansionAction<Form>();

        mainModule.getComponent(MMModuleComponent.class).initializeModelRepository();

        final String outName = last + ".gf";
        final File   outFile = new File(errorsOutputDir, outName);
        action.expandAllInFile(psiFile, new PrintWriter(outFile));

        final File goldenFile = new File(goldenDir, outName);
        checkDiff(outFile, goldenFile);
    }

    @Test public void expandWidgetDefTest()
        throws FileNotFoundException
    {
        final String fileString = TestUtil.readFile(file.getPath());

        final String[]                                 split   = file.getName().split("/");
        final String                                   last    = split[split.length - 1];
        final PsiFile                                  psiFile = TestUtil.createFile(mainModule, TestUtil.findChild(srcMM, "test"), last, fileString);
        final DefaultUiModelExpansionAction<WidgetDef> action  = new DefaultUiModelExpansionAction<WidgetDef>();

        mainModule.getComponent(MMModuleComponent.class).initializeModelRepository();

        final String outName = last + ".gw";
        final File   outFile = new File(errorsOutputDir, outName);
        action.expandAllInFile(psiFile, new PrintWriter(outFile));

        final File goldenFile = new File(goldenDir, outName);
        checkDiff(outFile, goldenFile);
    }

    @Test public void tidyTest()
        throws FileNotFoundException
    {
        final String fileString = TestUtil.readFile(file.getPath());

        final String[] split   = file.getName().split("/");
        final String   last    = split[split.length - 1];
        final String   outName = last + ".ty";
        final PsiFile  psiFile = TestUtil.createFile(mainModule, TestUtil.findChild(srcMM, "test"), last, fileString);

        final File outFile = new File(errorsOutputDir, outName);

        final TidyFileAction tidyAction = new TidyFileAction();
        tidyAction.writeTidy(psiFile, new PrintWriter(outFile));

        final File goldenFile = new File(goldenDir, outName);
        checkDiff(outFile, goldenFile, new Diff.Equals<String>() {
                @Override public boolean doEqualComparison(String a, String b) {
                    return (a.startsWith(Constants.GENERATED) && b.startsWith(Constants.GENERATED)) || a.trim().equals(b.trim());
                }
            });
    }

    @After protected void afterEach()
        throws InvocationTargetException, InterruptedException
    {
        SwingUtilities.invokeAndWait(() -> {
            try {
                tearDown();
            }
            catch (final Exception e) {
                // ignore
            }
        });
    }

    @Before protected void beforeEach()
        throws InvocationTargetException, InterruptedException
    {
        SwingUtilities.invokeAndWait(() -> {
            try {
                setUp();
                initProject();
            }
            catch (final Exception e) {
                // ignore
            }
        });
    }

    //~ Methods ......................................................................................................................................

    @BeforeClass public static void init() {
        errorsOutputDir.mkdirs();
    }

    @Parameterized.Parameters(name = "{0}")
    public static Seq<Object[]> listFiles() {
        return Tests.listFiles(dataDir, ".mm");
    }

    //~ Static Fields ................................................................................................................................

    private static final File errorsOutputDir = new File(TestUtil.TARGET_PLUGIN_MM_TEST_OUTPUT_ERRORS);
}  // end class ActionsTest
