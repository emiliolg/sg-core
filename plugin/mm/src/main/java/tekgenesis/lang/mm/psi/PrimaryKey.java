
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import tekgenesis.lang.mm.MMElementType;

/**
 * An Identifier.
 */
class PrimaryKey extends ReferencesList {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PrimaryKey(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean isPhysical() {
        return false;
    }
}
