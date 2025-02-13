
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

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.common.core.Option.option;

/**
 * An Entity Field.
 */
public class PsiEntityField extends PsiModelField {

    //~ Constructors .................................................................................................................................

    /** Creates an entity field. */
    PsiEntityField(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    /** Return field as inner entity option. */
    public Option<PsiEntity> asInnerEntity() {
        return option((PsiEntity) findChildByType(MMElementType.ENTITY));
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.ATTRIBUTE_ICON;
    }

    @Override public boolean isPhysical() {
        return false;
    }

    @Override public PsiElement setName(@NonNls @NotNull String name)
        throws IncorrectOperationException
    {
        final PsiElement newChild    = MetaModelElementFactory.createIdentifier(getProject(), name);
        final ASTNode    childByType = findChildByType(MMElementType.LABELED_ID);
        if (childByType != null) childByType.replaceChild(childByType.getFirstChildNode(), newChild.getNode());
        return this;
    }

    /** Get entity attribute Type element. */
    public PsiElement getPsiType() {
        if (getParent().getParent() instanceof PsiView) return findPsiChildByType(MMElementType.FIELD_REF);
        else return findPsiChildByType(MMElementType.TYPE);
    }

    /** Get entity attribute Type name. */
    public String getTypeName() {
        final PsiElement type = getPsiType();
        return type != null ? type.getText() : "";
    }

    /** Get entity attribute TypeReference node (if any). For test purposes. */
    public PsiElement getTypeRef() {
        final PsiElement type = getPsiType();
        return type != null ? type.getFirstChild() : null;
    }

    /** checks if selected field is InnerEntity. */
    public boolean isInnerEntity() {
        return findChildByType(MMElementType.ENTITY) != null;
    }
}  // end class PsiEntityField
