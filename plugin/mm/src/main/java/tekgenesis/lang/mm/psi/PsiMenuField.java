
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

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;

/**
 * A Menu Field.
 */
public class PsiMenuField extends PsiModelField {

    //~ Constructors .................................................................................................................................

    /** Create a menu field. */
    PsiMenuField(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public Icon getIcon(int flags) {
        return MMFileType.ATTRIBUTE_ICON;
    }
}  // end class PsiMenuField
