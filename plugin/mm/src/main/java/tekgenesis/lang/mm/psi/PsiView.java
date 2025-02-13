
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

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.metadata.entity.View;
import tekgenesis.type.MetaModelKind;

/**
 * View Psi.
 */
public class PsiView extends PsiDatabaseObject<View> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiView(MMElementType t) {
        super(t, MetaModelKind.VIEW, View.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public Icon getIcon(int flags) {
        return MMFileType.VIEW_ICON;
    }

    PsiEntityField getBaseEntityField(String fieldName) {
        for (final PsiEntityField viewField : getBaseEntityFields()) {
            if (fieldName.equals(viewField.getName())) return viewField;
        }
        return null;
    }

    PsiEntityField[] getBaseEntityFields() {
        final View view = getModel().getOrNull();
        if (view != null && view.getBaseEntity().isPresent()) {
            final Option<PsiEntity> mm = resolveMetaModel(view.getBaseEntity().get().getFullName()).castTo(PsiEntity.class);
            if (mm.isPresent()) return mm.get().getFields();
        }
        return EMPTY_VIEW_FIELDS;
    }

    //~ Static Fields ................................................................................................................................

    private static final PsiEntityField[] EMPTY_VIEW_FIELDS = {};
}  // end class PsiView
