
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.SocialProfile;
import tekgenesis.authorization.User;
import tekgenesis.common.env.context.Context;

/**
 * This interface is the core class of the SuiGeneris Social module. It represents an authentication
 * mechanism to validate user's credentials and retrieve his user profile.
 */
public interface SocialProvider<C extends SocialCredential, P extends Profile> {

    //~ Methods ......................................................................................................................................

    /** Redirect to the authentication provider for a social client. */
    String redirect(@NotNull final WebContext context);

    /** Useful method user field population, and / or related entities creation. */
    default void userCreated(@NotNull final P profile, @NotNull final User user) {
        SocialProfile.create(profile.getProvider(), profile.getId()).setUser(user).insert();
    }

    /** Get social provider callback manager. */
    @NotNull CallbackManager getCallbackManager();

    /** Get the credentials from the web context. */
    C getCredentials(@NotNull final WebContext context);

    /** Get social provider login manager. */
    @NotNull LoginManager getLoginManager();

    /** Get social provider name. */
    String getName();

    /** Return provider associated properties. */
    @NotNull default SocialProviderProps getSocialProviderProps() {
        return Context.getEnvironment().get(getName(), SocialProviderProps.class);
    }

    /** Get the user profile based on the provided credentials. */
    P getUserProfile(@NotNull final C credentials, @NotNull final WebContext context);
}
