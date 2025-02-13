
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.infinispan.Cache;

import tekgenesis.authorization.shiro.AppTokenSession;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.LruCache;
import tekgenesis.database.support.JdbcUtils;

/**
 * Abstract class for storing Session in an external source with local cache.
 */
public abstract class ExternalSessionDAO extends AbstractSessionDAO {

    //~ Instance Fields ..............................................................................................................................

    private Cache<Serializable, Session> cache          = null;
    private final int                    sessionTimeout;

    private final LruCache<Serializable, Session> tokenSessions = LruCache.createLruCache(MAX_SIZE);

    //~ Constructors .................................................................................................................................

    /** Constructor with cluster props. */
    protected ExternalSessionDAO(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    //~ Methods ......................................................................................................................................

    @Override public void delete(Session session) {
        if (session instanceof AppTokenSession) tokenSessions.remove(session.getId());
        else {
            getCache().remove(session.getId());
            deleteSession(session.getId().toString());
        }
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public void update(Session session)
        throws UnknownSessionException
    {
        if (session instanceof AppTokenSession) {
            tokenSessions.put(session.getId(), session);
            return;
        }
        if (session instanceof ValidatingSession) {
            if (((ValidatingSession) session).isValid()) {
                final String LAST_UPDATED    = "last_updated_external";
                final Object lastUpdatedAttr = session.getAttribute(LAST_UPDATED);

                if ((lastUpdatedAttr == null && session.getAttributeKeys().contains(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)) ||
                    (lastUpdatedAttr != null &&
                     DateTime.current().minutesFrom(DateTime.fromMilliseconds(new Long(lastUpdatedAttr.toString()))) > sessionTimeout / 60 * 2))
                {
                    session.setAttribute(LAST_UPDATED, JdbcUtils.toTimestamp(DateTime.current()).getTime());
                    try {
                        storeSession(session);
                    }
                    catch (final IOException e) {
                        logger.error(e);
                    }
                }
                getCache().put(session.getId(), session);
            }
            else delete(session);
        }
        else getCache().put(session.getId(), session);
    }

    @Override public Collection<Session> getActiveSessions() {
        return getCache().values();
    }

    /** Delete session. */
    protected abstract void deleteSession(String sessionId);

    @Override protected Serializable doCreate(Session session) {
        final Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        getCache().put(sessionId, session);
        try {
            storeSession(session);
        }
        catch (final IOException e) {
            logger.error(e);
        }
        return sessionId;
    }

    @Override protected Session doReadSession(Serializable sessionId) {
        Session session = tokenSessions.get(sessionId);
        if (session == null) {
            session = getCache().get(sessionId);
            if (session == null) {
                session = loadSession(sessionId);
                if (session != null) getCache().put(sessionId, session);
            }
        }
        return session;
    }

    protected abstract SessionData loadSessionData(Serializable sessionId);

    /** Store session. */
    protected void storeSession(Session session)
        throws IOException
    {
        final Map<String, Object> sessionAttributes = new HashMap<>();
        for (final Object s : session.getAttributeKeys())
            sessionAttributes.put(s.toString(), session.getAttribute(s));

        storeSession(session, new SessionData(Long.toString(session.getStartTimestamp().getTime()), session.getHost(), sessionAttributes));
    }

    protected abstract void storeSession(Session session, SessionData data)
        throws IOException;

    private Session loadSession(Serializable sessionId) {
        final String      id   = sessionId.toString();
        final SessionData data = loadSessionData(sessionId);
        if (data == null) {
            logger.warning("Unable to load session attributes for session " + id);
            return null;
        }

        final SimpleSession session = new SimpleSession();

        session.setId(id);
        final String createdAt      = data.getCreatedAt();
        final Date   startTimestamp = new Date(new Long(createdAt));
        if (DateTime.current().minutesFrom(DateTime.fromDate(startTimestamp)) > sessionTimeout / SIXTY * 2) {
            deleteSession(id);
            return null;
        }

        session.setStartTimestamp(startTimestamp);
        session.setLastAccessTime(new Date(JdbcUtils.toTimestamp(DateTime.current()).getTime()));
        session.setHost(data.getHost());

        final Map<String, Object> sessionAttributeMap = data.getAttributes();

        for (final String s : sessionAttributeMap.keySet())
            session.setAttribute(s, sessionAttributeMap.get(s));

        return session;
    }
    private Cache<Serializable, Session> getCache() {
        if (cache == null) cache = Context.getSingleton(InfinispanCacheManager.class).getCache(Constants.SHIRO_SESSION_CACHE);

        return cache;
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_SIZE = 10000;

    protected static final int SIXTY = 60;

    private static final Logger logger = Logger.getLogger(ExternalSessionDAO.class);
}  // end class ExternalSessionDAO
