
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.io.PrintWriter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.mmcompiler.builder.BuilderFromAST;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.util.MMDumper.createDumper;

/**
 * Action to generate a Form from an Entity.
 */
public class TidyFileAction extends AnAction {

    //~ Instance Fields ..............................................................................................................................

    private PsiFile psiFileInEditor = null;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public TidyFileAction() {
        final Presentation presentation = getTemplatePresentation();
        presentation.setDescription(TIDY_FILE);
        presentation.setText(TIDY_FILE);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull final AnActionEvent event) {
        final UndoableTidyAction tidyAction = new UndoableTidyAction(event, psiFileInEditor);
        CommandProcessor.getInstance().executeCommand(event.getProject(), tidyAction, "TidyFile", null);
    }

    @Override public void update(@NotNull AnActionEvent event) {
        final Presentation presentation = event.getPresentation();
        final DataContext  dataContext  = event.getDataContext();

        final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            presentation.setEnabled(false);
            return;
        }

        final Editor editor = getEditor(dataContext);
        if (editor != null) psiFileInEditor = PsiUtilBase.getPsiFileInEditor(editor, project);

        presentation.setEnabled(psiFileInEditor != null);
    }

    /** Writes new tidy file for a given PsiFile. */
    public void writeTidy(PsiFile psi, PrintWriter writer) {
        final Module module = ModuleUtil.findModuleForPsiElement(psi);
        if (module == null) return;

        final ModelRepository      modelRepository = module.getComponent(MMModuleComponent.class).getRepository();
        final BuilderErrorListener listener        = new BuilderErrorListener.Silent();
        final BuilderFromAST       builder         = new BuilderFromAST(modelRepository, listener);
        final MetaModelAST         ast             = ((MMFile) psi).getFirstRoot();
        final VirtualFile          virtualFile     = psi.getVirtualFile();

        if (virtualFile == null) return;

        final String filePath = virtualFile.getPath();
        builder.build(filePath, ast);

        createDumper(modelRepository).file(filePath).withPackage().toWriter(writer);
    }

    protected boolean isValidForFile(PsiFile file) {
        return file.getFileType().equals(MMFileType.INSTANCE);
    }

    //~ Methods ......................................................................................................................................

    @Nullable private static Editor getEditor(@NotNull DataContext dataContext) {
        return PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
    }

    //~ Static Fields ................................................................................................................................

    private static final String TIDY_FILE = "CLEAN ME UP!";

    //~ Inner Classes ................................................................................................................................

    private static class UndoableTidyAction implements Runnable, UndoableAction {
        private Document document;

        private final AnActionEvent event;
        private String              newText;
        private String              oldText;
        private final PsiFile       psiFileInEditor;

        private UndoableTidyAction(final AnActionEvent event, final PsiFile psiFileInEditor) {
            this.event           = event;
            this.psiFileInEditor = psiFileInEditor;
            newText              = null;
            oldText              = null;
            document             = null;
        }

        @Override public void redo()
            throws UnexpectedUndoException {}

        @Override public void run() {
            final Editor editor = getEditor(event.getDataContext());
            if (editor == null) return;

            final Module module = ModuleUtil.findModuleForPsiElement(psiFileInEditor);
            if (module == null) return;
            final ModelRepository modelRepository = module.getComponent(MMModuleComponent.class).getRepository();

            final VirtualFile virtualFile = psiFileInEditor.getVirtualFile();
            if (virtualFile == null) return;
            final String filePath = virtualFile.getPath();

            final BuilderErrorListener listener = new BuilderErrorListener.Silent();
            final BuilderFromAST       builder  = new BuilderFromAST(modelRepository, listener);
            final MetaModelAST         ast      = ((MMFile) psiFileInEditor).getFirstRoot();
            builder.build(filePath, ast);

            if (listener.hasErrors()) return;

            newText  = createDumper(modelRepository).file(filePath).withPackage().toString();
            document = editor.getDocument();
            oldText  = document.getText();
            ApplicationManager.getApplication().runWriteAction(() -> {
                document.deleteString(0, document.getTextLength());
                document.insertString(0, newText);
            });
        }

        @Override public void undo()
            throws UnexpectedUndoException
        {
            ApplicationManager.getApplication().runWriteAction(() -> {
                document.deleteString(0, document.getTextLength());
                document.insertString(0, oldText);
            });
        }

        @Override public DocumentReference[] getAffectedDocuments() {
            return null;
        }

        @Override public boolean isGlobal() {
            return false;
        }
    }  // end class UndoableTidyAction
}  // end class TidyFileAction
