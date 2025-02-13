
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
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.content.Content;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;

import static tekgenesis.lang.mm.graph.GraphUtils.isUltimate;
import static tekgenesis.lang.mm.graph.graphUltimate.utils.UltimateGraphUtils.createModuleGraphAndShow;

/**
 * Show EntityRelation Graph for Module.
 */
public class UltimateMMGraphAction extends MMAction {

    //~ Instance Fields ..............................................................................................................................

    private Content content = null;

    //~ Methods ......................................................................................................................................

    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor  editor  = PlatformDataKeys.EDITOR.getData(e.getDataContext());
        final Project project = e.getProject();

        if (project != null && editor != null) {
            MMFile file = (MMFile) PsiUtilBase.getPsiFileInEditor(editor, project);
            if (file == null) file = (MMFile) LangDataKeys.PSI_FILE.getData(e.getDataContext());
            if (file != null) content = createModuleGraphAndShow(file, e);
        }
    }

    /**
     * Dispose graph content. Used for testing, where you can't control when to hide and dispose
     * window
     */
    public void disposeContent() {
        if (content != null) Disposer.dispose(content);
    }

    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        final String title = isUltimate() ? MMGRAPH_FOR_MODULE : MMGRAPH_FOR_MODULE + "(Only In Ultimate)";
        e.getPresentation().setText(title);
        e.getPresentation().setEnabled(isUltimate());
    }

    //~ Static Fields ................................................................................................................................

    public static final String MMGRAPH_FOR_MODULE = "MMGraph For Module";
}  // end class UltimateMMGraphAction
