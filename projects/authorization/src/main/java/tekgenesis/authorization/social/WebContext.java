
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.service.Headers;

/**
 * Social module web context.
 */
public interface WebContext {

    //~ Methods ......................................................................................................................................

    /** Return request headers. */
    @NotNull Headers getHeaders();

    /** Return host. */
    @NotNull String getHost();

    /** Return request parameter. */
    @Nullable String getRequestParameter(@NotNull final String parameter);

    /** Return scheme. */
    @NotNull String getScheme();

    /** Return request uri. */
    @NotNull String getUri();

    /** Return request url. */
    @NotNull String getUrl();
}
