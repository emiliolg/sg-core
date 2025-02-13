
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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.PsiEntity;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiType;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.mmcompiler.ast.MMToken;

import static com.intellij.psi.util.PsiUtilBase.getPsiFileInEditor;

import static tekgenesis.lang.mm.psi.PsiUtils.findParentModel;
import static tekgenesis.lang.mm.psi.PsiUtils.getEditor;

/**
 * Abstract action to generate code immediately after a metamodel.
 */
public abstract class GenerateForModelAction extends MMActionTree implements Runnable, UndoableAction {

    //~ Instance Fields ..............................................................................................................................

    int             offset;
    PsiMetaModel<?> psiModel   = null;
    String          text       = null;
    int             typeOffset;
    String          typeText   = null;

    //~ Constructors .................................................................................................................................

    protected GenerateForModelAction() {}

    protected GenerateForModelAction(Editor editor, Project project) {
        this.editor  = editor;
        this.project = project;
        setupFromEditor(project, editor);
    }

    protected GenerateForModelAction(PsiMetaModel<?> psiModel, Editor editor) {
        this.editor   = editor;
        this.psiModel = psiModel;
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent event) {
        editor  = getEditor(event.getDataContext());
        project = event.getProject();
        CommandProcessor.getInstance().executeCommand(event.getProject(), this, getActionName(), null);
    }

    /** For default this works for entities, but it could be overwritten to work on other models. */
    @Override public boolean hasValidContext() {
        return psiModel instanceof PsiEntity && !psiModel.isInner();
    }

    @Override public void redo()
        throws UnexpectedUndoException {}

    @Override public void run() {
        offset = psiModel.isInner() ? PsiUtils.findParentModel(psiModel).get().getTextRange().getEndOffset() : psiModel.getTextRange().getEndOffset();
        final Document document = editor.getDocument();

        text = getTextToWrite();

        ApplicationManager.getApplication().runWriteAction(() -> document.insertString(offset, text));

        // for types also tag the default generated widget in the type (Ej. type MyType widget MyTypeWidget {...})
        if (psiModel instanceof PsiType && psiModel.children(MMToken.WIDGET).isEmpty()) {
            typeOffset = psiModel.getTextOffset() + 5 + psiModel.getName().length();  // 5 for "type " and then the name

            typeText = getTextToWriteInModel();

            ApplicationManager.getApplication().runWriteAction(() -> document.insertString(typeOffset, typeText));
        }
    }

    @Override public void undo()
        throws UnexpectedUndoException
    {
        ApplicationManager.getApplication().runWriteAction(() -> editor.getDocument().deleteString(offset, offset + text.length()));
    }

    @Override public DocumentReference[] getAffectedDocuments() {
        return null;
    }

    @Override public boolean isGlobal() {
        return false;
    }

    @Override protected void setupFromEditor(Project p, Editor e) {
        file = getPsiFileInEditor(e, p);

        if (file != null) psiModel = getCaretPsiMetaModel(e, file);
    }

    @Override protected void setupFromTree(DataContext context) {
        final Navigatable data = PlatformDataKeys.NAVIGATABLE.getData(context);
        if (data instanceof PsiElement) {
            psiModel = getContainingPsiMetaModel((PsiElement) data).getOrNull();
            if (psiModel != null) file = psiModel.getContainingFile();
        }
    }

    abstract String getActionName();

    /** This gives you the text that will be written immediately after the metamodel. */
    abstract String getTextToWrite();

    /** This gives you the text that will be written immediately after the metamodel. */
    abstract String getTextToWriteInModel();

    @Nullable private PsiMetaModel<?> getCaretPsiMetaModel(@NotNull final Editor e, @NotNull final PsiFile psiFile) {
        offset = e.getCaretModel().getOffset();
        final PsiElement element = psiFile.findElementAt(offset);
        return element != null ? getContainingPsiMetaModel(element).getOrNull() : null;
    }

    @NotNull private Option<PsiMetaModel<?>> getContainingPsiMetaModel(@NotNull final PsiElement element) {
        return findParentModel(element);
    }
}  // end class GenerateForModelAction
