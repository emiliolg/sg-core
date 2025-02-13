
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.media.MediaType;
import tekgenesis.common.media.Mime;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.cookie.MutableCookie;
import tekgenesis.common.service.etl.MessageConverter;
import tekgenesis.common.service.server.Response;
import tekgenesis.type.Type;

import static tekgenesis.common.service.Status.OK;

abstract class ResultImpl<T> implements Result<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final Response response;
    @Nullable Type          resultType;
    @NotNull final Status   status;

    //~ Constructors .................................................................................................................................

    private ResultImpl(@NotNull Status status, @NotNull Response response) {
        this.status   = status;
        this.response = response;
        resultType    = null;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<T> withCache(int days) {
        getHeaders().withCache(days);
        return this;
    }

    @NotNull @Override public Result<T> withCache(int duration, TimeUnit unit) {
        getHeaders().withCache(duration, unit);
        return this;
    }

    @NotNull @Override public Result<T> withContentType(@NotNull MediaType mime) {
        response.withContentType(mime);
        return this;
    }

    @NotNull @Override public Result<T> withContentType(@NotNull Mime mime, @NotNull Charset charset) {
        response.withContentType(mime, charset);
        return this;
    }

    @NotNull @Override public MutableCookie withCookie(@NotNull String name, @NotNull String value) {
        return response.withCookie(name, value);
    }

    @NotNull @Override public Result<T> withHeader(String name, String value) {
        response.withHeader(name, value);
        return this;
    }

    @NotNull @Override public Headers getHeaders() {
        return response.getHeaders();
    }

    @NotNull @Override public Status getStatus() {
        return status;
    }

    @NotNull Result<T> withResultType(@NotNull Type type) {
        resultType = type;
        return this;
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Empty result.
     */
    static class EmptyResult extends ResultImpl<Object> {
        EmptyResult(@NotNull Status status, @NotNull Response response) {
            super(status, response);
        }
    }

    /**
     * Forward result.
     */
    static class ForwardResult extends ResultImpl<Object> {
        final boolean routing;
        final String  url;

        ForwardResult(@NotNull Response response, @NotNull String url, boolean routing) {
            super(OK, response);
            this.url     = url;
            this.routing = routing;
        }
    }

    /**
     * Object result.
     */
    static class ObjectResult extends ResultImpl<Object> {
        @NotNull final Object                    content;
        @NotNull final List<MessageConverter<?>> converters;

        ObjectResult(@NotNull Status status, @NotNull Response response, @NotNull List<MessageConverter<?>> converters, @NotNull Object content) {
            super(status, response);
            this.converters = converters;
            this.content    = content;
        }
    }

    /**
     * Redirect result.
     */
    static class RedirectResult extends ResultImpl<Object> {
        final String url;

        RedirectResult(@NotNull Status status, @NotNull Response response, @NotNull String url) {
            super(status, response);
            this.url = url;
        }
    }
}  // end class ResultImpl
