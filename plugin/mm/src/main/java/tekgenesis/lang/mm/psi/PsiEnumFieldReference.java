
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
 * PsiReference of an enum field inside a MM file.
 */
public class PsiEnumFieldReference extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    PsiEnumFieldReference(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public EnumFieldReference getReference() {
        return new EnumFieldReference("", TextRange.create(0, getTextLength()), this);
    }
}
