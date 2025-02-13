
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import javax.swing.*;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.actions.DefaultUiModelExpansionAction;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.lang.mm.actions.DefaultUiModelExpansionAction.UI_MODEL_EXPANSION;

/**
 * Form Psi.
 */
public class PsiForm extends PsiUiModel<Form> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiForm(MMElementType t) {
        super(t, MetaModelKind.FORM, Form.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        super.annotate(holder);
        if (getWidgets().isEmpty()) {
            final Annotation annotation = holder.createWeakWarningAnnotation(getTextRange().grown(-1), EXPAND_DEFAULT_FORM);
            annotation.registerFix(new ExpandFormAction(this));
        }
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.FORM_ICON;
    }

    //~ Static Fields ................................................................................................................................

    private static final String EXPAND_DEFAULT_FORM = "Expand Default Form";

    //~ Inner Classes ................................................................................................................................

    private static class ExpandFormAction implements IntentionAction {
        private final PsiForm form;

        ExpandFormAction(PsiForm form) {
            this.form = form;
        }

        @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException
        {  //
            PsiUtils.getModelRepository(form).ifPresent(repository -> {
                final DefaultUiModelExpansionAction<Form> expand = new DefaultUiModelExpansionAction<Form>(editor, project, repository);
                CommandProcessor.getInstance().executeCommand(project, expand, UI_MODEL_EXPANSION, null);
            });
        }

        @Override public boolean startInWriteAction() {
            return true;
        }

        @Override public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return PsiUtils.getModelRepository(form).isPresent();
        }

        @NotNull @Override public String getFamilyName() {
            return getText();
        }

        @NotNull @Override public String getText() {
            return "Expand form";
        }
    }
}  // end class PsiForm
