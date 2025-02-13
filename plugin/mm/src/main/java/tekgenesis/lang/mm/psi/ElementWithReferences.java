
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
import com.intellij.psi.PsiReference;

import tekgenesis.lang.mm.MMElementType;

/**
 * Defines an element with one or more references.
 */
class ElementWithReferences extends MMCommonComposite implements PsiMetaModelCodeReferenceElement {

    //~ Constructors .................................................................................................................................

    /** Creates an element with references. */
    ElementWithReferences(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiReference getReference() {
        return null;
    }

    protected TextRange getEntireTextRange() {
        return TextRange.create(0, getTextLength());
    }
}
