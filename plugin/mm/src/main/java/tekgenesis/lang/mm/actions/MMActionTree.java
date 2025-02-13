
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;

import static tekgenesis.lang.mm.psi.PsiUtils.getEditor;

/**
 * MMaction that may be called from a the file tree or from editor.
 */
public abstract class MMActionTree extends MMAction {

    //~ Instance Fields ..............................................................................................................................

    Editor  editor = null;
    PsiFile file   = null;

    Project project = null;

    //~ Methods ......................................................................................................................................

    @Override public void update(@NotNull AnActionEvent event) {
        final Presentation presentation = event.getPresentation();
        final DataContext  dataContext  = event.getDataContext();
        project = PlatformDataKeys.PROJECT.getData(dataContext);
        editor  = getEditor(dataContext);
        if (project == null) {
            presentation.setEnabled(false);
            return;
        }
        if (editor == null) setupFromTree(dataContext);
        else {
            file = PsiUtilBase.getPsiFileInEditor(editor, project);
            setupFromEditor(project, editor);
        }
        presentation.setEnabled(hasValidContext() && file instanceof MMFile);
    }
    protected abstract boolean hasValidContext();
    protected abstract void setupFromEditor(Project project, Editor editor);
    protected abstract void setupFromTree(DataContext context);
}
