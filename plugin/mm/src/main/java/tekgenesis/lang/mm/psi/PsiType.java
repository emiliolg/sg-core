
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

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.metadata.entity.TypeDef;
import tekgenesis.type.MetaModelKind;

/**
 * Psi Type.
 */
public class PsiType extends PsiMetaModel<TypeDef> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiType(MMElementType t) {
        super(t, MetaModelKind.TYPE, TypeDef.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiEntityField getFieldNullable(String name) {
        return getPsiModelField(name, getFields());
    }

    @NotNull @Override public PsiEntityField[] getFields() {
        if (findPsiChildByType(MMElementType.LIST) != null)
            return getPsiModelFields(MMElementType.LIST,
                MMElementType.ENTITY_FIELD,
                PsiEntityField[]::new,
                PsiDatabaseObject.EMPTY_ENTITY_MODEL_FIELDS);
        else return getChildrenAsPsiElements(MMElementType.ENTITY_FIELD, PsiEntityField[]::new);
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.TYPE_ICON;
    }
}
