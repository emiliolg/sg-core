
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroFilter;

import tekgenesis.common.logging.Logger;

/**
 * Tuned ShiroFilter that supports 'not touchy' paths.
 */
public class TouchlessShiroFilter extends ShiroFilter {

    //~ Methods ......................................................................................................................................

    @Override protected void updateSessionLastAccessTime(ServletRequest request, ServletResponse response) {
        if (!isHttpSessions()) {  // 'native' sessions
            final Subject subject = SecurityUtils.getSubject();
            // Subject should never _ever_ be null, but just in case:
            if (subject != null) {
                final Session session = subject.getSession(false);
                if (session != null) {
                    try {
                        if (requestMustTouchSession(request)) session.touch();
                    }
                    catch (final Throwable t) {
                        log.error(
                            "session.touch() method invocation has failed.  Unable to update" +
                            "the corresponding session's last access time based on the incoming request.",
                            t);
                    }
                }
            }
        }
    }

    private boolean requestMustTouchSession(ServletRequest request) {
        return request instanceof HttpServletRequest && !SOCKET_SERVICE_PATH.equals(((HttpServletRequest) request).getServletPath());
    }

    //~ Static Fields ................................................................................................................................

    // This path must match with the one on the web.xml file.
    private static final String SOCKET_SERVICE_PATH = "/sgforms/socket";

    private static final Logger log = Logger.getLogger(TouchlessShiroFilter.class);
}
