
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import java.io.Serializable;
import java.util.Map;

/**
 * Serializable session data.
 */
public class SessionData implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private Map<String, Object> attributes = null;
    private String              createdAt  = null;
    private String              host       = null;

    //~ Constructors .................................................................................................................................

    protected SessionData() {}
    /** Public constructor. */
    public SessionData(String createdAt, String host, Map<String, Object> attributes) {
        this.host       = host;
        this.createdAt  = createdAt;
        this.attributes = attributes;
    }

    //~ Methods ......................................................................................................................................

    /** Return session attributes. */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /** Return session creation time. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** Return session host. */
    public String getHost() {
        return host;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8202679016698697973L;
}
