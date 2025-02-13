
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiQualifiedReference;
import com.intellij.psi.PsiReference;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a reference found in MetaModel code (either an identifier or a sequence of identifiers
 * separated by periods, optionally with generic type arguments).
 */
public interface PsiMetaModelCodeReference extends PsiReference, PsiQualifiedReference {

    //~ Methods ......................................................................................................................................

    /** Returns the element which is the target of the reference. */
    @Nullable @Override PsiElement resolve();

    /**
     * Returns the element which is the target of the reference, specifying true if the code in the
     * context of which the reference is being resolved is considered incomplete.
     */
    @Nullable PsiElement resolve(boolean incompleteCode);

    /** Returns true if reference contains qualifier. */
    default boolean isQualified() {
        return getElement().isQualified();
    }

    /** Returns underlying code reference element. */
    @Override PsiMetaModelCodeReferenceElement getElement();
    @Nullable @Override default PsiElement getQualifier() {
        return getElement().getQualifier();
    }
    @Nullable @Override default String getReferenceName() {
        return getElement().getReferenceName();
    }
}
