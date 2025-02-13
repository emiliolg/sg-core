
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
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiWidget;

import static tekgenesis.common.Predefined.cast;

/**
 * Base checkFile impl for MissingIdForTranslationInspection & IgnoreLabelInspection.
 */
public abstract class BaseInspection extends LocalInspectionTool {

    //~ Methods ......................................................................................................................................

    @Override public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        final MMFile    mmFile = cast(file);
        final PsiDomain domain = mmFile.getDomain();
        if (domain == null || domain.getDomainName() == null) return null;

        final ArrayList<ProblemDescriptor> problemDescriptors = new ArrayList<>();
        for (final PsiForm form : mmFile.getForms())
            checkInspection(form.getWidgets(), problemDescriptors, manager, isOnTheFly);

        return problemDescriptors.toArray(new ProblemDescriptor[problemDescriptors.size()]);
    }

    abstract void checkInspection(Seq<PsiWidget> widgets, List<ProblemDescriptor> problemDescriptors, InspectionManager manager, boolean isOnTheFly);
}
