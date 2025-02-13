
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import java.util.Set;

import org.jetbrains.annotations.NonNls;

/**
 * DuplicateRolePermissionsException.
 */
public class DuplicateRolePermissionsException extends BuilderException {

    //~ Instance Fields ..............................................................................................................................

    private final Set<String> duplicates;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public DuplicateRolePermissionsException(Set<String> duplicates, String fqn) {
        super(PERMISSION_ALREADY_DEFINED, fqn);
        this.duplicates = duplicates;
    }

    //~ Methods ......................................................................................................................................

    /** Return duplicate permissions. */
    public Set<String> getDuplicates() {
        return duplicates;
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String PERMISSION_ALREADY_DEFINED = "Permission already defined";

    private static final long serialVersionUID = 2019037274330072016L;
}
