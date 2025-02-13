
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notNull;

/**
 * Shiro Utils. See {@link tekgenesis.common.env.security.SecurityUtils}
 */
public class ShiroUtils {

    //~ Constructors .................................................................................................................................

    private ShiroUtils() {}

    //~ Methods ......................................................................................................................................

    /** Return current user id or "". */
    public static String currentUserId() {
        final Subject subject   = SecurityUtils.getSubject();
        final Object  principal = subject.getPrincipal();
        return principal != null ? principal.toString() : "";
    }

    /** Return current user name or user id or "". */
    public static String currentUserName() {
        String                    name       = "";
        final Subject             subject    = SecurityUtils.getSubject();
        final PrincipalCollection principals = subject.getPrincipals();
        if (principals != null) {
            final Collection<?> fromRealm = principals.fromRealm(ShiroConfiguration.SG_REALM);
            if (!fromRealm.isEmpty()) name = fromRealm.iterator().next().toString();
        }
        return isEmpty(name) ? notNull(subject.getPrincipal(), "").toString() : name;
    }
}
