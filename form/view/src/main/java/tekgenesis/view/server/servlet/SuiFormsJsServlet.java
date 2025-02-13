
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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tekgenesis.common.env.Version;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.WebResourceManager;

import static tekgenesis.common.env.security.SecurityUtils.getSession;
import static tekgenesis.common.media.Mime.APPLICATION_JAVASCRIPT;
import static tekgenesis.security.shiro.web.URLConstants.*;

/**
 * Servlet that writes the suigeneris forms bootstrap javascript.
 */
public class SuiFormsJsServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType(APPLICATION_JAVASCRIPT.getMime());

        final PrintWriter writer = resp.getWriter();
        writer.print(formsJs(req));
        writer.print("\n");
        writer.print(resource("/public/sg/js/sg-forms-jquery.js"));
        writer.print("\n");
        writer.print(resource("/public/sg/js/sg-forms-angular.js"));
        writer.print("\n");
    }

    private String formsJs(HttpServletRequest req) {
        final String userId = getSession().isAuthenticated() ? getSession().getPrincipal().getId() : "";

        // set theme sha as an attribute, defaulting to the tekgenesis theme if missing
        String themeSha = sha(DEFAULT_THEME);
        if (DEFAULT_THEME.equals(themeSha)) themeSha = sha(DEFAULT_TEKGENESIS_THEME);

        return "/**\n" +
               " * Forms bootstrap js for Suigeneris\n" +
               " * Versions: " + Version.getInstance().toString() + "\n" +
               " */\n" +
               "(function () {\n" +
               "\n" +
               "    window.__gwt_Locale = '" + Context.getContext().getLocale() + "';\n" +
               "    window.__gwt_UserId = '" + userId + "';\n" +
               "\n" +
               "    function fetch(type, files) {\n" +
               "        for (var i = 0; i < files.length; i++) {\n" +
               "            var e = document.createElement(type);\n" +
               "            for (var p in files[i]) if (files[i].hasOwnProperty(p)) e[p] = files[i][p];\n" +
               "            document.getElementsByTagName('head')[0].appendChild(e);\n" +
               "        }\n" +
               "    }\n" +
               "\n" +
               "    fetch('link', [\n" +
               "        {rel: 'stylesheet', href: '" + themeSha + "', id: 'theme'},\n" +
               "        {rel: 'stylesheet', href: '" + sha(SUIGENERIS_CSS) + "'},\n" +
               "        {rel: 'stylesheet', href: '" + sha(FORMS_CSS) + "'}\n" +
               "    ]);\n" +
               "\n" +
               "    fetch('script', [{src: '/sgforms/sgforms.nocache.js'}]);\n" +
               "\n" +
               "})();";
    }

    private String resource(String uri) {
        final byte[] bytes = WebResourceManager.getInstance().readWebResource(uri);
        return bytes != null ? new String(bytes) : "";
    }

    private String sha(String path) {
        return WebResourceManager.getInstance().shaPath(path);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6422211503408730776L;
}  // end class SuiFormsJsServlet
