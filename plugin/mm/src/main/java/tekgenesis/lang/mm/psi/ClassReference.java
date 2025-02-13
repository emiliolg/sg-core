
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.QName.qualify;

/**
 * Reference to a java class.
 */
class ClassReference extends AbstractReference {

    //~ Constructors .................................................................................................................................

    ClassReference(PsiClassReference metaModelASTs) {
        super(metaModelASTs);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<PsiClass> resolveInner() {
        return PsiUtils.getPsiClassForFqn(getProject(), getClassName());
    }

    @Override public boolean isReferenceTo(PsiElement element) {
        return element instanceof PsiClass && super.isReferenceTo(element);
    }

    @NotNull @Override public Object[] getVariants() {
        return LookupElement.EMPTY_ARRAY;
    }

    private String getClassName() {
        final PsiMetaModelCodeReferenceElement element = getElement();
        return qualify(element.getDomain(), element.getText());
    }
}
