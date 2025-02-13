
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
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.lang.mm.psi.PsiUtils.getEditor;

/**
 * Localize all files in Module.
 */

@SuppressWarnings("WeakerAccess")
public class LocalizePackageAction extends AbstractLocalizeAction {

    //~ Methods ......................................................................................................................................

    public void actionPerformed(@NotNull AnActionEvent event) {
        final DataContext context = event.getDataContext();
        final Editor      e       = getEditor(context);
        final Project     p       = event.getProject();

        if (e == null || p == null) return;

        final MMFile mmFile = (MMFile) PsiUtilBase.getPsiFileInEditor(e, p);
        if (mmFile == null) return;

        final PsiDomain domain = mmFile.getDomain();
        if (domain != null && isNotEmpty(domain.getDomain())) internationalize(mmFile, mmFile.getModelRepository().getModels(domain.getDomain()));
    }
}
