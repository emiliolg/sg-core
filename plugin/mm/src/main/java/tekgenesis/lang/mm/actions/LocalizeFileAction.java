
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
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;

/**
 * Internationalization Action: all MetaModels in file.
 */

@SuppressWarnings("WeakerAccess")
public class LocalizeFileAction extends AbstractLocalizeAction {

    //~ Methods ......................................................................................................................................

    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor  data     = PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(e.getDataContext());
        final Project eProject = e.getProject();

        if (data != null && eProject != null) {
            final MMFile mmFile = (MMFile) PsiUtilBase.getPsiFileInEditor(data, eProject);

            if (mmFile != null) internationalize(mmFile);
        }
    }
}
