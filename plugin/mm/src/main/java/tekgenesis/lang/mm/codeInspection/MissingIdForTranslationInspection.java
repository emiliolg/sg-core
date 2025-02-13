
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import java.util.ArrayList;
import java.util.List;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.ASTNode;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.WidgetType;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR_OR_WARNING;

/**
 * Inspection for structures that has label but not identifier. In this cases the i18n property can
 * not be generated and the translation will be incomplete.
 */
@SuppressWarnings("WeakerAccess")
public class MissingIdForTranslationInspection extends BaseInspection {

    //~ Methods ......................................................................................................................................

    @Override void checkInspection(@NotNull final Seq<PsiWidget> widgets, @NotNull final List<ProblemDescriptor> problems,
                                   @NotNull InspectionManager m, boolean isOnTheFly) {
        for (final PsiWidget widget : widgets) {
            final WidgetType widgetType = widget.getWidgetType();
            if (!skippedElementsToInspect.contains(widgetType)) {
                final Option<ASTNode> label = widget.getWidgetLabel();
                if (label.isPresent() && widget.getWidgetId().isEmpty() && widget.getBindingFqn().isEmpty()) {
                    final String            msg     = label.get().getText() + " does not have ID. The i18n property is not going to be generated.";
                    final AbstractFix<?, ?> fix     = null;
                    final ProblemDescriptor problem = m.createProblemDescriptor(label.get().getPsi(), msg, fix, GENERIC_ERROR_OR_WARNING, isOnTheFly);
                    problems.add(problem);
                }
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final List<WidgetType> skippedElementsToInspect = new ArrayList<>();

    static {
        skippedElementsToInspect.add(WidgetType.HEADER);
        skippedElementsToInspect.add(WidgetType.FOOTER);
        skippedElementsToInspect.add(WidgetType.INTERNAL);
    }
}  // end class MissingIdForTranslationInspection
