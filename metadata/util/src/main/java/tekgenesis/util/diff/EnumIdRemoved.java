
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
 * A {@link Change} that indicates that an {@link EnumType} id has been removed.
 */
public class EnumIdRemoved implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final EnumType anEnum;
    private final String   id;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the {@link EnumType} and the removed id */
    public EnumIdRemoved(EnumType anEnum, String id) {
        this.anEnum = anEnum;
        this.id     = id;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return "Id " + id + " has been removed from enum " + anEnum.getFullName();
    }
}
