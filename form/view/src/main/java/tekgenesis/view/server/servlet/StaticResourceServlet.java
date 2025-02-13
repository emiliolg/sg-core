
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.media.Mimes;
import tekgenesis.form.WebResourceManager;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.util.Files.copy;
import static tekgenesis.view.server.servlet.Servlets.HALF_YEAR;
import static tekgenesis.view.server.servlet.Servlets.SHA_SERVLET_PATH;
import static tekgenesis.view.server.servlet.Servlets.addCacheHeaders;

/**
 * Serves static resources. Supports: /img/myFile.png for and image. /sha/XXX to return a resource
 * using its sha. /img/?enum=MyEnum&name=NAME to load the image of an enum.
 */
public class StaticResourceServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        @Nullable final InputStream resource;
        final String                resourceName;
        final String                path = req.getRequestURI();
        if (path.startsWith(SHA_SERVLET_PATH)) {
            // url includes sha
            addCacheHeaders(resp, HALF_YEAR);

            // obtain resource with sha
            resource     = WebResourceManager.getInstance()
                           .readShaResource(path.substring(path.lastIndexOf("/") + 1),
                        path.substring(SHA_SERVLET_PATH.length(), path.lastIndexOf("/")));
            resourceName = path.substring(0, path.lastIndexOf("/"));
        }
        else {
            resourceName = resolvePath(req, resp);

            final byte[] bytes = WebResourceManager.getInstance().readWebResource(resourceName);
            resource = bytes == null ? null : new ByteArrayInputStream(bytes);
        }

        if (resource == null) resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        else {
            final String contentType = Mimes.getMimeType(resourceName);
            resp.setContentType(contentType);
            copy(resource, resp.getOutputStream());
        }
    }

    private String resolvePath(HttpServletRequest req, HttpServletResponse resp) {
        final String servletPath = req.getServletPath();
        final String path        = req.getPathInfo();

        // direct path to a user img, css or js
        addCacheHeaders(resp, 1);

        // try to resolve enum resource
        final String enumClass = req.getParameter(ENUM_PARAM);
        final String enumName  = req.getParameter(ENUM_NAME_PARAM);
        if (isNotEmpty(enumClass) && isNotEmpty(enumName)) {
            final Enumeration<?, ?> e = Enumerations.valueOf(enumClass, enumName);
            return notNull(e.imagePath(), "img/" + enumName + ".png");
        }

        // direct path to resource
        return servletPath + path;
    }

    //~ Static Fields ................................................................................................................................

    private static final String ENUM_PARAM      = "enum";
    private static final String ENUM_NAME_PARAM = "name";

    private static final long serialVersionUID = -4845744325932219068L;
}  // end class StaticResourceServlet
