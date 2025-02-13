
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.awt.event.InputEvent;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.actions.SelectedMMGraphAction;
import tekgenesis.lang.mm.actions.UltimateMMGraphAction;
import tekgenesis.lang.mm.psi.MMFile;

/**
 * test Actions.
 */
public class TestMMGraph extends MMProjectTest {

    //~ Methods ......................................................................................................................................

    /** test UltimateGraph. */
    public void testUltimateGraphActionFile() {
        final Option<MMFile> mmFileInProject = ProjectUtils.findMmFileInProject(myProject, ESSOS, TARGARYEN);
        assertTrue(mmFileInProject.isPresent());
        final MMFile                mmFile                = mmFileInProject.get();
        final SelectedMMGraphAction ultimateMMGraphAction = new SelectedMMGraphAction();
        final DummyDataContext      dataContext           = new DummyDataContext(mmFile);
        ultimateMMGraphAction.setupFromTest(mmFile, mmFile.getEntity(DANERYS));
        final DummyActionEvent e = new DummyActionEvent(null,
                dataContext,
                MAIN_MENU,
                new Presentation(UltimateMMGraphAction.MMGRAPH_FOR_MODULE),
                ActionManager.getInstance(),
                0);
        ultimateMMGraphAction.actionPerformed(e);
        ultimateMMGraphAction.disposeContent();
        EditorFactory.getInstance().releaseEditor(dataContext.getEditor());
    }

    /** test UltimateGraph. */
    public void testUltimateGraphActionModule() {
        final Option<MMFile> mmFileInProject = ProjectUtils.findMmFileInProject(myProject, ESSOS, TARGARYEN);
        assertTrue(mmFileInProject.isPresent());
        final MMFile                mmFile                = mmFileInProject.get();
        final UltimateMMGraphAction ultimateMMGraphAction = new UltimateMMGraphAction();
        final DummyDataContext      dataContext           = new DummyDataContext(mmFile);
        final DummyActionEvent      e                     = new DummyActionEvent(null,
                dataContext,
                MAIN_MENU,
                new Presentation(UltimateMMGraphAction.MMGRAPH_FOR_MODULE),
                ActionManager.getInstance(),
                0);
        ultimateMMGraphAction.actionPerformed(e);
        ultimateMMGraphAction.disposeContent();
        EditorFactory.getInstance().releaseEditor(dataContext.getEditor());
    }

    @Override protected String getProjectPath() {
        return PLUGIN_MM_TEST_PROJECT;
    }

    //~ Static Fields ................................................................................................................................

    static final String PLUGIN_MM_TEST_PROJECT = "plugin/mm/src/test/data/testProject";

    public static final String DANERYS = "Danerys";

    private static final String MAIN_MENU = "MainMenu";
    public static final String  ESSOS     = "essos";
    public static final String  TARGARYEN = "Targaryen";

    //~ Inner Classes ................................................................................................................................

    private class DummyActionEvent extends AnActionEvent {
        public DummyActionEvent(@Nullable InputEvent inputEvent, @NotNull DataContext dataContext, @NonNls @NotNull String place,
                                @NotNull Presentation presentation, ActionManager actionManager, @JdkConstants.InputEventMask int modifiers) {
            super(inputEvent, dataContext, place, presentation, actionManager, modifiers);
        }
    }

    /**
     * Implements current DataContext logic.
     */
    private class DummyDataContext implements DataContext, DataProvider {
        private final Editor editor;

        private final PsiFile psiFile;

        public DummyDataContext(PsiFile mmFile) {
            psiFile = mmFile;
            final Document    document    = EditorFactory.getInstance().createDocument(psiFile.getText());
            final VirtualFile virtualFile = psiFile.getVirtualFile();
            assert (virtualFile != null);
            editor = EditorFactory.getInstance().createEditor(document, myProject, virtualFile, false);
        }

        @Nullable public Object getData(@NonNls String dataId) {
            if (LangDataKeys.LANGUAGE.getName().equals(dataId)) return psiFile.getLanguage();
            if (PlatformDataKeys.PROJECT.getName().equals(dataId)) return myProject;
            if (LangDataKeys.PSI_FILE.getName().equals(dataId)) return psiFile;
            if (PlatformDataKeys.EDITOR.getName().equals(dataId) || PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getName()
                                                                                                            .equals(dataId)) return editor;
            return null;
        }

        public Editor getEditor()
        {
            return editor;
        }
    }
}  // end class TestMMGraph
