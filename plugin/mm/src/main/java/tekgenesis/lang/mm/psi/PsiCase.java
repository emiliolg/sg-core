
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
import tekgenesis.metadata.workflow.Case;
import tekgenesis.type.MetaModelKind;

/**
 * Psi Type.
 */
public class PsiCase extends PsiMetaModel<Case> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiCase(MMElementType t) {
        super(t, MetaModelKind.CASE, Case.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiModelField getFieldNullable(String fieldName) {
        return null;
    }

    @NotNull @Override public PsiModelField[] getFields() {
        return EMPTY_MODEL_FIELDS;
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.CASE_ICON;
    }
}
