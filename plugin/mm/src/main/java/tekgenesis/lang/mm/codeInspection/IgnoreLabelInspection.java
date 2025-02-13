
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import java.util.List;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.MMLeafElement;
import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR_OR_WARNING;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.metadata.form.widget.WidgetType.INPUT_GROUP;

/**
 * Inspection to highlight that contents of input group widget will ignore any associated label.
 */
public class IgnoreLabelInspection extends BaseInspection {

    //~ Methods ......................................................................................................................................

    @Override void checkInspection(@NotNull final Seq<PsiWidget> widgets, @NotNull final List<ProblemDescriptor> problems,
                                   @NotNull InspectionManager m, boolean isOnTheFly) {
        for (final PsiWidget widget : widgets) {
            final Option<ASTNode> label = widget.getWidgetLabel();

            if (label.isPresent() && (checkInputGroupParent(widget) || checkForFieldType(widget))) {
                final String            msg     = MSGS.labelIgnored(label.get().getText());
                final AbstractFix<?, ?> fix     = new IgnoreLabelFix(label.get().getPsi());
                final ProblemDescriptor problem = m.createProblemDescriptor(label.get().getPsi(), msg, fix, GENERIC_ERROR_OR_WARNING, isOnTheFly);
                problems.add(problem);
            }
        }
    }

    private boolean checkForFieldType(final PsiWidget field) {
        final Option<Widget> widget = field.getWidget();
        return field.getWidgetType() == WidgetType.SUBFORM && widget.isPresent() && widget.get().isInline();
    }

    private boolean checkInputGroupParent(PsiElement field) {
        final PsiElement parent = field.getParent();
        return !(parent instanceof PsiForm) &&
               (parent instanceof PsiWidget && ((PsiWidget) parent).getWidgetType() == INPUT_GROUP || checkInputGroupParent(parent));
    }

    //~ Inner Classes ................................................................................................................................

    private class IgnoreLabelFix extends AbstractFix<PsiElement, PsiElement> {
        private IgnoreLabelFix(PsiElement element) {
            super(MSGS.removeLabel(), element);
        }

        @Override public void doApplyFix(Project project, PsiElement startElement, PsiElement endElement) {
            final MMLeafElement label = (MMLeafElement) startElement;
            label.delete();
        }
    }
}  // end class IgnoreLabelInspection
