
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
import tekgenesis.metadata.handler.Handler;
import tekgenesis.type.MetaModelKind;

/**
 * Psi Handler.
 */
public class PsiHandler extends PsiMetaModel<Handler> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiHandler(MMElementType t) {
        super(t, MetaModelKind.HANDLER, Handler.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public PsiModelField getFieldNullable(String fieldName) {
        return null;
    }

    @NotNull @Override public PsiHandlerField[] getFields() {
        return getPsiModelFields(MMElementType.LIST, MMElementType.ROUTE, PsiHandlerField[]::new, EMPTY_HANDLER_MODEL_FIELDS);
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.HANDLER_ICON;
    }

    //~ Static Fields ................................................................................................................................

    protected static final PsiHandlerField[] EMPTY_HANDLER_MODEL_FIELDS = {};
}
