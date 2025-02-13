
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.g.PermissionsBase;

/**
 * User class for Entity: Permissions
 */
@SuppressWarnings("WeakerAccess")
public class Permissions extends PermissionsBase {

    //~ Methods ......................................................................................................................................

    /** Creates a Permission based on the app, domain and current permission String. */
    @NotNull public WildcardPermission createPermission(String application, String domain) {
        return new WildcardPermission(application + PERMISSION_SEPARATOR + domain + PERMISSION_SEPARATOR + getPermission(), false);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4738635192292963012L;
    /** Shiro permission separator. */
    private static final String PERMISSION_SEPARATOR = ":";
}
