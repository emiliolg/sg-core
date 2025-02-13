
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

import tekgenesis.metadata.authorization.Assignee;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Constants.NOT_FOUND;

/**
 * Utility class to deal with Assignees.
 */
public class Assignees {

    //~ Constructors .................................................................................................................................

    private Assignees() {}

    //~ Methods ......................................................................................................................................

    /** Return Assignee from string representation: u:admin, r:developer. */
    @NotNull public static Assignee fromString(@NotNull final String a) {
        final String id = a.substring(USER_PREFIX.length());
        return ensureNotNull(a.startsWith(USER_PREFIX) ? User.find(id) : a.startsWith(ROLE_PREFIX) ? Role.find(id) : null,
            "User or Role '" + a + NOT_FOUND);
    }

    static String asString(@NotNull final User user) {
        return USER_PREFIX + user.getId();
    }

    static String asString(@NotNull final Role role) {
        return ROLE_PREFIX + role.getId();
    }

    //~ Static Fields ................................................................................................................................

    private static final String USER_PREFIX = "u:";
    private static final String ROLE_PREFIX = "r:";
}
