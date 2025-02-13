
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.lang.ASTFactory;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;

/**
 * AST Factory for MM.
 */
@SuppressWarnings("WeakerAccess")
public class MMASTFactory extends ASTFactory {

    //~ Methods ......................................................................................................................................

    @Nullable
    @SuppressWarnings({ "OverlyComplexMethod", "OverlyCoupledMethod", "OverlyLongMethod", "MethodWithMultipleReturnPoints" })
    public MMCommonComposite createComposite(final IElementType t) {
        if (!(t instanceof MMElementType)) return null;

        final MMElementType type = (MMElementType) t;
        switch (type.getTokenType()) {
        case FIELD_REF:
            return new PsiFieldReference(type);
        case ENTITY:
            return new PsiEntity(type);
        case ENUM:
            return new PsiEnum(type);
        case ENUM_FIELD:
            return new PsiEnumField(type);
        case FIELD:
            return new PsiEntityField(type);
        case WIDGET_FIELD:
            return new PsiWidget(type);
        case ROUTE:
            return new PsiHandlerField(type);
        case TYPE:
            return new PsiType(type);
        case VIEW:
            return new PsiView(type);
        case HANDLER:
            return new PsiHandler(type);
        case LINK:
            return new PsiLink(type);
        case CASE:
            return new PsiCase(type);
        case REFERENCE:
            return new PsiMetaModelCodeReferenceElementImpl(type);
        case CLASS:
            return new PsiClassReference(type);
        case METHOD_REF:
            return new PsiMethodReference(type);
        case ENUM_VALUE:
            return new PsiEnumConstantReference(type);
        case PACKAGE:
            return new PsiDomain(type);
        case IMPORT:
            return new PsiImport(type);
        case TYPE_REF:
            return new PsiTypeReference(type);
        case ENUM_FIELD_REF:
            return new PsiEnumFieldReference(type);
        case FILTER_REF:
            return new PsiFilterFieldRef(type);
        case PATH:
            return new PsiPath(type);
        case INTERPOLATION:
            return new PsiInterpolation(type);
        case PRIMARY_KEY:
            return new PrimaryKey(type);
        case FORM:
            return new PsiForm(type);
        case WIDGET:
            return new PsiWidgetDef(type);
        case MENU:
            return new PsiMenu(type);
        case TASK:
            return new PsiTask(type);
        case MENU_ELEMENT:
            return new PsiMenuField(type);
        case ROLE:
            return new PsiRole(type);
        case ROLE_ELEMENT:
            return new PsiRoleField(type);
        case ROLE_PERMISSION:
            return new PsiRolePermission(type);
        case OPTION:
            return new PsiFieldOption(type);
        case WIDGET_TYPE:
            return new PsiWidgetType(type);
        case INDEX:
        case UNIQUE:
            return new PsiIndex(type);
        default:
            return new MMCommonComposite(type);
        }
    }  // end method createComposite

    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public MMLeafElement createLeaf(@NotNull final IElementType t, @NotNull CharSequence text) {
        if (!(t instanceof MMElementType)) return null;
        final MMElementType type = (MMElementType) t;
        switch (type.getTokenType().getKind()) {
        case WHITE_SPACE:
            return new MMLeafElement(type, text);
        case IDENTIFIER:
            return new MMIdentifier(text);
        case COMMENT:
            return new MMComment(type, text);
        case KEYWORD:
            return new MMKeyword(type, text);

        default:
            return new MMLeafElement(type, text);
        }
    }
}  // end class MMASTFactory
