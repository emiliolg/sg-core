
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.g.RoleAssignmentBase;
import tekgenesis.metadata.authorization.User;

import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * User class for Entity: RoleAssignment
 */
@SuppressWarnings("WeakerAccess")
public class RoleAssignment extends RoleAssignmentBase implements tekgenesis.metadata.authorization.RoleAssignment {

    //~ Methods ......................................................................................................................................

    /** Checks if a Role is assigned to a User. */
    public static boolean includes(@NotNull final Role role, @NotNull final User user) {
        return selectFrom(ROLE_ASSIGNMENT).where(ROLE_ASSIGNMENT.USER_ID.eq(user.getId()), ROLE_ASSIGNMENT.ROLE_ID.eq(role.getId())).exists();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3618445576949019290L;
}
