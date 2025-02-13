
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.MMCommonComposite;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;
import tekgenesis.util.CheckDefault;

/**
 * Inspection for a Redundant Widget.
 */
public class DefaultWidgetInspection extends LocalInspectionTool {

    //~ Methods ......................................................................................................................................

    /** builds visitor for PsiElements. */
    @NotNull public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new PsiElementVisitor() {
            public void visitElement(PsiElement element) {
                if (element.getNode().getElementType() == MMElementType.WIDGET) {
                    final PsiWidget  psiWidget      = (PsiWidget) element;
                    final PsiElement widgetTypeNode = psiWidget.findWidgetType();
                    if (widgetTypeNode == null || psiWidget.getWidgetDataType().isEmpty()) return;

                    psiWidget.getUiModel().getModel().ifPresent(metadata -> {
                        if (metadata.containsElement(psiWidget.getName())) {
                            final Widget          widget     = metadata.getElement(psiWidget.getName());
                            final ModelRepository repository = psiWidget.getModelRepository();

                            if (CheckDefault.isDefaultWidget(widget, repository))
                                holder.registerProblem(
                                    holder.getManager()
                                          .createProblemDescriptor(widgetTypeNode,
                                              RemoveUnnecessaryWidget.REDUNDANT_WIDGET_TYPE,
                                              true,
                                              ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                              isOnTheFly,
                                              new RemoveUnnecessaryWidget(widgetTypeNode)));
                        }
                    });
                }
            }
        };
    }

    @Nls @NotNull @Override public String getDisplayName() {
        return "Redundant Widget ";  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Nls @NotNull @Override public String getGroupDisplayName() {
        return "Widget Inspections";  // To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull @Override public String getShortName() {
        return "DefaultWidget";  // To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isEnabledByDefault() {
        return false;
    }

    //~ Inner Classes ................................................................................................................................

    private static class RemoveUnnecessaryWidget extends AbstractFix<PsiElement, PsiElement> {
        private RemoveUnnecessaryWidget(PsiElement e) {
            super(REDUNDANT_WIDGET_TYPE, e);
        }

        @Override public void doApplyFix(Project project, PsiElement startElement, PsiElement endElement) {
            final MMCommonComposite widget        = (MMCommonComposite) startElement;
            final PsiElement        previousComma = widget.getPreviousComma();
            if (previousComma != null) previousComma.delete();
            widget.delete();
        }

        static final String REDUNDANT_WIDGET_TYPE = "Remove redundant widget type";
    }
}  // end class DefaultWidgetInspection
