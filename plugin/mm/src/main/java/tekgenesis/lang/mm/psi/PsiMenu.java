
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
import tekgenesis.metadata.menu.Menu;
import tekgenesis.type.MetaModelKind;

/**
 * Menu Psi.
 */
public class PsiMenu extends PsiMetaModel<Menu> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiMenu(MMElementType t) {
        super(t, MetaModelKind.MENU, Menu.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiMenuField getFieldNullable(String fieldName) {
        return getPsiModelField(fieldName, getFields());
    }

    @NotNull @Override public PsiMenuField[] getFields() {
        return getPsiModelFields(MMElementType.LIST, MMElementType.MENU_ELEMENT, PsiMenuField[]::new, EMPTY_MENU_MODEL_FIELDS);
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.MENU_ICON;
    }

    //~ Static Fields ................................................................................................................................

    private static final PsiMenuField[] EMPTY_MENU_MODEL_FIELDS = {};
}
