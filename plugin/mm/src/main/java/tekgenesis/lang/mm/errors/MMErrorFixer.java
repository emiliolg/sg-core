
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.errors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.intention.AbstractIntentionAction;
import com.intellij.ide.DataManager;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.actions.RenameElementAction;
import com.intellij.refactoring.rename.RenameHandlerRegistry;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.lang.mm.Distance;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.MMCommonComposite;
import tekgenesis.lang.mm.psi.PsiEntityField;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderErrors;
import tekgenesis.metadata.exception.NullableInPrimaryKeyException;
import tekgenesis.mmcompiler.ast.MetaModelAST;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * MetaModel Error Fixes.
 */
public class MMErrorFixer {

    //~ Constructors .................................................................................................................................

    private MMErrorFixer() {}

    //~ Methods ......................................................................................................................................

    /** create list popup for the specified Tuple. */
    @SuppressWarnings("WeakerAccess")
    public static BaseListPopupStep<Tuple<String, String>> createPopUp(final PsiFile file, final Document document,
                                                                       final List<Tuple<String, String>> choices, final Annotation annotation,
                                                                       final String oldAttribute) {
        final BaseListPopupStep<Tuple<String, String>> step;
        if (!choices.isEmpty()) step = new BaseListPopupStep<Tuple<String, String>>("Did you mean?", choices) {
                @NotNull @Override public String getTextFor(Tuple<String, String> value) {
                    return value.first() + ": " + value.second();}
                @Override public PopupStep<?> onChosen(final Tuple<String, String> selectedValue, boolean finalChoice) {
                    if (selectedValue == null) return FINAL_CHOICE;

                    if (finalChoice) {
                        final UndoableAttributeFix fix = new UndoableAttributeFix(document, file, annotation, oldAttribute, selectedValue.first());
                        CommandProcessor.getInstance().executeCommand(file.getProject(), fix, "AttributeFix", null);
                        return FINAL_CHOICE;
                    }
                    return FINAL_CHOICE;
                }
            };
        else step = new BaseListPopupStep<>("Don't Panic! No Available Choices Found in Module", choices);
        return step;
    }

    /** returns the appropriate FixIntention for an error. */
    public static <T extends BuilderError> Option<FixIntention<T>> provideFix(T error) {
        final FixIntention<T> fix = cast(FIXES.get(error.getClass()));

        if (fix != null) {
            try {
                return some(fix.withError(error));
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        return Option.empty();
    }

    private static <T extends BuilderError> void registerFix(@NotNull final FixIntention<T> fix) {
        FIXES.put(fix.getErrorClass(), fix);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<Class<? extends BuilderError>, FixIntention<?>> FIXES = new HashMap<>();

    static {
        registerFix(new OptionalAttributeInPrimaryKeyFix());
        registerFix(new LowerCaseModelFix());
        registerFix(new JavaReservedWordModelFix());
    }

    //~ Inner Classes ................................................................................................................................

    public abstract static class FixIntention<T extends BuilderError> extends AbstractIntentionAction {
        Annotation             annotation = null;
        T                      error      = null;
        MetaModelAST           node       = null;
        private final Class<T> errorClass;

        private FixIntention(@NotNull final Class<T> errorClass) {
            this.errorClass = errorClass;
        }

        /** Set annotation context. */
        public FixIntention<T> withAnnotation(@NotNull final Annotation context) {
            annotation = context;
            return this;
        }

        /** Set node context. */
        public FixIntention<T> withNode(@NotNull final MetaModelAST context) {
            node = context;
            return this;
        }

        /** Get error corresponding to the fix. */
        public T getError() {
            return error;
        }

        @Nullable String basicInvoke(@NotNull Project project, Editor editor, @NotNull PsiFile file) {
            if (!FileModificationService.getInstance().prepareFileForWrite(file)) return null;

            final VirtualFile virtualFile = file.getVirtualFile();
            final Document    document    = editor.getDocument();
            if (virtualFile == null) return null;

            final List<Tuple<String, String>>              choices      = Distance.findSimilarFilterSiblings(node,
                    ModuleUtil.findModuleForFile(virtualFile, project));
            final String                                   oldAttribute = document.getText(
                    new TextRange(annotation.getStartOffset(), annotation.getEndOffset()));
            final BaseListPopupStep<Tuple<String, String>> step         = createPopUp(file, document, choices, annotation, oldAttribute);
            JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(editor);

            return oldAttribute;
        }

        /** Set node context. */
        abstract FixIntention<T> withError(@NotNull final T error);

        /** Set node context. */
        FixIntention<T> setError(@NotNull final T context) {
            error = context;
            return this;
        }

        private Class<T> getErrorClass() {
            return errorClass;
        }
    }  // end class FixIntention

    private static class ImportMetaModelReferenceFix extends FixIntention<BuilderErrors.UnresolvedReference> {
        private String oldAttribute = null;

        private ImportMetaModelReferenceFix() {
            super(BuilderErrors.UnresolvedReference.class);
        }

        @Override public void invoke(@NotNull final Project project, final Editor editor, @NotNull final PsiFile file)
            throws IncorrectOperationException
        {
            final String result = basicInvoke(project, editor, file);
            if (result != null) oldAttribute = result;
        }

        @NotNull @Override public String getText() {
            return MSGS.resolveReference();
        }

        @Override FixIntention<BuilderErrors.UnresolvedReference> withError(@NotNull BuilderErrors.UnresolvedReference e) {
            return new UnresolvedReferenceFix().setError(e);
        }
    }

    private static class JavaReservedWordModelFix extends FixIntention<BuilderErrors.JavaReservedWordModel> {
        private JavaReservedWordModelFix() {
            super(BuilderErrors.JavaReservedWordModel.class);
        }

        @Override public void invoke(@NotNull final Project project, final Editor editor, @NotNull final PsiFile file)
            throws IncorrectOperationException
        {
            if (!FileModificationService.getInstance().prepareFileForWrite(file)) return;

            // TODO It should be better an Inplace rename instead of the ui dialog

            final PsiElement              psiElement = (PsiElement) node;
            final HashMap<String, Object> map        = new HashMap<>();

            if (editor instanceof EditorWindow) {
                map.put(PlatformDataKeys.EDITOR.getName(), editor);
                map.put(LangDataKeys.PSI_ELEMENT.getName(), psiElement);
            }

            final Boolean selectAll = editor.getUserData(RenameHandlerRegistry.SELECT_ALL);
            try {
                editor.putUserData(RenameHandlerRegistry.SELECT_ALL, true);
                final DataContext   dataContext          = DataManager.getInstance().getDataContext(editor.getComponent());
                final DataContext   simpleContext        = SimpleDataContext.getSimpleContext(map, dataContext);
                final AnAction      action               = new RenameElementAction();
                final Presentation  templatePresentation = action.getTemplatePresentation();
                final AnActionEvent event                = new AnActionEvent(null,
                        simpleContext,
                        "",
                        templatePresentation,
                        ActionManager.getInstance(),
                        0);
                action.actionPerformed(event);
            }
            finally {
                editor.putUserData(RenameHandlerRegistry.SELECT_ALL, selectAll);
            }
        }

        @NotNull @Override public String getText() {
            return "Rename to...";
        }

        @Override FixIntention<BuilderErrors.JavaReservedWordModel> withError(@NotNull BuilderErrors.JavaReservedWordModel error1) {
            return new JavaReservedWordModelFix().setError(error1);
        }
    }  // end class JavaReservedWordModelFix

    private static class LowerCaseModelFix extends FixIntention<BuilderErrors.LowerCaseModel> {
        private LowerCaseModelFix() {
            super(BuilderErrors.LowerCaseModel.class);
        }

        @Override public void invoke(@NotNull final Project project, final Editor editor, @NotNull final PsiFile file)
            throws IncorrectOperationException
        {
            if (!FileModificationService.getInstance().prepareFileForWrite(file)) return;

            final VirtualFile virtualFile = file.getVirtualFile();
            final Document    document    = editor.getDocument();
            if (virtualFile == null) return;

            final int startOffset = ((PsiElement) node).getTextRange().getStartOffset();
            final int endOffset   = startOffset + error.getModelName().length();

            new WriteCommandAction.Simple<Object>(project, file) {
                    @Override public void run() {
                        document.replaceString(startOffset, endOffset, capitalizeFirst(error.getModelName()));
                    }
                }.execute();
        }

        @NotNull @Override public String getText() {
            return "Rename to '" + capitalizeFirst(error.getModelName()) + "'";
        }

        @Override FixIntention<BuilderErrors.LowerCaseModel> withError(@NotNull BuilderErrors.LowerCaseModel error1) {
            return new LowerCaseModelFix().setError(error1);
        }
    }

    private static class OptionalAttributeInPrimaryKeyFix extends FixIntention<NullableInPrimaryKeyException> {
        private OptionalAttributeInPrimaryKeyFix() {
            super(NullableInPrimaryKeyException.class);
        }

        @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException
        {
            final MMCommonComposite optionNode = (MMCommonComposite) getOptionalNode();

            assert optionNode != null;
            final PsiElement commaNode = optionNode.getPreviousComma();
            final Document   document  = editor.getDocument();

            new WriteCommandAction.Simple<Object>(project, file) {
                    @Override public void run() {
                        final TextRange textRange = optionNode.getTextRange();
                        if (commaNode != null) document.deleteString(commaNode.getTextOffset(), textRange.getEndOffset());
                    }
                }.execute();
        }

        @NotNull @Override public String getText() {
            return "Remove optional value";
        }

        @Override FixIntention<NullableInPrimaryKeyException> withError(@NotNull NullableInPrimaryKeyException error1) {
            return new OptionalAttributeInPrimaryKeyFix().setError(error1);
        }

        @Nullable
        @SuppressWarnings("DuplicateStringLiteralInspection")
        private ASTNode getOptionalNode() {
            ASTNode optionNode = ((PsiEntityField) node).findChildByType(MMElementType.OPTION);
            while (optionNode != null && !("optional".equals(optionNode.getText())))
                optionNode = optionNode.getTreeNext();
            return optionNode;
        }
    }

    private static class UndoableAttributeFix implements Runnable, UndoableAction {
        private final Annotation annotation;

        private final Document document;
        private final PsiFile  file;
        private final String   newAttribute;
        private final String   oldAttribute;

        private UndoableAttributeFix(Document document, PsiFile file, Annotation annotation, String oldAttribute, String newAttribute) {
            this.oldAttribute = oldAttribute;
            this.document     = document;
            this.newAttribute = newAttribute;
            this.file         = file;
            this.annotation   = annotation;
        }

        @Override public void redo()
            throws UnexpectedUndoException {}

        @Override public void run() {
            FileModificationService.getInstance().prepareFileForWrite(file);
            final int startOffset = annotation.getStartOffset();
            ApplicationManager.getApplication().runWriteAction(() -> {
                document.deleteString(startOffset, annotation.getEndOffset());
                document.insertString(startOffset, newAttribute);
            });
        }

        @Override public void undo()
            throws UnexpectedUndoException
        {
            ApplicationManager.getApplication().runWriteAction(() -> {
                document.deleteString(annotation.getStartOffset(), annotation.getStartOffset() + newAttribute.length());
                document.insertString(annotation.getStartOffset(), oldAttribute);
            });
        }

        @Override public DocumentReference[] getAffectedDocuments() {
            return null;
        }

        @Override public boolean isGlobal() {
            return false;
        }
    }  // end class UndoableAttributeFix

    private static class UnresolvedReferenceFix extends FixIntention<BuilderErrors.UnresolvedReference> {
        private String oldAttribute = null;

        private UnresolvedReferenceFix() {
            super(BuilderErrors.UnresolvedReference.class);
        }

        @Override public void invoke(@NotNull final Project project, final Editor editor, @NotNull final PsiFile file)
            throws IncorrectOperationException
        {
            final String result = basicInvoke(project, editor, file);
            if (result != null) oldAttribute = result;
        }

        @NotNull @Override public String getText() {
            return MSGS.resolveReference();
        }

        @Override FixIntention<BuilderErrors.UnresolvedReference> withError(@NotNull BuilderErrors.UnresolvedReference e) {
            return new UnresolvedReferenceFix().setError(e);
        }
    }
}  // end class MMErrorFixer
