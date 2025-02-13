
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.lesscss.deps.org.apache.commons.io.output.ByteArrayOutputStream;

import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.cookie.Cookies;
import tekgenesis.common.service.cookie.MutableCookie;
import tekgenesis.common.service.server.Response;

/**
 * Mock implementation of server Response.
 */
class MockServerResponse implements Response {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final OutputStream content;

    @NotNull private final List<MutableCookie> cookies;
    @NotNull private final Headers             headers;
    @NotNull private Status                    status;

    //~ Constructors .................................................................................................................................

    MockServerResponse() {
        this(new ByteArrayOutputStream());
    }

    MockServerResponse(@NotNull final OutputStream content) {
        this.content = content;
        cookies      = new ArrayList<>();
        status       = Status.OK;
        headers      = new Headers();
    }

    //~ Methods ......................................................................................................................................

    @Override public MutableCookie withCookie(@NotNull String name, @NotNull String value) {
        final MutableCookie result = Cookies.create(name, value);
        cookies.add(result);
        return result;
    }

    @NotNull @Override public OutputStream getContent()
        throws IOException
    {
        return content;
    }

    @NotNull @Override public Headers getHeaders() {
        return headers;
    }

    @NotNull @Override public Status getStatus() {
        return status;
    }

    @Override public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    @NotNull Iterable<MutableCookie> getCookies() {
        return cookies;
    }
}  // end class MockServerResponse
