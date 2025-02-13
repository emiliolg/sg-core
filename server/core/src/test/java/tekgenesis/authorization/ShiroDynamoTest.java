
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.io.Serializable;
import java.util.function.LongSupplier;

import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.jgroups.Address;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.authorization.shiro.sso.DynamoSessionDAO;
import tekgenesis.authorization.shiro.sso.SSOMode;
import tekgenesis.authorization.shiro.sso.SSOProps;
import tekgenesis.cache.database.infinispan.CacheManagerConfig.NoCluster;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.ClusterProps;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Times;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.persistence.StoreHandler;
import tekgenesis.persistence.sql.SqlStoreHandlerFactory;
import tekgenesis.security.shiro.ShiroSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "OverlyLongMethod" })
public class ShiroDynamoTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    public DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION) {
            public InfinispanCacheManager cacheManager = null;

            @Override protected void before() {
                createDatabase(dbName);
                final ClusterProps clusterProps = new ClusterProps();
                clusterProps.awsAccessKey = DynamoSessionDAO.TEST;
                clusterProps.clusterName  = "test";

                final SSOProps ssoProps = new SSOProps();
                ssoProps.mode = SSOMode.DYNAMO;
                env.put(ssoProps);
                env.put(clusterProps);
            }

            @Override protected StoreHandler.Factory createStoreHandlerFactory() {
                final ClusterManager<Address> cluster = new LocalClusterManager();
                Context.getContext().setSingleton(ClusterManager.class, cluster);
                cacheManager = new InfinispanCacheManager(database.getName(),
                        env,
                        getTransactionManager(),
                        new SqlStoreHandlerFactory(dbFactory),
                        cluster,
                        new NoCluster());
                Context.getContext().setSingleton(InfinispanCacheManager.class, cacheManager);
                return cacheManager;
            }
        };

    public ExternalResource resource = new ExternalResource() {
            @Override protected void before()
                throws Throwable
            {
                ShiroSession.clear();
            }

            @Override protected void after() {
                ShiroSession.clear();
                Context.getContext().unbind(InfinispanCacheManager.class);
            }
        };

    private final FormRule formRule = new FormRule();
    @Rule public TestRule  chain    = db.around(formRule).around(resource);

    //~ Methods ......................................................................................................................................

    @SuppressWarnings({ "ReuseOfLocalVariable", "NestedTryStatement" })
    @Test public void testShiroAuthentication() {
        final Session session = SecurityUtils.getSession();
        session.logout();

        assertThat(session.isAuthenticated()).isFalse();

        session.authenticate("admin", "password");
        Serializable sessionId = session.getId();

        assertThat(session.isAuthenticated()).isTrue();

        assertThat(formRule.shiroConfiguration.getSecurityManager().getSession(new DefaultSessionKey(sessionId))).isNotNull();
        org.apache.shiro.session.Session actual = formRule.shiroConfiguration.getSecurityManager().getSession(new DefaultSessionKey(sessionId));
        assertThat(actual).isNotNull();
        assertThat(actual.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)).isNotNull();
        assertThat(actual.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).isNotNull();
        Context.getSingleton(InfinispanCacheManager.class).getCache(Constants.SHIRO_SESSION_CACHE).clear();
        actual = formRule.shiroConfiguration.getSecurityManager().getSession(new DefaultSessionKey(sessionId));
        assertThat(actual).isNotNull();
        assertThat(org.apache.shiro.SecurityUtils.getSubject().isAuthenticated()).isTrue();
        assertThat(actual.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)).isNotNull();
        assertThat(actual.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).isNotNull();
        session.logout();
        try {
            formRule.shiroConfiguration.getSecurityManager().getSession(new DefaultSessionKey(sessionId));
            failBecauseExceptionWasNotThrown(UnknownSessionException.class);
        }
        catch (final UnknownSessionException ignore) {}

        session.authenticate("admin", "password");
        sessionId = session.getId();
        Context.getSingleton(InfinispanCacheManager.class).getCache(Constants.SHIRO_SESSION_CACHE).clear();
        final LongSupplier oldProvider = DateTime.setTimeSupplier(() -> timeInMillisShiftedBy(65));
        try {
            formRule.shiroConfiguration.getSecurityManager().getSession(new DefaultSessionKey(sessionId));
            failBecauseExceptionWasNotThrown(UnknownSessionException.class);
        }
        catch (final UnknownSessionException ignore) {}
        finally {
            DateTime.setTimeSupplier(oldProvider);
        }

        ShiroSession.clear();
        session.authenticate("admin", "password");
        sessionId = session.getId();
        try {
            DateTime.setTimeSupplier(() -> timeInMillisShiftedBy(35));
            org.apache.shiro.SecurityUtils.getSubject().getSession().touch();  // This updates the access time
            DateTime.setTimeSupplier(() -> timeInMillisShiftedBy(45));
            assertThat(formRule.shiroConfiguration.getSecurityManager().getSession(new DefaultSessionKey(sessionId))).isNotNull();
        }
        finally {
            DateTime.setTimeSupplier(oldProvider);
        }
    }                                                                          // end method testShiroAuthentication

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    public static long timeInMillisShiftedBy(final int minutes) {
        return System.currentTimeMillis() + minutes * Times.MILLIS_MINUTE;
    }
}  // end class ShiroDynamoTest
