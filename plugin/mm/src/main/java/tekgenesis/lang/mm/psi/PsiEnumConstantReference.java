
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.openapi.util.TextRange;

import tekgenesis.lang.mm.MMElementType;

/**
 * PsiReference from an enum field in MM to an enum constant in java.
 */
class PsiEnumConstantReference extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    PsiEnumConstantReference(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public EnumConstantReference getReference() {
        final String fqn = getEnumField().getFullName();
        return new EnumConstantReference(TextRange.create(0, getTextLength()), this, fqn);
    }

    private PsiEnumField getEnumField() {
        return (PsiEnumField) getParent();
    }
}
