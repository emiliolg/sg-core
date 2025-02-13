
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElementVisitor;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;

/**
 * Represents a reference found in MetaModel code (either an identifier or a sequence of identifiers
 * separated by periods).
 */
public class PsiMetaModelCodeReferenceElementImpl extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    PsiMetaModelCodeReferenceElementImpl(MMElementType type) {
        super(type);
    }

    //~ Methods ......................................................................................................................................

    @Override public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof MetaModelElementVisitor) ((MetaModelElementVisitor) visitor).visitReferenceElement(this);
        else visitor.visitElement(this);
    }

    @NotNull @Override public MetaModelReference getReference() {
        return new MetaModelReference(this);
    }
}
