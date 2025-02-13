
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiReference;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;

/**
 * References a java class.
 */
public class PsiClassReference extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    /** Create an class reference. */
    public PsiClassReference(@NotNull final MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiReference getReference() {
        return new ClassReference(this);
    }
}
