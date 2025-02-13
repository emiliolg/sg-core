
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
import tekgenesis.metadata.role.Role;
import tekgenesis.type.MetaModelKind;

/**
 * Psi Role.
 */
public class PsiRole extends PsiMetaModel<Role> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiRole(MMElementType t) {
        super(t, MetaModelKind.ROLE, Role.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiRoleField getFieldNullable(String fieldName) {
        return getPsiModelField(fieldName, getFields());
    }

    @NotNull @Override public PsiRoleField[] getFields() {
        return getPsiModelFields(MMElementType.LIST, MMElementType.ROLE_ELEMENT, PsiRoleField[]::new, EMPTY_ROLE_MODEL_FIELDS);
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.ROLE_ICON;
    }

    //~ Static Fields ................................................................................................................................

    private static final PsiRoleField[] EMPTY_ROLE_MODEL_FIELDS = {};
}
