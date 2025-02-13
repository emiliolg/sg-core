
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.media.MediaType;
import tekgenesis.common.media.Mime;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.cookie.MutableCookie;

/**
 * Service result.
 */
public interface Result<T> {

    //~ Methods ......................................................................................................................................

    /**
     * Add outbound cache headers with specified expiration in days, including
     * {@link #CACHE_CONTROL}, {@link #LAST_MODIFIED}, {@link #EXPIRES}.
     */
    @NotNull Result<T> withCache(int days);

    /**
     * Add outbound cache headers with specified expiration, including {@link #CACHE_CONTROL},
     * {@link #LAST_MODIFIED}, {@link #EXPIRES}.
     */
    @NotNull Result<T> withCache(int duration, TimeUnit unit);

    /** Set the content-type media type. */
    @NotNull Result<T> withContentType(@NotNull MediaType mime);

    /** Set the content-type media type. */
    @NotNull Result<T> withContentType(@NotNull Mime mime, @NotNull Charset charset);

    /** Set cookie on response. Returns {@link MutableCookie} for customization. */
    @NotNull MutableCookie withCookie(@NotNull String name, @NotNull String value);

    /** Add header value associated with given header name. */
    @NotNull Result<T> withHeader(String name, String value);

    /** Return result headers. */
    @NotNull Headers getHeaders();

    /** Return result status. */
    @NotNull Status getStatus();
}
