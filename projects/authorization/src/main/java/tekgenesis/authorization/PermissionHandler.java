
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

import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.authorization.shiro.AuthorizationUtils.hasPermission;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.type.permission.Permission.Custom.valueOf;

/**
 * User class for Handler: PermissionHandler
 */
public class PermissionHandler extends PermissionHandlerBase {

    //~ Constructors .................................................................................................................................

    PermissionHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Boolean> check(@NotNull String fqn, @NotNull String permission) {
        return ok(hasPermission(createQName(fqn), valueOf(permission)));
    }
}
