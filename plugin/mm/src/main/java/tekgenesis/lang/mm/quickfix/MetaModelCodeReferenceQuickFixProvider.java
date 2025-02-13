
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.quickfix;

import com.intellij.codeInsight.daemon.QuickFixActionRegistrar;
import com.intellij.codeInsight.quickfix.UnresolvedReferenceQuickFixProvider;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.PsiMetaModelCodeReference;

/**
 * MetaModel language quick fix provider.
 */
public class MetaModelCodeReferenceQuickFixProvider extends UnresolvedReferenceQuickFixProvider<PsiMetaModelCodeReference> {

    //~ Methods ......................................................................................................................................

    @Override public void registerFixes(@NotNull PsiMetaModelCodeReference ref, @NotNull QuickFixActionRegistrar registrar) {
        registrar.register(new ImportMetaModelFix(ref.getElement(), ref));
    }

    @NotNull @Override public Class<PsiMetaModelCodeReference> getReferenceClass() {
        return PsiMetaModelCodeReference.class;
    }
}
