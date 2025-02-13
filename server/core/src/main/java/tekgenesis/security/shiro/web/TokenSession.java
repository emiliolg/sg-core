
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro.web;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 * Serializable session for client side authentication.
 */
public class TokenSession implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private Map<Object, Object> attributes = new HashMap<>();
    private String              host       = null;

    private String id             = null;
    private Date   lastAccessTime = null;
    private Date   startTimestamp = null;
    private Date   stopTimestamp  = null;
    private long   timeout;

    //~ Constructors .................................................................................................................................

    TokenSession() {}
    /** Constructor from session. */
    TokenSession(Session session) {
        host           = session.getHost();
        startTimestamp = session.getStartTimestamp();
        lastAccessTime = session.getLastAccessTime();
        id             = session.getId() != null ? session.getId().toString() : null;
        timeout        = session.getTimeout();
        for (final Object key : session.getAttributeKeys())
            attributes.put(key, session.getAttribute(key));
    }

    //~ Methods ......................................................................................................................................

    /** Return SimpleSession. */
    public SimpleSession asSession() {
        final SimpleSession simpleSession = new SimpleSession(host);
        simpleSession.setAttributes(attributes);
        simpleSession.setStartTimestamp(startTimestamp);
        simpleSession.setLastAccessTime(lastAccessTime);
        simpleSession.setId(id);
        simpleSession.setTimeout(timeout);
        simpleSession.setExpired(new Date().getTime() - lastAccessTime.getTime() > timeout);
        return simpleSession;
    }

    /** Return attributes map. */
    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    /** Set attributes map. */
    public void setAttributes(Map<Object, Object> attributes) {
        this.attributes = attributes;
    }

    /** Return host. */
    public String getHost() {
        return host;
    }

    /** Set host. */
    public void setHost(String host) {
        this.host = host;
    }

    /** Return session id. */
    public Serializable getId() {
        return id;
    }

    /** Set session id. */
    public void setId(String id) {
        this.id = id;
    }

    /** Get last access time. */
    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    /** Set last access time. */
    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    /** Get start timestamp. */
    public Date getStartTimestamp() {
        return startTimestamp;
    }

    /** Set start timestamp. */
    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /** Get stop time. */
    public Date getStopTimestamp() {
        return stopTimestamp;
    }

    /** Set stop time. */
    public void setStopTimestamp(Date stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    /** Get timeout in milliseconds. */
    public long getTimeout() {
        return timeout;
    }

    /** Set timeout in milliseconds. */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2760957500914387871L;
}  // end class TokenSession
