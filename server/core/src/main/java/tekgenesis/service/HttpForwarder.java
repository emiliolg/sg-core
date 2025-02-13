
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

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;

/**
 * Http forwarder.
 */
public class HttpForwarder implements Forwarder {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ServletRequest  req;
    @NotNull private final ServletResponse resp;

    //~ Constructors .................................................................................................................................

    /** Http forwarder constructor. */
    public HttpForwarder(@NotNull HttpServletRequest req, @NotNull ServletResponse resp) {
        this.req  = req;
        this.resp = resp;
    }

    //~ Methods ......................................................................................................................................

    @Override public void forward(@NotNull String uri, @NotNull ResultHandler response, boolean routing) {
        try {
            req.getRequestDispatcher(uri).forward(req, resp);
        }
        catch (ServletException | IOException e) {
            logger.error(e);
        }
    }

    @Override public String url(@NotNull String uri) {
        return RequestForwarder.url(req.getScheme(), req.getServerName(), String.valueOf(req.getServerPort()), uri);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(HttpForwarder.class);
}  // end class HttpForwarder
