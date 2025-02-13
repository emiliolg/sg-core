
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightRangeExtension;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;

/**
 * Force highlight parent node in presence of errors.
 */
class MMHighlightRangeExtension implements HighlightRangeExtension {

    //~ Methods ......................................................................................................................................

    @Override public boolean isForceHighlightParents(@NotNull PsiFile file) {
        return file instanceof MMFile;
    }
}
