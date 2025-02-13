
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

import java.util.function.Consumer;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.subject.PrincipalCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.User;

/**
 * SuigenAuthorizer.
 */
@SuppressWarnings("EmptyMethod")
public interface SuiGenerisAuthorizer extends Authorizer {

    //~ Methods ......................................................................................................................................

    /** Create user. */
    User findOrCreateUser(@NotNull final String username, @Nullable Consumer<User> populator);

    /** Called on logout. */
    void onLogout(PrincipalCollection principals);
}
