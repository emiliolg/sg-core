
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;

/**
 * User interface.
 */
public interface User extends Assignee {

    //~ Methods ......................................................................................................................................

    /** Updates user property. */
    void updateProperty(@Nullable Property p);

    /** Return user's creation time. */
    @NotNull DateTime getCreationTime();

    /** Return user's creation user. */
    @Nullable String getCreationUser();

    /** Return user's organizational unit. */
    @Nullable OrganizationalUnit getDefaultOu();

    /** Return user's email. */
    @Nullable String getEmail();

    /** Returns the enabled devices of this User. */
    @NotNull Iterable<Device> getEnabledDevices();

    /** Return user's last login time. */
    @Nullable DateTime getLastLogin();

    /** Returns user properties. */
    @NotNull Iterable<Property> getProperties();

    /** Returns property or 'null' if the property doesn't exist. */
    @Nullable Property getProperty(String key);

    /** Return user role assignments for given organizational unit and its ancestors. */
    @NotNull Seq<RoleAssignment> getRoleAssignmentsForOrganization(@NotNull final OrganizationalUnit org);

    /** Return user roles for current organizational unit and its ancestors. */
    @NotNull Seq<Role> getRoles();

    /** Return user roles for given organizational unit and its ancestors. */
    @NotNull Seq<Role> getRolesForOrganization(@NotNull final OrganizationalUnit org);

    /** Return user's last update time. */
    @NotNull DateTime getUpdateTime();

    /** Return user's last update user. */
    @Nullable String getUpdateUser();
}  // end interface User
