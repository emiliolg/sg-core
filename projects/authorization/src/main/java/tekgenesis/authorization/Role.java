
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

import tekgenesis.authorization.g.RoleBase;
import tekgenesis.metadata.authorization.User;

/**
 * User class for Entity: Role
 */
@SuppressWarnings("WeakerAccess")
public class Role extends RoleBase implements tekgenesis.metadata.authorization.Role {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String asString() {
        return Assignees.asString(this);
    }

    @Override public boolean includes(@NotNull User user) {
        return RoleAssignment.includes(this, user);
    }

    @NotNull @Override public String getImage() {
        return "/sg/img/roles.jpg";
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -9029540892727988192L;

    //~ Inner Classes ................................................................................................................................

    public static final class Data extends OpenData {
        private static final long serialVersionUID = 0L;
    }
}
