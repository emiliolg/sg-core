
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.gutter;

import java.util.Collection;
import java.util.List;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.lang.mm.gutter.GutterManager.IMPLEMENTED_IN;

/**
 * Line Marker Provider for MM Files.
 */
@SuppressWarnings("WeakerAccess")
public class MMLineMarkerProvider implements LineMarkerProvider {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("rawtypes")
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {}

    @Override
    @SuppressWarnings("DialogTitleCapitalization")
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (element instanceof PsiMetaModel) {
            final List<PsiClass> targets = ((PsiMetaModel<?>) element).getLineMarkerTargets();

            if (targets.isEmpty()) return null;

            final String                                  msg         = CodeInsightBundle.message("goto.implementation.chooserTitle",
                    ((PsiMetaModel<?>) element).getName(),
                    targets.size(),
                    "");
            final NavigationGutterIconBuilder<PsiElement> iconBuilder = NavigationGutterIconBuilder.create(GutterManager.IMPLEMENTED_ICON)
                                                                        .setTargets(targets)
                                                                        .setPopupTitle(msg)
                                                                        .setTooltipText(IMPLEMENTED_IN + ((PsiMetaModel<?>) element).getFullName());
            return iconBuilder.createLineMarkerInfo(element);
        }

        if (element instanceof PsiWidget) {
            final PsiWidget  widget = (PsiWidget) element;
            final WidgetType type   = widget.getWidgetType();
            if (type != null && type.isMultiple()) return widget.getMultipleLineMarker();
        }

        return null;
    }
}  // end class MMLineMarkerProvider
