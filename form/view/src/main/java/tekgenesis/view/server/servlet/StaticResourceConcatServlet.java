
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Strings;
import tekgenesis.common.media.Mimes;
import tekgenesis.form.WebResourceManager;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.util.Files.close;
import static tekgenesis.common.util.Files.copy;
import static tekgenesis.view.server.servlet.Servlets.HALF_YEAR;
import static tekgenesis.view.server.servlet.Servlets.SHA_SERVLET_PATH;
import static tekgenesis.view.server.servlet.Servlets.addCacheHeaders;

/**
 * Static resource concat servlet.
 */
public class StaticResourceConcatServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // url should includes sha
        addCacheHeaders(resp, HALF_YEAR);

        final String                q    = notNull(req.getQueryString());
        final ImmutableList<String> urls = Strings.split(q, '&');

        for (final String first : urls.getFirst()) {
            final String uri      = path(first);
            final String mimeType = Mimes.getMimeType(uri);
            resp.setContentType(mimeType);
        }

        final ServletOutputStream out = resp.getOutputStream();

        try {
            for (final String url : urls) {
                final String      sha      = resolveSha(url);
                final InputStream resource = WebResourceManager.getInstance()
                                             .readShaResource(sha, url.substring(SHA_SERVLET_PATH.length(), url.lastIndexOf("/")));
                if (resource != null) {
                    out.println("/* " + path(url) + " */");  // Valid comment for css and js :D
                    copy(resource, out, false);
                    out.println();
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
                }
            }
        }
        finally {
            try {
                close(out);
            }
            catch (final Exception e) {
                // ignore
            }
        }
    }                                                        // end method doGet

    private String path(String first) {
        return first.substring(0, first.lastIndexOf("/"));
    }

    private String resolveSha(String path) {
        if (!path.startsWith(SHA_SERVLET_PATH)) return path;

        final int shaSeparator = path.lastIndexOf("/");
        return path.substring(shaSeparator + 1);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3438670960166638685L;
}  // end class StaticResourceConcatServlet
