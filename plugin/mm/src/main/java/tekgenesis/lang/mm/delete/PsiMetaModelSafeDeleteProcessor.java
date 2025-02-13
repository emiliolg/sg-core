
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.delete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.safeDelete.NonCodeUsageSearchInfo;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessorDelegateBase;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.gutter.GutterManager;
import tekgenesis.lang.mm.psi.MMIdentifier;
import tekgenesis.lang.mm.psi.PsiMetaModel;

import static tekgenesis.common.core.Option.some;

/**
 * Safe delete processor fot Meta Models.
 */
public class PsiMetaModelSafeDeleteProcessor extends SafeDeleteProcessorDelegateBase {

    //~ Instance Fields ..............................................................................................................................

    private boolean searchInComments      = false;
    private boolean searchTextOccurrences = false;

    //~ Methods ......................................................................................................................................

    @Nullable @Override public Collection<String> findConflicts(@NotNull PsiElement element, @NotNull PsiElement[] allElementsToDelete) {
        return null;
    }

    @Nullable @Override public NonCodeUsageSearchInfo findUsages(@NotNull PsiElement element, @NotNull PsiElement[] allElementsToDelete,
                                                                 @NotNull List<UsageInfo> result) {
        return null;
    }

    @Override public boolean handlesElement(PsiElement element) {
        return resolvePsiMetaModel(element).isPresent();
    }

    @Override public void prepareForDeletion(PsiElement element)
        throws IncorrectOperationException {}

    @Nullable @Override public UsageInfo[] preprocessUsages(Project project, UsageInfo[] usages) {
        return UsageInfo.EMPTY_ARRAY;
    }

    @Nullable @Override public Collection<PsiElement> getAdditionalElementsToDelete(@NotNull PsiElement             element,
                                                                                    @NotNull Collection<PsiElement> allElementsToDelete,
                                                                                    boolean                         askUser) {
        final List<PsiElement> result    = new ArrayList<>();
        final PsiClass         userClass = GutterManager.getPsiClass(resolvePsiMetaModel(element).get());
        if (userClass != null) {
            result.add(userClass);
            final PsiClass baseClass = userClass.getSuperClass();
            if (baseClass != null) result.add(baseClass);
        }
        return result;
    }

    @Nullable @Override public Collection<? extends PsiElement> getElementsToSearch(@NotNull PsiElement element, @Nullable Module module,
                                                                                    @NotNull Collection<PsiElement> allElementsToDelete) {
        final List<PsiElement> listOfElements = new ArrayList<>();
        for (final PsiElement auxElement : allElementsToDelete) {
            if (!(auxElement instanceof PsiMetaModel)) listOfElements.add(resolvePsiMetaModel(element).get());
            else listOfElements.add(element);
        }
        return listOfElements;
    }

    @Override public boolean isToSearchForTextOccurrences(PsiElement element) {
        return searchTextOccurrences;
    }

    @Override public boolean isToSearchInComments(PsiElement element) {
        return searchInComments;
    }

    @Override public void setToSearchForTextOccurrences(PsiElement element, boolean enabled) {
        searchTextOccurrences = enabled;
    }

    @Override public void setToSearchInComments(PsiElement element, boolean enabled) {
        searchInComments = enabled;
    }

    @SuppressWarnings("rawtypes")
    private Option<PsiMetaModel> resolvePsiMetaModel(PsiElement element) {
        Option<PsiMetaModel> result = Option.empty();
        if (element instanceof PsiMetaModel) result = some((PsiMetaModel) element);
        else if (element instanceof MMIdentifier && element.getParent().getParent() instanceof PsiMetaModel)
            result = some((PsiMetaModel) element.getParent().getParent());
        return result;
    }
}  // end class PsiMetaModelSafeDeleteProcessor
