
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.authorization;

import org.jetbrains.annotations.NotNull;

/**
 * Role assignment interface.
 */
public interface RoleAssignment {

    //~ Methods ......................................................................................................................................

    /** Return role assignment organizational unit. */
    @NotNull OrganizationalUnit getOu();

    /** Return role assignment role. */
    @NotNull Role getRole();
}
