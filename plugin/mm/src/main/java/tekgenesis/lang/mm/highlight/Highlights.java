
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.quickfix.QuickFixActionRegistrarImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.psi.PsiMetaModelCodeReferenceElement;

import static com.intellij.codeInsight.quickfix.UnresolvedReferenceQuickFixProvider.registerReferenceFixes;

/**
 * Highlight utility class.
 */
class Highlights {

    //~ Constructors .................................................................................................................................

    private Highlights() {}

    //~ Methods ......................................................................................................................................

    @Nullable static HighlightInfo checkReference(@NotNull final PsiMetaModelCodeReferenceElement ref) {
        final PsiReference reference = ref.getReference();
        if (reference != null) {
            final PsiElement resolve = reference.resolve();
            if (resolve == null) {
                final HighlightInfoType type = HighlightInfoType.INJECTED_LANGUAGE_FRAGMENT;
                final HighlightInfo     info = HighlightInfo.newHighlightInfo(type).range((PsiElement) ref).create();
                registerReferenceFixes(reference, new QuickFixActionRegistrarImpl(info));
                return info;
            }
        }
        return null;
    }
}
