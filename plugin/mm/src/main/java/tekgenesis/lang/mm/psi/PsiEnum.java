
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModelKind;

/**
 * Enum Psi.
 */
public class PsiEnum extends PsiMetaModel<EnumType> {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiEnum(MMElementType t) {
        super(t, MetaModelKind.ENUM, EnumType.class);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public PsiEnumField getFieldNullable(String fieldName) {
        return getPsiModelField(fieldName, getFields());
    }

    @NotNull @Override public PsiEnumField[] getFields() {
        return getPsiModelFields(MMElementType.LIST, MMElementType.ENUM_FIELD, PsiEnumField[]::new, EMPTY_ENUM_MODEL_FIELDS);
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.ENUM_ICON;
    }

    @Nullable PsiEntityField getAttribute(String name) {
        return getPsiModelField(name, getAttributes());
    }

    PsiEntityField[] getAttributes() {
        return getPsiModelFields(MMElementType.WITH, MMElementType.ENTITY_FIELD, PsiEntityField[]::new, EMPTY_ENTITY_MODEL_FIELDS);
    }

    //~ Static Fields ................................................................................................................................

    private static final PsiEntityField[] EMPTY_ENTITY_MODEL_FIELDS = {};
    private static final PsiEnumField[]   EMPTY_ENUM_MODEL_FIELDS   = {};
}  // end class PsiEnum
