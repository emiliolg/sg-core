
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

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.psi.*;

/**
 * Navigation inspection for quickly moving to the properties file.
 */
public class NavigationTranslationInspection extends MMTranslationInspection {

    //~ Methods ......................................................................................................................................

    @Override protected void checkChildren(String propertyFolderPath, MMCommonComposite commonComposite, MMFile mmFile,
                                           ArrayList<ProblemDescriptor> problemDescriptors, boolean isOnTheFly, @NotNull InspectionManager manager) {
        final VirtualFile vf = FileUtils.findVirtualFile(propertyFolderPath);
        if (vf == null) return;
        for (final VirtualFile child : vf.getChildren()) {
            if (child.getPath().matches(propertyFolderPath + "/" + commonComposite.getName() + "([\\.|_](\\w+))+")) {
                final PsiFile psiFile = PsiManager.getInstance(mmFile.getProject()).findFile(child);
                if (psiFile != null)
                    problemDescriptors.add(
                        manager.createProblemDescriptor(commonComposite.getIdentifier(),
                            "Navigate to " + psiFile.getName(),
                            new NavigationTranslationFix(psiFile),
                            ProblemHighlightType.WEAK_WARNING,
                            isOnTheFly));
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    private class NavigationTranslationFix extends AbstractFix<PsiFile, PsiElement> {
        private NavigationTranslationFix(PsiFile psiFile) {
            super("Navigate to properties file", psiFile);
        }

        @Override public void doApplyFix(Project project, PsiFile file, PsiElement endElement) {
            PsiUtils.scrollTo(file);
        }
    }
}
