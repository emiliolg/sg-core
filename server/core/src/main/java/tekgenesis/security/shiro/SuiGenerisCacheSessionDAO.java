
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro;

import java.io.Serializable;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import tekgenesis.authorization.shiro.AppTokenSession;
import tekgenesis.common.util.LruCache;

/**
 * EnterpriseCacheSessionDAO implementation to avoid caching session for app tokens.
 */
public class SuiGenerisCacheSessionDAO extends EnterpriseCacheSessionDAO {

    //~ Instance Fields ..............................................................................................................................

    private final LruCache<Serializable, Session> tokenSessions = LruCache.createLruCache(MAX_SIZE);

    //~ Methods ......................................................................................................................................

    @Override protected void cache(Session session, Serializable sessionId, Cache<Serializable, Session> cache) {
        if (shouldCache(session)) super.cache(session, sessionId, cache);
        else tokenSessions.put(sessionId, session);
    }

    @Override protected Session doReadSession(Serializable sessionId) {
        final Session session = tokenSessions.get(sessionId);
        return session != null ? session : super.doReadSession(sessionId);
    }

    @Override protected void uncache(Session session) {
        if (shouldCache(session)) super.uncache(session);
        else tokenSessions.remove(session.getId());
    }

    private boolean shouldCache(Session session) {
        // noinspection DuplicateStringLiteralInspection
        final Boolean cacheMark = session.getAttribute("cache") != null ? (Boolean) session.getAttribute("cache") : false;
        return !(session instanceof AppTokenSession) || cacheMark;
    }

    //~ Static Fields ................................................................................................................................

    protected static final int MAX_SIZE = 10000;
}
