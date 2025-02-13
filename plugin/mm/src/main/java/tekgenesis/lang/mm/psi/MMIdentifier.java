
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
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;

/**
 * An Identifier.
 */
public class MMIdentifier extends MMLeafElement implements PsiNamedElement, PsiNameIdentifierOwner {

    //~ Constructors .................................................................................................................................

    /** An Identifier. */
    MMIdentifier(CharSequence text) {
        super(MMElementType.IDENTIFIER, text);
    }

    //~ Methods ......................................................................................................................................

    @Override public String getName() {
        return getText();
    }

    // @Override public String toString() { return getName(); }

    @Override public PsiElement setName(@NonNls @NotNull String name)
        throws IncorrectOperationException
    {
        replace(MetaModelElementFactory.createIdentifier(getProject(), name));
        return this;
    }

    @Nullable @Override public PsiElement getNameIdentifier() {
        return this;
    }
}
