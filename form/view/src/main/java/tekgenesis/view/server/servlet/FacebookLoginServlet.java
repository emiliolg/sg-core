
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.authorization.shiro.social.FacebookToken;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.service.RequestForwarder.url;

/**
 * Facebook login servlet.
 */
public class FacebookLoginServlet extends HttpServlet {

    //~ Instance Fields ..............................................................................................................................

    private ShiroProps shiroProps = new ShiroProps();

    //~ Methods ......................................................................................................................................

    @Override public void init()
        throws ServletException
    {
        super.init();
        shiroProps = Context.getProperties(ShiroProps.class);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        final String        code          = request.getParameter("code");
        final FacebookToken facebookToken = FacebookToken.create()
                                            .withCode(code,
                shiroProps.facebookAppId,
                shiroProps.facebookAppSecret,
                request.getRequestURL().toString());
        try {
            SecurityUtils.getSubject().login(facebookToken);
        }
        catch (final Exception e) {
            logger.error(e);
        }

        final String url = notEmpty(request.getRequestURI().substring(SERVLET_URL.length()), "/");
        response.sendRedirect(url);
    }

    /** @see  HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {}

    //~ Methods ......................................................................................................................................

    /** Return Facebook redirect URL for login. */
    public static String getRedirectUri(HttpServletRequest req) {
        return url(req.getScheme(), req.getServerName(), String.valueOf(req.getServerPort()), SERVLET_URL);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger      = Logger.getLogger(FacebookLoginServlet.class);
    private static final String SERVLET_URL = "/sg/fb/login/";

    private static final long serialVersionUID = 1L;
}  // end class FacebookLoginServlet
