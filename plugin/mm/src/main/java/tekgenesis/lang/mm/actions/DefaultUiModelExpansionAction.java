
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

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiUiModel;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.WidgetDefBuilder;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.CompositeType;
import tekgenesis.type.Type;

import static com.intellij.psi.util.PsiUtilBase.getPsiFileInEditor;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.lang.mm.psi.PsiUtils.*;
import static tekgenesis.metadata.form.DefaultUiModelGenerator.defaultUiModel;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.form;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.widget;
import static tekgenesis.util.MMDumper.createDumper;

/**
 * Action to expand a default Form.
 */
public class DefaultUiModelExpansionAction<T extends UiModel> extends MMActionTree implements Runnable, UndoableAction {

    //~ Instance Fields ..............................................................................................................................

    private Document document = null;

    private Option<T> uiModel = null;
    private String          newText    = "";
    private int             offset;
    private String          oldText    = "";
    private ModelRepository repository = null;

    //~ Constructors .................................................................................................................................

    /** Action to expand a default UiModel. */
    public DefaultUiModelExpansionAction() {
        final Presentation presentation = getTemplatePresentation();
        presentation.setDescription(DEFAULT_UI_MODEL_EXPANSION);
        presentation.setText(DEFAULT_UI_MODEL_EXPANSION);
        uiModel = Option.empty();
    }
    /** Action to expand a default UiModel. */
    public DefaultUiModelExpansionAction(Editor editor, Project project, ModelRepository repository) {
        this.repository = repository;
        this.editor     = editor;
        this.project    = project;
        setupFromEditor(project, editor);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent event) {
        editor  = getEditor(event.getDataContext());
        project = event.getProject();
        CommandProcessor.getInstance().executeCommand(event.getProject(), this, UI_MODEL_EXPANSION, null);
    }

    /** Writes form expansion for all UiModels in a given PsiFile (used in test). */
    public void expandAllInFile(PsiFile psi, PrintWriter writer) {
        final MMFile mmFile = (MMFile) psi;

        repository = getModelRepository(ModuleUtil.findModuleForPsiElement(psi));

        for (final PsiUiModel<? extends UiModel> element : mmFile.getUiModels()) {
            uiModel = cast(element.getModel());
            final UiModel model = generateUiModel("");
            if (model != null) {
                final String generatedUiModel = createDumper(repository).model(model).toString();
                writer.write(generatedUiModel);
                writer.flush();
            }
        }
    }

    @Override public void redo()
        throws UnexpectedUndoException {}

    @Override public void run() {
        if (editor != null) {
            final PsiFile psiFileInEditor = getPsiFileInEditor(editor, project);

            if (psiFileInEditor != null) {
                final UiModel generated = generateUiModel(psiFileInEditor.getName());
                if (generated == null) return;
                newText  = createDumper(repository).model(generated).toString();
                document = editor.getDocument();
                final TreeElement ast = getCaretPsiUiModel(editor, psiFileInEditor);
                assert ast != null : "Null element at caret position on default form expansion!";
                offset  = ast.getStartOffsetInParent();
                oldText = document.getText(new TextRange(offset, offset + ast.getTextLength()));
                ApplicationManager.getApplication().runWriteAction(() ->
                        editor.getDocument()
                              .replaceString(offset, offset + ast.getTextLength(), newText));
            }
        }
    }

    @Override public void undo()
        throws UnexpectedUndoException
    {
        ApplicationManager.getApplication().runWriteAction(() -> document.replaceString(offset, offset + newText.length(), oldText));
    }

    @Override public DocumentReference[] getAffectedDocuments() {
        return null;
    }

    @Override public boolean isGlobal() {
        return false;
    }

    protected boolean hasValidContext() {
        return uiModel.filter(UiModel::isGenerated).isPresent() && repository != null;
    }

    /** Set up context for action. */
    protected void setupFromEditor(Project p, Editor e) {
        file = getPsiFileInEditor(e, p);

        if (file != null) {
            final PsiUiModel<T> element = getCaretPsiUiModel(e, file);

            if (element != null) {
                uiModel = element.getModel();

                final VirtualFile virtualFile = file.getVirtualFile();
                if (virtualFile != null) repository = getModelRepository(ModuleUtil.findModuleForFile(virtualFile, p));
            }
            else uiModel = Option.empty();
        }
    }

    protected void setupFromTree(DataContext dataContext) {
        final Navigatable data = PlatformDataKeys.NAVIGATABLE.getData(dataContext);

        if (data instanceof StructureViewTreeElement) {
            final Object value = ((StructureViewTreeElement) data).getValue();

            if (value instanceof PsiElement) {
                final PsiElement psiElement = (PsiElement) value;
                uiModel = getContainingPsiUiModel(psiElement).get().getModel();
                repository = getModelRepository(ModuleUtil.findModuleForPsiElement(psiElement));
            }
        }
    }

    @Nullable private UiModel generateUiModel(String sourceName) {
        if (hasValidContext()) {
            final UiModel original = uiModel.get();

            try {
                if (original instanceof Form) {
                    final FormBuilder      builder = form(sourceName, original.getDomain(), original.getName()).withRepository(repository);
                    final Option<DbObject> binding = repository.getModel(original.getBinding(), DbObject.class);

                    if (binding.isPresent()) {
                        if (((Form) original).isListing()) builder.asListing();
                        builder.withBinding(binding.get());
                        return defaultUiModel(repository, builder).setGenerated(false).build();
                    }
                } else {
                    final WidgetDefBuilder builder = widget(sourceName, original.getDomain(), original.getName()).withRepository(repository);
                    final Option<CompositeType> binding = repository.getModel(original.getBinding(), CompositeType.class);

                    if (binding.isPresent()) {
                        builder.withBinding(binding.castTo(Type.class).get());
                        return defaultUiModel(repository, builder).setGenerated(false).build();
                    }
                }
            }
            catch (final BuilderException e) {
                // Todo report in a right way
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    @Nullable private PsiUiModel<T> getCaretPsiUiModel(@NotNull final Editor e, @NotNull final PsiFile psiFile) {
        offset = e.getCaretModel().getOffset();
        final PsiElement element = psiFile.findElementAt(offset);
        return element != null ? getContainingPsiUiModel(element).getOrNull() : null;
    }

    @NotNull private Option<PsiUiModel<T>> getContainingPsiUiModel(@NotNull final PsiElement element) {
        return cast(findParentOfType(element, PsiUiModel.class));
    }

    //~ Static Fields ................................................................................................................................

    public static final String UI_MODEL_EXPANSION = "UiModelExpansion";

    private static final String DEFAULT_UI_MODEL_EXPANSION = "Default Form or WidgetDef expansion";
}  // end class DefaultFormExpansionAction
// end class DefaultFormExpansionAction
