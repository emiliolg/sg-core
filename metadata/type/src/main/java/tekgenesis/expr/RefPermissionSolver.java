
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import org.jetbrains.annotations.NotNull;

/**
 * This Solves the Type for a Ref.
 */
public interface RefPermissionSolver {

    //~ Methods ......................................................................................................................................

    /** Checks if the referenced permission exists. */
    boolean hasPermission(@NotNull String permissionName);
}
