
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.jetbrains.annotations.Nullable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import tekgenesis.authorization.shiro.AppTokenSession;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.LruCache;

import static tekgenesis.common.Predefined.notNull;

/**
 * Redis external storage for sessions.
 */
public class RedisSessionDAO extends AbstractSessionDAO {

    //~ Instance Fields ..............................................................................................................................

    private final JedisPool                       jedisPool;
    private final String                          prefix;
    private final LruCache<Serializable, Session> tokenSessions = LruCache.createLruCache(MAX_SIZE);

    //~ Constructors .................................................................................................................................

    /** Constructor with cluster props. */
    public RedisSessionDAO(SSOProps props) {
        final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(POOL_MAX);
        jedisPool = new JedisPool(config, URI.create(props.endPoint));
        prefix    = "shiro_session_" + ":";
    }

    //~ Methods ......................................................................................................................................

    @Override public void delete(Session session) {
        if (tokenSessions.remove(session.getId()) != null) return;

        try(final Jedis jedis = jedisPool.getResource()) {
            jedis.del(key(session.getId()));
        }
    }

    @Override public void update(Session session) {
        if (session instanceof AppTokenSession) {
            tokenSessions.put(session.getId(), session);
            return;
        }

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(final Jedis jedis = jedisPool.getResource();
            final ObjectOutputStream os = new ObjectOutputStream(byteArrayOutputStream))
        {
            os.writeObject(session);
            jedis.set(key(session.getId()), byteArrayOutputStream.toByteArray());
        }
        catch (final IOException e) {
            logger.error(e);
        }
    }

    @Override public Collection<Session> getActiveSessions() {
        try(final Jedis jedis = jedisPool.getResource()) {
            final List<Session> sessions = new ArrayList<>();
            for (final String key : notNull(jedis.keys(prefix + "*"))) {
                final Session s = deserializeSession(key.getBytes(), jedis);
                if (s != null) sessions.add(s);
            }
            sessions.addAll(tokenSessions.snapshot().values());
            return sessions;
        }
    }

    @Override protected Serializable doCreate(Session session) {
        final Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        update(session);
        return sessionId;
    }

    @Nullable @Override protected Session doReadSession(Serializable sessionId) {
        final Session session = tokenSessions.get(sessionId);
        if (session != null) return session;

        try(final Jedis resource = jedisPool.getResource()) {
            return deserializeSession(key(sessionId), resource);
        }
    }

    @Nullable private Session deserializeSession(byte[] key, Jedis jedis) {
        final byte[] content = jedis.get(key);
        if (content == null) return null;

        try(final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content))) {
            return (Session) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            logger.error(e);
            return null;
        }
    }

    private byte[] key(Serializable sessionId) {
        return (prefix + sessionId).getBytes();
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_SIZE = 10000;

    protected static final int POOL_MAX = 25;

    private static final Logger logger = Logger.getLogger(RedisSessionDAO.class);
}  // end class RedisSessionDAO
