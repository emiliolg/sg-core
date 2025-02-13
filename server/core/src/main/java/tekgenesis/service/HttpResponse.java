
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.cookie.Cookies;
import tekgenesis.common.service.cookie.MutableCookie;
import tekgenesis.common.service.server.Response;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.map;

/**
 * Http servlet response extendind service response.
 */
public class HttpResponse implements Response {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<MutableCookie> cookies;
    @NotNull private final Headers             headers;
    @NotNull private final HttpServletResponse resp;

    //~ Constructors .................................................................................................................................

    /** Response constructor receiving servlet response. */
    public HttpResponse(@NotNull final HttpServletResponse resp) {
        this.resp = resp;
        headers   = new Headers();
        cookies   = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public MutableCookie withCookie(@NotNull String name, @NotNull String value) {
        final MutableCookie cookie = Cookies.create(name, value);
        cookies.add(cookie);
        return cookie;
    }

    @Override public OutputStream getContent()
        throws IOException
    {
        writeHeaders();
        return resp.getOutputStream();
    }

    @NotNull @Override public Headers getHeaders() {
        return headers;
    }

    @NotNull @Override public Status getStatus() {
        return Status.fromCode(resp.getStatus());
    }

    @Override public void setStatus(@NotNull Status status) {
        resp.setStatus(status.code());
    }

    /** Return raw response. */
    @NotNull HttpServletResponse raw() {
        return resp;
    }

    /**
     * Write response headers, must be called before writing content or explicitly if no content is
     * to be written.
     */
    void writeHeaders() {
        map(cookies, this::adapt).forEach(resp::addCookie);
        for (final Map.Entry<String, Collection<String>> entry : headers.asMap().entrySet()) {
            final String name = entry.getKey();
            for (final String value : entry.getValue())
                resp.addHeader(name, value);
        }
    }

    private Cookie adapt(@NotNull MutableCookie c) {
        final Cookie result = new Cookie(c.getName(), c.getValue());
        result.setDomain(notEmpty(c.getDomain(), ""));
        result.setPath(c.getPath());
        result.setMaxAge((int) c.getMaxAge());
        result.setHttpOnly(c.isHttpOnly());
        result.setSecure(c.isSecure());
        result.setVersion(1);
        return result;
    }
}  // end class HttpResponse
