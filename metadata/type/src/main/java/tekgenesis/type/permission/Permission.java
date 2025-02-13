
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.permission;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.hashCodeAll;
import static tekgenesis.type.permission.PredefinedPermission.isPredefined;

/**
 * Permission interface. Predefined Permissions are the ones in {@link PredefinedPermission}. To
 * create custom ones use {@link Custom#valueOf(String)}.
 */
public interface Permission extends Serializable {

    //~ Instance Fields ..............................................................................................................................

    long serialVersionUID = -23048472516273172L;

    //~ Methods ......................................................................................................................................

    /** The name of the permission. */
    String name();

    /** The name of the permission. */
    default String getName() {
        return name().toLowerCase();
    }

    /** Returns true or false whether this permission is default or not. */
    default boolean isDefault() {
        return false;
    }

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("FieldMayBeFinal")  // GWT serialization
    class Custom implements Permission {
        private String permissionName;

        Custom() {
            permissionName = null;
        }  // GWT serialization

        /** Default custom implementation of a Permission. */
        private Custom(String permissionName) {
            this.permissionName = permissionName;
        }

        @Override public boolean equals(Object o) {
            return o instanceof Custom && equal(((Custom) o).permissionName, permissionName);
        }

        @Override public int hashCode() {
            return hashCodeAll(permissionName);
        }

        @Override public String name() {
            return permissionName;
        }

        @Override public String toString() {
            return getName();
        }

        @Override public String getName() {
            return permissionName;
        }

        /** Returns the PredefinedPermission that matches permissionName or a Custom one. */
        public static Permission valueOf(@NotNull final String permissionName) {
            return isPredefined(permissionName) ? PredefinedPermission.valueOf(permissionName.toUpperCase()) : new Permission.Custom(permissionName);
        }

        private static final long serialVersionUID = -87793072516273172L;
    }
}  // end interface Permission
