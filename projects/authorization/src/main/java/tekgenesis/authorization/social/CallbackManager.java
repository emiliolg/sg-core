
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

/**
 * Manage social provider callbacks.
 */
public interface CallbackManager {

    //~ Instance Fields ..............................................................................................................................

    String SG_SOCIAL_CALLBACK = "/sg/social/callback/";

    //~ Methods ......................................................................................................................................

    /** Compute callback url given context. */
    @NotNull default String computeCallback(@NotNull final WebContext context) {
        return context.getScheme() + "://" + context.getHost() + SG_SOCIAL_CALLBACK + getProvider();
    }

    /** Get social provider callback path. May define url-pattern syntax. */
    @NotNull default String getCallbackPath() {
        return SG_SOCIAL_CALLBACK + getProvider();
    }

    /** Return social provider name. */
    String getProvider();
}
