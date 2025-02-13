
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.permission;

import org.jetbrains.annotations.NotNull;

/**
 * Predefined Form Permissions.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public enum PredefinedPermission implements Permission {

    //~ Enum constants ...............................................................................................................................

    READ, QUERY, CREATE, UPDATE, DELETE, HANDLE_DEPRECATED;

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return getName();
    }

    @Override public String getName() {
        return name().toLowerCase();
    }

    @Override public boolean isDefault() {
        return true;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if the given permission name is within the predefined ones. */
    public static boolean isPredefined(@NotNull final String permissionName) {
        for (final PredefinedPermission predefined : values()) {
            if (predefined.getName().equalsIgnoreCase(permissionName)) return true;
        }
        return false;
    }
}
