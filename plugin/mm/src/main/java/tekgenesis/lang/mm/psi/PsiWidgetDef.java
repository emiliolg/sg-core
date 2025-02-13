
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
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.lang.mm.actions.DefaultUiModelExpansionAction.UI_MODEL_EXPANSION;

/**
 * Widget Def Psi.
 */
public class PsiWidgetDef extends PsiUiModel<WidgetDef> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiWidgetDef(MMElementType t) {
        super(t, MetaModelKind.WIDGET, WidgetDef.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        super.annotate(holder);
        if (getWidgets().isEmpty() && getTreeParent() != null && !(getTreeParent().getPsi() instanceof PsiType)) {
            final Annotation annotation = holder.createWeakWarningAnnotation(getTextRange().grown(-1), EXPAND_DEFAULT_WIDGET);
            annotation.registerFix(new ExpandWidgetAction(this));
        }
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.WIDGET_DEF_ICON;
    }

    //~ Static Fields ................................................................................................................................

    private static final String EXPAND_DEFAULT_WIDGET = "Expand default widget definition";

    //~ Inner Classes ................................................................................................................................

    private static class ExpandWidgetAction implements IntentionAction {
        private final PsiWidgetDef widgetDef;

        ExpandWidgetAction(PsiWidgetDef widgetDef) {
            this.widgetDef = widgetDef;
        }

        @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException
        {  //
            PsiUtils.getModelRepository(widgetDef).ifPresent(repository -> {
                final DefaultUiModelExpansionAction<WidgetDef> expand = new DefaultUiModelExpansionAction<WidgetDef>(editor, project, repository);
                CommandProcessor.getInstance().executeCommand(project, expand, UI_MODEL_EXPANSION, null);
            });
        }

        @Override public boolean startInWriteAction() {
            return true;
        }

        @Override public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return PsiUtils.getModelRepository(widgetDef).isPresent();
        }

        @NotNull @Override public String getFamilyName() {
            return getText();
        }

        @NotNull @Override public String getText() {
            return "Expand widget definition";
        }
    }
}  // end class PsiWidgetDef
