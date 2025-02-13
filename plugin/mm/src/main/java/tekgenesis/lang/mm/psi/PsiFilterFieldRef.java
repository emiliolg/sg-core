
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.type.EntityReference;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.Typed;

import static tekgenesis.lang.mm.psi.PsiUtils.findParentOfType;

/**
 * PsiReference of an entity field inside a MM file.
 */
public class PsiFilterFieldRef extends ElementWithReferences implements PsiMetaModelCodeReference {

    //~ Constructors .................................................................................................................................

    PsiFilterFieldRef(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public PsiElement bindToElement(@NotNull PsiElement psiElement)
        throws IncorrectOperationException
    {
        return null;
    }

    @Override public PsiElement handleElementRename(String s)
        throws IncorrectOperationException
    {
        replaceChild(getLastChildNode(), MetaModelElementFactory.createIdentifier(getProject(), getName()).getNode());
        return this;
    }

    @Nullable @Override public PsiElement resolve() {
        return resolve(false);
    }

    @Nullable @Override public PsiElement resolve(boolean incompleteCode) {
        final Option<PsiWidget> ancestor = getAncestor(PsiWidget.class);
        if (ancestor.isEmpty()) return null;

        final Type type = getFinalType(ancestor.get());
        if (type == null) return null;

        if (type.isEntity()) return ancestor.map(this::psiEntityFromWidget).map(entity -> entity.getFieldNullable(getName())).getOrNull();

        if (type.isEnum()) return ancestor.map(this::psiEnumFromWidget).map(e -> e.getAttribute(getName())).getOrNull();

        return null;
    }

    @NotNull @Override public String getCanonicalText() {
        return getText();
    }

    @Override public boolean isQualified() {
        return getQualifier() != null;
    }

    @Override public PsiMetaModelCodeReferenceElement getElement() {
        return this;
    }

    @Override public boolean isReferenceTo(PsiElement psiElement) {
        return findParentOfType(psiElement, PsiEntityField.class)  //
               .map(field -> getName().equals(field.getName()))    //
               .orElseGet(() -> Boolean.FALSE);                    //
    }

    @Nullable @Override public PsiElement getQualifier() {
        return null;
    }

    @Override public TextRange getRangeInElement() {
        return getEntireTextRange();
    }

    @Override public PsiReference getReference() {
        return this;
    }

    @Nullable @Override public String getReferenceName() {
        return null;
    }

    @Override public boolean isSoft() {
        return false;
    }

    @NotNull @Override public Object[] getVariants() {
        final Option<PsiWidget> ancestor = getAncestor(PsiWidget.class);
        if (ancestor.isEmpty()) return EMPTY_ARRAY;

        final Type type = getFinalType(ancestor.get());
        if (type == null) return EMPTY_ARRAY;

        if (type.isEntity()) return ancestor.map(this::psiEntityFromWidget).map(PsiEntity::getFields).orElseGet(() -> EMPTY_ARRAY);

        if (type.isEnum()) return ancestor.map(this::psiEnumFromWidget).map(PsiEnum::getAttributes).orElseGet(() -> EMPTY_ARRAY);

        return EMPTY_ARRAY;
    }

    @Nullable private PsiEntity psiEntityFromWidget(PsiWidget widget) {
        final Type type = getFinalType(widget);
        if (type instanceof EntityReference) {
            final Option<PsiMetaModel<?>> databaseObject = resolveMetaModel(((EntityReference) type).getFullName());
            if (databaseObject.isPresent()) return ((PsiEntity) databaseObject.get());
        }
        return null;
    }

    @Nullable private PsiEnum psiEnumFromWidget(PsiWidget widget) {
        final Type type = getFinalType(widget);
        if (type != null && type.isEnum()) {
            final EnumType                enumType  = (EnumType) type;
            final Option<PsiMetaModel<?>> metaModel = resolveMetaModel(enumType.getFullName());
            if (metaModel.isPresent()) return ((PsiEnum) metaModel.get());
        }
        return null;
    }

    @Nullable private Type getFinalType(PsiWidget widget) {
        return widget.getWidget().map(Typed::getFinalType).getOrNull();
    }

    //~ Static Fields ................................................................................................................................

    private static final PsiEntityField[] EMPTY_ARRAY = new PsiEntityField[0];
}  // end class PsiFilterFieldRef
