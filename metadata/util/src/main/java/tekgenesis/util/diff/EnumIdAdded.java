
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.type.EnumType;

/**
 * /** A {@link Change} that indicates that new id has been added to an {@link EnumType}.
 */
public class EnumIdAdded implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final EnumType anEnum;
    private final String   id;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the {@link EnumType} and the new id */
    public EnumIdAdded(EnumType anEnum, String id) {
        this.anEnum = anEnum;
        this.id     = id;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return "Id " + id + " has been added to enum " + anEnum.getFullName();
    }
}
