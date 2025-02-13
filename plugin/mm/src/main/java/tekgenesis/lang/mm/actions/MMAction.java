
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;

abstract class MMAction extends AnAction {

    //~ Methods ......................................................................................................................................

    @Override public void update(@NotNull AnActionEvent event) {
        final Presentation presentation = event.getPresentation();
        final DataContext  dataContext  = event.getDataContext();
        final Project      project      = PlatformDataKeys.PROJECT.getData(dataContext);

        final Editor editor = PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        if (editor == null) {
            final PsiFile file = event.getData(CommonDataKeys.PSI_FILE);
            if (file != null) presentation.setEnabled((file instanceof MMFile));
            else presentation.setEnabled(false);
        }
        else {
            if (project != null) {
                final PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
                presentation.setEnabled((file instanceof MMFile));
            }
        }
    }
}
