
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.filter;

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.social.WebContext;
import tekgenesis.common.service.Headers;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.service.HeaderNames.HOST;
import static tekgenesis.common.service.HeaderNames.X_FORWARDED_HOST;
import static tekgenesis.common.service.HeaderNames.X_FORWARDED_PROTO;

/**
 * Web Context implementation.
 */
class WebContextImpl implements WebContext {

    //~ Instance Fields ..............................................................................................................................

    private final HttpServletRequest request;

    //~ Constructors .................................................................................................................................

    private WebContextImpl(HttpServletRequest request) {
        this.request = request;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Headers getHeaders() {
        throw unreachable();
    }

    /** Return request host (domain, optionally port). */
    @NotNull @Override public String getHost() {
        return notEmpty(request.getHeader(X_FORWARDED_HOST), request.getHeader(HOST));
    }

    public String getRequestParameter(@NotNull final String name) {
        return request.getParameter(name);
    }

    @NotNull @Override public String getScheme() {
        return notEmpty(request.getHeader(X_FORWARDED_PROTO), request.getScheme());
    }

    public String getServerName() {
        return request.getServerName();
    }

    public int getServerPort() {
        return request.getServerPort();
    }

    @NotNull @Override public String getUri() {
        return request.getRequestURI();
    }

    @NotNull @Override public String getUrl() {
        return request.getRequestURL().toString();
    }

    //~ Methods ......................................................................................................................................

    static WebContext create(HttpServletRequest request) {
        return new WebContextImpl(request);
    }
}  // end class WebContextImpl
