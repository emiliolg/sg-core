
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import java.util.Arrays;
import java.util.List;

import com.intellij.psi.PsiElement;
import com.siyeh.ipp.base.PsiElementPredicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;

import static tekgenesis.common.collections.Colls.exists;

/**
 * Predicate for elements matching MetaModel top level definition identifiers.
 */
class MetaModelNamePredicate implements PsiElementPredicate {

    //~ Instance Fields ..............................................................................................................................

    private final List<MMElementType> models;

    //~ Constructors .................................................................................................................................

    MetaModelNamePredicate(@NotNull MMElementType... types) {
        models = Arrays.asList(types);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean satisfiedBy(PsiElement element) {
        // Only metamodel identifiers
        if (element.getNode().getElementType() != MMElementType.IDENTIFIER) return false;

        final PsiElement parent = element.getParent();
        if (parent == null || parent.getNode().getElementType() != MMElementType.LABELED_ID) return false;

        final PsiElement candidate = parent.getParent();
        return exists(models, m -> candidate != null && candidate.getNode().getElementType() == m);
    }
}
