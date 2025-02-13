
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;

/**
 * Admin session utils.
 */
public class SessionUtils {

    //~ Constructors .................................................................................................................................

    private SessionUtils() {}

    //~ Methods ......................................................................................................................................

    /** Returns Session DAO. */
    public static SessionDAO getSessionDAO() {
        final SessionManager sessionManager = ((DefaultSecurityManager) SecurityUtils.getSecurityManager()).getSessionManager();
        return ((DefaultSessionManager) sessionManager).getSessionDAO();
    }
}
