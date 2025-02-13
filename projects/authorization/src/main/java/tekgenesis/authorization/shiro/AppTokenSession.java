
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 * SimpleSession implementation for AppToken authentication.
 */
public class AppTokenSession extends SimpleSession {

    //~ Instance Fields ..............................................................................................................................

    private final ThreadLocal<HashMap<Object, Object>> attrs = ThreadLocal.withInitial(HashMap::new);

    //~ Methods ......................................................................................................................................

    @Override public Object removeAttribute(Object key) {
        return attrs.get().remove(key);
    }

    @Override public Object getAttribute(Object key) {
        return attrs.get().get(key);
    }

    @Override public void setAttribute(Object key, Object value) {
        attrs.get().put(key, value);
    }

    @Override public Collection<Object> getAttributeKeys()
        throws InvalidSessionException
    {
        return attrs.get().keySet();
    }

    @Override public Map<Object, Object> getAttributes() {
        return attrs.get();
    }

    @Override public void setAttributes(Map<Object, Object> attributes) {
        attrs.get().putAll(attributes);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5014184941895208715L;
}
