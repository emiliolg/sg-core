
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
import tekgenesis.metadata.link.Link;
import tekgenesis.type.MetaModelKind;

/**
 * Psi Link.
 */
public class PsiLink extends PsiMetaModel<Link> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiLink(MMElementType t) {
        super(t, MetaModelKind.LINK, Link.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiModelField getFieldNullable(String fieldName) {
        return null;
    }

    @NotNull @Override public PsiHandlerField[] getFields() {
        return EMPTY_LINK_MODEL_FIELDS;
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.LINK_ICON;
    }

    //~ Static Fields ................................................................................................................................

    protected static final PsiHandlerField[] EMPTY_LINK_MODEL_FIELDS = {};
}
