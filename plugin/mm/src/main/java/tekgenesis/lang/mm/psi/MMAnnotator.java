
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.CommonPsiElement;

/**
 * Annotator for psi elements.
 */
@SuppressWarnings("WeakerAccess")
public class MMAnnotator implements Annotator {

    //~ Methods ......................................................................................................................................

    @Override public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        final PsiFile file = element.getContainingFile();
        if (element instanceof CommonPsiElement && file != null && file.isWritable()) ((CommonPsiElement<?, ?>) element).annotate(holder);
    }
}
