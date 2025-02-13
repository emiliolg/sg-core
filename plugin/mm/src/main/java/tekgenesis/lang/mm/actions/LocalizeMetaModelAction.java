
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
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.type.MetaModel;

import static tekgenesis.lang.mm.intention.LocalizeMetaModelIntention.LOCALIZE_PREDICATE;
import static tekgenesis.lang.mm.psi.PsiUtils.findParentModel;
import static tekgenesis.lang.mm.psi.PsiUtils.getEditor;

/**
 * Action to localize an specific Meta Model.
 */

public class LocalizeMetaModelAction extends AbstractLocalizeAction implements Runnable, UndoableAction {

    //~ Instance Fields ..............................................................................................................................

    private PsiElement element = null;
    private MMFile     mmFile  = null;

    //~ Constructors .................................................................................................................................

    /** Action to localize a form, menu or enum. */
    public LocalizeMetaModelAction() {}

    /** Action to localize a form, menu or enum. */
    public LocalizeMetaModelAction(Project project, Editor editor) {
        this.project = project;
        this.editor  = editor;

        setupFromEditor(project, editor);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        editor  = getEditor(e.getDataContext());
        project = e.getProject();
        if (editor == null || project == null) {
            mmFile = getFile(e);
            if (mmFile != null) internationalize(mmFile);
        }

        CommandProcessor.getInstance().executeCommand(project, this, LOCALIZE_META_MODEL, null);
    }

    @Override public boolean hasValidContext() {
        return element != null && LOCALIZE_PREDICATE.satisfiedBy(element);
    }

    @Override public void redo()
        throws UnexpectedUndoException {}

    @Override public void run() {
        for (final PsiMetaModel<?> model : findParentModel(element)) {
            for (final MetaModel meta : model.getModel())
                internationalize(mmFile, Colls.listOf(meta));
        }
    }

    @Override public void undo()
        throws UnexpectedUndoException {}

    @Nullable @Override public DocumentReference[] getAffectedDocuments() {
        return null;
    }

    @Override public boolean isGlobal() {
        return false;
    }

    @Override protected void setupFromEditor(Project p, Editor e) {
        final PsiFile psiFileInEditor = PsiUtilBase.getPsiFileInEditor(e, p);
        if (psiFileInEditor instanceof MMFile) {
            mmFile  = (MMFile) psiFileInEditor;
            element = elementAtCaret(e, mmFile);
        }
    }

    @Nullable protected MMFile getFile(AnActionEvent e) {
        return (MMFile) e.getData(CommonDataKeys.PSI_FILE);
    }

    @Nullable private PsiElement elementAtCaret(@NotNull final Editor e, @NotNull final PsiFile psiFile) {
        return psiFile.findElementAt(e.getCaretModel().getOffset());
    }

    //~ Static Fields ................................................................................................................................

    public static final String LOCALIZE_META_MODEL = "LocalizeMetaModel";
}  // end class LocalizeMetaModelAction
