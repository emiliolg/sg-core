
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.delete;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.*;

/**
 * Safe delete support provider for Meta Models.
 */
public class SafeDeleteMMSupportProvider extends RefactoringSupportProvider {

    //~ Methods ......................................................................................................................................

    /**
     * Checks if the element is a Meta Model on witch Safe Delete refactoring can be applied The
     * Safe Delete refactoring also requires the plugin to implement Find Usages functionality.
     *
     * @param   element  the element for which Safe Delete was invoked
     *
     * @return  true if Safe Delete is available, false otherwise.
     */
    public boolean isAvailable(@NotNull PsiElement element) {
        return element instanceof PsiMetaModel;
    }

    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        if (element instanceof PsiMetaModel || element instanceof MMIdentifier) {
            PsiElement auxElement = element;
            while (!(auxElement instanceof PsiMetaModel))
                auxElement = auxElement.getParent();
            return true;
        }
        return false;
    }
}
