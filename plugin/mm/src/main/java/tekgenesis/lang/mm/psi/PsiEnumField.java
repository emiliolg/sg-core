
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import javax.swing.*;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;

/**
 * An EnumType field.
 */
public class PsiEnumField extends PsiModelField {

    //~ Constructors .................................................................................................................................

    /** Creates an enum field. */
    PsiEnumField(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public Icon getIcon(int flags) {
        return MMFileType.ENUM_VALUE_ICON;
    }

    @Override public boolean isPhysical() {
        return false;
    }

    @NotNull @Override public String getName() {
        final ASTNode id = findChildByType(MMElementType.ENUM_VALUE);
        return id != null ? id.getText() : "";
    }

    @Override public PsiElement setName(@NonNls @NotNull String name)
        throws IncorrectOperationException
    {
        final ASTNode childByType = findChildByType(MMElementType.ENUM_VALUE);
        if (childByType != null) {
            final ASTNode id = childByType.findChildByType(MMElementType.IDENTIFIER);
            if (id != null) replaceChild(id, MetaModelElementFactory.createIdentifier(getProject(), name).getNode());
        }
        return this;
    }

    /** Returns the EnumType value string. */
    public String getValue() {
        final PsiElement psiChildByType = findPsiChildByType(MMElementType.COLON);
        if (psiChildByType != null) {
            final PsiElement nextSibling = psiChildByType.getNextSibling();
            if (nextSibling != null) {
                final PsiElement sibling = nextSibling.getNextSibling();
                return (sibling != null ? sibling.getText() : "");
            }
        }
        return "";
    }
}  // end class PsiEnumField
