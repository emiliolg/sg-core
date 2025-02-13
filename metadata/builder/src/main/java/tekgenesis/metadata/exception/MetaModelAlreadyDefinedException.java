
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.common.core.QName;

/**
 * MetaModelAlreadyDefinedException.
 */
public class MetaModelAlreadyDefinedException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public MetaModelAlreadyDefinedException(String fqn) {
        super("Role already defines permissions for " + QName.extractName(fqn), fqn);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2019037284330072016L;
}
